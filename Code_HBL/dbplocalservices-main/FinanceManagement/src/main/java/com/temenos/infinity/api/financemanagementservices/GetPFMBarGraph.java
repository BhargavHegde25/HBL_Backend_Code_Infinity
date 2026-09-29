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

public class GetPFMBarGraph implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        preProcess(inputParams, dcRequest, result);

        result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.PFMBARGRAPH_GET);

        if (HelperMethods.hasRecords(result)) {
            postProcess(result);
        }

        return result;
    }

    private void postProcess(Result result) {
        String[] months = new String[] { "January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December" };
        List<Record> records = result.getAllDatasets().get(0).getAllRecords();
        String monthId = "1";
        for (Record rec : records) {
            monthId = HelperMethods.getFieldValue(rec, "monthId");
            rec.addParam(
                    new Param("monthName", months[Integer.parseInt(monthId) - 1], DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {

        String year = (String) inputParams.get(DBPUtilitiesConstants.YEAR);

        if (StringUtils.isNotBlank(year)) {

            String filter = DBPUtilitiesConstants.YEAR + DBPUtilitiesConstants.EQUAL + year;

            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        }

        return true;
    }

}
