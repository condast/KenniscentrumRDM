package org.rdm.aquabots.dashboard.model;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public class GeoView {

	public enum Location{
		IJSSEL,
		HEYPLAAT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public String toLonLat() {
			String str = "";
			switch( this ){
			case HEYPLAAT:
				str = "[" + HEY_LONGTITUDE + "," + HEY_LATITUDE + "]";
				break;
			case IJSSEL:
				str = "[" + DEF_LONGTITUDE + "," + DEF_LATITUDE + "]";
				break;
			}
			return str;
		}
		
		public static String[] getNames(){
			String[] results = new String[ values().length ];
			int index = 0;
			for( Location location: values()){
				results[index++] = location.toString();
			}
			return results;
		}
	}

	//Ijssel
	public static final float DEF_LONGTITUDE = 6.15388f;
	public static final float DEF_LATITUDE = 52.24967f;

	//Heyplaat
	public static final float HEY_LONGTITUDE = 4.421760f;
	public static final float HEY_LATITUDE = 51.8984489440918f;

	public static final int DEF_ZOOM = 17;
	
	public static final float DEF_HORIZONTAL = 0.001f;
	public static final float DEF_VERTICAL = 0.001f;
	
	private float longtitude;
	private float latitude;
	private int zoom;
	
	private static GeoView geoView = new GeoView();
	
	private GeoView() {
		super();
		zoom = DEF_ZOOM;
		this.longtitude = DEF_LONGTITUDE;
		this.latitude = DEF_LATITUDE;
		
	}
	
	public static GeoView getInstance(){
		return geoView;
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
		this.longtitude -= DEF_HORIZONTAL;
		return jump();
	}

	public String right(){
		this.longtitude += DEF_HORIZONTAL;
		if( this.longtitude < 0 )
			this.longtitude = 0;
		return jump();
	}

	public String zoom(){
		return "zoom(" + this.zoom + ");";
	}

	public String zoomout() {
		this.zoom ++;
		return zoom();
	}

	public String zoomin() {
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