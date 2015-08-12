package org.rdm.aquabots.dashboard.json;

import java.io.StringReader;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.stream.JsonParser;

import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.TrajectoryModel.Boats;
import org.rdm.aquabots.dashboard.model.boat.Path;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint.LonLat;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class PredefinedRoutes {

	public static final String S_NAME = "Name";
	
	public static final String S_WRN_UNKNOWN_KEY = "Unknown element with key=";
	
	public enum Routes{
		IJSSEL,
		STOP,
		HEIJPLAAT_KADE,
		HEIJPLAAT_RONDJE;

		@Override
		public String toString() {
			return StringStyler.prettyString(super.toString());
		}

		public static String[] getNames(){
			String[] results = new String[ values().length ];
			int index = 0;
			for( Routes routes: values()){
				results[index++] = routes.toString();
			}
			return results;
		}
	}

	public static final String S_IJSSEL_Example = "{'Name':'Aftica','Path':{'Length':5,'Currentwp':0," 
	+ "Waypoints':[{'lat':52.250240325927734,'lon':6.154749870300293},"
	+ "{'lat':52.250518798828125,'lon':6.1536102294921875},"
	+ "{'lat':52.249881744384766,'lon':6.15378999710083},"
	+ "{'lat':52.250240325927734,'lon':6.1547698974609375},"
	+ "{'lat':52.250240325927734,'lon':6.1547698974609375}]}}";

	public static final String S_Stop = "{\"Name\":\"Aftica\",\"Path\":{\"Length\":0,\"Currentwp\":0, \"Waypoints\":[]}}";

	/**
	 * Oversteek
	 * @return
	 */
	public static final String S_Heijplaat_Rondje = "{\"Name\":\"Aftica\",\"Path\":{\"Length\":12,\"Currentwp\":0,\"Waypoints\":"
				+ "[{\"lat\":51.89754104614258,\"lon\":4.420790195465088},"
				+ "{\"lat\":51.89775848388672,\"lon\":4.420470237731934},"
				+ "{\"lat\":51.89802169799805,\"lon\":4.420760154724121},"
				+ "{\"lat\":51.89822006225586,\"lon\":4.420479774475098},"
				+ "{\"lat\":51.89847183227539,\"lon\":4.4205498695373535},"
				+ "{\"lat\":51.89876174926758,\"lon\":4.420330047607422},"
				+ "{\"lat\":51.89888000488281,\"lon\":4.420450210571289},"
				+ "{\"lat\":51.89881896972656,\"lon\":4.420680046081543},"
				+ "{\"lat\":51.89830017089844,\"lon\":4.420690059661865},"
				+ "{\"lat\":51.897979736328125,\"lon\":4.420479774475098},"
				+ "{\"lat\":51.89754867553711,\"lon\":4.420760154724121},"
				+ "{\"lat\":51.89754867553711,\"lon\":4.420740127563477}]}}";
	
	/**
	 * Langs de kade
	 * @return
	 */
	public static final String S_Heijplaat_Kade = "{\"Name\":\"Aftica\",\"Path\":{\"Length\":10,\"Currentwp\":0,\"Waypoints\":"
				+ "[{\"lat\":51.897579193115234,\"lon\":4.42087984085083},"
				+ "{\"lat\":51.8974494934082,\"lon\":4.420810222625732},"
				+ "{\"lat\":51.89738082885742,\"lon\":4.420909881591797},"
				+ "{\"lat\":51.89725875854492,\"lon\":4.4208598136901855},"
				+ "{\"lat\":51.897178649902344,\"lon\":4.420949935913086},"
				+ "{\"lat\":51.89707946777344,\"lon\":4.420909881591797},"
				+ "{\"lat\":51.89704895019531,\"lon\":4.420839786529541},"
				+ "{\"lat\":51.89707946777344,\"lon\":4.420760154724121},"
				+ "{\"lat\":51.897579193115234,\"lon\":4.42087984085083},"
				+ "{\"lat\":51.897579193115234,\"lon\":4.42087984085083}]}}";

	private static Logger logger = Logger.getLogger( PredefinedRoutes.class.getName());

	public static String getRoute( Routes route ){
		switch( route ){
		case HEIJPLAAT_RONDJE:
			return S_Heijplaat_Rondje;
		case IJSSEL:
			return S_IJSSEL_Example;
		case HEIJPLAAT_KADE:
			return S_Heijplaat_Kade;
		default:
			return S_Stop;
		}
	}
	
	public static String getRandom(){
		int random = ( int )( Math.random() * Routes.values().length);
		Routes route = Routes.values()[random];
		return getRoute( route );
	}
	
	public static TrajectoryModel getTrajectory( Routes route ){
		String str = getRoute( route );
		StringReader reader = new StringReader( str );
		TrajectoryModel model = null;
		JsonParser.Event event;
		WayPoint waypoint = null;
		String key = null;
		try {
			JsonParser parser = Json.createParser(reader);
			int lonlat = 0;
			while( parser.hasNext() ){
				event = parser.next();
	            switch (event) {
	            case KEY_NAME:
	                key = parser.getString();
	                break;
	            case VALUE_STRING:
	            	model = setModel(key, parser.getString());
	            	break;
	            case START_OBJECT:
	                if( LonLat.isValid( key ))
	                	lonlat++;
	                WayPoint wp = setObject(key, model, lonlat);
	                if( wp != null ){
	                	waypoint = wp;
	                	model.addWayPoint(waypoint);
	                }
	                break;
	            case END_OBJECT:
	                if( Path.Attributes.WAYPOINTS.toString().equals(key )){
	            		waypoint = null;
	                }else if( LonLat.isValid( key ))
	                	lonlat = lonlat%2;
	            	break;
	            case VALUE_NUMBER:
	                setNumberValues( key, model, waypoint, parser );
	                break;
	            case VALUE_FALSE:
	                break;
	            case VALUE_TRUE:
	                break;
	            case VALUE_NULL:
	                break;
	            default:
	                break;
	            }						
			}
		} 
		catch (JsonException ex) {
			return null;
		}
		return model;
	}		

    private static void setNumberValues( String keyName, TrajectoryModel model, WayPoint waypoint, JsonParser parser ) {
        if( Path.Attributes.LENGTH.toString().equals(keyName )){
            /* Nothing */
        }else
    	if( Path.Attributes.CURRENTWP.toString().equals(keyName )){
            model.setActiveWaypoint( parser.getInt());
        }else
        if( LonLat.LON.name().toLowerCase().equals(keyName )){
            String str = parser.getString();
        	waypoint.setLongtitude( Float.valueOf( str ));
        }else
        if( LonLat.LAT.name().toLowerCase().equals(keyName )){
            String str = parser.getString();
        	waypoint.setLatitude( Float.valueOf( str ));
        }else{
            logger.warning( S_WRN_UNKNOWN_KEY + keyName);   
        }
    }

    private static WayPoint setObject( String keyName, TrajectoryModel model, int lonlat ) {
        if( Path.Attributes.WAYPOINTS.toString().equals(keyName )){
            return new WayPoint();
        }else if( LonLat.isValid( keyName )){
        	if( lonlat == 2 )	
        		return new WayPoint();
        }
        else{
            logger.warning( S_WRN_UNKNOWN_KEY + keyName);   
        }
        return null;
    }

    private static TrajectoryModel setModel( String key, String value) {
        TrajectoryModel model = null;
         if( S_NAME.equals( key )){
            String str = StringStyler.styleToEnum(value);
            model = TrajectoryModel.newTrajectory( Boats.valueOf(str));
         }else{
             logger.warning( S_WRN_UNKNOWN_KEY + key);   
        }
        return model;
    }
}
