package com.kony.dbputilities.billerservices;

import java.util.List;
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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetBillerByAccountNumber implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_MASTER_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        List<Record> billerMasters = result.getAllDatasets().get(0).getAllRecords();
        for (Record billerMaster : billerMasters) {
            String billerCategoryId = HelperMethods.getFieldValue(billerMaster, "billerCategoryId");
            billerMaster.addParam(new Param("billerCategoryName", getCategoryName(dcRequest, billerCategoryId),
                    DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private String getCategoryName(DataControllerRequest dcRequest, String billerCategoryId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billerCategoryId;
        Result billerMaster = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_CATEGORY_GET);
        return HelperMethods.getFieldValue(billerMaster, "cattegoryName");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String accountNumber = (String) inputParams.get("accountNumber");
        String billerName = (String) inputParams.get("billerName");
        String zipCode = (String) inputParams.get("zipCode");
        StringBuilder filter = new StringBuilder();
        if (StringUtils.isNotBlank(accountNumber)) {
            filter.append("accountNumber").append(DBPUtilitiesConstants.EQUAL).append(accountNumber);
            filter.append(DBPUtilitiesConstants.AND);
        }
        if (StringUtils.isNotBlank(billerName)) {
            filter.append("billerName").append(DBPUtilitiesConstants.EQUAL).append(billerName);
            filter.append(DBPUtilitiesConstants.AND);
        }
        if (StringUtils.isNotBlank(zipCode)) {
            filter.append("zipCode").append(DBPUtilitiesConstants.EQUAL).append(zipCode);
            filter.append(DBPUtilitiesConstants.AND);
        }
        filter.append("id").append(DBPUtilitiesConstants.NOT_EQ).append("0");
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        return true;
    }
}