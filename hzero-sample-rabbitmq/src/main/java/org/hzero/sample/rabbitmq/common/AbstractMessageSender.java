package org.hzero.sample.rabbitmq.common;

import java.util.Objects;
import java.util.Optional;

import org.hzero.core.util.UUIDUtils;
import org.hzero.sample.rabbitmq.config.MsgConstants;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * mq消息发送类。子类修改routingKey和exchangeName即可。
 * 包装用户会话信息到headers
 * </p>
 *
 * @author shengfq 2024-07-14 17:33:26
 */
@Slf4j
public abstract class AbstractMessageSender implements MessageSender {
    @Autowired
    protected RabbitTemplate rabbitTemplate;

    /**
     * 是否延迟发送标识
     */
    protected boolean delay = false;
    protected String routingKey;
    protected String exchangeName;
    /**
     * 消息唯一id
     */
    protected String messageId;
    @Override
    public void send(Object object) {
        send(object, routingKey, exchangeName);
    }
    /**
     * 延迟消息发送
     * */
    @Override
    public void send(Object object, Integer time) {
        send(object, routingKey, exchangeName, time);
    }

    public void send(Object object, String routingKey, String exchangeName) {
        Assert.notNull(routingKey, "routingKey is null");
        Assert.notNull(exchangeName, "exchangeName is null");


        // 准备消息唯一ID
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());

        // 准备消息实体
        String orderJsonStr = JSONUtil.toJsonStr(object);

        /*MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("retry-times", 0);
            return message;
        };*/
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("retry-times", 0);
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            message.getMessageProperties().setCorrelationId(correlationData.getId());
            // 本应用内会需要用户信息 则进行设置
                // 设置用户会话信息进入抬头
                Optional.ofNullable(DetailsHelper.getUserDetails()).ifPresent(userDetails -> {
                    JSONObject userInfo = new JSONObject();
                    userInfo.set("userId", userDetails.getUserId());
                    userInfo.set("tenantId", userDetails.getTenantId());
                    userInfo.set("username", userDetails.getUsername());
                    userInfo.set("realName", userDetails.getRealName());
                    userInfo.set("organizationId", userDetails.getOrganizationId());
                    userInfo.set("language", userDetails.getLanguage());
                    userInfo.set("additionInfo", userDetails.getAdditionInfo());
                    log.trace("交换机：{}路由键：{}->消息头设置用户信息成功：{}", exchangeName, routingKey, userInfo);
                    message.getMessageProperties().setHeader(MsgConstants.USER_DETAILS_KEY, userInfo.toString());
                });
            return message;
        };
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderJsonStr, messagePostProcessor, correlationData);
        log.trace("发送消息成功 exchage:{} routingkey:{} message:{}",exchangeName,routingKey,orderJsonStr);
    }

    /**
     *
     * 发送rabbitMQ消息
     *
     * @param object 消息体
     * @param routingKey 路由键
     * @param exchangeName 交换机名称
     * @param time 过期时间 或 延迟时间 秒
     */
    public void send(Object object, String routingKey, String exchangeName, Integer time) {
        send(null, object, routingKey, exchangeName, time);
    }

    /**
     *
     * 发送rabbitMQ消息
     *
     * @param rabbitTemplate 发送rabbitmq消息模板
     * @param object 消息体
     * @param routingKey 路由键
     * @param exchangeName 交换机名称
     * @param time 过期时间 或 延迟时间 秒
     */
    public void send(RabbitTemplate rabbitTemplate, Object object, String routingKey, String exchangeName,
                     Integer time) {
        Assert.notNull(routingKey, "routingKey is null");
        Assert.notNull(exchangeName, "exchangeName is null");

        // 是否默认消息发送模板
        boolean defaultFlag = Objects.isNull(rabbitTemplate);
        rabbitTemplate = defaultFlag ? this.rabbitTemplate : rabbitTemplate;

        // 准备消息唯一ID
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(StrUtil.isNotBlank(messageId) ? messageId : UUIDUtils.generateUUID());

        // 准备消息实体
        String messageStr = object instanceof String ? (String) object : JSONUtil.toJsonStr(object);

        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("retry-times", 0);
            message.getMessageProperties().setCorrelationId(correlationData.getId());
            // 本应用内会需要用户信息 则进行设置
            if (defaultFlag) {
                // 设置用户会话信息进入抬头
                Optional.ofNullable(DetailsHelper.getUserDetails()).ifPresent(userDetails -> {
                    JSONObject userInfo = new JSONObject();
                    userInfo.set("userId", userDetails.getUserId());
                    userInfo.set("tenantId", userDetails.getTenantId());
                    userInfo.set("username", userDetails.getUsername());
                    userInfo.set("realName", userDetails.getRealName());
                    userInfo.set("organizationId", userDetails.getOrganizationId());
                    userInfo.set("language", userDetails.getLanguage());
                    userInfo.set("additionInfo", userDetails.getAdditionInfo());
                    log.trace("交换机：{}路由键：{}->消息头设置用户信息成功：{}", exchangeName, routingKey, userInfo);
                    message.getMessageProperties().setHeader(MsgConstants.USER_DETAILS_KEY, userInfo.toString());
                });
            }
            return message;
        };
        // 开始发送消息
        rabbitTemplate.convertAndSend(exchangeName, routingKey, messageStr, messagePostProcessor, correlationData);
    }

}
