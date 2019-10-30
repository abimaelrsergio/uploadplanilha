package br.com.abgi.uploadplanilha.service;

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
		System.out.println(mensagem);
		Planilha planilha = new Planilha();
		planilha.setPath(mensagem);
		planilhaService.save(planilha);
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
