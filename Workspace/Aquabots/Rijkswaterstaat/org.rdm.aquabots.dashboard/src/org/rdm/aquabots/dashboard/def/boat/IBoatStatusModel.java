package org.rdm.aquabots.dashboard.def.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public interface IBoatStatusModel {

	/**
	 * The characteristics of a boat
	 * @author Kees
	 *
	 */
	public enum Boat{
		NAME,
		CONFIGURATION,
		CONTROLLER,
		HELM;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		public static boolean isValidStr( String key ){
			for( Boat boat: values() ){
				if( boat.name().equals( key ))
					return true;
			}
			return false;
		}
	}

	/**
	 * The characteristics of a boat
	 * @author Kees
	 *
	 */
	public enum BoatControl{
		MOTOR_CONTROL,
		SERVO;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		public static boolean isValidStr( String key ){
			for( BoatControl boat: values() ){
				if( boat.name().equals( key ))
					return true;
			}
			return false;
		}
	}

	/**
	 * Get the name of the boat
	 * @return
	*/
	public String getName();
	
	/**
	 * The helm control contains all the properties of the boat's position and heading
	 * @return
	 */
	public IHelmControl getHelmControl();
	
	/**
	 * The Boat control contains all the properties of the boat'motor and serve control
	 * @return
	 */
	public IBoatController getBoatControl();
}
