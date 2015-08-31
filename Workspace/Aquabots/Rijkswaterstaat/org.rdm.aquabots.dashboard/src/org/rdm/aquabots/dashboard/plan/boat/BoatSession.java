package org.rdm.aquabots.dashboard.plan.boat;

import org.rdm.aquabots.dashboard.active.boat.CurrentBoat;
import org.rdm.aquabots.dashboard.active.boat.CurrentBoatEvent;
import org.rdm.aquabots.dashboard.active.boat.ICurrentBoatListener;
import org.rdm.aquabots.dashboard.session.AbstractPushSession;

public class BoatSession extends AbstractPushSession {

	private CurrentBoat model;
	private boolean refresh = false;
	private ICurrentBoatListener listener = new ICurrentBoatListener() {

		@Override
		public void notifyStatusChanged(CurrentBoatEvent event) {
			if( CurrentBoatEvents.SET_MODEL.equals( event.getEvent() )){
				refresh = true;
				Thread.currentThread().interrupt();
			}
		}
	};
	
	private static BoatSession session = new BoatSession();
		
	public static BoatSession getInstance(){
		return session;
	}	
	
	public void setModel( CurrentBoat model) {
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
