package com.kony.achservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

public class BBACHTransactionRequestAction implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dcr,
            DataControllerResponse dataControllerResponse) throws Exception {
        try {
            Result result = new Result();
            HashMap<String, String> dataMap = new HashMap<>();
            HashMap<String, String> dataMapFilter = new HashMap<>();
            Map<String, String> inputParams = HelperMethods.getInputParamMap(objects);

            String requestId = inputParams.get("Request_id");
            String action = inputParams.get("Action");

            if (requestId == null) {
                return ErrorCodeEnum.ERR_12035.setErrorCode(new Result());
            }

            if (action == null) {
                return ErrorCodeEnum.ERR_12036.setErrorCode(new Result());
            }

            HashSet<String> accounts = CommonUtils.getAllAccounts(dcr);
            dataMapFilter.put(DBPUtilitiesConstants.FILTER, "Request_id" + DBPUtilitiesConstants.EQUAL + requestId);
            CommonUtils.addFilter(dataMapFilter, "Status" + DBPUtilitiesConstants.EQUAL + "9");

            Result fetchedTransactions = HelperMethods.callApi(dcr, dataMapFilter, HelperMethods.getHeaders(dcr),
                    URLConstants.BB_TRANSACTION_DETAILS_VIEW_GET);

            if (!HelperMethods.hasRecords(fetchedTransactions)) {
                return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
            }

            if (!accounts.contains(HelperMethods.getFieldValue(fetchedTransactions, "DebitAccount"))) {
                return ErrorCodeEnum.ERR_12002.setErrorCode(new Result());
            }

            String transactionType_id = HelperMethods.getFieldValue(fetchedTransactions, "TransactionType_id");
            String transactionType = CommonUtils.getTransactionType(dcr, transactionType_id);
            String vendorService = StringUtils.EMPTY;

            if ("Payment".equals(transactionType)) {
                vendorService = URLConstants.ACH_VENDOR_SERVICE_FOR_PAYMENTS;
            } else if ("Collection".equals(transactionType)) {
                vendorService = URLConstants.ACH_VENDOR_SERVICE_FOR_COLLECTIONS;
            }

            HashMap<String, JSONObject> approveList = SessionScope.getUserApproveServices(dcr);
            HashMap<String, JSONObject> selfApproveList = SessionScope.getUserSelfApproveServices(dcr);

            String userId = CommonUtils.getUserId(dcr);
            String transactionId = HelperMethods.getFieldValue(fetchedTransactions, "BBGeneralTransactionType_id");

            String companyId = CommonUtils.getUserCompanyId(dcr);

            boolean isSameCompanyRequest = companyId
                    .equals(HelperMethods.getFieldValue(fetchedTransactions, "Company_id"));

            if (!isSameCompanyRequest) {
                return ErrorCodeEnum.ERR_12004.setErrorCode(new Result());
            }

            boolean isCreatedByMe = userId.equals(HelperMethods.getFieldValue(fetchedTransactions, "createdby"));

            boolean isApproveEnabled = approveList.containsKey(transactionId);
            boolean isSelfApproveEnabled = selfApproveList.containsKey(transactionId);

            if ((!isCreatedByMe && !isApproveEnabled) || (isCreatedByMe && !isSelfApproveEnabled)) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            CommonUtils.addAnEntryToActedRequest(dcr, inputParams);
            dataMap.put("Transaction_id", HelperMethods.getFieldValue(fetchedTransactions, "Transaction_id"));
            dataMap.put("Status", CommonUtils.getStatusid(dcr, action));
            dataMap.put("ActedBy", CommonUtils.getUserName(dcr));

            if ("Approved".equals(action)) {
                inputParams.put("EffectiveDate", HelperMethods.getFieldValue(fetchedTransactions, "EffectiveDate"));
                Result achVendorServiceResponse = HelperMethods.callApi(dcr, inputParams, HelperMethods.getHeaders(dcr),
                        vendorService);
                String statusFromService = achVendorServiceResponse.getParamByName("Status").getValue();
                dataMap.put("Status", CommonUtils.getStatusid(dcr, statusFromService));
                dataMap.put("ReferenceID", achVendorServiceResponse.getParamByName("ReferenceID").getValue());
            }

            result = HelperMethods.callApi(dcr, dataMap, HelperMethods.getHeaders(dcr),
                    URLConstants.BB_TRANSACTION_UPDATE);

            return result;
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }

    }

}
