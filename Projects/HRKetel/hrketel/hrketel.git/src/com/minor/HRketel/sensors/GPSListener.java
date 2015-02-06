package com.minor.HRketel.sensors;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Listens for GPS location updates.
 */
public class GPSListener {

    private Activity activity;
    private Location location;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView textView;
    //private CompassListener compassListener;

    public GPSListener(Activity _activity)
    {
        activity = _activity;
        //compassListener = _compassListener;
    }

    public void initialize()
    {
        if(locationManager == null) locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        //textView = (TextView) activity.findViewById(R.id.test_tv);
    }

    public boolean available()
    {
        return (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))? true: false;
    }

    public void listen()
    {
        if(available())
        {
            locationListener = new ListenToLocation();
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1,1, locationListener);
        }
    }

    public void stopListening()
    {
        if(locationManager != null) locationManager.removeUpdates(locationListener);
    }

    public Location getLocation()
    {
       synchronized (this)
       {
        return location;
       }
    }


    /**
     * locationListener updates location if available.
     */
    private class ListenToLocation implements LocationListener{


        @Override
        public void onLocationChanged(Location _location) {

            location = _location;
            //Log.d("Boat: ", "lat: "+location.getLatitude()+" lng: "+location.getLongitude());
            //textView.setText("lat: "+location.getLatitude()+" lng: "+location.getLongitude());
            //compassListener.setLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
