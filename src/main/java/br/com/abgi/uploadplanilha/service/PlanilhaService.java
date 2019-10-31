package br.com.abgi.uploadplanilha.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.abgi.uploadplanilha.model.Planilha;
import br.com.abgi.uploadplanilha.repository.PlanilhaRepository;

@Service
public class PlanilhaService {
	
	@Autowired
	private PlanilhaRepository planilhaRepository;

	@Value("${pasta.temporaria}")
	private String raiz;

	@Value("${pasta.temporaria.planilha}")
	private String diretorioFotos;

	/**
	 * Grava o arquivo no disco.
	 * 
	 * @param planilha
	 */
	public void gravarNoDisco(MultipartFile planilha) {

		Path diretorioPath = Paths.get(this.raiz, this.diretorioFotos);
		Path arquivoPath = diretorioPath.resolve(planilha.getOriginalFilename());

		try {
			Files.createDirectories(diretorioPath);
			planilha.transferTo(arquivoPath.toFile());
		} catch (IOException e) {
			throw new RuntimeException("[ERRO] Gravar no HD");
		}
	}

	public String obterPath(MultipartFile planilha) {

		Path diretorioPath = Paths.get(this.raiz, this.diretorioFotos);
		Path arquivoPath = diretorioPath.resolve(planilha.getOriginalFilename());

		return arquivoPath.toString();
	}

	public void save(Planilha planilha) {
		planilhaRepository.save(planilha);	
	}

	public Planilha buscarPlanilha(int id) {
//		Planilha planilhaExemplo = new Planilha();
//		planilhaExemplo.setPath(id);
//		Example<Planilha> exemplo = Example.of(planilhaExemplo);
//		Optional<Planilha> encontrada = planilhaRepository.findOne(exemplo);
//		return encontrada.get();
		Optional<Planilha> planilha = planilhaRepository.findById(id);
		return planilha.get();
	}

	public List<Planilha> findAll() {
		return planilhaRepository.findAll();
	}
	
}
