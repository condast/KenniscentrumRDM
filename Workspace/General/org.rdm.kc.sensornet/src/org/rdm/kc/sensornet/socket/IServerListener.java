package org.rdm.kc.sensornet.socket;

public interface IServerListener {

	public void notifyMessageReceived( ClientEvent event );
}
