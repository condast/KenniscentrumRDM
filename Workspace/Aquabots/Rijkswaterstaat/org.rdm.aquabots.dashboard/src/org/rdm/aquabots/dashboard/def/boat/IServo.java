package org.rdm.aquabots.dashboard.def.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public interface IServo {

	/**
	 * Servo properties
	 * @author Kees
	 *
	 */
	public enum Servo{
		LEFT,
		RIGHT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValidStr( String key ){
			for( Servo servo: values() ){
				if( servo.name().equals( key ))
					return true;
			}
			return false;
		}
	}

	public abstract int getLeft();

	public abstract int getRight();
}