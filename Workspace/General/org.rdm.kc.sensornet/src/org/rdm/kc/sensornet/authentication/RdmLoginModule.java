package org.rdm.kc.sensornet.authentication;

/*
*
* Copyright (c) 2000, 2002, Oracle and/or its affiliates. All rights reserved.
*
* Redistribution and use in source and binary forms, with or
* without modification, are permitted provided that the following
* conditions are met:
*
* -Redistributions of source code must retain the above copyright
* notice, this  list of conditions and the following disclaimer.
*
* -Redistribution in binary form must reproduct the above copyright
* notice, this list of conditions and the following disclaimer in
* the documentation and/or other materials provided with the
* distribution.
*
* Neither the name of Oracle nor the names of
* contributors may be used to endorse or promote products derived
* from this software without specific prior written permission.
*
* This software is provided "AS IS," without a warranty of any
* kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
* WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
* EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
* DAMAGES OR LIABILITIES  SUFFERED BY LICENSEE AS A RESULT OF  OR
* RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR
* ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
* FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
* SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
* CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
* THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
*
* You acknowledge that Software is not designed, licensed or
* intended for use in the design, construction, operation or
* maintenance of any nuclear facility.
*/
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.rdm.authentication.core.DefaultPrincipal;
import org.rdm.kc.sensornet.utils.IOUtils;

/**
* <p> This sample LoginModule authenticates users with a password.
*
* <p> This LoginModule only recognizes one user:       testUser
* <p> testUser's password is:  testPassword
*
* <p> If testUser successfully authenticates itself,
* a <code>SamplePrincipal</code> with the testUser's user name
* is added to the Subject.
*
* <p> This LoginModule recognizes the debug option.
* If set to true in the login Configuration,
* debug messages will be output to the output stream, System.out.
*
*/
public class RdmLoginModule implements LoginModule {

	private static final String S_DEF_USERNAME = "Aquabots";

	private static final String S_VOORZ_MODULE = "[VoorZLoginModule]";
	private static final String S_AUTH_CONFIG = "/data/authentication.auth";
	
	private static final String S_ERR_INVALID_USERNAME = "User Name Incorrect";
	private static final String S_ERR_INVALID_PASSWORD = "Password Incorrect";

	// initial state
	private Subject subject;
	private CallbackHandler callbackHandler;
	@SuppressWarnings("unused")
	private Map<String,?> sharedState;
	@SuppressWarnings("unused")
	private Map<String,?> options;

	// configurable option
	private boolean debug = false;

	// the authentication status
	private boolean succeeded = false;
	private boolean commitSucceeded = false;

	// username and password
	private String username;
	private char[] password;

	// testUser's SamplePrincipal
	private DefaultPrincipal userPrincipal;
	
