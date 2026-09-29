package com.kony.dbputilities.util;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.Property;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.kony.dbputilities.util.BundleConfigurationHandler;




public class TransactionsCountProperties {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static Properties PROPS = null;
    
    public static final String ACCOUNT_TYPE_BUNDLE_NAME = "DBP_CONFIG_BUNDLE";
    public static final String POSTED_TRANSACTIONS_LIMIT = "POSTED_TRANSACTIONS_LIMIT";
    public static final String PENDING_TRANSACTIONS_LIMIT = "PENDING_TRANSACTIONS_LIMIT";
    public static final String CONFIGURATIONS = "configurations";
    public static final String TRANSACTIONS_PER_PAGE = "TRANSACTIONS_PER_PAGE";


    @SuppressWarnings("unused")
    private TransactionsCountProperties() {
        // Private Constructor
    }

    public TransactionsCountProperties(DataControllerRequest request, String limitType) {
        PROPS = loadProps(request,limitType);
    }
    
    

    @SuppressWarnings("unchecked")
    private static Properties loadProps(DataControllerRequest request, String limitType) {
        Properties properties = new Properties();

        String CACHE_KEY_PERPAGE_TRANSACTIONCOUNT_MAPPING = "PerPageTransactionsCountProperties";
        String CACHE_KEY_POSTED_TRANSACTIONCOUNT_MAPPING = "PostedTransactionsCountProperties";
        String CACHE_KEY_PEDNING_TRANSACTIONCOUNT_MAPPING = "PendingTransactionsCountProperties";
        final int CACHE_TIME = 1 * 60; // 1 minutes
        Map<String, String> transactionsCountProperties = new HashMap<>();

        try {
        	Object object = new Object();
        
        	if(limitType.equals("Posted")) {
        		
             object = MemoryManager.getDataFromCache(request,CACHE_KEY_POSTED_TRANSACTIONCOUNT_MAPPING);
              if(object == null) {
            	  JSONObject postedCount = BundleConfigurationHandler.getBundleConfigurations(
                          ACCOUNT_TYPE_BUNDLE_NAME, null, POSTED_TRANSACTIONS_LIMIT, null,request);
                  JSONObject postedConfigData = new JSONObject();
                  if (postedCount != null) {
                      JSONArray postedConfigurations = postedCount.optJSONArray(CONFIGURATIONS);
                      if (postedConfigurations != null && postedConfigurations.length() > 0) {
                      	postedConfigData = postedConfigurations.optJSONObject(0);
                         }
                  }
                  Map<String, String> postedTransactionsCountProperties = new HashMap<>();
                  if (postedConfigData!=null) {
                      try {
                          properties = Property.toProperties(postedConfigData);
                          postedTransactionsCountProperties = new ObjectMapper().readValue(postedConfigData.toString(), HashMap.class);
                      } catch (Exception e) {
                          alert.prepareError("Cannot convert string to properties" + e).log();
                      }
                  }
                  MemoryManager.insertDataIntoCache(request,postedTransactionsCountProperties,CACHE_KEY_POSTED_TRANSACTIONCOUNT_MAPPING,CACHE_TIME);
              }             
        	}
        	
        	else if(limitType.equals("Pending")) {
        		
        		object = MemoryManager.getDataFromCache(request,CACHE_KEY_PEDNING_TRANSACTIONCOUNT_MAPPING);
        		 if(object == null) {
                     JSONObject pendingCount = BundleConfigurationHandler.getBundleConfigurations(
                             ACCOUNT_TYPE_BUNDLE_NAME, PENDING_TRANSACTIONS_LIMIT, null,null, request);
                     
                     
                     JSONObject pendingConfigData = new JSONObject();
                     if (pendingCount != null) {
                         JSONArray pendingConfigurations = pendingCount.optJSONArray(CONFIGURATIONS);
                         if (pendingConfigurations != null && pendingConfigurations.length() > 0) {
                         	pendingConfigData = pendingConfigurations.optJSONObject(0);
                            }
                     }
                     Map<String, String> pendingTransactionsCountProperties = new HashMap<>();
                     if (pendingConfigData!=null) {
                         try {
                             properties = Property.toProperties(pendingConfigData);
                             pendingTransactionsCountProperties = new ObjectMapper().readValue(pendingConfigData.toString(), HashMap.class);
                         } catch (Exception e) {
                             alert.prepareError("Cannot convert string to properties" + e).log();
                         }
                     }
                     
                     MemoryManager.insertDataIntoCache(request,pendingTransactionsCountProperties, CACHE_KEY_PEDNING_TRANSACTIONCOUNT_MAPPING,CACHE_TIME );
                      } 
        	}
        	
        	else if(limitType.equals("perPage")) {
        		
        		object = MemoryManager.getDataFromCache(request,CACHE_KEY_PERPAGE_TRANSACTIONCOUNT_MAPPING);
        		if(object == null) {
        			JSONObject perPageCount = BundleConfigurationHandler.getBundleConfigurations(
                            ACCOUNT_TYPE_BUNDLE_NAME, null, null,TRANSACTIONS_PER_PAGE, request);
                    JSONObject perPageConfigData = new JSONObject();
                    if (perPageCount != null) {
                        JSONArray perPageConfigurations = perPageCount.optJSONArray(CONFIGURATIONS);
                        if (perPageConfigurations != null && perPageConfigurations.length() > 0) {
                        	perPageConfigData = perPageConfigurations.optJSONObject(0);
                           }
                    }
                    Map<String, String> perPageTransactionsCountProperties = new HashMap<>();
                    if (perPageConfigData!=null) {
                        try {
                            properties = Property.toProperties(perPageConfigData);
                            perPageTransactionsCountProperties = new ObjectMapper().readValue(perPageConfigData.toString(), HashMap.class);
                        } catch (Exception e) {
                            alert.prepareError("Cannot convert string to properties" + e).log();
                        }
                    }
                    MemoryManager.insertDataIntoCache(request,perPageTransactionsCountProperties,CACHE_KEY_PERPAGE_TRANSACTIONCOUNT_MAPPING, CACHE_TIME);
        		}
        	}
        	
            if (null != object) {
                if (StringUtils.isNotBlank(object.toString())) {
               
                    JSONObject txnTypes = new JSONObject(object.toString());
                    if (txnTypes.length() != 0) {
                    	transactionsCountProperties = new Gson().fromJson(txnTypes.toString(), Map.class);
                    }
                }
                if (!transactionsCountProperties.isEmpty()) {
                    properties.putAll(transactionsCountProperties);
                    return properties;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unable to fetch account types from bundle configurations").log();
        }

        return properties;
    }
    
    
   
    

    /**
     * Returns query associated with this key
     * 
     * @param key
     * @return
     */
    public static String getValue(String propertyKey) {
        return PROPS.getProperty(propertyKey);
    }

}
