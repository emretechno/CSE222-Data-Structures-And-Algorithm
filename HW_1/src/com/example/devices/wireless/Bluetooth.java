package com.example.devices.wireless;

import com.example.devices.State;
import com.example.protocols.Protocol;

// Bluetooth wireless, uses UART protocol.
public class Bluetooth extends WirelessIO {

    // Constructor: sets up the protocol.
    public Bluetooth(Protocol protocol) {
        super(protocol);
        if (!protocol.getProtocolName().equals("UART")) {
            throw new IllegalArgumentException(getName()+"supports only UART protocols");
        }
    }

    // Turns the Bluetooth ON.
    @Override
    public void turnON() {
        if (state == State.OFF) {
            System.out.println(getName() + ": Turning ON");
            protocol.write("turnON");
            state = State.ON;
        } else {
            System.out.println(getName() + " is already ON");
        }
    }

    // Turns the Bluetooth OFF.
    @Override
    public void turnOFF() {
        if (state == State.ON) {
            System.out.println(getName() + ": Turning OFF");
            protocol.write("turnOFF");
            state = State.OFF;
        } else {
            System.out.println(getName() + " is already OFF");
        }
    }

    // Returns the  name.
    @Override
    public String getName() {
        return "Bluetooth";
    }

    // Sends data via Bluetooth.
    @Override
    public void sendData(String data) {
        protocol.write("Wireless sending: " + data);
    }

    // Receives data via Bluetooth.
    @Override
    public String recvData() {
        return protocol.read();
    }
}
