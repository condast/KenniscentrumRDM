package com.minor.HRketel.behaviours;


import com.minor.HRketel.interfaces.Behaviour;
import com.minor.HRketel.modules.AgentActuatorManager;
import com.minor.HRketel.modules.AgentLocationManager;
import com.minor.HRketel.modules.AgentSensorManager;

/**
 * behaviours.AdjustVelocity, partially responsible for velocity (Adjust direction)
 */
public class AdjustVelocity implements Behaviour{

    private AgentSensorManager agentSensorManager;
    private AgentActuatorManager agentActuatorManager;
    private AgentLocationManager agentLocationManager;

    float direction;
    private String status;

    public AdjustVelocity()
    {
        status = "";
    }

    /**
     * @return value indicates if behaviour should take control to do 'action'
     */
    @Override
    public boolean takeControl() {
        boolean control = false;

//        direction = agentSensorManager.getCompassListener().calculateDirection(agentSensorManager.getGpsListener().getLocation(), 51.2315f, 4.1226f);
//        direction = agentSensorManager.getCompassListener().calculateDirection(agentSensorManager.getGpsListener().getLocation(), agentLocationManager.getTargetLocation());

        try {
            Thread.sleep(500);
        } catch (Exception e) {}

        direction = (float) new java.util.Random().nextInt(360);

        if(direction > 45f && direction < 315f) control = true; else control = false;

        return control;
    }

    /**
     * action taken when control is given.
     */
    @Override
    public void action()
    {
        if(direction > 45f && direction < 315f )
        {
            if(direction < 180f) {
//                status = String.format(">>, %s", direction);
                synchronized (this)
                {
                    agentActuatorManager.getMotor().right();
                }
                // Log.d("Boat: ", "Starboard, arr~!");
                // Log.d("Boat:", "Right");
            }

            if(direction > 180f) {
//                status = String.format("<<, %s", direction);

                synchronized (this)
                {
                    agentActuatorManager.getMotor().left();
                }
                // Log.d("Boat: ","Port, arr~!");
                // Log.d("Boat:", "Left");
            }
        }
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
    public void addActuatorManager(AgentActuatorManager agentActuatorManager) {
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
        synchronized (status) { return this.status;}
    }
}
