package com.example.devices.sensors;

import com.example.devices.State;
import com.example.protocols.Protocol;

// BME280 is a temperature sensor that supports I2C or SPI protocols.
public class BME280 extends TempSensor {

    public BME280(Protocol protocol) {
        super(protocol);
        if (!protocol.getProtocolName().equals("I2C") && !protocol.getProtocolName().equals("SPI")) {
            throw new IllegalArgumentException(getName() + " supports only I2C or SPI protocol");
        }
    }

    // Turns the sensor ON. Writes "turnON" to the protocol.
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

    // Turns the sensor OFF. Writes "turnOFF" to the protocol.
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

    // Returns the model name.
    @Override
    public String getName() {
        return "BME280";
    }
}
