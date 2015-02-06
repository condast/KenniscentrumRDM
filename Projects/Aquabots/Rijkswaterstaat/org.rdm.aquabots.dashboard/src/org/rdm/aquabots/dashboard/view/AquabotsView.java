package org.rdm.aquabots.dashboard.view;

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

public class AquabotsView extends Composite {

	private static final long serialVersionUID = 1L;

	private Browser browser;
	private Spinner spinner;
	private Spinner spinner_1;
	
	// Execute JavaScript in the browser
	private BrowserCallback bcb = new BrowserCallback(){
		private static final long serialVersionUID = 1L;

		@Override
		public void evaluationSucceeded(Object result) {
			System.out.println("succeeded");	
		}

		@Override
		public void evaluationFailed(Exception exception) {
			System.out.println("failed");	
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
		spinner.setSelection(4568);
        spinner.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, "createMap(" + spinner.getSelection() + ", " + spinner_1.getSelection() + ");", bcb );   
            }
        });
		
		Label lblLatitude = new Label(composite, SWT.NONE);
		lblLatitude.setText("Latitude:");
		
		spinner_1 = new Spinner(composite, SWT.BORDER);
		spinner_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		spinner_1.setMaximum( 10000);
		spinner_1.setSelection(3567);
        spinner_1.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			public void widgetSelected(SelectionEvent e) {
				BrowserUtil.evaluate(browser, "createMap(" + spinner.getSelection() + ", " + spinner_1.getSelection() + ");", bcb );   
            }
        });
		
		browser = new Browser(sashForm, SWT.NONE);
		
		CTabItem tbtmBathymetry = new CTabItem(tabFolder, SWT.NONE);
		tbtmBathymetry.setText("Bathymetry");
		
		CTabItem tbtmSystem = new CTabItem(tabFolder, SWT.NONE);
		tbtmSystem.setText("System");

		browser.setUrl("/aquabots/index.html");
		sashForm.setWeights(new int[] {147, 294});
        browser.addProgressListener(new ProgressListener() {
			private static final long serialVersionUID = 1L;
			@Override
            public void completed(ProgressEvent event) {
                System.out.println("Page loaded");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BrowserUtil.evaluate(browser, "alert(\"JavaScript, called from Java\");", bcb );
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
