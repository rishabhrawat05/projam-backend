package com.projam.projambackend.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.ContactRequest;
import com.projam.projambackend.services.ContactService;

@RestController
@RequestMapping("/projam/contact")
public class ContactController {
	
	private ContactService contactService;
	
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

	@PostMapping("/email")
	public ResponseEntity<String> contactUsEmail(@RequestBody ContactRequest contactRequest, Principal principal){
		return ResponseEntity.ok(contactService.contactUsMail(contactRequest, principal.getName()));
	}
}
