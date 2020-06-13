package it.uniroma3.siw.services;

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
		this.projectRepository.save(p);
	}
	
	@Transactional
	public Project shareProjectWithUser(Project p, User member) {
		p.addMember(member);
		return this.projectRepository.save(p);
	}
	
}
