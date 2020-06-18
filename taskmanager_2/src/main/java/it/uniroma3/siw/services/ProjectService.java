package it.uniroma3.siw.services;

import java.util.List;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Transactional
	public Project getProject(Long id) {
		Optional<Project> result = this.projectRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Project saveProject(Project p) {
		return this.projectRepository.save(p);
	}
	
	@Transactional
	public void deleteProject(Project p) {
		this.projectRepository.delete(p);
	}
	
	//Usato in un test
	@Transactional
	public Project shareProjectWithUser(Project p, User member) {
		p.addMember(member);
		return this.projectRepository.save(p);
	}
	
	@Transactional
	public List<Project> retrieveProjectsOwnedBy(User loggedUser) {
		List<Project> projectsList = (this.projectRepository.findByOwner(loggedUser)); //da rivedere!!!!
		return projectsList;
	}
	
	@Transactional
	public List<Project> retrieveProjectsSharedWith(User loggedUser) {
		List<Project> projectList = (this.projectRepository.findByMembers(loggedUser));
		return projectList;
	}

	@Transactional
	public Project getProject(String name) {
		Optional<Project> project = this.projectRepository.findByName(name);
		return project.orElse(null);
	}
}
