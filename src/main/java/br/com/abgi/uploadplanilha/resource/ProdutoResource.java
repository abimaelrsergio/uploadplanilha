package br.com.abgi.uploadplanilha.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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
	public Resource<Planilha> buscarPlanilha(){
		return null;
	}
}
