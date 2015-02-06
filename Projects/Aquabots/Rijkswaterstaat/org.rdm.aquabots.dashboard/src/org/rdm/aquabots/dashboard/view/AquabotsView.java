package org.rdm.aquabots.dashboard.view;

import java.util.logging.Logger;

import org.eclipse.rap.rwt.widgets.BrowserCallback;
import org.eclipse.rap.rwt.widgets.BrowserUtil;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.rdm.aquabots.dashboard.utils.GeoObject;
import org.rdm.aquabots.dashboard.utils.ImageResources;
import org.rdm.aquabots.dashboard.utils.ImageResources.Images;

public class AquabotsView extends Composite {

	public static final String S_INDEX_HTML = "/aquabots/index.html";
	
	private static final long serialVersionUID = 1L;

	private Browser browser;
	private Spinner spinner;
	private Spinner spinner_1;
	
	private GeoObject geo = new GeoObject();
	
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
		
		SashForm sashForm = new SashForm(tabFolder, SWT.NONE);
		tbtmRoute_1.setControl(sashForm);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblLongtitude = new Label(composite, SWT.NONE);
		lblLongtitude.setText("Longtitude:");
		
		spinner = new Spinner(composite, SWT.BORDER);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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

		spinner_1 = new Spinner(composite, SWT.BORDER);
		spinner_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		spinner_1.setMaximum(10000);
		spinner_1.setSelection( geo.getLatitude());
		spinner_1.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				geo.setLatitude( spinner_1.getSelection());
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
			}
		});
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(3, false));
		GridData gd_composite_1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 2);
		gd_composite_1.heightHint = 93;
		composite_1.setLayoutData(gd_composite_1);
		new Label(composite_1, SWT.NONE);
		
		Button button = new Button(composite_1, SWT.NONE);
		button.setImage( ImageResources.getInstance().getImage( Images.UP ));
        button.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, geo.jump(), bcb );   
            }
        });
		new Label(composite_1, SWT.NONE);
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setImage( ImageResources.getInstance().getImage( Images.LEFT ));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, "zoomin();", bcb );   
            }
        });
		new Label(composite_1, SWT.NONE);
		
		Button btnNewButton_1 = new Button(composite_1, SWT.NONE);
		btnNewButton_1.setImage( ImageResources.getInstance().getImage( Images.RIGHT ));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, "zoomin();", bcb );   
            }
        });
		new Label(composite_1, SWT.NONE);
		
		Button btnVv = new Button(composite_1, SWT.NONE);
		btnVv.setImage( ImageResources.getInstance().getImage( Images.DOWN ));
		btnVv.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, "zoomin();", bcb );   
            }
        });
		new Label(composite_1, SWT.NONE);
		
		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.setText("zoom+");
		btnNewButton_2.setImage( ImageResources.getInstance().getImage( Images.LEFT ));
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, "zoomout();", bcb );   
            }
        });
		
		Button btnZoom = new Button(composite, SWT.NONE);
		btnZoom.setText("zoom-");
        btnZoom.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, "zoomin();", bcb );   
            }
        });
 		browser = new Browser(sashForm, SWT.NONE);
		
		CTabItem tbtmBathymetry = new CTabItem(tabFolder, SWT.NONE);
		tbtmBathymetry.setText("Bathymetry");
		
		CTabItem tbtmSystem = new CTabItem(tabFolder, SWT.NONE);
		tbtmSystem.setText("System");

		browser.setUrl( S_INDEX_HTML );
		sashForm.setWeights(new int[] {232, 209});
        browser.addProgressListener(new ProgressListener() {
			private static final long serialVersionUID = 1L;
			@Override
            public void completed(ProgressEvent event) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("Browser activated" );
            }
            @Override
            public void changed(ProgressEvent event) {
            }
        });	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	
}
