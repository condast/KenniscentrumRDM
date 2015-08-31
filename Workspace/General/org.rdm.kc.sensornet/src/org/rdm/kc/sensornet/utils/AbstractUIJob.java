package org.rdm.kc.sensornet.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractUIJob {

	private ExecutorService service;
	private Display display;
	private ServerPushSession pushSession = new ServerPushSession();;
	
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			display.syncExec( new Runnable(){

				@Override
				public void run() {
					onJobStarted();
				}

			});
			pushSession.stop();
		}
	};
	
	public AbstractUIJob( Display display ) {
		super();
		this.display = display;
		service = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Implement the required functionality
	 */
	protected abstract void onJobStarted();
	
	public void start(){
		pushSession.start();
		service.execute( this.runnable );
	}
	
	public void stop(){
		Thread.currentThread().interrupt();
		this.service.shutdown();
	}
}
