package org.rdm.aquabots.dashboard.utils;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public abstract class AbstractEventBuffer<T extends Object> {

	private LinkedList<T> buffer;
	private Lock lock;
	private boolean started;
	private ExecutorService service;
	private int time;
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			while( started ){
				if( !buffer.isEmpty() ){
					lock.lock();
					try{
					  onEventReceived( buffer.getLast());
					}
					finally{
						lock.unlock();
					}
				}
				try{
					Thread.sleep( time );
				}
				catch( InterruptedException ex ){
					logger.warning( ex.getMessage() );
				}
			}	
		}
	};
	
	protected AbstractEventBuffer( int time ) {
		super();
		this.time = time;
		buffer = new LinkedList<T>();
		lock = new ReentrantLock();
		service = Executors.newCachedThreadPool();
	}
	
	protected abstract void onEventReceived( T last );
	
	public synchronized void push( T event ){
		this.lock.lock();
		try{
			this.buffer.add( event );
		}
		finally{
			this.lock.unlock();
		}
	}

	public synchronized T poll(){
		this.lock.lock();
		try{
			return this.buffer.poll();
		}
		finally{
			this.lock.unlock();
		}
	}
	
	public void start(){
		this.started = true;
		this.service.execute(runnable);
	}
	
	public void stop(){
		this.started = false;
		Thread.currentThread().interrupt();
		this.service.shutdown();
	}
	
	protected boolean isEmpty(){
		return this.buffer.isEmpty();
	}
}
