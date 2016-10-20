package de.marcelgross.httphero.request.property;

import android.util.Base64;

public class HttpBasicAuthentication implements Authentication {

	private final String principal;

	private final String password;

	public HttpBasicAuthentication(final String principal, final String password) {
		this.principal = principal;
		this.password = password;
	}

	@Override
	public String authenticationHeader() {
		return "Basic " + codedCredentials();
	}

	private String codedCredentials() {
		final String credentials = this.principal + ":" + this.password;
		return Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
	}
}
