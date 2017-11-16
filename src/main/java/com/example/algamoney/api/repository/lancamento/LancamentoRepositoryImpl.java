package com.example.algamoney.api.repository.lancamento;

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

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;


/* Classe de implementação do méotodo filtrar do LancamentoRepositoryQuery */

/* Obs:. Importar sempre do javax.persistence */

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	/* Importação do Entity Manager para se fazer a consulta */
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, 
			Pageable pageable) {
				
		/* Cara que consegue construir as criterias */
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		/* Criação da CriteriaQuery para Lancamento */
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		/* Adição das restrições */
		
		/* Root serve para pegar os atributos a serem utilizados no filtro */
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		/* Criação das Restrições */
		Predicate [] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);		
		
		/* Criação e retorno da Query */
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		
		/* Quantidade total de resultados a serem retornados	 */
		adicionarRestricoesDePaginacao(query, pageable);
				
		/* O retorno PageImpl é uma implementação do Page que retorna o 
		 * conteúdo, o pageable e o total (qtde de elem. deste filtro)		 * 
		 */
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));	
		
		
	}
	/* Cálcula a quantidade total de elementos do filtro */
	private Long total(LancamentoFilter lancamentoFilter) {
				
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = 
				builder.createQuery(Long.class); 
		/* FROM */
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter,
				builder, root);
		
		/* WHERE */
		criteria.where(predicates);
		/* COUNT (*) */
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}
	
	/* Método que calcula o número total de registros por página e 
	 * quando começa	 */
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
			
		int paginaAtual = pageable.getPageNumber();
		/*Número de páginas passado na URI */
		int totalRegistrosPorPagina = pageable.getPageSize();
		/* Ex: Pg 2 * 3 reg. por pagina = 6 */
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
		
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		
		/* Como o array de Predicate[] pode ter tamanho variável é criada uma lista de
		 *  predicates */
		List<Predicate> predicates = new ArrayList<>();
		
		/* Criação da lista de predicates com base do que é informado no filtro */
		
		//Como se fosse um WHERE descricao LIKE '%dsfsfsf%'
		if(!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get("descricao")), "%" +
					lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}		
		
		if(lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"), 
					lancamentoFilter.getDataVencimentoDe()));
		}
		
		if(lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"), 
					lancamentoFilter.getDataVencimentoAte()));
		}
		
		/* Retorna a lista de predicates transformada em um array */
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	
	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
				
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		/* Busca do ResumoLancamento a partir da entidade root */
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		/* A partir da busca agora é feita a filtragem e seleção */
		criteria.select(builder.construct(ResumoLancamento.class, 
				root.get("codigo"), 
				root.get("descricao"),
				root.get("dataVencimento"),
				root.get("dataPagamento"),
				root.get("valor"),
				root.get("tipo"),
				root.get("categoria").get("nome"),
				root.get("pessoa").get("nome")));
		
		/* Caso estivesse feito do outro modo seria: */
		/* criteria.select(builder.construct(ResumoLancamento.class, 
				root.get(Lancamento_.codigo), 
				root.get(Lancamento_.descricao),
				root.get(Lancamento_.dataVencimento),
				root.get(Lancamento_.dataPagamento),
				root.get(Lancamento_.valor),
				root.get(Lancamento_.tipo),
				root.get(Lancamento_.categoria).get(Categoria_.nome),
				root.get(Lancamento_.pessoa).get(Pessoa_.nome))); */
		
		/* Criação das Restrições */
		Predicate [] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);		
		
		/* Criação e retorno da Query */
		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		
		/* Quantidade total de resultados a serem retornados	 */
		adicionarRestricoesDePaginacao(query, pageable);
				
		/* O retorno PageImpl é uma implementação do Page que retorna o 
		 * conteúdo, o pageable e o total (qtde de elem. deste filtro)		 * 
		 */
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));	
		
	}
	
	

}
