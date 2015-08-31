package org.rdm.authentication.core;

import java.util.ArrayList;
import java.util.Collection;

import org.rdm.authentication.utils.StringStyler;

public abstract class AbstractLoginBean {

	public enum LogStates{
		LOGIN,
		LOGOFF;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	private String userName;
	private boolean loggedin;
	
	private Collection<IAuthenticationListener> listeners;
	
	protected AbstractLoginBean() {
		super();
		listeners = new ArrayList<IAuthenticationListener>();
	}

	public void addLoginListener( IAuthenticationListener listener ){
		this.listeners.add( listener );
	}

	public void removeLoginListener( IAuthenticationListener listener ){
		this.listeners.remove( listener );
	}
	
	protected void notifyListeners( AuthenticationEvent event ){
		for( IAuthenticationListener listener: listeners )
			listener.notifyLoginChanged(event);
	}

	public void clear(){
		this.loggedin = false;
		this.notifyListeners( new AuthenticationEvent( this ));
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
		loggedin = true;
		this.notifyListeners( new AuthenticationEvent( this ));
	}
	public boolean isLoggedin() {
		return loggedin;
	}	
	
	public String getText(){
		if( !this.loggedin )
			return LogStates.LOGIN.toString();
		else
			return "Welcome " + this.userName + ": " + LogStates.LOGOFF.toString(); 
	}
}
