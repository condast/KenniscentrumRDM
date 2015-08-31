package org.rdm.aquabots.dashboard.def.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public interface IBoatConfiguration {

	/**
	 * Boat configuration
	 * @author Kees
	 *
	 */
	public enum Configuration{
		SENSORS,
		ACTUATORS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
}
