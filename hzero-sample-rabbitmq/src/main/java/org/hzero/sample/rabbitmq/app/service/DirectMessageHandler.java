package org.hzero.sample.rabbitmq.app.service;

import com.rabbitmq.client.Channel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hzero.sample.rabbitmq.common.AbstractMessageHandler;
import org.hzero.sample.rabbitmq.config.MsgConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ClassName: DirectMessageHandler
 * Description: 消息监听器
 *
 * @author shengfq
 * @date: 2024/7/14 6:06 下午
 */
@Slf4j
@Component
public class DirectMessageHandler extends AbstractMessageHandler {

    /**
     * 消息接收处理监听方法
     *
     * @param message
     * @param headers
     * @param channel
     */
    @Override
    @RabbitListener(queues = MsgConstants.QUEUE_NAME_EXAMPLE)//监听的队列名称 TestDirectQueue
    @RabbitHandler
    public void onMessage(Message message, Map<String, Object> headers, Channel channel) {
        log.debug("DirectReceiver消费者收到消息  : " + message.toString());
        handleWithUser(message,headers,channel,(s)->{onBusiness(message);});
    }

    private void onBusiness(Message message){
        log.debug("业务逻辑处理  : " + message.toString());
    }
}
