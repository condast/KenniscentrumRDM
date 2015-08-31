package org.rdm.aquabots.dashboard.active.boat;

import java.util.EventObject;

import org.rdm.aquabots.dashboard.active.boat.ICurrentBoatListener.CurrentBoatEvents;
import org.rdm.aquabots.dashboard.plan.waypoint.WayPoint;

public class CurrentBoatEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private WayPoint waypoint;
	private CurrentBoatEvents event;

	public CurrentBoatEvent(CurrentBoatEvents event, Object arg0 ) {
		this(event, arg0, null );
	}

	public CurrentBoatEvent( CurrentBoatEvents event, Object arg0, WayPoint waypoint ) {
		super(arg0);
		this.waypoint = waypoint;
		this.event = event;
	}

	public CurrentBoatEvents getEvent() {
		return event;
	}

	public WayPoint getWayPoint() {
		return waypoint;
	}
}
