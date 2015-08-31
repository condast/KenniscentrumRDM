package org.rdm.aquabots.dashboard.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rdm.aquabots.dashboard.test.json.JsonTest;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		JsonTest test = new JsonTest();
		try{
			test.testSystenString( JsonTest.SYSTEM_STRING );
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
