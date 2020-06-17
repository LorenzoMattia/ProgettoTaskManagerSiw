package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.services.CommentService;
import it.uniroma3.siw.services.ProjectService;
import it.uniroma3.siw.services.TaskService;
import it.uniroma3.siw.services.UserService;

@Controller
public class CommentController {
	
	@Autowired
	protected CommentService commentService;
	
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired 
	protected ProjectService projectService;
	
	@Autowired
	protected SessionData sessionData;
	
	@RequestMapping(value = { "/task/commentForm/{taskId}/{projectId}" }, method = RequestMethod.GET)
	public String commentForm(@PathVariable Long taskId,
					   @PathVariable Long projectId,
					   Model model) {
		model.addAttribute("task", this.taskService.getTask(taskId));
		model.addAttribute("project", this.projectService.getProject(projectId));
		model.addAttribute("comment", new Comment());
		return "commentForm";
	}
	
	@RequestMapping(value = { "/task/comment/{taskId}/{projectId}" }, method = RequestMethod.POST)
	public String commentTask(@ModelAttribute ("comment") Comment comment,
							  @PathVariable Long taskId,
							  @PathVariable Long projectId,
							  Model model) {
		Task task = this.taskService.getTask(taskId);
		comment.setT(task);
		this.taskService.addComment(comment, task);
		
		User owner = this.sessionData.getLoggedUser();
		comment.setOwner(owner);
		this.userService.addComment(comment, owner);
		
		this.commentService.save(comment);
		return "redirect:/task/viewTask/{taskId}/{projectId}";
		
	}
}
