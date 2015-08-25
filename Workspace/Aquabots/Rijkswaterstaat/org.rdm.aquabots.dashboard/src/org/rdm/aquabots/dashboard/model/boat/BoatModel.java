package org.rdm.aquabots.dashboard.model.boat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.rdm.aquabots.dashboard.active.boat.CurrentBoatEvent;
import org.rdm.aquabots.dashboard.active.boat.ICurrentBoatListener;
import org.rdm.aquabots.dashboard.history.TrajectoryHistory;
import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;

public class BoatModel implements IBoatModel {

	private String name;
	private URL url;
	
	private TrajectoryModel model;
	private Collection<WayPoint> waypoints;
	
	private Collection<ICurrentBoatListener> listeners;
	
	private TrajectoryHistory history;

	public BoatModel( String name) {
		this.name = name;
		model = new TrajectoryModel();
		listeners = new ArrayList<ICurrentBoatListener>();
		waypoints = new ArrayList<WayPoint>();
		this.history = new TrajectoryHistory();
	}

	public BoatModel( String name, String url) throws MalformedURLException {
		this( name, new URL( url ));
	}
	
	public BoatModel( String name, URL url) {
		this(name);
		this.url = url;
	}

	/**
	 * Get the name of the boat
	 * @return
	 */
	public String getName(){
		return name;
	}

	public URL getURL() {
		return url;
	}

	public void addListener( ICurrentBoatListener listener ){
		this.listeners.add( listener );
	}
	
	public void removeListener( ICurrentBoatListener listener ){
		this.listeners.remove( listener );
	}
	
	protected void notifyListeners( CurrentBoatEvent event ){
		for( ICurrentBoatListener listener: this.listeners )
			listener.notifyStatusChanged(event);
	}
	
	/**
	 * Get the plotted trajectory of the boat
	 * @return
	 */
	public TrajectoryModel getTrajectory(){
		return model;
	}
	
	public WayPoint[] getTrip(){
		return waypoints.toArray( new WayPoint[ waypoints.size() ] );
	}
	
	@Override
	public TrajectoryModel createTrajectory(){
		TrajectoryModel result = model.createTrajectory();
		if( result == null )
			return null;
		history.addTrajectory( result );
		return result;

	}
}