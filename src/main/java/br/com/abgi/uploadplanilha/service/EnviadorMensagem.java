package br.com.abgi.uploadplanilha.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.abgi.uploadplanilha.model.Planilha;


@Service
public class EnviadorMensagem {

	private static final String TOPIC_CONTAINER_FACTORY = "jmsFactoryTopic";
	private static final String TOPICO_PLANILHAS = "TOPICO.PLANILHAS";
	private static final String FILA_PLANILHAS = "FILA.PLANILHAS";
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private JmsTemplate jmsTemplateTopic;
	@Autowired
	private PlanilhaService planilhaService;

	@JmsListener(destination = FILA_PLANILHAS)
	public void onReceiverQueue(String mensagem) {
		Planilha planilha = new Planilha();
		planilha.setPath(mensagem);

		try {
			lerPlanilha(planilha);
			planilha.setProcessado(true);
		} catch (IOException e) {
			e.printStackTrace();
			planilha.setProcessado(false);
		}

		planilhaService.save(planilha);
	}

	private void lerPlanilha(Planilha planilha) throws IOException {
		InputStream ExcelFileToRead = new FileInputStream("C:/Test.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

		XSSFWorkbook test = new XSSFWorkbook();

		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;

		Iterator rows = sheet.rowIterator();

		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			while (cells.hasNext()) {
				cell = (XSSFCell) cells.next();

				if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
					System.out.print(cell.getStringCellValue() + " ");
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					System.out.print(cell.getNumericCellValue() + " ");
				} else {
					// U Can Handel Boolean, Formula, Errors
				}
			}
			System.out.println();
		}
	}

	@JmsListener(destination = TOPICO_PLANILHAS, containerFactory = TOPIC_CONTAINER_FACTORY)
	public void onReceiverTopic(String mensagem) {
		System.out.println(mensagem);
	}

	public void enviarFila(MultipartFile planilha) {

		jmsTemplate.convertAndSend(FILA_PLANILHAS, planilhaService.obterPath(planilha));
	}

	public void enivarTopico() {
		jmsTemplateTopic.convertAndSend(TOPICO_PLANILHAS, "{nome: 'Abimael', sobrenome: 'Sergio'}");
	}
}
