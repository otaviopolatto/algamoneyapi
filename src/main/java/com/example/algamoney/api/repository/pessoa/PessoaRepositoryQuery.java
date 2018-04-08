package com.example.algamoney.api.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.filter.PessoaFilter;

/* Interface criada para a implementação da pesquisa de pessoas. Na interface 
 * PessoaRepository do SpringDataJPA não é possível implementar um novo método customizado
 * portanto cria-se então está interface PessoaRepositoryQuery,que deve obrigatoriamente 
 * ter este nome para o SpringDataJPA entender sua implementação. */

public interface PessoaRepositoryQuery {
	
	/* Este método filtrar somente aparece nas classes pois PessoaFilter extende-se a está 
	 * interface também. */
	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);

}
