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

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.services.ProjectService;
import it.uniroma3.siw.services.TagService;
import it.uniroma3.siw.validators.TagValidator;


@Controller
public class TagController {

	@Autowired
	TagService tagService;

	@Autowired
	ProjectService projectService;

	@Autowired
	TagValidator tagValidator;

	@Autowired
	SessionData sessionData;

	@RequestMapping(value = { "/project/{projectId}/tag/create" }, method = RequestMethod.GET)
	public String createTagForm (Model model, @PathVariable Long projectId) {
		if(projectService.getProject(projectId).getOwner().equals(this.sessionData.getLoggedUser())) {
			model.addAttribute("project", this.projectService.getProject(projectId));
			model.addAttribute("tag", new Tag());
			return "createTag";
		}

		return "redirect:/project/{projectId}";
	}

	@RequestMapping(value = { "/project/{projectId}/tag/create" }, method = RequestMethod.POST)
	public String createTag (
			Model model,
			@PathVariable Long projectId,
			@Valid @ModelAttribute Tag tag,
			BindingResult bindingResult) {

		this.tagValidator.validate(tag, bindingResult);
		Project project = this.projectService.getProject(projectId);
		if(projectService.getProject(projectId).getOwner().equals(this.sessionData.getLoggedUser())) {
			if(!bindingResult.hasErrors()) {
				project.addTag(tag);
				this.projectService.saveProject(project);

				return "redirect:/project/{projectId}"; //DA RIVEDERE
			}
		}
		model.addAttribute("tag", tag);
		model.addAttribute("project", project);

		return "createTag";
	}

	@RequestMapping(value = { "/project/{projectId}/editTag/{tagId}" }, method = RequestMethod.GET)
	public String editTagForm (Model model, @PathVariable Long projectId, @PathVariable Long tagId) {

		if(projectService.getProject(projectId).getOwner().equals(this.sessionData.getLoggedUser())) {
			model.addAttribute("project", this.projectService.getProject(projectId));
			model.addAttribute("tag", this.tagService.getTag(tagId));
			return "updateTag";
		}
		return "redirect:/project/{projectId}";
	}

	@RequestMapping(value = { "/project/{projectId}/editTag/{tagId}" }, method = RequestMethod.POST)
	public String editTag (
			Model model,
			@PathVariable Long projectId,
			@PathVariable Long tagId,
			@Valid @ModelAttribute Tag newTag,
			BindingResult bindingResult) {
		Tag tag = this.tagService.getTag(tagId);
		if(projectService.getProject(projectId).getOwner().equals(this.sessionData.getLoggedUser())) {
			this.tagValidator.validate(newTag, bindingResult);
			if(!bindingResult.hasErrors()) {
				tag.setName(newTag.getName());
				tag.setColor(newTag.getColor());
				tag.setDescription(newTag.getDescription());
				tagService.saveTag(tag);
				return "redirect:/project/{projectId}";
			}
		}
		model.addAttribute("project", this.projectService.getProject(projectId));
		newTag.setId(tag.getId());
		model.addAttribute("tag", newTag);

		return "updateTag";
	}

	@RequestMapping(value = { "/project/{projectId}/deleteTag/{tagId}" }, method = RequestMethod.POST)
	public String deleteTag (Model model, @PathVariable Long projectId, @PathVariable Long tagId) {
		Project project = projectService.getProject(projectId);
		if(project.getOwner().equals(this.sessionData.getLoggedUser())) {
			Tag tag = this.tagService.getTag(tagId);
			this.tagService.deleteTag(tag, project);
		}
		return "redirect:/project/{projectId}";
	}
}
