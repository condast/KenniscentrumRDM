package org.rdm.aquabots.dashboard.model.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public class BoatOS {

	public enum Compass{
		BEARING,
		ROLL,
		PITCH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}

	}

	public enum GPS{
		FIX,
		SABERTOOTH,
		SERVO,
		PATH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	public enum Sabertooth{
		MOTOR_LEFT,
		MOTOR_RIGHT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	public enum Servo{
		SERVO_LEFT,
		SERVO_RIGHT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	public enum Path{
		LENGTH,
		CURRENTWP,
		WAYPOINTS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	private String name;
	private Compass compass;
	private Sabertooth sb;
	private Servo servo;
	private Path path;
}
