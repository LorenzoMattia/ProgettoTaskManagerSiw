package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;

@Controller
public class UserController {
	
	@Autowired
	SessionData sessionData;
	
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
}
