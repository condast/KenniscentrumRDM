package org.rdm.aquabots.nmea.standard;

import org.rdm.aquabots.nmea.utils.StringStyler;
import org.rdm.aquabots.nmea.utils.Utils;

public interface INmeaGpsSentence {

	/**
	 * The GPS sentences
	 * @author Kees
	 *
	 */
	public enum NMEAGpsCommand{
		GPGGA,//192817,2545.5634,N,08010.4256,W,8,11,2.00,0.00,M,0.00,M,,*51
		GPGLL,//2545.5634,N,08010.4256,W,192817,V,S*5A
		HCHDM;//,M*07

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( NMEAGpsCommand sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * Global Positioning System Fix Data 
	 * @see http://aprs.gids.nl/nmea/#gga
	 * @author Kees
	 *
	 */
	public enum GPGGA{
		TIME,       		//170834 	17:08:34 Z
		LATITUDE,   		//4124.8963, N 	41d 24.8963' N or 41d 24' 54" N
		LONGTITUDE, 		//08151.6838, W 	81d 51.6838' W or 81d 51' 41" W
		FIX_QUALITY, 		//		- 0 = Invalid	- 1 = GPS fix - 2 = DGPS fix 	1 	Data is from a GPS fix
		NR_OF_SATELLITES, 	// 	05 	5 Satellites are in view
		HOR_DILUTION_OF_PRECISION, //(HDOP) 	1.5 	Relative accuracy of horizontal position
		ALTITUDE, 			// 	280.2, M 	280.2 meters above mean sea level
		HEIGHT_OF_GEOID_ABOVE_WGS84, // ellipsoid 	-34.0, M 	-34.0 meters
		TIME_SINCE_LAST_DGPS_UPDATE, //	blank 	No last update
		DGPS_REFERENCE_STATION_ID;			 //*75 	Used by program to check for transmission errors
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( GPGGA sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}		
	}

	/**
	 * Geographic Position, Latitude / Longitude and time.  
	 * @see http://aprs.gids.nl/nmea/#gga
	 * @author Kees
	 *
	 */
	public enum GPGLL{
		LATITUDE, 					//ddmm.mmm format (leading zeros must be transmitted) 
		LATITUDE_HEMISPHERE,    	//Latitude hemisphere, N or S 
		LONGTITUDE, 		    	// dddmm.mmm format (leading zeros must be transmitted) 
		LONGITUDE_HEMISPHERE; 		//E or W 
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( GPGLL sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}		
	}

	/**
	 * where "HC" specifies the talker as being a magnetic compass, 
	 * @author Kees
	 * the "HDM" specifies the magnetic heading message follows. 
	 * The "238" is the heading value, 
	 * and "M" designates the heading value as magnetic
	 * 
	 * @see: http://www.nuovamarea.net/blog/general-sentence-format-of-nmea-0183
	 **/
	public enum HCHDM{
		HEADING_VALUE,
		HEADING_TYPE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
}