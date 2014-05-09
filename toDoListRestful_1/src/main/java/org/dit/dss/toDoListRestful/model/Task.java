package org.dit.dss.toDoListRestful.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the TASK database table.
 * 
 */
@Entity
@Table(name="TASK")
@NamedQueries({@NamedQuery(name="Task.findAll", query="SELECT t FROM Task t"),
@NamedQuery(name="Task.findByPK", query="SELECT t FROM Task t WHERE t.task_ID=:task_ID"),
@NamedQuery(name="Task.findByName", query="SELECT t FROM Task t WHERE t.name=:task")})
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="`Task ID`", unique=true, nullable=false)
	private int task_ID;

	@Column(name="Name", nullable=false, length=150)
	private String name;

	//bi-directional many-to-one association to CList
	@OneToMany(mappedBy="task")
	private List<CList> CLists;

	public Task() {
	}
	
	public Task clone(){
		Task t = new Task();
		t.setName(name);
		t.setTask_ID(task_ID);
		//ignore CLists
		return t;
	}

	public int getTask_ID() {
		return this.task_ID;
	}

	public void setTask_ID(int task_ID) {
		this.task_ID = task_ID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CList> getCLists() {
		return this.CLists;
	}

	public void setCLists(List<CList> CLists) {
		this.CLists = CLists;
	}

	public CList addCList(CList CList) {
		getCLists().add(CList);
		CList.setTask(this);

		return CList;
	}

	public CList removeCList(CList CList) {
		getCLists().remove(CList);
		CList.setTask(null);

		return CList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Task [task_ID=" + task_ID + ", name=" + name + "]";
	}

}