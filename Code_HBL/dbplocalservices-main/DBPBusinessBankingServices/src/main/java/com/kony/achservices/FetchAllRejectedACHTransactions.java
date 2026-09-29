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

/**
 * Class FetchBBUserRequests
 */
public class FetchAllRejectedACHTransactions implements JavaService2 {

    /**
     * @param s
     * @param objects
     * @return
     * @throws Exception
     * @description Base invoke method
     */
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dcr, DataControllerResponse dcResponse)
            throws Exception {
        return fetchAllRejectedACHTransactionsHelper(dcr);
    }

    /**
     * @param dcr
     * @return
     * @throws Exception
     * @description Helper function for fetching transaction of a BB User
     */
    public Result fetchAllRejectedACHTransactionsHelper(DataControllerRequest dcr) throws Exception {
        try {
            Map<String, String> dataMap = new HashMap<>();

            String filter = "Company_id" + DBPUtilitiesConstants.EQUAL + CommonUtils.getUserCompanyId(dcr)
                    + DBPUtilitiesConstants.AND + "Status" + DBPUtilitiesConstants.EQUAL
                    + CommonUtils.getStatusid(dcr, "Rejected") + DBPUtilitiesConstants.AND + "Request_id"
                    + DBPUtilitiesConstants.NOT_EQ + "null" + DBPUtilitiesConstants.AND + "createdby"
                    + DBPUtilitiesConstants.EQUAL + CommonUtils.getUserId(dcr);

            dataMap.put(DBPUtilitiesConstants.FILTER, filter);
            dataMap = CommonUtils.addSoftDeleteFilter(dataMap);

            return HelperMethods.callApi(dcr, dataMap, HelperMethods.getHeaders(dcr),
                    URLConstants.BB_TRANSACTION_DETAILS_VIEW_GET);
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
