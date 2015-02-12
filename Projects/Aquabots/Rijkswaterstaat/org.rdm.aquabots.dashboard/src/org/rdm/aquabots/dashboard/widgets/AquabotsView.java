package org.rdm.aquabots.dashboard.widgets;

import java.util.logging.Logger;

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
import org.rdm.aquabots.dashboard.history.TrajectoryHistory;
import org.rdm.aquabots.dashboard.json.JsonUtils;
import org.rdm.aquabots.dashboard.json.PredefinedRoutes;
import org.rdm.aquabots.dashboard.model.GeoView;
import org.rdm.aquabots.dashboard.model.ITrajectoryListener;
import org.rdm.aquabots.dashboard.model.TrajectoryEvent;
import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint.Styles;
import org.rdm.aquabots.dashboard.utils.AbstractEventBuffer;
import org.rdm.aquabots.dashboard.utils.AbstractUIJob;
import org.rdm.aquabots.dashboard.utils.ImageResources;
import org.rdm.aquabots.dashboard.utils.ImageResources.Images;
import org.rdm.aquabots.dashboard.utils.RandomRoutes;
import org.rdm.aquabots.dashboard.utils.StringStyler;
import org.rdm.aquabots.dashboard.websocket.WebSocket;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.CCombo;

public class AquabotsView extends Composite {

	public static final String S_INDEX_HTML = "/main/index.html";

	private static final long serialVersionUID = 1L;

	private Browser browser;
	private DigitsSpinner spinner;
	private DigitsSpinner spinner_1;

	Display display;
	private List list;
	private Button btnExecute;
	//private Canvas bathycanvas;

	private GeoView geo = GeoView.getInstance();

	private WebSocket socket;

	//private TrajectoryView trajectory = new TrajectoryView( geo );

	private TrajectoryModel model = TrajectoryModel.getInstance();
	private TrajectoryHistory history;
	
	private RandomRoutes routes;

	//private WayPointManager manager = WayPointManager.getInstance();
	private ITrajectoryListener listener = new ITrajectoryListener() {

		@Override
		public synchronized void notifyTrajectoryChanged( TrajectoryEvent event) {
			if(( display == null ) || ( display.isDisposed()))
				return;
			if( !Styles.POINT.equals( event.getWayPoint().getStyle()))
				return;
			buffer.push(event);
		}
	};

