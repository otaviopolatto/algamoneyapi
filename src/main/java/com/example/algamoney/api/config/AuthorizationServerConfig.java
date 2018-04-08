package com.example.algamoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.example.algamoney.api.config.token.CustomTokenEnhancer;

@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer /* Diz que está é uma classe de autorização do servidor */
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	/* Quem gerencia a autenticação com usuário e senha */
	@Autowired
	public AuthenticationManager authenticationManager;
	
	/*Configuração da aplicação (Cliente: quem o usuário está usando)	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//super.configure(clients);
		clients.inMemory() /* Escolhido inMemory pois está é uma API apenas para se
		comunicar com um cliente em Angular */
			.withClient("angular")
			.secret("@ngul@r0")
			.scopes("read", "write") /* Poderia ser um array de Strings */
			/* Obs:. Pode-se definir escopos diferentes para clientes diferentes.
			 * Não é a String que define mas sim o que se faz com elas depois */
			.authorizedGrantTypes("password", "refresh_token") /* Fluxo Password Flow. Refresh 
			na aplicação também passa a validar o Token */
			/* Fluxo onde a aplicação recebe o usuário e senha do cliente, o angular
			 * no caso, e envia para o servidor solicitando o token. */
			/* Obs:. Aplicação deve ser de confiança do usuário pois ela tem seu
			 * usuário e senha */
			.accessTokenValiditySeconds(1800) /* 1800/60 = 30 minutos */ /* Substituido por: 20 segundos */	 
			.refreshTokenValiditySeconds(3600 * 24) /* Um dia inteiro para validar 3600(1 hora) * 24 */
		.and()
			.withClient("mobile")
			.secret("m0b1l30")
			.scopes("read")	/* Escopo não faz diferença nenhuma se não for utilizado em algum local */
			.authorizedGrantTypes("password", "refresh_token")		
			.accessTokenValiditySeconds(1800) 	 
			.refreshTokenValiditySeconds(3600 * 24);
		
		/* Obs:. O escopo prevalece sobre a permissão do usuário, exemplo: O usuário admin com o cliente 
		 * angular pode ler e escrever, o usuário admin com o cliente mobile pode apenas ler. */
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		/* Criação de um objeto Token com mais detalhes, tokenEnhancerChain */
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints
			.tokenStore(tokenStore()) /* Token precisa ser armazendo em algum lugar */
			.tokenEnhancer(tokenEnhancerChain) /* Objeto tokenEnhancerChain, que é o token personalizado */
			.accessTokenConverter(accessTokenConverter()) /* Especificação do conversor de Token */
			.reuseRefreshTokens(false) /* Evita a reutilização de um mesmo RefreshToken, ou seja a 
			cada requisição é sempre gerado um novo refreshToken. Caso a reutilização fosse verdadeira
			o token iria expirar em 24 horas. */
			.authenticationManager(authenticationManager); /* Validar usuário e senha */		
	}
	
	/* Especificação de um Token JWT */
	
	@Bean /* Quem precisar do accessTokenConverter consegue recupera-lo pelo Bean */
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks"); /* Chave de validação do Token */
		return accessTokenConverter;
	}

	/* Um Token pode ser armazenado no banco de dados ou em memória, porém ao utilizar o 
	 * web token JWT não é mais necessário armazena-lo no servidor. JwtTokenStore é um 
	 * objeto necessário para fazer a validação do Token.  */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
		/* Retorna um cara customizado que vai incrementar o Token */
		return new CustomTokenEnhancer();
	}

}
