package org.hzero.sample.rabbitmq.domain;

import lombok.Data;

/**
 * ClassName: DirectMessage
 * Description: 消息对象
 *
 * @author shengfq
 * @date: 2024/7/20 10:22 下午
 */
@Data
public class DirectMessage {

    private String messageId;

    private String messageData;

    private String createTime;
}
