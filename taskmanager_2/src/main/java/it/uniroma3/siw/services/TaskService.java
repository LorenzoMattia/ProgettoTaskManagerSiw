package it.uniroma3.siw.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.repository.TaskRepository;


@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Transactional
	public Task getTask(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public void deleteTask(Task t) {
		this.taskRepository.delete(t);
	}
	
	@Transactional
	public Task saveTask(Task t) {
		return this.taskRepository.save(t);
	}
	
	@Transactional
	public Task setCompleted(Task t) {
		t.setCompleted(true);
		return this.taskRepository.save(t);
	}

	@Transactional
	public void deleteById(Long taskId) {
		this.taskRepository.deleteById(taskId);
		
	}	
	
}
