package edu.neu.webtools.domain;

import edu.neu.webtools.security.UserSession;

public class AuthResponse {
	private boolean success;
	private String authToken;
	private UserSession userSession;

	public AuthResponse(boolean success, String authToken, UserSession userSession) {
		super();
		this.success = success;
		this.authToken = authToken;
		this.userSession = userSession;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

}
