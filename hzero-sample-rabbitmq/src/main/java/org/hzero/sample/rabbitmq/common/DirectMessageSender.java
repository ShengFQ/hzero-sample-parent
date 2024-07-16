package org.hzero.sample.rabbitmq.common;

import org.hzero.sample.rabbitmq.config.MsgConstants;
import org.hzero.sample.rabbitmq.common.AbstractMessageSender;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ClassName: DirectMessageSender
 * Description: 消息发送者
 *
 * @author shengfq
 * @date: 2024/7/14 5:38 下午
 */
@Component
public class DirectMessageSender extends AbstractMessageSender {

    @PostConstruct
    public void init(){
        routingKey= MsgConstants.DIRECT_ROUTING_KEY_EXAMPLE;
        exchangeName= MsgConstants.EXCHANGE_NAME_EXAMPLE;
    }
}
