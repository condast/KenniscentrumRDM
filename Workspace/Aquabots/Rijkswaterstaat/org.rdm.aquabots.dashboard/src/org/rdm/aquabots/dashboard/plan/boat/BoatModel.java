package org.rdm.aquabots.dashboard.plan.boat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.rdm.aquabots.dashboard.active.boat.CurrentBoatEvent;
import org.rdm.aquabots.dashboard.active.boat.ICurrentBoatListener;
import org.rdm.aquabots.dashboard.def.boat.IBoatModel;
import org.rdm.aquabots.dashboard.history.TrajectoryHistory;
import org.rdm.aquabots.dashboard.plan.TrajectoryModel;
import org.rdm.aquabots.dashboard.plan.waypoint.WayPoint;

public class BoatModel implements IBoatModel {

	private String name;
	private URL url;
	
	private TrajectoryModel trajectory;
	private Collection<WayPoint> waypoints;
	private StatusModel status;
	
	private Collection<ICurrentBoatListener> listeners;
	
	private TrajectoryHistory history;

	public BoatModel( String name) {
		this.name = name;
		listeners = new ArrayList<ICurrentBoatListener>();
		waypoints = new ArrayList<WayPoint>();
		this.history = new TrajectoryHistory();
		trajectory = this.createTrajectory();
		this.status = new StatusModel();
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

	@Override
	public StatusModel getStatus() {
		return status;
	}

	public void setStatus(StatusModel status) {
		this.status = status;
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
		return trajectory;
	}
	
	public WayPoint[] getTrip(){
		return waypoints.toArray( new WayPoint[ waypoints.size() ] );
	}
	
	@Override
	public TrajectoryModel createTrajectory(){
		trajectory = new TrajectoryModel();
		history.addTrajectory( trajectory );
		return trajectory;
	}
}