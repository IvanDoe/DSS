package org.dit.dss.toDoListRestful.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the C_LIST database table.
 * 
 */
@Entity
@Table(name="C_LIST")
@NamedQueries({@NamedQuery(name="CList.findAll", query="SELECT c FROM CList c"),
	@NamedQuery(name="CList.findByUserPK", query="SELECT c FROM CList c WHERE c.client.client_ID=:clientID"),
	@NamedQuery(name="CList.findByTask_ClientPK", query="SELECT c FROM CList c WHERE c.task.task_ID=:taskID AND c.client.client_ID=:clientID")})
public class CList implements  Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="`List ID`", unique=true, nullable=false)
	private int list_ID;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="`Date Due`")
	private Date date_Due;

	@Column(name="`Task ID`", nullable=true, insertable=false, updatable=false)
	private int task_ID;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="`Client ID`")
	private Client client;

	//bi-directional many-to-one association to Task
	@ManyToOne
	@JoinColumn(name="`Task ID`")
	private Task task;

	public CList() {
	}
	
	/*public List<CList> findAll() {
		EntityManager em = PersistenceUtil.emf.createEntityManager();
			List<CList> rows = (List<CList>) em.createNamedQuery("CList.findAll").getResultList();
			em.close();
			if (rows.size() == 0)
				return null;
			else 
				return rows;
	}*/

	public int getList_ID() {
		return this.list_ID;
	}

	public void setList_ID(int list_ID) {
		this.list_ID = list_ID;
	}

	public Date getDate_Due() {
		return this.date_Due;
	}

	public void setDate_Due(Date date_Due) {
		this.date_Due = date_Due;
	}

	public int getTask_ID() {
		return this.task_ID;
	}

	public void setTask_ID(int task_ID) {
		this.task_ID = task_ID;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CList [list_ID=" + list_ID + ", date_Due=" + date_Due
				+ ", task_ID=" + task_ID + ", client=" + client + ", task="
				+ task + "]";
	}
	
	

}