package it.uniroma3.siw.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Task;
@Component
public class TaskValidator implements Validator {

	private final Integer MAX_NAME_LENGTH = 100;
	private final Integer MIN_NAME_LENGTH = 2;
	private final Integer MAX_DESCRIPTION_LENGTH = 1000;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Task.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Task t = (Task) target;
		String name = t.getName().trim();
		String description = t.getDescription().trim();
		
		if(name.isEmpty()) {
			errors.rejectValue("name", "required");
		}
		else if(name.length()<this.MIN_NAME_LENGTH || name.length()>this.MAX_NAME_LENGTH) {
			errors.rejectValue("name", "size");
		}
		
		if(description.length()>this.MAX_DESCRIPTION_LENGTH) {
			errors.rejectValue("description", "size");
		}
		
	}

}
