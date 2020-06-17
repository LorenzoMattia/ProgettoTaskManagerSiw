package it.uniroma3.siw.services;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	
	@Transactional
	public List<User> findAllUsers() {
		Iterable<User> i = this.userRepository.findAll();
		ArrayList<User> lista = new ArrayList<>();
		for(User u : i)
			lista.add(u);
		return lista;
	}

	public List<User> getMembers(Project project) {
		return this.userRepository.findByVisibleProjects(project);
	}
	
	public List<User> getNotMembers(Project project) {
		return this.userRepository.findByNotVisibleProjects(project.getId());
	}

	public void addComment(Comment comment, User owner) {
		owner.getComments().add(comment);
		this.userRepository.save(owner);
	}
}
