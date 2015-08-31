package org.rdm.aquabots.nmea.standard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.rdm.aquabots.nmea.AbstractNMEASentence;
import org.rdm.aquabots.nmea.garmin.IGarminSentence.Hemisphere;
import org.rdm.aquabots.nmea.garmin.IGarminSentence.PGRMF;

public class HCHDMBean extends AbstractNMEASentence{
	
	public HCHDMBean() {
		super( "PGRMF", (byte)14 );
	}
	
	public int getCourseOverGround(){
		String value = super.getValue( PGRMF.COURSE_OVER_GROUD );
		return Integer.valueOf( value );
	}

	public int getFixType(){
		String value = super.getValue( PGRMF.FIX_TYPE );
		return Integer.valueOf( value );
	}

	public int getGPSLeapSecondCount(){
		String value = super.getValue( PGRMF.GPS_LEAP_SECOND_COUNT );
		return Integer.valueOf( value );
	}

	public int getGps_week() {
		String value = super.getValue( PGRMF.GPS_WEEK );
		return Integer.valueOf( value );
	}
	
	public int getGps_seconds() {
		String value = super.getValue( PGRMF.GPS_SECONDS );
		return Integer.valueOf( value );
	}

	public Date getUtc_position_fix() throws ParseException {
		String value = super.getValue( PGRMF.UTC_DATE_OF_POSITION_FIX );
		value += " " + super.getValue( PGRMF.UTC_TIME_OF_POSITION_FIX );
		SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yy hh:mm:ss"); 
		return dt.parse( value );
	}

	public float getLatitude() {
		String value = super.getValue( PGRMF.LATITUDE );
		return Float.valueOf( value );
	}

	public Hemisphere getLat_hemisphere() {
		String value = super.getValue( PGRMF.LATITUDE_HEMISPHERE );
		return Hemisphere.valueOf( value );
	}

	public float getLongtitude() {
		String value = super.getValue( PGRMF.LONGTITUDE );
		return Float.valueOf( value );
	}
	
	public Hemisphere getLon_hemisphere() {
		String value = super.getValue( PGRMF.LONGITUDE_HEMISPHERE );
		return Hemisphere.valueOf( value );
	}
	
	public String getMode() {
		String value = super.getValue( PGRMF.COURSE_OVER_GROUD );
		return value;
	}
}
