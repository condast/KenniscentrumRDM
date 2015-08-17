package org.rdm.aquabots.dashboard.authentication;

import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.rdm.aquabots.dashboard.Activator;
import org.rdm.aquabots.dashboard.utils.authentication.AbstractSecureHttpContext;

public class RdmSecureHttpContext extends AbstractSecureHttpContext {

	private static final String S_REALM = "Aquabots Realm";
	
	public RdmSecureHttpContext() {
		super( getResourceBase(), getConfigFile(), S_REALM);
	}
	
	private static URL getResourceBase(){
		BundleContext context = Activator.getDefault();
		Bundle bundle = context.getBundle();
		return bundle.getEntry("");
	}
	
	private static URL getConfigFile(){
		String jaasConfigFile = "data/jaas.cfg";
		return Activator.getDefault().getBundle().getEntry( jaasConfigFile );
	}
}
