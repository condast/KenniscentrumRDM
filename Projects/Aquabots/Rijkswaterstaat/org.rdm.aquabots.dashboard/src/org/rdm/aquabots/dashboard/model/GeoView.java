package org.rdm.aquabots.dashboard.model;

public class GeoView {

	public static final double DEF_LONGTITUDE = 6.15388;
	public static final double DEF_LATITUDE = 52.24967;
	public static final int DEF_ZOOM = 16;
	
	public static final float DEF_HORIZONTAL = 0.005f;
	public static final float DEF_VERTICAL = 0.02f;
	
	private double longtitude;
	private double latitude;
	private int zoom;
	
	
	public GeoView() {
		super();
		zoom = DEF_ZOOM;
		this.longtitude = DEF_LONGTITUDE;
		this.latitude = DEF_LATITUDE;
		
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude( double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude( double latitude) {
		this.latitude = latitude;
	}
	public int getZoom() {
		return zoom;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public String up(){
		this.latitude += DEF_VERTICAL;
		return jump();
	}
	
	public String down(){
		this.latitude -= DEF_VERTICAL;
		if( this.latitude < 0 )
			this.latitude = 0;
		return jump();
	}

	public String left(){
		this.longtitude += DEF_HORIZONTAL;
		return jump();
	}

	public String right(){
		this.longtitude -= DEF_HORIZONTAL;
		if( this.longtitude < 0 )
			this.longtitude = 0;
		return jump();
	}

	public String zoom(){
		return "zoom(" + this.zoom + ");";
	}

	public String zoomin() {
		this.zoom ++;
		return zoom();
	}

	public String zoomout() {
		if( this.zoom > 0)
			this.zoom--;
		return zoom();
	}

	public String init(){
		return jump() + zoom();
	}

	public String jump(){
		return "jump(" + this.longtitude + ", " + this.latitude + ");";
	}
}
