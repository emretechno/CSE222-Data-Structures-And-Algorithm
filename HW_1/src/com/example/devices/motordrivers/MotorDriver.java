package com.example.devices.motordrivers;

import com.example.devices.Device;
import com.example.protocols.Protocol;

/**
 * Abstract MotorDriver class.
 * It extends Device and introduces the setMotorSpeed() method.
 * The HWSystem calls setSpeed(), which is forwarded to setMotorSpeed() in subclasses ( PCA9685, SparkFunMD)
 */
public abstract class MotorDriver extends Device {

    public MotorDriver(Protocol protocol) {
        super(protocol);
    }

    @Override
    public String getDevType() {
        // All motor drivers return "MotorDriver" as their device type.
        return "MotorDriver";
    }

    // Subclasses are implementing the actual motor speed setting.
    public abstract void setMotorSpeed(int speed);

    // HWSystem calls setSpeed(), so we simply forward it. It is done to prevent downcasting in hwsystem.
    @Override
    public void setSpeed(int speed) {
        setMotorSpeed(speed);
    }
}
