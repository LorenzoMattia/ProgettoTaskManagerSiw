package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.services.CredentialsService;

@Controller
public class AdminController { //non capisco se ci va
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	CredentialsService credentialsService;
	
	@RequestMapping(value = {"/admin" }, method = RequestMethod.GET)
	public String home(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("admin", loggedUser);
		return "admin";
	}
	
	@RequestMapping(value = {"/admin/users" }, method = RequestMethod.GET)
	public String usersList(Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		List<Credentials> allCredentials = this.credentialsService.getAllCredentials();
		
		model.addAttribute("user", loggedUser);
		model.addAttribute("credentialsList", allCredentials);
		
		return "allUsers";
	}
	
	@RequestMapping(value = {"/admin/users/{username}/delete" }, method = RequestMethod.GET)
	public String removeUser(Model model, @PathVariable String username) {
		this.credentialsService.deleteCredentials(username);
		return "redirect:/admin/users";
	}
	
	
}
