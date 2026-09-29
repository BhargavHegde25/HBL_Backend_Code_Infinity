package com.temenos.infinity.api.holdings.javaservice;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.holdings.config.HoldingsAPIServices;
import com.temenos.infinity.api.holdings.config.ServerConfigurations;
import com.temenos.infinity.api.holdings.constants.ErrorCodeEnum;
import com.temenos.infinity.api.holdings.constants.OperationName;
import com.temenos.infinity.api.holdings.resource.api.AccountTransactionsResource;
import com.temenos.infinity.api.holdings.util.HoldingsUtils;
import com.temenos.infinity.api.holdings.util.TransactionTypeProperties;

/**
 * 
 * @author KH2281
 * @version 1.0 Java Service end point to fetch all the transactions of a particular account
 */

public class GetAccountPendingAndPostedTransactionsOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
	@Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
	            // Initializing of AccountTransactions through Abstract factory method
	            AccountTransactionsResource AccountTransactionsResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(AccountTransactionsResource.class);

            	String searchStartDate=request.getParameter("searchStartDate");
        		String order=request.getParameter("order");
        		String offset=request.getParameter("offset");
        		String limit=request.getParameter("limit");
        		String accountID=request.getParameter("accountID");
        		String transactionType=request.getParameter("transactionType");
        		String sortBy=request.getParameter("sortBy");
        		Map<String, String> inputParamMap = new HashMap<>();
    			inputParamMap.put("customerId",HoldingsUtils.getUserAttributeFromIdentity(request, "customer_id"));
    			String authToken = TokenUtils.getHoldingsMSAuthToken(inputParamMap);
        		HoldingsUtils.setCompanyIdToRequest(request);
        		if(accountID==null) {
        			return ErrorCodeEnum.ERR_20042.setErrorCode(new Result());
        		}
        		String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
        		if (ARRANGEMENTS_BACKEND!= null) {
        		if (ARRANGEMENTS_BACKEND.equals("t24")) {
        			Dataset Transactions = new Dataset("accountransactionview");
        			String isScheduled = request.getParameter("isScheduled");
        			if (StringUtils.isNotBlank(isScheduled) && "true".equalsIgnoreCase(isScheduled)) {
        				result = (Result) CommonUtils.callIntegrationService(request, null, null, OperationName.SERVICE_ID_TRANSORCH,
        						OperationName.OPER_ID_SCHEDULED, true);
        				Dataset ds =result.getDatasetById("Transactions");
        				Transactions.addAllRecords(ds.getAllRecords());
        				result.addDataset(Transactions);
        				result.removeDatasetById("Transactions");
        			}
        			
        			else if (StringUtils.isNotBlank(transactionType)) {
        				if (transactionType.equalsIgnoreCase("loanschedule")) {
        					Map<String,Object> inputparam=new HashMap<String, Object>();
        					if(inputArray.length >1) {
        						inputparam=(Map<String,Object>) inputArray[1];
        					}
        					
        					String isFutureRequired  =request.getParameter("isFutureRequired");
                			if(StringUtils.isBlank(isFutureRequired)) {
                				isFutureRequired="false";
                			}
         					request.addRequestParam_("isFutureRequired",isFutureRequired);
         					inputparam.put("isFutureRequired", isFutureRequired);
         					
        					result = (Result) CommonUtils.callIntegrationService(request,inputparam, null, OperationName.SERVICE_ID_ARRTRANSACTIONS,
        							OperationName.OPER_ID_LOANSCHEDULE, true);
        				} else {
        					result = (Result) CommonUtils.callIntegrationService(request, null, null, OperationName.SERVICE_ID_TRANSORCH,
        							OperationName.OPER_ID_TRANSACTIONS, true);
        				}
        				Dataset ds =result.getDatasetById("Transactions");
        				Transactions.addAllRecords(ds.getAllRecords());
        				result.addDataset(Transactions);
        				result.removeDatasetById("Transactions");
        			} else {
        				result.addDataset(Transactions);
        			}
        			
        			return result;
            	}
        		else if (ARRANGEMENTS_BACKEND.equals("MOCK")) {
        	        	Map<String, Object> inputMap = new HashMap<>();
        	            Map<String, Object> headerMap = new HashMap<>();
        	            if(transactionType.equals("LoanSchedule")) {
        	            	String transactionResponse = Executor.invokePassThroughServiceAndGetString((InfinityServices)
            	            		HoldingsAPIServices.LOAN_SCHEDULED_TRANSACTIONS_MOCK, inputMap, headerMap);
        	            	JSONObject transJson = new JSONObject(transactionResponse);
            				JSONArray transactionsArr = (JSONArray) transJson.get("Transactions");
            				JSONObject transactionsRes = new JSONObject();
            				transactionsRes.put("accountransactionview",transactionsArr);
            				return JSONToResult.convert(transactionsRes.toString());        	            }
        	            else {
        	            String transactionResponse = Executor.invokePassThroughServiceAndGetString((InfinityServices)
        	            		HoldingsAPIServices.ACCOUNT_PENDING_AND_POSTED_TRANSACTIONS_MOCK, inputMap, headerMap);
        	            JSONObject transJson = new JSONObject(transactionResponse);
        				JSONArray transactionsArr = (JSONArray) transJson.get("Transactions");
        				JSONObject transactionsRes = new JSONObject();
        				transactionsRes.put("accountransactionview",transactionsArr);
        				return JSONToResult.convert(transactionsRes.toString());
        	            }
        	    	}
        		}
        		//Load Transaction Types
                TransactionTypeProperties props = new TransactionTypeProperties(request);
        		if(transactionType.equals("LoanSchedule")) {
        			String isFutureRequired  =request.getParameter("isFutureRequired");
        			if(StringUtils.isBlank(isFutureRequired)) {
        				isFutureRequired="false";
        			}
        			result = AccountTransactionsResource.getLoanScheduleTransactions(order,offset,limit,accountID,transactionType,sortBy,searchStartDate,isFutureRequired,request,authToken);
        		}
        		else {
        			result = AccountTransactionsResource.getAccountPendingAndPostedTransactions(order,offset,limit,accountID,transactionType,sortBy,searchStartDate,request,authToken);
        		}
        } catch (Exception e) {
        	return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
        }

        return result;
    }
    

}