	/**
	 * Initialize this <code>LoginModule</code>.
	 *
	 * <p>
	 *
	 * @param subject the <code>Subject</code> to be authenticated. <p>
	 *
	 * @param callbackHandler a <code>CallbackHandler</code> for communicating
	 *                  with the end user (prompting for user names and
	 *                  passwords, for example). <p>
	 *
	 * @param sharedState shared <code>LoginModule</code> state. <p>
	 *
	 * @param options options specified in the login
	 *                  <code>Configuration</code> for this particular
	 *                  <code>LoginModule</code>.
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initialize(Subject subject,
			CallbackHandler callbackHandler,
			Map sharedState,
			Map options) {

		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;

		// initialize any configured options
		debug = "true".equalsIgnoreCase((String)options.get("debug"));
	}

	
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * Authenticate the user by prompting for a user name and password.
	 *
	 * <p>
	 *
	 * @return true in all cases since this <code>LoginModule</code>
	 *          should not be ignored.
	 *
	 * @exception FailedLoginException if the authentication fails. <p>
	 *
	 * @exception LoginException if this <code>LoginModule</code>
	 *          is unable to perform the authentication.
	 */
	public boolean login() throws LoginException {

		// prompt for a user name and password
		if (callbackHandler == null){
			this.callbackHandler = new RdmCallbackHandler();
			//throw new LoginException("Error: no CallbackHandler available " +
			//		"to garner authentication information from the user");
		}

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("user name: ");
		callbacks[1] = new PasswordCallback("password: ", false);

		try {
			callbackHandler.handle(callbacks);
			username = ((NameCallback)callbacks[0]).getName();
			char[] tmpPassword = ((PasswordCallback)callbacks[1]).getPassword();
			if (tmpPassword == null) {
				// treat a NULL password as an empty password
				tmpPassword = new char[0];
			}
			password = new char[tmpPassword.length];
			System.arraycopy(tmpPassword, 0,
					password, 0, tmpPassword.length);
			((PasswordCallback)callbacks[1]).clearPassword();

		} catch (java.io.IOException ioe) {
			throw new LoginException(ioe.toString());
		} catch (UnsupportedCallbackException uce) {
			throw new LoginException("Error: " + uce.getCallback().toString() +
					" not available to garner authentication information " +
					"from the user");
		}

		// print debugging information
		if (debug) {
			System.out.println("\t\t " + S_VOORZ_MODULE + 
					"user entered user name: " +
					username);
			System.out.print("\t\t " + S_VOORZ_MODULE + 
					"user entered password: ");
			for (int i = 0; i < password.length; i++)
				System.out.print(password[i]);
			System.out.println();
		}

		// verify the username/password
		URL url = RdmLoginModule.class.getResource( S_AUTH_CONFIG );
		if( checkLogin(username, password, url )){
			RdmLoginBean.getInstance().setUserName(username);
			succeeded = true;
			return true;
		}
		return false;
	}

	/**
	 * <p> This method is called if the LoginContext's
	 * overall authentication succeeded
	 * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
	 * succeeded).
	 *
	 * <p> If this LoginModule's own authentication attempt
	 * succeeded (checked by retrieving the private state saved by the
	 * <code>login</code> method), then this method associates a
	 * <code>SamplePrincipal</code>
	 * with the <code>Subject</code> located in the
	 * <code>LoginModule</code>.  If this LoginModule's own
	 * authentication attempted failed, then this method removes
	 * any state that was originally saved.
	 *
	 * <p>
	 *
	 * @exception LoginException if the commit fails.
	 *
	 * @return true if this LoginModule's own login and commit
	 *          attempts succeeded, or false otherwise.
	 */
	public boolean commit() throws LoginException {
		if (succeeded == false) {
			return false;
		} else {
			// add a Principal (authenticated identity)
			// to the Subject

			// assume the user we authenticated is the SamplePrincipal
			userPrincipal = new DefaultPrincipal(username);
			if (!subject.getPrincipals().contains(userPrincipal))
				subject.getPrincipals().add(userPrincipal);

			if (debug) {
				System.out.println("\t\t" + S_VOORZ_MODULE + 
						"added SamplePrincipal to Subject");
			}

			// in any case, clean out state
			username = null;
			for (int i = 0; i < password.length; i++)
				password[i] = ' ';
			password = null;

			commitSucceeded = true;
			return true;
		}
	}

	/**
	 * <p> This method is called if the LoginContext's
	 * overall authentication failed.
	 * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
	 * did not succeed).
	 *
	 * <p> If this LoginModule's own authentication attempt
	 * succeeded (checked by retrieving the private state saved by the
	 * <code>login</code> and <code>commit</code> methods),
	 * then this method cleans up any state that was originally saved.
	 *
	 * <p>
	 *
	 * @exception LoginException if the abort fails.
	 *
	 * @return false if this LoginModule's own login and/or commit attempts
	 *          failed, and true otherwise.
	 */
	public boolean abort() throws LoginException {
		if (succeeded == false) {
			return false;
		} else if (succeeded == true && commitSucceeded == false) {
			// login succeeded but overall authentication failed
			succeeded = false;
			username = null;
			if (password != null) {
				for (int i = 0; i < password.length; i++)
					password[i] = ' ';
				password = null;
			}
			userPrincipal = null;
		} else {
			// overall authentication succeeded and commit succeeded,
			// but someone else's commit failed
			logout();
		}
		return true;
	}

