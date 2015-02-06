package com.minor.HRketel.arbitrators;

import java.util.ArrayList;

import com.minor.HRketel.behaviours.ControlManually;
import com.minor.HRketel.interfaces.Arbitrator;
import com.minor.HRketel.interfaces.Behaviour;
import com.minor.HRketel.modules.AgentActuatorManager;
import com.minor.HRketel.modules.AgentLocationManager;
import com.minor.HRketel.modules.AgentSensorManager;

public class Agent implements Arbitrator{

    private ArrayList<Behaviour> behaviours;

    private AgentSensorManager agentSensorManager;
    private AgentActuatorManager agentActuatorManager;
    private final AgentLocationManager agentLocationManager;

    private boolean isOn;
    private String status;

    public Agent(AgentSensorManager _agentSensorManager, AgentActuatorManager _agentActuatorManager, AgentLocationManager _agentLocationManager)
    {
        agentSensorManager = _agentSensorManager;
        agentActuatorManager= _agentActuatorManager;
        agentLocationManager = _agentLocationManager;

        behaviours = new ArrayList<Behaviour>();
        isOn = false;
        status = "initializing...";
    }

    public void addBehaviours(Behaviour behaviour)
    {
        behaviour.addSensorManager(agentSensorManager);
        behaviour.addActuatorManager(agentActuatorManager);
        behaviour.addLocationManager(agentLocationManager);
        behaviours.add(behaviour);
    }

    public AgentSensorManager getAgentSensorManager()
    {
        return this.agentSensorManager;
    }

    public AgentActuatorManager getAgentActuatorManager()
    {
        return this.agentActuatorManager;
    }

    public AgentLocationManager getAgentLocationManager()
    {
        return this.agentLocationManager;
    }

    /**
     * cycle through each behaviour and arbitrate control.
     */
    @Override
    public void run() {

        //Log.d("Boat:","Agent is on");
        //while (isOn)
        //{
            if(agentSensorManager.sensorsReady())
            {

                if (((ControlManually)behaviours.get(3)).getManualControlEnabled()) {
                    Behaviour b = behaviours.get(3);

                    if(b.takeControl())
                    {
                        b.action();
                        status(b.status());
                        //Log.d("Boat: ", ""+behaviour.status());
                    }
                } else {
                    if(agentLocationManager.getTargetLocation() != null)
                    {
                        for(Behaviour behaviour: behaviours)
                        {
                            if(behaviour.takeControl())
                            {
                                behaviour.action();
                                status(behaviour.status());
                                //Log.d("Boat: ", ""+behaviour.status());
                            }
                        }
                    }
                }
            }
        //}
        //Log.d("Boat:","Agent is off");
    }

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    public void status(String _status)
    {
        synchronized (status) { status = _status;}
    }

    public String status()
    {
        synchronized (status){ return this.status;}
    }
}
