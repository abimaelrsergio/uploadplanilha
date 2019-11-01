package br.com.abgi.uploadplanilha.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	 * Faz upload de uma planilha excel
	 * @return Integer
	 */
	@PostMapping("/produtos/planilhas")
	public Resource<Integer> uploadProdutosExcel(@RequestParam MultipartFile planilha) {
		
		planilhaService.gravarNoDisco(planilha);

		Integer id = enviadorMensagem.enviarFila(planilha);
		
		if (id == null)
			return null;
		
		ControllerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).buscarPlanilha(id));
		Resource<Integer> resource = new Resource<Integer>(id);
		resource.add(linkTo.withRel("buscar-resultado"));
		
		return resource;
	}
	
	@GetMapping("/produtos/{id}")
	public Resource<Planilha> buscarPlanilha(@PathVariable int id){
		Planilha resultado = planilhaService.findById(id);
		
		if (!resultado.isProcessado())
			return null;
		
		ControllerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).retrieveAllProdutos());
		Resource<Planilha> resource = new Resource<Planilha>(resultado);
		resource.add(linkTo.withRel("todas-planilhas"));
		
		return resource;
	}
	
	@GetMapping("/produtos")
	public List<Produto> retrieveAllProdutos() {
		return produtoService.findAll();
	}
}
