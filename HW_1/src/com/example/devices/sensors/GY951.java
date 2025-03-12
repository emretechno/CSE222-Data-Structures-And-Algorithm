package com.example.devices.sensors;

import com.example.devices.State;
import com.example.protocols.Protocol;

// GY951 is an IMU sensor that supports either SPI or UART.
public class GY951 extends IMUSensor {

    public GY951(Protocol protocol) {
        super(protocol);
        // Check protocol compatibility for GY951.
        if (!protocol.getProtocolName().equals("SPI") && !protocol.getProtocolName().equals("UART")) {
            throw new IllegalArgumentException(getName() + " supports only SPI or UART protocol");
        }
    }

    // Turns the sensor ON.
    @Override
    public void turnON() {
        if (state != State.ON) {
            state = State.ON;
            System.out.println(getName() + " Turning ON");
            protocol.write("turnON");
        } else {
            protocol.write(getName() + " is already ON");
        }
    }

    // Turns the sensor OFF.
    @Override
    public void turnOFF() {
        if (state != State.OFF) {
            state = State.OFF;
            System.out.println(getName() + " Turning OFF");
            protocol.write("turnOFF");
        } else {
            protocol.write(getName() + " is already OFF");
        }
    }

    // Returns the model name.
    @Override
    public String getName() {
        return "GY-951";
    }
}
