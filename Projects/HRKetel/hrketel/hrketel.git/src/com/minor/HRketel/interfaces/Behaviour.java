package com.minor.HRketel.interfaces;

import com.minor.HRketel.modules.AgentActuatorManager;
import com.minor.HRketel.modules.AgentLocationManager;
import com.minor.HRketel.modules.AgentSensorManager;

/**
 * interfaces.Behaviour
 */
public interface Behaviour {

    /**
     *
     * @return value indicates if behaviour should take control to do 'action'
     */
    public boolean takeControl();

    /**
     * action taken when control is given.
     */
    public void action();

    /**
     * suppress 'action' output of other layer
     */
    public void suppress();

    /**
     * inhibit input to other layer
     */
    public void inhibit();

    /**
     *
     * @param agentSensorManager Agent sensor manager.
     */
    public void addSensorManager(AgentSensorManager agentSensorManager);


    /**
     *
     * @param agentActuatorManager Agent actuator manager.
     */
    public void addActuatorManager(AgentActuatorManager agentActuatorManager);

    /**
     *
     * @param agentLocationManager Agent location manager.
     */
    public void addLocationManager(AgentLocationManager agentLocationManager);

    /**
     *
     * @return behaviour status.
     */
    public String status();

}
