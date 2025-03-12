package com.example.protocols;

/**
 * Protocol interface defines the basic operations for a communication protocol.
 * It provides methods to get the protocol name, simulate reading data, and simulate writing data.
 */
public interface Protocol {
    String getProtocolName();

    // Simulates reading data from the port.
    // Returns a string  "<protocol>: Reading."
    String read();

    // Simulates writing data to the port.
    // Prints a message  "<protocol>: Writing "<data>""
    void write(String data);
}
