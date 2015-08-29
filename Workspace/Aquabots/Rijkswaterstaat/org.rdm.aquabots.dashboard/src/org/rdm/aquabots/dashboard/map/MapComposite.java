package org.rdm.aquabots.dashboard.map;

import java.util.logging.Logger;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.widgets.BrowserCallback;
import org.eclipse.rap.rwt.widgets.BrowserUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;
import org.rdm.aquabots.dashboard.active.boat.CurrentBoat;
import org.rdm.aquabots.dashboard.active.boat.CurrentBoatEvent;
import org.rdm.aquabots.dashboard.active.boat.ICurrentBoatListener;
import org.rdm.aquabots.dashboard.json.JsonUtils;
import org.rdm.aquabots.dashboard.model.GeoView;
import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint.Styles;
import org.rdm.aquabots.dashboard.persistence.LocationStore;
import org.rdm.aquabots.dashboard.session.ISessionListener;
import org.rdm.aquabots.dashboard.session.SessionEvent;
import org.rdm.aquabots.dashboard.utils.ImageResources;
import org.rdm.aquabots.dashboard.utils.RandomRoutes;
import org.rdm.aquabots.dashboard.utils.ImageResources.Images;
import org.rdm.aquabots.dashboard.utils.StringStyler;
import org.rdm.aquabots.dashboard.websocket.WebSocket;
import org.rdm.aquabots.dashboard.widgets.DigitsSpinner;

public class MapComposite extends Composite {
	private static final long serialVersionUID = 1L;

	public static final String S_INDEX_HTML = "/main/index.html";

	public static final String S_LOGIN = "Login";
	public static final String S_TRAJECTORY = "trajectory";
	public static final String S_TRAJECTORY_ITEM = "trajectoryitem";

	public static final String S_LONGTITUDE = "Longtitude:";
	public static final String S_LATITUDE = "Latitude:";
	public static final String S_ROUTE = "Route";
	public static final String S_CHART = "Chart";
	public static final String S_BOAT = "Boat";

	private Browser browser;
	private DigitsSpinner spinner_lon;
	private DigitsSpinner spinner_lat;
	private Spinner spinner_zoom;

	private Display display;
	private List list;
	private Button btnExecute;
	private CCombo combo;
	private CCombo boatsCombo;

	private GeoView geo = GeoView.getInstance();

	private CurrentBoat model = CurrentBoat.getInstance();
	
	private WebSocket socket;

	private RandomRoutes routes;
		
	private LocationStore store;

	private Logger logger = Logger.getLogger( this.getClass().getName() );

	private ICurrentBoatListener listener = new ICurrentBoatListener() {

		@Override
		public void notifyStatusChanged(CurrentBoatEvent event) {
			if(( display == null ) || ( display.isDisposed()))
				return;
			if( !Styles.POINT.equals( event.getWayPoint().getStyle()))
				return;
		}
	};

	private MapSession session = MapSession.getInstance();
	private ISessionListener sl = new ISessionListener(){

		@Override
		public void notifySessionChanged(SessionEvent event) {
			refresh();		
		}	
	};

	// Execute JavaScript in the browser
	private BrowserCallback bcb = new BrowserCallback(){
		private static final long serialVersionUID = 1L;

		@Override
		public void evaluationSucceeded(Object result) {
			logger.info("succeeded: " + result);	
		}

		@Override
		public void evaluationFailed(Exception exception) {
			logger.warning( "failed: " + exception);	
		}   	
	};

	public MapComposite(Composite parent, int style) {
		super(parent, style);
		store = new LocationStore();
		GeoView.getInstance().jump();
		this.model.init();
		this.model.addListener(listener);
		this.createComposite(parent, style);
		this.session.addSessionListener(sl);
		this.session.init( Display.getDefault());
		this.session.start();
	}
	
