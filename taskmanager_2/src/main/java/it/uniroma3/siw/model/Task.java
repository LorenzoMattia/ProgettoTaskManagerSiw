package it.uniroma3.siw.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@Column(nullable = false, unique = true)
	String name;
	String description;
	boolean completed;
	LocalDateTime creationTimeStamp;
	LocalDateTime lastUpdateTimeStamp;
	
	@ManyToOne
	//@Column(nullable = false)
	Project project;
	
	@ManyToMany
	List<Tag> tags;
	
	@ManyToMany
	List<User> members;
	
	@OneToMany(mappedBy = "task")
	List<Comment> comments;
	
	public Task() {
		this.tags = new ArrayList<Tag>();
		this.members = new ArrayList<User>();
		this.comments = new ArrayList<Comment>();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setCompleted(boolean esito) {
		this.completed = esito;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
		return "task [id=" + id + ", name=" + name + ", description=" + description + ", creationTimeStamp="
				+ creationTimeStamp + ", lastUpdateTimeStamp=" + lastUpdateTimeStamp + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (completed ? 1231 : 1237);
		result = prime * result + ((creationTimeStamp == null) ? 0 : creationTimeStamp.hashCode());
		result = prime * result + ((lastUpdateTimeStamp == null) ? 0 : lastUpdateTimeStamp.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Task other = (Task) obj;
		if (completed != other.completed)
			return false;
		if (creationTimeStamp == null) {
			if (other.creationTimeStamp != null)
				return false;
		} else if (!creationTimeStamp.equals(other.creationTimeStamp))
			return false;
		if (lastUpdateTimeStamp == null) {
			if (other.lastUpdateTimeStamp != null)
				return false;
		} else if (!lastUpdateTimeStamp.equals(other.lastUpdateTimeStamp))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	
	public void removeTag(Tag tag) {
		this.tags.remove(tag);
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public boolean isCompleted() {
		return completed;
	}
}
