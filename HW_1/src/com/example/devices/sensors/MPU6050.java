package com.example.devices.sensors;

import com.example.devices.State;
import com.example.protocols.Protocol;

// MPU6050 is an IMU sensor that only supports I2C.
public class MPU6050 extends IMUSensor {

    public MPU6050(Protocol protocol) {
        super(protocol);
        if (!protocol.getProtocolName().equals("I2C")) {
            throw new IllegalArgumentException(getName()+"supports only I2C protocols");
        }
    }

    // Turns the sensor ON.
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

    // Turns the sensor OFF.
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
        return "MPU6050";
    }
}
