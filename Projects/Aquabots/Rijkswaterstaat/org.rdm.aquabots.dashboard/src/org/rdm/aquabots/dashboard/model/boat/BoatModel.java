package org.rdm.aquabots.dashboard.model.boat;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

import org.rdm.aquabots.dashboard.json.PredefinedRoutes.Routes;
import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.TrajectoryModel.Boats;
import org.rdm.aquabots.dashboard.model.boat.Gps.GPS;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.model.waypoint.WayPointManager;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint.LonLat;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class BoatModel {

	public static final int DEFAULT_SPEED = 50;
	
	public static final String S_NAME = "name";

	public static final String S_WRN_UNKNOWN_KEY = "Unknown key: ";

	public enum Boats{
		AFTICA,
		EIND_MAAS,
		COSTA,
		JULES_DOCK;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	public enum Attributes{
		ID,
		BOAT,
		ARGUMENTS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	public enum Types{
		CONTINUOUS,
		BOUNDED;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	public enum Parameters{
		TYPE,
		STYLE,
		COORDINATES;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	public enum Append{
		DEFAULT,
		LAST,
		ACTIVE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	public enum ParameterTypes{
		DRAWSTART,
		DRAWEND;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	private int id;
	private long time;

	private Vector<WayPoint> waypoints;
	private int active;
	private WayPointManager manager = WayPointManager.getInstance();

	private Collection<IBoatListener> listeners;

	private static BoatModel model = new BoatModel( Boats.AFTICA.toString() );

	private WayPoint last;
	private Lock lock;
	private String boat;
	
	private static Logger logger = Logger.getLogger( BoatModel.class.getName() );

	private BoatModel( String boat ) {
		this.time = Calendar.getInstance().getTimeInMillis();
		this.boat = boat;
		waypoints = new Vector<WayPoint>();
		this.active = 0;
		this.listeners = new ArrayList<IBoatListener>();
		this.lock = new ReentrantLock();
		this.id = this.hashCode();
	}

	private BoatModel( int id, String boat, Vector<WayPoint> waypoints ) {
		this.id = id;
		this.boat = boat;
		this.time = Calendar.getInstance().getTimeInMillis();
		this.waypoints = waypoints;
		this.active = 0;
		this.listeners = new ArrayList<IBoatListener>();
		this.lock = new ReentrantLock();
	}

	public static BoatModel getInstance(){
		return model;
	}

	public int getID(){
		return this.id;
	}
	
	public String getBoat(){
		return this.boat;
	}
	
	public void clear(){
		this.listeners.clear();
	}

	public void reset(){
		this.listeners.clear();
		this.waypoints.clear();
	}

	public void addListener( IBoatListener listener ){
		this.listeners.add( listener );
	}

	public void removeListener( IBoatListener listener ){
		this.listeners.remove( listener );
	}

	protected void notifyListeners( WayPoint waypoint){
		for( IBoatListener listener: this.listeners ){
			listener.notifyTrajectoryChanged( new BoatEvent( this, waypoint ));
		}
	}

	public WayPoint getActiveWaipoint(){
		return this.waypoints.get( active );
	}
	
	
	public void setActiveWaypoint(int active) {
		this.active = active;
	}

	public int getActiveIndex(){
		return active;
	}
	
	public void next(){
		this.active++;
	}
	
	public synchronized void addWayPoint( WayPoint waypoint ){
		this.lock.lock();
		try{
			if(( last != null ) && last.compareTo( waypoint ) == 0 )
				return;
			this.last = waypoint;
			this.waypoints.add( waypoint );
			manager.addWayPoint(waypoint);
			waypoint.setEvent( WayPoint.WayPointEvents.APPENDED );
			this.notifyListeners( waypoint );
		}
		finally{
			lock.unlock();
		}
	}

	public void insertWayPoint( WayPoint waypoint ){
		this.lock.lock();
		try{
			if(( last != null ) && last.compareTo( waypoint ) == 0 )
				return;
			this.last = waypoint;
			this.waypoints.insertElementAt( waypoint, this.active);
			waypoint.setEvent( WayPoint.WayPointEvents.INSERTED );
			this.notifyListeners( waypoint );
		}
		finally{
			lock.unlock();
		}
	}

	public void overrideWayPoint( WayPoint waypoint ){
		this.lock.lock();
		try{
			if(( last != null ) && last.compareTo( waypoint ) == 0 )
				return;
			this.last = waypoint;
			while( this.waypoints.size() >= this.active )
				this.waypoints.remove(this.active);

			this.waypoints.add( waypoint );
			waypoint.setEvent( WayPoint.WayPointEvents.OVERRIDDEN );
			this.notifyListeners( waypoint );
		}
		finally{
			lock.unlock();
		}
	}

	public synchronized void removeWayPoint( WayPoint waypoint ){
		this.lock.lock();
		try{
			this.waypoints.add( waypoint );
			waypoint.setEvent( WayPoint.WayPointEvents.REMOVED );
			this.notifyListeners( waypoint );
		}
		finally{
			lock.unlock();
		}
	}

	public WayPoint[] getWayPoints(){
		this.lock.lock();
		try{		
			return this.waypoints.toArray( new WayPoint[this.waypoints.size() ]);
		}
		finally{
			lock.unlock();
		}
	}
	
	/**
	 * Create a waypoint 
	 * @param params
	 * @return
	 */
	public WayPoint[] createWayPoints( Map<String,String> params, Append append ){
		Collection<WayPoint> results = new ArrayList<WayPoint>();
		Set<Map.Entry<String, String>> entryset = params.entrySet();
		Iterator<Map.Entry<String, String>> iterator = entryset.iterator();

		ParameterTypes type = null;

		WayPoint.Styles style = null;
		String[] coords = null;
		while( iterator.hasNext() ){
			Map.Entry<String, String> entry = iterator.next();
			Parameters param = Parameters.valueOf( StringStyler.styleToEnum( entry.getKey()));
			switch( param ){
			case TYPE:
				type = ParameterTypes.valueOf( StringStyler.styleToEnum( entry.getValue() ));
				break;
			case STYLE:
				style = WayPoint.Styles.valueOf( StringStyler.styleToEnum( entry.getValue() ));
				break;
			case COORDINATES:
				coords = entry.getValue().split(",");
				break;
			}
		}

		WayPoint waypoint = null;
		switch( style ){
		case POINT:
			waypoint = new WayPoint( DEFAULT_SPEED, style );
			waypoint.setLongtitude( Float.parseFloat( coords[0] ));
			waypoint.setLatitude( Float.parseFloat( coords[1] ));
			appendWayPoint( waypoint, append );
			results.add( waypoint );
			break;
		default:
			int i=0;
			while( i<coords.length ){
				waypoint = new WayPoint( DEFAULT_SPEED, style );
				waypoint.setLongtitude( Float.parseFloat( coords[i++] ));
				waypoint.setLatitude( Float.parseFloat( coords[i++] ));
				appendWayPoint( waypoint, append );
				results.add( waypoint );		
			}
			break;
		}
		return results.toArray( new WayPoint[ results.size() ]);
	}

	private void appendWayPoint( WayPoint waypoint, Append append ){
		this.lock.lock();
		try{
			if( this.waypoints.isEmpty())
				return;
			switch( append ){
			case ACTIVE:
				waypoint.setSpeed( waypoints.get( this.active ).getSpeed());
				break;
			case LAST:
				waypoint.setSpeed( waypoints.get( this.waypoints.size() - 1 ).getSpeed());
				break;
			default:
				waypoint.setSpeed( DEFAULT_SPEED );

			}					
		}
		finally{
			lock.unlock();
		}
	}

	protected boolean contains( WayPoint waypoint ){
		for( WayPoint wp: this.waypoints ){
			if( wp.compareTo( waypoint ) == 0 )
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		if( this.waypoints.isEmpty())
			return super.toString();
		String str = this.waypoints.get(0).toLongLat() + ":" + time;
		return str;
	}

	public static BoatModel newTrajectory( Boats boat){
		return new BoatModel( boat.toString());
	}
	
	public BoatModel createTrajectory(){
		this.lock.lock();
		try{
			if( this.waypoints.isEmpty())
				return null;
			Vector<WayPoint> wps = new Vector<WayPoint>( this.waypoints);
			this.waypoints.clear();
			return new BoatModel( id, this.boat, wps );
		}
		finally{
			lock.unlock();
		}
	}
	
	public int size(){
		return this.waypoints.size();
	}

	public static TrajectoryModel getTrajectory( String str ){
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
	            	//model = setModel(key, parser.getString());
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

    /*
    private static void setModel( Gps gps, String key, String value) {
        String str = StringStyler.styleToEnum(value);
        switch(key){
        case S_NAME:
            gps = new Gps( str );
            break;
        case Gps.Base.MODE.toString():
            gps.setMode( value );
        	break;
        case Gps.Compass.BEARING.toString():
            gps.getCompass().setMode( value );
        	break;
        default:
             logger.warning( S_WRN_UNKNOWN_KEY + key);   
        }
        return model;
    }
*/
}
