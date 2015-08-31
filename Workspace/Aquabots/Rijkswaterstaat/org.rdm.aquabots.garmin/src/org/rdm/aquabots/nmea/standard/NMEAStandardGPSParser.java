package org.rdm.aquabots.nmea.standard;

import org.rdm.aquabots.nmea.AbstractNMEAParser;
import org.rdm.aquabots.nmea.INMEASentence;
import org.rdm.aquabots.nmea.NMEAFormatException;

public class NMEAStandardGPSParser extends AbstractNMEAParser implements INmeaGpsSentence{

	private static final String S_ERR_INVALID_COMMAND = "The command is not a Garmin Sentence: ";
	
	public NMEAStandardGPSParser(String sequence) {
		super(sequence );
		super.addSentence((INMEASentence) new GPGGABean());
		super.addSentence((INMEASentence) new GPGLLBean());
		super.addSentence((INMEASentence) new HCHDMBean());
	}

	@Override
	protected boolean prepareCommand(String command) throws NMEAFormatException {
		if( NMEAGpsCommand.isValidString( command ))
			throw new NMEAFormatException( S_ERR_INVALID_COMMAND + command );
		return true;
	}

	@Override
	protected void onParse(int index, int length, String line)
			throws NMEAFormatException {
		INMEASentence sentence = super.getSentence( super.getCommand() );
		NMEAGpsCommand command = NMEAGpsCommand.valueOf( super.getCommand() );
		switch( command ){
			case GPGGA:
			sentence.setValue( NMEAGpsCommand.GPGGA.values()[ index ], line );
			break;
		case GPGLL:
			sentence.setValue( NMEAGpsCommand.GPGLL.values()[ index ], line );
			break;
		case HCHDM:
			sentence.setValue( NMEAGpsCommand.HCHDM.values()[ index ], line );
			break;
		default:
			throw new NMEAFormatException( S_ERR_INVALID_COMMAND + line );		
		}
	}
}