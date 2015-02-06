package com.minor.HRketel.behaviours;

import com.minor.HRketel.interfaces.Behaviour;
import com.minor.HRketel.modules.AgentActuatorManager;
import com.minor.HRketel.modules.AgentLocationManager;
import com.minor.HRketel.modules.AgentSensorManager;

/**
 * behaviours.Move, partially responsible for velocity of the boat, (adjust speed in direction)
 */
public class Move implements Behaviour{


    private AgentSensorManager agentSensorManager;
    private AgentActuatorManager agentActuatorManager;
    private AgentLocationManager agentLocationManager;

    private float direction;
    private String status;


    public Move()
    {
        status = "";
    }

    /**
     * @return value indicates if behaviour should take control to do 'action'
     */
    @Override
    public boolean takeControl() {
        boolean control = false;
        if(agentSensorManager.sensorsReady())
        {
//            direction = agentSensorManager.getCompassListener().calculateDirection(agentSensorManager.getGpsListener().getLocation(), 51.2315f, 4.1226f);
            direction = agentSensorManager.getCompassListener().calculateDirection(agentSensorManager.getGpsListener().getLocation(), agentLocationManager.getTargetLocation());


//            direction = (float) new Random().nextInt(360);

            if(direction < 45f || direction > 315f) {
                control = true;
            } else {
                control = false;
            }
        }
        return control;
    }

    /**
     * action taken when control is given.
     */
    @Override
    public void action() {

//        status = "/\\"+" , "+direction;

        synchronized (this) {
            agentActuatorManager.getMotor().forward();
        }
        //Log.d("Boat: ", "Forward arr~!");
    }

    /**
     * suppress 'action' output of other layer
     */
    @Override
    public void suppress() {

    }

    /**
     * inhibit input to other layer
     */
    @Override
    public void inhibit() {

    }

    /**
     * @param agentSensorManager Agent sensor manager.
     */
    @Override
    public void addSensorManager(AgentSensorManager agentSensorManager)
    {
        this.agentSensorManager = agentSensorManager;
    }

    /**
     * @param agentActuatorManager Agent sensor manager.
     */
    @Override
    public void addActuatorManager(AgentActuatorManager agentActuatorManager)
    {
        this.agentActuatorManager = agentActuatorManager;
    }

    /**
     * @param agentLocationManager Agent location manager.
     */
    @Override
    public void addLocationManager(AgentLocationManager agentLocationManager) {
        this.agentLocationManager = agentLocationManager;
    }

    /**
     * @return behaviour status.
     */
    @Override
    public String status() {
        synchronized (status){return status;}
    }
}
