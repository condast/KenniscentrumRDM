package com.minor.HRketel.interfaces;

import com.minor.HRketel.communicators.Header_t;

/**
 * Created by Rutger on 03/02/14.
 */
public interface Sora {
    public static final int SENSOR = 0x00;
    public static final int ACTUATOR = 0x01;

    void notify(Header_t header, byte[] data, int len);
}
