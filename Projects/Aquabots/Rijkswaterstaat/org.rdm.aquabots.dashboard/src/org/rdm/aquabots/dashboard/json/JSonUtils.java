package org.rdm.aquabots.dashboard.json;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;

public class JSonUtils {

	/**
	 * Create a Json object for the waypoints
	 * @param waypoint
	 * @return
	 */
	public static JsonObject createJsonWayPoint( WayPoint waypoint ){
        JsonObjectBuilder wpBuilder = Json.createObjectBuilder();
        wpBuilder.add( WayPoint.Attributes.ID.toString(), waypoint.getID())
       		.add( toLowerCase( WayPoint.Attributes.NAME), "WayPoint" )
       		.add( toLowerCase( WayPoint.Attributes.LONGTITUDE), waypoint.getLongtitude() )
            .add( toLowerCase( WayPoint.Attributes.LATITUDE), waypoint.getLatitude())
            .add( toLowerCase( WayPoint.Attributes.SPEED), waypoint.getSpeed())
            .add( toLowerCase( WayPoint.Attributes.EVENT), waypoint.getEvent().toString());
        return wpBuilder.build();
	}

	/**
	 * Create a Json object for the waypoints
	 * @param waypoint
	 * @return
	 */
	public static JsonObject createJsonTrajectory( TrajectoryModel trajectory ){
        if( trajectory == null )
        	return null;
		JsonArrayBuilder waypointsBuilder = Json.createArrayBuilder();

		for ( WayPoint waypoint: trajectory.getWayPoints() ) {
			waypointsBuilder.add( createJsonWayPoint( waypoint ));
		}

		JsonObjectBuilder tBuilder = Json.createObjectBuilder();

		tBuilder.add( TrajectoryModel.Attributes.ID.toString(), trajectory.getID())
   			.add( toLowerCase( TrajectoryModel.Attributes.NAME ), "Trajectory" )
   			.add( toLowerCase( TrajectoryModel.Attributes.ARGUMENTS ), waypointsBuilder );
		return tBuilder.build();
	}

	public String toString( JsonObject jo ){
		return jo.toString();
	}
	
	public static String sendMessage( JsonObject jo ){
		return "wsSend( " + jo.toString() + ")";
	}

	public static String sendMessage( TrajectoryModel trajectory ){
		return sendMessage( createJsonTrajectory(trajectory));
	}

	public static String toLowerCase( TrajectoryModel.Attributes attr ){
		return attr.toString().toLowerCase();
	}

	public static String toLowerCase( WayPoint.Attributes attr ){
		return attr.toString().toLowerCase();
	}

}