	protected void createComposite( Composite parent, int style ){
		setLayout(new GridLayout(3, false));

		Composite composite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_composite.widthHint = 304;
		composite.setLayoutData(gd_composite);
		composite.setData( RWT.CUSTOM_VARIANT, S_TRAJECTORY );
		composite.setLayout(new GridLayout(1, false));
		
		Group grpChart = new Group(composite, SWT.NONE);
		grpChart.setText( S_CHART );
		grpChart.setLayout(new GridLayout(2, false));
		GridData gd_grpChart = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gd_grpChart.heightHint = 50;
		grpChart.setLayoutData(gd_grpChart);

		combo = new CCombo(grpChart, SWT.BORDER);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		combo.setItems( GeoView.Location.getNames());
		combo.setText( geo.getLocation());
		Label lblLongtitude = new Label(grpChart, SWT.NONE);
		lblLongtitude.setText( S_LONGTITUDE);

		spinner_lon = new DigitsSpinner(grpChart, SWT.BORDER);
		spinner_lon.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		spinner_lon.setDigits(5);
		spinner_lon.setMaximum( 10000);
		spinner_lon.setSelection( geo.getLongtitude());
		spinner_lon.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				geo.setLongtitude( (float) spinner_lon.getSelection());
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}
		});

		Label lblLatitude = new Label(grpChart, SWT.NONE);
		lblLatitude.setText( S_LATITUDE);

		spinner_lat = new DigitsSpinner(grpChart, SWT.BORDER);
		GridData gd_spinner_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_spinner_1.widthHint = 203;
		spinner_lat.setLayoutData(gd_spinner_1);
		spinner_lat.setDigits(5);
		spinner_lat.setMaximum(10000);
		spinner_lat.setSelection( geo.getLatitude());
		spinner_lat.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				geo.setLatitude( (float) spinner_lat.getSelection());
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}
		});
		
		Label lblZoom = new Label(grpChart, SWT.NONE);
		lblZoom.setText("Zoom");
		
		spinner_zoom = new Spinner(grpChart, SWT.BORDER);
		spinner_zoom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		spinner_zoom.setMaximum(50);
		spinner_zoom.setIncrement(1);
		spinner_zoom.setSelection( geo.getZoom());	
		spinner_zoom.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				geo.setZoom( (int) spinner_zoom.getSelection());
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}
		});
		combo.addSelectionListener( new SelectionListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				String str = StringStyler.styleToEnum( combo.getText() );
				GeoView.createGeoView( GeoView.Location.valueOf( str ));
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//NOTHING
			}

		});		

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gd_composite_1.widthHint = 158;
		gd_composite_1.heightHint = 138;
		composite_1.setLayoutData(gd_composite_1);
		new Label(composite_1, SWT.NONE);

		Button upButton = new Button(composite_1, SWT.NONE);
		upButton.setImage( ImageResources.getInstance().getImage( Images.UP ));
		upButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				spinner_lat.setSelection(geo.getLatitude());
				BrowserUtil.evaluate(browser, geo.up(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button zoominButton = new Button(composite_1, SWT.NONE);
		zoominButton.setImage( ImageResources.getInstance().getImage( Images.ZOOM_IN ));
		zoominButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				spinner_zoom.setSelection(geo.getZoom());
				BrowserUtil.evaluate(browser, geo.zoomout(), bcb );   
			}
		});

		Button leftButton = new Button(composite_1, SWT.NONE);
		leftButton.setData( RWT.CUSTOM_VARIANT, "leftButton" );
		leftButton.setImage( ImageResources.getInstance().getImage( Images.LEFT ));
		leftButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				spinner_lon.setSelection(geo.getLongtitude());
				BrowserUtil.evaluate(browser, geo.left(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button rightButton = new Button(composite_1, SWT.NONE);
		rightButton.setImage( ImageResources.getInstance().getImage( Images.RIGHT ));
		rightButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				spinner_lon.setSelection(geo.getLongtitude());
				BrowserUtil.evaluate(browser, geo.right(), bcb );   
			}
		});

		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);

		Button downButton = new Button(composite_1, SWT.NONE);
		downButton.setImage( ImageResources.getInstance().getImage( Images.DOWN ));
		downButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				spinner_lat.setSelection(geo.getLatitude());
				BrowserUtil.evaluate(browser, geo.down(), bcb );   
			}
		});

		new Label(composite_1, SWT.NONE);
		Button zoomoutButton = new Button(composite_1, SWT.NONE);
		zoomoutButton.setImage( ImageResources.getInstance().getImage( Images.ZOOM_OUT ));
		zoomoutButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				spinner_zoom.setSelection(geo.getZoom());
				BrowserUtil.evaluate(browser, geo.zoomin(), bcb );   
			}
		});
		browser = new Browser(this, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		browser.setUrl( S_INDEX_HTML );
		browser.addProgressListener(new ProgressListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void completed(ProgressEvent event) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				BrowserUtil.evaluate(browser, geo.init(), bcb );
				logger.info("Browser activated" );
			}

			@Override
			public void changed(ProgressEvent event) {
			}
		});

		Composite composite_4 = new Composite(this, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		composite_4.setLayout(new GridLayout(3, false));

		Group grpDraw = new Group(composite_4, SWT.NONE);
		grpDraw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1));
		grpDraw.setText( S_BOAT );
		grpDraw.setLayout(new GridLayout(3, false));

		boatsCombo = new CCombo(grpDraw, SWT.BORDER);
		boatsCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		boatsCombo.setItems( model.getNames() );
		boatsCombo.select(0);
		boatsCombo.addSelectionListener( new SelectionListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.switchBoat( boatsCombo.getText() );
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		list = new List(grpDraw, SWT.BORDER | SWT.MULTI);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		display = list.getDisplay();

		btnExecute = new Button(grpDraw, SWT.NONE);
		btnExecute.setText("Execute");
		btnExecute.setEnabled(true);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				String str = JsonUtils.sendMessage( model.getModel() );
				socket.sendMessage( str );
				logger.info( str );

				model.getModel().createTrajectory();
				list.removeAll();
				btnExecute.setEnabled(false);
			}
		});

		Button btnAppend = new Button(grpDraw, SWT.NONE);
		btnAppend.setText("Append");
		btnAppend.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				String rect = GeoView.fillBounds();
				BrowserUtil.evaluate(browser, rect, bcb );   
			}
		});


		Button btnStopoverride = new Button(grpDraw, SWT.NONE);
		btnStopoverride.setText("Stop");
		btnStopoverride.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				socket.sendStop();
			}
		});
		
		socket = new WebSocket( browser );
		routes = new RandomRoutes( display, socket, 60000 );
	}

	public void focusFromTab(){
		BrowserUtil.evaluate(browser, geo.init(), bcb );		
	}
	
	/**
	 * Refresh the UI
	 */
	public void refresh(){
		if( display.isDisposed() )
			return;
		display.asyncExec( new Runnable(){

			@Override
			public void run() {
				if( list == null )
					return;
				try{
					list.removeAll();
					TrajectoryModel trajectory = model.getModel().getTrajectory(); 
					for( WayPoint wp: trajectory.getWayPoints() ){
						list.add( wp.toLongLat());
					}
					if( list.getItemCount() > 15 )
						list.remove(0);
					list.update();
					list.setSelection( list.getItemCount() - 1);
					list.computeSize(list.getSize().x, SWT.DEFAULT);
					btnExecute.setEnabled( list.getItems().length > 0);

					layout();
					update();
					//  BrowserUtil.evaluate(browser, GeneralViewFunctions.refresh(), bcb );
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
			}	
		});		
	}

	@Override
	public void dispose() {
		if( routes != null )
			routes.stop();
		this.socket.closeSocket();
		this.model.removeListener(listener);
		store.save();
		super.dispose();
	}	

}
