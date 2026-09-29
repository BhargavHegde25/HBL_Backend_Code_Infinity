package com.kony.testscripts.getscripts;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author sowmya.vandanapu
 *
 */
public class GetActivationCodeAndUsernameForEnrollment implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse)
            throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String id = StringUtils.isNotBlank(inputParams.get("id")) ? inputParams.get("id")
                : dcRequest.getParameter("id");
        String isCore = StringUtils.isNotBlank(inputParams.get("isCore")) ? inputParams.get("isCore")
                : dcRequest.getParameter("isCore");
        String filterQuery = "";
        Result temp = new Result();
        if (Boolean.parseBoolean(isCore)) {
            filterQuery = "BackendId" + DBPUtilitiesConstants.EQUAL + id;
            temp = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BACKENDIDENTIFIER_GET);
            id = HelperMethods.getFieldValue(temp, "Customer_id");
        }
        filterQuery = "UserName" + DBPUtilitiesConstants.EQUAL + id;
        temp = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.CREDENTIAL_CHECKER_GET);
        result.addStringParam("UserName", HelperMethods.getFieldValue(result, "UserName"));
        result.addStringParam("activationCode", HelperMethods.getFieldValue(temp, "id"));
        return result;
    }

}
