package org.rdm.aquabots.dashboard.plan;

public interface ITrajectoryListener {

	public void notifyTrajectoryChanged( TrajectoryEvent event );
}
