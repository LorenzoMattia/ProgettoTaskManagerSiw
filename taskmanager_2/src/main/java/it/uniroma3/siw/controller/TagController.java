package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.services.ProjectService;
import it.uniroma3.siw.validators.TagValidator;


@Controller
public class TagController {
	
	//@Autowired
	//TagService tagService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	TagValidator tagValidator;
	
	@RequestMapping(value = { "project/{projectId}/tag/create" }, method = RequestMethod.GET)
	public String createTagForm (Model model, @PathVariable Long projectId) {
		
		model.addAttribute("project", this.projectService.getProject(projectId));
		model.addAttribute("tag", new Tag());
		
		return "createTag";
	}
	
	@RequestMapping(value = { "project/{projectId}/tag/create" }, method = RequestMethod.POST)
	public String createTag (
			Model model, 
			@PathVariable Long projectId, 
			@Valid @ModelAttribute Tag tag, 
			BindingResult bindingResult) {
		
		this.tagValidator.validate(tag, bindingResult);
		Project project = this.projectService.getProject(projectId);
		if(!bindingResult.hasErrors()) {
			project.addTag(tag);
			this.projectService.saveProject(project);
			
			return "redirect:/project/{projectId}"; //DA RIVEDERE
		}
		model.addAttribute("tag", tag);
		model.addAttribute("project", project);
		return "createTag";
	}
	
}
