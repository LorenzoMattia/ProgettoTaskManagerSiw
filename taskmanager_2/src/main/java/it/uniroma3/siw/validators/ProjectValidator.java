package it.uniroma3.siw.validators;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.services.ProjectService;

@Component
public class ProjectValidator implements Validator {
	
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;
	
	@Autowired
	protected ProjectService projectService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Project p = (Project) target;
		String name = p.getName().trim();
		String description = p.getDescription().trim();
		
		if(name.isEmpty())
			errors.rejectValue("name", "required");
		else if(name.length() < this.MIN_NAME_LENGTH || name.length() > this.MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		else if(this.projectService.getProject(name)!=null)
			errors.rejectValue("name", "duplicate");
		
		if(description.length() > this.MAX_DESCRIPTION_LENGTH)
			errors.rejectValue("description", "size");
		
	}

}
