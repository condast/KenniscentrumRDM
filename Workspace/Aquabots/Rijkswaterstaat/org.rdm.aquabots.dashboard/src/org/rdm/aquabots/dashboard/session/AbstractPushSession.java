package org.rdm.aquabots.dashboard.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractPushSession {

	public static final int DEFAULT_TIMEOUT = 5000;
	
	private Display display;
	
	private ServerPushSession session;
	private ExecutorService es;
	private boolean refresh = false;
	private int timeout;
	
	private Collection<ISessionListener> listeners;

	protected AbstractPushSession() {
		this( DEFAULT_TIMEOUT );
	}

	protected AbstractPushSession( int timeout ) {
		this.timeout = timeout;
		listeners = new ArrayList<ISessionListener>();
		es = Executors.newCachedThreadPool();
	}

	private Runnable runnable = new Runnable() {
		public void run() {
			while(!refresh && !runSession()){
				try{
					Thread.sleep( timeout );
				}
				catch( InterruptedException ex ){
					ex.printStackTrace();
				}
			}
			if( display.isDisposed())
				return;
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
	
	protected boolean isRefresh() {
		return refresh;
	}

	protected void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public synchronized void start(){
		session = new ServerPushSession();
		session.start();
		this.refresh = false;
		es.execute(runnable);
	}

	/**
	 * Run the session. returns true if the session completed succesfully.
	 * @return
	 */
	protected abstract boolean runSession();

	public synchronized void stop(){
		this.listeners.clear();
		this.refresh = false;
		es.shutdown();
	}
}
