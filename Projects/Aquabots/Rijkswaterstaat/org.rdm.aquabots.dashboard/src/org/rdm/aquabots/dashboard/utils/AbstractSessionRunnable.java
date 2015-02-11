package org.rdm.aquabots.dashboard.utils;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.swt.widgets.Display;

/**
 * @See: http://eclipse.org/rap/developers-guide/devguide.php?topic=threads.html&version=2.3#asyncexec
 * @author Kees
 *
 */
public abstract class AbstractSessionRunnable implements Runnable{

	  final private UISession session;

	  public AbstractSessionRunnable( Display display ) {
	    session = RWT.getUISession( display );
	  }

	  @Override
	  public void run() {
	    session.exec( new Runnable() {
	      @Override
	      public void run() {
	        AbstractSessionRunnable.this.runInSession();
	      }
	    } );
	  }

	  protected abstract void runInSession();
}
