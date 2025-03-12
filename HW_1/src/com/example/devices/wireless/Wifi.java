package com.example.devices.wireless;

import com.example.devices.State;
import com.example.protocols.Protocol;

// Wifi wireless , supports SPI or UART protocols.
public class Wifi extends WirelessIO {

    // Constructor: initializes with the given protocol.
    public Wifi(Protocol protocol) {
        super(protocol);
        if (!protocol.getProtocolName().equals("SPI") && !protocol.getProtocolName().equals("UART")) {
            throw new IllegalArgumentException(getName() + " supports only SPI or UART protocol");
        }
    }

    // Turns the Wifi ON.
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

    // Turns the Wifi OFF.
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

    // Returns the name.
    @Override
    public String getName() {
        return "Wifi";
    }

    // Sends data via Wifi.
    @Override
    public void sendData(String data) {
        protocol.write("Wireless sending: " + data);
    }

    // Receives data via Wifi.
    @Override
    public String recvData() {
        return protocol.read();
    }
}
