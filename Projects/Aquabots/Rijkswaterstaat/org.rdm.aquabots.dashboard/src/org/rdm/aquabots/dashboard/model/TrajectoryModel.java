package org.rdm.aquabots.dashboard.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public class TrajectoryModel {

	public static final int DEFAULT_SPEED = 50;
	public enum Parameters{
		TYPE,
		STYLE,
		COORDINATES;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	public enum Styles{
		POINT,
		LINE_STRING,
		POLYGON;
	
		public static String[] getTypes(){
			String[] strarray = new String[3];
			int index = 0;
			for( Styles type: values() ){
				strarray[index++] = type.toString();
			}
			return strarray;
		}
	
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

	public enum Types{
		DRAWSTART,
		DRAWEND;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	private Vector<WayPoint> waypoints;
	private int active;

	private Collection<ITrajectoryListener> listeners;
	
	private static TrajectoryModel model = new TrajectoryModel();

	private TrajectoryModel() {
		waypoints = new Vector<WayPoint>();
		this.active = 0;
		this.listeners = new ArrayList<ITrajectoryListener>();
	}
	
	public static TrajectoryModel getInstance(){
		return model;
	}
	
	public void addListener( ITrajectoryListener listener ){
		this.listeners.add( listener );
	}
	
	public void removeListener( ITrajectoryListener listener ){
		this.listeners.remove( listener );
	}

	protected void notifyListeners( WayPoint waypoint ){
		for( ITrajectoryListener listener: this.listeners ){
			listener.notifyTrajectoryChanged( new TrajectoryEvent( this, waypoint ));
		}
	}

	public void addWayPoint( WayPoint waypoint ){
		this.waypoints.add( waypoint );
		waypoint.setEvent( WayPoint.WayPointEvents.APPENDED );
		this.notifyListeners( waypoint );
	}

	public void insertWayPoint( WayPoint waypoint ){
		this.waypoints.insertElementAt( waypoint, this.active);
		waypoint.setEvent( WayPoint.WayPointEvents.INSERTED );
		this.notifyListeners( waypoint );
	}

	public void overrideWayPoint( WayPoint waypoint ){
		while( this.waypoints.size() >= this.active )
			this.waypoints.remove(this.active);
			
		this.waypoints.add( waypoint );
		waypoint.setEvent( WayPoint.WayPointEvents.OVERRIDDEN );
		this.notifyListeners( waypoint );
	}

	public void removeWayPoint( WayPoint waypoint ){
		this.waypoints.add( waypoint );
		waypoint.setEvent( WayPoint.WayPointEvents.REMOVED );
		this.notifyListeners( waypoint );
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
		
		Types type = null;
		
		Styles style = null;
		String[] coords = null;
		while( iterator.hasNext() ){
			Map.Entry<String, String> entry = iterator.next();
			Parameters param = Parameters.valueOf( StringStyler.styleToEnum( entry.getKey()));
			switch( param ){
			case TYPE:
				type = Types.valueOf( StringStyler.styleToEnum( entry.getValue() ));
				break;
			case STYLE:
				style = Styles.valueOf( StringStyler.styleToEnum( entry.getValue() ));
				break;
			case COORDINATES:
				coords = entry.getValue().split(",");
				break;
			}
		}

		WayPoint waypoint = null;
		switch( style ){
		case POINT:
			waypoint = new WayPoint( DEFAULT_SPEED );
			waypoint.setLongtitude( Float.parseFloat( coords[0] ));
			waypoint.setLatitude( Float.parseFloat( coords[1] ));
			appendWayPoint( waypoint, append );
			results.add( waypoint );
			break;
		default:
			int i=0;
			while( i<coords.length ){
				waypoint = new WayPoint( DEFAULT_SPEED );
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
}
