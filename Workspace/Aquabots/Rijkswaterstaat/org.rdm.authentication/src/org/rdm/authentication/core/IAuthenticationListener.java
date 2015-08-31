package org.rdm.authentication.core;

public interface IAuthenticationListener {

	public void notifyLoginChanged( AuthenticationEvent event );
}
