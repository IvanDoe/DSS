package org.dit.dss.toDoListRestful.util;

public class RegisterGson {

	private String username;
	private String password;
	private String name;
	
	
	public RegisterGson(String username, String password, String name) {
		this.username = username;
		this.password = password;
		this.name = name;
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegisterGson [username=" + username + ", password=" + password
				+ ", name=" + name + "]";
	}
	
}
