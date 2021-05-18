package com.siss.api.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;

	private void enviar(SimpleMailMessage email) {
		mailSender.send(email);
	}
}
