package org.rdm.aquabots.dashboard.def.boat;

import java.net.URL;

import org.rdm.aquabots.dashboard.plan.TrajectoryModel;
import org.rdm.aquabots.dashboard.plan.boat.StatusModel;
import org.rdm.aquabots.dashboard.plan.waypoint.WayPoint;

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

	public TrajectoryModel createTrajectory();

	/**
	 * Get the actual status of the boat
	 * @return
	 */
	public StatusModel getStatus();
	
	/**
	 * Set the actual status of the boat 
	 * @param model
	 */
	public void setStatus( StatusModel model );
}
