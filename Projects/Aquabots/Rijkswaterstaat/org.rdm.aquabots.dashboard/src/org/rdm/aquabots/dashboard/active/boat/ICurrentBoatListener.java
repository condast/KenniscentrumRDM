package org.rdm.aquabots.dashboard.active.boat;

public interface ICurrentBoatListener {

	public void notifyStatusChanged( CurrentBoatEvent event );
}
