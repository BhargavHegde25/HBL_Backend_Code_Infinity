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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class DeletePayee implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_UPDATE);
            if (HelperMethods.hasRecords(result)) {
                String payeeId = HelperMethods.getFieldValue(result, "Id");
                String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
                Result payee = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.PAYEE_GET);
                String isWiredRecepient = HelperMethods.getFieldValue(payee, "isWiredRecepient");
                result.addParam(new Param("isWiredRecepient", isWiredRecepient, DBPUtilitiesConstants.STRING_TYPE));
                result.addParam(new Param("Id", payeeId, DBPUtilitiesConstants.STRING_TYPE));
            }
        } else {
            result.addParam(new Param("errmsg", "Not a valid user", "String"));
        }

        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;

        if (checkRecordExists(inputParams, dcRequest, result)) {
        	String id = (String) inputParams.get("payeeId");
        	if (StringUtils.isBlank(id)) {
        		id = (String) inputParams.get("id");
            }
            if (!StringUtils.isNotBlank(id)) {
                HelperMethods.setValidationMsg("Please provide payee id.", dcRequest, result);
                status = false;
            }

            if (status) {
                inputParams.put("Id", id);
                inputParams.put("softDelete", "true");
            }
        } else {

            status = false;

        }
        return status;
    }

    private boolean checkRecordExists(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        boolean status = true;
        
        String userid = null;
        String organizationId = null;
        String isBusinessPayee = null;
        
        if(inputParams.get("isBusinessPayee") != null)
        	isBusinessPayee = inputParams.get("isBusinessPayee").toString();
        
        if(StringUtils.isNotBlank(isBusinessPayee) && "1".equals(isBusinessPayee) && inputParams.get("companyId") != null) {
        	organizationId = inputParams.get("companyId").toString();
        }
        
        if(inputParams.get("userId") != null){
        	userid = inputParams.get("userId").toString();
        }
        else {
        	userid = HelperMethods.getCustomerIdFromSession(dcRequest);
        }
        
        String payeeId = inputParams.get("payeeId");
        if(StringUtils.isBlank(payeeId)) {
        	payeeId = inputParams.get("id");
        }

        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId + DBPUtilitiesConstants.AND
                + "(" + "User_Id" + DBPUtilitiesConstants.EQUAL + userid;
        
        if(StringUtils.isNotBlank(organizationId)) {
        	filter = filter + DBPUtilitiesConstants.OR + "organizationId" + DBPUtilitiesConstants.EQUAL + organizationId;
        }
        
        filter = filter + ")";
        
        Result chkResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);

        if (HelperMethods.hasRecords(chkResult)) {
            status = true;
        } else {
            status = false;
        }

        return status;
    }
}
