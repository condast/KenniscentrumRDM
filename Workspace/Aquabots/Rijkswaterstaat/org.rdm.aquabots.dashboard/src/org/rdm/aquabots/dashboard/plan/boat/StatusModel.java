package org.rdm.aquabots.dashboard.plan.boat;

import javax.json.stream.JsonParser;

import org.rdm.aquabots.dashboard.def.boat.IBoatConfiguration;
import org.rdm.aquabots.dashboard.def.boat.IBoatController;
import org.rdm.aquabots.dashboard.def.boat.IBoatStatusModel;
import org.rdm.aquabots.dashboard.def.boat.ICompass;
import org.rdm.aquabots.dashboard.def.boat.IGps;
import org.rdm.aquabots.dashboard.def.boat.IHelmControl;
import org.rdm.aquabots.dashboard.def.boat.IHelmControl.Helm;
import org.rdm.aquabots.dashboard.def.boat.IMotorController;
import org.rdm.aquabots.dashboard.def.boat.IServo;
import org.rdm.aquabots.dashboard.def.boat.IHelmControl.Gps;
import org.rdm.aquabots.dashboard.def.boat.IMotorController.MCTypes;
import org.rdm.aquabots.dashboard.def.boat.IMotorController.MotorControl;
import org.rdm.aquabots.dashboard.structure.AbstractStatusStructure;
import org.rdm.aquabots.dashboard.structure.IStatusStructure;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class StatusModel extends AbstractStatusStructure implements IBoatStatusModel{

	private enum ReceivedStatus{
		NAME,
		COMPASS,
		GPS,
		SABERTOOTH,
		SERVO,
		PATH;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}		

		public static boolean isValidStr( String key ){
			for( ReceivedStatus boat: values() ){
				if( boat.name().equals( key ))
					return true;
			}
			return false;
		}

	}
	
	private HelmStructure helm;
	private BoatControl control;
	private IBoatConfiguration configuration; //optional

	protected StatusModel() {
		this( null );
	}

	protected StatusModel( String boat ) {
		this( boat, null );
	}
	
	protected StatusModel( String boat, IBoatConfiguration configuration ) {
		super( boat );
		this.configuration = configuration;
		helm = new HelmStructure( this);
		super.addChild(helm);
		control = new BoatControl( this );
		super.addChild( control );
	}

	@Override
	public String getName() {
		return super.getId();
	}

	@Override
	public IHelmControl getHelmControl() {
		return helm;
	}

	@Override
	public IBoatController getBoatControl() {
		return control;
	}

	/**
	 * Create a status model from a JSON string
	 * @param object
	 * @return
	 */
	public static IBoatStatusModel createBoatStatusModel( String jsonString ){
		StatusModel model = new StatusModel(); 
		createStructure( model, jsonString );
		return model;
	}

	@Override
	public void setAttribute(String key, Object value) {
		if(!ReceivedStatus.isValidStr(key))
			return;
		ReceivedStatus status = ReceivedStatus.valueOf( key );
		switch( status ){
		case NAME:
			super.setAttribute( ID, value);
			break;
		default:
			break;
		}		
	}	

	
	@Override
	public boolean onStartObject(String key, JsonParser parser ) {
		IStatusStructure structure = null;
		if( Helm.isValidStr( StringStyler.styleToEnum( key ))){
			structure = super.getChild( IBoatStatusModel.Boat.HELM.toString() );
			super.createStructure( structure.getChild( key ) , parser);
			return true;
		}else if( MCTypes.isValidStr( StringStyler.styleToEnum( key))){
			MCTypes type = MCTypes.valueOf( StringStyler.styleToEnum( key));
			structure = super.getChild( IBoatStatusModel.Boat.CONTROLLER.toString() );
			structure.setAttribute( MotorControl.TYPE.toString(), type );
			super.createStructure( structure.getChild( IBoatController.Attributes.MOTOR_CONTROLLER.toString() ), parser);
			return true;
		}else if( IBoatStatusModel.BoatControl.isValidStr( StringStyler.styleToEnum( key))){
			structure = super.getChild( IBoatStatusModel.Boat.CONTROLLER.toString() );
			super.createStructure( structure.getChild( key ) , parser);
			return true;
		}else 
			return super.onStartObject(key, parser );
	}

	@Override
	public boolean onEndObject( String key, JsonParser parser) {
		if( Helm.isValidStr( key )){
			return true;
		}
		return super.onEndObject(key, parser);
	}


	/**
	 * Implementation of the Helm control
	 * @author Kees
	 *
	 */
	private static class HelmStructure extends AbstractStatusStructure implements IHelmControl{

		protected HelmStructure( IStatusStructure parent ) {
			super( parent, Boat.HELM.toString() );
			super.addChild( new CompassStructure( this ));
			super.addChild( new GpsStructure( this ));
			super.addChild( new Path( this ));
		}

		@Override
		public IGps getGPS() {
			return (IGps) super.getChild( Helm.GPS.toString());
		}

		@Override
		public ICompass getCompass() {
			return (ICompass) super.getChild( Helm.COMPASS.toString());
		}

		@Override
		public Path getPath() {
			return (Path) super.getChild( Helm.PATH.toString());
		}

		@Override
		public void setAttribute(String key, Object value) {
			if(!Helm.isValidStr(key))
				return;
			super.setAttribute( key, value );
		}
	}
	
	private static class CompassStructure extends AbstractStatusStructure implements ICompass{

		protected CompassStructure(IStatusStructure parent) {
			super(parent, IHelmControl.Helm.COMPASS.toString() );
		}
	
		/* (non-Javadoc)
		 * @see org.rdm.aquabots.dashboard.model.boat.ICompass#getBearing()
		 */
		@Override
		public int getBearing() {
			Object obj = super.getAttribute( Compass.BEARING.name());
			return ( obj == null )? 0: ( Integer )obj;
		}

		/* (non-Javadoc)
		 * @see org.rdm.aquabots.dashboard.model.boat.ICompass#getRoll()
		 */
		@Override
		public int getRoll() {
			Object obj = super.getAttribute( Compass.ROLL.name() );
			return ( obj == null )? 0: ( Integer )obj;
		}

		/* (non-Javadoc)
		 * @see org.rdm.aquabots.dashboard.model.boat.ICompass#getPitch()
		 */
		@Override
		public int getPitch() {
			Object obj = super.getAttribute( Compass.PITCH.name() );
			return ( obj == null )? 0: ( Integer )obj;
		}

		@Override
		public void setAttribute(String key, Object value) {
			if(!ICompass.Compass.isValidStr(key))
				return;
			super.setAttribute(key, value);
		}
		
	}

	private static class GpsStructure extends AbstractStatusStructure implements IGps{

		protected GpsStructure(IStatusStructure parent) {
			super(parent, IHelmControl.Helm.GPS.toString() );
		}
	
		/* (non-Javadoc)
		 * @see org.rdm.aquabots.dashboard.model.boat.IGps#getFix()
		 */
		@Override
		public int getFix() {
			Object obj = super.getAttribute( Gps.FIX.name());
			return ( obj == null )? 0: (Integer)obj ;
		}
		
	}

	/**
	 * Implementation of the Boat control
	 * @author Kees
	 *
	 */
	private static class BoatControl extends AbstractStatusStructure implements IBoatController{

		protected BoatControl( IStatusStructure parent ) {
			super( parent, Boat.CONTROLLER.toString() );
			super.addChild( new ServoStructure(this));
			super.addChild( new MCStructure(this));
		}

		@Override
		public IServo getServo() {
			return (IServo) super.getChild( IBoatController.Attributes.SERVO.toString());
		}

		@Override
		public IMotorController getMotorController() {
			return (IMotorController )super.getChild( IBoatController.Attributes.MOTOR_CONTROLLER.toString());
		}

		@Override
		public MotorTypes[] getSupportedTypes() {
			return MotorTypes.values();
		}

		@Override
		public void setAttribute(String key, Object value) {
			if(!IBoatController.Attributes.isValidStr(key))
				return;
			super.setAttribute(key, value);
		}
	}
	
	private static class ServoStructure extends AbstractStatusStructure implements IServo{

		protected ServoStructure(IStatusStructure parent) {
			super(parent, IBoatController.Attributes.SERVO.toString() );
		}

		@Override
		public int getLeft() {
			Object obj = super.getAttribute( Servo.LEFT.name() );
			return ( obj == null )? 0: ( Integer )obj;
		}

		@Override
		public int getRight() {
			Object obj = super.getAttribute( Servo.RIGHT.name() );
			return ( obj == null )? 0: ( Integer )obj;
		}
	}

	private static class MCStructure extends AbstractStatusStructure implements IMotorController{

		protected MCStructure(IStatusStructure parent) {
			super(parent, IBoatController.Attributes.MOTOR_CONTROLLER.toString() );
		}

		@Override
		public String getType() {
			Object obj = super.getAttribute( MotorControl.TYPE.name() );
			return ( obj == null )? "None": ( String )obj;
		}

		@Override
		public int getLeft() {
			Object obj = super.getAttribute( MotorControl.LEFT.name() );
			return ( obj == null )? 0: ( Integer )obj;
		}

		@Override
		public int getRight() {
			Object obj = super.getAttribute( MotorControl.RIGHT.name() );
			return ( obj == null )? 0: ( Integer )obj;
		}
	}
}