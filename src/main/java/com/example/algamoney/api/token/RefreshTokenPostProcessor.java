package com.example.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.algamoney.api.AlgamoneyApiApplication;
import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

/* Processador para o RefreshToken
 * 
 *  OAuth2AccessToken é o tipo de dado que deve ser interceptado no retorno. */

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {
	
	/* Objeto de definição do profile da aplicação */
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		
		/* Só é verdadeiro quando o nome do método for postAccessToken 
		 * Obs:. Informações como o método postAccessToken foram obtidas lendo a documentação
		 * do Spring. */
		return returnType.getMethod().getName().equals("postAccessToken");
	}
	
	/* Regra definida abaixo só é executada quando o supports retornar true */
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType 
			selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, 
			ServerHttpRequest request, ServerHttpResponse response) {
		
		/* Para adicionar o refreshToken no Cookie Http é necessário obter o 
		 * HttpServletRequest e HttpServletResponse, que são obtidos de ServerHttpRequest e 
		 * ServerHttpResponse respectivamente, convertidos para ServletServerHttpRequest e 
		 * ServletServerHttpResponse e armazenados em suas respectivas variáveis.
		 */
		
		HttpServletRequest req = ( (ServletServerHttpRequest) request).getServletRequest();
		
		HttpServletResponse resp = ( (ServletServerHttpResponse) response).getServletResponse();
		
		/* Obtido o body default de OAuth2 para poder remover o RefreshToken do Body. O Body
		 * OAuth2AccessToken não possui o método setRefreshToken */
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		String refreshToken = body.getRefreshToken().getValue();
		adicionarRefreshTokenNoCookie(refreshToken, req, resp);
		/* Remoção do RefreshToken do Body */
		removerRefreshTokenDoBody(token);
		
		return body;
	}
	
	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		
		token.setRefreshToken(null);
		
	}

	/* Maiores informações sobre como criar um Cookie podem ser obtidas em conteúdos
	 * sobre Servlet e JSP.  */
	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req,
			HttpServletResponse resp) {
		
		/* Criação do Cookie */
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		
		/* Definição de propriedades */
		refreshTokenCookie.setHttpOnly(false); /* Somente acessível em HTTP ? */
		//refreshTokenCookie.setSecure(false); /* Somente HTTPS. Mudar para true em produção */ 
		refreshTokenCookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
		refreshTokenCookie.setPath(req.getContentType() + "/oauth/token"); /* Para qual caminho o
		cookie deve ser enviado no browser ? */
		refreshTokenCookie.setMaxAge(2592000); /* Em quanto tempo este cookie deve expirar ? 30 dias */ 
		resp.addCookie(refreshTokenCookie); /* Adicionado o cookie na resposta */
		
	}

}
