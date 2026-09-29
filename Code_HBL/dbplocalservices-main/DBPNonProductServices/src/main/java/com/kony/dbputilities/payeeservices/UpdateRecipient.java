package com.kony.dbputilities.payeeservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateRecipient implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_UPDATE);
        }
        
        if(HelperMethods.hasRecords(result)) {
    		Result rs = new Result();
    		rs.addAllParams(result.getAllDatasets().get(0).getRecord(0).getAllParams());
    		return rs;
        }
        
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        String payeeId = (String) inputParams.get("payeeId");
        if(StringUtils.isBlank(payeeId)) {
        	payeeId = (String) inputParams.get("id");
        }
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result payee = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        if (!HelperMethods.hasRecords(payee)) {
            return false;
        }
        String userId = HelperMethods.getFieldValue(payee, "User_Id");
        String organizationIdTable = HelperMethods.getFieldValue(payee, "organizationId");
        String type = (String) inputParams.get("type");
        String userid = null;
        String isBusinessPayee = null;
        String organizationId = null;
        String IBAN = (String) inputParams.get("IBAN");
        
        if(inputParams.get("isBusinessPayee") != null)
        	isBusinessPayee = inputParams.get("isBusinessPayee").toString();
        
        if(StringUtils.isNotBlank(isBusinessPayee) && isBusinessPayee.equals("1") && inputParams.get("companyId") != null) {
        	organizationId = inputParams.get("companyId").toString();
        }
        if(inputParams.get("userId") != null){
        	userid = inputParams.get("userId").toString();
        }
        
        if(userid == null) {
        	userid = HelperMethods.getCustomerIdFromSession(dcRequest);
        }
        
        if (StringUtils.isBlank(userid) || !userid.equals(userId)) {
            return false;
        }
        
        if(StringUtils.isNotBlank(isBusinessPayee) && isBusinessPayee.equals("1")) {
        	if(StringUtils.isBlank(organizationIdTable) || StringUtils.isBlank(organizationId) || !organizationId.contentEquals(organizationIdTable)) {
        	     return false;
        	}
        }

        if(StringUtils.isBlank(type)) {
        	type = (String) inputParams.get("typeId");
        }
        
        String inputId = (String) inputParams.get("payeeId");
        String inputNickName = (String) inputParams.get("payeeNickName");
        String inputName = (String) inputParams.get("payeeName");
        if(StringUtils.isBlank(inputId)) {
        	inputId = (String) inputParams.get("id");
        }
        if(StringUtils.isBlank(inputNickName)) {
        	inputNickName = (String) inputParams.get("nickName");
        }
        if(StringUtils.isBlank(inputName)) {
        	inputName = (String) inputParams.get("name");
        }
        
        if (StringUtils.isBlank(IBAN)) {
        	IBAN = (String) inputParams.get("iban");
        }
        if(!StringUtils.isBlank(IBAN)) {
        	inputParams.put("IBAN", IBAN);
        }
        
        inputParams.put("Id", inputId);
        inputParams.put("nickName", inputNickName);
        inputParams.put("eBillEnable", inputParams.get("EBillEnable"));
        inputParams.put("name", inputName);
        inputParams.put("Type_id", type);
        HelperMethods.removeNullValues(inputParams);
        return true;
    }
}
