package org.dit.dss.toDoListRestful.util;

public class TaskGson {

	private String username;
	private String task;
	
	
	public TaskGson(String username, String task) {
		this.username = username;
		this.task = task;
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
	 * @return the task
	 */
	public String getTask() {
		return task;
	}


	/**
	 * @param task the task to set
	 */
	public void setTask(String task) {
		this.task = task;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TaskGson [username=" + username + ", task=" + task + "]";
	}
	
	
	
}
