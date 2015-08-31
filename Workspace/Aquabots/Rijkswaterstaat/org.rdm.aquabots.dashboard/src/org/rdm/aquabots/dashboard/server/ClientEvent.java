package org.rdm.aquabots.dashboard.server;

import java.util.EventObject;

public class ClientEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private String message; 
	
	public ClientEvent(Object arg0, String message ) {
		super(arg0);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
