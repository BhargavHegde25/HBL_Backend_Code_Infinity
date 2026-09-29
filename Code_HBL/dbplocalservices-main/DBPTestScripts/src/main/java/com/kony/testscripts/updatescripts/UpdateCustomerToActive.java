package com.kony.testscripts.updatescripts;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCustomerToActive implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        String id = inputParams.get("id");
        if (StringUtils.isNotBlank(dcRequest.getParameter("UserName"))) {
            inputParams.put("UserName", dcRequest.getParameter("UserName"));
        }
        inputParams.put("Status_id", "SID_CUS_ACTIVE");
        inputParams.put("Password", "$2a$11$zf5JKbRq6bEjT/M0AqKAnOChHbfxTj1LHXK6jig9a1hc7t1ze.4jm");
        inputParams.put("isEagreementSigned", "1");
        inputParams.put("isEnrolled", "true");

        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_UPDATE);
        
        Map<String,String> map = new HashMap<>();
		map.put("isOLB","true");
		map.put("statusId",HelperMethods.getCustomerStatus().get("ACTIVE"));
		map.put("customerId",id);
		map.put("legalEntityList","GB0010001"); // Updates all associated users to active irrespective of legalEntityList
		HelperMethods.callApiJson(dcRequest, map, HelperMethods.getHeaders(dcRequest), URLConstants.UPDATE_USERSTATUS_BY_LEGALENTITY_PROC);
		

        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
        pm.makePasswordEntry(dcRequest, id, inputParams.get("Password"));

        inputParams.clear();
        inputParams.put(DBPUtilitiesConstants.SELECT, "UserName");
        String filterQuery = "id" + DBPUtilitiesConstants.EQUAL + id;
        result = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERVERIFY_GET);

        return result;
    }

}