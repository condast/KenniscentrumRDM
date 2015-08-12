package org.rdm.aquabots.dashboard.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;
import org.rdm.aquabots.dashboard.json.PredefinedRoutes;
import org.rdm.aquabots.dashboard.websocket.WebSocket;

public class RandomRoutes {

	private ExecutorService service;
	private boolean started;
	private WebSocket socket;
	private int time;
	private Display display;
	private ServerPushSession pushSession = new ServerPushSession();;
	
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			time = 2000;
			while( started ){
				display.syncExec( new Runnable(){

					@Override
					public void run() {
						try{
							String route = PredefinedRoutes.getRandom();
							socket.sendMessage( route );
						}
						catch( Exception ex ){
							ex.printStackTrace();
						}
					}
					
				});
				try{
					Thread.sleep( time );
				}
				catch( InterruptedException ex ){
					ex.printStackTrace();
				}
			}
			pushSession.stop();
		}
	};
	
	public RandomRoutes( Display display, WebSocket socket, int time ) {
		super();
		this.socket = socket;
		this.time = time;
		this.display = display;
		service = Executors.newSingleThreadExecutor();
	}
	
	public void start(){
		pushSession.start();
		service.execute( this.runnable );
		this.started = true;
	}
	
	public void stop(){
		Thread.currentThread().interrupt();
		this.service.shutdown();
		this.started = false;
	}
}
