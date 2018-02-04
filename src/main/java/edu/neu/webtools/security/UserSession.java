package edu.neu.webtools.security;

public class UserSession {
	private long userId;
	private String username;
	private String firstName;
	private String lastName;
	private String roleName;

	public UserSession(long userId, String username, String firstName, String lastName, String roleName) {
		super();
		this.userId = userId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roleName = roleName;
	}

	public long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getRoleName() {
		return roleName;
	}

}