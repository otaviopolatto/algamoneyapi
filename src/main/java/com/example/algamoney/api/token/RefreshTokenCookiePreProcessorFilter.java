package com.example.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/* Pré Processor, ou seja, antes de entrar */

@Component /* Componente do Spring */
@Order(Ordered.HIGHEST_PRECEDENCE) /*
									 * Filtro que tem prioridade alta. Deve ser analisado antes de todo mundo. Caso
									 * exista o RefreshToken content type, ou seja, caso o '/oauth/token' tenha o
									 * cookie, este deve ser adicionado na requisição para que a mesma funcione.
									 * Está classe é responsável por fazer com que o cookie seja adicionado
									 * automaticamente na requisição.
									 */
/* Filter deve ser de javax.servlet */
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		/*
		 * Usa o refresh_token que está no cookie apenas quando o grant_type for
		 * refresh_token
		 */
		if ("/oauth/token".equalsIgnoreCase(req.getRequestURI())
				&& "refresh_token".equals(req.getParameter("grant_type")) && req.getCookies() != null) {

			for (Cookie cookie : req.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					String refreshToken = cookie.getValue();
					/*
					 * Agora deve colocar no mapa de parâmetros da requisição, porém não é possível
					 * mecher mais na requisição depois que ela já está pronta, então é criada uma
					 * classe extra MyServletRequestWrapper (Um Wrapper do Servlet Request)
					 * personalizada.
					 */
					req = new MyServletRequestWrapper(req, refreshToken);
				}
			}
		}

		/* Continua seguindo a cadeia do filtro */

		chain.doFilter(req, response);

	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}

		/*
		 * Os valores que já estão dentro do mapa continuam, e então é adicionado o
		 * refreshToken, denominado como "refresh_token" pelo Spring Security. Este nome
		 * pode ser visto no Postman.
		 */
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			/*
			 * Mapa é definido como locked para evitar o risco de alguém incluir algum
			 * atributo neste mapa.
			 */
			map.setLocked(true);
			return map;
		}

	}

}
