package org.rdm.aquabots.nmea.garmin;

import org.rdm.aquabots.nmea.utils.StringStyler;
import org.rdm.aquabots.nmea.utils.Utils;

public interface IGarminSentence {

	public enum GarminCommands{
		GPRMC,
		GPGGA,
		GPGSA,
		GPGSV,
		PGRME,
		GPGLL,
		GPVTG,
		PGRMI,
		PGRMV,
		PGRMF,
		PGRMB,
		PGRMT,
		PGRMZ;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( GarminCommands sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}
	}

	public enum Hemisphere{
		NORTH,
		SOUTH,
		EAST,
		WEST;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static Hemisphere getHemisphere( String str ){
			String hem = StringStyler.styleToEnum( str );
			if( str.length() == 1 ){
				if( "N".equals( hem))
					return Hemisphere.NORTH;
				if( "S".equals( hem))
					return Hemisphere.SOUTH;
				if( "W".equals( hem))
					return Hemisphere.WEST;
				if( "E".equals( hem))
					return Hemisphere.EAST;
			}
			return Hemisphere.valueOf( hem );
		}
	}

	public enum FixType{
		NONE,
		TWO_D,
		THREE_D;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static FixType getFixType( String str ){
			String hem = StringStyler.styleToEnum( str );
			if( str.length() == 2 ){
				if( "2D".equals( hem))
					return FixType.TWO_D;
				if( "3D".equals( hem))
					return FixType.THREE_D;
				return FixType.valueOf( hem );
			}
			return FixType.valueOf( str );
		}
	}
	//2.2.2	Estimated Error Information (PGRME) 
	public enum PGRME{ 
		EST_HOR_POS_ERROR, //Estimated horizontal position error (HPE), 0.0 to 999.9 meters 
		EST_VER_POS_ERROR, //Estimated vertical position error (VPE), 0.0 to 999.9 meters 
		EST_POS_ERR; 	   //Estimated position error (EPE), 0.0 to 999.9 meters 

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( PGRME sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}		
	}
	
	//GPS Fix Data Sentence (PGRMF)
	public enum PGRMF
	{
		GPS_WEEK,					 // (0 - 1023) 
		GPS_SECONDS, 				 // seconds (0 - 604799) 
		UTC_DATE_OF_POSITION_FIX, 	//ddmmyy format 
	    UTC_TIME_OF_POSITION_FIX, 	//hhmmss format 
	    GPS_LEAP_SECOND_COUNT, 		// leap second count 
		LATITUDE, 					//ddmm.mmm format (leading zeros must be transmitted) 
		LATITUDE_HEMISPHERE,    	//Latitude hemisphere, N or S 
		LONGTITUDE, 		    	// dddmm.mmm format (leading zeros must be transmitted) 
		LONGITUDE_HEMISPHERE, 		//E or W 
		MODE, 						//M = manual, A = automatic 
		FIX_TYPE, 					//0 = no fix, 1 = 2D fix, 2 = 3D fix 
		SPEED_OVER_GROUND, 			// 0 to 1051 kilometers/hour 
		COURSE_OVER_GROUD, 			//Course over ground, 0 to 359 degrees, true 
		POSITION_DILUTION_OF_PRECISION, // 0 to 9 (rounded to nearest integer value) 
		TIME_DILUTION_OF_PRECISTION; // 0 to 9 (rounded to nearest integer value) 

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( PGRMF sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}
	}
	
	//2.2.4	Aviation Height and VNAV Data (PGRMH) 
	public enum PGRMH{
		DATA_STATUS, //A = data valid, v = data unusable 
	    VERTICAL_SPEED, //feet per minute: negative = down, positive = up 
	    VNAV_PROFILE_ERROR, //feet: -999 ft (below VNAV profile) to 999 ft (above VNAV profile) 
	    VERTICAL_SPEED_TO_VNAV_TARGET, //feet per minute: negative = down, positive = up 
	    VERTICAL_SPEED_TO_NEXT_WAYPOINT, //feet per minute: negative = down, positive = up 
	    APPROXIMATE_HEIGHT_ABOVE_TERRAIN, //feet (rounded to next lowest 100 feet) 
	    DESIRED_TRACK, //degrees true 
	    COURSE_OF_NEXT_ROUTE_AFTER_ACTIVE_WAYPOINT; //degrees true 
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( PGRMH sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}
	}

