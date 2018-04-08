package com.example.algamoney.api.config.token;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import com.example.algamoney.api.util.UsuarioSistema;

/* Classe que personaliza o token para que seja possível adicionar mais informações 
 * no mesmo */
public class CustomTokenEnhancer implements TokenEnhancer {
	
	/* Com a autenticação é possível pegar o getPrincipal que tem o usuário logado */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		/* UsuarioSistema é uma classe personalizada que extende de User */
		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();
		
		/* Criação de um mapa que contém as informações a serem adicionadas no Token */
		Map<String, Object> addInfo = new HashMap<>();
		addInfo.put("nome", usuarioSistema.getUsuario().getNome());
		
		( (DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
		return accessToken;
	}
	
	

}
