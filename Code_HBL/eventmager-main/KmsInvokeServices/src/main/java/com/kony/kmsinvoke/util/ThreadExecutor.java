package com.kony.kmsinvoke.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public final class ThreadExecutor {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	private static ExecutorService threadpool = null;

	private ThreadExecutor() {
	}

	public static synchronized void createExecutor(int size) {
		if (threadpool == null || threadpool.isShutdown()) {
			ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
					.setNameFormat("KMSExecutorService-%d").build();
			threadpool = Executors.newFixedThreadPool(size, namedThreadFactory);
		}
	}

	public static void shutdownExecutor() {
		if (threadpool != null && !threadpool.isShutdown())
			threadpool.shutdown();
		try {
			if (threadpool != null && threadpool.awaitTermination(5, TimeUnit.SECONDS)) {
				diagnostic.prepareDebug("KMSExecutorService shutdown").log();
			} else {
				if (threadpool != null)
					threadpool.shutdownNow();
			}
			diagnostic.prepareDebug("KMSExecutorService shutdown").log();
		} catch (Exception ex) {
			diagnostic.prepareDebug("Error in shutting down KMSExecutorService", ex).log();
		}

	}

	public static void execute(Runnable r) {
		threadpool.execute(r);
	}

	public static ExecutorService getExecutor() {
		return threadpool;
	}

}