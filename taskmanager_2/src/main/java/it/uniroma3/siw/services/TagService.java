package it.uniroma3.siw.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepository;

	public List<Tag> getTagsNotAdded(Task task, Project project) {
		return this.tagRepository.getTagsNotAddedById(task.getId(), project.getId());
	}

	public List<Tag> getTagsAdded(Task task, Project project) {
		return this.tagRepository.findByTasksAndTasksProject(task, project);
	}

	public Tag getTag(Long tagId) {
		Optional<Tag> result = this.tagRepository.findById(tagId);
		return result.orElse(null);
	}	
	
}
