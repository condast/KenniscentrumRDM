package org.rdm.aquabots.nmea;


public interface INMEASentence {

	/**
	 * Get the command
	 * @return
	 */
	public String getCommand();
	
	/**
	 * Get the length of the command variables
	 * @return
	 */
	public byte getLength();

	/**
	 * Set the value for the given key
	 * @param key
	 * @param value
	 */
	public void setValue( Enum<?> key, String value );

}
