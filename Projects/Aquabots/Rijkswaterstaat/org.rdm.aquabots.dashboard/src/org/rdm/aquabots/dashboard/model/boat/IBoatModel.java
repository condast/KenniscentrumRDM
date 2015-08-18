package org.rdm.aquabots.dashboard.model.boat;

import java.net.URL;

import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;

public interface IBoatModel {

	
	/**
	 * Get the name of the boat
	 * @return
	 */
	public String getName();
	
	/**
	 * Get the URL for communication with the boat
	 * @return
	 */
	public URL getURL();
	
	/**
	 * Get the plotted trajectory of the boat
	 * @return
	 */
	public TrajectoryModel getTrajectory();
	
	public WayPoint[] getTrip();

	TrajectoryModel createTrajectory();
}
