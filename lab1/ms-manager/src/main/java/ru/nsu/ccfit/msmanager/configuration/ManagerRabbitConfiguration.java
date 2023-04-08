package ru.nsu.ccfit.msmanager.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class ManagerRabbitConfiguration {
    private static final String TASK_QUEUE_NAME = "task_queue";
    private static final String TASK_STATUS_QUEUE_NAME = "task_status_queue";
    @Bean
    Queue taskQueue() {
        return QueueBuilder.durable(TASK_QUEUE_NAME)
                .deadLetterExchange(DirectExchange.DEFAULT.getName())
                .build();
    }

    @Bean
    Queue taskStatusQueue() {
        return QueueBuilder.durable(TASK_STATUS_QUEUE_NAME)
                .deadLetterExchange(DirectExchange.DEFAULT.getName())
                .build();
    }

    @Bean
    Binding taskQueueBinding() {
        return BindingBuilder.bind(taskQueue()).to(DirectExchange.DEFAULT).with(TASK_QUEUE_NAME);
    }

    @Bean
    Binding taskStatusQueueBinding() {
        return BindingBuilder.bind(taskStatusQueue()).to(DirectExchange.DEFAULT).with(TASK_STATUS_QUEUE_NAME);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localRabbitMQ");
        cachingConnectionFactory.setUsername("user");
        cachingConnectionFactory.setPassword("password");
        cachingConnectionFactory.setVirtualHost("tvh");
        return cachingConnectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2XmlMessageConverter());
        return factory;
    }

    @Bean
    public RabbitTemplate configureRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2XmlMessageConverter());
        return rabbitTemplate;
    }
}
