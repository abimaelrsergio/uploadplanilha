package br.com.abgi.uploadplanilha.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
		Optional<Planilha> planilha = planilhaRepository.findById(id);
		return planilha.get();
	}

	public List<Planilha> findAll() {
		return planilhaRepository.findAll();
	}
	
	@SuppressWarnings("deprecation")
	public void lerPlanilha(Planilha planilha) throws IOException {
		InputStream ExcelFileToRead = new FileInputStream(planilha.getPath());
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;

		Iterator<Row> rows = sheet.rowIterator();

		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				cell = (XSSFCell) cells.next();

				if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
					System.out.print(cell.getStringCellValue() + " ");
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					System.out.print(cell.getNumericCellValue() + " ");
				}
			}
			System.out.println();
		}
		wb.close();
	}

	
}
