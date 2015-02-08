package org.rdm.aquabots.dashboard.model;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public class TrajectoryView {
	private TrajectoryModel.Styles type = TrajectoryModel.Styles.POINT;

	private GeoView geoView;
	
	
	public TrajectoryView( GeoView geoView) {
		super();
		this.geoView = geoView;
	}

	public TrajectoryModel.Styles getType() {
		return type;
	}

	public void setType(TrajectoryModel.Styles type) {
		this.type = type;
	}

	public void setType( String type) {
		this.type = TrajectoryModel.Styles.valueOf( StringStyler.styleToEnum( type ));
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
