package com.payee.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.jms.ConnectionFactory;

@EnableJms
@Configuration
public class JMSConfig {

    @Bean
    public MarshallingMessageConverter bookingMessageConverter() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.payee.booking.model");

        MarshallingMessageConverter converter = new MarshallingMessageConverter();
        converter.setMarshaller(marshaller);
        converter.setUnmarshaller(marshaller);
        converter.setTargetType(MessageType.TEXT);

        return converter;
    }


    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MarshallingMessageConverter bookingMessageConverter) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setMessageConverter(bookingMessageConverter);
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory, MarshallingMessageConverter bookingMessageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(bookingMessageConverter);
        factory.setConcurrency("1-1");
        return factory;
    }

}

