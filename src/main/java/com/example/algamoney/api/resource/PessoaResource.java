package com.example.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private PessoaService pessoaService;
	
	@GetMapping
	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, 
			HttpServletResponse response) {
			
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);	
		
		//Constroi a URI de devolução da location
		//This = source (quem, objeto que gerou o publisher)
		//Chama o objeto que cria um evento para a criação do header location utilizando este recurso
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		//Devolve o body do recurso criado
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);	
		
	}
	
			
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Pessoa> buscarPeloCodigo(@PathVariable Long codigo) {
		Pessoa pessoa = pessoaRepository.findOne(codigo);
		return pessoa != null ? ResponseEntity.ok(pessoa) :
			ResponseEntity.notFound().build();
	}
	
	/* Repare que ao incluir a segurança a mesma vale para a aplicação toda. A não inclusão
	 * da annotation @PreAuthorize neste caso não quer dizer que o recurso não está protegido,
	 * ele está automaticamente pelo escopo de cada cliente, angular ou mobile. Observe nos
	 * métodos abaixo:  */
	
	@DeleteMapping("/{codigo}")
	//Retorna código 204. Deu certo porém não tem o que retornar
	@ResponseStatus(HttpStatus.NO_CONTENT) 
	public void remover(@PathVariable Long codigo) {
		pessoaRepository.delete(codigo);
	}
		

	/* Método atualizar
	 * Obs:. Repare que todas as validações feitas até agora valém para a operação
	 * de UPDATE */
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid
			@RequestBody Pessoa pessoa) {
		
		//Chama a função atualizar da classe de serviço, que contém regras de negócio
		Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.ok(pessoaSalva);
		
	}
	
	
	/* Obs:. Por default @RequestBody required=true */
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, 
			@RequestBody Boolean ativo) {
		
		pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
		
		
	}
	
	
	
	

}
