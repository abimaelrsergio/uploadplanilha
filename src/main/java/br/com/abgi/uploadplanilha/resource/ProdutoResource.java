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
import br.com.abgi.uploadplanilha.service.EnviadorMensagem;
import br.com.abgi.uploadplanilha.service.PlanilhaService;

@RestController
public class ProdutoResource {

	@Autowired
	private PlanilhaService planilhaService;

	@Autowired
	private EnviadorMensagem enviadorMensagem;

	/**
	 * Faz upload de uma planilha excel
	 */
	@PostMapping("/produtos/planilhas")
	public void uploadProdutosExcel(@RequestParam MultipartFile planilha) {
		
		planilhaService.gravarNoDisco(planilha);

		enviadorMensagem.enviarFila(planilha);
	}
	
	@GetMapping("/produtos/{id}")
	public Resource<Planilha> buscarPlanilha(@PathVariable int id){
		Planilha resultado = planilhaService.buscarPlanilha(id);
		
		if (resultado == null)
			return null;
		
		ControllerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
		Resource<Planilha> resource = new Resource<Planilha>(resultado);
		resource.add(linkTo.withRel("todas-planilhas"));
		
		return null;
	}
	
	@GetMapping("/produtos")
	public List<Planilha> retrieveAllUsers() {
		return planilhaService.findAll();
	}
}
