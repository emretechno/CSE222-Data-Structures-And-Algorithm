package com.example.devices.displays;

import com.example.devices.State;
import com.example.protocols.Protocol;

/**
 * OLED class.
 * Implements the abstract Display methods for an OLED display.
 */
public class OLED extends Display {

    public OLED(Protocol protocol) {
        super(protocol);
        if (!protocol.getProtocolName().equals("SPI")) {
            throw new IllegalArgumentException(getName() + " supports only SPI protocol");
        }
    }

    @Override
    public void turnON() {
        if (state == State.OFF) {
            // Turn the OLED on
            System.out.println(getName() + ": Turning ON");
            protocol.write("turnON");
            state = State.ON;
        } else {
            System.out.println(getName() + " is already ON");
        }
    }

    @Override
    public void turnOFF() {
        if (state == State.ON) {
            // Turn the OLED off
            System.out.println(getName() + ": Turning OFF");
            protocol.write("turnOFF");
            state = State.OFF;
        } else {
            System.out.println(getName() + " is already OFF");
        }
    }

    @Override
    public String getName() {
        return "OLED";
    }

    @Override
    public void printData(String data) {
        // OLED prints the message using its protocol
        protocol.write("Display printing: " + data);
    }
}
