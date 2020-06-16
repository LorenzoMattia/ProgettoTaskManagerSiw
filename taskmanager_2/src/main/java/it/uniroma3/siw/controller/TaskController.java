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
	
	@RequestMapping(value = { "/task/add/{projectId}" }, method = RequestMethod.GET)
	public String addTaskForm(Model model, @PathVariable Long projectId) {
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		//model.addAttribute("user", sessionData.getLoggedUser());
		model.addAttribute("task", new Task());
		return "addTask";
		
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
			this.projectService.saveProject(p);
			return "redirect:/projects";
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
		if(!taskBindingResult.hasErrors()) {
			if(loggedUser.equals(owner)) {
				t.setName(task.getName());
				t.setDescription(task.getDescription());
				this.taskService.saveTask(t);
				return "redirect:/project/" + projectId;
			}
		}
		//return "/task/updateForm/" + taskId + "/" + projectId; 
		model.addAttribute("task", t);
		model.addAttribute("project", p);
		return "updateTask";
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

}
