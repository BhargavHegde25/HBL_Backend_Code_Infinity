package com.temenos.infinity.api.holdings.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.holdings.config.HoldingsAPIServices;
import com.temenos.infinity.api.holdings.config.ServerConfigurations;
import com.temenos.infinity.api.holdings.constants.ErrorCodeEnum;
import com.temenos.infinity.api.holdings.resource.api.AccountTransactionsResource;
import com.temenos.infinity.api.holdings.util.HoldingsUtils;
import com.temenos.infinity.api.holdings.util.TransactionTypeProperties;
import org.json.JSONObject;

public class GetSearchTransactionsOperation implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		//Load Transaction Types
        TransactionTypeProperties props = new TransactionTypeProperties(request);
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        HashMap<String, Object> inputParams1 = new HashMap<String, Object>();
		try {
			// Initializing of AccountTransactions through Abstract factory method

			AccountTransactionsResource AccountTransactionsResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(AccountTransactionsResource.class);
			diagnostic.debug("searchAccountTransactions1 ###");
			Map<String, String> inputParamMap = new HashMap<>();
			inputParamMap.put("customerId",HoldingsUtils.getUserAttributeFromIdentity(request, "customer_id"));
			String authToken = TokenUtils.getHoldingsMSAuthToken(inputParamMap);
			diagnostic.debug("searchAccountTransactions authToken ###"+ authToken);
			HoldingsUtils.setCompanyIdToRequest(request);
			diagnostic.debug("transactionType ###"+ inputParams.get("transactionType"));
			String transactionType = inputParams.get("transactionType");
			diagnostic.debug(" requestType ###"+ inputParams.get("requestType"));
			String requestType = inputParams.get("requestType");
			
			//HBL Changes
			String dateFrom = inputParams.get("searchStartDate");
			String dateTo = inputParams.get("searchEndDate");
			diagnostic.debug(" GetSearchTransactionsOperation:dateFrom *********** "+dateFrom );
			diagnostic.debug(" GetSearchTransactionsOperation:dateTo *********** "+dateTo );
			if("Blocked".equalsIgnoreCase(transactionType)) {
				return searchBlockedFunds(request,inputParams);
			}
			diagnostic.debug(" searchBlockedFunds ###" );
			
			
			String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
			diagnostic.debug(" ARRANGEMENTS_BACKEND ###"+ ARRANGEMENTS_BACKEND );
			if (StringUtils.isNotBlank(ARRANGEMENTS_BACKEND)) {
				for ( Map.Entry<String, String> entry : inputParams.entrySet()) {
				    String key = entry.getKey();
				    String value = entry.getValue();
				    inputParams1.put(key, value);    
				}
				if (ARRANGEMENTS_BACKEND.equals("t24")) {				
	        		if (!requestType.equals("download")) {
							String transactions = DBPServiceExecutorBuilder.builder()
			                        .withServiceId("ArrangementsT24ISTransactions")
			                        .withOperationId("searchTransactions")
			                        .withRequestParameters(inputParams1).withRequestHeaders(request.getHeaderMap())
			                        .withDataControllerRequest(request).build().getResponse();
							return JSONToResult.convert(transactions);
	        		}
//        		else {
//        			String transactions = DBPServiceExecutorBuilder.builder()
//                            .withServiceId("T24TransactionsHBL")
//                            .withOperationId("searchTransactionsTimed")
//                            .withRequestParameters(inputParams1).withRequestHeaders(request.getHeaderMap())
//                            .withDataControllerRequest(request).build().getResponse();
//        			diagnostic.prepareDebug("searchTransactionsTimed ::"+transactions).log();
//    				return JSONToResult.convert(transactions);	
//        		}
	        		// HBL Changes for Account Statement
	        		else {
	        			inputParams1.put("searchStartDate", dateFrom);
	        			inputParams1.put("searchEndDate", dateTo);
	        			String transactions = DBPServiceExecutorBuilder.builder()
	                            .withServiceId("ArrangementsT24ISTransactions")
	                            .withOperationId("searchTransactionsTimedHBL")
	                            .withRequestParameters(inputParams1).withRequestHeaders(request.getHeaderMap())
	                            .withDataControllerRequest(request).build().getResponse();
	        			diagnostic.prepareDebug("GetSearchTransactionsOperation:getCompletedTransactions ::"+transactions).log();
	    				return JSONToResult.convert(transactions);	
	        		}
			}
			
			else if (ARRANGEMENTS_BACKEND.equals("MOCK")) {
	        	Map<String, Object> inputMap = new HashMap<>();
	            Map<String, Object> headerMap = new HashMap<>();
	     
	            String transactionResponse = Executor.invokePassThroughServiceAndGetString((InfinityServices)
	            		HoldingsAPIServices.DOWNLOAD_STATEMENTS_MOCK, inputMap, headerMap);
				return JSONToResult.convert(transactionResponse);
	            
	    	}
		}
			diagnostic.debug("Before searchAccountTransactions ###");
			 result = AccountTransactionsResource.searchAccountTransactions((Map<String, Object>) inputArray[1], request,authToken);
			 diagnostic.debug("result ###"+ ResultToJSON.convert(result));
		} catch (Exception exception) {
			diagnostic.debug("Before exception ###");
			alert.prepareError("::"+exception.getMessage()).log();
			if (exception.getMessage().contains("searchTransactionsTimed")) {
				String eventTriggered = DBPServiceExecutorBuilder.builder()
                        .withServiceId("DownloadTransactionJavaService")
                        .withOperationId("generateAdhocStatementFile")
                        .withRequestParameters(inputParams1).withRequestHeaders(request.getHeaderMap())
                        .withDataControllerRequest(request).build().getResponse();
    			JSONObject event = new JSONObject(eventTriggered);
    			if (event.has("opstatus") && event.getInt("opstatus")==0) {
    				//event.append("Transactions", "Adhoc Statement");
    				event.put("MoreRecordsflag", "Adhoc Statement");
    				return JSONToResult.convert(event.toString());	
    			}
			}
			
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}
		return result;
	}
	
	private Result searchBlockedFunds(DataControllerRequest request,Map<String, String> inputParams ) {
		Result result = new Result();
		try {
			
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
    		//HashMap<String, Object> inputParams = new HashMap<String, Object>();
    		HashMap<String, Object> inputParams1 = new HashMap<String, Object>();
			for ( Map.Entry<String, String> entry : inputParams.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    inputParams1.put(key, value);    
			}
    		//inputParams.put("accountID", request.getParameter("accountID"));
			 String transactionResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId("ArrangementsT24Services")
                    .withOperationId("getBlockedFunds")
                    .withRequestParameters(inputParams1).withRequestHeaders(headerParams)
                    .withDataControllerRequest(request).build().getResponse();
			 result = JSONToResult.convert(transactionResponse);
            
        } catch (Exception e) {
           // alert.prepareError("Unable to fetch Arrangements " + e).log();
            //throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
		return result;
	}
}
