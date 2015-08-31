package org.rdm.aquabots.dashboard.def.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public interface IMotorController {

	public enum MCTypes{
		SABERTOOTH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValidStr( String key ){
			for( MCTypes mc: values() ){
				if( mc.name().equals( key ))
					return true;
			}
			return false;
		}

	}
	
	/**
	 * Servo properties
	 * @author Kees
	 *
	 */
	public enum MotorControl{
		TYPE,
		LEFT,
		RIGHT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValidStr( String key ){
			for( MotorControl mc: values() ){
				if( mc.name().equals( key ))
					return true;
			}
			return false;
		}
	}

	public abstract String getType();

	public abstract int getLeft();

	public abstract int getRight();
}