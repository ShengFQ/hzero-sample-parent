package org.hzero.samples.core.context;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

/**
 * @ClassName ExecuteTaskUtils
 * @Description 执行异步线程工具类
 * @Author Luozelin
 * @Date 2021/5/28 0028 上午 8:23
 * @Version
 */
@Slf4j
public class ExecuteTaskUtils {

    private static ExecuteTaskService executeTaskService;

    /**
     * 获取单例对象
     *
     * @return
     */
    private static synchronized ExecuteTaskService getInstance() {
        if (executeTaskService == null) {
            executeTaskService = new ExecuteTaskService();
        }
        return executeTaskService;
    }


    /**
     * 提交异步任务
     *
     * @return
     */
    public static void submitTask(ITask task) {
        submitTask(task, null);
    }

    /**
     * 提交异步任务，执行结束后回调方法.
     *
     * @param task
     * @param callback
     */
    public static void submitTask(ITask task, ICallback callback) {
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();
        String sid = EssContextHolder.getSID();
        String token = EssContextHolder.getToken();
        String unionId = EssContextHolder.getUnionId();
        getInstance().submitTask(() -> {
            try {
                // 设置
                if (mdcMap != null) {
                    MDC.setContextMap(mdcMap);
                }
                EssContextHolder.setSID(sid);
                EssContextHolder.setToken(token);
                EssContextHolder.setUnionId(unionId);
                task.executeTask();
                if (callback != null) {
                    callback.callback(true);
                }
            } catch (Exception e) {
                log.error("执行异步任务异常:", e);
                if (callback != null) {
                    callback.callback(false);
                }
            }
        });
    }
}
