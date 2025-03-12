package com.example.protocols;

// I2C protocol implementation.
public class I2C implements Protocol {
    private final String portName; // Port name (stored but not shown in output)

    // Constructor: sets the port name
    public I2C(String portName) {
        this.portName = portName;
    }

    // Returns the protocol name "I2C"
    @Override
    public String getProtocolName() {
        return "I2C";
    }

    // Simulates a read operation
    @Override
    public String read() {
        return getProtocolName() + ": Reading.";
    }

    // Simulates a write operation by printing the message
    @Override
    public void write(String data) {
        System.out.println(getProtocolName() + ": Writing \"" + data + "\"");
    }
}
