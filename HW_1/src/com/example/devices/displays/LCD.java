package com.example.devices.displays;

import com.example.devices.State;
import com.example.protocols.Protocol;

/**
 * LCD class.
 * Implements the abstract Display methods for an LCD display.
 */
public class LCD extends Display {

    public LCD(Protocol protocol) {
        super(protocol);
        if (!protocol.getProtocolName().equals("I2C")) {
            throw new IllegalArgumentException(getName() + " supports only I2C protocol");
        }
    }

    @Override
    public void turnON() {
        if (state == State.OFF) {
            // Turn the LCD on
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
            // Turn the LCD off
            System.out.println(getName() + ": Turning OFF");
            protocol.write("turnOFF");
            state = State.OFF;
        } else {
            System.out.println(getName() + " is already OFF");
        }
    }

    @Override
    public String getName() {
        return "LCD";
    }

    @Override
    public void printData(String data) {
        // Print the message to the LCD using the protocol's write
        protocol.write("Display printing: " + data);
    }
}
