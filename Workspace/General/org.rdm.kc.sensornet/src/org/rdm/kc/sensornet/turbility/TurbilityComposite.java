package org.rdm.kc.sensornet.turbility;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;

public class TurbilityComposite extends Composite {

	public TurbilityComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Group grpGps = new Group(this, SWT.NONE);
		GridData gd_grpGps = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpGps.heightHint = 79;
		grpGps.setLayoutData(gd_grpGps);
		grpGps.setText("GPS");
		grpGps.setLayout(new GridLayout(3, false));
		
		Label lblLatitude = new Label(grpGps, SWT.NONE);
		lblLatitude.setText("Latitude: ");
		
		Label lblNewLabel_1 = new Label(grpGps, SWT.NONE);
		lblNewLabel_1.setText("New Label");
		
		Label lblNewLabel = new Label(grpGps, SWT.NONE);
		lblNewLabel.setText("New Label");
		
		Label lblLongtitude = new Label(grpGps, SWT.NONE);
		lblLongtitude.setText("Longtitude:");
		
		Label lblNewLabel_2 = new Label(grpGps, SWT.NONE);
		lblNewLabel_2.setText("New Label");
		
		Label lblNewLabel_3 = new Label(grpGps, SWT.NONE);
		lblNewLabel_3.setText("New Label");
		
		Label lblHeading = new Label(grpGps, SWT.NONE);
		lblHeading.setText("Heading:");
		
		Label lblNewLabel_4 = new Label(grpGps, SWT.NONE);
		lblNewLabel_4.setText("New Label");
		
		Label lblNewLabel_5 = new Label(grpGps, SWT.NONE);
		lblNewLabel_5.setText("New Label");
		
		Label lblQuality = new Label(grpGps, SWT.NONE);
		lblQuality.setText("Quality:");
		
		Label lblNewLabel_15 = new Label(grpGps, SWT.NONE);
		lblNewLabel_15.setText("New Label");
		new Label(grpGps, SWT.NONE);
		
		Group grpDepth = new Group(this, SWT.NONE);
		GridData gd_grpDepth = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_grpDepth.heightHint = 200;
		grpDepth.setLayoutData(gd_grpDepth);
		grpDepth.setText("Depth:");
		
		Canvas depthCanvas = new Canvas(grpDepth, SWT.NONE);
		depthCanvas.setLocation(0, 26);
		depthCanvas.setSize(192, 165);
		depthCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		
		Group grpSounder = new Group(this, SWT.NONE);
		GridData gd_grpSounder = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpSounder.heightHint = 78;
		grpSounder.setLayoutData(gd_grpSounder);
		grpSounder.setText("Sounder");
		grpSounder.setLayout(new GridLayout(3, false));
		
		Label lblDepth = new Label(grpSounder, SWT.NONE);
		lblDepth.setText("Depth:");
		
		Label lblNewLabel_6 = new Label(grpSounder, SWT.NONE);
		lblNewLabel_6.setText("New Label");
		
		Label lblNewLabel_7 = new Label(grpSounder, SWT.NONE);
		lblNewLabel_7.setText("New Label");
		new Label(grpSounder, SWT.NONE);
		
		Label lblNewLabel_8 = new Label(grpSounder, SWT.NONE);
		lblNewLabel_8.setText("New Label");
		
		Label lblNewLabel_9 = new Label(grpSounder, SWT.NONE);
		lblNewLabel_9.setText("New Label");
		
		Group grpCourse = new Group(this, SWT.NONE);
		grpCourse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpCourse.setText("Course:");
		grpCourse.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel_10 = new Label(grpCourse, SWT.NONE);
		lblNewLabel_10.setText("Speed:");
		
		Label lblNewLabel_12 = new Label(grpCourse, SWT.NONE);
		lblNewLabel_12.setText("New Label");
		
		Label lblNewLabel_11 = new Label(grpCourse, SWT.NONE);
		lblNewLabel_11.setText("New Label");
		
		Label lblHeading_1 = new Label(grpCourse, SWT.NONE);
		lblHeading_1.setBounds(0, 0, 55, 15);
		lblHeading_1.setText("Heading:");
		
		Label lblNewLabel_13 = new Label(grpCourse, SWT.NONE);
		lblNewLabel_13.setText("New Label");
		
		Label lblNewLabel_14 = new Label(grpCourse, SWT.NONE);
		lblNewLabel_14.setText("New Label");
		
		Button btnRadioButton = new Button(grpCourse, SWT.RADIO);
		btnRadioButton.setText("Degress:");
		new Label(grpCourse, SWT.NONE);
		new Label(grpCourse, SWT.NONE);
		
		Group grpTemperature = new Group(this, SWT.NONE);
		GridData gd_grpTemperature = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_grpTemperature.heightHint = 200;
		grpTemperature.setLayoutData(gd_grpTemperature);
		grpTemperature.setText("Temperature:");
		
		Canvas tempCanvas = new Canvas(grpTemperature, SWT.NONE);
		tempCanvas.setLocation(10, 37);
		tempCanvas.setSize(201, 151);
		
		Group grpControl = new Group(this, SWT.NONE);
		GridData gd_grpControl = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpControl.heightHint = 115;
		grpControl.setLayoutData(gd_grpControl);
		grpControl.setText("Control:");
		
		Button btnClear = new Button(grpControl, SWT.NONE);
		btnClear.setBounds(10, 71, 75, 25);
		btnClear.setText("Clear");
	}
}
