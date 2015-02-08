package org.rdm.aquabots.dashboard.model;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public class WayPoint {
	
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

	private long id;
	private float longtitude;
	private float latitude;	
	private int speed;
	boolean started;
	private WayPointEvents event; 

	public WayPoint( ) {
		this( 0, 0, 0);
	}
	
	public WayPoint( int speed ) {
		this( 0, 0, speed);
		this.event = WayPointEvents.APPENDED;
	}

	public WayPoint( float longtitude, float latitude) {
		this( longtitude, latitude, WayPointEvents.STOPPED );
	}

	public WayPoint( float longtitude, float latitude, WayPointEvents event ) {
		this.longtitude = longtitude;
		this.latitude = latitude;	
		this.started = false;
		this.id = this.toString().hashCode();
		this.event = event;
	}

	public WayPoint( float longtitude, float latitude, float speed ) {
		this( longtitude, latitude );
		this.event = WayPointEvents.STOPPED;
	}

	public long getID(){
		return this.id;
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

	void setEvent(WayPointEvents event) {
		this.event = event;
	}

	public float getLongtitude() {
		return longtitude;
	}
	public void setLongtitude( float longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
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
}