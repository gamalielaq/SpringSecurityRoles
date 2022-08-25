package com.SpringSecurityRoles.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.SpringSecurityRoles.constant.EmailConstant;

@Service
public class EmailService {

	public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
		Message message = createEmail(firstName, password, email);
		Transport transport = getEmailSession().getTransport(EmailConstant.SIMPLE_MAIL_TRANSFER_PROTOCOL);
		transport.connect(EmailConstant.OUTLOOK_SMTP_SERVER, EmailConstant.USERNAME, EmailConstant.PASSWORD);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	private Message createEmail(String firstName, String password, String email) throws MessagingException {
		Message message = new MimeMessage(this.getEmailSession());
		message.setFrom(new InternetAddress(EmailConstant.FROM_EMATL));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
		message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(EmailConstant.CC_EMATL, false));
		message.setSubject(EmailConstant.EMAIL_SUBJECT);
		message.setText("Hello " + firstName + "\n \n Para tu nueva cuenta cueta la contraseña es: " + password
				+ "\n \n Equipo de Soporte");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;

	}

	private Session getEmailSession() {
		Properties props = System.getProperties();
		props.put(EmailConstant.SMTP_HOST, EmailConstant.OUTLOOK_SMTP_SERVER);
		props.put(EmailConstant.SMTP_AUTH, true);
		props.put(EmailConstant.SMTP_PORT, EmailConstant.DEFAULT_PORT);
		props.put(EmailConstant.SMTP_STARITLS_ENABLE, true);
		props.put(EmailConstant.SMTP_STARTTLS_REQUIRED, true);
		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
		return Session.getInstance(props, null);
	}
}
