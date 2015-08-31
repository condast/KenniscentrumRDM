package org.rdm.kc.sensornet.authentication;

import org.rdm.authentication.core.AbstractLoginBean;

public class RdmLoginBean extends AbstractLoginBean{

	private static RdmLoginBean login = new RdmLoginBean();
	
	private RdmLoginBean() {
		super();
	}

	public static RdmLoginBean getInstance() {
		return login;
	}
}
