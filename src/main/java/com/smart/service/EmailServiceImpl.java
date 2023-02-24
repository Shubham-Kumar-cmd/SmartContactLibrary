package com.smart.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import com.smart.model.EmailRequest;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Override
	public boolean sendEmail(EmailRequest email) {
		boolean flag=false;
		String from="kumar53shubham@gmail.com";
		String pwd="dqexbcsqibkscmrc";
		
		//variable for gmail
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties prop=new Properties();
		
		//setting the information to properties object
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.auth", "true");
		
		//step 1. to get the session object
		Session session=Session.getInstance(prop,new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from,pwd);
			}
		});
		session.setDebug(true);
		
		//step 2 compose the message
		MimeMessage mimeMessage=new MimeMessage(session);
		
		try {
			//from email
			mimeMessage.setFrom(from);
			
			//adding recipient to message
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
			
			//adding subject to message
			mimeMessage.setSubject(email.getSubject());
			
			//adding text to message
			//mimeMessage.setText(email.getMessage());
			mimeMessage.setContent(email.getMessage(), "text/html");
			
			//send
			
			//step 3 send the message using Transport
			Transport.send(mimeMessage);
			
			System.out.println("Email send Successfully");
			flag=true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return flag;
	}

}
