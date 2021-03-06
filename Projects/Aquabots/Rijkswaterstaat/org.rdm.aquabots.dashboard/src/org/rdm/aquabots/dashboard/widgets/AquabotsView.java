package org.rdm.aquabots.dashboard.widgets;

import java.net.URL;
import java.util.logging.Logger;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.widgets.BrowserCallback;
import org.eclipse.rap.rwt.widgets.BrowserUtil;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.rdm.aquabots.dashboard.Activator;
import org.rdm.aquabots.dashboard.active.boat.CurrentBoat;
import org.rdm.aquabots.dashboard.active.boat.CurrentBoatEvent;
import org.rdm.aquabots.dashboard.active.boat.ICurrentBoatListener;
import org.rdm.aquabots.dashboard.authentication.RdmLoginBean;
import org.rdm.aquabots.dashboard.json.JsonUtils;
import org.rdm.aquabots.dashboard.model.GeoView;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint.Styles;
import org.rdm.aquabots.dashboard.servlet.MapSession;
import org.rdm.aquabots.dashboard.session.ISessionListener;
import org.rdm.aquabots.dashboard.session.SessionEvent;
import org.rdm.aquabots.dashboard.utils.ImageResources;
import org.rdm.aquabots.dashboard.utils.ImageResources.Images;
import org.rdm.aquabots.dashboard.utils.RandomRoutes;
import org.rdm.aquabots.dashboard.utils.StringStyler;
import org.rdm.aquabots.dashboard.utils.authentication.AuthenticationEvent;
import org.rdm.aquabots.dashboard.utils.authentication.IAuthenticationListener;
import org.rdm.aquabots.dashboard.websocket.WebSocket;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class AquabotsView extends Composite {

	public static final String S_INDEX_HTML = "/main/index.html";
	public static final String S_JAAS_CFG = "data/jaas.cfg";
	
	public static final String S_LOGIN = "Login";
	public static final String S_TRAJECTORY = "trajectory";
	public static final String S_TRAJECTORY_ITEM = "trajectoryitem";

	public static final String S_LONGTITUDE = "Longtitude:";
	public static final String S_LATITUDE = "Latitude:";
	public static final String S_ROUTE = "Route";
	public static final String S_CHART = "Chart";
	public static final String S_BOAT = "Boat";
	
	public static final String S_ERR_LOGIN1 = "Error while logging in";
	public static final String S_ERR_LOGIN2 =  "Incorrect User and/or password. Please try again.";

	private static final long serialVersionUID = 1L;

	private Browser browser;
	private DigitsSpinner spinner;
	private DigitsSpinner spinner_1;

	private Display display;
	private List list;
	private Button btnExecute;
	//private Canvas bathycanvas;
	private CCombo combo;
	private CCombo boatsCombo;
	private ToolItem tltmLogin;
	private CTabFolder tabFolder;

	private GeoView geo = GeoView.getInstance();

	private WebSocket socket;

	//private TrajectoryView trajectory = new TrajectoryView( geo );

	private CurrentBoat model = CurrentBoat.getInstance();
	
	private RandomRoutes routes;

	//private WayPointManager manager = WayPointManager.getInstance();
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
		
	private Logger logger = Logger.getLogger( this.getClass().getName() );

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

    private RdmLoginBean login = RdmLoginBean.getInstance();
	private IAuthenticationListener authlistener = new IAuthenticationListener() {

		@Override
		public void notifyLoginChanged(AuthenticationEvent event) {
			if( login.isLoggedin())
				refresh();
		}
	};

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AquabotsView(Composite parent, int style) {
		super(parent, style);
		
		this.model.init();
		this.model.addListener(listener);
		this.createComposite(parent, style);

		login.addLoginListener(authlistener);
		this.session.addSessionListener(sl);
		this.session.init( Display.getDefault());
		this.session.start();
	}

	public void createComposite( Composite parent, int style ){
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		setLayout(gridLayout);
		
		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		toolBar.setData( RWT.CUSTOM_VARIANT, "toolbar" );
		
		tltmLogin = new ToolItem(toolBar, SWT.NONE);
		tltmLogin.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				if( login.isLoggedin() ){
					login.clear();
					tltmLogin.setText( S_LOGIN );
					return;
				}
				try {
					String jaasConfigFile = S_JAAS_CFG;
					URL configUrl = Activator.getDefault().getBundle().getEntry( jaasConfigFile );
					ILoginContext secureContext = LoginContextFactory.createContext( Activator.S_CONTEXT,
							configUrl );
					secureContext.login();
					tltmLogin.setText(login.getText());
					tabFolder.setEnabled( login.isLoggedin() );
				} catch( Exception ex ) {
					MessageDialog.openError(getShell(), S_ERR_LOGIN1, S_ERR_LOGIN2 );
					ex.printStackTrace();
				}				
			}
		});
		tltmLogin.setText( S_LOGIN );

		tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tabFolder.setEnabled( login.isLoggedin() );
		
		CTabItem tbtmRoute_1 = new CTabItem(tabFolder, SWT.NONE);
		tabFolder.setData( RWT.CUSTOM_VARIANT, S_TRAJECTORY_ITEM );
		tbtmRoute_1.setText( S_ROUTE );

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmRoute_1.setControl(composite_2);
		composite_2.setLayout(new GridLayout(3, false));

		Composite composite = new Composite(composite_2, SWT.NONE);
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
		combo.select(0);
		Label lblLongtitude = new Label(grpChart, SWT.NONE);
		lblLongtitude.setText( S_LONGTITUDE);

		spinner = new DigitsSpinner(grpChart, SWT.BORDER);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		spinner.setDigits(5);
		spinner.setMaximum( 10000);
		spinner.setSelection( geo.getLongtitude());
		spinner.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				geo.setLongtitude( (float) spinner.getSelection());
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}
		});

		Label lblLatitude = new Label(grpChart, SWT.NONE);
		lblLatitude.setText( S_LATITUDE);

		spinner_1 = new DigitsSpinner(grpChart, SWT.BORDER);
		GridData gd_spinner_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_spinner_1.widthHint = 203;
		spinner_1.setLayoutData(gd_spinner_1);
		spinner_1.setDigits(5);
		spinner_1.setMaximum(10000);
		spinner_1.setSelection( geo.getLatitude());
		spinner_1.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				geo.setLatitude( (float) spinner_1.getSelection());
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}
		});
		combo.addSelectionListener( new SelectionListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				String str = StringStyler.styleToEnum( combo.getText() );
				GeoView.Location location = GeoView.Location.valueOf( str );
				String lonlat = location.toLonLat().replace("[", "");
				lonlat = lonlat.replace("]", "");
				String[] split = lonlat.split("[,]");
				GeoView geo = GeoView.getInstance();
				geo.setLongtitude( Float.parseFloat( split[0]));
				geo.setLatitude( Float.parseFloat( split[1]));
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

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
				spinner_1.setSelection(geo.getLatitude());
				BrowserUtil.evaluate(browser, geo.up(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button zoominButton = new Button(composite_1, SWT.NONE);
		zoominButton.setImage( ImageResources.getInstance().getImage( Images.ZOOM_IN ));
		zoominButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.zoomout(), bcb );   
			}
		});

		Button leftButton = new Button(composite_1, SWT.NONE);
		leftButton.setData( RWT.CUSTOM_VARIANT, "leftButton" );
		leftButton.setImage( ImageResources.getInstance().getImage( Images.LEFT ));
		leftButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				spinner.setSelection(geo.getLongtitude());
				BrowserUtil.evaluate(browser, geo.left(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button rightButton = new Button(composite_1, SWT.NONE);
		rightButton.setImage( ImageResources.getInstance().getImage( Images.RIGHT ));
		rightButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				spinner.setSelection(geo.getLongtitude());
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
				spinner_1.setSelection(geo.getLatitude());
				BrowserUtil.evaluate(browser, geo.down(), bcb );   
			}
		});

		new Label(composite_1, SWT.NONE);
		Button zoomoutButton = new Button(composite_1, SWT.NONE);
		zoomoutButton.setImage( ImageResources.getInstance().getImage( Images.ZOOM_OUT ));
		zoomoutButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.zoomin(), bcb );   
			}
		});
		browser = new Browser(composite_2, SWT.NONE);
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

		Composite composite_4 = new Composite(composite_2, SWT.NONE);
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
								model.createTrajectory();
								list.removeAll();
								String str = JsonUtils.sendMessage( model.getModel() );
								socket.sendMessage( str );
								logger.info( str );
								//btnExecute.setEnabled(false);
							}
						});
		
				Button btnAppend = new Button(grpDraw, SWT.NONE);
				btnAppend.setText("Append");
		
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

		// Add a listener to get the close button on each tab
		tabFolder.addSelectionListener( new SelectionListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.init(), bcb );
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		CTabItem tbtmBathymetry = new CTabItem(tabFolder, SWT.NONE);
		tbtmBathymetry.setText("Bathymetry");
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmBathymetry.setControl(composite_3);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));
		//bathycanvas = new BathymetryCanvas( composite_3, SWT.NONE); 
		Label drw = new Label( composite_3, SWT.NONE );
		drw.setImage( ImageResources.getInstance().getImage( Images.DEPTH ));

		//Browser echoBrowser = new Browser(tabFolder, SWT.NONE);
		//tbtmBathymetry.setControl(echoBrowser);


		CTabItem tbtmSystem = new CTabItem(tabFolder, SWT.NONE);
		tbtmSystem.setText("System");
		Composite boatComposite = new BoatResponseView(tabFolder, SWT.NONE);
		tbtmSystem.setControl( boatComposite);

		tabFolder.setSelection(0);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public boolean setFocus() {
		return super.setFocus();
	}

	@Override
	public void redraw()
    {
        
		//bathycanvas.redraw();
		super.redraw();
    }

	/**
	 * Refresh the UI
	 */
	public void refresh(){
		if( display.isDisposed() )
			return;
		display.syncExec( new Runnable(){

			@Override
			public void run() {
				if( list == null )
					return;
				try{
					list.removeAll();
					for( WayPoint wp: model.getModel().getTrajectory().getWayPoints() ){
						list.add( wp.toLongLat());
					}
					if( list.getItemCount() > 15 )
						list.remove(0);
					list.update();
					list.setSelection( list.getItemCount() - 1);
					list.computeSize(list.getSize().x, SWT.DEFAULT);
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
		routes.stop();
		this.socket.closeSocket();
		login.removeLoginListener(authlistener);
		this.model.removeListener(listener);
		super.dispose();
	}
}
