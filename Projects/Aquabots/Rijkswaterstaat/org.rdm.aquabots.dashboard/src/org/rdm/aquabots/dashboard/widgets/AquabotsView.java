package org.rdm.aquabots.dashboard.widgets;

import java.util.logging.Logger;

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
import org.rdm.aquabots.dashboard.model.GeoView;
import org.rdm.aquabots.dashboard.model.TrajectoryView;
import org.rdm.aquabots.dashboard.utils.ImageResources;
import org.rdm.aquabots.dashboard.utils.ImageResources.Images;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;

public class AquabotsView extends Composite {

	public static final String S_INDEX_HTML = "/aquabots/index.html";
	
	private static final long serialVersionUID = 1L;

	private Browser browser;
	private DigitsSpinner spinner;
	private DigitsSpinner spinner_1;
	
	private GeoView geo = new GeoView();
	private TrajectoryView trajectory = new TrajectoryView();
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	// Execute JavaScript in the browser
	private BrowserCallback bcb = new BrowserCallback(){
		private static final long serialVersionUID = 1L;

		@Override
		public void evaluationSucceeded(Object result) {
			System.out.println("succeeded: " + result);	
		}

		@Override
		public void evaluationFailed(Exception exception) {
			System.out.println("failed: " + exception);	
		}   	
    };

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AquabotsView(Composite parent, int style) {
		super(parent, style);
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
				geo.setLongtitude( spinner.getSelection());
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
				geo.setLatitude( spinner_1.getSelection());
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
		gd_composite_1.heightHint = 150;
		composite_1.setLayoutData(gd_composite_1);
		new Label(composite_1, SWT.NONE);

		Button button = new Button(composite_1, SWT.NONE);
		//button.setImage( ImageResources.getInstance().getImage( Images.UP ));
		ImageResources.setImage( button, Images.UP );
		button.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.up(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setImage( ImageResources.getInstance().getImage( Images.LEFT ));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.left(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button btnNewButton_1 = new Button(composite_1, SWT.NONE);
		btnNewButton_1.setImage( ImageResources.getInstance().getImage( Images.RIGHT ));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.right(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);

		Button btnVv = new Button(composite_1, SWT.NONE);
		btnVv.setImage( ImageResources.getInstance().getImage( Images.DOWN ));
		btnVv.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.down(), bcb );   
			}
		});
		new Label(composite_1, SWT.NONE);
		new Label(composite, SWT.NONE);

		Button btnZoom = new Button(composite, SWT.NONE);
		btnZoom.setText("zoom-");

		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.setText("zoom+");
		btnNewButton_2.setImage( ImageResources.getInstance().getImage( Images.LEFT ));
		new Label(composite, SWT.NONE);
		
		Group grpDraw = new Group(composite, SWT.NONE);
		grpDraw.setText("Draw");
		grpDraw.setLayout(new GridLayout(2, false));
		grpDraw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		
		Label lblNewLabel_2 = new Label(grpDraw, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Type");
		
		Combo combo = new Combo(grpDraw, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setItems( TrajectoryView.Types.getTypes());
		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				trajectory.setType( combo.getText() );
				BrowserUtil.evaluate(browser, trajectory.setTrajectory(), bcb );   
			}
		});
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.zoomin(), bcb );   
			}
		});
		btnZoom.addSelectionListener(new SelectionAdapter() {
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
}