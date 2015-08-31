package org.rdm.kc.sensornet;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.rdm.kc.sensornet.view.SensorView;


public class BasicEntryPoint extends AbstractEntryPoint {
	private static final long serialVersionUID = 1L;

	@Override
    protected void createContents(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        new SensorView( parent, SWT.NONE );
    }

}
