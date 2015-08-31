package org.rdm.aquabots.dashboard.plan.boat;

import org.rdm.aquabots.dashboard.def.boat.IBoatModel;
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
	
	public Command( IBoatModel model ) {
		this.path = new Path(null );
		this.boat = model.getName().toString();
	}

	public String getName() {
		return this.boat;
	}

	public Path getPath() {
		return path;
	}
}
