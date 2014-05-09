package org.dit.dss.toDoListRestful.util;

public class Authentication {
	
	private  String username;
	private  String url;
	private String status;
	

	public Authentication(String username, String url, String status) {
		this.username = username;
		this.url = url;
		this.status = status;
	}


	public Authentication() {
		this.username = null;
		this.url = null;
		this.status = null;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	
}
