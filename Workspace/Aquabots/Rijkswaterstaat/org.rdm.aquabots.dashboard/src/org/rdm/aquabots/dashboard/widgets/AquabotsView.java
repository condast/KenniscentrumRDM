package org.rdm.aquabots.dashboard.widgets;

import java.net.URL;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.rdm.aquabots.dashboard.Activator;
import org.rdm.aquabots.dashboard.authentication.RdmLoginBean;
import org.rdm.aquabots.dashboard.map.MapComposite;
import org.rdm.aquabots.dashboard.map.MapSession;
import org.rdm.aquabots.dashboard.model.boat.BoatResponseView;
import org.rdm.aquabots.dashboard.utils.ImageResources;
import org.rdm.aquabots.dashboard.utils.ImageResources.Images;
import org.rdm.authentication.core.AuthenticationEvent;
import org.rdm.authentication.core.IAuthenticationListener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class AquabotsView extends Composite {

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

	private MapSession session = MapSession.getInstance();

	//private Canvas bathycanvas;
	private ToolItem tltmLogin;
	private CTabFolder tabFolder;
		
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
		
		this.createComposite(parent, style);

		login.addLoginListener(authlistener);
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
					ILoginContext secureContext = LoginContextFactory.createContext( Activator.S_CONTEXT, configUrl );
					//Thread.currentThread().setContextClassLoader( new SecureClassLoader( Thread.currentThread().getContextClassLoader(), RdmLoginModule.class.getClassLoader() ));
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
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tabFolder.widthHint = 595;
		gd_tabFolder.heightHint = 451;
		tabFolder.setLayoutData(gd_tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tabFolder.setEnabled( login.isLoggedin() );
		
		CTabItem tbtmRoute_1 = new CTabItem(tabFolder, SWT.NONE);
		tabFolder.setData( RWT.CUSTOM_VARIANT, S_TRAJECTORY_ITEM );
		tbtmRoute_1.setText( S_ROUTE );

		final MapComposite composite_route = new MapComposite(tabFolder, SWT.NONE);
		tbtmRoute_1.setControl(composite_route);

		// Add a listener to get the close button on each tab
		tabFolder.addSelectionListener( new SelectionListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				composite_route.focusFromTab();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TNOTHING
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
		
		CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Turbility");
		
		Composite composite_5 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_5);
		composite_5.setLayout(new GridLayout(1, false));
		
		Label lblNewLabel = new Label(composite_5, SWT.NONE);
		lblNewLabel.setText("New Label");
		
		Composite turbilityCanvas = new Composite(composite_5, SWT.NONE);
		turbilityCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

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
		Display display = Display.getDefault();
		if( display.isDisposed() )
			return;
		display.asyncExec( new Runnable(){

			@Override
			public void run() {
				try{
					layout();
					update();
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
			}	
		});		
	}

	@Override
	public void dispose() {
		login.removeLoginListener(authlistener);
		super.dispose();
	}	
}