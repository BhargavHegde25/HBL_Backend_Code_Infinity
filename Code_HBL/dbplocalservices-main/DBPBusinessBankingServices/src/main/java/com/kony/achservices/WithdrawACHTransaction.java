package com.kony.achservices;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class WithdrawACHTransaction implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
            DataControllerResponse dataControllerResponse) throws Exception {

        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(objects);
            HashMap<String, String> dataMap = new HashMap<>();
            Result result = new Result();
            HashMap<String, String> dataMapFilter = new HashMap<>();

            dataMapFilter.put(DBPUtilitiesConstants.FILTER,
                    "Request_id" + DBPUtilitiesConstants.EQUAL + inputParams.get("Request_id"));
            Result fetchedTransactions = HelperMethods.callApi(dataControllerRequest, dataMapFilter,
                    HelperMethods.getHeaders(dataControllerRequest), URLConstants.BB_TRANSACTION_DETAILS_VIEW_GET);

            if (!CommonUtils.getUserId(dataControllerRequest)
                    .equals(HelperMethods.getFieldValue(fetchedTransactions, "createdby"))) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            } else {
                // Delete entry from BBRequests
                CommonUtils.deleteBBRequestHelper(inputParams, dataControllerRequest);

                dataMap.put("Transaction_id", HelperMethods.getFieldValue(fetchedTransactions, "Transaction_id"));

                // Update Status in BBGeneralTransaction
                result = updateACHTransaction(dataMap, dataControllerRequest);
            }

            return result;
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }

    }

    /**
     * @param inputParams
     * @param dcr
     * @return
     * @throws Exception
     * @description Helper function for updating general transaction of a BB User
     */
    public Result updateACHTransaction(HashMap<String, String> inputParams, DataControllerRequest dcr)
            throws Exception {

        String status_id = CommonUtils.getStatusid(dcr, "Withdrawn");

        inputParams.put("Status", status_id);
        inputParams.put("ActedBy", CommonUtils.getUserName(dcr));

        return HelperMethods.callApi(dcr, inputParams, HelperMethods.getHeaders(dcr),
                URLConstants.BB_TRANSACTION_UPDATE);

    }
}
