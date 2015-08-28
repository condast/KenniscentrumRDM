package org.rdm.aquabots.authentication;

public class RdmLoginBean extends AbstractLoginBean{

	private static RdmLoginBean login = new RdmLoginBean();
	
	private RdmLoginBean() {
		super();
	}

	public static RdmLoginBean getInstance() {
		return login;
	}
}
