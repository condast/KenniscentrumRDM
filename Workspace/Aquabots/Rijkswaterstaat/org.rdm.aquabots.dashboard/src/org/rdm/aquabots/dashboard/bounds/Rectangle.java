package org.rdm.aquabots.dashboard.bounds;

public class Rectangle {

	private Coordinate top, bottom;

	public Rectangle( Coordinate top, Coordinate bottom) {
		this.top = top;
		this.bottom = bottom;
	}

	public Rectangle( float lon1, float lon2, float lat1, float lat2) {
		float north = ( lon1 > lon2 ) ? lon1: lon2;
		float south = ( lon1 > lon2 ) ? lon2: lon1;
		float west = ( lat1 > lat2 ) ? lat1: lat2;
		float east = ( lat1 > lat2 ) ? lat2: lat1;
		this.top = new Coordinate( north, west );
		this.bottom = new Coordinate( south, east );;
	}

	public Coordinate getTop() {
		return top;
	}

	public Coordinate getBottom() {
		return bottom;
	}	
	
	public float getNorth(){
		return top.getLongitude();
	}

	public float getSouth(){
		return bottom.getLongitude();
	}
	
	public float getWest(){
		return ( top.getLatitude() > bottom.getLatitude() ) ? bottom.getLatitude(): top.getLatitude();
	}

	public float getEast(){
		return ( top.getLatitude() > bottom.getLatitude() ) ? top.getLatitude(): bottom.getLatitude();
	}

	@Override
	public String toString() {
		return "(" + getNorth() + ", " + getSouth() + ", " + getWest() + ", "+ getEast() + ")";
	}
}
