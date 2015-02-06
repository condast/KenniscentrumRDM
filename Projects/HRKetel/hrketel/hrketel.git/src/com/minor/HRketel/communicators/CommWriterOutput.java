package com.minor.HRketel.communicators;

/**
 * Created by Rutger on 12/01/14.
 */
public interface CommWriterOutput {
//    public void push(byte b);
//    public void push(byte[] b);
//
//    public void flush();

    public void sendData(byte[] message);
}
