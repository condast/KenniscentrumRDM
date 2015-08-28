package org.rdm.aquabots.dashboard.model.waypoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WayPointManager {

	public static final int SLEEP_TIME = 2000;
	
	private Queue<WayPoint> waypoints;

	private ExecutorService service;
	private Lock lock;
	private int lastSize;
	private boolean started;
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			started = true;
			while( started ){
				if( waypoints.size() <= lastSize ){
					lastSize = waypoints.size();
				}else{
					lastSize = waypoints.size();

					notifyChanged( new WayPointManagerEvent(this, waypoints.poll() ));
					try{
						Thread.sleep( SLEEP_TIME );
					}
					catch( InterruptedException ex ){
						ex.printStackTrace();
					}
				}
			}
		}	
	};
	private Collection<IWayPointManagerListener> listeners;
	
	//private static WayPointManager manager = new WayPointManager();

	public WayPointManager() {
		this.started = false;
		waypoints = new LinkedList<WayPoint>();
		service = Executors.newCachedThreadPool();
		lock = new ReentrantLock();
		listeners = new ArrayList<IWayPointManagerListener>();
	}

	public void addListener( IWayPointManagerListener listener){
		this.listeners.add( listener );
	}

	public void removeListener( IWayPointManagerListener listener){
		this.listeners.remove( listener );
	}

	protected void notifyChanged( WayPointManagerEvent event ){
		for( IWayPointManagerListener listener: this.listeners ){
			listener.notifyChanged(event);
		}
	}
	
	public synchronized void addWayPoint( WayPoint waypoint ){
		lock.lock();
		try{
		   this.waypoints.add( waypoint );
		}
		finally{
			lock.unlock();
		}
	}

	public synchronized WayPoint removeWayPoint(){
		lock.lock();
		try{
			if( !this.waypoints.isEmpty())
				return this.waypoints.poll();
		}
		finally{
			lock.unlock();
		}
		return null;
	}

	public boolean isEmpty(){
		return this.waypoints.isEmpty();
	}
	
	public void start(){
		this.service.execute( this.runnable );
	}

	public void stop(){
		this.started = false;
		Thread.currentThread().interrupt();
		this.service.shutdown();
	}
}
