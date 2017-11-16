package com.example.algamoney.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenhas {
	
	public static void main(String[] args) {
		/* Executar como uma aplicação simples em Java para gerar a senha */
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("mypassword"));
	}

}