	/**
	 * Logout the user.
	 *
	 * <p> This method removes the <code>SamplePrincipal</code>
	 * that was added by the <code>commit</code> method.
	 *
	 * <p>
	 *
	 * @exception LoginException if the logout fails.
	 *
	 * @return true in all cases since this <code>LoginModule</code>
	 *          should not be ignored.
	 */
	public boolean logout() throws LoginException {

		subject.getPrincipals().remove(userPrincipal);
		succeeded = false;
		succeeded = commitSucceeded;
		username = null;
		if (password != null) {
			for (int i = 0; i < password.length; i++)
				password[i] = ' ';
			password = null;
		}
		userPrincipal = null;
		return true;
	}

	/**
	 * Checks the userName and password against a list of possibilities, from the given url
	 * @param userName
	 * @param password
	 * @param url
	 * @return
	 * @throws LoginException, IOException
	 */
	public static boolean checkLogin( String userName, char[] password, boolean debug ) throws LoginException{
		// verify the username/password
		boolean usernameCorrect = false;
		boolean passwordCorrect = false;
		if (userName.equals( S_DEF_USERNAME ))
			usernameCorrect = true;
		if (usernameCorrect &&
				password.length == 12 &&
				password[0] == 't' &&
				password[1] == 'e' &&
				password[2] == 's' &&
				password[3] == 't' &&
				password[4] == 'P' &&
				password[5] == 'a' &&
				password[6] == 's' &&
				password[7] == 's' &&
				password[8] == 'w' &&
				password[9] == 'o' &&
				password[10] == 'r' &&
				password[11] == 'd') {

			// authentication succeeded!!!
			passwordCorrect = true;
			if (debug)
				System.out.println("\t\t" + S_VOORZ_MODULE + 
						"authentication succeeded");
			return passwordCorrect;
		} else {

			// authentication failed -- clean out state
			if (debug)
				System.out.println("\t\t" + S_VOORZ_MODULE + 
						"authentication failed");
			userName = null;
			for (int i = 0; i < password.length; i++)
				password[i] = ' ';
			password = null;
			if (!usernameCorrect) {
				throw new FailedLoginException("User Name Incorrect");
			} else {
				throw new FailedLoginException("Password Incorrect");
			}
		}
	}
	
	/**
	 * Checks the userName and password against a list of possibilities, from the given url
	 * @param userName
	 * @param password
	 * @param url
	 * @return
	 * @throws FailedLoginException
	 */
	public static boolean checkLogin( String userName, char[] password, URL url ) throws LoginException{
		InputStream in = null;
		Scanner scanner = null;
		boolean retval = false;
		try{
			in = url.openStream();
			scanner = new Scanner( in );
			while( scanner.hasNext() ){
				String line = scanner.nextLine();
				if( line.startsWith("#"))
					continue;
				line = line.replace(";", "");
				line = line.replace(" ", "");
				String[] split = line.split("[:]");
				if(!userName.equals( split[0])){
					throw new FailedLoginException( S_ERR_INVALID_USERNAME );
				}
				char[] pwd = split[1].toCharArray();
				if( password.length != pwd.length )
					throw new FailedLoginException( S_ERR_INVALID_PASSWORD );
				for( int i=0; i<password.length; i++ ){
					if( password[i] != pwd[i] )
						throw new FailedLoginException( S_ERR_INVALID_PASSWORD );
				}
			}
			retval = true;
		}
		catch( IOException ex ){
			ex.printStackTrace();
			throw new LoginException( ex.getMessage() );
		}
		finally{
			scanner.close();
			IOUtils.closeInputStream( in );
		}
		return retval;
	}
}