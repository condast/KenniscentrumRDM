package org.rdm.aquabots.dashboard.model.boat;

import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class Command {

	public enum Attributes{
		NAME,
		PATH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	private String boat;
	private Path path;
	
	public Command( TrajectoryModel trajectory ) {
		this.path = new Path( trajectory );
		this.boat = trajectory.getBoat();
	}

	public String getName() {
		return this.boat;
	}

	public Path getPath() {
		return path;
	}
}
