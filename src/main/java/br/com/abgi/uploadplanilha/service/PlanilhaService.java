package br.com.abgi.uploadplanilha.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PlanilhaService {

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
	
}