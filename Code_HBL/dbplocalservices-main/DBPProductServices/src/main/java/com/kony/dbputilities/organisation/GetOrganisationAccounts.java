package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetOrganisationAccounts implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_GET);
            postProcess(result, dcRequest);
        }

        return result;
    }

    private Result postProcess(Result result, DataControllerRequest dcRequest) {

        Map<String, String> accounttypeNames = HelperMethods.getAccountsNames();
        if (HelperMethods.hasRecords(result)) {
            result.getAllDatasets().get(0).setId("OgranizationAccounts");
            List<Record> records = result.getAllDatasets().get(0).getAllRecords();
            for (Record record : records) {
                record.addParam(new Param("Organization_Id", HelperMethods.getFieldValue(record, "Organization_id"),
                        "String"));
                record.addParam(new Param("account_id", HelperMethods.getFieldValue(record, "Account_id"), "String"));
                record.addParam(new Param("accountName", HelperMethods.getFieldValue(record, "AccountName"), "String"));
                record.addParam(new Param("accountType",
                        accounttypeNames.get(HelperMethods.getFieldValue(record, "Type_id")), "String"));

                if (StringUtils.isNotBlank(URLFinder.getPathUrl(DBPUtilitiesConstants.ARRANGEMENT_HOST_URL_RUNTIME_KEY,
                        dcRequest))) {
                    Result accountResult = null;
                    Map<String, String> inputParams = new HashMap<>();
                    inputParams.put("Account_id", HelperMethods.getFieldValue(record, "arrangementId"));
                    try {
                        accountResult = HelperMethods.callApi(dcRequest, inputParams,
                                HelperMethods.getHeaders(dcRequest), URLConstants.GETALLACCOUNTS_OBJECTSERVICE_POST);
                    } catch (HttpCallException e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                    if (HelperMethods.hasRecords(accountResult)) {
                        Record account = accountResult.getAllDatasets().get(0).getRecord(0);
                        for (int i = 0; i < account.getAllParams().size(); i++) {
                            record.addParam(account.getAllParams().get(i));

                        }

                    }

                }
            }

            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.ACCOUNTS_EXISTS_IN_DBX, result);
        } else if (HelperMethods.hasError(result)) {
            ErrorCodeEnum.ERR_11022.setErrorCode(result, HelperMethods.getError(result));
        } else {
            result.getAllDatasets().get(0).setId("OgranizationAccounts");
            ErrorCodeEnum.ERR_11023.setErrorCode(result);
        }

        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        String orgId = HelperMethods.getOrganizationIDfromLoggedInUser(dcRequest);

        if (StringUtils.isBlank(orgId) && StringUtils.isBlank(inputParams.get("Organization_Id"))) {
            ErrorCodeEnum.ERR_11021.setErrorCode(result);
            return false;
        }

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);

        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
            // if (!userPermissions.contains("USER_MANAGEMENT")) {
            // ErrorCodeEnum.ERR_11020.setErrorCode(result);
            // return false;
            // }
            inputParams.put("Organization_id", orgId);
        }

        String filter = "";
        if (StringUtils.isNotBlank(orgId)) {

            inputParams.put("Organization_id", orgId);
            String[] list = { "Organization_id" };

            filter = "";

            for (int i = 0; i < list.length; i++) {
                String filterKey = list[i];
                String filtervalue = inputParams.get(filterKey);
                if (StringUtils.isNotBlank(filtervalue)) {
                    if (!filter.isEmpty()) {
                        filter += DBPUtilitiesConstants.AND;
                    }

                    filter += filterKey + DBPUtilitiesConstants.EQUAL + filtervalue;
                }
            }
        } else {
            filter = "";
            filter += "Organization_id" + DBPUtilitiesConstants.EQUAL + inputParams.get("Organization_Id");
        }

        if (StringUtils.isBlank(filter)) {
            ErrorCodeEnum.ERR_11021.setErrorCode(result);
            return false;
        }

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        return true;
    }
}
