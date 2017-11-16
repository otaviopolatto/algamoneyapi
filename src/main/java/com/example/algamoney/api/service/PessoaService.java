package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
				
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);		
		/*Classe utilitária do Spring que copia as propriedades de um objeto
		para outro, ignorando a propriedade codigo */
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
		return pessoaRepository.save(pessoaSalva);
	}

	

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {		
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);	
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);				
	}
	
	
	private Pessoa buscarPessoaPeloCodigo(Long codigo) {
		//Validação de que a pessoa recebida existe no banco, ou é ela mesma
		Pessoa pessoaSalva = pessoaRepository.findOne(codigo);
		if(pessoaSalva == null) {
			//É esperado ao menos um resultado mais foi retornado 0
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}
	

}
