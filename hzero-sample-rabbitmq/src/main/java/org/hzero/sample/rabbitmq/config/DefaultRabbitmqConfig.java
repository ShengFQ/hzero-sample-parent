package org.hzero.sample.rabbitmq.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
/**
 * rabbitmq amqp-client初始化连接
 * @author sheng
 * @date 2024-07-14
 * */
@Configuration
public class DefaultRabbitmqConfig {

    @Bean(name = MsgConstants.RABBITMQ_CONNECTION_FACTORY)
    @Primary
    public CachingConnectionFactory connectionFactory(RabbitProperties rabbitProperties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        if (StrUtil.isNotBlank(rabbitProperties.getAddresses())) {
            connectionFactory.setAddresses(rabbitProperties.getAddresses());
        } else {
            connectionFactory.setHost(rabbitProperties.getHost());
            connectionFactory.setPort(rabbitProperties.getPort());
        }
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        return connectionFactory;
    }

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(
                    @Qualifier(MsgConstants.RABBITMQ_CONNECTION_FACTORY) ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    @Primary
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
                    @Qualifier(MsgConstants.RABBITMQ_CONNECTION_FACTORY) ConnectionFactory connectionFactory,
                    RabbitProperties rabbitProperties) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        RabbitProperties.SimpleContainer simpleContainer = rabbitProperties.getListener().getSimple();
        factory.setAcknowledgeMode(simpleContainer.getAcknowledgeMode());
        factory.setPrefetchCount(simpleContainer.getPrefetch());
        // 设置重试配置
        RabbitProperties.ListenerRetry retryConfig = simpleContainer.getRetry();
        if (retryConfig.isEnabled()) {
            RetryInterceptorBuilder<?> builder = retryConfig.isStateless() ? RetryInterceptorBuilder.stateless()
                            : RetryInterceptorBuilder.stateful();
            builder.maxAttempts(retryConfig.getMaxAttempts());
            builder.backOffOptions(retryConfig.getInitialInterval().toMillis(), retryConfig.getMultiplier(),
                            retryConfig.getMaxInterval().toMillis());
            builder.recoverer(new RejectAndDontRequeueRecoverer());
            factory.setAdviceChain(builder.build());
        }
        return factory;
    }

    @Bean
    @Primary
    public RabbitAdmin rabbitAdmin(
                    @Qualifier(MsgConstants.RABBITMQ_CONNECTION_FACTORY) ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

}
