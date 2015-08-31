package org.rdm.aquabots.nmea.garmin;

import org.rdm.aquabots.nmea.AbstractNMEASentence;
import org.rdm.aquabots.nmea.garmin.IGarminSentence.FixType;
import org.rdm.aquabots.nmea.garmin.IGarminSentence.PGRMZ;

public class PgrmzBean extends AbstractNMEASentence{


	public PgrmzBean() {
		super( "PGRMZ", (byte)2 );
	}
	
	public FixType getFixType(){
		String value = super.getValue( PGRMZ.FIX_TYPE );
		return FixType.valueOf( value );
	}

	public int getGPSSensorConfiguration(){
		String value = super.getValue( PGRMZ.CURRENT_ALTITUDE );
		return Integer.valueOf( value );
	}
}
