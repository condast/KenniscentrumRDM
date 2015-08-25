package org.rdm.aquabots.dashboard.history;

import java.util.ArrayList;
import java.util.List;

import org.rdm.aquabots.dashboard.model.TrajectoryModel;

public class TrajectoryHistory {

	private List<TrajectoryModel> trajectories;

	private int index = 0;
	
	public TrajectoryHistory() {
		super();
		trajectories = new ArrayList<TrajectoryModel>();
	}
	
	public void addTrajectory( TrajectoryModel model ){
		this.trajectories.add( model );
	}
	
	public void removeTrajectory( TrajectoryModel model ){
		this.trajectories.remove( model );
	}
	
	public TrajectoryModel get(){
		return this.trajectories.get( index );
	}
	
	public void next(){
		if( this.index < this.trajectories.size())
			this.index++;
	}

	public void previous(){
		if( this.index > 0 )
			this.index--;
	}
}