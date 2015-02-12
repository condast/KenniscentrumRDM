package org.rdm.aquabots.dashboard.websocket;

import java.util.logging.Logger;

import javax.json.JsonObject;

import org.eclipse.rap.rwt.widgets.BrowserCallback;
import org.eclipse.rap.rwt.widgets.BrowserUtil;
import org.eclipse.swt.browser.Browser;
import org.rdm.aquabots.dashboard.json.PredefinedRoutes;

public class WebSocket {

	private boolean opening;
    private boolean opened;

	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	// Execute JavaScript in the browser
	private BrowserCallback bcb = new BrowserCallback(){
		private static final long serialVersionUID = 1L;

		@Override
		public void evaluationSucceeded(Object result) {
			if( opening )
				opened = true;
			opening = false;
			logger.info("succeeded: " + result);	
		}

		@Override
		public void evaluationFailed(Exception exception) {
			logger.warning( "failed: " + exception);	
			opening = false;
		}   	
    };
	
    private Browser browser;
    
    
	public WebSocket(  Browser browser ) {
		super();
		this.browser = browser;
	}

	public void openSocket( ){
		if( this.opened )
			return;
		String str = "openSocket();";
		this.opening = true;
		BrowserUtil.evaluate(browser, str, bcb );
	}

	public void closeSocket(){
		this.opened = false;
		String str = "closeSocket();";
		BrowserUtil.evaluate(browser, str, bcb );
	}

	public void sendMessage( String msg ){
		String str = "send('" + msg + "');";
		logger.info( msg );
		BrowserUtil.evaluate(browser, str, bcb );
	}
	
	public void sendMessage( JsonObject object ){
		sendMessage( object.toString() );
	}
	
	public void sendStop(){
		sendMessage( PredefinedRoutes.S_Stop );
	}
	
	public void sendIJsselExample(){
		sendMessage( PredefinedRoutes.S_IJSSEL_Example );
	}
}
