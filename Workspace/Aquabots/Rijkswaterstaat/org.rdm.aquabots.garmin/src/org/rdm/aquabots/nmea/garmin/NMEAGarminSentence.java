package org.rdm.aquabots.nmea.garmin;

import org.rdm.aquabots.nmea.AbstractNMEAParser;
import org.rdm.aquabots.nmea.INMEASentence;
import org.rdm.aquabots.nmea.NMEAFormatException;

public class NMEAGarminSentence extends AbstractNMEAParser implements IGarminSentence{

	private static final String S_ERR_INVALID_COMMAND = "The command is not a Garmin Sentence: ";
	
	public NMEAGarminSentence(String sequence) {
		super(sequence );
		super.addSentence( new PgrmfBean() );
		super.addSentence( new PgrmiBean() );
		super.addSentence( new PgrmtBean() );
		super.addSentence( new PgrmzBean() );
	}

	@Override
	protected boolean prepareCommand(String command) throws NMEAFormatException {
		if( GarminCommands.isValidString( command ))
			throw new NMEAFormatException( S_ERR_INVALID_COMMAND + command );
		return true;
	}

	@Override
	protected void onParse(int index, int length, String line)
			throws NMEAFormatException {
		GarminCommands command = GarminCommands.valueOf( super.getCommand() );
		INMEASentence sentence = super.getSentence( super.getCommand() );
		switch( command ){
		case PGRMI:
			sentence.setValue( IGarminSentence.PGRMI.values()[ index ], line );
			break;
		case PGRMF:
			sentence.setValue( IGarminSentence.PGRMF.values()[ index ], line );
			break;
		case PGRMT:
			sentence.setValue( IGarminSentence.PGRMT.values()[ index ], line );
			break;
		case PGRMZ:
			sentence.setValue( IGarminSentence.PGRMZ.values()[ index ], line );
			break;
		default:
			throw new NMEAFormatException( S_ERR_INVALID_COMMAND + line );		
		}
	}
}