package com.example.algamoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

/* Habilitação do CORS na Aplicação */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter{
	
	//Overriden
	/* Para produção é uma coisa para teste é outra */
	//private String origimPermitida = "http://localhost:8000"; // Configurar para diferentes ambientes
	
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	
	/* CORS nada mais é do que adicionar os Headers HTTP */

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
			
		HttpServletRequest request = (HttpServletRequest) req;
		
		HttpServletResponse response = (HttpServletResponse) resp;
		
		/* Definição da origem permitida */
		response.setHeader("Access-Control-Allow-Origin", algamoneyApiProperty.getOrigimPermitida());
		/* Allow-Credentials é true para que o cookie do refresh token seja enviado */
		response.setHeader("Access-Control-Allow-Credentials", "true");
		/* Os items acima estão fora do OPTION pois estes devem ser enviados obrigatoriamente
		 * em todas as requisições */
		
		/* Se a requisição for um "OPTIONS" e a origimPermitida for igual a Origin do browser então 
		 * é permitida o PréLighted Request */
		if("OPTIONS".equals(request.getMethod()) && algamoneyApiProperty.getOrigimPermitida().equals(request.getHeader("Origin")) ) {
			/* Definição de todos os métodos, headers e tempo aceitos no Header */
			response.setHeader("Access-Control-Allow-Methods","POST, GET, DELETE, PUT, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers","Authorization, Content-Type, Accept");
			response.setHeader("Access-Control-Max-Age","3600"); //Uma hora
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			chain.doFilter(req, resp);
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		
	}

	@Override
	public void destroy() {
		
		
	}
}
