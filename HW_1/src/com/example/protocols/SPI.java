package com.example.protocols;

// SPI protocol implementation.
public class SPI implements Protocol {
    private final String portName; // Port name (not used in output)

    // Constructor initializes port name
    public SPI(String portName) {
        this.portName = portName;
    }

    // Returns "SPI" as protocol name
    @Override
    public String getProtocolName() {
        return "SPI";
    }

    // Simulates a read operation
    @Override
    public String read() {
        return getProtocolName() + ": Reading.";
    }

    // Simulates a write operation
    @Override
    public void write(String data) {
        System.out.println(getProtocolName() + ": Writing \"" + data + "\"");
    }
}
