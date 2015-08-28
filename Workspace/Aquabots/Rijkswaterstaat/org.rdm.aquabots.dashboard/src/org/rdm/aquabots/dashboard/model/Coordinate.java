package org.rdm.aquabots.dashboard.model;

public class Coordinate {

	private float longitude;
	private float latitude;
	
	
	protected Coordinate( float longitude, float latitude ) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	
	
}
