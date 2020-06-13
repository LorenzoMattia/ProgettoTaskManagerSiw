package it.uniroma3.siw.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@Column(nullable = false, unique = true)
	String username;
	@Column(nullable = false, unique = true)
	String password;
	@Column(nullable = false)
	String firstname;
	@Column(nullable = false)
	String lastname;
	LocalDateTime creationTimeStamp;
	LocalDateTime lastUpdateTimeStamp;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
	List<Project> ownedProject;
	
	@ManyToMany(mappedBy = "members")
	List<Project> visibleProjects; 
	
	public User(String username, String password, String firstname, String lastname) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	public User() {
		this.ownedProject = new ArrayList<Project>();
		this.visibleProjects = new ArrayList<Project>();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public LocalDateTime getCreationTimeStamp() {
		return creationTimeStamp;
	}


	public void setCreationTimeStamp(LocalDateTime creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}


	public LocalDateTime getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}


	public void setLastUpdateTimeStamp(LocalDateTime lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}
	
	@PrePersist
	public void onPersist() {
		this.creationTimeStamp = LocalDateTime.now();
		this.lastUpdateTimeStamp = LocalDateTime.now();
	}
	
	@PreUpdate
	public void onUpdate() {
		this.lastUpdateTimeStamp = LocalDateTime.now();
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", creationTimeStamp=" + creationTimeStamp + ", lastUpdateTimeStamp="
				+ lastUpdateTimeStamp + "]";
	}


	public List<Project> getOwnedProject() {
		return ownedProject;
	}


	public void setOwnedProject(List<Project> ownedProject) {
		this.ownedProject = ownedProject;
	}


	public List<Project> getVisibleProject() {
		return visibleProjects;
	}


	public void setVisibleProject(List<Project> visibleProjects) {
		this.visibleProjects = visibleProjects;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationTimeStamp == null) ? 0 : creationTimeStamp.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastUpdateTimeStamp == null) ? 0 : lastUpdateTimeStamp.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (creationTimeStamp == null) {
			if (other.creationTimeStamp != null)
				return false;
		} else if (!creationTimeStamp.equals(other.creationTimeStamp))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastUpdateTimeStamp == null) {
			if (other.lastUpdateTimeStamp != null)
				return false;
		} else if (!lastUpdateTimeStamp.equals(other.lastUpdateTimeStamp))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
