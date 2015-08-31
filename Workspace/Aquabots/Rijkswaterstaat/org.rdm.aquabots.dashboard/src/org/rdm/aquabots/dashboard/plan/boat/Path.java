package org.rdm.aquabots.dashboard.plan.boat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.json.stream.JsonParser;

import org.rdm.aquabots.dashboard.def.boat.IHelmControl.Helm;
import org.rdm.aquabots.dashboard.plan.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.structure.AbstractArrayStructure;
import org.rdm.aquabots.dashboard.structure.AbstractStatusStructure;
import org.rdm.aquabots.dashboard.structure.IStatusStructure;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class Path extends AbstractStatusStructure{
	
	public static final String S_WAYPOINTS = "Waypoints";
	
	public enum Attributes{
		LENGTH,
		CURRENTWP,
		WAYPOINTS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}

		public static boolean isValidStr( String key ){
			for( Attributes boat: values() ){
				if( boat.name().equals( key ))
					return true;
			}
			return false;
		}
	}
	
	private List<WayPoint> route;

	private static Logger logger = Logger.getLogger( AbstractStatusStructure.class.getName() );

	public Path( IStatusStructure parent ) {
		super( parent, Helm.PATH.toString() );
		this.route = new ArrayList<WayPoint>();
	}
	
	public void reset(){
		super.setAttribute( Attributes.LENGTH.toString(), 0);
		this.setAttribute( Attributes.CURRENTWP.toString(), 0);
	}
	
	public int getLength(){
		Object obj = super.getAttribute( Attributes.LENGTH.name());
		int length = route.size();
		int providedLength = ( obj == null ) ? 0: (Integer)obj;
		if( providedLength > 0 )
			length = providedLength;
		return length;
			
	}

	public int getIndex(){
		Object obj = super.getAttribute( Attributes.CURRENTWP.name());
		return ( obj == null ) ? 0: (Integer)obj;
	}

	public WayPoint getCurrentWP(){
		if( route.isEmpty())
			return null;
		return route.get( getIndex() );
	}
	
	/**
	 * Get the next waypoint, or null if the end of the routs is reached
	 * @return
	 */
	public WayPoint next(){
		int index = getIndex();
		return( index < route.size() )? route.get(++index): null;
	}
	
	/**
	 * Get the previousc waypoint, or null if the beginning of the route is reached
	 * @return
	 */
	public WayPoint previous(){
		int index = getIndex();
		return( index > 0 )? route.get(--index): null;
	}
	
	public void addWayPoint( WayPoint waypoint ){
		this.route.add( waypoint );
	}

	public void removeWayPoint( WayPoint waypoint ){
		this.route.remove( waypoint );
	}

	public WayPoint[] getWayPoints(){
		return route.toArray( new WayPoint[this.route.size()]);
	}

	@Override
	public void setAttribute(String key, Object value) {
		if(!Attributes.isValidStr(key))
			return;
		Attributes attribute = Attributes.valueOf( key );
		switch( attribute ){
		case LENGTH:
		case CURRENTWP:
			super.setAttribute(key, value);
			break;
		default:
			break;
		}
	}

	@Override
	public String printStructure(int tabs) {
		StringBuffer buffer = new StringBuffer();
		buffer.append( super.printStructure(tabs));
		for( WayPoint wp: this.route ){
			buffer.append( wp.printStructure(tabs+1));
		}
		return buffer.toString();
	}

	@Override
	public AbstractArrayStructure<?> createArray(String arrayName, JsonParser parser) {
		if( S_WAYPOINTS.equals( arrayName)){
			WayPointArray array = new WayPointArray( this.route );
			return array;
		}
		return super.createArray(arrayName, parser);
	}
	
	private static class WayPointArray extends AbstractArrayStructure<WayPoint>{

		private WayPoint waypoint;
		
		protected WayPointArray( Collection<WayPoint> route ) {
			super( S_WAYPOINTS, route );
		}

		@Override
		public WayPoint createElement(String key) {
			this.waypoint = new WayPoint();
			return this.waypoint;
		}

		@Override
		public boolean fillValue(WayPoint element, String key, JsonParser parser) {
			String enm = StringStyler.styleToEnum( key );
			if( !WayPoint.LonLat.isValid( enm ))
				return false;
			WayPoint.LonLat attr = WayPoint.LonLat.valueOf( enm );
			boolean retval = true;
			switch( attr ){
			case LON:
				this.waypoint.setLongtitude(Float.parseFloat( parser.getString() ));
				break;
			case LAT:
				this.waypoint.setLatitude(Float.parseFloat( parser.getString() ));
				break;
			default:
				logger.warning( S_WRN_INVALID_SEQUENCE + key );
				retval = false;
				break;	
			}
			return retval;
		}
	}	
}
