package br.com.abgi.uploadplanilha.resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.abgi.uploadplanilha.exceptions.ProdutoNotFoundException;
import br.com.abgi.uploadplanilha.model.Planilha;
import br.com.abgi.uploadplanilha.model.Produto;
import br.com.abgi.uploadplanilha.service.EnviadorMensagem;
import br.com.abgi.uploadplanilha.service.PlanilhaService;
import br.com.abgi.uploadplanilha.service.ProdutoService;

@RestController
public class ProdutoResource {

	@Autowired
	private PlanilhaService planilhaService;

	@Autowired
	private EnviadorMensagem enviadorMensagem;

	@Autowired
	private ProdutoService produtoService;

	/**
	 * Receberá uma planilha de produtos (segue em anexo) que deve ser processada em
	 * background (queue).
	 * 
	 * @return Integer
	 */
	@PostMapping("/produtos/planilhas")
	public Resource<Integer> uploadProdutosExcel(@RequestParam MultipartFile planilha) {

		planilhaService.gravarNoDisco(planilha);

		Integer id = enviadorMensagem.enviarFila(planilha);

		ControllerLinkBuilder linkTo = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).buscarPlanilha(id));
		Resource<Integer> resource = new Resource<Integer>(id);
		resource.add(linkTo.withRel("buscar-resultado"));

		return resource;
	}

	@PostMapping("/produtos/produto")
	public ResponseEntity<Object> atualizaProduto(@RequestBody Produto produto) {

		Produto produtoSalvo = produtoService.save(produto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(produtoSalvo.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * Ter um endpoint que informe se a planilha for processada com sucesso ou não.
	 * Verifica se os produtos passados na planilha foram criados
	 * 
	 * @param id protocolo de processamento da planilha
	 * @return true se a planilha foi processada
	 */
	@GetMapping("/produtos/{id}")
	public Resource<Planilha> buscarPlanilha(@PathVariable int id) {
		Planilha resultado = planilhaService.findById(id);

		if (!resultado.isProcessado())
			throw new ProdutoNotFoundException("id-" + id);

		ControllerLinkBuilder linkTo = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).retrieveAllProdutos());
		Resource<Planilha> resource = new Resource<Planilha>(resultado);
		resource.add(linkTo.withRel("todas-planilhas"));

		return resource;
	}

	/**
	 * Seja possível apagar um produto na base de dados
	 * 
	 * @param id
	 */
	@DeleteMapping("/produtos/{id}")
	public void apagarProduto(@PathVariable int id) {
		Produto resultado = produtoService.deleteById(id);

		if (resultado == null)
			throw new ProdutoNotFoundException("id-" + id);
	}

	/**
	 * Seja possível visualizar Busca todos os produtos na base
	 * 
	 * @return
	 */
	@GetMapping("/produtos")
	public List<Produto> retrieveAllProdutos() {
		return produtoService.findAll();
	}
}
