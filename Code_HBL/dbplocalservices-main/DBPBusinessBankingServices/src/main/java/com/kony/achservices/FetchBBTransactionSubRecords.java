package com.kony.achservices;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * Class FetchBBTransactionSubRecords
 */
public class FetchBBTransactionSubRecords implements JavaService2 {

    /**
     *
     * @param s
     * @param objects
     * @param dataControllerRequest
     * @param dataControllerResponse
     * @return
     * @throws Exception
     * @description Base invoke method
     */
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
            DataControllerResponse dataControllerResponse) throws Exception {
        Map<String, String> inputParams = HelperMethods.getInputParamMap(objects);
        return fetchBBTransactionSubRecordHelper(inputParams, dataControllerRequest);
    }

    /**
     *
     * @param inputParams
     * @param dcr
     * @return
     * @throws Exception
     * @description Helper function for fetching transaction subRecord of a BB User
     */
    public Result fetchBBTransactionSubRecordHelper(Map<String, String> inputParams, DataControllerRequest dcr)
            throws Exception {
        try {
            Map<String, String> dataMap = new HashMap<>();
            String TransactionRecord_id = inputParams.get("TransactionRecord_id");
            if (TransactionRecord_id == null) {
                return ErrorCodeEnum.ERR_12038.setErrorCode(new Result());
            }
            String filter = "TransactionRecord_id" + DBPUtilitiesConstants.EQUAL + TransactionRecord_id;
            dataMap.put(DBPUtilitiesConstants.FILTER, filter);
            return HelperMethods.callApi(dcr, dataMap, HelperMethods.getHeaders(dcr),
                    URLConstants.BB_TRANSACTION_SUB_RECORD_DETAILS_VIEW_GET);
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
