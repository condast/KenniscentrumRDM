package org.rdm.aquabots.dashboard.def.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public interface ICompass {

	/**
	 * Servo properties
	 * @author Kees
	 *
	 */
	public enum Compass{
		BEARING,
		ROLL,
		PITCH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValidStr( String key ){
			for( Compass compass: values() ){
				if( compass.name().equals( key ))
					return true;
			}
			return false;
		}
	}

	public abstract int getBearing();

	public abstract int getRoll();

	public abstract int getPitch();

}