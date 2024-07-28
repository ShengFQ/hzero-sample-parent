package org.hzero.sample.rabbitmq.api;

import org.hzero.core.base.BaseConstants;
import org.hzero.sample.rabbitmq.common.DirectMessageSender;
import org.hzero.sample.rabbitmq.domain.DirectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author : shengfq
 * @CreateTime : 2024-07-14
 * @Description : 发送消息
 **/

@RestController("sendMessageController.v2")
@RequestMapping("/v2")
@SuppressWarnings("all")
public class SendMessageController {

    @Autowired
    DirectMessageSender directMessageSender;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DirectMessage message=new DirectMessage();
        message.setMessageId(messageId);
        message.setMessageData(messageData);
        message.setCreateTime(createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        directMessageSender.send(message);
        return "ok";
    }


}
