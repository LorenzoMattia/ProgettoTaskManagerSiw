package it.uniroma3.siw.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Tag;

@Component
public class TagValidator implements Validator {

	private final Integer MIN_NAME_LENGTH = 1;
	private final Integer MAX_NAME_LENGTH = 200;
	
	private final Integer MAX_COLOR_LENGTH = 30;
	
	private final Integer MAX_DESCRIPTION_LENGTH = 10000;

	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Tag tag = (Tag) target;
		String name = tag.getName().trim();
		String color = tag.getDescription().trim();
		String description = tag.getDescription().trim();
		
		if(name.isEmpty())
			errors.reject("required");
		else if(name.length() < this.MIN_NAME_LENGTH || name.length() > this.MAX_NAME_LENGTH)
			errors.reject("size");
		
		else if(color.length() > this.MAX_COLOR_LENGTH)
			errors.reject("size");
		
		else if(description.length() > this.MAX_DESCRIPTION_LENGTH)
			errors.reject("size");
	}

}
