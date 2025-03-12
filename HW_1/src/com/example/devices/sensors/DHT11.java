package com.example.devices.sensors;

import com.example.devices.State;
import com.example.protocols.Protocol;

// DHT11 is a temperature sensor that requires the OneWire protocol.
public class DHT11 extends TempSensor {

    public DHT11(Protocol protocol) {
        super(protocol);
        if (!protocol.getProtocolName().equals("OneWire")) {
            throw new IllegalArgumentException(getName()+" supports only OneWire protocols");
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
        return "DHT11";
    }
}
