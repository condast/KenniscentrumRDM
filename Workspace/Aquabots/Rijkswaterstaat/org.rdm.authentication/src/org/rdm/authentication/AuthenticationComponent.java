package org.rdm.authentication;

import java.util.ArrayList;
import java.util.Collection;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class AuthenticationComponent {

	Collection<LoginModule> modules;
	
	
	protected AuthenticationComponent() {
		super();
		modules = new ArrayList<LoginModule>();
	}

	public void activate(){}
	
	public void deactivate(){}
	
	public void addModule( LoginModule module ){
		this.modules.add( module );
	}

	public void removeModule( LoginModule module ){
		this.modules.remove( module );
	}

	/*
	private final void loadModule( LoginModule module ){
		ClassLoader loader = LoginModule.class.getClassLoader();
		loader.
				Thread.currentThread().getContextClassLoader();

		Class<SomeClassInMyBundle> classFromBundle = 
				SomeClassInMyBundle.class;

		ClassLoader classloaderWithSomeClassFromBundle = classFromBundle.getClassLoader();

		Thread.currentThread().setContextClassLoader(
				classloaderWithSomeClassFromBundle);

		final String applicationName = "myapp";
		LoginContext lc;
		try {

			lc = new LoginContext(applicationName, subject, 
					jaasCallbackHandler, configuration);

		} catch (LoginException e) {

			LOGGER.error("LoginContext#<init> failed because LoginException" +
				"username="+ suppliedUsername, e);
			return false;

		} finally {
			Thread.currentThread().setContextClassLoader(myBundleClassloader);
		}

		try {
			lc.login();
			LOGGER.info("login success for username=" + suppliedUsername);
			return true;
		} catch (LoginException e) {

			LOGGER.error("LoginContext#login failed because LoginException" + 
				"username="+ suppliedUsername, e);

			return false;
		}
	}
	*/
}