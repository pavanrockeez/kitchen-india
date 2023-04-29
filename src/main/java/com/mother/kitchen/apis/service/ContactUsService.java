package com.mother.kitchen.apis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mother.kitchen.apis.modal.ContactUs;
import com.mother.kitchen.apis.modal.Status;
import com.mother.kitchen.apis.modal.UserEmailMobileNumber;
import com.mother.kitchen.apis.response.ContactUsResponse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class ContactUsService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private final String motherKitchenEmail = "we@motherskitchenindia.com";
    private final String adminEmail = "me@sreedhartruly.com";

    File file = new File("ContactUsRequestValues.txt");
    File file1 = new File("UserEmailMobileRequests.txt");
    private String body = "<html><body style='background-color: #f5f5f5; font-family: Arial, sans-serif;'>"
    		+ "<div style='width:100% ;max-width:500px;align-items: center; justify-content: center; flex-direction: column; margin: 0 auto; text-align: center;padding-top:10px'>"
    		+ "<div style='text-align:center;'>"
            + "<p style='font-weight: 500; font-size:13px; color: #18191B;'>Eat just like@home</p>"
            + "<p style='font-weight: 700; font-size:18px; color:#9B000A;'>MOTHER'S KITCHEN</p>"
            + "<div style='right:0'><p>Healthy & Spicy</div>"
            + "</div>"
            + "<div style='width: 100%;max-width:500px; height: 150px; border-radius: 20px; background-color:#9B000A; margin-bottom:20px ; text-align: center; padding-top: 50px;'>"
            + "<p style='font-weight: 800; font-size: 20px; color: #ffffff;'>Thank you</p>"
            + "<p style='font-weight: 600; font-size: 15px; color: #ffffff;'>for submitting contact information</p>"
            + "</div>"
            + "<div style='width: 100%;max-width:500px; height: 150px; border-radius: 20px; background-color:#FFF4D2; margin-bottom:20px ; text-align: center; padding-top: 50px;'>"
            + "<p style='font-weight:500; font-size:15px;color:#18191B'>Our team at Mother's Kitchen values your interest and will be in touch as soon as possible to address any questions or concerns you may have. We appreciate your time and look forward to providing you with the best possible service.</p>"
            + "</div>"
            + "<div style='text-align:center'><p style='font-weight:700; font-size:13px; color:#18191B'>OUR SERVICES</p></div>"
            + "<div style='display:flex;'>"
            + "<div style='width:100%;max-width: 250px; height: 150px; border-radius: 20px; background-color:#FFF4D2; margin-bottom:10px ; text-align: center;display: inline-block; padding-top: 50px;'>"
            + "<p style='font-weight:700; font-size:16px; color: #9B000A;'>Breakfast</p>"
            + "<p style='font-weight:500; font-size:12px; color: #18191B;'>Power Up Your Day with Our Tech-Friendly Breakfast Options!</p>"
            + "</div>"
            + "<div style='width:100%;max-width: 250px; height: 150px; border-radius: 20px; margin-left:10px; background-color:#FFF4D2; margin-bottom:20px ; text-align: center;display: inline-block; padding-top: 50px;'>"
            + "<p style='font-weight:700; font-size:16px; color: #9B000A;'>Snacks</p>"
            + "<p style='font-weight:500; font-size:12px; color: #18191B;'>Snack Time Just Got Better with Our Mouthwatering Treats</p>"
            + "</div>"
            + "</div>"
            + "<div style='width: 100%;max-width:500px; height: 150px; border-radius: 20px; background-color:#FFF4D2; margin-bottom:20px ; text-align: center; padding-top: 50px;padding-left:10px'>"
            + "<p style='font-weight:500; font-size:13px; color:#18191B;'>If you need to reach us urgently, please feel free to email us at</p> "
            + "<span style='font-weight:500; font-size:15px; color:#9B000A;'>info@motherskitchenindia.com</span><span style='font-weight:500; font-size:13px;'> or call us at</span> "
            + "<span style='font-weight:500; font-size:15px; color:#9B000A'>9703 44 66 88</span>.<pstyle='font-weight:500; font-size:13px;'> Thank you again for considering Mother's Kitchen"
            + " for your food needs!</p>"
            + "</div>"
            + "<div style='text-align:center;'>"
            + "<p style='font-weight: 700; font-size:15px; color: #18191B;'>www.motherskitchenindia.com</p></div>"
            + "</div>"
            + "</body></html>";
	
	public ContactUsResponse saveContactUs(ContactUs req) throws MessagingException {
		ContactUsResponse response =null;
		Status status = null;
		String email =req.getEmail();
		String mobileNumber = req.getMobileNumber();
		if (email == null || email.isEmpty() || !isValidEmail(email)) {
			status = Status.builder().code("400").type("FAILURE").message("INVALID_EMAIL_FORMAT")
                    .description("Email is required or has invalid format").build();
            response =ContactUsResponse.builder().status(status).build();
            return response;
        }
		else if (mobileNumber == null || mobileNumber.isEmpty() || !isValidMobileNumber(mobileNumber)) {
        	status = Status.builder().code("400").type("FAILURE").message("INVALID_MOBILE_NUMBER_FORMAT")
                    .description("Mobile number is required or has invalid format").build();
            response = ContactUsResponse.builder().status(status).build();
            return response;
        } else {
        	writeRequestValuesToFile(req);
    		sendEmailToUser(req.getEmail());
    		sendEmailToAdmin(req);
    		status = Status.builder().code("201").type("SUCCESS").message("QUERY_SAVED")
					.description("Query Reached We Will Reach Out To you").build();
    		response = ContactUsResponse.builder().status(status).build();
    		return response;
    	}
		
	}
	
	public ContactUsResponse saveUserEmailAndMobileNumber(UserEmailMobileNumber req) {
		Status status = null;
		String email =req.getEmail();
		String mobileNumber = req.getMobileNumber();
		if (email != null && !email.isEmpty() && !email.contains("string")) {
	        if (!isValidEmail(email)) {
	            status = Status.builder().code("400").type("FAILURE").message("INVALID_EMAIL_FORMAT")
	                    .description("Email is required or has an invalid format").build();
	        }
	    } else if (mobileNumber != null && !mobileNumber.isEmpty() && !mobileNumber.contains("string")) {
	        if (!isValidMobileNumber(mobileNumber)) {
	            status = Status.builder().code("400").type("FAILURE").message("INVALID_MOBILE_NUMBER_FORMAT")
	                    .description("Mobile number is required or has an invalid format").build();
	        }
	    } else {
	        status = Status.builder().code("400").type("FAILURE").message("INVALID_INPUT")
	                .description("Either email or mobile number is required").build();
	    }
	    if (status != null) {
	        return ContactUsResponse.builder().status(status).build();
	    }
	    try {
	    	userEmailMobileRequestValues(req);
	    	sendEmailMessageToUser(req);
		    sendGmailToAdmin(req);
		    status = Status.builder().code("201").type("SUCCESS").message("QUERY_SAVED")
		               .description("Query reached. We will reach out to you.").build();
		    return ContactUsResponse.builder().status(status).build();
	    } catch(Exception e) {
	        status = Status.builder().code("400").type("FAILURE").message(e.getMessage())
	                .description("Failed to Details").build();
	        return ContactUsResponse.builder().status(status).build();
	    }
	}
		
	public void sendEmailMessageToUser(UserEmailMobileNumber req) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(motherKitchenEmail);
			helper.setTo(req.getEmail());
			helper.setSubject("Query Raised");
			helper.setText(body, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	private void sendGmailToAdmin(UserEmailMobileNumber req) {
		List<String> adminEmails = Arrays.asList(motherKitchenEmail, adminEmail);
		String text ="";
		if(req.getEmail().length() != 0 && !req.getEmail().equals("string")) {
			text = req.getEmail();
		} else {
			text = req.getMobileNumber();
		}
		for(String adminEmail : adminEmails) {
			try {
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				helper.setFrom(motherKitchenEmail);
				helper.setTo(adminEmail);
				helper.setSubject("Contact Details");
				String body = "<html><body style='background-color: #f5f5f5; font-family: Arial, sans-serif;'>"
						+ "<p style='font-weight: bold;'>"
						+ "<span style='color:#9B000A; font-size:15px; font-weight:700px'>" + text + "</span>"
						+ " User has contacted you</p>"
			            + "</body></html>";
				helper.setText(body, true);
				javaMailSender.send(message);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendEmailToUser(String email) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(motherKitchenEmail);
			helper.setTo(email);
			helper.setSubject("Thanks you for reached us");
			helper.setText(body, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendEmailToAdmin(ContactUs req){
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(motherKitchenEmail);
			helper.setTo(motherKitchenEmail);
			helper.setSubject("Contact Details");
			String htmlContent = emailBody(req);
			helper.setText(htmlContent, true);
			javaMailSender.send(message);
			sendEmailToMainAdmin(req);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendEmailToMainAdmin(ContactUs req) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(motherKitchenEmail);
			helper.setTo(adminEmail);
			helper.setSubject("Contact Details");
			String htmlContent = emailBody(req);
			helper.setText(htmlContent, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	private String emailBody(ContactUs req) {
		String body = "<html><body style='background-color: #f5f5f5; font-family: Arial, sans-serif;'>"
		           + "<p style='color: #333;'>Contact Details</p>"
		           + "<p style='font-weight: bold;'>Name: " + req.getName() + "</p>"
		           + "<p style='font-weight: bold;'>Email: " + req.getEmail() + "</p>"
		           + "<p style='font-weight: bold;'>Mobile Number: " + req.getMobileNumber() + "</p>"
		           + "<p style='font-weight: bold;'>Selected Query: " + req.getSelectValue() + "</p>"
		           + "<p style='font-weight: bold;'>Message: " + req.getMessage() + "</p>"
		           + "</body></html>";
		return body;
	}
	
	private boolean isValidEmail(String email) {
	    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    return email.matches(emailRegex);
	}
	
	private boolean isValidMobileNumber(String mobileNumber) {
	    String mobileNumberRegex = "^[0-9]{10}$";
	    return mobileNumber.matches(mobileNumberRegex);
	}
	
	private void writeRequestValuesToFile(ContactUs req) {
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String timestamp = dateFormat.format(new Date());
			writer.write(req.getName() + ", " + req.getMobileNumber() + ", " + req.getEmail() + ", " 
			             + req.getSelectValue() + ", " + req.getMessage() + ", " + timestamp + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void userEmailMobileRequestValues(UserEmailMobileNumber req) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file1, true));
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String timestamp = dateFormat.format(new Date());
		    if(isValidEmail(req.getEmail())) {
		    	writer.write(req.getEmail() + ", " + timestamp + "\n");
		    } else {
		    	writer.write(req.getMobileNumber() + ", " + timestamp + "\n");
		    }
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
