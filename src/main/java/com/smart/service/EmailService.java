package com.smart.service;

import com.smart.model.EmailRequest;

public interface EmailService {
	
	boolean sendEmail(EmailRequest email);
}
