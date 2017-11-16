package com.example.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

/*Recurso de Categoria, controlador da classe categoria */

/*Controlador Rest indica que um retorno será facilitado pois será convertido
 * para JSON por exemplo */

/* RequestMapping faz o mapeamento da requisição, quando for chegar uma requisição
 * ex: localhost:8080/categorias indica que é pra ser entregue nesta classe 
 *  */
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	/*Injeção do repositório que já contem os métodos pré-definidos
	 *automaticamente. Sem está annotation ocorrerá um erro ao executar
	 *o projeto pois o Spring não vai achar uma implementação da interface
	 *CategoriaRepository.  */
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	/* Mapeamento do Get para a URL categorias */
	@GetMapping
	/* Permite que todas as origens possam chamar o método listar (CORS). MaxAge é como
	 * se fosse um cache de 10 segundos. localhost:8000 é permitido. O @CrossOrigin poderia
	 * ser definido para a aplicação inteira, mais informações:
	 * http://spring.io/guides/gs/rest-service-cors/
	 * Obs:. O suporte ao cors do Spring não está funcionando perfeitamente com o OAuth2*/
	//@CrossOrigin (maxAge = 10, origins  = { "http://localhost:8000"})
	/* @PreAuthorize faz a validação em conjunto com o MethodSecurityExpressionHandler de que
	 * o usuário tem as permissões necessárias para executar o método. */
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}
	
	/* Mapeamento apenas para conhecimento:
	 * 
	 *O que é colocado em GetMapping("/outro") é incremental ao que está acima.
	 *Caso o mapeamento fosse apenas @GetMapping ocorreria um erro e a aplicação
	 *não iria subir.
	 *URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}")
				.buildAndExpand(categoriaSalva.getCodigo()).toUri();
			//Compõe o objeto response com o nome Location e com os dados da URI construída 
			response.setHeader("Location",uri.toASCIIString());
	 *@GetMapping("/outro")
		public String outro() {
		return "OK";
		}
	 */
	
	/*@PostMapping é a annotation responsável por salvar um objeto no banco de
	 * dados. @RequestBody é uma annotation do Spring responsável por transformar
	 * categoria direto em um objeto, sendo necessário apenas especificar a
	 * annotation.  
	 * 
	 * @ResponseStatus(HttpStatus.CREATED faz com que ao terminar a execução
	 * do método abaixo seja retornado o resultado 201 CREATED
	 * 
	 * O método REST diz que no Header do retorno deve ter a Location que diz
	 * onde o recurso foi criado. Quem faz este retorno é o HttpServletResponse
	 * 
	 * @Valid é uma annotation do BeanValidation para validar os campos. Com está
	 * validação ao informar um valor @NotNull em nome por exemplo já é retornado
	 * o erro 400 Bad Request. O método da classe Exception que trata de argumentos
	 * inválidos é o handleMethodArgumentNotValid
	 */
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) //201 CREATED
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		//Chama o objeto que cria um evento para a criação do header location utilizando este recurso
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		//Devolve o body do recurso criado
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
		
	}
	
	/*@PathVariable busca o código e armazena o mesmo na variável codigo, e então
	 * é feita a busca
	 * Uma ResponseEntity é criada no objeto para fazer o retorno do 200 OK ou
	 * do 404 Not Found caso o código referenciado seja inválido. 
	 *  */	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaRepository.findOne(codigo);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		categoriaRepository.delete(codigo);
	}
	
	
	
	
}
