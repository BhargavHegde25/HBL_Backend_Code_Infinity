package com.kony.dbputilities.billerservices;

import java.util.List;

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

public class GetBillerMasters implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        result = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_MASTER_GET);
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
}