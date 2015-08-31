package org.rdm.aquabots.dashboard.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.widgets.Display;

public abstract class AbstractExecutorJob {

	private Runnable runnable = null;
	private ExecutorService service;

	public AbstractExecutorJob() {
		super();
		this.service = Executors.newCachedThreadPool();
	}

	protected abstract void onUpdate();

	public void start(){
		runnable = new Runnable() {
			
			Display display = null;//Platform.get.getDisplay();
			public void run() {
				// do some background work ...
				// schedule the UI update
				display.asyncExec( new Runnable() {
					public void run() {
						onUpdate();
					}
				} );
			}
		};
		this.service.execute( runnable );
	}
}