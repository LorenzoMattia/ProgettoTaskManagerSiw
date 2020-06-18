package it.uniroma3.siw.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.services.CredentialsService;

@Component
public class CredentialsValidatorRegistration implements Validator {

	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	SessionData sessionData;
	
	final Integer MAX_USERNAME_LENGTH = 28;
	final Integer MIN_USERNAME_LENGTH = 4;
	final Integer MAX_PASSWORD_LENGTH = 60;
	final Integer MIN_PASSWORD_LENGTH = 6;
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.equals(clazz);
	}
	@Override
	public void validate(Object target, Errors errors) {
		Credentials credentials = (Credentials) target;
		
		String username = credentials.getUsername().trim();
		String password = credentials.getPassword().trim();
		
		if(username.isEmpty())
			errors.rejectValue("username", "required");
		
		else if(username.length()<this.MIN_USERNAME_LENGTH || username.length()>this.MAX_USERNAME_LENGTH)
			errors.rejectValue("username", "size");
		
		else if(this.credentialsService.getCredentials(username)!=null) {
			errors.rejectValue("username", "duplicate");
		}
			
		
		if(password.isEmpty()) {
			errors.rejectValue("password", "required");
		}
		else if(password.length()<this.MIN_PASSWORD_LENGTH || password.length()>this.MAX_PASSWORD_LENGTH)
			errors.rejectValue("password", "size");
	}

}
