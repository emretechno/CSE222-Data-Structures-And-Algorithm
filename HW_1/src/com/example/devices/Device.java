package com.example.devices;
import com.example.protocols.Protocol;
/**
 * Abstract Device class.
 * It defines common fields and methods for all devices.
 * If a device doesn't support an operation (like wirelessSend), the default method throws an exception.
 */

public abstract class Device {
    // Communication protocol
    protected Protocol protocol;
    // Current state: ON or OFF
    protected State state;
    // Unique device ID assigned by HWSystem
    protected int devID;

    public Device(Protocol protocol) {
        this.protocol = protocol;
        this.state = State.OFF;
        this.devID = -1; // not assigned yet
    }

    // HWSystem uses these to track the device ID.
    public int getDevID() {
        return this.devID;
    }

    public void setDevID(int id) {
        this.devID = id;
    }

    // Subclasses are implementing these methods
    public abstract void turnON();
    public abstract void turnOFF();
    public abstract String getName();
    public abstract String getDevType();

    public State getState() {
        return this.state;
    }

    // If not supported by the device, throw exception. These methods will be overridden by specific device classes.
    // For example, the WirelessIO class overrides wirelessSend() and wirelessReceive().
    //These were added because everything will be controlled from Device class and the specific device classes will only implement the methods.
    public String getDataString() {
        throw new UnsupportedOperationException("getDataString() not supported by " + getName());
    }

    public void displayMessage(String msg) {
        throw new UnsupportedOperationException("displayMessage() not supported by " + getName());
    }

    public String wirelessReceive() {
        throw new UnsupportedOperationException("wirelessReceive() not supported by " + getName());
    }

    public void wirelessSend(String msg) {
        throw new UnsupportedOperationException("wirelessSend() not supported by " + getName());
    }

    public void setSpeed(int speed) {
        throw new UnsupportedOperationException("setSpeed() not supported by " + getName());
    }
}
