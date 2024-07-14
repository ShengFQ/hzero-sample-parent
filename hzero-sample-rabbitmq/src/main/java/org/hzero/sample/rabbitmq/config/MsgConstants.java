package org.hzero.sample.rabbitmq.config;

import org.hzero.core.base.BaseConstants;

/**
 * ClassName: CoreConstants
 * Description: 公共的常量
 *
 * @author shengfq
 * @date: 2024/7/14 5:11 下午
 */
public class MsgConstants implements BaseConstants {
    public static final String USER_DETAILS_KEY="userDetails";
    public static final String DEFAULT="default";
    public static final String RABBITMQ_CONNECTION_FACTORY="connectionFactory";
    public static final String DIRECT_ROUTING_KEY_EXAMPLE="rk.sample.example";
    public static final String EXCHANGE_NAME_EXAMPLE="ex.sample.example";
    public static final String QUEUE_NAME_EXAMPLE="queue.sample.example";
}
