package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = true, unique = true)
	private String name;
	private String color;
	private String description;
	
	//private String commento;
	
	@ManyToMany(mappedBy = "tags")
	List<Task> tasks;
	
	public Tag() {
		this.tasks = new ArrayList<Task>();
	}
}
