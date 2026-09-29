package com.temenos.infinity.api.financemanagementservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetPFMPieChart implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PFMPIECHART_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(result);
        }

        return result;
    }

    private void postProcess(Result result) {
        List<Record> pfms = result.getAllDatasets().get(0).getAllRecords();
        double spentTotal = 0;
        Param totalCashSpent = new Param("totalCashSpent", "0", DBPUtilitiesConstants.STRING_TYPE);
        for (Record pfm : pfms) {
            pfm.addParam(totalCashSpent);
            spentTotal = spentTotal + Double.parseDouble(HelperMethods.getFieldValue(pfm, "cashSpent"));
        }
        totalCashSpent.setValue(String.valueOf(spentTotal));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        String monthId = (String) inputParams.get(DBPUtilitiesConstants.MONTH_ID);
        String year = (String) inputParams.get(DBPUtilitiesConstants.YEAR);

        if (StringUtils.isBlank(monthId) || StringUtils.isBlank(year) ) {
            HelperMethods.setValidationMsg( "Both "+ DBPUtilitiesConstants.MONTH_ID + " and "+DBPUtilitiesConstants.YEAR+" fields are mandatory." , dcRequest, result);
        	result.addErrMsgParam( "Both "+ DBPUtilitiesConstants.MONTH_ID + " and "+DBPUtilitiesConstants.YEAR+" fields are mandatory.");
            status = false;
        }
        
        if (status) {
            String filter = "";

            if (StringUtils.isNotBlank(monthId)) {
                filter = DBPUtilitiesConstants.MONTH_ID + DBPUtilitiesConstants.EQUAL + monthId;

            }
            if (StringUtils.isNotBlank(year)) {
                if (!filter.isEmpty()) {
                    filter = filter + DBPUtilitiesConstants.AND;
                }
                filter = filter + DBPUtilitiesConstants.YEAR + DBPUtilitiesConstants.EQUAL + year;
            }

            inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        }
        return status;
    }
}