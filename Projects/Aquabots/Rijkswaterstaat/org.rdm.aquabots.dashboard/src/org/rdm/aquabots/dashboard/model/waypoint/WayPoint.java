package org.rdm.aquabots.dashboard.model.waypoint;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public class WayPoint implements Comparable<WayPoint>{
	
	public enum Attributes{
		ID,
		NAME,
		LATITUDE,
		LONGTITUDE,
		SPEED,
		EVENT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	public enum LonLat{
		LON,
		LAT;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	public enum WayPointEvents{
		APPENDED,
		INSERTED,
		OVERRIDDEN,
		REMOVED,
		STOPPED,
		RESTART;
	
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

	private long id;
	private float longtitude;
	private float latitude;	
	private int speed;
	boolean started;
	private WayPointEvents event; 
	private WayPoint.Styles style;

	public WayPoint( ) {
		this( 0, 0, 0, Styles.POINT);
	}
	
	public WayPoint( int speed, Styles style ) {
		this( 0, 0, speed, Styles.POINT);
		this.event = WayPointEvents.APPENDED;
	}

	public WayPoint( float longtitude, float latitude, Styles style) {
		this( longtitude, latitude, WayPointEvents.STOPPED, style );
	}

	public WayPoint( float longtitude, float latitude, WayPointEvents event, Styles style ) {
		this.longtitude = longtitude;
		this.latitude = latitude;	
		this.started = false;
		this.id = this.toString().hashCode();
		this.event = event;
		this.style = style;
	}

	public WayPoint( float longtitude, float latitude, float speed, Styles style ) {
		this( longtitude, latitude, style );
		this.event = WayPointEvents.STOPPED;
	}

	public long getID(){
		return this.id;
	}

	public WayPoint.Styles getStyle() {
		return style;
	}

	public boolean hasStarted(){
		return this.started;
	}
	
	public void activate(){
		this.started = true;
	}
	
	public WayPointEvents getEvent() {
		return event;
	}

	public void setEvent(WayPointEvents event) {
		this.event = event;
	}

	public float getLongtitude() {
		return longtitude;
	}
	public void setLongtitude( float longtitude) {
		this.longtitude = longtitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude( float latitude) {
		this.latitude = latitude;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String toLongLat() {
		return "[" + this.longtitude + ", " + this.latitude + "]" ;
	}

	@Override
	public String toString() {
		return this.toLongLat() + super.toString();
	}

	@Override
	public int compareTo(WayPoint arg0) {
		float mul = (float) Math.pow(10, 6);
		float diff = this.longtitude - arg0.getLongtitude();
		long comp = (long)( diff * mul );
		if( comp != 0 )
			return ( comp<0)? -1: 1;
		comp = (long)( this.latitude - arg0.getLatitude() ) * 10^6;
		return ( comp<0)? -1: 1;
	}
}