package com.walmart;

import javax.inject.Inject;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.walmart.ticketservice.dao.HoldSeatsDao;
import com.walmart.ticketservice.dao.impl.HoldSeatsDaoImpl;
import com.walmart.ticketservice.dao.impl.HoldSeatsDaoImpl2;
import com.walmart.ticketservice.dao.impl.ResetHoldSeatsDaoImpl;

/**
 * 
 * @author maharshi Purpose: This Class will Set up the RebitMQ config in the
 *         context.
 *
 */

@Configuration
@ComponentScan("com.memorynotfound.rabbitmq")
public class RabbitMqConfig {

	/**
	 * Declaring and initiating simple message queue.
	 */
	private static final String SIMPLE_MESSAGE_QUEUE = "direct";

	@Value("${cloud.mq.host}")
	private String rabbitMQURI;

	@Value("${cloud.mq.virtual.host}")
	private String rabbitMQVirtualHost;

	@Value("${cloud.mq.user}")
	private String rabbitMQUser;

	@Value("${cloud.mq.password}")
	private String rabbitMQPassword;

	/**
	 * Injecting the MongoTemplet to pass it as parameter in MQ Listner.
	 */
	@Inject
	MongoTemplate templet;

	/**
	 * Creating the bean for RabbitMQ Connection.
	 * 
	 * @return ConnectionFactory for rebbitMQ.
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
				rabbitMQURI);
		connectionFactory.setUsername(rabbitMQUser);
		connectionFactory.setPassword(rabbitMQPassword);
		connectionFactory.setVirtualHost(rabbitMQVirtualHost);
		return connectionFactory;
	}

	/**
	 * Creating the bean for RabbitMQ Queue.
	 * 
	 * @return Queue Object for declared queue.
	 */
	@Bean
	public Queue simpleQueue() {
		return new Queue(SIMPLE_MESSAGE_QUEUE);
	}

	/**
	 * Creating the bean for jsonMessageConverter which will be used for
	 * converting Object to Json.
	 * 
	 * @return MessageConverter as a JsonMessageConverter.
	 */
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new JsonMessageConverter();
	}

	/**
	 * Creating Bean for listner container and registering custom java class as
	 * listner.
	 * 
	 * @return SimpleMessageListenerContainer for MQ Listner.
	 */
	@Bean
	public SimpleMessageListenerContainer listenerContainer() {
		SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
		listenerContainer.setConnectionFactory(connectionFactory());
		listenerContainer.setQueues(simpleQueue());
		listenerContainer.setMessageConverter(jsonMessageConverter());
		listenerContainer
				.setMessageListener(new ResetHoldSeatsDaoImpl(templet));
		listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return listenerContainer;
	}
	
	@Bean
	public HoldSeatsDao holdseatdao(){
		System.out.println("Here");
//		return new HoldSeatsDaoImpl();

		return new HoldSeatsDaoImpl2();
		
	}

}
