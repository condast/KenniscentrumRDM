package org.rdm.aquabots.dashboard.bathymetry;

import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint.Styles;

public class DepthMap {

	private float LAT_TOP_LEFT = 6.154749870300293f;
	private float LON_TOP_LEFT = 52.250240325927734f;

	private float LAT_BOTTOM_RIGHT = 6.154749870300293f;
	private float LON_BOTTOM_RIGHTT = 52.250240325927734f;

	private static int DEF_HORIZONTAL = 6000;
	private static int DEF_VERTICAL = 3000;

	private int maxDepth = 50000;//5 m

	private WayPoint topLeft, bottomRight;
	
	private int hor_pixels = 6000;
	private int ver_pixels = 3000;
	
	private DepthWayPoint[][] survey;
	
	private static DepthMap depthMap = new DepthMap( DEF_HORIZONTAL, DEF_VERTICAL );
	
	private DepthMap( int horizontal, int vertical ) {
		super();
		survey = new DepthWayPoint[ horizontal][ vertical];
		topLeft=  new WayPoint( LON_TOP_LEFT, LAT_TOP_LEFT, Styles.POINT );
		bottomRight = new WayPoint( LON_BOTTOM_RIGHTT, LAT_BOTTOM_RIGHT, Styles.POINT );
	}
	
	public void init(){
		this.fillBaseLayer();
		this.fillRandom( 1000, 0);
		this.fade( 1 );
		
	}
	
	public static DepthMap getInstance(){
		return depthMap;
	}
	
	protected void fillBaseLayer(){
		float step_lon = bottomRight.getLongtitude() - topLeft.getLongtitude();
		float step_lat = topLeft.getLatitude() - bottomRight.getLatitude();
		for( int i=0; i < hor_pixels; i++ ){
			for( int j=0; i < ( hor_pixels/2); j++ ){
			   float lon = topLeft.getLongtitude() + step_lon*j;
			   float lat = topLeft.getLatitude() + step_lat*j;
			   int depth = j*j;
			   depth = ( depth > maxDepth )? maxDepth: depth; 
			   survey[i][j] = new DepthWayPoint( lon, lat, j*j);
			   survey[i][hor_pixels -j] = new DepthWayPoint( lon, lat, j*j );
			}
		}
	}

	protected void fillRandom( int range, int offset ){
		for( int i=0; i < hor_pixels; i++ ){
			for( int j=offset; i < ( hor_pixels/2); j++ ){
			   int depth = survey[i][j].getDepth();
			   depth = ( depth > maxDepth )? maxDepth: depth; 
			   survey[i][j].depth = (int) (depth + offset * ( 0.5- Math.random()));
			}
		}
	}

	protected void fade( int multiplier ){
		for( int i=0; i < hor_pixels; i++ ){
			for( int j=0; i < ( hor_pixels/2); j++ ){
			   int a = i-1;
			   int b = j -1;
			   int depth = 0;
			   while( a>=0 ){
				   while( b>=0 ){
					 if(( a == 0 ) && ( b ==0 ))
						 continue;
					 depth += survey[a][b].getDepth();
				   }
			   }
			   survey[i][j].setDepth((int)(multiplier * depth/9));
			}
		}
	}

	private static class DepthWayPoint extends WayPoint{
		
		private int depth;

		private DepthWayPoint(float longtitude, float latitude, int depth ) {
			super(longtitude, latitude, Styles.POINT);
		}
		
		private DepthWayPoint(float longtitude, float latitude ) {
			this(longtitude, latitude, 0);
		}

		int getDepth() {
			return depth;
		}

		void setDepth(int depth) {
			this.depth = depth;
		}
	}
	
	public static float measure( WayPoint waypoint1, WayPoint waypoint2){ 
	    float R = 6378.137f; // Radius of earth in KM
	    float dLat = (float) ((waypoint2.getLatitude() - waypoint1.getLatitude()) * Math.PI / 180);
	    double dLon = (waypoint2.getLongtitude() - waypoint1.getLongtitude()) * Math.PI / 180;
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos( waypoint1.getLatitude() * Math.PI / 180) * Math.cos(waypoint2.getLatitude() * Math.PI / 180) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double d = R * c;
	    return (float) (d * 1000); // meters
	}
}
