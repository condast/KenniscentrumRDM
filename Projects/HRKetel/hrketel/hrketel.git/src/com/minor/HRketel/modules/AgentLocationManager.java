package com.minor.HRketel.modules;

import android.location.Location;
import android.location.LocationManager;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Rutger on 04/02/14.
 */
public class AgentLocationManager {
    private Location startLocation;
    private Location endLocation;
    private Location targetLocation;
    private Queue<Location> travelRoute;


    public AgentLocationManager() {

        travelRoute = new LinkedList<Location>();

        startLocation = new Location(LocationManager.GPS_PROVIDER);
        endLocation = new Location(LocationManager.GPS_PROVIDER);

        // 51.2315f, 4.1226f
        //targetLocation = new Location(LocationManager.GPS_PROVIDER);
        //targetLocation.setLatitude(0f);
        //targetLocation.setLongitude(0f);
    }


    public void populateCourse(String locations) {
        String[] latlngs = locations.split(";");

        String[] first = latlngs[0].split(",");
        String[] last = latlngs[latlngs.length -1].split(",");

        startLocation.setLatitude(Float.parseFloat(first[0]));
        startLocation.setLongitude(Float.parseFloat(first[1]));

        endLocation.setLatitude(Float.parseFloat(last[0]));
        endLocation.setLongitude(Float.parseFloat(last[1]));


        for (String e : latlngs) {
            Location curr = new Location(LocationManager.GPS_PROVIDER);

            String[] spl = e.split(",");
            curr.setLatitude(Float.parseFloat(spl[0]));
            curr.setLongitude(Float.parseFloat(spl[1]));

            travelRoute.add(curr);
        }

        targetLocation = startLocation;
    }


    public Location getTargetLocation() {
        synchronized (this) {
            return targetLocation;
        }
    }

    public void setTargetLocation(Location targetLocation) {
        synchronized (targetLocation) {
            this.targetLocation = targetLocation;
        }
    }

    public float calculateDistanceToTarget(Location currentLocation) {

        return currentLocation.distanceTo(targetLocation);

    }
}
