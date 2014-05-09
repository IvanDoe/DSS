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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;



/**
 * The persistent class for the CLIENT database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(name="CLIENT", uniqueConstraints = @UniqueConstraint(columnNames = "Username"))
@NamedQueries({@NamedQuery(name="Client.findAll", query="SELECT c FROM Client c"),
	@NamedQuery(name="Client.login", query="SELECT c FROM Client c WHERE c.username=:user AND c.password=:pass"),
	@NamedQuery(name="Client.findByUsername", query="SELECT c FROM Client c WHERE c.username=:user")})
public class Client implements Serializable  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="`Client ID`", unique=true, nullable=false)
	private int client_ID;

	@NotNull
	@Size(min = 1, max = 45)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	@Column(name="Name", nullable=false, length=45)
	private String name;

	@NotNull
    @Size(min = 1, max = 12)
	@Column(name="Password", nullable=false, length=150)
	private String password;

	@NotNull
    @NotEmpty
    @Email
	@Column(name="Username", nullable=false, length=150)
	private String username;

	//bi-directional many-to-one association to CList
	@OneToMany(mappedBy="client")
	private List<CList> CLists;

	public Client() {
	}
	
	
	

	public int getClient_ID() {
		return this.client_ID;
	}

	public void setClient_ID(int client_ID) {
		this.client_ID = client_ID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<CList> getCLists() {
		return this.CLists;
	}

	public void setCLists(List<CList> CLists) {
		this.CLists = CLists;
	}

	public CList addCList(CList CList) {
		getCLists().add(CList);
		CList.setClient(this);

		return CList;
	}

	public CList removeCList(CList CList) {
		getCLists().remove(CList);
		CList.setClient(null);

		return CList;
	}




	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client [client_ID=" + client_ID + ", name=" + name
				+ ", password=" + password + ", username=" + username + "]";
	}




	

	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + client_ID;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	 (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (client_ID != other.client_ID)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}*/
	
	

}