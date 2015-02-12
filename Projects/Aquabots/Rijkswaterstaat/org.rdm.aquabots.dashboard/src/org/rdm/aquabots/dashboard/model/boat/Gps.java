package org.rdm.aquabots.dashboard.model.boat;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public class Gps {

	public enum Base{
		NAME,
		MODE,
		COMPASS,
		GPS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}	

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
			switch( this ){
			case MOTOR_LEFT:
				return "Servo Left";
			default:
				return "Servo Right";
			}
		}
	}

	public enum Servo{
		SERVO_LEFT,
		SERVO_RIGHT;

		@Override
		public String toString() {
			switch( this ){
			case SERVO_LEFT:
				return "Servo Left";
			default:
				return "Servo Right";
			}
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
	
	private Sabertooth sb;
	private Servo servo;
	private Path path;
	private String boat;
	private Compass compass;
	
	private String mode;
	
	public Gps( String boat ) {
		super();
		this.boat = boat;
	}
	
	public String getBoat(){
		return boat;
	}

	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	
	public Compass getCompass() {
		return compass;
	}

	public void setCompass(Compass compass) {
		this.compass = compass;
	}

	public Sabertooth getSb() {
		return sb;
	}
	public void setSb(Sabertooth sb) {
		this.sb = sb;
	}
	public Servo getServo() {
		return servo;
	}
	public void setServo(Servo servo) {
		this.servo = servo;
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	
	
}
