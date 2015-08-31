package org.rdm.aquabots.nmea.standard;

import org.rdm.aquabots.nmea.utils.StringStyler;
import org.rdm.aquabots.nmea.utils.Utils;

public interface INmeaDepthSentence {

	/**
	 * The GPS sentences
	 * @author Kees
	 *
	 */
	public enum NMEAGpsSequence{
		SDDBT,	//14.45,f,4.40,M,2.41,F*05
		SDDPT,	//4.40,-0.20,*54
		SDMTW,	//16.11,C*33
		SDVHW;	//,T,,M,8.75,N,16.20,K*7D
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( NMEAGpsSequence sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * DBT - Depth below transducer
  	* $--DBT,x.x,f,x.x,M,x.x,F*hh<CR><LF>
	 * @author Kees
	 *
	 */
	public enum SDDBT{
		FIELD_NUMBER, 
		DEPTHF, //feet
		FEET, //f = feet
		DEPTHM, //meters
		METERS, //M = meters
		DEPTHFA,//Fathoms
		FATHOMS; //F = Fathoms
		// A = Auto Locate, R = Unit Reset 	

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( SDDBT sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}		
	}

	/**
	 * Mean temperature of Water
	 * @author Kees
	 * @see http://www.catb.org/gpsd/NMEA.html
	 */
	public enum SDMTW{
		DEGREES,
		UNIT; //Ç' Celsius

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
	}

	/**
	 * Water speed and heading
	 * @author Kees
	 * @see http://www.catb.org/gpsd/NMEA.html
	 */
	public enum SDVHW{
		DEGRESS, //Degress True
		T,// = True
		DEGREES, //Degrees Magnetic
		M, // = Magnetic
		KNOTS,	// (speed of vessel relative to the water)
		N,  //= Knots
		KILOMETRERS, //ilometers (speed of vessel relative to the water)
		K;	// = Kilometers

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

	}
}