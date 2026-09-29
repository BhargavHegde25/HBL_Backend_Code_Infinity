package com.hbl.infinity.accounts.perf;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The single bounded thread pool used by the getList cache, so that a slow or unreachable Fabric cache can never
 * hold a request thread for longer than the configured timeout.
 * <p>
 * The pool is created on first use with {@value GetListPerfConstants#PROP_POOL_SIZE} threads (a change needs a
 * restart). Its threads are daemons. When the pool and its queue are full, work runs on the calling thread instead
 * of failing, which is what the request would have done without the pool.
 */
public final class PerfExecutor {

    private static final int QUEUE_SIZE_PER_THREAD = 4;

    /** Package-private so that tests in this package can install their own. */
    static volatile PerfExecutor instance;

    /** Null for an executor that runs everything on the calling thread (tests only). */
    private final ThreadPoolExecutor pool;

    private PerfExecutor() {
        this.pool = null;
    }

    /**
     * @return an executor that runs every task on the calling thread, without timeouts (tests only)
     */
    static PerfExecutor callerRuns() {
        return new PerfExecutor();
    }

    PerfExecutor(int poolSize) {
        AtomicInteger threadNumber = new AtomicInteger();
        ThreadFactory factory = runnable -> {
            Thread thread = new Thread(runnable, "hbl-getlist-perf-" + threadNumber.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
        this.pool = new ThreadPoolExecutor(poolSize, poolSize, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(poolSize * QUEUE_SIZE_PER_THREAD), factory,
                new ThreadPoolExecutor.AbortPolicy());
        this.pool.allowCoreThreadTimeOut(true);
    }

    /**
     * @return the shared executor, created on first use
     */
    public static PerfExecutor get() {
        PerfExecutor current = instance;
        if (current == null) {
            synchronized (PerfExecutor.class) {
                current = instance;
                if (current == null) {
                    current = new PerfExecutor(GetListPerfConfig.getPoolSize());
                    instance = current;
                }
            }
        }
        return current;
    }

    /**
     * Runs a task and waits at most {@code timeoutMs} for its result. When the pool is saturated the task runs on
     * the calling thread, without a timeout.
     *
     * @param task      work to run
     * @param timeoutMs maximum wait in milliseconds
     * @return the task's result
     * @throws TimeoutException when the task did not finish in time (the task is cancelled)
     * @throws Exception        whatever the task threw
     */
    public <T> T call(Callable<T> task, long timeoutMs) throws Exception {
        if (pool == null) {
            return task.call();
        }
        Future<T> future;
        try {
            future = pool.submit(task);
        } catch (RejectedExecutionException e) {
            return task.call();
        }
        try {
            return future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw e;
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw e;
        } catch (InterruptedException e) {
            future.cancel(true);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    /**
     * Runs a task without waiting for it. When the pool is saturated the task runs on the calling thread.
     * Exceptions thrown by the task are the task's own responsibility.
     *
     * @param task work to run
     */
    public void submit(Runnable task) {
        if (pool == null) {
            task.run();
            return;
        }
        try {
            pool.execute(task);
        } catch (RejectedExecutionException e) {
            task.run();
        }
    }
}
