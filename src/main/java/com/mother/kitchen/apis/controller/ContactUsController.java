package com.mother.kitchen.apis.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mother.kitchen.apis.modal.ContactUs;
import com.mother.kitchen.apis.modal.Status;
import com.mother.kitchen.apis.response.ContactUsResponse;
import com.mother.kitchen.apis.service.ContactUsService;

@RestController
@RequestMapping("mother-kitchen")
public class ContactUsController {
	
	@Autowired
	private ContactUsService contactUsService;
	
	@PostMapping("contact-us")
	public ResponseEntity<ContactUsResponse> saveContactUsDetails(@RequestBody ContactUs req){
		ContactUsResponse response = null;
		Status status = null;
		try {
			response = contactUsService.saveContactUs(req);
			if(response.getStatus().getCode().equalsIgnoreCase("201")) {
				return new ResponseEntity<ContactUsResponse>(response,HttpStatus.OK);
			}
		} catch (MessagingException e) {
			status = Status.builder().code("400").type("FAILUTE").message(e.getMessage())
					.description("Failed to save query").build();
			response = ContactUsResponse.builder().status(status).build();
			return new ResponseEntity<ContactUsResponse>(response,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ContactUsResponse>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	@PostMapping("save-mobile-email/input/{input}")
	public ResponseEntity<ContactUsResponse> saveUserEmail(@PathVariable String input){
		ContactUsResponse response = null;
		response= contactUsService.saveUserEmailAndMobileNumber(input);
		if(response.getStatus().getCode().equalsIgnoreCase("201")) {
			return new ResponseEntity<ContactUsResponse>(response,HttpStatus.OK);
				
		}
		return new ResponseEntity<ContactUsResponse>(response,HttpStatus.BAD_REQUEST);
	}
}
