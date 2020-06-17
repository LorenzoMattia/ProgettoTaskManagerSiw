package it.uniroma3.siw.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
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

	//@Autowired
	//protected ProjectRepository projectRepository;

	@Autowired
	protected ProjectValidator projectValidator;

	@Autowired
	protected SessionData sessionData;

	@RequestMapping(value = { "/projects" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retrieveProjectsOwnedBy(loggedUser);
		//model.addAttribute("user", loggedUser);
		model.addAttribute("projectsList", projectsList);
		model.addAttribute("my", "true");

		return "projectsList";
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

		//model.addAttribute("user", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		
		if(loggedUser.equals(project.getOwner()))
			model.addAttribute("my", "true");

		return "project";
	}

	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		//model.addAttribute(sessionData.getLoggedUser());
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
		//model.addAttribute(loggedUser);
		return "addProject";
	}
	
	@RequestMapping(value = { "/project/{projectId}/manageMembers" }, method=RequestMethod.GET)
	public String manageMembersForm(Model model, @PathVariable Long projectId) {
		
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		
		List<User> notMembers = this.userService.getNotMembers(project);
		notMembers.remove(project.getOwner());
		model.addAttribute("notMembers", notMembers);
		
		List<User> members = this.userService.getMembers(project);
		model.addAttribute("members", members);
		
		//model.addAttribute("loggedUser", this.sessionData.getLoggedCredentials());
		return "manageMembers";
	}
	
	@RequestMapping(value = { "/project/{projectId}/addMember/{memberId}" }, method=RequestMethod.GET)
	public String addMember(Model model, @PathVariable Long projectId, @PathVariable Long memberId) {
		
		Project project = projectService.getProject(projectId);
		
		if(this.sessionData.getLoggedUser().equals(project.getOwner())) {
			projectService.shareProjectWithUser(project, userService.getUser(memberId));
		}
		
		return "redirect:/project/{projectId}/manageMembers";
	}
	
	@RequestMapping(value = { "/project/{projectId}/removeMember/{memberId}" }, method=RequestMethod.GET)
	public String removeMember(Model model, @PathVariable Long projectId, @PathVariable Long memberId) {
		
		Project project = projectService.getProject(projectId);
		
		if(this.sessionData.getLoggedUser().equals(project.getOwner())) {
			project.removeMember(userService.getUser(memberId));
			projectService.saveProject(project);
		}

		return "redirect:/project/{projectId}/manageMembers";
	}
	
	@RequestMapping(value = { "/projects/shared" }, method = RequestMethod.GET)
	public String sharedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		
		List<Project> projectsList = projectService.retrieveProjectsSharedWith(loggedUser);
		//model.addAttribute("user", loggedUser);
		model.addAttribute("projectsList", projectsList);
		model.addAttribute("my", "false");
		
		return "projectsList";
	}

	@RequestMapping(value = { "/addTask/{projectId}" }, method = RequestMethod.GET)
	public String addTaskForm(Model model, @PathVariable Long projectId) {
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		model.addAttribute("task", new Task());
		return "addTask";
	}

	@RequestMapping(value = { "/projects/updateForm/{projectId}" }, method = RequestMethod.GET)
	public String updateProjectForm(Model model, @PathVariable Long projectId) { // ERRORE DI BATTITURA
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		return "updateProject";
	}

	@RequestMapping(value = { "/projects/update/{projectId}" }, method = RequestMethod.POST)
	public String updateProject(@Valid @ModelAttribute("project") Project project,
								BindingResult projectBindingResult,
								@PathVariable Long projectId,
								Model model) {
		//User loggedUser = this.sessionData.getLoggedUser();
		projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			Project p = this.projectService.getProject(projectId);
			p.setName(project.getName());
			p.setDescription(project.getDescription());
			//p.setOwner(loggedUser);
			this.projectService.saveProject(p);
			return "redirect:/project/" + p.getId();
		}
		//model.addAttribute(loggedUser);
		project.setId(projectId);
		model.addAttribute("project", project);
		return "updateProject";
	}
	
	@RequestMapping(value = { "/project/delete/{projectId}" }, method = RequestMethod.POST)
	public String deleteProject(@PathVariable Long projectId,
								Model model) {
		this.projectService.deleteProject(this.projectService.getProject(projectId));
		return "redirect:/projects";
	}
	
	/*@RequestMapping(value = { "/project/tasks/delete/{taskId}/{projectId}" }, method = RequestMethod.POST)
	public String deleteTaskFromProject(@PathVariable ("taskId") Long taskId,
							 			@PathVariable ("projectId") Long projectId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		User owner = project.getOwner();
		if(loggedUser.equals(owner)) {
			project.removeTaskWithId(taskId);
			this.projectService.saveProject(project);
			return "redirect:/project/" + projectId;
		}
		return "redirect:/project/" + projectId;
	}*/
		
}
