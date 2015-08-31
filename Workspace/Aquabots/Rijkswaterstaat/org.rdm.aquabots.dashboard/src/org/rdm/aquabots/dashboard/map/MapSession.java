package org.rdm.aquabots.dashboard.map;

import org.rdm.aquabots.dashboard.active.boat.CurrentBoat;
import org.rdm.aquabots.dashboard.active.boat.CurrentBoatEvent;
import org.rdm.aquabots.dashboard.active.boat.ICurrentBoatListener;
import org.rdm.aquabots.dashboard.session.AbstractPushSession;

public class MapSession extends AbstractPushSession {

	private CurrentBoat model;
	private ICurrentBoatListener listener = new ICurrentBoatListener() {

		@Override
		public void notifyStatusChanged(CurrentBoatEvent event) {
			if( CurrentBoatEvents.TRAJECTORY_CHANGE.equals( event.getEvent() )){
				setRefresh( true );
				Thread.interrupted();
			}
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
		return ( this.model != null );
	}

	@Override
	public void stop() {
		if( this.model != null )
			this.model.removeListener(listener);
		super.stop();
	}
}