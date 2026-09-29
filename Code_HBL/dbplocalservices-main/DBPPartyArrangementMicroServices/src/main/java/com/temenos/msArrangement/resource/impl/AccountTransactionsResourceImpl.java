package com.temenos.msArrangement.resource.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.msArrangement.utils.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.msArrangement.resource.api.AccountTransactionsResource;
import com.temenos.msArrangement.businessdelegate.api.AccountTransactionsBusinessDelegate;
import com.temenos.msArrangement.dto.AccountTransactionsDTO;

/**
 * 
 * @author KH2281
 * @version 1.0 Extends the {@link AccountTransactionsResource}
 */
public class AccountTransactionsResourceImpl implements AccountTransactionsResource {
    
    //Initialize Result Object
    Result result = new Result(); 
    JSONObject responseObj = new JSONObject();
    
    //Get Instance of OfficerBusiness Delegate
    AccountTransactionsBusinessDelegate GetMicroServiceBDInstance = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountTransactionsBusinessDelegate.class);
     
    //Implementing the get AccountTransactions method
    @Override
    public Result getAccountTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        
        //Set Request Parameters
        
    	AccountTransactionsDTO inputPayLoad=new AccountTransactionsDTO();
    	inputPayLoad.setOrder(request.getParameter("order"));
    	inputPayLoad.setOffset(request.getParameter("offset"));
    	inputPayLoad.setLimit(request.getParameter("limit"));
    	inputPayLoad.setAccountId(request.getParameter("accountID"));
    	inputPayLoad.setTransactionType(request.getParameter("transactionType"));
    	inputPayLoad.setIsScheduled(request.getParameter("isScheduled"));
    	inputPayLoad.setSortBy(request.getParameter("sortBy"));
    	//Invoke and get the Account Transaction details from MicroServices
        List<AccountTransactionsDTO> accountTransactionsDTO = GetMicroServiceBDInstance.getDetailsFromHoldingsMicroService(inputPayLoad);
		if (accountTransactionsDTO == null) {
			JSONArray NoRecords=new JSONArray();
			responseObj.put("accountransactionview",NoRecords);
			result = JSONToResult.convert(responseObj.toString());
	        return result;
		}
        
		JSONArray transactionsJSONArr = new JSONArray(accountTransactionsDTO);
		// Written this as after converting to JSONArray the keys are being converted to lower case insted of retaining in its original state 
		for (int i = 0; i < transactionsJSONArr.length(); i++) {
            JSONObject transaction = transactionsJSONArr.getJSONObject(i);
            String id = transaction.getString("id");
            transaction.put("Id", id);
            transaction.remove("id"); 
		}
		
		responseObj.put("accountransactionview", transactionsJSONArr);
		result = JSONToResult.convert(responseObj.toString());
        return result;
    }
       
}