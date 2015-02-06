package com.minor.HRketel.behaviours;


import android.location.Location;
import com.minor.HRketel.interfaces.Behaviour;
import com.minor.HRketel.modules.AgentActuatorManager;
import com.minor.HRketel.modules.AgentLocationManager;
import com.minor.HRketel.modules.AgentSensorManager;

import java.util.Queue;

/**
 * behaviours.MapCourse, responsible for plotting course and relaying next target to lower layer.
 */
public class MapCourse implements Behaviour {

    private AgentSensorManager agentSensorManager;
    private AgentActuatorManager agentActuatorManager;
    private AgentLocationManager agentLocationManager;

    private Location startLocation;
    private Location endLocation;
    private Queue<Location> travelRoute;

    private String status;


    public MapCourse() {

    }

    /**
     * @return value indicates if behaviour should take control to do 'action'
     */
    @Override
    public boolean takeControl() {
        boolean control = false;

        try {
            if (agentLocationManager.calculateDistanceToTarget(agentSensorManager.getGpsListener().getLocation()) <= 6f) {
                control = true;
            }
        } catch (NullPointerException e) {
            // no "totarget" (yet) ...
        }

        return control;
    }

    /**
     * action taken when control is given.
     */
    @Override
    public void action() {
        Location nextTarget = travelRoute.poll();

        if (nextTarget != null) {
            agentLocationManager.setTargetLocation(nextTarget);
        } else {

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
        synchronized (status){return status;}
    }
}
