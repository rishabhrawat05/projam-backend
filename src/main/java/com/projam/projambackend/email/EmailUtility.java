package com.projam.projambackend.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class EmailUtility {

	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String email;
	
	public EmailUtility(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	@Async
	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
	}
	
	@Async
	public void sendEmail(String name, String from, String subject, String messageBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject(subject);
        message.setText("Sender: " + name + "\n\nMessage:\n" + messageBody);
        javaMailSender.send(message);
    }
}
