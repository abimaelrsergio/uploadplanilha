package br.com.abgi.uploadplanilha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EnviadorMensagem {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private JmsTemplate jmsTemplateTopic;
	@Autowired
	private PlanilhaService planilhaService;

	@JmsListener(destination = "FILA.PLANILHAS")
	public void onReceiverQueue(String str) {
		System.out.println(str);
	}

	@JmsListener(destination = "TOPICO.PLANILHAS", containerFactory = "jmsFactoryTopic")
	public void onReceiverTopic(String str) {
		System.out.println(str);
	}

	public void enviarFila(MultipartFile planilha) throws Exception {
		jmsTemplate.convertAndSend("FILA.PLANILHAS", planilhaService.obterPath(planilha));
	}

	public void enivarTopico() throws Exception {
		jmsTemplateTopic.convertAndSend("TOPICO.PLANILHAS", "{user: 'wolmir', usando: 'tópico'}");
	}
}
