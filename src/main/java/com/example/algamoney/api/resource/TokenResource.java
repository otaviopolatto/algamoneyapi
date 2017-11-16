package com.example.algamoney.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	
	/* Classe responsável por revogar, invalidar o acesso do AccessToken ao efetuar o logout da 
	 * aplicação. O usuário ao clicar em logout vai apagar do browser o cookie com o AccessToken
	 * ou RefreshToken que ele estiver usando, e no servidor deve ser removido o RefreshToken do 
	 * Cookie, isto é o que está sendo implementado abaixo:  */
	
	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest req, HttpServletResponse resp) {
		
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		//cookie.setSecure(false); //Em produção será true
		/* Definição de acordo com o Profile */
		cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);
		
		resp.addCookie(cookie);
		resp.setStatus(HttpStatus.NO_CONTENT.value());
		
	}

}
