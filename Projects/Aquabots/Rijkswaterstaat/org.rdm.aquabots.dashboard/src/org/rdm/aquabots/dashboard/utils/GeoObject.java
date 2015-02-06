package org.rdm.aquabots.dashboard.utils;

public class GeoObject {

	public static final int DEF_LONGTITUDE = 50;
	public static final int DEF_LATITUDE = 15;
	public static final int DEF_ZOOM = 16;
	
	private int longtitude;
	private int latitude;
	private int zoom;
	
	
	public GeoObject() {
		super();
		zoom = DEF_ZOOM;
		this.longtitude = DEF_LONGTITUDE;
		this.latitude = DEF_LATITUDE;
		
	}
	public int getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(int longtitude) {
		this.longtitude = longtitude;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getZoom() {
		return zoom;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	public String jump(){
		return "jump(" + this.longtitude + ", " + this.latitude + ");";
	}
}
