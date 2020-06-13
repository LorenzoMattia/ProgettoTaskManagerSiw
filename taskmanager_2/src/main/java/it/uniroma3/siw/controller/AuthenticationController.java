package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.services.CredentialsService;
import it.uniroma3.siw.validators.CredentialsValidator;
import it.uniroma3.siw.validators.UserValidator;


@Controller
public class AuthenticationController {

	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired
	CredentialsValidator credentialsValidator;
	
	@RequestMapping(value = { "/users/register" }, method = RequestMethod.GET)
	public String showRegisterForm(Model model) {
		model.addAttribute("User", new User());
		model.addAttribute("Credentials", new Credentials());
		
		return "registerUser";
	}
	
	//@RequestMapping(value = { "/users/register" }, method = RequestMethod.POST)
	@RequestMapping(value = { "/users/register" }, method = RequestMethod.POST)
	public String registerUser( @Valid @ModelAttribute("User") User user,
								BindingResult userBindingResult,
								@Valid @ModelAttribute("Credentials") Credentials credentials,
								BindingResult credentialsBindingResult,
								Model model ) {
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			return "registrationSuccesful";
		}
		return "registerUser";
	}
	
	
	
 }
