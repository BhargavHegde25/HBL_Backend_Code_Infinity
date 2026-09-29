package com.kony.campaign.engine;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.util.CampaignUtil;

public final class CampaignThreadPoolExecutor {
	
	  private static final String CAMPAIGN_THREAD_POOL_NAME = "CampaignThreadPool-%d";
	  private static final ExecutorService CAMPAIGN_THREAD_POOL = createExecutor();
	  private static final int DEFAULT_POOL_SIZE = 5;  

	  private static ExecutorService createExecutor()
	  {
	    String campaignPoolSize = CampaignUtil.getServerProperty(CampaignConstants.CAMPAIGN_THREAD_POOL_SIZE,
	    		String.valueOf(DEFAULT_POOL_SIZE));
	    ThreadFactoryBuilder tfbuilder = new ThreadFactoryBuilder().setNameFormat(CAMPAIGN_THREAD_POOL_NAME);
	    return Executors.newFixedThreadPool(Integer.parseInt(campaignPoolSize),tfbuilder.build());	   
	  }
	  
	  public static <T> Future<T> execute(Callable<T> callable)
	  {
	    return CAMPAIGN_THREAD_POOL.submit(callable);
	  }
	  
	  public static void shutDown() {
		  CAMPAIGN_THREAD_POOL.shutdown();
	  }
	
	  private CampaignThreadPoolExecutor() { } 


}
