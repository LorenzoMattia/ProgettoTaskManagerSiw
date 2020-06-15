package it.uniroma3.siw.controller;

import java.util.List;
import java.util.Optional;

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
import it.uniroma3.siw.repository.ProjectRepository;
import it.uniroma3.siw.services.ProjectService;
import it.uniroma3.siw.services.UserService;
import it.uniroma3.siw.validators.ProjectValidator;

@Controller
public class ProjectController {

	@Autowired
	protected ProjectService projectService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected ProjectRepository projectRepository;
	
	@Autowired
	protected ProjectValidator projectValidator;
	
	@Autowired
	protected SessionData sessionData;
	
	@RequestMapping(value = { "/projects" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("user", loggedUser);
		model.addAttribute("projectsList", projectsList);
		
		return "myOwnedProjects";
	}
	
	@RequestMapping(value = { "/project/{projectId}" }, method = RequestMethod.GET)
	public String project(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		Project project = projectService.getProject(projectId);
		if(projectId == null) {
			return "redirect:/projects";
		}
		
		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser)) {
			return "redirect:/projects";
		}
		
		model.addAttribute("user", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		
		return "project";
	}
	
	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		model.addAttribute(sessionData.getLoggedUser());
		model.addAttribute("project", new Project());
		return "addProject";
	}
	
	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("project") Project project,
								BindingResult projectBindingResult,
								Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/project/" + project.getId();
		}
		model.addAttribute(loggedUser);
		return "addProject";
	}
	
	@RequestMapping(value = { "/addTask/{projectId}" }, method = RequestMethod.GET)
	public String addTaskForm(Model model, @PathVariable Long projectId) {
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		//model.addAttribute("user", sessionData.getLoggedUser());
		model.addAttribute("task", new Task());
		return "addTask";
	}
	
	@RequestMapping(value = { "/projects/updateForm/{projectId}" }, method = RequestMethod.GET)
	public String updatePorjectForm(Model model, @PathVariable Long projectId) {
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		return "updateProject";
	}
	
	@RequestMapping(value = { "/projects/update/{projectId}" }, method = RequestMethod.POST)
	public String updateProject(@Valid @ModelAttribute("project") Project project,
								BindingResult projectBindingResult,
								@PathVariable Long projectId,
								Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			Project p = this.projectService.getProject(projectId);
			p.setName(project.getName());
			p.setDescription(project.getDescription());
			p.setOwner(loggedUser);
			this.projectService.saveProject(p);
			return "redirect:/project/" + p.getId();
		}
		model.addAttribute(loggedUser);
		return "updateProject";
	}
}
