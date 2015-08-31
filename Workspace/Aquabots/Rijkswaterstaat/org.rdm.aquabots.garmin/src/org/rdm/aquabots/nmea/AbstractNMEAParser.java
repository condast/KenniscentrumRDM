package org.rdm.aquabots.nmea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractNMEAParser {

	private static final String S_ERR_INVALID_COMMAND = "The command does not start with $: ";
	private static final String S_ERR_INVALID_COMMAND_NO_END = "The command does not contain an end character (*): ";
	private static final String S_ERR_INVALID_LENGTH = "The sequence is too short for an NMEA sequence: ";
	private static final String S_ERR_INVALID_CHARACTER = "Invalid character at index: ";
	private static final String S_ERR_INVALID_CHECKSUM_CAPS = "Invalid checksum. The Hex code must be in capitals: ";
	private static final String S_ERR_INVALID_CHECKSUM = "Invalid checksum: ";
	
	private String sequence;
	private byte checksum;
	private String command;
	private boolean performCheckSum;

	private Map<String, INMEASentence> sentences;

	private Collection<INMEAListener> listeners;
	
	protected AbstractNMEAParser( String sequence ) {
		this( sequence, true );
	}

	protected AbstractNMEAParser( String sequence, boolean performChecksum ) {
		this.sequence = sequence;
		this.performCheckSum = performChecksum;
		this.sentences = new HashMap<String, INMEASentence>();
		this.listeners = new ArrayList<INMEAListener>();
	}

	public String getCommand() {
		return command;
	}

	protected void addSentence( INMEASentence sentence ){
		this.sentences.put( sentence.getCommand(), sentence );
	}

	protected void removeSentence( INMEASentence sentence ){
		this.sentences.remove( sentence );
	}

	protected INMEASentence getSentence( String command ){
		return this.sentences.get( command );
	}
	
	public void addListener( INMEAListener listener ){
		this.listeners.add(listener );
	}

	public void removeListener( INMEAListener listener ){
		this.listeners.remove(listener );
	}
	
	protected void notifyListeners( NMEAEvent event ){
		for( INMEAListener listener: this.listeners )
			listener.notifySentenceParsed(event);
	}

	@Override
	public int hashCode() {
		return this.sequence.hashCode();
	}

	@Override
	public String toString() {
		return this.sequence.toString();
	}

	/**
	 * Extract the command from the given line
	 * @param line
	 * @return
	 * @throws NMEAFormatException 
	 */
	protected abstract boolean prepareCommand( String command ) throws NMEAFormatException;
	
	public void parse() throws NMEAFormatException{
		this.checksum = checksum( this.sequence );
		String[] split = this.sequence.split("[,]");
		if( split.length <= 4)
			throw new NMEAFormatException( S_ERR_INVALID_LENGTH + this.sequence );
		for( int i=0; i<split.length; i++ )
			parse( i, split.length, split[i].trim() );
		INMEASentence result = this.createSentence();
		this.notifyListeners( new NMEAEvent( this, result ));
	}

	/**
	 * Parse one line of the sentence
	 * @param index
	 * @param length
	 * @param line
	 * @throws NMEAFormatException
	 */
	protected abstract void onParse( int index, int length, String line ) throws NMEAFormatException;

	/**
	 * Create the sentence
	 * @return
	 * @throws NMEAFormatException
	 */
	protected INMEASentence createSentence() throws NMEAFormatException {
		INMEASentence sentence = this.sentences.get( this.command );
		return sentence;
	}

	private final void parse( int index, int length, String line ) throws NMEAFormatException{
		if( index == 0 ){
			if( !line.startsWith( "$" ))
				throw new NMEAFormatException( index, line, S_ERR_INVALID_COMMAND );
			this.command = line.substring(1, line.length() );
			this.prepareCommand( command );
		} else if( index == (length - 3)){
			if( !line.startsWith( "*" ))
				throw new NMEAFormatException( index, line, S_ERR_INVALID_CHARACTER, ". Expected : *" );
			String str = line.replace("*", "").trim();
			if( str.length() != 2 )
				throw new NMEAFormatException( index, line, S_ERR_INVALID_CHARACTER, ". Expected : *" );
			if( !str.toUpperCase().equals(str ))
				throw new NMEAFormatException( index, line, S_ERR_INVALID_CHECKSUM_CAPS );
			byte cs = Byte.valueOf( str );
			if( !this.performCheckSum || ( cs == 0 ))
				return ;
			if( cs != checksum )
				throw new NMEAFormatException( index, line, S_ERR_INVALID_CHECKSUM, String.valueOf( this.checksum ) );				
		} else if( index == (length - 2)){
			try{
			  byte bt = Byte.parseByte(line );
			  if( bt != 0x0A)
					throw new NMEAFormatException( index, line, S_ERR_INVALID_CHARACTER, "<LF>" );			
			}
			catch( Exception ex ){
				throw new NMEAFormatException( index, line, S_ERR_INVALID_CHARACTER, "<LF>" );			
			}
		} else if( index == (length - 1)){
			try{
				  byte bt = Byte.parseByte(line );
				  if( bt != 0x0D)
						throw new NMEAFormatException( index, line, S_ERR_INVALID_CHARACTER, "<CR>" );			
				}
				catch( Exception ex ){
					throw new NMEAFormatException( index, line, S_ERR_INVALID_CHARACTER, "<CR>" );			
				}
		}else{
			this.onParse(index, length, line);
		}
	}
		
	public final static byte checksum( String nmea ) throws NMEAFormatException{
		byte[] bytes = nmea.getBytes();
		if( bytes[0] != '$')
			throw new NMEAFormatException( 0, nmea, S_ERR_INVALID_CHARACTER, ". Expected : $" );
		int index = 0;
		byte checksum = bytes[++index];
		while( bytes[index] != (byte)42 ){
			checksum ^= bytes[++index];
			if( index >= nmea.length()-1 )
				throw new NMEAFormatException( S_ERR_INVALID_COMMAND_NO_END + nmea );
		}
		
		return checksum;
	}
	
	protected static final String printError( int index, String line, String first, String last ){
		return first + "[" + index + ", " + line + "]" + last;
	}
}
