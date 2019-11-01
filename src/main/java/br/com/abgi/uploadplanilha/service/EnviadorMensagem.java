package br.com.abgi.uploadplanilha.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.abgi.uploadplanilha.model.Planilha;
import br.com.abgi.uploadplanilha.model.Produto;


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
	@Autowired
	private ProdutoService produtoService;

	@JmsListener(destination = FILA_PLANILHAS, containerFactory = "jmsFactory")
	//public void onReceiverQueue(String mensagem) {
	public void onReceiverQueue(Planilha planilha) {
//		Planilha planilha = new Planilha();
//		planilha.setPath(mensagem);

		try {
			List<Produto> produtos = planilhaService.lerPlanilha(planilha);
			planilha.setProcessado(true);
			
			for (Produto produto : produtos) {
				System.out.println(produto.getName());
				produtoService.save(produto);
			}
		} catch (IOException e) {
			e.printStackTrace();
			planilha.setProcessado(false);
		}

		planilhaService.save(planilha);
	}

	@JmsListener(destination = TOPICO_PLANILHAS, containerFactory = TOPIC_CONTAINER_FACTORY)
	public void onReceiverTopic(String mensagem) {
		System.out.println(mensagem);
	}

	public void enviarFila(MultipartFile file) {

		Planilha planilha = new Planilha();
		planilha.setPath(planilhaService.obterPath(file));
		planilhaService.save(planilha);
		planilha.setProcessado(false);
		
		jmsTemplate.convertAndSend(FILA_PLANILHAS, planilha);
	}

	public void enivarTopico() {
		jmsTemplateTopic.convertAndSend(TOPICO_PLANILHAS, "{nome: 'Abimael', sobrenome: 'Sergio'}");
	}
}
