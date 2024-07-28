package org.hzero.samples.core.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.task.pool")
public class AsyncTaskThreadPoolConfig {
	// 核心线程数量
	private int corePoolSize = 5;
	// 最大线程数量
	private int maxPoolSize = 50;
	// 线程池维护线程数所允许的空闲时间
	private int keepAliveSecond = 60;
	// 队列的长度
	private int queueCapacity =10000;

	// 线程队列名前缀
	private String threadNamePrefix = "ESS-AsyncTask-";

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveSecond() {
		return keepAliveSecond;
	}

	public void setKeepAliveSecond(int keepAliveSecond) {
		this.keepAliveSecond = keepAliveSecond;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public String getThreadNamePrefix() {
		return threadNamePrefix;
	}

	public void setThreadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix;
	}
}
