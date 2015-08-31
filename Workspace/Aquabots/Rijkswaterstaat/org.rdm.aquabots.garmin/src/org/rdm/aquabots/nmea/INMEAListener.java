package org.rdm.aquabots.nmea;

public interface INMEAListener {

	public void notifySentenceParsed( NMEAEvent event );
}
