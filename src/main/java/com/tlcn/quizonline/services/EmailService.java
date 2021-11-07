package com.tlcn.quizonline.services;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tlcn.quizonline.models.User;
import com.tlcn.quizonline.models.VerifyToken;

@Service
public class EmailService {
	@Autowired
	JavaMailSender eMailSender;

	private String emailAddress = "xhuuanng1289@gmail.com";

	public void sendMail(User user, VerifyToken token) throws MessagingException {
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(emailAddress);
        mailMessage.setText("To confirm your account, please click here : "
        +"http://localhost:8080/verify?token="+token.getToken());

        eMailSender.send(mailMessage);
		
		System.out.println("sent email");
	}

}
