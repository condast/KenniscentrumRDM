package org.rdm.aquabots.nmea;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractNMEASentence implements INMEASentence {

	private String command;
	private byte length;
	private Map<Enum<?>, String> attributes;
	
	protected AbstractNMEASentence( String command, byte length ) {
		attributes = new HashMap<Enum<?>, String>();
		this.command = command;
		this.length = length;
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public byte getLength() {
		return length;
	}

	protected String getValue(Enum<?> key) {
		return this.attributes.get(key);
	}

	@Override
	public void setValue(Enum<?> key, String value) {
		this.attributes.put(key, value);
	}

	
}
