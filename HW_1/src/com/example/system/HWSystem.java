package com.example.system;

import com.example.devices.*;
import com.example.protocols.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class HWSystem {

    // Each Protocol represents one port.
    private final ArrayList<Protocol> protocols;
    // attachedDevices holds the Device on each port (null if none)
    private final ArrayList<Device> attachedDevices;
    // All added devices are stored in this list
    private final ArrayList<Device> devices;

    // Counters to track unique device IDs per category
    private int sensorCount;
    private int displayCount;
    private int wirelessCount;
    private int motorCount;

    // Limits read from the configuration file
    private int maxSensors;
    private int maxDisplays;
    private int maxWireless;
    private int maxMotors;

    public HWSystem() {
        protocols = new ArrayList<>();
        attachedDevices = new ArrayList<>();
        devices = new ArrayList<>();
        sensorCount = 0;
        displayCount = 0;
        wirelessCount = 0;
        motorCount = 0;
    }

    public void loadConfig(String filePath) {
        try {
            Scanner sc = new Scanner(new File(filePath));
            // Read the protocol configuration line
            String line1 = sc.nextLine().trim();
            if (line1.startsWith("Port Configuration:")) {
                line1 = line1.substring("Port Configuration:".length()).trim();
            }
            String[] protoList = line1.split(",");
            for (int i = 0; i < protoList.length; i++) {
                String protoName = protoList[i].trim();
                Protocol p = createProtocol(protoName, i);
                if (p != null) {
                    protocols.add(p);
                    attachedDevices.add(null); // Initialize port as empty
                }
            }
            // Read limits for each device type
            maxSensors = parseLimit(sc.nextLine().trim());
            maxDisplays = parseLimit(sc.nextLine().trim());
            maxWireless = parseLimit(sc.nextLine().trim());
            maxMotors = parseLimit(sc.nextLine().trim());
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading config: " + e.getMessage());
        }
    }

    // Helper method to parse lines like "# of sensors: 1"
    private int parseLimit(String line) {
        String[] parts = line.split(":");
        try {
            return Integer.parseInt(parts[1].trim());
        } catch (Exception e) {
            System.out.println("Error parsing limit from: " + line);
            return 0;
        }
    }

    // Create a Protocol instance from its name and port index.
    private Protocol createProtocol(String protoName, int portID) {
        String portLabel = "Port" + portID;
        if (protoName.equalsIgnoreCase("I2C")) {
            return new I2C(portLabel);
        } else if (protoName.equalsIgnoreCase("SPI")) {
            return new SPI(portLabel);
        } else if (protoName.equalsIgnoreCase("UART")) {
            return new UART(portLabel);
        } else if (protoName.equalsIgnoreCase("OneWire")) {
            return new OneWire(portLabel);
        } else {
            System.out.println("Error: Unknown protocol: " + protoName);
            return null;
        }
    }

    // ------------------ Command Processing ------------------ //

    public void processCommand(String cmdLine) {
        String[] parts = cmdLine.split("\\s+");
        if (parts.length == 0) return;
        String cmd = parts[0];
        //System.out.println("Command: " + cmdLine);
        try {
            switch (cmd) {
                case "turnON":
                case "turnOFF":
                    handleTurnOnOff(cmd, parts);
                    break;
                case "addDev":
                    handleAddDev(parts);
                    break;
                case "rmDev":
                    handleRmDev(parts);
                    break;
                case "list":
                    handleList(parts);
                    break;
                case "readSensor":
                    handleReadSensor(parts);
                    break;
                case "printDisplay":
                    handlePrintDisplay(parts);
                    break;
                case "readWireless":
                    handleReadWireless(parts);
                    break;
                case "writeWireless":
                    handleWriteWireless(parts);
                    break;
                case "setMotorSpeed":
                    handleSetMotorSpeed(parts);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Error: Unknown command " + cmd);
            }
        } catch (Exception e) {
            System.out.println("Command error: " + e.getMessage());
        }
    }

    // Turns a device ON or OFF by portID.
    private void handleTurnOnOff(String cmd, String[] parts) {
        if (parts.length < 2) {
            System.out.println("Error: missing portID");
            return;
        }
        int pID = Integer.parseInt(parts[1]);
        if (pID < 0 || pID >= protocols.size()) {
            System.out.println("Error: Invalid port " + pID);
            return;
        }
        Device d = attachedDevices.get(pID);
        if (d == null) {
            System.out.println("Error: No device at port " + pID);
            return;
        }
        if (cmd.equals("turnON"))
            d.turnON();
        else
            d.turnOFF();
    }

    // Adds a device using the command: addDev <devName> <portID> <devID>
    private void handleAddDev(String[] parts) {
        if (parts.length < 4) {
            System.out.println("Error: addDev requires 3 arguments");
            return;
        }
        String devName = parts[1];
        int pID = Integer.parseInt(parts[2]);
        int providedDevID;
        try {
            providedDevID = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            System.out.println("Error: devID must be an integer");
            return;
        }
        if (pID < 0 || pID >= protocols.size()) {
            System.out.println("Error: Invalid port " + pID);
            return;
        }
        if (attachedDevices.get(pID) != null) {
            System.out.println("Error: Port " + pID + " is already occupied");
            return;
        }
        Device d = createDevice(devName, protocols.get(pID));
        if (d == null) return; // error already printed

        // Set the device ID based on its category.
        String devType = d.getDevType();
        if (devType.contains("Sensor")) {
            if (providedDevID != sensorCount) {
                System.out.println("Error: Sensor devID " + providedDevID + " is already used or invalid");
                return;
            }
            if (sensorCount >= maxSensors) {
                System.out.println("Error: Sensor limit exceeded");
                return;
            }
            d.setDevID(sensorCount);
            sensorCount++;
        } else if (devType.equals("Display")) {
            if (providedDevID != displayCount) {
                System.out.println("Error: Display devID " + providedDevID + " is already used or invalid");
                return;
            }
            if (displayCount >= maxDisplays) {
                System.out.println("Error: Display limit exceeded");
                return;
            }
            d.setDevID(displayCount);
            displayCount++;
        } else if (devType.equals("WirelessIO")) {
            if (providedDevID != wirelessCount) {
                System.out.println("Error: Wireless devID " + providedDevID + " is already used or invalid");
                return;
            }
            if (wirelessCount >= maxWireless) {
                System.out.println("Error: Wireless adapter limit exceeded");
                return;
            }
            d.setDevID(wirelessCount);
            wirelessCount++;
        } else if (devType.equals("MotorDriver")) {
            if (providedDevID != motorCount) {
                System.out.println("Error: MotorDriver devID " + providedDevID + " is already used or invalid");
                return;
            }
            if (motorCount >= maxMotors) {
                System.out.println("Error: MotorDriver limit exceeded");
                return;
            }
            d.setDevID(motorCount);
            motorCount++;
        }
        devices.add(d);
        attachedDevices.set(pID, d);
        System.out.println("Device " + d.getName() + " added at port " + pID + " (devID=" + providedDevID + ")");
    }

    // Removes a device using the command: rmDev <portID>
    private void handleRmDev(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Error: missing portID");
            return;
        }
        int pID = Integer.parseInt(parts[1]);
        if (pID < 0 || pID >= protocols.size()) {
            System.out.println("Error: Invalid port " + pID);
            return;
        }
        Device d = attachedDevices.get(pID);
        if (d == null) {
            System.out.println("Error: No device at port " + pID);
            return;
        }
        if (d.getState() == State.ON) {
            System.out.println("Device is active.");
            System.out.println("Command failed.");
            return;
        }
        devices.remove(d);
        attachedDevices.set(pID, null);
        System.out.println(d.getName() + " removed from port");
    }

    // Processes list commands (ports, sensors, displays, wireless, motordrivers)
    private void handleList(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Error: missing list target");
            return;
        }
        String target = parts[1];
        if (target.equalsIgnoreCase("ports")) {
            listPorts();
        } else if (target.equalsIgnoreCase("sensors") || target.equalsIgnoreCase("sensor")) {
            listSensors();
        } else if (target.equalsIgnoreCase("displays")) {
            listDisplays();
        } else if (target.equalsIgnoreCase("wireless")) {
            listWireless();
        } else if (target.equalsIgnoreCase("motordrivers")) {
            listMotors();
        } else {
            System.out.println("Error: unknown list target " + target);
        }
    }

    // Lists all ports and their statuses.
    private void listPorts() {
        System.out.println("List of ports:");
        for (int i = 0; i < protocols.size(); i++) {
            Protocol p = protocols.get(i);
            Device d = attachedDevices.get(i);
            if (d == null) {
                System.out.println(i + " " + p.getProtocolName() + " empty");
            } else {
                System.out.println(i + " " + p.getProtocolName() + " occupied "
                        + d.getName() + " " + d.getDevType() + " "
                        + d.getDevID() + " " + d.getState());
            }
        }
    }

    // Lists sensors with their details.
    private void listSensors() {
        System.out.println("List of Sensors:");
        for (Device d : devices) {
            if (d.getDevType().contains("Sensor")) {
                int portIndex = findPortIndex(d);
                Protocol p = protocols.get(portIndex);
                System.out.println(d.getName() + " " + d.getDevID() + " "
                        + portIndex + " " + p.getProtocolName());
            }
        }
    }

    // Lists displays with their details.
    private void listDisplays() {
        System.out.println("List of Displays:");
        for (Device d : devices) {
            if (d.getDevType().equals("Display")) {
                int portIndex = findPortIndex(d);
                Protocol p = protocols.get(portIndex);
                System.out.println(d.getName() + " " + d.getDevID() + " "
                        + portIndex + " " + p.getProtocolName());
            }
        }
    }

    // Lists wireless devices with their details.
    private void listWireless() {
        System.out.println("List of WirelessIO:");
        for (Device d : devices) {
            if (d.getDevType().equals("WirelessIO")) {
                int portIndex = findPortIndex(d);
                Protocol p = protocols.get(portIndex);
                System.out.println(d.getName() + " " + d.getDevID() + " "
                        + portIndex + " " + p.getProtocolName());
            }
        }
    }

    // Lists motor drivers with their details.
    private void listMotors() {
        System.out.println("List of MotorDrivers:");
        for (Device d : devices) {
            if (d.getDevType().equals("MotorDriver")) {
                int portIndex = findPortIndex(d);
                Protocol p = protocols.get(portIndex);
                System.out.println(d.getName() + " " + d.getDevID() + " "
                        + portIndex + " " + p.getProtocolName());
            }
        }
    }

    // Finds a device by a type substring and its devID.
    private Device findDeviceByTypeAndID(String typeSubstring, int devID) {
        for (Device d : devices) {
            if (d.getDevType().contains(typeSubstring) && d.getDevID() == devID) {
                return d;
            }
        }
        return null;
    }

    // Finds the port index where a device is attached.
    private int findPortIndex(Device d) {
        for (int i = 0; i < attachedDevices.size(); i++) {
            if (attachedDevices.get(i) == d) {
                return i;
            }
        }
        return -1;
    }

    // ------------------ Command-Specific Operations ------------------ //

    // Handle readSensor <devID>
    private void handleReadSensor(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Error: missing devID");
            return;
        }
        int devID = Integer.parseInt(parts[1]);
        Device d = findDeviceByTypeAndID("Sensor", devID);
        if (d == null) {
            System.out.println("Error: Sensor " + devID + " not found");
            return;
        }
        if (d.getState() == State.OFF) {
            System.out.println("Device is not active.");
            System.out.println("Command failed.");
            return;
        }
        System.out.println(d.getName() + " " + d.getDevType() + ": " + d.getDataString());
    }

    // Handle printDisplay <devID> <String>
    private void handlePrintDisplay(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Error: printDisplay requires devID and a message string");
            return;
        }
        int devID = Integer.parseInt(parts[1]);
        Device d = findDeviceByTypeAndID("Display", devID);
        if (d == null) {
            System.out.println("Error: Display " + devID + " not found");
            return;
        }
        if (d.getState() == State.OFF) {
            System.out.println("Error: Display " + d.getName() + " is OFF");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            sb.append(parts[i]).append(" ");
        }
        d.displayMessage(sb.toString().trim());
    }

    // Handle readWireless <devID>
    private void handleReadWireless(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Error: missing devID");
            return;
        }
        int devID = Integer.parseInt(parts[1]);
        Device d = findDeviceByTypeAndID("WirelessIO", devID);
        if (d == null) {
            System.out.println("Error: Wireless device " + devID + " not found");
            return;
        }
        if (d.getState() == State.OFF) {
            System.out.println("Device is not active.");
            System.out.println("Command failed.");
            return;
        }
        System.out.println("Wireless read: " + d.wirelessReceive());
    }

    // Handle writeWireless <devID> <String>
    private void handleWriteWireless(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Error: writeWireless requires devID and a message string");
            return;
        }
        int devID = Integer.parseInt(parts[1]);
        Device d = findDeviceByTypeAndID("WirelessIO", devID);
        if (d == null) {
            System.out.println("Error: Wireless device " + devID + " not found");
            return;
        }
        if (d.getState() == State.OFF) {
            System.out.println("Device is not active.");
            System.out.println("Command failed.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            sb.append(parts[i]).append(" ");
        }
        d.wirelessSend(sb.toString().trim());
    }

    // Handle setMotorSpeed <devID> <int>
    private void handleSetMotorSpeed(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Error: setMotorSpeed requires devID and a speed value");
            return;
        }
        int devID = Integer.parseInt(parts[1]);
        Device d = findDeviceByTypeAndID("MotorDriver", devID);
        if (d == null) {
            System.out.println("Error: MotorDriver " + devID + " not found");
            return;
        }
        if (d.getState() == State.OFF) {
            System.out.println("Device is not active.");
            System.out.println("Command failed.");
            return;
        }
        int speed = Integer.parseInt(parts[2]);
        d.setSpeed(speed);
    }


    // Runs commands interactively from the console.
    public void runInteractive() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter commands (write 'exit' to quit):");
        System.out.print("> ");
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) {
                processCommand(line);
            }
            System.out.print("> ");
        }
        sc.close();
    }

    // Runs commands from a given file
    public void runFromFile(String commandFilePath) {
        try {
            Scanner sc = new Scanner(new File(commandFilePath));
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) {
                    System.out.println("Command: " + line);
                    processCommand(line);
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error reading commands: " + e.getMessage());
        }
    }

    // ------------------ Create Device ------------------ //

    // Creates a device instance based on its name and protocol.
    private Device createDevice(String devName, Protocol protocol) {
        String pName = protocol.getProtocolName();
        switch (devName) {
            case "DHT11":
                if (!pName.equals("OneWire")) {
                    System.out.println("Error: DHT11 requires OneWire");
                    return null;
                }
                return new com.example.devices.sensors.DHT11(protocol);
            case "BME280":
                if (!pName.equals("I2C") && !pName.equals("SPI")) {
                    System.out.println("Error: BME280 requires I2C or SPI");
                    return null;
                }
                return new com.example.devices.sensors.BME280(protocol);
            case "MPU6050":
                if (!pName.equals("I2C")) {
                    System.out.println("Error: MPU6050 requires I2C");
                    return null;
                }
                return new com.example.devices.sensors.MPU6050(protocol);
            case "GY951":
                if (!pName.equals("SPI") && !pName.equals("UART")) {
                    System.out.println("Error: GY951 requires SPI or UART");
                    return null;
                }
                return new com.example.devices.sensors.GY951(protocol);
            case "LCD":
                if (!pName.equals("I2C")) {
                    System.out.println("Error: LCD requires I2C");
                    return null;
                }
                return new com.example.devices.displays.LCD(protocol);
            case "OLED":
                if (!pName.equals("SPI")) {
                    System.out.println("Error: OLED requires SPI");
                    return null;
                }
                return new com.example.devices.displays.OLED(protocol);
            case "Bluetooth":
                if (!pName.equals("UART")) {
                    System.out.println("Error: Bluetooth requires UART");
                    return null;
                }
                return new com.example.devices.wireless.Bluetooth(protocol);
            case "Wifi":
                if (!pName.equals("SPI") && !pName.equals("UART")) {
                    System.out.println("Error: Wifi requires SPI or UART");
                    return null;
                }
                return new com.example.devices.wireless.Wifi(protocol);
            case "PCA9685":
                if (!pName.equals("I2C")) {
                    System.out.println("Error: PCA9685 requires I2C");
                    return null;
                }
                return new com.example.devices.motordrivers.PCA9685(protocol);
            case "SparkFunMD":
                if (!pName.equals("SPI")) {
                    System.out.println("Error: SparkFunMD requires SPI");
                    return null;
                }
                return new com.example.devices.motordrivers.SparkFunMD(protocol);
            default:
                System.out.println("Error: Unknown device name " + devName);
                return null;
        }
    }
}