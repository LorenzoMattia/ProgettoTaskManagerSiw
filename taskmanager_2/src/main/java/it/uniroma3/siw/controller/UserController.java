package it.uniroma3.siw.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
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
import it.uniroma3.siw.validators.CredentialsValidator;
import it.uniroma3.siw.validators.UserValidator;

@Controller
public class UserController {
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired
	CredentialsValidator credentialsValidator;
	
	@RequestMapping(value = {"/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "home";
	}
	
	@RequestMapping(value = { "/users/me"}, method = RequestMethod.GET)
	public String me(Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Credentials credentials = this.sessionData.getLoggedCredentials();
		model.addAttribute("user", loggedUser);
		model.addAttribute("credentials", credentials);
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
		this.credentialsValidator.validate(credentials, credentialsBindingResult);

		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			Credentials c = this.credentialsService.getCredentials(credentialsId);
			User u = c.getUser();
			u.setFirstname(user.getFirstname());
			u.setLastname(user.getLastname());
			c.setUsername(credentials.getUsername());
			c.setPassword(credentials.getPassword());
			credentials.setUser(u);
			credentialsService.saveCredentials(c);
			this.sessionData.removeUser();
			this.sessionData.removeCredentials();
			return "updateSuccessful";
		}
		return "updateProfile";
	}

}
