package org.rdm.aquabots.dashboard.model;

import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class TrajectoryView {
	private WayPoint.Styles type = WayPoint.Styles.POINT;

	private GeoView geoView;
	
	
	public TrajectoryView( GeoView geoView) {
		super();
		this.geoView = geoView;
	}

	public WayPoint.Styles getType() {
		return type;
	}

	public void setType(WayPoint.Styles type) {
		this.type = type;
	}

	public void setType( String type) {
		this.type = WayPoint.Styles.valueOf( StringStyler.styleToEnum( type ));
	}

	public String setTrajectory(){
		return "typeSelect('" + this.type.toString() + "');";
	}

	private int x = 1, y = 1;
	public String addPoint(){
		float gx = this.geoView.getLongtitude() + x/100;
		float gy = this.geoView.getLongtitude() + y/100;
		x++; y++;
		return "addPoint(" + gx + "," + gy + ");";
	}

}
