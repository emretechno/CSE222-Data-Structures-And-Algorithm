package com.example.devices.sensors;

import com.example.protocols.Protocol;

// Abstract class for temperature sensors.
public abstract class TempSensor extends Sensor {

    public TempSensor(Protocol protocol) {
        super(protocol);
    }

    // Returns a random temperature after simulating a protocol read.
    public Float getTemp() {
        System.out.println(protocol.read());
        return (float) (Math.random() * 100.0);
    }

    // Returns sensor type as "TempSensor"
    @Override
    public String getSensType() {
        return "TempSensor";
    }

    // Returns sensor data as a formatted temperature string.
    @Override
    public String data2String() {
        return String.format("Temp: %.2fC", getTemp());
    }
}
