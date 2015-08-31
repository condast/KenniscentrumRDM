package org.rdm.aquabots.dashboard.def.boat;

import org.rdm.aquabots.dashboard.plan.boat.Path;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public interface IHelmControl {

	/**
	 * Boat configuration
	 * @author Kees
	 *
	 */
	public enum Helm{
		GPS,
		COMPASS,
		PATH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValidStr( String key ){
			for( Helm helm: values() ){
				if( helm.name().equals( key ))
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
	public enum Gps{
		FIX;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValidStr( String key ){
			for( Gps gps: values() ){
				if( gps.name().equals( key ))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * Get the GPS position of the boat
	 * @return
	 */
	public IGps getGPS();
	
	/**
	 * Get the compass bearings
	 * @return
	 */
	public ICompass getCompass();
	
	/**
	 * Get the path 
	 * @return
	 */
	public Path getPath();
}