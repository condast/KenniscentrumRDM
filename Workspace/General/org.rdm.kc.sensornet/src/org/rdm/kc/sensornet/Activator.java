package org.rdm.kc.sensornet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rdm.kc.sensornet.socket.Server;

public class Activator implements BundleActivator {

    public static String BUNDLE_ID = "org.rdm.kc.sensornet";
	public static final String S_CONTEXT = "KCRDM";

	private static BundleContext bc; 
    
	private Server server = Server.getInstance();	
	
	public void start(BundleContext context) throws Exception {
    	bc = context;
    	server.setPort( 8080 );
    	server.start(null);
   }

    public void stop(BundleContext context) throws Exception {
      bc = null;
      server.stop();
  	  System.out.println("Server stopped" );
    }
    
    public static BundleContext getDefault(){
    	return bc;
    }

}
