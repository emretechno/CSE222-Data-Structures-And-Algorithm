package com.example.devices.wireless;

import com.example.devices.Device;
import com.example.protocols.Protocol;

/**
 * WirelessIO is a type of Device for wireless communication.
 * It declares abstract methods for sending and receiving data.
 * The HWSystem calls wirelessSend() and wirelessReceive(), which we map to these methods.
 */
public abstract class WirelessIO extends Device {

    // Constructor that passes the protocol to the Device constructor
    public WirelessIO(Protocol protocol) {
        super(protocol);
    }

    // Returns "WirelessIO" as the device type
    @Override
    public String getDevType() {
        return "WirelessIO";
    }

    // Abstract method: Child classes implement how to send data wirelessly.
    public abstract void sendData(String data);

    // Abstract method: Child classes implement how to receive data wirelessly.
    public abstract String recvData();

    // HWSystem calls this method to send wireless data.
    // We just delegate the call to sendData().
    @Override
    public void wirelessSend(String msg) {
        sendData(msg);
    }

    // HWSystem calls this method to receive wireless data.
    // We delegate the call to recvData().
    @Override
    public String wirelessReceive() {
        return recvData();
    }
}
