package org.hzero.samples.core.context;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @ClassName ThreadMdcUtil
 * @Description TODO
 * @Author Luozelin
 * @Date 2021/5/28 0028 上午 10:54
 * @Version
 */
public class ThreadMdcUtil {

    public static <T> Callable<T> wrap(final String sid,final String token,final String unionId,final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            EssContextHolder.setSID(sid);
            EssContextHolder.setToken(token);
            EssContextHolder.setUnionId(unionId);
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final String sid,final String token,final String unionId,final Runnable runnable, final Map<String, String> context) {
        return () -> {
            EssContextHolder.setSID(sid);
            EssContextHolder.setToken(token);
            EssContextHolder.setUnionId(unionId);
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
