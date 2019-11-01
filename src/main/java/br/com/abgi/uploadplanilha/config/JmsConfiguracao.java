package br.com.abgi.uploadplanilha.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class JmsConfiguracao {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Value("${spring.activemq.user}")
	private String usuario;

	@Value("${spring.activemq.password}")
	private String senha;

	@Bean
	public ActiveMQConnectionFactory connectionFactory() {
		if ("".equals(usuario)) {
			return new ActiveMQConnectionFactory(brokerUrl);
		}
		return new ActiveMQConnectionFactory(usuario, senha, brokerUrl);
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public JmsListenerContainerFactory jmsFactoryTopic(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setMessageConverter(jacksonJmsMessageConverter());
		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(true);
		return factory;
	}
	
    /*
     * Usado para receber a mensagem
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(jacksonJmsMessageConverter());
        configurer.configure(factory, connectionFactory);
        return factory;
    }
	
	  @Bean // Serialize message content to json using TextMessage
	  public MessageConverter jacksonJmsMessageConverter() {
	      MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	      converter.setTargetType(MessageType.TEXT);
	      converter.setTypeIdPropertyName("_type");
	      return converter;
	  }

	@Bean
	public JmsTemplate jmsTemplate() {
//		return new JmsTemplate(connectionFactory());
        JmsTemplate template = new JmsTemplate();
        template.setMessageConverter(jacksonJmsMessageConverter());
        template.setConnectionFactory(connectionFactory());
        return template;
	}

	@Bean
	public JmsTemplate jmsTemplateTopic() {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}
}
