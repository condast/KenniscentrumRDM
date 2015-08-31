package org.rdm.aquabots.dashboard;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rdm.aquabots.dashboard.server.Server;

public class Activator implements BundleActivator {

    public static String BUNDLE_ID = "org.rdm.aquabots.dashboard";
	public static final String S_CONTEXT = "AQUABOTS";
    
    private static BundleContext defaultContext;
 
	private Server server = Server.getInstance();	

	public void start(BundleContext context) throws Exception {
		defaultContext = context;
  		server.setPort( 9720 );
  		server.start(null);
    }

    public void stop(BundleContext context) throws Exception {
        defaultContext = null;
    	server.stop();
    	System.out.println("Server stopped" );
    }
    
    
    public static BundleContext getDefault(){
    	return defaultContext;
    }

}
