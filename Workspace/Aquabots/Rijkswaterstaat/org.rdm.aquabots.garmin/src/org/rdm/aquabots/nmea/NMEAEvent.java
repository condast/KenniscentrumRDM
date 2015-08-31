package org.rdm.aquabots.nmea;

import java.util.EventObject;

public class NMEAEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private INMEASentence sentence;
	
	public NMEAEvent(Object arg0, INMEASentence sentence ) {
		super(arg0);
		this.sentence = sentence;
	}

	public INMEASentence getSentence() {
		return sentence;
	}
}
