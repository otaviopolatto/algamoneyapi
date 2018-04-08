package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	/* Tem que verificar se a pessoa a qual o lançamento se refere
	 * existe e está ativa */
	public Lancamento salvar(Lancamento lancamento) {
		
		validarPessoa(lancamento);
		/* A pessoa existe e está ativa portanto pode-se salva-la */		
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		
		Lancamento lancamentoSalvo = buscarLancamentoExistentePeloCodigo(codigo);
		/* Se a pessoa do lançamento não for igual a pessoa do lançamento
		 * salvo já existente, então é feita uma nova validação */
		if(!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}
		
		/* Classe utilitária do Spring que copia as propriedades de um
		 * objeto para outro, ignorando a propriedade codigo caso a mesma
		 * venha na atualização e utiliza sim o código já existente, ou seja,
		 * atualiza mas ignora a propriedade código */
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		return lancamentoRepository.save(lancamentoSalvo);
	}

	
	/* Função que faz a validação da pessoa, se a mesma existe e 
	 * está ativa ou não */
	private void validarPessoa(Lancamento lancamento) {
		
		Pessoa pessoa = null;
		
		/* Se pessoa não for inexistente */
		if(lancamento.getPessoa().getCodigo() != null) {
			/* Obtém a pessoa do código referenciado */
			pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		}
		/* Se pessoa não existe e estiver inativa então é lançada
		 * uma PessoaInexistenteOuInativaException */
		if(pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		/* A pessoa existe e está ativa portanto pode-se salva-la */
		
	}
	
	private Lancamento buscarLancamentoExistentePeloCodigo(Long codigo) {
		
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		
		/* Caso não exista lança uma IllegalArgumentException para ser
		 * capturada na classe LancamentoResource */		
		if(lancamentoSalvo == null) {
			throw new IllegalArgumentException();
		}
		
		return lancamentoSalvo;
	}
	

}
