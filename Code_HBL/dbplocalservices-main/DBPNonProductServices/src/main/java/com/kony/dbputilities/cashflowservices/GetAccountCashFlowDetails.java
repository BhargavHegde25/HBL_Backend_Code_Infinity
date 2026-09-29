package com.kony.dbputilities.cashflowservices;

import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountCashFlowDetails implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        Result result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_CASH_FLOW_GET);
        postProcess(result);
        return result;
    }

    private void postProcess(Result result) {
        if (HelperMethods.hasRecords(result)) {
            Formatter fmt = new Formatter();
            Calendar cal = Calendar.getInstance();
            List<Record> records = result.getAllDatasets().get(0).getAllRecords();
            for (Record temp : records) {
                temp.addParam(new Param("month", fmt.format("%tB", cal).toString(), DBPUtilitiesConstants.STRING_TYPE));
            }
            fmt.close();
        }
    }
}
