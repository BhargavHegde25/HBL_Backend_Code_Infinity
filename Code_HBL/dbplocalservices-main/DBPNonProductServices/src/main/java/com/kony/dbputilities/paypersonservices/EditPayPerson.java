package com.kony.dbputilities.paypersonservices;

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

public class EditPayPerson implements JavaService2 {
    /**
     * updates a payperson in database
     * 
     * inputs : firstName, lastName, nickName, phone, email, secondaryEmail, secondaryPhoneNumber,
     * secondaryPhoneNumber2, secondaryEmail2, id
     * 
     * Mandatory inputs: id
     * 
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYPERSON_UPDATE);
        } else {
            result.addParam(new Param("errmsg", "Not a valid user to edit", "String"));
        }
        return result;
    }

    /**
     * validates mandatory field : Payperson id
     * 
     * @param inputParams
     *            - map of input params defined for service api
     * @param dcRequest
     * @param result
     * @return - returns boolean
     * @throws HttpCallException
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;

        if (checkRecordExists(inputParams, dcRequest, result)) {
            String id = (String) inputParams.get("PayPersonId");
            if (!StringUtils.isNotBlank(id)) {
                HelperMethods.setValidationMsg("Please provide payperson id.", dcRequest, result);
                status = false;
            }
            inputParams.put(DBPUtilitiesConstants.PP_ID, id);
            HelperMethods.removeNullValues(inputParams);
            return status;
        } else {

            status = false;
        }
        return status;
    }

    private boolean checkRecordExists(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        boolean status = true;

        String id = HelperMethods.getCustomerIdFromSession(dcRequest);
        if(inputParams.get("userId") != null) {
        	id = inputParams.get("userId").toString();
        }

        String filter = "id" + DBPUtilitiesConstants.EQUAL + inputParams.get("PayPersonId") + DBPUtilitiesConstants.AND
                + "User_id" + DBPUtilitiesConstants.EQUAL + id;

        Result existsResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAY_PERSON_GET);

        if (HelperMethods.hasRecords(existsResult)) {
            status = true;
        } else {
            status = false;
        }

        return status;
    }
}
