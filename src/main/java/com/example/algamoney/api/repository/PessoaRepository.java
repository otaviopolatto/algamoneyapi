package com.example.algamoney.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.filter.PessoaFilter;
import com.example.algamoney.api.repository.pessoa.PessoaRepositoryQuery;

/* Interface que extende também de LancamentoRepositoryQuery para que seja possível a 
 * implementação da query de pesquisa pois somente nesta interface não seria possível
 * está implementação. */

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, 
	PessoaRepositoryQuery {

	/* Page<Pessoa> filtar(PessoaFilter pessoaFilter, Pageable pageable); */

}
