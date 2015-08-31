package org.rdm.kc.sensornet.authentication;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.rdm.authentication.core.AbstractLoginDialog;
import org.rdm.kc.sensornet.utils.ImageResources;
import org.rdm.kc.sensornet.utils.ImageResources.Images;


/**
 * Handles the callbacks to show a RCP/RAP UI for the LoginModule.
 */
public class RdmCallbackHandler extends AbstractLoginDialog {
	private static final long serialVersionUID = 1L;

	public RdmCallbackHandler() {
	    this( Display.getDefault().getActiveShell() );
	  }

	  protected RdmCallbackHandler( Shell parentShell ) {
	    super( parentShell );
		setBlockOnOpen(false);
	  }

	  @Override
	protected Point getInitialSize() {
	    return new Point( 450, 300 );
	  }

	  @Override
	protected Control createDialogArea( Composite parent ) {
	    super.setTitleImage( ImageResources.getInstance().getImage( Images.AQUABOTS3 ));
		Composite dialogarea = ( Composite )super.createDialogArea( parent );
	    dialogarea.setLayoutData( new GridData( GridData.FILL_BOTH ) );
	    Composite composite = new Composite( dialogarea, SWT.NONE );
	    composite.setLayout( new GridLayout( 2, false ) );
	    createCallbackHandlers( composite );
	    return composite;
	  }

	  private void createCallbackHandlers( Composite composite ) {
	    Callback[] callbacks = getCallbacks();
	    for( int i = 0; i < callbacks.length; i++ ) {
	      Callback callback = callbacks[ i ];
	      if( callback instanceof TextOutputCallback ) {
	        createTextOutputHandler( composite, ( TextOutputCallback )callback );
	      } else if( callback instanceof NameCallback ) {
	        createNameHandler( composite, ( NameCallback )callback );
	      } else if( callback instanceof PasswordCallback ) {
	        createPasswordHandler( composite, ( PasswordCallback )callback );
	      }
	    }
	  }

	  private void createPasswordHandler( Composite composite, final PasswordCallback callback ) {
	    Label label = new Label( composite, SWT.NONE );
	    label.setText( callback.getPrompt() );
	    final Text passwordText = new Text( composite, SWT.SINGLE
	                                                   | SWT.LEAD
	                                                   | SWT.PASSWORD
	                                                   | SWT.BORDER );
	    passwordText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

		@Override
		public void modifyText( ModifyEvent event ) {
	        callback.setPassword( passwordText.getText().toCharArray() );
	      }
	    } );
	  }

	  private void createNameHandler( Composite composite, final NameCallback callback ) {
	    Label label = new Label( composite, SWT.NONE );
	    label.setText( callback.getPrompt() );
	    final Text text = new Text( composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER );
	    text.addModifyListener( new ModifyListener() {
     		private static final long serialVersionUID = 1L;

		@Override
		public void modifyText( ModifyEvent event ) {
	        callback.setName( text.getText() );
	      }
	    } );
	  }

	  private void createTextOutputHandler( Composite composite, TextOutputCallback callback ) {
	    int messageType = callback.getMessageType();
	    int dialogMessageType = IMessageProvider.NONE;
	    switch( messageType ) {
	      case TextOutputCallback.INFORMATION:
	        dialogMessageType = IMessageProvider.INFORMATION;
	      break;
	      case TextOutputCallback.WARNING:
	        dialogMessageType = IMessageProvider.WARNING;
	      break;
	      case TextOutputCallback.ERROR:
	        dialogMessageType = IMessageProvider.ERROR;
	      break;
	    }
	    setMessage( callback.getMessage(), dialogMessageType );
	  }

	  @Override
	public void internalHandle() {
	  }

	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

	  
	
	/**
     * Invoke an array of Callbacks.
     *
     * <p>
     *
     * @param callbacks an array of <code>Callback</code> objects which contain
     *                  the information requested by an underlying security
     *                  service to be retrieved or displayed.
     *
     * @exception java.io.IOException if an input or output error occurs. <p>
     */
/*
	  public void handle(Callback[] callbacks)
    throws IOException {

        for ( Callback callback: callbacks) {
            if (callback instanceof TextOutputCallback) {

                // display the message according to the specified type
                TextOutputCallback toc = (TextOutputCallback)callback;
                switch (toc.getMessageType()) {
                case TextOutputCallback.INFORMATION:
                    System.out.println(toc.getMessage());
                    break;
                case TextOutputCallback.ERROR:
                    System.out.println("ERROR: " + toc.getMessage());
                    break;
                case TextOutputCallback.WARNING:
                    System.out.println("WARNING: " + toc.getMessage());
                    break;
                default:
                    throw new IOException("Unsupported message type: " +
                                        toc.getMessageType());
                }

            } else if (callback instanceof NameCallback) {

                // prompt the user for a username
                NameCallback nc = (NameCallback)callback;

                System.err.print(nc.getPrompt());
                System.err.flush();
                nc.setName((new BufferedReader
                        (new InputStreamReader(System.in))).readLine());

            } else if (callback instanceof PasswordCallback) {

                // prompt the user for sensitive information
                PasswordCallback pc = (PasswordCallback)callback;
                System.err.print(pc.getPrompt());
                System.err.flush();
                pc.setPassword(readPassword(System.in));

            } else {
                throw new UnsupportedCallbackException
                        (callback, "Unrecognized Callback");
            }
        }
    }

    // Reads user password from given input stream.
    private char[] readPassword(InputStream in) throws IOException {

        char[] lineBuffer;
        char[] buf;
        int i;

        buf = lineBuffer = new char[128];

        int room = buf.length;
        int offset = 0;
        int c;

loop:   while (true) {
            switch (c = in.read()) {
            case -1:
            case '\n':
                break loop;

            case '\r':
                int c2 = in.read();
                if ((c2 != '\n') && (c2 != -1)) {
                    if (!(in instanceof PushbackInputStream)) {
                        in = new PushbackInputStream(in);
                    }
                    ((PushbackInputStream)in).unread(c2);
                } else
                    break loop;

            default:
                if (--room < 0) {
                    buf = new char[offset + 128];
                    room = buf.length - offset - 1;
                    System.arraycopy(lineBuffer, 0, buf, 0, offset);
                    Arrays.fill(lineBuffer, ' ');
                    lineBuffer = buf;
                }
                buf[offset++] = (char) c;
                break;
            }
        }

        if (offset == 0) {
            return null;
        }

        char[] ret = new char[offset];
        System.arraycopy(buf, 0, ret, 0, offset);
        Arrays.fill(buf, ' ');

        return ret;
    }
*/
}
