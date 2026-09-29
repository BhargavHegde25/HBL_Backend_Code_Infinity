package com.temenos.infinity.api.docmanagement.javaservices;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.GetAccountTransHelper;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;

public class GetBillTransactions implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

        if (preProcess(inputParams, dcRequest, result, user)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
        }

        if (HelperMethods.hasRecords(result)) {
            result.getAllDatasets().get(0).setId("Transactions");
        } else {
            result = new Result();
            Dataset dataset = new Dataset();
            dataset.setId("Transactions");
            result.addDataset(dataset);
        }

        return result;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
   	    Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
        String legalEntityId = (String) customer.get("legalEntityId");
        boolean status = true;
        inputParams.put("countryCode", user.get("countryCode"));
        if(inputParams.get("User_id")==null) {
        	inputParams.put(DBPUtilitiesConstants.UA_USR_ID, user.get("user_id"));
        }
        if (StringUtils.isNotBlank(legalEntityId)) { 
        	inputParams.put("legalEntityId", legalEntityId);
        }
        GetAccountTransHelper helper = new GetAccountTransHelper();
        helper.constructQuery(inputParams, dcRequest, result);
        return status;
    }
}
