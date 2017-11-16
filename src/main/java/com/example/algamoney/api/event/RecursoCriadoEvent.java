package com.example.algamoney.api.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

/*Um recurso criado por Evento é importante pois pode-se criar um comportamento
 * específico para cada objeto, ou um comportamento diferente quando outro
 * recurso for criado, etc.  
 */

public class RecursoCriadoEvent extends ApplicationEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HttpServletResponse response;
	private Long codigo;


	public RecursoCriadoEvent(Object source, HttpServletResponse response,
			Long codigo) {
		super(source);
		this.response = response;
		this.codigo = codigo;
		
	}


	public HttpServletResponse getResponse() {
		return response;
	}


	public Long getCodigo() {
		return codigo;
	}
	
	


}
