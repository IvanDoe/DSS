package org.dit.dss.toDoListRestful.util;

public class TaskEditGson {

	private String oldTask;
	private String newTask;
	
	
	public TaskEditGson(String oldTask, String newTask) {
		this.oldTask =oldTask;
		this.newTask = newTask;
	}


	/**
	 * @return the oldTask
	 */
	public String getOldTask() {
		return oldTask;
	}


	/**
	 * @param oldTask the oldTask to set
	 */
	public void setOldTask(String oldTask) {
		this.oldTask = oldTask;
	}


	/**
	 * @return the newTask
	 */
	public String getNewTask() {
		return newTask;
	}


	/**
	 * @param newTask the newTask to set
	 */
	public void setNewTask(String newTask) {
		this.newTask = newTask;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TaskEditGson [oldTask=" + oldTask + ", newTask=" + newTask
				+ "]";
	}

}
