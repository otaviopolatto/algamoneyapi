package com.example.algamoney.api.repository.filter;

/* Classe de filtro para a pesquisa de Pessoas. A pesquisa poderá ser feita por
 * nome */

public class PessoaFilter {
	
	private String nome;
	private Boolean ativo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	
	
	

}
