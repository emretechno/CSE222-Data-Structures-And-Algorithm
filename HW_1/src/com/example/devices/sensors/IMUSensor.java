package com.example.devices.sensors;

import com.example.protocols.Protocol;

// Abstract class for IMU sensors.
// Provides methods to get acceleration and rotation data.
public abstract class IMUSensor extends Sensor {

    public IMUSensor(Protocol protocol) {
        super(protocol);
    }

    // Simulate reading acceleration value.
    public Float getAccel() {
        System.out.println(protocol.read());
        return (float) (Math.random() * 10.0);
    }

    // Simulate reading rotation value.
    public Float getRot() {
        System.out.println(protocol.read());
        return (float) (Math.random() * 360.0);
    }

    // Returns the sensor type.
    @Override
    public String getSensType() {
        return "IMUSensor";
    }

    // Returns sensor data as a formatted string.
    @Override
    public String data2String() {
        float a = getAccel();
        float r = getRot();
        return String.format("Accel: %.2f, Rot: %.2f", a, r);
    }
}
