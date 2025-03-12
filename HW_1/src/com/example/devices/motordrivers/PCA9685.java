package com.example.devices.motordrivers;

import com.example.devices.State;
import com.example.protocols.Protocol;

/**
 * PCA9685 Motor Driver class.
 * This device requires the I2C protocol.
 */
public class PCA9685 extends MotorDriver {

    public PCA9685(Protocol protocol) {
        super(protocol);
        // Check protocol compatibility at construction time.
        if (!protocol.getProtocolName().equals("I2C")) {
            throw new IllegalArgumentException(getName() + " supports only I2C protocol");
        }
    }

    @Override
    public void setMotorSpeed(int speed) {
        System.out.println(getName() + " Setting Motor Speed to " + speed);
        protocol.write("setMotorSpeed " + speed);
    }

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

    @Override
    public String getName() {
        return "PCA9685";
    }
}
