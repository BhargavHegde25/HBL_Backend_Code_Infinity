package com.kony.achservices;

import java.util.Map;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * Class FetchBBACHTransactions
 */
public class FetchAllACHTransactionsRequestedByMeForApproval implements JavaService2 {

    /**
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

        return fetchAllTransactionsRequestedByMeForApprovalHelper(dataControllerRequest);
    }

    /**
     * @param dcr
     * @return
     * @throws Exception
     * @description Helper function for fetching AllTransactionsRequested By a User and went for an approval
     */
    public Result fetchAllTransactionsRequestedByMeForApprovalHelper(DataControllerRequest dcr) throws Exception {
        try {
            Map<String, String> dataMap = CommonUtils.getMyRequestsFilter(dcr);
            dataMap = CommonUtils.addSoftDeleteFilter(dataMap);
            return HelperMethods.callApi(dcr, dataMap, HelperMethods.getHeaders(dcr),
                    URLConstants.BB_TRANSACTION_DETAILS_VIEW_GET);
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
