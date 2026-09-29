package com.kony.dbputilities.transservices;

import java.util.Map;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateTransactionStatus implements JavaService2{

	 @SuppressWarnings({ "rawtypes", "unchecked" })
	    @Override
	    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws Exception {
	        Result result = new Result();
	        Map inputParams = HelperMethods.getInputParamMap(inputArray);
	        String id = (String) inputParams.get("transactionId");
	        String status = (String) inputParams.get("status");
	        inputParams.put(DBPUtilitiesConstants.T_TRANS_ID, id);
	        inputParams.put(DBPUtilitiesConstants.STATUS_DESC,status);
	        
	        if("MSSQL".equalsIgnoreCase(QueryFormer.getDBType(dcRequest)))
	    	{
	        	
	    		result=TransOperations.createDataSetUpdate(dcRequest, inputParams);
	    	}
	        else {
	            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
	                    URLConstants.ACCOUNT_TRANSACTION_UPDATE);}
	            if(HelperMethods.hasParam(result, "updatedRecords"))
	            	result.addParam(new Param("referenceId", inputParams.get("transactionId").toString()));
	        
	        
	        return result;
	    }

}
