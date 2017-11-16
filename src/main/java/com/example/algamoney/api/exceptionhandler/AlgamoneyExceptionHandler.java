package com.example.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

/* Um cara que captura exceções quando é preciso responder as entidades.   
 * É extendido de ResponseEntityExceptionHandler pois esta classe já tem vários
 * métodos úteis para capturar e tratar exceções.
 * 
 * Está classe tem a finalidade de tratar apenas as exceções gerais, que podem ser
 * utilizadas por vários objetos e não exceções mais específicas. */

/*Obs:. Para a classe conseguir capturar exceções é necessário que a mesmma
 * seja um Controller, mas um ControllerAdvice pois captura exceções */

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	/*
	 * Objeto MessageSource fica disponível a partir do momento que a aplicação
	 * Spring subiu. @Autowired injeta este objeto nesta classe.
	 */
	
	@Autowired
	private MessageSource messageSource;

	/* Mensagens que o Spring não conseguiu ler */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
			//Captura a mensagem do arquivo messages.properties
			String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
			//Mensagem para o desenvolvedor
			String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString()
					: ex.toString();
			List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
			return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
			//Ao testar com o POST sem argumento deve retornar a Mensagem Inválida
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
			/* É passada uma lista de erros como resposta para o caso de mais erros */
			List<Erro> erros = criarListaDeErros(ex.getBindingResult());
			return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	// BindingResult contém a lista de todos os erros
	private List<Erro> criarListaDeErros(BindingResult bindingResult) {

		List<Erro> erros = new ArrayList<>();

		// Monta a mensagem para o usuário e para o desenvolvedor
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			
			//fieldError = Campo que possui o erro
			String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			//fieldError.toString() = Mensagem de erro completa
			String mensagemDesenvolvedor = fieldError.toString();			
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}

		return erros;
	}

	

	public static class Erro {

		private String mensagemUsuario;
		private String mensagemDesenvolvedor;

		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			super();
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}

		public String getMensagemUsuario() {
			return mensagemUsuario;
		}

		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}

	}
	
	/* A exceção EmptyResultDataAccessException foi capturada da pilha
	 * de erros onde é informado que o código excluído não existe. A partir
	 * deste erro desenvolvemos o método para tratar está exceção
	 * Obs:. Para funcionar foi necessário importar manualmente a classe
	 * de exceção a partir do endereço na pilha de erros.  */
	
	/*
	@ExceptionHandler({EmptyResultDataAccessException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND) //Erro 404 - Não encontrado
	public void handleEmptyResultDataAccessException(RuntimeException ex) {
		
	}
	*/	
	
	/* Tratamento de exceção handleEmptyResultDataAccess com retorno de mensagem no body */
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(
			EmptyResultDataAccessException ex, WebRequest request) {
		
		//Mensagem para o usuário
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null,
				LocaleContextHolder.getLocale());
		//Mensagem para o desenvolvedor
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		//É retornado um new HttpHeader pois ocorre erro ao não retorna-lo
		return handleExceptionInternal(ex, erros, new HttpHeaders(),
				HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleDataIntegrityViolationException(
			DataIntegrityViolationException ex, WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
				//ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), 
				HttpStatus.BAD_REQUEST, request);
		
		
	}
	

}