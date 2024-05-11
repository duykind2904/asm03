package com.asm3.mail;

import java.io.File;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailDetail implements EmailService{    
	@Autowired private JavaMailSender javaMailSender;

	@Override
    public void sendMail(String recipientEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
	
	@Override
	 public void sendMailHtml(String recipientEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }
	
	@Override
	public void sendMailWithFile(String recipientEmail, String subject, String text, File pdfFile) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(text);
        
        if(pdfFile != null)
        helper.addAttachment(pdfFile.getName(), pdfFile);

        javaMailSender.send(message);
    }

}
