package org.rdm.kc.sensornet.echosounder.garmin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BathymetryCanvas extends Canvas {
	private static final long serialVersionUID = 1L;

	private DepthMap survey = DepthMap.getInstance();

	private Display display;
	private Shell shell;

	public BathymetryCanvas(Composite parent, int style) {
		super(parent, style);
		//survey.init();
		this.display = getDisplay();
		shell = new Shell( display );
		shell.addPaintListener(new PaintListener(){
			private static final long serialVersionUID = 1L;

			public void paintControl(PaintEvent e){
				Rectangle clientArea = shell.getClientArea();
				int width = clientArea.width;
				int height = clientArea.height; 
				e.gc.setBackground( display.getSystemColor(SWT.COLOR_CYAN)); 
				e.gc.fillPolygon(new int[] {0,0,width,0,width/2,height}); 

				e.gc.setLineWidth(3);
				e.gc.drawOval(5,5,40,40);
				e.gc.setLineWidth(1);
				//e.gc.setLineStyle(SWT.LINE_DOT);
				e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
				e.gc.drawRectangle(60,5,60,40); 
			}
		});
		shell.setSize(150,150);
		setBackground(getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));

	}

	@Override
	public void dispose() {
		//image.dispose();
		//display.dispose();
		super.dispose();
	}
}
