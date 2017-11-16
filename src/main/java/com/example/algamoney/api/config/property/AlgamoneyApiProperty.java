package com.example.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/* Classe de configuração da aplicação. Por meio desta classe defini-se o
 * profile a ser utilizado.  */

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {
	
	private String origimPermitida = "http://localhost:8000";
	
	private final Seguranca seguranca = new Seguranca();
	
	public String getOrigimPermitida() {
		return origimPermitida;
	}
	
	public void setOrigimPermitida(String origimPermitida) {
		this.origimPermitida = origimPermitida;
	}
	
	public Seguranca getSeguranca() {
		return seguranca;
	}
	
	public static class Seguranca {
		
		private boolean enableHttps;
		
		public boolean isEnableHttps() {
			return enableHttps;
		}
		
		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
		
	}
	

}
