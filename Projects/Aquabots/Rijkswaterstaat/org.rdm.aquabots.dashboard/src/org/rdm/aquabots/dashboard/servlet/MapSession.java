package org.rdm.aquabots.dashboard.servlet;

import org.rdm.aquabots.dashboard.active.boat.CurrentBoat;
import org.rdm.aquabots.dashboard.active.boat.CurrentBoatEvent;
import org.rdm.aquabots.dashboard.active.boat.ICurrentBoatListener;
import org.rdm.aquabots.dashboard.session.AbstractPushSession;

public class MapSession extends AbstractPushSession {

	private CurrentBoat model;
	private boolean refresh = false;
	private ICurrentBoatListener listener = new ICurrentBoatListener() {

		@Override
		public void notifyStatusChanged(CurrentBoatEvent event) {
			Thread.interrupted();
			refresh = true;
		}
	};
	
	private static MapSession session = new MapSession();
		
	public static MapSession getInstance(){
		return session;
	}	
	
	public void setModel(CurrentBoat model) {
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
