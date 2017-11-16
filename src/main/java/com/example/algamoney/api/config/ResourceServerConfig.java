package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration /* Classe de Configuração */
@EnableWebSecurity /* Habilitador de Segurança */
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) /* Habilita a segurança nos métodos */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	/*
	 * Ao extender de ResourceServerConfigurerAdapter ganha-se alguns métodos que
	 * facilitam a configuração de segurança, como o método configure.
	 */

	/* Classe de detalhamento de usuários do Spring */
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired /* Injetado */
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		/* Overrided */
		// auth.inMemoryAuthentication()
		// .withUser("admin").password("admin").roles("ROLE");
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

	}

	/* Autorização de Requisições */

	/*
	 * Método pai é público portanto sua visibilidade não pode ser mais específica
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// super.configure(http);
		/* Para qualquer requisição é necessário estar autenticado */
		http.authorizeRequests()
				/*
				 * Uma vez aplicada a segurança dos métodos de categorias definindo quais ROLES
				 * podem executa-los a linha de código abaixo deixa de fazer sentido.
				 */
				.antMatchers("/categorias").permitAll() /* Exceto categorias */
				.anyRequest().authenticated().and()
				// .httpBasic().and() não é httpBasic() mais
				.sessionManagement() /* Desabilita a criação de sessão no servidor */
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				/* Não mantém estado de nada, garante que seja STATELESS */
				.csrf().disable(); /* E também csrf desabilitado */
		/* CSRF seria basicamente um JavaScript Injection dentro do serviço */
	}

	/* Para que a parte de segurança do servidor seja STATELESS */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		
		// super.configure(resources);
		resources.stateless(true);
	}

	/*
	 * A classe GeradorSenha do pacote util gera senhas encodadas utilizadas no
	 * passwordEncoder
	 */

	@Bean /* Método definido como um Bean pois houve erro na migração do Flyway */
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}

	/*
	 * Método que retorna um objeto que permite tratar segurança dos métodos com o
	 * OAuth2
	 */
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}

}
