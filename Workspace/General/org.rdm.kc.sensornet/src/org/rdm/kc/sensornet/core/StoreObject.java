package org.rdm.kc.sensornet.core;

import java.sql.Date;
import java.util.Map;

public class StoreObject<T,U extends Object> {

	private long timeinmillis;
	
	private Map<T,U> data;
	
	public StoreObject( long timeinMillis, Map<T,U> data ) {
		this.timeinmillis = timeinMillis;
		this.data = data;
	}

	public Date getTime(){
		return new Date( this.timeinmillis );
	}

	public Map<T, U> getData() {
		return data;
	}
}
