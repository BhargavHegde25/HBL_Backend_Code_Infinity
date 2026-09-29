package com.kony.achservices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.kony.dbputilities.sessionmanager.SessionScope;
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
public class FetchAllACHTransactionsPendingForMyApproval implements JavaService2 {

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

        return fetchAllACHTransactionsPendingForMyApprovalHelper(dcr);

    }

    /**
     * @param dcr
     * @return
     * @throws Exception
     * @description Helper function for fetching transaction of a BB User
     */
    public Result fetchAllACHTransactionsPendingForMyApprovalHelper(DataControllerRequest dcr) throws Exception {
        try {
            Map<String, String> dataMap = new HashMap<>();
            Map<String, String> listOfIds = CommonUtils.getAllTypeMappings(dcr);
            Set<String> accounts = CommonUtils.getAllAccounts(dcr);

            String accountsFilter = CommonUtils.getFilterForAList(accounts, "DebitAccount");
            HashMap<String, JSONObject> approveList = SessionScope.getUserApproveServices(dcr);
            HashMap<String, JSONObject> selfApproveList = SessionScope.getUserSelfApproveServices(dcr);

            String permissionFilter = " ( ";

            Set<String> keys = listOfIds.keySet();

            Iterator<String> itr = keys.iterator();

            while (itr.hasNext()) {
                String key = itr.next();
                if ("ACH Payment".equals(key) || "ACH Collection".equals(key)) {
                    permissionFilter = permissionFilter + CommonUtils.getQueryForApprovals(dcr, approveList,
                            selfApproveList, listOfIds.get(key), "BBGeneralTransactionType_id")
                            + DBPUtilitiesConstants.OR;
                }
            }

            permissionFilter = permissionFilter + "BBGeneralTransactionType_id" + DBPUtilitiesConstants.EQUAL + "0"
                    + " ) ";

            String filter = "Company_id" + DBPUtilitiesConstants.EQUAL + CommonUtils.getUserCompanyId(dcr)
                    + DBPUtilitiesConstants.AND + "Status" + DBPUtilitiesConstants.EQUAL
                    + CommonUtils.getStatusid(dcr, "Pending") + DBPUtilitiesConstants.AND + "Request_id"
                    + DBPUtilitiesConstants.NOT_EQ + "null" + DBPUtilitiesConstants.AND + permissionFilter
                    + DBPUtilitiesConstants.AND + accountsFilter;

            dataMap.put(DBPUtilitiesConstants.FILTER, filter);

            dataMap = CommonUtils.addSoftDeleteFilter(dataMap);

            Result result = HelperMethods.callApi(dcr, dataMap, HelperMethods.getHeaders(dcr),
                    URLConstants.BB_TRANSACTION_DETAILS_VIEW_GET);

            return result;
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
