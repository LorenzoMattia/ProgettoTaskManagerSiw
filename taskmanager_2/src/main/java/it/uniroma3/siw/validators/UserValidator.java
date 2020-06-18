package it.uniroma3.siw.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.User;

@Component
public class UserValidator implements Validator {
	
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		String firstname = user.getFirstname().trim();
		String lastname = user.getLastname().trim();
		
		if(firstname.isEmpty())
			errors.rejectValue("firstname", "required");
		
		else if(firstname.length()<this.MIN_NAME_LENGTH || firstname.length()>this.MAX_NAME_LENGTH)
			errors.rejectValue("firstname", "size");
			
		
		if(lastname.isEmpty()) {
			errors.rejectValue("lastname", "required");
		}
		else if(lastname.length()<this.MIN_NAME_LENGTH || lastname.length()>this.MAX_NAME_LENGTH)
			errors.rejectValue("lastname", "size");
	}

}
