package org.hzero.sample.rabbitmq.common;

import cn.hutool.core.util.ObjectUtil;

import java.util.List;

/*
 *消息发送者抽象接口定义本地发送API方法
   *@author shengfq
 * @date 2024-07-14
  *
 */
public interface MessageSender {
    /**
     *
     *
     * @param object
     */
    void send(Object object);

    /**
     *
     *
     * @param object
     * @param time 秒
     */
    void send(Object object, Integer time);

    /**
     * \
     *
     * @param list
     */
    default void sendList(List<?> list) {
        if (ObjectUtil.isNotNull(list)) {
            list.forEach(this::send);
        }
    }
}
