package com.temenos.infinity.api.arrangements.javaservice;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

public class saveActionsPermissionsInCacheJavaService implements JavaService2{
	
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	  
	  private static final int CACHE_IN_SECONDS = 1800;
	  
	  public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
	    HashMap<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
	    Result result = new Result();
	    try {
	      String customerId = HelperMethods.getCustomerIdFromSession(request);
	      diagnostic.prepareDebug("*********** customerId  from request **********" + customerId).log();
	      String coreCustomerIdList = (String)inputParams.get("coreCustomerIdList");
	      diagnostic.prepareDebug("*********** coreCustomerIdList  from request **********" + coreCustomerIdList).log();
	      if (!StringUtils.isBlank(coreCustomerIdList)) {
	        Set<String> coreCustomerIdSet = getcoreCustomerIdSet(coreCustomerIdList);
	        diagnostic.prepareDebug("*********** coreCustomerIdSet  from getcoreCustomerIdSet **********" + coreCustomerIdSet).log();
	        JSONArray accountsCacheArray = new JSONArray();
	        if (coreCustomerIdSet != null) {
	          Iterator<String> iterator = coreCustomerIdSet.iterator();
	          while (iterator.hasNext()) {
	            String corecustomers = iterator.next();
	            JSONArray accountsSubSetArray = new JSONArray();
	            String accountsCache = (String)MemoryManager.getFromCache("ACCOUNTS" + customerId + "_" + corecustomers);
	            diagnostic.prepareDebug("accountActions retrieved from cache is :" + accountsCache).log();
	            JSONObject CoreCustomerActions = null;
	            if (StringUtils.isNotBlank(accountsCache))
	              CoreCustomerActions = new JSONObject(accountsCache); 
	            diagnostic.prepareDebug("*********** Key in  saveActionsPermissionsInCacheJavaService  **********ACCOUNTS" + customerId + "_" + corecustomers).log();
	            if (CoreCustomerActions != null && 
	              CoreCustomerActions.has("Accounts")) {
	              accountsSubSetArray = (JSONArray)CoreCustomerActions.get("Accounts");
	              diagnostic.prepareDebug("*********** accountsSubSetArray  **********" + accountsSubSetArray).log();
	              for (Object accountsSubSet : accountsSubSetArray)
	                accountsCacheArray.put(accountsSubSet); 
	            } 
	          }
	          JSONArray newAccountsSubSetArray = new JSONArray();
	          String newAccountsCache = (String)MemoryManager.getFromCache("ACCOUNTS" + customerId +"NewAccounts");
	          diagnostic.prepareDebug("NEWACCOUNTACTIONS retrieved from cache is :" + newAccountsCache).log();
	          JSONObject newAccountActions = null;
	          if (StringUtils.isNotBlank(newAccountsCache)) {
	        	  newAccountActions = new JSONObject(newAccountsCache); 
	          if (newAccountActions != null && 
	        		  newAccountActions.has("Accounts")) {
	        	  newAccountsSubSetArray = (JSONArray)newAccountActions.get("Accounts");
	                  diagnostic.prepareDebug("*********** newAccountsSubSetArray  **********" + newAccountsSubSetArray).log();
	                  for (Object accountsSubSet : newAccountsSubSetArray)
	                    accountsCacheArray.put(accountsSubSet); 
	                } 
	        }
	          
	            JSONObject cacheJsonNew = new JSONObject();
	            cacheJsonNew.put("Accounts", accountsCacheArray);
	            diagnostic.prepareDebug("*********** cacheJsonNew from cacheJsonNew  **********" +cacheJsonNew ).log();
	            MemoryManager.saveIntoCache("ACCOUNTS" + customerId, cacheJsonNew
	                .toString(), 3600);
	            result.addIntParam("AccountsCount", accountsCacheArray.length());
	            diagnostic.prepareDebug("*********** Accounts from Cache Array  **********" + (String)MemoryManager.getFromCache("ACCOUNTS" + customerId)).log();
	            //result.addStringParam("AccountsList",(String)MemoryManager.getFromCache("ACCOUNTS" + customerId));
	            return result;
	          
	        } 
	      } 
	    } catch (Exception e) {
	      alert.prepareError("Exception occured in saveActionsAndPermissions JAVA service. Error: ", e).log();
	      result.addParam("dbpErrCode", "1200");
	      result.addParam("dbpErrMsg", "Internal Service Error");
	      return result;
	    } 
	    return result;

}
	  public Set<String> getcoreCustomerIdSet(String coreCustomerIdList) {
		    Set<String> customerSet = new HashSet<>();
		    try {
		      if (coreCustomerIdList != null) {
		        customerSet = new HashSet<>(Arrays.asList(coreCustomerIdList.split(",")));
		        diagnostic.prepareDebug("core customerSet are.." + customerSet).log();
		      } 
		    } catch (NullPointerException nullPointerException) {}
		    return customerSet;
		  }
}

	