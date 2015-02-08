package org.rdm.aquabots.dashboard.model;

public interface ITrajectoryListener {

	public void notifyTrajectoryChanged( TrajectoryEvent event );
}
