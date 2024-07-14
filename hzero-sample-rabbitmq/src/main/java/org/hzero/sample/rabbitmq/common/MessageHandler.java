package org.hzero.sample.rabbitmq.common;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.util.Map;

/**
 * ClassName: MessageHandler
 * Description: 消息接收处理接口
 *
 * @author shengfq
 * @date: 2024/7/14 5:08 下午
 */
public interface MessageHandler {
    /**
     * 消息接收处理监听方法
     * */
    void onMessage(Message message, Map<String,Object> headers, Channel channel);
}
