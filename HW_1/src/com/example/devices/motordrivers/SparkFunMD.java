package com.example.devices.motordrivers;

import com.example.devices.State;
import com.example.protocols.Protocol;

/**
 * SparkFunMD Motor Driver class.
 * This device uses the SPI protocol.
 * It provides its own implementations for turning on/off and setting motor speed.
 */
public class SparkFunMD extends MotorDriver {

    public SparkFunMD(Protocol protocol) {
        super(protocol);
        if (!protocol.getProtocolName().equals("SPI")) {
            throw new IllegalArgumentException(getName() + " supports only SPI protocol");
        }
    }

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

    @Override
    public String getName() {
        return "SparkFunMD";
    }

    @Override
    public void setMotorSpeed(int speed) {
        System.out.println(getName() + " Setting Motor Speed to " + speed);
        protocol.write("setMotorSpeed " + speed);
    }
}
