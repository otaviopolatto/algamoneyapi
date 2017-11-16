package com.example.algamoney.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

/* Classe de filtro para a pesquisa de lançamentos. A pesquisa podera ser feita por descrição
 * e por data de vencimento de até ... 
 */
public class LancamentoFilter {
	
	private String descricao;
	
	/* Provavelemente por algum bug no framework não houve uma forma de configurar este formato de 
	 * data como global, portanto foi adicionada a annotation DateTimeFormat localmente. */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoDe;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoAte;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public LocalDate getDataVencimentoDe() {
		return dataVencimentoDe;
	}
	public void setDataVencimentoDe(LocalDate dataVencimentoDe) {
		this.dataVencimentoDe = dataVencimentoDe;
	}
	public LocalDate getDataVencimentoAte() {
		return dataVencimentoAte;
	}
	public void setDataVencimentoAte(LocalDate dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}

}
