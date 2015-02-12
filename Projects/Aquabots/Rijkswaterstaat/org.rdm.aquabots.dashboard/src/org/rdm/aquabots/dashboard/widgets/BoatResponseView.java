package org.rdm.aquabots.dashboard.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.List;
import org.rdm.aquabots.dashboard.utils.AbstractUIJob;

public class BoatResponseView extends Composite {

	private static final long serialVersionUID = 1L;
	private Text text;
	private Text text_1;
	private Text mtrRightText;
	private Text srvLeftText;
	private Text srvRightText;

	private UIJob uijob;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BoatResponseView(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Group grpGps = new Group(this, SWT.NONE);
		grpGps.setLayout(new GridLayout(2, false));
		GridData gd_grpGps = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpGps.heightHint = 48;
		gd_grpGps.widthHint = 297;
		grpGps.setLayoutData(gd_grpGps);
		grpGps.setText("GPS");
		
		Label lblFix = new Label(grpGps, SWT.NONE);
		lblFix.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFix.setText("Fix");
		
		text = new Text(grpGps, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpPath = new Group(this, SWT.NONE);
		grpPath.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpPath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		grpPath.setText("Path");
		
		List list = new List(grpPath, SWT.BORDER);
		
		Group grpMotor = new Group(this, SWT.NONE);
		grpMotor.setLayout(new GridLayout(2, false));
		GridData gd_grpMotor = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_grpMotor.heightHint = 80;
		gd_grpMotor.widthHint = 157;
		grpMotor.setLayoutData(gd_grpMotor);
		grpMotor.setSize(70, 82);
		grpMotor.setText("Motor");
		
		Label lblLeft = new Label(grpMotor, SWT.NONE);
		lblLeft.setText("Left:");
		
		mtrRightText = new Text(grpMotor, SWT.BORDER);
		mtrRightText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRight = new Label(grpMotor, SWT.NONE);
		lblRight.setText("Right:");
		
		text_1 = new Text(grpMotor, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpServo = new Group(this, SWT.NONE);
		grpServo.setLayout(new GridLayout(2, false));
		GridData gd_grpServo = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpServo.heightHint = 87;
		gd_grpServo.widthHint = 128;
		grpServo.setLayoutData(gd_grpServo);
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
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private static class UIJob extends AbstractUIJob{

		public UIJob(Display display) {
			super(display);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onJobStarted() {
			// TODO Auto-generated method stub
			
		}
	};	
}
