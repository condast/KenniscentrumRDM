package org.rdm.aquabots.dashboard.active.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public interface ICurrentBoatListener {

	public enum CurrentBoatEvents{
		TRAJECTORY_CHANGE,
		SET_MODEL;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}		
	}
	public void notifyStatusChanged( CurrentBoatEvent event );
}
