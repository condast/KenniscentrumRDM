package org.rdm.aquabots.dashboard.model;

import java.util.EventObject;

public class TrajectoryEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private WayPoint waypoint;
	
	public TrajectoryEvent(Object arg0, WayPoint waypoint ) {
		super(arg0);
		this.waypoint = waypoint;
	}

	public WayPoint getWayPoint() {
		return waypoint;
	}

	
}
