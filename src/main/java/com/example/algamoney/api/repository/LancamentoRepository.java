package com.example.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;

/* Interface extende também de LancamentoRepositoryQuery para que seja possível a 
 * implementação da query de pesquisa. Nesta interface não seria possível está
 * implementação.  
 */
public interface LancamentoRepository extends JpaRepository <Lancamento, Long>, 
	LancamentoRepositoryQuery{

}
