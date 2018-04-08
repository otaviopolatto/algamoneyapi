package com.example.algamoney.api.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.filter.PessoaFilter;

/* Classe de implementação do método filtrar do PessoaRepositoryQuery */

/* Obs:. Importar sempre do javax.persistence */

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {
	
	/* Importação do Entity Manager para se fazer a consulta */
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
		
		/* Cara que consegue construir as criterias */
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		/* Criação da CriteriaQuery para Pessoa */
		CriteriaQuery<Pessoa> criteriaPessoa = criteriaBuilder.createQuery(Pessoa.class);
		
		/* Adição das Restrições */
		
		/* Root serve para pegar os atributos a serem utilizados no filtro */
		Root<Pessoa> root = criteriaPessoa.from(Pessoa.class);
				
		/* Criação das Restrições */
		Predicate [] predicates = criarRestricoes(pessoaFilter, criteriaBuilder, root);
		criteriaPessoa.where(predicates);
		
		/* Criação do Retorno da Query */
		TypedQuery<Pessoa> queryReturn = entityManager.createQuery(criteriaPessoa);
		
		/* Quantidade total de resultados a serem adicionados */
		adicionarRestricoesDePaginacao(queryReturn, pageable);
		
		return new PageImpl<>(queryReturn.getResultList(), pageable, total(pessoaFilter));
	}
	
	private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder builder, Root<Pessoa> root) {
		/* Como o array de Predicates [] pode ser de tamanho variável é criada uma
		 * lista de Predicates */
		List<Predicate> predicates = new ArrayList<>();
		
		/* Criação da lista de predicates com base no que é informado no filtro */
		
		/* Como se fosse um WHERE LIKE 'HFSDSD' */
		if(!StringUtils.isEmpty(pessoaFilter.getNome())) {
			predicates.add(builder.like(builder.lower(root.get("nome")), "%" 
					+ pessoaFilter.getNome().toLowerCase() + "%"));
		}
		//if(!StringUtils.isEmpty(pessoaFilter.isAtivo())){
		if(pessoaFilter.isAtivo() != null) {
			predicates.add(builder.isTrue(builder.equal(root.get("ativo"), pessoaFilter.isAtivo())));
			//predicates.add(builder.equal(root.get("ativo"), pessoaFilter.isAtivo()));
		}
		
		/* Retorna a lista de Predicates transformada em um Array */
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	/* Método que cálcula o número total de registros por página e quando começa */
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		
		int paginaAtual = pageable.getPageNumber();
		/* Número de páaginas passada na URI */
		int totalRegistrosPorPagina = pageable.getPageSize();
		/* Ex: página 2 * 3 registros por página = 6 registros */
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
		
	}

	/* Cálcula a quantidade total de elementos do filtro */
	private Long total(PessoaFilter pessoaFilter) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		/* FROM */
		Root<Pessoa> root = criteria.from(Pessoa.class);
		/* WHERE */
		Predicate [] predicates = criarRestricoes(pessoaFilter, builder, root);
		criteria.where(predicates);
		/* COUNT */
		criteria.select(builder.count(root));
		
		return entityManager.createQuery(criteria).getSingleResult();
	}

}
