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
import org.rdm.aquabots.dashboard.json.JSonUtils;
import org.rdm.aquabots.dashboard.model.GeoView;
import org.rdm.aquabots.dashboard.model.ITrajectoryListener;
import org.rdm.aquabots.dashboard.model.TrajectoryEvent;
import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint.Styles;
import org.rdm.aquabots.dashboard.utils.ImageResources;
import org.rdm.aquabots.dashboard.utils.ImageResources.Images;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

public class AquabotsView extends Composite {

	public static final String S_INDEX_HTML = "/aquabots/index.html";
	
	private static final long serialVersionUID = 1L;

	private Browser browser;
	private DigitsSpinner spinner;
	private DigitsSpinner spinner_1;
	
	Display display;
	private List list;
	private Button btnExecute;
	
	private GeoView geo = new GeoView();
	
	//private TrajectoryView trajectory = new TrajectoryView( geo );
	
	private TrajectoryModel model = TrajectoryModel.getInstance();
	
	//private WayPointManager manager = WayPointManager.getInstance();
	private ITrajectoryListener listener = new ITrajectoryListener() {
		
		@Override
		public void notifyTrajectoryChanged( TrajectoryEvent event) {
			if(( display == null ) || ( display.isDisposed()))
				return;
			if( !Styles.POINT.equals( event.getWayPoint().getStyle()))
				return;
		    display.asyncExec( new Runnable(){

				@Override
				public void run() {
					if( btnExecute != null ){
						btnExecute.setEnabled( true );
					}
					if( list == null )
						return;
					list.add( event.getWayPoint().toLongLat());
					if( list.getItemCount() > 5 )
						list.remove(0);
					list.update();
					list.setSelection( list.getItemCount() - 1);
					display.update();
				}
				
			});
			
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
		this.model.clear();
		this.model.addListener(listener);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CTabFolder tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmRoute_1 = new CTabItem(tabFolder, SWT.NONE);
		tbtmRoute_1.setText("Route");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmRoute_1.setControl(composite_2);
		composite_2.setLayout(new GridLayout(2, false));
		
		Composite composite = new Composite(composite_2, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_composite.heightHint = 392;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_lblNewLabel.heightHint = 22;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_lblNewLabel_1.heightHint = 24;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label lblLongtitude = new Label(composite, SWT.NONE);
		lblLongtitude.setText("Longtitude:");
		
		spinner = new DigitsSpinner(composite, SWT.BORDER);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
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

		Label lblLatitude = new Label(composite, SWT.NONE);
		lblLatitude.setText("Latitude:");

		spinner_1 = new DigitsSpinner(composite, SWT.BORDER);
		spinner_1.setDigits(5);
		spinner_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,2, 1));
		spinner_1.setMaximum(10000);
		spinner_1.setSelection( geo.getLatitude());
		spinner_1.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				geo.setLatitude( (float) spinner_1.getSelection());
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}
		});
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(3, false));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd_composite_1.widthHint = 113;
		gd_composite_1.heightHint = 107;
		composite_1.setLayoutData(gd_composite_1);
		new Label(composite_1, SWT.NONE);

		Button upButton = new Button(composite_1, SWT.NONE);
		upButton.setData( RWT.CUSTOM_VARIANT, "upButton" );
		//ImageResources.setImage( upButton, Images.UP );
		upButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.up(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button leftButton = new Button(composite_1, SWT.NONE);
		leftButton.setData( RWT.CUSTOM_VARIANT, "leftButton" );
		//ImageResources.setImage( leftButton, Images.LEFT );
		leftButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.left(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button rightButton = new Button(composite_1, SWT.NONE);
		rightButton.setImage( ImageResources.getInstance().getImage( Images.RIGHT ));
		rightButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.right(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button downButton = new Button(composite_1, SWT.NONE);
		downButton.setImage( ImageResources.getInstance().getImage( Images.DOWN ));
		downButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.down(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);
		new Label(composite, SWT.NONE);

		Button zoominButton = new Button(composite, SWT.NONE);
		zoominButton.setText("zoom-");

		Button zoomoutButton = new Button(composite, SWT.NONE);
		zoomoutButton.setText("zoom+");
		zoomoutButton.setImage( ImageResources.getInstance().getImage( Images.LEFT ));
		new Label(composite, SWT.NONE);
		
		Group grpDraw = new Group(composite, SWT.NONE);
		grpDraw.setText("Draw");
		grpDraw.setLayout(new GridLayout(1, false));
		grpDraw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		
		list = new List(grpDraw, SWT.BORDER | SWT.MULTI);
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, true, false, 0, 1);
		gd_list.widthHint = 135;
		list.setLayoutData(gd_list);
		display = list.getDisplay();
		
		btnExecute = new Button(composite, SWT.NONE);
		btnExecute.setEnabled(false);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				TrajectoryModel sendModel = model.createTrajectory();
				if( sendModel == null )
					return;
				String str = JSonUtils.sendMessage( sendModel);
				BrowserUtil.evaluate(browser, str, bcb );
				logger.info( str );
				btnExecute.setEnabled(false);
			}
		});
		btnExecute.setText("Execute");
		
		Button btnOverride = new Button(composite, SWT.NONE);
		btnOverride.setText("Override");
		
		Button btnStopoverride = new Button(composite, SWT.NONE);
		btnStopoverride.setText("Stop");
		zoomoutButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.zoomin(), bcb );   
			}
		});
		zoominButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.zoomout(), bcb );   
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
		
		CTabItem tbtmSystem = new CTabItem(tabFolder, SWT.NONE);
		tbtmSystem.setText("System");
		
		tabFolder.setSelection(0);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public boolean setFocus() {
        BrowserUtil.evaluate(browser, geo.init(), bcb );
		return super.setFocus();
	}

	@Override
	public void dispose() {
		this.model.removeListener(listener);
		super.dispose();
	}
	
	
}