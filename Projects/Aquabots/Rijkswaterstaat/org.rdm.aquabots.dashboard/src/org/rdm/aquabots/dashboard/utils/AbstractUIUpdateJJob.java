package org.rdm.aquabots.dashboard.utils;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractUIUpdateJJob {

	private final ServerPushSession pushSession;
	private Runnable runnable = null;


	public AbstractUIUpdateJJob() {
		super();
		this.pushSession = new ServerPushSession();
	}

	protected abstract void onUpdate();

	public void start(){
		runnable = new Runnable() {
			
			public void run() {
				Display display = PlatformUI.getWorkbench().getDisplay();
				// do some background work ...
				// schedule the UI update
				display.asyncExec( new Runnable() {
					public void run() {
						onUpdate();
					}
				} );
			}
		};
		pushSession.start();
		Thread bgThread = new Thread( runnable );
		bgThread.setDaemon( true );
		bgThread.start();	
	}
}