	AbstractEventBuffer<TrajectoryEvent> buffer = new AbstractEventBuffer<TrajectoryEvent>( 100 ){

		@Override
		protected void onEventReceived(TrajectoryEvent last) {
			//listjob.start();

			display.asyncExec( new Runnable(){

				@Override
				public void run() {
					if( btnExecute != null ){
						btnExecute.setEnabled( true );
					}
					if( list == null )
						return;
					while( !isEmpty() ){
						list.add( poll().getWayPoint().toLongLat());
					}
					if( list.getItemCount() > 5 )
						list.remove(0);
				}});
			//refresh();
		}
	};
	private AbstractUIJob listjob = new AbstractUIJob( display ){
		
		@Override
		protected void onJobStarted() {
			if( btnExecute != null ){
				btnExecute.setEnabled( true );
			}
			if( list == null )
				return;
			while( !buffer.isEmpty() ){
				list.add( buffer.poll().getWayPoint().toLongLat());
			}
			if( list.getItemCount() > 5 )
				list.remove(0);
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

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AquabotsView(Composite parent, int style) {
		super(parent, style);
		this.buffer.start();
		this.model.clear();
		this.model.addListener(listener);
		this.history = new TrajectoryHistory();
		this.createComposite(parent, style);
	}

	public void createComposite( Composite parent, int style ){

		setLayout(new FillLayout(SWT.HORIZONTAL));

		CTabFolder tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tbtmRoute_1 = new CTabItem(tabFolder, SWT.NONE);
		tabFolder.setData( RWT.CUSTOM_VARIANT, "trajectoryItem" );
		tbtmRoute_1.setText("Route");

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmRoute_1.setControl(composite_2);
		composite_2.setLayout(new GridLayout(2, false));

		Composite composite = new Composite(composite_2, SWT.NONE);
		composite.setData( RWT.CUSTOM_VARIANT, "trajectory" );
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_composite.widthHint = 296;
		gd_composite.heightHint = 392;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(4, false));
		
		Group grpAftica = new Group(composite, SWT.NONE);
		grpAftica.setText("Aftica");
		grpAftica.setLayout(new GridLayout(2, false));
		GridData gd_grpAftica = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1);
		gd_grpAftica.heightHint = 100;
		grpAftica.setLayoutData(gd_grpAftica);

		CCombo combo = new CCombo(grpAftica, SWT.BORDER);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		combo.setItems( GeoView.Location.getNames());
		combo.select(0);
		Label lblLongtitude = new Label(grpAftica, SWT.NONE);
		lblLongtitude.setText("Longtitude:");
		lblLongtitude.setData( RWT.CUSTOM_VARIANT, "customLabel" );

		spinner = new DigitsSpinner(grpAftica, SWT.BORDER);
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

		Label lblLatitude = new Label(grpAftica, SWT.NONE);
		lblLatitude.setText("Latitude:");

		spinner_1 = new DigitsSpinner(grpAftica, SWT.BORDER);
		spinner_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
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
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
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

		Group grpDraw = new Group(composite, SWT.NONE);
		grpDraw.setText("Trajectory");
		grpDraw.setLayout(new GridLayout(2, false));
		GridData gd_grpDraw = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gd_grpDraw.heightHint = 100;
		gd_grpDraw.widthHint = 264;
		grpDraw.setLayoutData(gd_grpDraw);

		CCombo examplesCombo = new CCombo(grpDraw, SWT.BORDER);
		examplesCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		examplesCombo.setItems( PredefinedRoutes.Routes.getNames() );
		examplesCombo.select(0);
		examplesCombo.addSelectionListener( new SelectionListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				String str = StringStyler.styleToEnum( examplesCombo.getText() );
				PredefinedRoutes.Routes route = PredefinedRoutes.Routes.valueOf( str );
				TrajectoryModel path = PredefinedRoutes.getTrajectory( route);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

		});

		list = new List(grpDraw, SWT.BORDER | SWT.MULTI);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 1));
		
		Button btnRefreshButton = new Button(grpDraw, SWT.NONE);
		btnRefreshButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnRefreshButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				//routes.start();
			}
		});
		btnRefreshButton.setText("Refresh");
		display = list.getDisplay();

		btnExecute = new Button(composite, SWT.NONE);
		btnExecute.setText("Execute");
		btnExecute.setEnabled(false);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				TrajectoryModel sendModel = model.createTrajectory();
				if( sendModel == null )
					return;
				history.addTrajectory(sendModel);
				list.removeAll();
				String str = JsonUtils.sendMessage( sendModel );
				socket.sendMessage( str );
				logger.info( str );
				btnExecute.setEnabled(false);
			}
		});
		new Label(composite, SWT.NONE);

		Button btnAppend = new Button(composite, SWT.NONE);
		btnAppend.setText("Append");

		Button btnStopoverride = new Button(composite, SWT.NONE);
		btnStopoverride.setText("Stop");
		btnStopoverride.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				socket.sendStop();
			}
		});
		browser = new Browser(composite_2, SWT.NONE);
		GridData gd_browser = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_browser.heightHint = 367;
		browser.setLayoutData(gd_browser);

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
		display.syncExec( new Runnable(){

			@Override
			public void run() {
				list.update();
				list.setSelection( list.getItemCount() - 1);
				list.computeSize(list.getSize().x, SWT.DEFAULT);
				layout();
				update();
				//try{
				//  BrowserUtil.evaluate(browser, GeneralViewFunctions.refresh(), bcb );
				//}
				//catch( Exception ex ){
				//	/* ignore */
				//}
			}	
		});		
	}

	@Override
	public void dispose() {
		routes.stop();
		this.socket.closeSocket();
		this.buffer.stop();
		this.model.removeListener(listener);
		super.dispose();
	}
}