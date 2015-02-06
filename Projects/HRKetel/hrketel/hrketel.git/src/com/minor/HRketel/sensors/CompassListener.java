package com.minor.HRketel.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.*;
import android.location.Location;
import android.widget.TextView;

/**
 * Listens for compass sensor updates.
 */
public class CompassListener {

    private Float azimuth;
    //private float direction;
    private Activity activity;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor accel_sensor;
    private Sensor magn_sensor;
    private TextView textView;
    private Location location;

    public CompassListener(Activity _activity)
    {
        azimuth = 0f;
        activity = _activity;
    }

    public void initialize()
    {
        if(sensorManager == null)
        {
            sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
            accel_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magn_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            //textView = (TextView) activity.findViewById(R.id.compass_tv);
        }
    }

    public boolean available()
    {
        return (accel_sensor != null && magn_sensor != null)? true: false;
    }

    public void listen()
    {
        if(available())
        {
            sensorEventListener = new ListenToCompassSensor();
            sensorManager.registerListener(sensorEventListener, accel_sensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorEventListener, magn_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stopListening()
    {
        if(sensorManager != null)
        {
            sensorManager.unregisterListener(sensorEventListener,accel_sensor);
            sensorManager.unregisterListener(sensorEventListener, magn_sensor);
        }
    }

    /**
     *
     * @param location  Current location (happy now =p?)
     *
     * @return Direction relative to target (not north)
     */
    public float calculateDirection(Location location, Location target)
    {
        synchronized (this)
        {
            if(location == null) return 0f;

//            Location target = new Location(LocationManager.GPS_PROVIDER);
//            target.setLatitude(lat);
//            target.setLongitude(lng);
            float bearingTo = location.bearingTo(target);
            if(bearingTo < 0) bearingTo = bearingTo + 360;
            GeomagneticField geomagneticField = new GeomagneticField( (float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), System.currentTimeMillis());
            azimuth -= geomagneticField.getDeclination();
            float _direction = bearingTo - azimuth;
            if(_direction < 0) _direction = _direction + 360;
            float direction = _direction;
            return direction;
        }
    }

    public float getAzimuth()
    {
        synchronized (azimuth) { return azimuth;}
    }

//    public float getDirection()
//    {
//        return direction;
//    }

    public void setLocation(Location _location)
    {
        this.location = _location;
    }

    private class ListenToCompassSensor implements SensorEventListener{

        private float[] accel_values;
        private float[] magn_values;

        public ListenToCompassSensor()
        {
            accel_values = new float[3];
            magn_values = new float[3];
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            switch (event.sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER: accel_values = event.values.clone(); break;
                case Sensor.TYPE_MAGNETIC_FIELD: magn_values = event.values.clone(); break;
            }
            float[] R = new float[16];
            float[] orientation_values = new float[3];
            SensorManager.getRotationMatrix(R, null, accel_values, magn_values);
            SensorManager.getOrientation(R, orientation_values);
            azimuth = (float) Math.toDegrees(orientation_values[0]);
            //Log.d("Boat:", " " + azimuth);
            //calculateDirection(51.2222f, 4.4111f,  azimuth);
            //textView.setText("direction: "+direction);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
