package com.minor.HRketel.modules;

public class AgentActuatorManager {

    private Motor motor;

    public AgentActuatorManager() {
        motor = new Motor();
    }

    public Motor getMotor() {
        synchronized (this)
        {
            return motor;
        }
    }

    public void registerMotor(Motor m) {
        synchronized (this) {
            motor = m;
        }
    }
}
