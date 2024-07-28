package org.hzero.samples.core.context;

import org.slf4j.MDC;

import java.util.concurrent.*;

/**
 * @ClassName ttt
 * @Description TODO
 * @Author Luozelin
 * @Date 2021/5/28 0028 上午 10:53
 * @Version
 */
public class ThreadPoolExecutorMdcWrapper extends ThreadPoolExecutor {


    public ThreadPoolExecutorMdcWrapper(AsyncTaskThreadPoolConfig config, ThreadFactory threadFactory, RejectedExecutionHandler handler ) {
        this(config.getCorePoolSize(), config.getMaxPoolSize(), config.getKeepAliveSecond(), TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(config.getQueueCapacity()),threadFactory,handler);
    }

    public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                        BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                        BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                        RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable task) {
        String sid =   EssContextHolder.getSID();
        String token = EssContextHolder.getToken();
        String unionId = EssContextHolder.getUnionId();
        super.execute(ThreadMdcUtil.wrap(sid,token,unionId,task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        String sid =   EssContextHolder.getSID();
        String token = EssContextHolder.getToken();
        String unionId = EssContextHolder.getUnionId();
        return super.submit(ThreadMdcUtil.wrap(sid,token,unionId,task, MDC.getCopyOfContextMap()), result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        String sid =   EssContextHolder.getSID();
        String token = EssContextHolder.getToken();
        String unionId = EssContextHolder.getUnionId();
        return super.submit(ThreadMdcUtil.wrap(sid,token,unionId,task, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        String sid =   EssContextHolder.getSID();
        String token = EssContextHolder.getToken();
        String unionId = EssContextHolder.getUnionId();
        return super.submit(ThreadMdcUtil.wrap(sid,token,unionId,task, MDC.getCopyOfContextMap()));
    }
}
