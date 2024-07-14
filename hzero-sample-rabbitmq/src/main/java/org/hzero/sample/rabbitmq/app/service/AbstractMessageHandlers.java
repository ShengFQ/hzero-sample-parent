package org.hzero.sample.rabbitmq.app.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

import org.hzero.core.exception.OptimisticLockException;
import org.hzero.core.message.MessageAccessor;
import org.springframework.amqp.core.Message;

import com.rabbitmq.client.Channel;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;

import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 消息处理类
 * </p>
 *
 * @author gw_zhoujp@dealersany.com.cn 2021-12-22 20:35
 */
@Slf4j
public abstract class AbstractMessageHandlers implements MessageHandler {
    private AbstractMessageHandlers() {}

    public static void handleMessage(Message msg, Map<String, Object> headers, Channel channel, Consumer<String> fn) {
        String consumerQueue = msg.getMessageProperties().getConsumerQueue();
        try {
            log.trace("消息队列[{}]处理, headers={}", consumerQueue, headers);
            fn.accept(new String(msg.getBody(), StandardCharsets.UTF_8));
        } catch (CommonException e) {
            log.error("消息队列[{}], 消息处理失败业务异常->{}", consumerQueue,
                            MessageAccessor.getMessage(e.getCode(), e.getParameters(), e.getMessage()).desc());
        } catch (IllegalArgumentException e) {
            log.error("消息队列[{}], 消息处理失败业务异常: {}", consumerQueue, e.getMessage());
        } catch (OptimisticLockException e) {
            // 如果乐观锁异常 则触发重试
            log.warn("消息队列[{}], 数据处理乐观锁异常触发重试", consumerQueue);
            throw e;
        } catch (Exception e) {
            log.error("消息队列[{}], 消息处理失败系统异常", consumerQueue, e);
        }
    }

    /**
     * 携带用户会话信息 -> 消息头获取用户会话信息
     *
     * 经AbstractMessageSender 发送MQ消息会在消息头附加用户会话信息
     *
     * JSON示例如下
     *
     * { "language":"zh_CN","userId":47,
     * "organizationId":3,"realName":"erp接口测试用户","tenantId":3,"username":"erp-int"
     * "additionInfo":{"facility_name":"泵送制造工厂","facility_no":"5802","facility_id":251391029288880018} }
     *
     * @param msg
     * @param headers
     * @param channel
     * @param fn
     */
    public static void handleWithUser(Message msg, Map<String, Object> headers, Channel channel, Consumer<String> fn) {
        try {
            // 生产执行发出的mq消息头带有用户会话信息 切勿用于其他系统、模块的mq消息处理
            Object userDetails = headers.get(MeBaseConstants.USER_DETAILS_KEY);
            if (ObjectUtil.isNull(userDetails)) {
                throw new CommonException("MQ消息头获取用户会话信息失败！！！");
            }
            JSONObject jsonObject = JSONUtil.parseObj(userDetails);
            log.trace("设置用户会话信息，传入JSONObject信息: {}", jsonObject);

            CustomUserDetails details = new CustomUserDetails(jsonObject.getStr("username"), MeBaseConstants.DEFAULT);
            details.setTenantId(jsonObject.getLong("tenantId"));
            details.setUserId(jsonObject.getLong("userId"));
            details.setOrganizationId(jsonObject.getLong("organizationId"));
            details.setRealName(jsonObject.getStr("realName"));
            details.setLanguage(jsonObject.getStr("language"));
            details.setAdditionInfo(jsonObject.getJSONObject("additionInfo"));
            DetailsHelper.setCustomUserDetails(details);
            // 设置用户会话异常
        } catch (Exception e) {
            log.error("消息队列[{}], 消息处理失败", msg.getMessageProperties().getConsumerQueue(), e);
            return;
        }
        handleMessage(msg, headers, channel, fn);
    }

    public static void handleWithNack(Message msg, Map<String, Object> headers, Channel channel, Consumer<String> fn) {
        String consumerQueue = msg.getMessageProperties().getConsumerQueue();
        try {
            log.trace("消息队列[{}]处理, headers={}", consumerQueue, headers);
            fn.accept(new String(msg.getBody(), StandardCharsets.UTF_8));
            ack(msg, channel);
        } catch (CommonException e) {
            log.error("消息队列[{}], 消息处理失败业务异常->{}", consumerQueue,
                            MessageAccessor.getMessage(e.getCode(), e.getParameters(), e.getMessage()).desc());
            nack(msg, channel);
        } catch (IllegalArgumentException e) {
            log.error("消息队列[{}], 消息处理失败业务异常: {}", consumerQueue, e.getMessage());
            nack(msg, channel);
        } catch (Exception e) {
            log.error("消息队列[{}], 消息处理失败系统异常：{}", consumerQueue, e);
            nack(msg, channel);
        }
    }

    public static void handleWithUserThrowEx(Message msg, Map<String, Object> headers, Channel channel,
                    Consumer<String> fn) {
        String consumerQueue = msg.getMessageProperties().getConsumerQueue();

        try {
            // 生产执行发出的mq消息头带有用户会话信息 切勿用于其他系统、模块的mq消息处理
            Object userDetails = headers.get(MeBaseConstants.USER_DETAILS_KEY);
            if (ObjectUtil.isNull(userDetails)) {
                throw new CommonException("MQ消息头获取用户会话信息失败！！！");
            }
            JSONObject jsonObject = JSONUtil.parseObj(userDetails);
            log.trace("设置用户会话信息，传入JSONObject信息: {}", jsonObject);

            CustomUserDetails details = new CustomUserDetails(jsonObject.getStr("username"), MeBaseConstants.DEFAULT);
            details.setTenantId(jsonObject.getLong("tenantId"));
            details.setUserId(jsonObject.getLong("userId"));
            details.setOrganizationId(jsonObject.getLong("organizationId"));
            details.setRealName(jsonObject.getStr("realName"));
            details.setLanguage(jsonObject.getStr("language"));
            details.setAdditionInfo(jsonObject.getJSONObject("additionInfo"));
            DetailsHelper.setCustomUserDetails(details);

            log.trace("消息队列[{}]处理, headers={}", consumerQueue, headers);
            fn.accept(new String(msg.getBody(), StandardCharsets.UTF_8));
        } catch (CommonException e) {
            log.error("消息队列[{}], 消息处理失败业务异常->{}, 详细信息：", consumerQueue,
                            MessageAccessor.getMessage(e.getCode(), e.getParameters(), e.getMessage()).desc(), e);
            throw e;
        } catch (Exception e) {
            log.error("消息队列[{}], 消息处理失败系统异常：{}", consumerQueue, e);
            throw e;
        }
    }

    private static void ack(Message msg, Channel channel) {
        try {
            long deliverTag = msg.getMessageProperties().getDeliveryTag();
            channel.basicAck(deliverTag, false);
        } catch (IOException ioe) {
            log.error("消息处理ack异常{}", ioe.getMessage(), ioe);
        }
    }

    private static void nack(Message msg, Channel channel) {
        try {
            long deliverTag = msg.getMessageProperties().getDeliveryTag();
            channel.basicNack(deliverTag, false, true);
        } catch (IOException ioe) {
            log.error("消息处理nack异常{}", ioe.getMessage(), ioe);
        }
    }

}
