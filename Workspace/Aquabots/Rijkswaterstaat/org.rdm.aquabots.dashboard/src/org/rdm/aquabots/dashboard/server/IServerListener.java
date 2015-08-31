package org.rdm.aquabots.dashboard.server;

public interface IServerListener {

	public void notifyMessageReceived( ClientEvent event );
}
