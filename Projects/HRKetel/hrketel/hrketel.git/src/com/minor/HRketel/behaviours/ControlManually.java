package com.minor.HRketel.behaviours;

import android.util.Log;
import com.minor.HRketel.interfaces.Behaviour;
import com.minor.HRketel.modules.AgentActuatorManager;
import com.minor.HRketel.modules.AgentLocationManager;
import com.minor.HRketel.modules.AgentSensorManager;
import com.minor.HRketel.server.Server;
import com.minor.HRketel.server.ServerCommunicationCallback;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * behaviours.ControlManually, responsible for actions in case of a failure.
 */
public class ControlManually implements Behaviour, ServerCommunicationCallback {

    private AgentSensorManager agentSensorManager;
    private AgentActuatorManager agentActuatorManager;
    private AgentLocationManager agentLocationManager;

    private Server server;

    private String status;
    private String message;
    private boolean manualControlEnabled = false;
    private boolean neutralizeMotor;

    public boolean getManualControlEnabled() {
        synchronized (this) {
            return manualControlEnabled;
        }
    }

    private int commandLength;

    private int dir1;
    private int dir2;
    private int spd1;
    private int spd2;


    public ControlManually() {
        server = new Server(this);
        status = "ControllManually";
    }


    /**
     * @return value indicates if behaviour should take control to do 'action'
     */
    @Override
    public boolean takeControl() {
        return manualControlEnabled;
    }

    /**
     * action taken when control is given.
     */
    @Override
    public void action() {
//        Log.wtf("control", "-> action");
//        status = "ctr actn";

        if (neutralizeMotor) {
            agentActuatorManager.getMotor().forward(0);
            neutralizeMotor = false;
        }


        if (commandLength > 1) // control the motor !?
        {

            synchronized (this)
            {
                agentActuatorManager.getMotor().left(dir1, spd1);
            }

            synchronized (this)
            {
                agentActuatorManager.getMotor().right(dir2, spd2);
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
    public void addSensorManager(AgentSensorManager agentSensorManager) {
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

    @Override
    public void onReceiveMessage(String message) {
        this.message = message;

        Log.wtf("control", "-> " + message);

        if (message != null)
        {
            synchronized (status) {
                status = message;
            }
        }

        JSONObject json;
        try {
            json = new JSONObject(message);

            String action = json.getString("action");
            String data = json.getString("data");
            this.commandLength = data.length();

            if (action.equals("coords")) {
                agentLocationManager.populateCourse(data);
                Log.wtf("control", String.format("coords -> %s", message));
            }

            if (action.equals("command")) {
                if (data.length() == 1) {

                    synchronized (this) {
                        Log.wtf("control", String.format("command AI on/off [1] -> %s", data));
                        manualControlEnabled = (Integer.valueOf(data) > 0)? true: false;
                        neutralizeMotor = manualControlEnabled;
                        Log.wtf("control", String.format("command AI boolean -> '%s'", manualControlEnabled));
                    }



                } else {
                    // motor
                    Log.wtf("control", String.format("command motor -> %s", data));

                    status = data.toString();

                    String[] parts = data.split(";");
                    dir1 = Integer.parseInt(parts[0]);
                    dir2 = Integer.parseInt(parts[1]);
                    spd1 = Integer.parseInt(parts[2]);
                    spd2 = Integer.parseInt(parts[3]);


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            this.message = null;
        }
    }
}
