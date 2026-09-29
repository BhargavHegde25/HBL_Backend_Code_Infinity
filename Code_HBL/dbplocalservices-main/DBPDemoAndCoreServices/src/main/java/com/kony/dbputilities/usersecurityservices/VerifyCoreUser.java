package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class VerifyCoreUser implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.USER_GET);
            result = postProcess(result);
        }

        return result;
    }

    private Result postProcess(Result result) {

        Dataset dataset = HelperMethods.getDataSet(result);
        dataset.setId(DBPUtilitiesConstants.CORE_ATTR);

        Dataset dataset2 = new Dataset();
        dataset2.setId(DBPUtilitiesConstants.CORE_ATTR + 1);

        Record usrAttr = new Record();
        if (HelperMethods.hasRecords(result)) {
            HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.USER_EXISTS_IN_EXTERNALBANK,
                    ErrorCodes.RECORD_FOUND, usrAttr);
        } else if (HelperMethods.hasError(result)) {
            HelperMethods.setValidationMsgwithCode(HelperMethods.getError(result), ErrorCodes.ERROR_SEARCHING_RECORD,
                    usrAttr);
        } else {
            HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.USER_NOT_EXISTS_IN_EXTERNALBANK,
                    ErrorCodes.RECORD_NOT_FOUND, usrAttr);
        }

        dataset2.addRecord(usrAttr);

        result.addDataset(dataset2);
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String ssn = inputParams.get("ssn");
        String phone = inputParams.get("phone");
        String email = inputParams.get("email");
        String filterKey = "";
        String filtervalue = "";
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(ssn)) {
            filterKey = "ssn";
            filtervalue = ssn;
        } else if (StringUtils.isNotBlank(phone)) {
            filterKey = "phone";
            filtervalue = phone;
        } else if (StringUtils.isNotBlank(email)) {
            filterKey = "email";
            filtervalue = email;
        } else {
            Record record = new Record();
            record.setId(DBPUtilitiesConstants.CORE_ATTR);
            HelperMethods.setValidationMsgwithCode(DBPUtilitiesConstants.INVALID_DETAILS,
                    ErrorCodes.ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS, record);
            result.addRecord(record);
            status = false;
        }

        if (status) {
            sb.append(filterKey).append(DBPUtilitiesConstants.EQUAL).append(filtervalue);
            inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        }
        return status;
    }
}
