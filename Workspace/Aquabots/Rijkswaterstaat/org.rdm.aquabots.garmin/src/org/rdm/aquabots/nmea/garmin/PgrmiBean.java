package org.rdm.aquabots.nmea.garmin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.rdm.aquabots.nmea.AbstractNMEASentence;
import org.rdm.aquabots.nmea.garmin.IGarminSentence.Hemisphere;
import org.rdm.aquabots.nmea.garmin.IGarminSentence.PGRMI;

public class PgrmiBean extends AbstractNMEASentence{

	
	public PgrmiBean() {
		super( "PGRMI", (byte)14 );
	}
	
	public int getReceiverCommand(){
		String value = super.getValue( PGRMI.RECEIVER_COMMAND );
		return Integer.valueOf( value );
	}

	public Date getCurrentUTCData() throws ParseException {
		String value = super.getValue( PGRMI.CURRENT_UTC_DATE );
		value += " " + super.getValue( PGRMI.CURRENT_UTC_TIME );
		SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yy hh:mm:ss"); 
		return dt.parse( value );
	}

	public float getLatitude() {
		String value = super.getValue( PGRMI.LATITUDE );
		return Float.valueOf( value );
	}

	public Hemisphere getLat_hemisphere() {
		String value = super.getValue( PGRMI.LATITUDE_HEMISPHERE );
		return Hemisphere.valueOf( value );
	}

	public float getLongtitude() {
		String value = super.getValue( PGRMI.LONGTITUDE );
		return Float.valueOf( value );
	}
	
	public Hemisphere getLon_hemisphere() {
		String value = super.getValue( PGRMI.LONGITUDE_HEMISPHERE );
		return Hemisphere.valueOf( value );
	}
}