	public enum PGRMI{
		LATITUDE, 				//ddmm.mmm format (leading zeros must be transmitted) 
		LATITUDE_HEMISPHERE,    //Latitude hemisphere, N or S 
		LONGTITUDE, 		    // dddmm.mmm format (leading zeros must be transmitted) 
		LONGITUDE_HEMISPHERE, 	//E or W 
		CURRENT_UTC_DATE,		//ddmmyy format 
		CURRENT_UTC_TIME,		//hhmmss format 
		RECEIVER_COMMAND;		// A = Auto Locate, R = Unit Reset 	

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( PGRMI sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}		
	}

	
	//2.2.5   Map Datum (PGRMM) 
	public enum PGRMM{
	      CUJRRENT_MAP_DATUM; //(variable length field, for example, “WGS 84”) 

	      @Override
	      public String toString() {
	    	  return StringStyler.prettyString( super.toString() );
	      }	

	      public static boolean isValidString( String str ){
	    	  if( Utils.isNull(str))
	    		  return false;
	    	  for( PGRMM sentence: values() ){
	    		  if( sentence.toString().equals( str ))
	    			  return true;
	    	  }
	    	  return false;
	      }

	}
	
	//2.2.6	Sensor Status Information (PGRMT) 
	//The Garmin Proprietary sentence $PGRMT gives information concerning the status of a GPS sensor. 
	//This	sentence is transmitted once per minute regardless of the selected baud rate. 
	public enum PGRMT{
		PRODUCT, //model and software version (variable length field, e.g., “GPS 10 SW VER  2.01 BT VER 1.27 764”) 
	    ROM_CHECKSUM_TEST, //P = pass, F = fail 
	    RECEIVER_FAILURE_DISCRETE, //P = pass, F = fail 
	    STARTED_DATA_LOST, //R = retained, L = lost 
	    REAL_TIME_CLOCK_LOST, //R = retained, L = lost 
	    OSCILATOR_DRIFT_DISCRETE, //P = pass, F = excessive drift detected 
	    DATA_COLLECTION_DISCRETE, //C = collecting, null if not collecting 
	    GPS_SENSOR_TEMPERATURE, //degrees C 
	    GPS_SENSOR_CONFIGURATION; //R = retained, L = lost 

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( PGRMT sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}
	}
	//2.2.7	3D velocity Information (PGRMV) 
	public enum PGRMV{ 
	   TRUE_EAST_VELOCITY, //514.4 to 514.4 meters/second 
	   TRUE_NORTH_VELOCITY, //514.4 to 514.4 meters/second 
	   UP_VELOCITY; //999.9 to 9999.9 meters/second 
	
	@Override
	public String toString() {
		return StringStyler.prettyString( super.toString() );
	}	

	public static boolean isValidString( String str ){
		if( Utils.isNull(str))
			return false;
		for( PGRMV sentence: values() ){
			if( sentence.toString().equals( str ))
				return true;
		}
		return false;
	}

	}
	
	//2.2.8	Altitude (PGRMZ) 
	public enum PGRMZ{
	   CURRENT_ALTITUDE, //feet 
	   FIX_TYPE; //1 = no fix, 2 = 2D fix, 3 = 3D fix 

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		public static boolean isValidString( String str ){
			if( Utils.isNull(str))
				return false;
			for( PGRMZ sentence: values() ){
				if( sentence.toString().equals( str ))
					return true;
			}
			return false;
		}
	}
	
	//2.2.9  DGPS Beacon Information (PGRMB) 
	public enum PGRMB{ 
	      BEACON_TUNE, //0.0, 283.5 – 325.0 kHz in 0.5 kHz steps 
	      BEACON_BIT_RATE, //0, 25, 50, 100, or 200 bps 
	      BEACON_SNR, //0 to 31 
	      BEACON_DATA_QUALITY, //0 to 100 
	      DISTANCE_TO_BEACON_REF_STATION, //in kilometers 
	      BEACON_RECEIVER_COMM_STATUS, //(0 = Check Wiring, 1 = No Signal, 2 = Tuning, 3 =Receiving, 4= Scanning) 
	      DGPS_FIX_SOURCE, //(R = RTCM, W = WAAS, N = Non-DGPS Fix) 
	      DGPS_MODE; //A = Automatic, W = WAAS Only, R = RTCM Only, N = None (DGPS disabled) 

			@Override
			public String toString() {
				return StringStyler.prettyString( super.toString() );
			}	

			public static boolean isValidString( String str ){
				if( Utils.isNull(str))
					return false;
				for( PGRMB sentence: values() ){
					if( sentence.toString().equals( str ))
						return true;
				}
				return false;
			}

	}
}