package org.rdm.aquabots.dashboard.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TypedListener;

/**
 * This component can be use to change value with a spinner.
 * 
 * @author patapouf
 * 
 */
public class DigitsSpinner extends Composite {
	private static final long serialVersionUID = 1L;

	/**
	 * Margin for label layout calculation.
	 */
	static final int VALUE_MARGIN = 2;

	/**
	 * Scale component use to change the value.
	 */
	Spinner spinner;

	/**
	 * Label showing the selected value.
	 */
	Label value;

	/**
	 * Expression to format the display value.
	 */
	String unitExpression = ""; //$NON-NLS-1$

	/**
	 * Selection listener to dispatch event.
	 */
	Listener listener = new Listener() {
		private static final long serialVersionUID = 1L;

		public void handleEvent(Event event) {
			DigitsSpinner.this.notifyListeners(event.type, event);
		}
	};

	/**
	 * Create a new bitrate editor.
	 * 
	 * @param parent
	 *            the composite parent.
	 * @param style
	 *            the style of this widgets.
	 */
	public DigitsSpinner(Composite parent, int style) {
		super(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		super.setLayout(layout);

		this.spinner = new Spinner(this, style);
		this.spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		int[] types = new int[] { SWT.Modify, SWT.Selection,
				SWT.DefaultSelection, SWT.MouseDoubleClick, SWT.MouseDown,
				SWT.MouseUp };
		for (int index = 0; index < types.length; index++) {
			this.spinner.addListener(types[index], this.listener);
		}

		this.value = new Label(this, SWT.NONE);
		this.value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		updateLayoutValue();
		this.value.setText(this.unitExpression);

	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the user changes the receiver's value, by sending it one of the
	 * messages defined in the SelectionListener interface.
	 * 
	 * @param listener
	 *            the listener which should be notified.
	 */
	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	/**
	 * Use to convert value from digits to integer.
	 * 
	 * @param dblValue
	 *            the double value.
	 * @return the integer value.
	 */
	private int fromDigits(double dblValue) {
		return (int) (dblValue * Math.pow(10, this.spinner.getDigits()));
	}

	/**
	 * Returns the number of decimal places used by the receiver.
	 * 
	 * @return the digits.
	 */
	public int getDigits() {
		return this.spinner.getDigits();
	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed.
	 * 
	 * @return the increment value.
	 */
	public double getIncrement() {
		return toDigits(this.spinner.getIncrement());
	}
	
	/**
	 * Returns the maximum value which the receiver will allow.
	 * 
	 * @return the maximum value.
	 */
	public double getMaximum() {
		return toDigits(this.spinner.getMaximum());
	}
	
	/**
	 * Returns the minimum value which the receiver will allow.
	 * 
	 * @return the minimum value.
	 */
	public double getMinimum() {
		return toDigits(this.spinner.getMinimum());
	}

	/**
	 * Returns the 'selection', which is the receiver's position.
	 * 
	 * @return the selection.
	 */
	public double getSelection() {
		return toDigits(this.spinner.getSelection());
	}

	/**
	 * Return the format expression use to display the value.
	 * 
	 * @return the format expression.
	 */
	public String getUnitString() {
		return this.unitExpression;
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the user changes the receiver's value.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified.
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		removeListener(SWT.Selection, listener);
		removeListener(SWT.DefaultSelection, listener);
	}

	/**
	 * Sets the number of decimal places used by the receiver.
	 * <p>
	 * The digit setting is used to allow for floating point values in the
	 * receiver. For example, to set the selection to a floating point value of
	 * 1.37 call setDigits() with a value of 2 and setSelection() with a value
	 * of 137. Similarly, if getDigits() has a value of 2 and getSelection()
	 * returns 137 this should be interpreted as 1.37. This applies to all
	 * numeric APIs.
	 * </p>
	 * 
	 * @param value
	 *            the new digits (must be greater than or equal to zero)
	 */
	public void setDigits(int value) {
		this.spinner.setDigits(value);
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.value.setEnabled(enabled);
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#setLayout(org.eclipse.swt.widgets.Layout)
	 */
	public void setLayout(Layout layout) {
		// Do nothing
	}
	
	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed to the argument, which must be
	 * at least one.
	 * 
	 * @param value
	 *            the increment value.
	 */
	public void setIncrement(double value) {
		this.spinner.setIncrement(fromDigits(value));
	}

	/**
	 * Sets the maximum value that the receiver will allow.
	 * 
	 * @param value
	 *            the maximum value.
	 */
	public void setMaximum(double value) {
		this.spinner.setMaximum(fromDigits(value));
	}

	/**
	 * Sets the minimum value that the receiver will allow.
	 * 
	 * @param value
	 *            the minimum value.
	 */
	public void setMinimum(double value) {
		this.spinner.setMinimum(fromDigits(value));
	}
	
	/**
	 * Sets the 'selection', which is the receiver's value, to the argument
	 * which must be greater than or equal to zero.
	 * 
	 * @param value
	 *            the value
	 */
	public void setSelection(double value) {
		this.spinner.setSelection(fromDigits(value));
	}


	/**
	 * Sets the format string to display the value. This string will be use to
	 * display the value by using the Formatter class. The string must contain
	 * %d.
	 * 
	 * @param unit
	 *            the format string.
	 */
	public void setUnitString(String unit) {
		this.unitExpression = unit;
		this.value.setText(this.unitExpression);
		updateLayoutValue();
		
	}

	/**
	 * Use to convert value to digits from integer.
	 * 
	 * @param intValue
	 *            the integer value.
	 * @return the double value.
	 */
	private double toDigits(int intValue) {
		return intValue / Math.pow(10, this.spinner.getDigits());
	}

	/**
	 * Update the display value
	 */
	private void updateLayoutValue() {

		GC gc = new GC(this);
		Point size = gc.textExtent(this.unitExpression);
		gc.dispose();

		this.value.setLayoutData(new GridData(size.x + VALUE_MARGIN, SWT.DEFAULT));
	}
}