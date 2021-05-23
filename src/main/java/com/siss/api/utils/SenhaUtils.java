package com.siss.api.utils;

import java.util.SplittableRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SenhaUtils {
	
	public static String gerarHash(String senha) {
		if (senha == null || senha == "") {
			return senha;
		}
		return new BCryptPasswordEncoder().encode(senha);
	}

	public static int gerarHashCode() {
		SplittableRandom splittableRandom = new SplittableRandom();
		return splittableRandom.nextInt(10000, 99999);
	}
	
	public static boolean compararHash(String senha, String senhaEncoded) {
		return new BCryptPasswordEncoder().matches(senha, senhaEncoded);
	}
}