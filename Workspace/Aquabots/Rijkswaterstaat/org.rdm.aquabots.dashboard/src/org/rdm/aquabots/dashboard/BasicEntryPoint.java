package org.rdm.aquabots.dashboard;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.rdm.aquabots.dashboard.widgets.AquabotsView;


public class BasicEntryPoint extends AbstractEntryPoint {
	private static final long serialVersionUID = 1L;

	@Override
    protected void createContents(Composite parent) {
        parent.setLayout(new FillLayout());
        new AquabotsView( parent, SWT.BORDER );
    }

}
