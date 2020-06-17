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
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.services.ProjectService;
import it.uniroma3.siw.services.TagService;
import it.uniroma3.siw.services.TaskService;
import it.uniroma3.siw.services.UserService;
import it.uniroma3.siw.validators.TaskValidator;

@Controller
public class TaskController {
	
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected TaskValidator taskValidator;
	
	@Autowired
	protected ProjectService projectService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected SessionData sessionData;

	@Autowired
	TagService tagService;
	
	@RequestMapping(value = { "/task/add/{projectId}" }, method = RequestMethod.GET)
	public String addTaskForm(Model model, @PathVariable Long projectId) {
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		//model.addAttribute("user", sessionData.getLoggedUser());
		model.addAttribute("task", new Task());
		return "addTask";
	}
		
	@RequestMapping(value = { "/task/viewTask/{taskId}/{projectId}" }, method = RequestMethod.GET)
	public String viewTask(@PathVariable Long taskId,
						   @PathVariable Long projectId,
						   Model model) {
		Task task = this.taskService.getTask(taskId);
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("task", task);
		model.addAttribute("project", project);
		List<User> members = task.getMembers();
		model.addAttribute("members", members);
		return "task";
	}
	
	@RequestMapping(value= { "/task/add/{projectId}" }, method = RequestMethod.POST)
	public String addTask(@Valid @ModelAttribute("task") Task task,
						  BindingResult taskBindingResult,
						  @PathVariable("projectId") Long projectId,
						  Model model) {
		this.taskValidator.validate(task, taskBindingResult);
		Project p = this.projectService.getProject(projectId);
		if(!taskBindingResult.hasErrors()) {
			p.getTasks().add(task);
			task.setProject(p);
			this.projectService.saveProject(p);
			return "redirect:/project/" + projectId;
		}
		//return "redirect:/addTask/" + projectId;
		model.addAttribute("project", p);
		return "addTask";
	}
	
	@RequestMapping(value= { "/task/updateForm/{taskId}/{projectId}" }, method = RequestMethod.GET)
	public String updateForm(@PathVariable ("taskId") Long taskId,
							 @PathVariable ("projectId") Long projectId, Model model) {
		model.addAttribute("task", this.taskService.getTask(taskId));
		model.addAttribute("project", this.projectService.getProject(projectId));
		
		return "updateTask";
	}
	
	@RequestMapping(value= { "/task/update/{taskId}/{projectId}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("task") Task task,
						 BindingResult taskBindingResult,
						 @PathVariable ("taskId") Long taskId,
						 @PathVariable ("projectId") Long projectId, 
						 Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		User owner = this.projectService.getProject(projectId).getOwner();
		this.taskValidator.validate(task, taskBindingResult);
		Task t = this.taskService.getTask(taskId);
		Project p = this.projectService.getProject(projectId);
		if(!taskBindingResult.hasErrors() && loggedUser.equals(owner)) {
			t.setName(task.getName());
			t.setDescription(task.getDescription());
			this.taskService.saveTask(t);
			return "redirect:/project/" + projectId;
		}
		//return "/task/updateForm/" + taskId + "/" + projectId;
		//task.setId(taskId);
		model.addAttribute("task", task);
		model.addAttribute("project", p);
		return "updateTask";
		//return "redirect:/task/updateForm/{taskId}/{projectId}";
	}
	
	@RequestMapping(value = { "/tasks/delete/{taskId}/{projectId}" }, method = RequestMethod.POST)
	public String deleteTask(@PathVariable Long taskId,
							 @PathVariable Long projectId,
							 Model model) {
		Task task = this.taskService.getTask(taskId);
		Project project = this.projectService.getProject(projectId);
		
		User owner = project.getOwner();
		User loggedUser = this.sessionData.getLoggedUser();
		
		if(owner.equals(loggedUser)) {
			project.getTasks().remove(task);
			this.projectService.saveProject(project);
			
			this.taskService.deleteTask(task);
			return "redirect:/project/" + projectId;
		}
		return "redirect:/project/" + projectId;
	}
	
	@RequestMapping(value = { "/task/assignUserToTask/{taskId}/{projectId}" }, method = RequestMethod.GET)
	public String chooseUser(@PathVariable ("taskId") Long taskId,
						 	 @PathVariable ("projectId") Long projectId, 
						 	 Model model) {
		
		Project project = this.projectService.getProject(projectId);
		if(project.getOwner().equals(this.sessionData.getLoggedUser())) {
			List<User> members = project.getMembers();
			model.addAttribute("members", members);
			model.addAttribute("project", project);
			model.addAttribute("task", this.taskService.getTask(taskId));
			return "taskMembers";
		}
		return "redirect:/task/viewTask/" + taskId + "/" + projectId;
		
	}
	
	@RequestMapping(value = {"/task/userChosen/{taskId}/{projectId}/{userId}"}, method = RequestMethod.GET)
	public String assignToUser(@PathVariable Long taskId,
							   @PathVariable Long projectId,
							   @PathVariable Long userId,
							   Model model) {
		User user = this.userService.getUser(userId);
		Task task = this.taskService.getTask(taskId);
		if(!task.getMembers().contains(user)) {
			task.getMembers().add(user);
			this.taskService.saveTask(task);
			user.getTasks().add(task);
			this.userService.saveUser(user);
			return "redirect:/task/viewTask/" + taskId + "/" + projectId;
		}
		return "redirect:/task/viewTask/" + taskId + "/" + projectId;
	}
	
	@RequestMapping(value = { "/project/{projectId}/task/{taskId}/manageTag" }, method = RequestMethod.GET)
	public String manageTag(Model model, @PathVariable Long projectId, @PathVariable Long taskId) {
		
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("task", task);
		
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		
		List<Tag> tagsNotAdded = this.tagService.getTagsNotAdded(task, project);
		model.addAttribute("tagsNotAdded", tagsNotAdded);
		
		List<Tag> tagsAdded = this.tagService.getTagsAdded(task, project);
		model.addAttribute("tagsAdded", tagsAdded);
		
		//model.addAttribute("loggedUser", this.sessionData.getLoggedCredentials());
		
		return "manageTags";
	}
	
	@RequestMapping(value = { "/project/{projectId}/task/{taskId}/assignTag/{tagId}" }, method = RequestMethod.GET)
	public String assignTag(Model model, @PathVariable Long projectId, @PathVariable Long taskId, @PathVariable Long tagId) {
		
		Task task = this.taskService.getTask(taskId);
		Project project = this.projectService.getProject(projectId);
		if(this.sessionData.getLoggedUser().equals(project.getOwner())) {
			Tag tag = this.tagService.getTag(tagId);
			task.addTag(tag);
			tag.addTask(task);
			this.taskService.saveTask(task);
		}
		
		return "redirect:/project/{projectId}/task/{taskId}/manageTag";
	}
	
	@RequestMapping(value = { "/project/{projectId}/task/{taskId}/unassignTag/{tagId}" }, method = RequestMethod.GET)
	public String unassignTag(Model model, @PathVariable Long projectId, @PathVariable Long taskId, @PathVariable Long tagId) {
		
		Task task = this.taskService.getTask(taskId);
		Project project = this.projectService.getProject(projectId);
		if(this.sessionData.getLoggedUser().equals(project.getOwner())) {
			Tag tag = this.tagService.getTag(tagId);
			task.removeTag(tag);
			tag.removeTask(task);
			this.taskService.saveTask(task);
		}
		
		return "redirect:/project/{projectId}/task/{taskId}/manageTag";
	}
}
