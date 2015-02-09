package org.rdm.aquabots.dashboard.model.boat;

import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class Path {
	
	public enum Attributes{
		LENGTH,
		CURRENTWP,
		WAYPOINTS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	private TrajectoryModel trajectory;
	
	public Path( TrajectoryModel trajectory ) {
		super();
		this.trajectory = trajectory;
	}
	
	public int getLength(){
		return this.trajectory.size();
	}
	
	public int getCurrentWP(){
		return this.trajectory.getActiveIndex();
	}
	
	public WayPoint[] getWayPoints(){
		return this.trajectory.getWayPoints();
	}
	
}
