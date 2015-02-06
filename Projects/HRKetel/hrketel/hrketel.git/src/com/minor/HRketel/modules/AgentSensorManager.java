package com.minor.HRketel.modules;

import android.app.Activity;
import com.minor.HRketel.sensors.CompassListener;
import com.minor.HRketel.sensors.GPSListener;

/**
 * modules.AgentSensorManager initializes and manages sensors.
 */
public class AgentSensorManager {

    private Activity context;
    private CompassListener compassListener;
    private GPSListener gpsListener;

    public AgentSensorManager(Activity _context)
    {
        context = _context;
    }

    public void initialize()
    {
        compassListener = new CompassListener(context);
        gpsListener = new GPSListener(context);

        compassListener.initialize();
        gpsListener.initialize();
    }

    public void activateSensors()
    {
        if(sensorsReady())
        {
            compassListener.listen();
            gpsListener.listen();
        }
    }

    public void deactivateSensors()
    {
        if(sensorsReady())
        {
            compassListener.stopListening();
            gpsListener.stopListening();
        }
    }

    public boolean sensorsReady()
    {
        return (compassListener.available() && gpsListener.available());
    }

    public CompassListener getCompassListener()
    {
        return compassListener;
    }

    public GPSListener getGpsListener()
    {
        return gpsListener;
    }
}
