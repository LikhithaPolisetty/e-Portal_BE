package com.tabner.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.Mail;

/*
 * this class is written to send the email notifications to the employees to let them know that the payroll has been processed 
 */

@Service
public class Task1MailService {

	@Autowired
	private JavaMailSenderImpl mailSender;

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	
	
	
	public void sendMailToUser(String username, String key) {
		MimeMessage message = mailSender.createMimeMessage();
		String s = "http://localhost:8080/TabnerEmployeePayroll/"+ username + "/" + key;
		try {
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(username);
		helper.setSubject("REGISTER HERE");
			helper.setText("<!DOCTYPE html>\r\n" + 
					"<html lang=\"en\">\r\n" + 
					"<head>\r\n" + 
					"    <meta charset=\"UTF-8\">\r\n" + 
					"    <title>Title</title>\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"    <div>\r\n" + 
					"        <h4 style=\"font-family: 'Bookman Old Style';color: #2d60a3\">Hi Customer,</h4>\r\n" + 
					"\r\n" + 
					"        <P style=\"margin-left: 20px; font-family: 'Bookman Old Style';color: #2d60a3\">Please <span> <a href= " + s + ">click here</a>\r\n" + 
					"</span> to complete your registration.</P>\r\n" + 
					"\r\n" + 
					"        <br>\r\n" + 
					"        \r\n" + 
					"\r\n" + 
					"    </div>\r\n" + 
					"</body>\r\n" + 
					"</html>", true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailSender.send(message);
		
	}

	

}
