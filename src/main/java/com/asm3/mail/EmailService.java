package com.asm3.mail;

import java.io.File;

import javax.mail.MessagingException;

public interface EmailService {
	void sendMail(String recipientEmail, String subject, String text);
	
	void sendMailHtml(String recipentEmail, String subject, String text) throws MessagingException;

	void sendMailWithFile(String recipientEmail, String subject, String text, File pdfAttachment) throws MessagingException;

}
