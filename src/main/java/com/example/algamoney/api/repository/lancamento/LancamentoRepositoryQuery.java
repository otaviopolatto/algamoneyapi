package com.example.algamoney.api.repository.lancamento;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;

/* Interface criada para a implementação da pesquisa de lançamentos. Na interface 
 * LancamentoRepository do SpringDataJPA não é possível implementar um novo método customizado
 * portanto cria-se então está interface LancamentoRepositoryQuery,que deve obrigatoriamente 
 * ter este nome para o SpringDataJPA entender sua implementação. */

public interface LancamentoRepositoryQuery {
	
	/* Este método filtrar somente aparece nas classes pois LancamentoFilter extende-se a está 
	 * interface também. */
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
 	
	
}
