package com.kony.dbputilities.extaccountservices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class CreateExternalAccount implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String,String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            String id = inputParams.getOrDefault("id", HelperMethods.getRandomNumericString(8));
        	inputParams.put("Id", id);
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.EXT_ACCOUNTS_CREATE);
        } else {
            return new Result();
        }
        if (HelperMethods.hasError(result)) {
            HelperMethods.setValidationMsg("Account Number or IBAN already exists in your Payee List", dcRequest,
                    result);
            result.addStringParam("errmsg", "Failed to add the account");
        }
        else if(HelperMethods.hasRecords(result)) {
    		Result rs = new Result();
    		rs.addAllParams(result.getAllDatasets().get(0).getRecord(0).getAllParams());
    		return rs;
        }

        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map<String,String> inputParams, DataControllerRequest dcRequest, Result result) throws HttpCallException {
        boolean status = true;

        /* if the user id is not fetched from reqParam then look for it in session */
        HelperMethods.removeNullValues(inputParams);
        String userFilter = "";
        /* fetch from input params */
        String userId = inputParams.get(DBPInputConstants.USER_ID);
        String companyId = inputParams.get(DBPUtilitiesConstants.COMPANYID);
        String isBusinessPayee = inputParams.get(DBPUtilitiesConstants.IS_BUSINESS_PAYEE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createdOn = dateFormat.format(new Date());

        /* if input params does not have this fetch from session */
        if(StringUtils.isBlank(userId))
            userId = HelperMethods.getUserIdFromSession(dcRequest);
        if(StringUtils.isBlank(companyId))
            companyId = HelperMethods.getOrganizationIDForUser(userId,dcRequest);
        if(StringUtils.isBlank(isBusinessPayee))
            isBusinessPayee = "0";

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(DBPUtilitiesConstants.FILTER, "Customer_id eq " + userId);

        String isSameBankAccnt = inputParams.get(DBPUtilitiesConstants.IS_SAME_BANK_ACCNT);
        String isIntAccount =    inputParams.get(DBPUtilitiesConstants.IS_INT_ACCNT);
        inputParams.put(DBPUtilitiesConstants.USER_ID, userId);
        inputParams.put(DBPUtilitiesConstants.CREATED_ON, createdOn);
        if("1".equals(isBusinessPayee))
            inputParams.put(DBPUtilitiesConstants.P_ORGANIZATION_ID, companyId);

        if (StringUtils.isNotBlank(inputParams.get(DBPInputConstants.BANK_ID))) {
            inputParams.put(DBPUtilitiesConstants.BANK_ID, inputParams.get(DBPInputConstants.BANK_ID));
        }
        if (StringUtils.isNotBlank(inputParams.get(DBPInputConstants.USR_ACCNT))) {
            inputParams.put(DBPUtilitiesConstants.USR_ACCNT, inputParams.get(DBPInputConstants.USR_ACCNT));
        }
        if (!StringUtils.isNotBlank(isIntAccount)) {
            inputParams.put(DBPUtilitiesConstants.IS_SAME_BANK_ACCNT, "false");
        }
        if (!StringUtils.isNotBlank(isSameBankAccnt)) {
            inputParams.put(DBPUtilitiesConstants.IS_INT_ACCNT, "false");
        }

        String iBAN = (String) inputParams.get("IBAN");
        if(StringUtils.isBlank(iBAN))
        	iBAN = inputParams.get("iban");
        if (StringUtils.isNotBlank(iBAN)) {
            inputParams.put("IBAN", iBAN);
        }
        
        /* remove product related fields */
        inputParams.remove("userId");
        inputParams.remove("companyId");
        inputParams.remove("isBusinessPayee");

        return status;
    }
}