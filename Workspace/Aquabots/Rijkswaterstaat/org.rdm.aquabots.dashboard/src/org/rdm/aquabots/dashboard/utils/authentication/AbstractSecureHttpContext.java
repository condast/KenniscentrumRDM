package org.rdm.aquabots.dashboard.utils.authentication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.osgi.service.http.HttpContext;

public abstract class AbstractSecureHttpContext implements HttpContext {

	private URL resourceBase;
	private URL configFile;
	private String realm;

	private Logger logger = Logger.getLogger( this.getClass().getName() );

	public AbstractSecureHttpContext(URL resourceBase, URL configFile, String realm) {
		this.resourceBase = resourceBase;
		this.configFile = configFile;
		this.realm = realm;
	}

	private boolean failAuthorization(HttpServletRequest request,
			HttpServletResponse response) {
		// force a session to be created
		request.getSession(true);
		response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
		try {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (IOException e) {
			// do nothing
		}
		return false;
	}

	public String getMimeType(String name) {
		return null;
	}

	public URL getResource(String name) {
		try {
			return new URL(resourceBase, name);
		} catch (MalformedURLException e) {
			logger.severe("Unable to create resource URL");
			e.printStackTrace();
			return null;
		}
	}

	public boolean handleSecurity(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String auth = request.getHeader("Authorization");
		if (auth == null)
			return failAuthorization(request, response);

		StringTokenizer tokens = new StringTokenizer(auth);
		String authscheme = tokens.nextToken();
		if (!authscheme.equals("Basic"))
			return failAuthorization(request, response);

		String base64credentials = tokens.nextToken();
		Base64 b64 = new Base64();
		String credentials = new String(b64.decode(base64credentials
				.getBytes()));
		int colon = credentials.indexOf(':');
		String userid = credentials.substring(0, colon);
		String password = credentials.substring(colon + 1);
		Subject subject = null;
		try {
			subject = login(request, userid, password);
		} catch (LoginException e) {
			return failAuthorization(request, response);
		}
		request.setAttribute(HttpContext.REMOTE_USER, userid);
		request.setAttribute(HttpContext.AUTHENTICATION_TYPE, authscheme);
		request.setAttribute(HttpContext.AUTHORIZATION, subject);
		return true;
	}

	private Subject login(HttpServletRequest request, final String userid,
			final String password) throws LoginException {
		HttpSession session = request.getSession(false);
		if (session == null)
			return null;
		ILoginContext context = (ILoginContext) session
				.getAttribute("securitycontext");
		if (context != null)
			return context.getSubject();
		context = LoginContextFactory.createContext("SimpleConfig", configFile,
				new CallbackHandler() {
			public void handle(Callback[] callbacks)
					throws IOException, UnsupportedCallbackException {
				for (int i = 0; i < callbacks.length; i++) {
					if (callbacks[i] instanceof NameCallback)
						((NameCallback) callbacks[i]).setName(userid);
					else if (callbacks[i] instanceof PasswordCallback)
						((PasswordCallback) callbacks[i])
						.setPassword(password.toCharArray());
					else
						throw new UnsupportedCallbackException(
								callbacks[i]);
				}
			}
		});
		// cause the context to try the login. don't stash the context until
		// we are successful.
		Subject result = context.getSubject();
		session.setAttribute("securitycontext", context);
		return result;
	}
}
