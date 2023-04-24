package com.mother.kitchen.apis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mother.kitchen.apis.dao.ContactUsDao;
import com.mother.kitchen.apis.modal.ContactUs;
import com.mother.kitchen.apis.modal.UserEmailMobileNumber;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class ContactUsService {
	
	@Autowired
	private ContactUsDao contactUsDao;
	
	@Autowired
	private JavaMailSender javaMailSender;
	

	
    private static final String BASE_URL = "https://jdxenk.api.infobip.com";
    private static final String API_KEY = "3ff253ccf8427e863e6ca79b36a60310-6f3290a6-ec65-4a81-90db-9c7f1aba9a41";

    private static final String SENDER = "447491163443";
    private static final String RECIPIENT = "919000369106";
    private static final String MESSAGE_TEXT = "This is a sample message";
    
    private final String motherKitchenEmail = "we@motherskitchenindia.com";
    private final String adminEmail = "me@sreedhartruly.com";
    //me@sreedhartruly.com
	
	public long saveContactUs(ContactUs req) throws MessagingException {
		long data = contactUsDao.saveContacUs(req);
		if(data>0) {
			sendEmailToUser(req.getEmail());
			sendEmailToAdmin(req);
		}
		return data;
	}
	
	public long saveUserEmailAndMobileNumber(UserEmailMobileNumber req) {
		long data = 0;
		if(req.getEmail().length()!=0 || req.getMobileNumber().length()!=0) {
			data = contactUsDao.saveUserEmailAndMobileNumber(req);
		}
		if(data > 0) {
			sendEmailMessageToUser(req);
			sendGmailToAdmin(req);
		}
		return data;
	}
		
	public void sendEmailMessageToUser(UserEmailMobileNumber req) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(motherKitchenEmail);
			helper.setTo(req.getEmail());
			helper.setSubject("Thanks for reached us");
			String body = "<html><body style='background-color: #f5f5f5; font-family: Arial, sans-serif;'>"
					+ "<br><img src='cid:image001'style='height: 200px; width: 300px;'/><br><b>Best Regards</b>"
					+ "<p style='font-weight: bold;'>Thank you for reaching us</p>"
		            + "</body></html>";
			FileSystemResource resource = new FileSystemResource(new File("motherkitchen.jpg"));
			helper.addInline("image001", resource);
			helper.setText(body, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendGmailToAdmin(UserEmailMobileNumber req) {
		List<String> adminEmails = Arrays.asList(motherKitchenEmail, adminEmail);
		String text ="";
		System.out.println(req.getEmail());
		if(req.getEmail().length() != 0 && !req.getEmail().equals("string")) {
			text = req.getEmail();
		} else {
			text = req.getMobileNumber();
			System.out.println("here");
		}
		System.out.println(text);
		for(String adminEmail : adminEmails) {
			try {
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				helper.setFrom(motherKitchenEmail);
				helper.setTo(adminEmail);
				helper.setSubject("Thanks for reached us");
				String body = "<html><body style='background-color: #f5f5f5; font-family: Arial, sans-serif;'>"
						+ "<p style='font-weight: bold;'>"+text+" Raised a Query</p>"
			            + "</body></html>";
				helper.setText(body, true);
				javaMailSender.send(message);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
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
			helper.setSubject("Thanks for reached us");
	        String body = "<html><body style='background-color: #f5f5f5; font-family: Arial, sans-serif;'>"
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
			helper.setText(body, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
	
	
	


}
