package org.rdm.aquabots.dashboard.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractPushSession {

	private Display display;
	
	private ServerPushSession session;
	private ExecutorService es;
	
	private Collection<ISessionListener> listeners;
		
	protected AbstractPushSession() {
		listeners = new ArrayList<ISessionListener>();
		es = Executors.newCachedThreadPool();
	}

	private Runnable runnable = new Runnable() {
		public void run() {
			while(!runSession()){
				try{
					Thread.sleep( 5000 );
				}
				catch( InterruptedException ex ){
					ex.printStackTrace();
				}
			}
			display.asyncExec( new Runnable() {
				public void run() {
					for(ISessionListener listener: listeners)
						listener.notifySessionChanged( new SessionEvent( this ) );
					session.stop();
					start();
				}
			});
		};
	};

	public void addSessionListener( ISessionListener listener ){
		this.listeners.add( listener );
	}

	public void removeSessionListener( ISessionListener listener ){
		this.listeners.remove( listener );
	}

	public void init( Display display ){
			this.display = display;
	}
	
	public void start(){
		session = new ServerPushSession();
		session.start();
		es.execute(runnable);
	}
	
	/**
	 * Run the session. returns true if the session completed succesfully.
	 * @return
	 */
	protected abstract boolean runSession();

	public void stop(){
		this.listeners.clear();
		es.shutdown();
	}
}
