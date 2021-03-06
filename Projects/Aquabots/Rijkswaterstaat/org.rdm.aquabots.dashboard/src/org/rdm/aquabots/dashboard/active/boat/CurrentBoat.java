package org.rdm.aquabots.dashboard.active.boat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Scanner;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.rdm.aquabots.dashboard.model.ITrajectoryListener;
import org.rdm.aquabots.dashboard.model.TrajectoryEvent;
import org.rdm.aquabots.dashboard.model.boat.BoatModel;
import org.rdm.aquabots.dashboard.model.boat.IBoatModel;
import org.rdm.aquabots.dashboard.utils.IOUtils;

/**
 * Maintains the boat currently displayed on the main view 
 * @author Kees
 *
 */
public class CurrentBoat {

	public static final String S_BOATS_CONFIG = "/data/boats.cfg";

	private static CurrentBoat current = new CurrentBoat();
	
	private Map<String, IBoatModel> models;
	
	private String currentBoat;

	private Collection<ICurrentBoatListener> listeners;
	
	private ITrajectoryListener tl = new ITrajectoryListener() {
		
		@Override
		public void notifyTrajectoryChanged(TrajectoryEvent event) {
			notifyListeners( new CurrentBoatEvent( this, event.getWayPoint()));
		}
	};
	
	private CurrentBoat() {
		super();
		models = new HashMap<String, IBoatModel>();
		listeners = new ArrayList<ICurrentBoatListener>();
	}
	
	public static CurrentBoat getInstance(){
		return current;
	}

	public void init() {
		models.clear();
		URL url = CurrentBoat.class.getResource( S_BOATS_CONFIG );
		try {
			this.readBoats(url);
			this.currentBoat = models.keySet().iterator().next();
		} catch (LoginException e) {
			e.printStackTrace();
		}
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
	
	public IBoatModel getModel(){
		return switchBoat( currentBoat );
	}
	
	/**
	 * Switch to another boat
	 * @param name
	 * @return
	 */
	public IBoatModel switchBoat( String name ){
		IBoatModel model = models.get( currentBoat ); 
		model.getTrajectory().removeListener(tl);
		model = models.get( name );
		model.getTrajectory().addListener(tl);
		currentBoat = name;
		return model;
	}
	
	/**
	 * Create a trajectory for the current model
	 */
	public void createTrajectory(){
		IBoatModel model = this.getModel();
		model.createTrajectory();
	}

	/**
	 * Get the boat names
	 * @return
	 */
	public String[] getNames(){
		return this.models.keySet().toArray( new String[ this.models.size()]);
	}
	
	/**
	 * Checks the userName and password against a list of possibilities, from the given url
	 * @param url
	 * @return
	 * @throws FailedLoginException
	 */
	public boolean readBoats( URL url ) throws LoginException{
		InputStream in = null;
		Scanner scanner = null;
		boolean retval = false;
		try{
			in = url.openStream();
			scanner = new Scanner( in );
			while( scanner.hasNext() ){
				String line = scanner.nextLine();
				if( line.startsWith("#"))
					continue;
				line = line.replace(";", "");
				line = line.replace(" ", "");
				String[] split = line.split("[,]");
				this.models.put( split[0], new BoatModel( split[0], split[1]));
			}
			retval = true;
		}
		catch( IOException ex ){
			ex.printStackTrace();
			throw new LoginException( ex.getMessage() );
		}
		finally{
			scanner.close();
			IOUtils.closeInputStream( in );
		}
		return retval;
	}

}
