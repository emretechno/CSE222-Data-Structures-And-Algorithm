package com.example.protocols;

// UART protocol implementation.
public class UART implements Protocol {
    private final String portName; // Port name (not used in printed output)

    // Setting the port name
    public UART(String portName) {
        this.portName = portName;
    }

    // Returns UART as protocol name
    @Override
    public String getProtocolName() {
        return "UART";
    }

    // Simulates reading data
    @Override
    public String read() {
        return getProtocolName() + ": Reading.";
    }

    // Simulates writing data
    @Override
    public void write(String data) {
        System.out.println(getProtocolName() + ": Writing \"" + data + "\"");
    }
}
