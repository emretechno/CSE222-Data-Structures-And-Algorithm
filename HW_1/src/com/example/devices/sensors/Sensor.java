package com.example.devices.sensors;

import com.example.devices.Device;
import com.example.protocols.Protocol;

// Abstract base class for all sensors.
public abstract class Sensor extends Device {

    public Sensor(Protocol protocol) {
        super(protocol);
    }

    public abstract String getSensType();


    public abstract String data2String();

    @Override
    public String getDevType() {
        return getSensType() + " Sensor";
    }

    // Maps getDataString() call from HWSystem to data2String().
    @Override
    public String getDataString() {
        return data2String();
    }
}
