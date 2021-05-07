package com.vaccine.slots.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

	public static void sendemail(String user, String password, String body, String recipientEmail) {
		String from = user;
		String pass = password;
		String[] to = { recipientEmail }; // list of recipient email addresses
		String subject = "Vaccine availablity!!";

		sendFromGMail(from, pass, to, subject, body);
	}

	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);

		try {
			MimeMessage message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart("alternative");

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(body, "text/html; charset=utf-8");
			multipart.addBodyPart(htmlPart);
			message.setContent(multipart);
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];
			System.out.println("Body: " + body);
			// To get the array of addresses
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			// Required magic (violates principle of least astonishment).
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
}
