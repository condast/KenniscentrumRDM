package org.rdm.aquabots.dashboard.servlet;

import org.rdm.aquabots.dashboard.model.ITrajectoryListener;
import org.rdm.aquabots.dashboard.model.TrajectoryEvent;
import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.session.AbstractPushSession;

public class MapSession extends AbstractPushSession {

	private TrajectoryModel model;
	private boolean refresh = false;
	private ITrajectoryListener listener = new ITrajectoryListener() {

		@Override
		public synchronized void notifyTrajectoryChanged( TrajectoryEvent event) {
			Thread.currentThread().interrupt();
			refresh = true;
		}
	};
	
	private static MapSession session = new MapSession();
		
	public static MapSession getInstance(){
		return session;
	}	
	
	public TrajectoryModel getModel() {
		return model;
	}

	public void setModel(TrajectoryModel model) {
		this.model = model;
		this.model.addListener(listener);
	}


	@Override
	protected boolean runSession() {
		if( this.model == null )
			return false;
		return this.refresh;
	}

	
	@Override
	public void start() {
		this.refresh = false;
		super.start();
	}

	@Override
	public void stop() {
		this.refresh = false;
		if( this.model != null )
			this.model.removeListener(listener);
		super.stop();
	}
}
