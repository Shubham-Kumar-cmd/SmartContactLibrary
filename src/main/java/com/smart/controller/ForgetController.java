package com.smart.controller;

import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.helper.Message;
import com.smart.model.EmailRequest;
import com.smart.model.User;
import com.smart.repository.UserRepository;
import com.smart.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgetController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	//forget password handler
	@GetMapping("/forget")
	public String forgetPassword() {
		return "forget_email_form";
	}
	
	//send OTP handler
	@PostMapping("/send-otp")
	public String sendOtpToEmail(
			@RequestParam("email") String email,
			//Principal principal,
			Model model,
			HttpSession session
			) {
				
		//generating 7-digit otp 
		Random random = new Random();
		long otp = random.nextLong(10000000);
		System.out.println("otp "+otp);
		
		//write code to send otp to email
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setMessage(""
				+"<div style='border:1px solid #e2e2e2; padding:20px;'"
				+"<h1>"
				+"OTP of SmartContactLibrary is "
				+"<b>"
				+"<a href='#'>"+otp+"</a>"
				+"</b>"
				+"</h1>"
				+"</div>"
				);
		emailRequest.setSubject("OTP from SCL");
		emailRequest.setTo(email);
		
		
		boolean sendEmail = this.emailService.sendEmail(emailRequest);
		if(sendEmail) {
			model.addAttribute("title", "OTP send successfully");
			session.setAttribute("generatedOTP", otp);
			session.setAttribute("email", email);
			return "verify_otp";
			
		}
		model.addAttribute("title", "Forget Password");
		session.setAttribute("message", new Message("Something went wrong!!","alert-danger"));
		return "forget_email_form";
	}
	
	
	//verify otp handler
	@PostMapping("/verify-otp")
	public String processOtp(@RequestParam("otp") Long userOTP ,Model model,HttpSession session) {
		
		Long generatedOTP=(Long) session.getAttribute("generatedOTP");
		String email=(String) session.getAttribute("email");
		if(generatedOTP .equals(userOTP) ) {
			
			//password change form
			User user = this.userRepository.getUserByUserNameUser(email);
			if(user==null) {
				//send error message
				session.setAttribute("message", new Message("User not exist", "alert-danger"));
				return "forget_email_form";
			}
			else {
				model.addAttribute("title", "Recover your Password");
				return "change_password";
			}
			
		}
		else {
			//send change password form
			session.setAttribute("message", new Message("Invalid Otp", "alert-danger"));
			return "verify_otp";
		}
	}
	
	
	//change password handler
	@PostMapping("/recoverpassword")
	public String changePassword(@RequestParam("password") String password,Model model,HttpSession session) {
		String email=(String) session.getAttribute("email");
		User user = this.userRepository.getUserByUserNameUser(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(password));
		User save = this.userRepository.save(user);
		//session.setAttribute("message", new Message("password recovered successfully", "alert-success"));
		//return "login";
		return "redirect:/signin?change=password recovered successfully";
	}
}
