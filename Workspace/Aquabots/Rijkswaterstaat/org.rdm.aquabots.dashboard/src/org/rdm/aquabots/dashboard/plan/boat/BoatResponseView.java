package org.rdm.aquabots.dashboard.plan.boat;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.List;
import org.rdm.aquabots.dashboard.active.boat.CurrentBoat;
import org.rdm.aquabots.dashboard.plan.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.session.ISessionListener;
import org.rdm.aquabots.dashboard.session.SessionEvent;

public class BoatResponseView extends Composite {

	private static final long serialVersionUID = 1L;
	private Text text_gps;
	private Text text_mr_left;
	private Text mtrRightText;
	private Text srvLeftText;
	private Text srvRightText;
	private CCombo boatsCombo;
	private List list;
	private List console;

	private BoatSession session = BoatSession.getInstance();
	private ISessionListener sl = new ISessionListener(){

		@Override
		public void notifySessionChanged(SessionEvent event) {
			
			refresh();		
		}	
	};
	
	private CurrentBoat boats = CurrentBoat.getInstance();
	
	private boolean disposed;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BoatResponseView(Composite parent, int style) {
		super(parent, style);
		this.disposed = false;
		this.createComposite(parent, style);
		this.session.addSessionListener(sl);
		this.session.init( Display.getDefault());
		this.session.start();
	}
	
	protected void createComposite( Composite parent, int style ){
		setLayout(new GridLayout(3, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite.widthHint = 181;
		gd_composite.heightHint = 54;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(0, 0, 55, 15);
		lblNewLabel.setText("Boat:");
		
		boatsCombo = new CCombo(composite, SWT.BORDER);
		GridData gd_boatsCombo = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_boatsCombo.widthHint = 137;
		boatsCombo.setLayoutData(gd_boatsCombo);
		boatsCombo.setItems( boats.getNames() );
		boatsCombo.setText( boats.getModel().getName());
		boatsCombo.addSelectionListener( new SelectionListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				boats.switchBoat( boatsCombo.getText() );
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
		
		Group grpPath = new Group(this, SWT.NONE);
		GridData gd_grpPath = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5);
		gd_grpPath.heightHint = 36;
		grpPath.setLayoutData(gd_grpPath);
		grpPath.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpPath.setText("Path");
		
		list = new List(grpPath, SWT.BORDER);

		Group grpConsole = new Group(this, SWT.NONE);
		GridData gd_grpConsole = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 5);
		gd_grpConsole.widthHint = 300;
		gd_grpConsole.heightHint = 36;
		grpConsole.setLayoutData(gd_grpConsole );
		grpConsole.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpConsole.setText("Console");

			console = new List(grpConsole, SWT.BORDER);
		
				Group grpGps = new Group(this, SWT.NONE);
				grpGps.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
				grpGps.setLayout(new GridLayout(2, false));
				grpGps.setText("GPS");
				
				Label lblFix = new Label(grpGps, SWT.NONE);
				lblFix.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblFix.setText("Fix");
				
				text_gps = new Text(grpGps, SWT.BORDER);
				text_gps.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);
		
		Group grpMotor = new Group(this, SWT.NONE);
		grpMotor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpMotor.setLayout(new GridLayout(2, false));
		grpMotor.setSize(70, 82);
		grpMotor.setText("Motor");
		
		Label lblLeft = new Label(grpMotor, SWT.NONE);
		lblLeft.setText("Left:");
		
		mtrRightText = new Text(grpMotor, SWT.BORDER);
		mtrRightText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRight = new Label(grpMotor, SWT.NONE);
		lblRight.setText("Right:");
		
		text_mr_left = new Text(grpMotor, SWT.BORDER);
		text_mr_left.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpServo = new Group(this, SWT.NONE);
		grpServo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpServo.setLayout(new GridLayout(2, false));
		grpServo.setText("Servo");
		
		Label lblLeft_1 = new Label(grpServo, SWT.NONE);
		lblLeft_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLeft_1.setText("Left:");
		
		srvLeftText = new Text(grpServo, SWT.BORDER);
		srvLeftText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRight_1 = new Label(grpServo, SWT.NONE);
		lblRight_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRight_1.setText("Right:");
		
		srvRightText = new Text(grpServo, SWT.BORDER);
		srvRightText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Refresh the UI
	 */
	public void refresh(){
		Display display = Display.getDefault();
		if( display.isDisposed() )
			return;
		display.asyncExec( new Runnable(){

			@Override
			public void run() {
				try{
					if( disposed )
						return;
					StatusModel sm = boats.getModel().getStatus();
					text_gps.setText( String.valueOf( sm.getHelmControl().getGPS().getFix()));
					text_mr_left.setText( 
							String.valueOf( sm.getBoatControl().getMotorController().getLeft()));
					mtrRightText.setText( 
							String.valueOf( sm.getBoatControl().getMotorController().getRight()));
					srvLeftText.setText( 
							String.valueOf( sm.getBoatControl().getServo().getLeft()));
					srvRightText.setText( 
							String.valueOf( sm.getBoatControl().getServo().getRight()));
					updateList( sm );
					updateConsole( sm );
					layout();
					update();
				}
				catch( SWTException ex){
					ex.printStackTrace();
				}
			}	
		});		
	}

	protected void updateList( StatusModel sm ){
		try{
			list.removeAll();
			WayPoint[] waypoints = sm.getHelmControl().getPath().getWayPoints(); 
			for( WayPoint wp: waypoints ){
				list.add( wp.toLongLat());
			}

			list.update();
			list.setSelection( list.getItemCount() - 1);
			list.computeSize(list.getSize().x, SWT.DEFAULT);
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	protected void updateConsole( StatusModel sm ){
		try{
			String[] split = sm.printStructure().split("[\n]");
			console.removeAll();
			for( String str: split ){
				str = str.replace("\t", "    ");
				console.add( str );
			}

			console.update();
			console.setSelection( list.getItemCount() - 1);
			console.computeSize(list.getSize().x, SWT.DEFAULT);
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	
	@Override
	public boolean setFocus() {
		this.disposed = false;
		return super.setFocus();
	}

	@Override
	public void dispose() {
		this.disposed = true;
		super.dispose();
	}	
}
