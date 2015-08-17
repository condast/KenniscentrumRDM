package org.rdm.aquabots.dashboard.model.boat;

import org.rdm.aquabots.dashboard.session.AbstractPushSession;

public class BoatSession extends AbstractPushSession {

	private BoatModel model;
	private boolean refresh = false;
	private IBoatListener listener = new IBoatListener() {

		@Override
		public void notifyTrajectoryChanged(BoatEvent event) {
			Thread.currentThread().interrupt();
			refresh = true;
		}
	};
	
	private static BoatSession session = new BoatSession();
		
	public static BoatSession getInstance(){
		return session;
	}	
	
	public BoatModel getModel() {
		return model;
	}

	public void setModel(BoatModel model) {
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
