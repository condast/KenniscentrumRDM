package org.rdm.aquabots.dashboard.authentication;

import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.rdm.aquabots.dashboard.Activator;
import org.rdm.aquabots.dashboard.utils.authentication.AbstractSecureHttpContext;

public class RdmSecureHttpContext extends AbstractSecureHttpContext {

	private static final String CONFIG_FILE = "data/jaas.cfg";

	private static final String S_REALM = "Aquabots Realm";
	
	public RdmSecureHttpContext() {
		super( getResourceBase(), getConfigFile( CONFIG_FILE), S_REALM);
	}
	
	private static URL getResourceBase(){
		BundleContext context = Activator.getDefault();
		Bundle bundle = context.getBundle();
		return bundle.getEntry("");
	}
	
	private static URL getConfigFile( String jaasConfigFile ){
		return RdmSecureHttpContext.class.getResource( jaasConfigFile );
	}
}
