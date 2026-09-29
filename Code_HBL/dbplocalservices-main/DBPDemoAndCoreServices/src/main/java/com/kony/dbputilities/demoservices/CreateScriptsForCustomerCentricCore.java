package com.kony.dbputilities.demoservices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateScriptsForCustomerCentricCore implements JavaService2 {

    SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);
        final String INPUT_MEMBERSHIP = "Membership";
        final String INPUT_MEMBERSHIPOWNERS = "MembershipOwners";
        String membership =
                inputMap.containsKey(INPUT_MEMBERSHIP) ? inputMap.get(INPUT_MEMBERSHIP)
                        : request.getParameter(INPUT_MEMBERSHIP);
        String membershipOwner = inputMap.containsKey(INPUT_MEMBERSHIPOWNERS) ? inputMap.get(INPUT_MEMBERSHIPOWNERS)
                : request.getParameter(INPUT_MEMBERSHIPOWNERS);

        List<HashMap<String, String>> membershipList = HelperMethods.getAllRecordsMap(membership);
        List<HashMap<String, String>> membershipOwnerList = HelperMethods.getAllRecordsMap(membershipOwner);

        Result membershipResult = createMembership(membershipList, request);
        String id = HelperMethods.getFieldValue(membershipResult, "id");
        String taxId = HelperMethods.getFieldValue(membershipResult, "taxId");
        String name = HelperMethods.getFieldValue(membershipResult, "name");

        createMembershipOwner(id, membershipOwnerList, request);
        Result returnResult = createMembershipAccounts(id, request);
        returnResult.addParam(new Param("companyId", id, DBPUtilitiesConstants.STRING_TYPE));
        returnResult.addParam(new Param("companyName", name, DBPUtilitiesConstants.STRING_TYPE));
        returnResult.addParam(new Param("companyTaxId", taxId, DBPUtilitiesConstants.STRING_TYPE));
        return returnResult;
    }

    private Result createMembershipAccounts(String id, DataControllerRequest dcRequest) {
        StringBuilder sb = new StringBuilder();
        Result result = new Result();

        for (int i = 0; i < 3; i++) {

            Map<String, String> input = new HashMap<>();
            String name = "admin";
            if (i == 1) {
                name = "williams";
            }
            if (i == 2) {
                name = "admin";
            }
            if (i == 3) {
                name = "john";
            }
            Calendar cal = Calendar.getInstance();
            String accountNum = idFormatter.format(new Date());
            cal.add(Calendar.DATE, -1);
            input.put("Account_id", accountNum);
            input.put("AccountName", "Savings");
            input.put("Type_id", "2");
            input.put("Product_id", "8");
            input.put("Bank_id", "1");
            input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
            String accHolderName = "{\"username\": \"" + name + "\", \"fullname\": \""
                    + name + "\"}";
            input.put("AccountHolder", accHolderName);
            input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
            input.put("EStatementmentEnable", "false");
            input.put("ownership", "Single");

            HelperMethods.removeNullValues(input);
            try {
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.DMS_ACCOUNTS_CREATE);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            sb.append(accountNum);
            if ((i + 1) != 3)
                sb.append(",");

            input.clear();

            input.put("id", UUID.randomUUID().toString());
            input.put("membershipId", id);
            input.put("accountId", accountNum);

            try {
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            input.clear();

        }
        result.addStringParam("Accounts", sb.toString());
        return result;

    }

    private void createMembershipOwner(String id, List<HashMap<String, String>> membershipOwnerList,
            DataControllerRequest dcRequest) {
        for (HashMap<String, String> map : membershipOwnerList) {
            map.put("id", idFormatter.format(new Date()));
            map.put("membershipId", id);
            try {
                HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                        URLConstants.MEMBERSHIPOWNER_CREATE);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

    }

    private Result createMembership(List<HashMap<String, String>> membershipList, DataControllerRequest dcRequest) {

        Map<String, String> membershipInput = membershipList.get(0);
        membershipInput.put("id", idFormatter.format(new Date()));
        membershipInput.put("addressId", "ADDR1");
        membershipInput.put("taxId", idFormatter.format(new Date()));
        if (StringUtils.isBlank(membershipInput.get("name"))) {
            membershipInput.put("name", "org" + idFormatter.format(new Date()));
        }

        try {
            return HelperMethods.callApi(dcRequest, membershipInput, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MEMBERSHIP_CREATE);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        return new Result();
    }

}
