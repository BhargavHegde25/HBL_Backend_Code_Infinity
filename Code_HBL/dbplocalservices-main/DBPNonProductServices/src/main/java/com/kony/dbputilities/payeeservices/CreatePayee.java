package com.kony.dbputilities.payeeservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreatePayee implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
        	inputParams.put("Id", HelperMethods.getRandomNumericString(8));
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_CREATE);
        }
        if(HelperMethods.hasRecords(result)) {
    		Result rs = new Result();
    		rs.addAllParams(result.getAllDatasets().get(0).getRecord(0).getAllParams());
    		return rs;
        }

        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        
        boolean status = true;
        String accountNumber = inputParams.get("accountNumber");
        String nickName = inputParams.get("payeeNickName");
        String billerId = inputParams.get("billerId");
        
        if(StringUtils.isBlank(nickName))
        	nickName = inputParams.get("nickName");
        String name = nickName;
        
        String userId = inputParams.get("userId");
        if (StringUtils.isBlank(userId)) {
        	userId = HelperMethods.getUserIdFromSession(dcRequest);
        }
        
        if (StringUtils.isBlank(userId)) {
        	HelperMethods.setValidationMsg("Not a valid userId", dcRequest, result);
        	return false;
        }
        
        String organizationId = null;
        String isBusinessPayee = inputParams.get("isBusinessPayee");
        if(StringUtils.isNotBlank(isBusinessPayee) && "1".equals(isBusinessPayee)) {
        	organizationId = inputParams.get("companyId");
        	if (StringUtils.isBlank(organizationId)) {
            	HelperMethods.setValidationMsg("Not a valid organizationId", dcRequest, result);
            	return false;
            }
        }
        
        if (StringUtils.isNotBlank(inputParams.get("payeeName"))) {
            name = inputParams.get("payeeName");
        }
        if (null == accountNumber || null == nickName) {
            HelperMethods.setValidationMsg("Please provide NickName " + "and AccountNumber", dcRequest, result);
            status = false;
        }
        if (StringUtils.isBlank(billerId)) {
            billerId = "1";
        }
        if (status) {
            inputParams.put("billerId", billerId);
            inputParams.put("User_Id", userId);
            inputParams.put("nickName", nickName);
            inputParams.put("name", name);
            inputParams.put("billermaster_id", billerId);
            inputParams.put("organizationId", organizationId);
        }
        return status;
    }
}