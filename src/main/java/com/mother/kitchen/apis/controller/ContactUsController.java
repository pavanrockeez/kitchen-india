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
import com.mother.kitchen.apis.modal.UserEmailMobileNumber;
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
		long data;
		try {
			data = contactUsService.saveContactUs(req);
			if(data > 0) {
				status = Status.builder().code("201").type("SUCCESS").message("QUERY_SAVED")
						.description("Query Reached We Will Reach Out To you").build();
				response = ContactUsResponse.builder().data(data).status(status).build();
				return new ResponseEntity<ContactUsResponse>(response,HttpStatus.OK);
					
			}
		} catch (MessagingException e) {
			status = Status.builder().code("400").type("FAILUTE").message(e.getMessage())
					.description("Failed to save query").build();
			response = ContactUsResponse.builder().status(status).build();
			return new ResponseEntity<ContactUsResponse>(response,HttpStatus.BAD_REQUEST);
		}
		status = Status.builder().code("400").type("FAILUTE").message("FAILED_TO_SAVE")
				.description("Failed to save query").build();
		response = ContactUsResponse.builder().data(data).status(status).build();
		return new ResponseEntity<ContactUsResponse>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	@PostMapping("save-mobile-email")
	public ResponseEntity<ContactUsResponse> saveUserEmail(@RequestBody UserEmailMobileNumber req){
		ContactUsResponse response = null;
		Status status = null;
		long data = contactUsService.saveUserEmailAndMobileNumber(req);
		if(data > 0) {
			status = Status.builder().code("201").type("SUCCESS").message("EMAIL_REACHED")
					.description("Successfully email reached").build();
			response = ContactUsResponse.builder().data(data).status(status).build();
			return new ResponseEntity<ContactUsResponse>(response,HttpStatus.OK);
				
		}
		status = Status.builder().code("400").type("FAILUTE").message("FAILED_TO_SAVE")
				.description("Failed to Send Email").build();
		response = ContactUsResponse.builder().data(data).status(status).build();
		return new ResponseEntity<ContactUsResponse>(response,HttpStatus.BAD_REQUEST);
	}
}
