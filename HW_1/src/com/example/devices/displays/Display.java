package com.example.devices.displays;

import com.example.devices.Device;
import com.example.protocols.Protocol;

/**
 * Abstract Display class.
 * Extends Device and adds a method for printing data.
 */
public abstract class Display extends Device {

    public Display(Protocol protocol) {
        super(protocol);
    }

    @Override
    public String getDevType() {
        // All displays return "Display" as their device type.
        return "Display";
    }

    // these will be implemented by child classes (lcd , oled)
    public abstract void printData(String data);

    // Map displayMessage() to printData() for HWSystem call.
    @Override
    public void displayMessage(String msg) {
        printData(msg);
    }
}
