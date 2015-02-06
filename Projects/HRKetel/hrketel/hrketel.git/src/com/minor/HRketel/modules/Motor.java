package com.minor.HRketel.modules;

import com.minor.HRketel.communicators.CommWriter;
import com.minor.HRketel.communicators.Header_t;
import com.minor.HRketel.enums.Sora_t;
import com.minor.HRketel.interfaces.Actuator;
import com.minor.HRketel.interfaces.Sora;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Rutger on 03/02/14.
 */
public class Motor implements Actuator {

    private CommWriter cw;

    private int propeller_left;
    private int propeller_right;

    private int last_left_spd;
    private int last_left_dir;

    private int last_right_spd;
    private int last_right_dir;

    private enum State_t {
        FORWARD, REVERSE, LEFT, RIGHT, NEUTRAL
    };

    private State_t last_state;

    public Motor() {
        cw = new CommWriter();

        propeller_left = Sora_t.MOTOR_LEFT.ordinal();
        propeller_right = Sora_t.MOTOR_RIGHT.ordinal();
    }

    public void setOstream(OutputStream ostr) {
        synchronized (this) {
            cw = new CommWriter(ostr);
        }
    }


    public void left() {
        left(1, 0);
        right(1, 180);

        last_state = State_t.LEFT;
    }

    public void right() {
        left(1, 180);
        right(1, 0);

        last_state = State_t.RIGHT;
    }

    public void forward() {
        forward(180);

        last_state = State_t.FORWARD;
    }

    public void forward(int speed) {
        left(1, speed);
        right(1, speed);

        last_state = (speed == 0)? State_t.NEUTRAL : State_t.FORWARD;
    }

    public void reverse() {
        cw.createHeader(Sora.ACTUATOR, Sora_t.MOTOR_LEFT.ordinal());
        cw.addInt(0);
        cw.addInt(180);

        try {
            cw.send();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cw.createHeader(Sora.ACTUATOR, Sora_t.MOTOR_RIGHT.ordinal());
        cw.addInt(0);
        cw.addInt(180);

        try {
            cw.send();
        } catch (IOException e) {
            e.printStackTrace();
        }

        last_state = State_t.REVERSE;
    }



    public void left(int direction, int speed) {
        try {
            cw.createHeader(Sora.ACTUATOR, Sora_t.MOTOR_LEFT.ordinal());
            cw.addInt(direction);
            cw.addInt(speed);
            cw.send();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void right(int direction, int speed) {
        try {
            cw.createHeader(Sora.ACTUATOR, Sora_t.MOTOR_RIGHT.ordinal());
            cw.addInt(direction);
            cw.addInt(speed);
            cw.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(Header_t header, byte[] data, int len) {

    }

}
