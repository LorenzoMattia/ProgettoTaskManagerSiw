package it.uniroma3.siw.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.services.ProjectService;
import it.uniroma3.siw.services.TaskService;

@Controller
public class TaskController {
	
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected ProjectService projectService;
	
	@Autowired
	protected SessionData sessionData;
	
	@RequestMapping(value= { "/task/add/{projectId}" }, method = RequestMethod.POST)
	public String addTask(@Valid @ModelAttribute("task") Task task,
						  BindingResult taskBindingResult,
						  @PathVariable("projectId") Long projectId,
						  Model model) {
		Project p = this.projectService.getProject(projectId);
		p.getTasks().add(task);
		this.projectService.saveProject(p);
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("user", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "myOwnedProjects";
	}
}
