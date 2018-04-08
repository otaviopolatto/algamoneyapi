package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("basic-security")
@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/* O userDetailsService é o mesmo userDetailsService do que o oauth-config,
	 * que é o AppUserDetailsService que faz a busca do usuário e faz a 
	 * autenticação no banco de dados.  
	 */
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/* Ao extender de WebSecurityConfigurerAdapter ganha-se alguns métodos
	 * que facilitam a configuração de segurança, como o método
	 * configure.
	 */
	
	@Override
	protected void configure(AuthenticationManagerBuilder oauth) throws Exception {
		oauth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/* Autorização de Requisições */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/* Para qualquer requisição é necessário estar autenticado */
		http.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.httpBasic()
			.and()
			.sessionManagement() /* Desabilita a criação de sessão no servidor */
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and() /* Não mantém estado de nada */
			.csrf().disable(); /* CSRF seria basicamente um JavaScript Injection dentro do serviço */
	}
	
	
	
	

}
