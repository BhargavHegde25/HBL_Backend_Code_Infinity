package com.temenos.dbx.product.utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * Thread executor is used to maintain a common thread pool across Customer 360 app and carry out multi thread
 * operations
 * 
 *
 */

public final class ThreadExecutor {

    private ExecutorService threadPool;
    private int defaultPoolSize = 10;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    private static class SingletonHolder {
        private static final ThreadExecutor INSTANCE = new ThreadExecutor();
    }

    private ThreadExecutor() {
    	String environmentPoolSize = EnvironmentConfigurationsHandler.getValue(
    			URLConstants.DBP_THREAD_POOL_SIZE);
    	if(threadPool == null ) {
    		try {
                defaultPoolSize = StringUtils.isNotBlank(environmentPoolSize) ? 
                		Integer.parseInt(environmentPoolSize) : defaultPoolSize;
            }catch (Exception e) {
            	alert.prepareError("Error while parsing pool size from"
            			+ " runtimetime parameter").log();
            }
    		diagnostic.prepareInfo("ThreadExecutor object is created").log();
    		threadPool =  Executors.newFixedThreadPool(defaultPoolSize,
    			new NamedThreadFactory());
    	}
    }
    
    private class NamedThreadFactory implements ThreadFactory {
		final AtomicLong count = new AtomicLong(0);
        	@Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("DBXCommonPool" + "-" + count.getAndIncrement());
                return thread;
                }
	}


    public static ThreadExecutor getExecutor(DataControllerRequest dcRequest) {
        return getExecutor();
    }
    
    
    public static ThreadExecutor getExecutor(FabricRequestManager requestManager) {
        
        return getExecutor();
        
    }
    
    public static ThreadExecutor getExecutor() {
        return SingletonHolder.INSTANCE;
    }
    
    public void shutdownExecutor() {
        // Shutdown the thread pool when the app gets unpublished from the environment
    	diagnostic.prepareInfo("shutdownExecutor called").log();
        if (threadPool != null) {
            threadPool.shutdown();
            diagnostic.prepareInfo("shutdownExecutor shutdown successfully").log();
        }
    }

    public <T> Future<T> execute(Callable<T> callable) throws InterruptedException {
        if (threadPool != null) {
            return threadPool.submit(callable);
        }
        return null;
    }

    public <T> List<Future<T>> execute(List<Callable<T>> callables) throws InterruptedException {
        if (threadPool != null) {
            return threadPool.invokeAll(callables);
        }
        return null;
    }

//    public static <T> List<T> executeAndWaitforCompletion(List<Callable<T>> callables)
//            throws InterruptedException, ExecutionException {
//        if (threadPool != null) {
//            List<T> t = new ArrayList<T>();
//            List<Future<T>> futures = threadPool.invokeAll(callables);
//            // Wait for all the futures to complete
//            for (Future<T> future : futures) {
//                t.add(future.get());
//            }
//            return t;
//        }
//        return null;
//    }
}
