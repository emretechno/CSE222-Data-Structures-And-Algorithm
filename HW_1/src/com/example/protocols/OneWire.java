package com.example.protocols;

// OneWire protocol implementation.
public class OneWire implements Protocol {
    private final String portName; // Port name (not used in printed output)

    // Constructor initializes port name
    public OneWire(String portName) {
        this.portName = portName;
    }

    // Returns "OneWire" as protocol name
    @Override
    public String getProtocolName() {
        return "OneWire";
    }

    // Simulates reading data from the port
    @Override
    public String read() {
        return getProtocolName() + ": Reading.";
    }

    // Simulates writing data to the port
    @Override
    public void write(String data) {
        System.out.println(getProtocolName() + ": Writing \"" + data + "\"");
    }
}
