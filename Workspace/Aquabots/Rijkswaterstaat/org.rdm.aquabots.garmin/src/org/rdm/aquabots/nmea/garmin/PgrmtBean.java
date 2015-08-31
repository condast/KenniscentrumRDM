package org.rdm.aquabots.nmea.garmin;

import org.rdm.aquabots.nmea.AbstractNMEASentence;
import org.rdm.aquabots.nmea.garmin.IGarminSentence.PGRMT;
import org.rdm.aquabots.nmea.utils.StringStyler;

public class PgrmtBean extends AbstractNMEASentence{

	public enum Persist{
		RETAINED,
		LOST;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		public static Persist getPersistStatus( String str ){
			String hem = StringStyler.styleToEnum( str );
			if( str.length() == 1 ){
			if( "R".equals( hem))
				return Persist.RETAINED;
			if( "L".equals( hem))
				return Persist.LOST;
			}
			return Persist.valueOf( hem );
		}
	}

	public enum PassFail{
		PASS,
		FAIL;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		public static PassFail getPersistStatus( String str ){
			String hem = StringStyler.styleToEnum( str );
			if( str.length() == 1 ){
			if( "P".equals( hem))
				return PassFail.PASS;
			if( "F".equals( hem))
				return PassFail.FAIL;
			}
			return PassFail.valueOf( hem );
		}
	}

	public PgrmtBean() {
		super( "PGRMT", (byte)14 );
	}
	
	public int getDataCollectionDiscrete(){
		String value = super.getValue( PGRMT.DATA_COLLECTION_DISCRETE );
		return Integer.valueOf( value );
	}

	public Persist getGPSSensorConfiguration(){
		String value = super.getValue( PGRMT.GPS_SENSOR_CONFIGURATION );
		return Persist.valueOf( value );
	}

	public int getGPSSensorTemperature(){
		String value = super.getValue( PGRMT.GPS_SENSOR_TEMPERATURE );
		return Integer.valueOf( value );
	}

	public PassFail getOscillatorDrift() {
		String value = super.getValue( PGRMT.OSCILATOR_DRIFT_DISCRETE );
		return PassFail.valueOf( value );
	}
	
	public String getProduct() {
		return super.getValue( PGRMT.PRODUCT );
	}

	public Persist getRealTimeClockLost(){
		String value = super.getValue( PGRMT.REAL_TIME_CLOCK_LOST );
		return Persist.valueOf( value );
	}

	public PassFail getReceiverFailureDiscrete(){
		String value = super.getValue( PGRMT.RECEIVER_FAILURE_DISCRETE );
		return PassFail.valueOf( value );
	}

	public PassFail getRomChecksumTest(){
		String value = super.getValue( PGRMT.ROM_CHECKSUM_TEST );
		return PassFail.valueOf( value );
	}

	public Persist getStartedDataLost() {
		String value = super.getValue( PGRMT.STARTED_DATA_LOST );
		return Persist.valueOf( value );
	}
}
