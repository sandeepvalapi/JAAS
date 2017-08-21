package com.za.tutorial.java.security;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class ZaLoginModule implements LoginModule {
	public static final String[][] TEST_USERS = { { "zauser1", "password1" }, { "zauser2", "password2" },
			{ "zauser3", "password3" } };
	private Subject subject = null;
	private CallbackHandler callbackHandler = null;
	private ZaPrincipal zaPrincipal = null;

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		System.out.println("ZaLoginModule.initialize...");
	}

	@Override
	public boolean login() throws LoginException {
		boolean flag = false;
		System.out.println("ZaLoginModule.login...");
		Callback[] callbackArray = new Callback[2];
		callbackArray[0] = new NameCallback("User name : ");
		callbackArray[1] = new PasswordCallback("Password : ", false);
		try {
			callbackHandler.handle(callbackArray);
			String name = ((NameCallback) callbackArray[0]).getName();
			String password = new String(((PasswordCallback) callbackArray[1]).getPassword());
			int i = 0;
			while (i < TEST_USERS.length) {
				if (TEST_USERS[i][0].equals(name) && TEST_USERS[i][1].equals(password)) {
					zaPrincipal = new ZaPrincipal(name);
					System.out.println("Authentication Success..");
					flag = true;
					break;
				}
				i++;
			}
			if (flag == false)
				throw new FailedLoginException("authentication failure...");
		} catch (IOException | UnsupportedCallbackException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean commit() throws LoginException {
		boolean flag = false;
		System.out.println("ZaLoginModule.commit...");
		if (subject != null && !subject.getPrincipals().contains(zaPrincipal)) {
			subject.getPrincipals().add(zaPrincipal);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean abort() throws LoginException {
		if (subject != null && zaPrincipal != null && subject.getPrincipals().contains(zaPrincipal))
			subject.getPrincipals().remove(zaPrincipal);
		subject = null;
		zaPrincipal = null;
		System.out.println("ZaLoginModule.abort...");
		return false;
	}

	@Override
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(zaPrincipal);
		subject = null;
		zaPrincipal = null;
		System.out.println("ZaLoginModule.logout...");
		return true;
	}

}
