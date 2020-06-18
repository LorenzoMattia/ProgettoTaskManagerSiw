package it.uniroma3.siw.validators;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.services.CredentialsService;

@Component
public class CredentialsValidatorWithoutPassword implements Validator { //bisognerebbe spostare il package validators in controller e rinominarlo in validation

	@Autowired
	CredentialsService credentialsService;
	
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
		
		if(username.isEmpty())
			errors.rejectValue("username", "required");
		
		else if(username.length()<this.MIN_USERNAME_LENGTH || username.length()>this.MAX_USERNAME_LENGTH)
			errors.rejectValue("username", "size");
		
		else if(this.credentialsService.getCredentials(username)!=null) {
			errors.rejectValue("username", "duplicate");
		}

	}
	
	

}
