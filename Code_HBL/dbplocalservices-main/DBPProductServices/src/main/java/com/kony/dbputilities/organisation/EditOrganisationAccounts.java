package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class EditOrganisationAccounts {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private EditOrganisationAccounts() {

    }

    public static Result invoke(Map<String, String> inputParams, List<HashMap<String, String>> removedList,
            DataControllerRequest dcRequest) {
        Result result = new Result();
        String id = inputParams.get("id");
        String[] accountIds = new String[0];
        if (StringUtils.isBlank(id)) {
            id = dcRequest.getParameter("id");
        }

        Set<String> accountSet = new HashSet<>();
        for (HashMap<String, String> map : removedList) {
            String accountId = map.get("Account_id");
            accountSet.add(accountId);
            map.put("Account_id", accountId);
            map.put("Organization_id", "");

            try {
                result = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNTS_UPDATE);
            } catch (HttpCallException e) {

            }
            if (HelperMethods.hasError(result)) {
                return result;
            }
        }
        try {
            ApprovalMatrixBusinessDelegate approvalmatrixDelegate =
                    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                            .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
            accountIds = convertHashsetToStringArray(accountSet);
            approvalmatrixDelegate.deleteApprovalMatrixEntry(id, "", accountSet, "accountId");
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            alert.prepareError("Error occured while deleting the default approval matrix").log();
        }

        String accountsCSV = String.join(",", accountIds);
        Map<String, String> input = new HashMap<>();
        input.put("_accounts", accountsCSV);
        input.put("_organisationId", id);

        try {
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATION_CUSTOMERACCOUNTS_DELETE_PROC);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        return result;
    }

    private static String[] convertHashsetToStringArray(Set<String> hashSet) {
        String[] actionsList = new String[hashSet.size()];
        hashSet.toArray(actionsList);
        return actionsList;

    }

    static String getMembershipID(String orgId, DataControllerRequest dcRequest) throws HttpCallException {
        Result result = new Result();
        if (StringUtils.isNotBlank(orgId)) {
            String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATIONMEMBERSHIP_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "Membership_id");
        }
        return "";
    }

    static String getTaxID(String orgId, DataControllerRequest dcRequest) throws HttpCallException {
        Result result = new Result();
        if (StringUtils.isNotBlank(orgId)) {
            String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATIONMEMBERSHIP_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "Taxid");
        }
        return "";
    }
}
