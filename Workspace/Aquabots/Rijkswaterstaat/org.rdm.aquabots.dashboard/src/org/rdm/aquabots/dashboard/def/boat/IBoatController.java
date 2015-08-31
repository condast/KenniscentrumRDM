package org.rdm.aquabots.dashboard.def.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public interface IBoatController {

	/**
	 * Boat Controller
	 * @author Kees
	 *
	 */
	public enum Attributes{
		SERVO,
		MOTOR_CONTROLLER;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValidStr( String key ){
			for( Attributes props: values() ){
				if( props.name().equals( key ))
					return true;
			}
			return false;
		}		
	
	}

	/**
	 * Motor control properties
	 * @author Kees
	 *
	 */
	public enum MotorController{
		TYPE,
		LEFT,
		RIGHT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * Motor control types
	 * @author Kees
	 *
	 */
	public enum MotorTypes{
		SABERTOOTH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * Get the servo data for this boat
	 * @return
	 */
	public IServo getServo();
	
	/**
	 * Get the actual motor controller for this boat
	 * @return
	 */
	public IMotorController getMotorController();
	
	/**
	 * Get the supported types
	 * @return
	 */
	public MotorTypes[] getSupportedTypes();
}
