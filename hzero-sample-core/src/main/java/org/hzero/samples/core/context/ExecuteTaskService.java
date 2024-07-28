package org.hzero.samples.core.context;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
class ExecuteTaskService {


    protected static ThreadPoolExecutor pools = null;

    /**
     * 提交任务请求
     * @param task
     */
    public void submitTask(Runnable task) {
        pools.execute(task);
    }


    /**
     * 停止线程池
     */
    public void shutdown() {
        pools.shutdown();
    }


    /**
     * 初始化线下池
     */
    public ExecuteTaskService(){
         initPool(6,10);
    }

    /**
     * 初始化线下池
     */
    public ExecuteTaskService(int corePoolSize, int maxPoolSize){
        initPool(corePoolSize,maxPoolSize);
    }


    private void initPool(int corePoolSize,int maxPoolSize){
        ThreadFactory guavaThreadFactory = new ThreadFactoryBuilder().setNameFormat("ess-task-pool-%d").build();
        pools = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 10L,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10000),guavaThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

    }
}
