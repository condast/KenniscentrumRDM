package org.rdm.aquabots.dashboard.json;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;

import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.boat.Command;
import org.rdm.aquabots.dashboard.model.boat.Path;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint.LonLat;

public class JsonUtils {

	/**
	 * Create a Json object for the waypoints
	 * @param waypoint
	 * @return
	 */
	public static JsonObject createJsonWayPoint( WayPoint waypoint ){
        JsonObjectBuilder wpBuilder = Json.createObjectBuilder();
        wpBuilder.add( WayPoint.Attributes.ID.toString(), waypoint.getID())
       		.add( toLowerCase( WayPoint.Attributes.NAME), "WayPoint" )
       		.add( toLowerCase( WayPoint.Attributes.LONGTITUDE), breakDigits( waypoint.getLongtitude(), 5 ))
            .add( toLowerCase( WayPoint.Attributes.LATITUDE), breakDigits( waypoint.getLatitude(), 5 ))
            .add( toLowerCase( WayPoint.Attributes.SPEED), waypoint.getSpeed())
            .add( toLowerCase( WayPoint.Attributes.EVENT), waypoint.getEvent().toString());
        return wpBuilder.build();
	}

	/**
	 * Create a Json object for the waypoints
	 * @param waypoint
	 * @return
	 */
	public static JsonObject createSimpleJsonWayPoint( WayPoint waypoint ){
        JsonObjectBuilder wpBuilder = Json.createObjectBuilder();
        wpBuilder.add( toLowerCase( WayPoint.LonLat.LAT ), breakDigits( waypoint.getLatitude(), 5 ))
       		.add( toLowerCase( WayPoint.LonLat.LON ), breakDigits( waypoint.getLongtitude(), 5 ));
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
   			.add( toLowerCase( TrajectoryModel.Attributes.BOAT ), trajectory.getBoat() )
   			.add( toLowerCase( TrajectoryModel.Attributes.ARGUMENTS ), waypointsBuilder );
		return tBuilder.build();
	}

	/**
	 * Create a Json object for the waypoints
	 * @param waypoint
	 * @return
	 */
	public static JsonObject createJsonPath( TrajectoryModel trajectory ){
        if( trajectory == null )
        	return null;
		Path path = new Path( trajectory );
        JsonArrayBuilder waypointsBuilder = Json.createArrayBuilder();

		for ( WayPoint waypoint: path.getWayPoints() ) {
			waypointsBuilder.add( createSimpleJsonWayPoint(waypoint));
		}

		JsonObjectBuilder tBuilder = Json.createObjectBuilder();

		tBuilder.add( Path.Attributes.LENGTH.toString(), path.getLength() )
   			.add( Path.Attributes.CURRENTWP.toString(), path.getCurrentWP() )
   			.add( Path.Attributes.WAYPOINTS.toString(), waypointsBuilder );
		return tBuilder.build();
	}
	
	/**
	 * Create a Json object for the waypoints
	 * @param waypoint
	 * @return
	 */
	public static JsonObject createCommand( TrajectoryModel trajectory ){
        if( trajectory == null )
        	return null;
        Command command = new Command( trajectory ); 

		JsonObjectBuilder tBuilder = Json.createObjectBuilder();

		tBuilder.add( Command.Attributes.NAME.toString(), command.getName() )
   			.add( Command.Attributes.PATH.toString(), createJsonPath( trajectory ) );
		return tBuilder.build();
	}
	public String toString( JsonObject jo ){
		return jo.toString();
	}
	
	public static String sendMessage( JsonObject jo ){
		return "wsSend( " + jo.toString() + ")";
	}

	public static String sendMessage( TrajectoryModel trajectory ){
		return sendMessage( createCommand(trajectory));
	}

	public static String toLowerCase( TrajectoryModel.Attributes attr ){
		return attr.toString().toLowerCase();
	}

	public static String toLowerCase( WayPoint.Attributes attr ){
		return attr.toString().toLowerCase();
	}

	public static String toLowerCase( Path.Attributes attr ){
		return attr.toString().toLowerCase();
	}

	public static String toLowerCase( Command.Attributes attr ){
		return attr.toString().toLowerCase();
	}

	public static String toLowerCase( LonLat attr ){
		return attr.toString().toLowerCase();
	}

	private static float breakDigits( float number, int digits ){
		String format = "%." + digits + "f";
		String str = String.format( format, number );
		str = str.replace(",", ".");
		return Float.valueOf( str );
	}
	
	public static boolean validateJson( String str ){
		StringReader reader = new StringReader( str );
		try {
			JsonParser parser = Json.createParser(reader);
			while( parser.hasNext() ){
				parser.next();
			}
		} 
		catch (JsonException ex) {
			return false;
		}
		return true;
	}
	
	public static JsonObject convertToJson( String str ){
		JsonReader jsonReader = Json.createReader(new StringReader( str ));
		JsonObject object = null;
		try{
			object = jsonReader.readObject();
		}
		finally{
			jsonReader.close();
		}
		return object;
	}
}
