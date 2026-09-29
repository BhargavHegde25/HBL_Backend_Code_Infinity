package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateBackendIdentifier implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (dcRequest.getSource() != null) {
            for (Entry<String, String[]> entry : dcRequest.getSource().entrySet()) {
                String[] value = entry.getValue();
                inputParams.put(entry.getKey(), value[0]);
            }
        }
        if (!StringUtils.isBlank(inputParams.get(DBPConstants.DBP_ERROR_CODE_KEY))) {
            result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, inputParams.get(DBPConstants.DBP_ERROR_CODE_KEY),
                    "String"));
            result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY,
                    inputParams.get(DBPConstants.DBP_ERROR_MESSAGE_KEY), "String"));
            return result;
        }

        String customerId = inputParams.get("Customer_id");
        if (StringUtils.isBlank(customerId)) {
            customerId = dcRequest.getParameter("Customer_id");
        }
        if (StringUtils.isBlank(customerId)) {
            customerId = dcRequest.getParameter("applicantID");
        }
        if (StringUtils.isBlank(customerId)) {
            customerId = inputParams.get("applicantID");
        }
        String backendIdentifierInfo = dcRequest.getParameter("backendIdentifierInfo");
        List<HashMap<String, String>> allRecords = HelperMethods.getAllRecordsMap(backendIdentifierInfo);

        for (HashMap<String, String> map : allRecords) {
            map.put("id", UUID.randomUUID().toString());
            map.put("Customer_id", customerId);
            HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BACKENDIDENTIFIER_CREATE);
        }
        return result;
    }

}
