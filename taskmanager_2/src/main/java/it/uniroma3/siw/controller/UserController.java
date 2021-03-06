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
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.services.CredentialsService;
import it.uniroma3.siw.services.UserService;
import it.uniroma3.siw.validators.CredentialsValidator;
import it.uniroma3.siw.validators.CredentialsValidatorWithoutPassword;
import it.uniroma3.siw.validators.UserValidator;

@Controller
public class UserController {
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired
	CredentialsValidator credentialsValidator;

	@Autowired
	CredentialsValidatorWithoutPassword credentialsValidatorWithoutPassword;
	
	@RequestMapping(value = {"/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "home";
	}
	
	@RequestMapping(value = { "/users/me"}, method = RequestMethod.GET)
	public String me(Model model) {
		String mine = "true";
		User loggedUser = this.sessionData.getLoggedUser();
		Credentials credentials = this.sessionData.getLoggedCredentials();
		model.addAttribute("user", loggedUser);
		model.addAttribute("credentials", credentials);
		model.addAttribute("mine", mine);
		return "userProfile";
	}
	
	@RequestMapping(value = { "/users/me/{userId}" }, method = RequestMethod.GET)
	public String UserProfile(Model model, @PathVariable Long userId) {
		User u = this.userService.getUser(userId);
		String mine = "false";
		model.addAttribute("mine", mine);
		model.addAttribute("user", u);
		return "userProfile";
	}
	
	@RequestMapping(value = { "/users/updateForm" }, method = RequestMethod.GET)
	public String updateForm(Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		model.addAttribute("User", loggedUser);
		Credentials loggedCredentials = this.sessionData.getLoggedCredentials();
		model.addAttribute("Credentials", loggedCredentials);
		return "updateProfile";
	}
	
	@RequestMapping(value = { "/users/update/{credentialsId}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("User") User user,
						 BindingResult userBindingResult,
						 @Valid @ModelAttribute("Credentials") Credentials credentials,
						 BindingResult credentialsBindingResult,
						 @PathVariable ("credentialsId") Long credentialsId,
						 Model model) {
		
		this.userValidator.validate(user, userBindingResult);
		
		if(!credentials.getPassword().isEmpty())
			this.credentialsValidator.validate(credentials, credentialsBindingResult);
		else
			this.credentialsValidatorWithoutPassword.validate(credentials, credentialsBindingResult);
		
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			Credentials c = this.credentialsService.getCredentials(credentialsId);
			User u = c.getUser();
			u.setFirstname(user.getFirstname());
			u.setLastname(user.getLastname());
			c.setUsername(credentials.getUsername());
			credentials.setUser(u);
			if(!credentials.getPassword().isEmpty()) {
				c.setPassword(credentials.getPassword());
				credentialsService.saveCredentials(c);
			}
			else
				credentialsService.saveCredentialsWithoutPassword(c);
			this.sessionData.removeUser();
			this.sessionData.removeCredentials();
			return "updateSuccessful";
		}
		credentials.setId(credentialsId);
		model.addAttribute("Credentials", credentials);
		return "updateProfile";
	}

}
