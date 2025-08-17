package com.projam.projambackend.services;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.ContactRequest;
import com.projam.projambackend.email.EmailUtility;

@Service
public class ContactService {

	private EmailUtility emailUtility;
	
	public ContactService(EmailUtility emailUtility) {
		this.emailUtility = emailUtility;
	}
	
	public String contactUsMail(ContactRequest request, String email) {
		emailUtility.sendEmail(request.getName(), email, request.getType(), request.getMessage());
		return "Email Sent Successfully";
	}
}
