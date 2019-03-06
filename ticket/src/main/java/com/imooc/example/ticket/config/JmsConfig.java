package com.imooc.example.ticket.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.connection.TransactionAwareConnectionFactoryProxy;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JmsConfig {
	@Bean
	public ConnectionFactory connectionFactory(){
		ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://123.59.197.127:61616");
		TransactionAwareConnectionFactoryProxy proxy = new TransactionAwareConnectionFactoryProxy();
		proxy.setTargetConnectionFactory(cf);
		proxy.setSynchedLocalTransactionAllowed(true);
		return proxy;	
	}

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,MessageConverter jacksonJmsMessageConverter) {
        JmsTemplate template = new JmsTemplate(connectionFactory); // JmsTemplate使用的connectionFactory跟JmsTransactionManager使用的必须是同一个，不能在这里封装成caching之类的。
        template.setMessageConverter(jacksonJmsMessageConverter);
        template.setSessionTransacted(true);
        return template;
    }

    // 这个用于设置 @JmsListener使用的containerFactory
    @Bean
    public JmsListenerContainerFactory<?> msgFactory(ConnectionFactory cf,
                                                     DefaultJmsListenerContainerFactoryConfigurer configurer,
                                                     PlatformTransactionManager transactionManager) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setTransactionManager(transactionManager);
        factory.setCacheLevelName("CACHE_CONNECTION");
        factory.setReceiveTimeout(10000L);
        factory.setConcurrency("10");
        configurer.configure(factory, cf);
        return factory;
    }
    
    @Bean
    public MessageConverter jacksonJmsMessageConverter(){
    	MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    	converter.setTargetType(MessageType.TEXT);
    	converter.setTypeIdPropertyName("_type");
    	return converter;
    }
//    
//
//    @Bean
//    public PlatformTransactionManager transactionManager(ConnectionFactory connectionFactory) {
//        return new JmsTransactionManager(connectionFactory);
//    }
	

}
