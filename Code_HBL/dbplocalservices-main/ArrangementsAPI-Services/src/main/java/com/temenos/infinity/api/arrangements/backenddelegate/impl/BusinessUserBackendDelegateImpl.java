package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.backenddelegate.api.BusinessUserBackendDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 * 
 * @author smugesh
 *
 */
public class BusinessUserBackendDelegateImpl implements BusinessUserBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    // Back end implementation for business user accounts
    @Override
    public List<ArrangementsDTO> getBusinessUserArrangements(ArrangementsDTO inputPayload,
            DataControllerRequest request) throws ApplicationException {
        // Set Header Map

        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));
        // To Set Admin Privileges.
        if (StringUtils.isNotBlank(request.getParameter(TemenosConstants.TRANSACTION_PERMISSION))) {
            headerMap.put(TemenosConstants.TRANSACTION_PERMISSION,
                    request.getParameter(TemenosConstants.TRANSACTION_PERMISSION));
            headerMap.put("customerId", inputPayload.getCustomerID());
            headerMap.put("type_id", inputPayload.getCustomerType());
        }

        // Get DBX Customer id to fetch favourite accounts
        String dbxCustomerId = inputPayload.getCustomerID();
        HashMap<String, String> favouriteAccounts = getFavouriteAccounts(dbxCustomerId, request);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("customerId", inputPayload.getCustomerID());
        String ArrangementResponse = null;
        JSONObject ArrResponse = null;

        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();

        try {
            ArrangementResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ArrangementsAPIServices.T24ISACCOUNTS_GETBUSINESSACCOUNTS.getServiceName())

                    .withOperationId(ArrangementsAPIServices.T24ISACCOUNTS_GETBUSINESSACCOUNTS.getOperationName())

                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Failed to fetch backend data" + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20045);
        }

        if (StringUtils.isNotBlank(ArrangementResponse)) {
            ArrResponse = new JSONObject(ArrangementResponse);
        }
        JSONArray arrangements = ArrResponse.getJSONArray("Accounts");
        for (int i = 0; i < arrangements.length(); i++) {
            ArrangementsDTO acDTO = new ArrangementsDTO();
            JSONObject arrangement = arrangements.getJSONObject(i);

            // Set Account response in DTO
            // Set Account Id
            if (arrangement.has("accountId")) {
                acDTO.setAccount_id(arrangement.getString("accountId"));
            }

            if (arrangement.has("customerId")) {
                acDTO.setCustomerID(arrangement.getString("customerId"));
            }
            // Set Product Description as account Name
            if (arrangement.has("productGroupName")) {
                acDTO.setProductGroupName(arrangement.getString("productGroupName"));
            }
            // Set Product Description as account Name
            if (arrangement.has("productGroupId")) {
                acDTO.setProductGroupId(arrangement.getString("productGroupId"));
            }
            if (arrangement.has("arrangementId")) {
                acDTO.setArrangementId(arrangement.getString("arrangementId"));
            }
            // Set Error Message
            if (arrangement.has("dbpErrCode")) {
                acDTO.setDbpErrCode(arrangement.getString("dbpErrCode"));
            }

            // Set Error Message
            if (arrangement.has("dbpErrMsg")) {
                acDTO.setDbpErrMsg(arrangement.getString("dbpErrMsg"));
            }

            // Set Account Type based on products
            if (arrangement.has("accountType")) {
                acDTO.setTypeDescription(arrangement.getString("accountType"));
            }

            // Set Account NickName
            if (arrangement.has("nickName")) {
                acDTO.setNickName(arrangement.getString("nickName"));
            }

            if (arrangement.has("limitReference")) {
                acDTO.setLimitReference(arrangement.getString("limitReference"));
            }

            if (arrangement.has("accountName")) {
                acDTO.setAccountName(arrangement.getString("accountName"));
            }

            // Set Account Balance Details
            if (arrangement.has("availableBalance")) {
                Object availableFunds = arrangement.get("availableBalance");
                if (availableFunds instanceof Integer) {
                    acDTO.setAvailableBalance(((Number) availableFunds).doubleValue());
                } else if (availableFunds instanceof String) {
                    if (StringUtils.isNotBlank(availableFunds.toString()))
                        acDTO.setAvailableBalance(Double.parseDouble((String) availableFunds));
                } else {
                    acDTO.setAvailableBalance((double) arrangement.get("availableBalance"));
                }
            } else {
                acDTO.setAvailableBalance(0.00);
            }

            if (arrangement.has("onlineActualBalance")) {
                Object onlineClearedBalance = arrangement.get("onlineActualBalance");
                if (onlineClearedBalance instanceof Integer) {
                    acDTO.setCurrentBalance(((Number) onlineClearedBalance).doubleValue());
                } else if (onlineClearedBalance instanceof String) {
                    if (StringUtils.isNotBlank(onlineClearedBalance.toString()))
                        acDTO.setCurrentBalance(Double.parseDouble((String) arrangement.get("onlineActualBalance")));
                } else {
                    acDTO.setCurrentBalance((double) arrangement.get("onlineActualBalance"));
                }

            } else {
                acDTO.setCurrentBalance(0.00);
            }
            acDTO.setOnlineActualBalance(acDTO.getCurrentBalance());

            if (arrangement.has("workingBalance")) {
                Object workingBalance = arrangement.get("workingBalance");
                if (workingBalance instanceof Integer) {
                    acDTO.setWorkingBalance(((Number) workingBalance).doubleValue());
                } else {
                    acDTO.setWorkingBalance((double) arrangement.get("workingBalance"));
                }
            } else {
                acDTO.setWorkingBalance(0.00);
            }

            if (arrangement.has("clearedBalance")) {
                Object clearedBalance = arrangement.get("clearedBalance");
                if (clearedBalance instanceof Integer) {
                    acDTO.setClearedBalance(((Number) clearedBalance).doubleValue());
                } else {
                    acDTO.setClearedBalance((double) clearedBalance);
                }
            } else {
                acDTO.setClearedBalance(0.00);
            }

            if (arrangement.has("lockedAmount")) {
                Object lockedAmount = arrangement.get("lockedAmount");
                if (lockedAmount instanceof Integer) {
                    acDTO.setLockedAmount(((Number) lockedAmount).doubleValue());
                } else {
                    acDTO.setLockedAmount((double) lockedAmount);
                }
            } else {
                acDTO.setLockedAmount(0.00);
            }

            if (arrangement.has("Taxid")) {
                acDTO.setTaxId(arrangement.getString("Taxid"));
            }

            if (arrangement.has("portfolioId")) {
                acDTO.setPortfolioId(arrangement.getString("portfolioId"));
            }

            if (arrangement.has("availableBalanceWithLimit")) {
                Object availableBalanceWithLimit = arrangement.get("availableBalanceWithLimit");
                if (availableBalanceWithLimit instanceof Double) {
                    acDTO.setAvailableBalanceWithLimit(availableBalanceWithLimit.toString()); 
                } else {
                    if ((availableBalanceWithLimit instanceof String) &&StringUtils.isNotBlank(availableBalanceWithLimit.toString()))
                        acDTO.setAvailableBalanceWithLimit(availableBalanceWithLimit.toString());
                }

            } else {
                acDTO.setAvailableBalanceWithLimit("0.00"); 
            }

            // Set Routing Number
            if (arrangement.has("sortCode")) {
                acDTO.setRoutingNumber(arrangement.getString("sortCode"));
            }

            // Set Currency Code
            if (arrangement.has("currencyCode")) {
                acDTO.setCurrencyCode(arrangement.getString("currencyCode"));
            }

            // Set Creation Date
            if (arrangement.has("openingDate")) {
                acDTO.setOpeningDate(arrangement.getString("openingDate"));
            }
            if (arrangement.has("Membership_id")) {
                acDTO.setMembership_id(arrangement.getString("Membership_id"));
            }

            // Set account IBAN
            // Set Creation Date
            if (arrangement.has("accountIBAN")) {
                acDTO.setIBAN(arrangement.getString("accountIBAN"));
            }

            // Set Primary Account Holder
            // Set Primary and secondary holders
            // Set Primary and secondary holders
            if (arrangement.has("accountHolder") && StringUtils.isNotBlank(arrangement.getString("accountHolder"))) {
                acDTO.setAccountHolder(arrangement.getString("accountHolder"));
            }

            if (arrangement.has("jointHolders") && StringUtils.isNotBlank(arrangement.getString("jointHolders"))) {
                acDTO.setJointHolders(arrangement.getString("jointHolders"));
            }
            // Set Support Flag fields
            acDTO.setSupportBillPay("1");
            acDTO.setSupportChecks("1");
            acDTO.setSupportDeposit("1");
            acDTO.setSupportTransferFrom("1");
            acDTO.setSupportTransferTo("1");
            acDTO.setIsBusinessAccount("true");

            // Set Favourite Status
            String favouriteStatus = favouriteAccounts.get(acDTO.getAccount_id()) != null
                    ? favouriteAccounts.get(acDTO.getAccount_id()) : "0";
            acDTO.setFavouriteStatus(favouriteStatus);

            // Set userName
            if (StringUtils.isNotBlank(inputPayload.getUserName())) {
                acDTO.setUserName(inputPayload.getUserName());
            }
            acDTO.setIsTypeBusiness("0");

            acDTO.setCustomerID(inputPayload.getCustomerID());

            // Add the account DTO in arrangement DTo List
            arrangementsDTO.add(acDTO);
        }
        return arrangementsDTO;
    }

    @Override
    public List<ArrangementsDTO> getBusinessUserArrangementOverview(ArrangementsDTO inputPayload,
            DataControllerRequest request) throws ApplicationException {

        // Set Input Parameters
        String accountID = inputPayload.getAccount_id();
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("accountID", accountID);

        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();
        String ArrangementResponse = null;
        JSONObject arrangement = null;

        try {
            ArrangementResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ArrangementsAPIServices.T24ISACCOUNTS_GETBUSINESSACCOUNTOVERVIEW.getServiceName())

                    .withOperationId(
                            ArrangementsAPIServices.T24ISACCOUNTS_GETBUSINESSACCOUNTOVERVIEW.getOperationName())

                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20046);
        }

        if (StringUtils.isNotBlank(ArrangementResponse)) {
            arrangement = new JSONObject(ArrangementResponse);
        }
        // JSONArray arrangements = ArrResponse.getJSONArray("body");
        ArrangementsDTO acDTO = new ArrangementsDTO();
        if (arrangement.has("accountID")) {
            acDTO.setAccount_id(arrangement.getString("accountID"));
        }
        if (arrangement.has("accountName")) {
            acDTO.setAccountName(arrangement.getString("accountName"));
        }
        // Set nextpaymentamount and nextpaymentdate
     	if (arrangement.has("nextPaymentAmount")) {
     		acDTO.setNextPaymentAmount(arrangement.getString("nextPaymentAmount"));
     	}

     	if (arrangement.has("nextPaymentDate")) {
     		acDTO.setNextPaymentDate(arrangement.getString("nextPaymentDate"));
     	}
     		
        // Set Current Balance for Account
        if (arrangement.has("availableFunds")) {
            Object availableFunds = arrangement.get("availableFunds");
            if (availableFunds instanceof Integer) {
                acDTO.setAvailableBalance(((Number) availableFunds).doubleValue());
            } else if (availableFunds instanceof String) {
                if (StringUtils.isNotBlank(availableFunds.toString()))
                    acDTO.setAvailableBalance(Double.parseDouble((String) availableFunds));
            } else {
                acDTO.setAvailableBalance((double) availableFunds);
            }
        } else {
            acDTO.setAvailableBalance(0.00);
        }
        if (arrangement.has("onlineClearedBalance")) {
            Object onlineClearedBalance = arrangement.get("onlineClearedBalance");
            if (onlineClearedBalance instanceof Integer) {
                acDTO.setCurrentBalance(((Number) onlineClearedBalance).doubleValue());
            } else if (onlineClearedBalance instanceof String) {
                acDTO.setCurrentBalance(Double.parseDouble((String) onlineClearedBalance));
            } else {
                acDTO.setCurrentBalance((double) onlineClearedBalance);
            }
        } else {
            acDTO.setCurrentBalance(0.00);
        }
        acDTO.setOnlineClearedBalance(acDTO.getCurrentBalance());

        if (arrangement.has("pendingDeposit")) {
            Object pendingDeposit = arrangement.get("pendingDeposit");
            if (pendingDeposit instanceof Integer) {
                acDTO.setPendingDeposit(((Number) pendingDeposit).doubleValue());
            } else if (pendingDeposit instanceof String) {
                acDTO.setPendingDeposit(Double.parseDouble((String) pendingDeposit));
            } else {
                acDTO.setPendingDeposit((double) pendingDeposit);
            }
        } else {
            acDTO.setPendingDeposit(0.00);
        }

        if (arrangement.has("pendingWithdrawal")) {
            Object pendingWithdrawal = arrangement.get("pendingWithdrawal");
            if (pendingWithdrawal instanceof Integer) {
                acDTO.setPendingWithdrawal(((Number) pendingWithdrawal).doubleValue());
            } else if (pendingWithdrawal instanceof String) {
                acDTO.setPendingWithdrawal(Double.parseDouble((String) pendingWithdrawal));
            } else {
                acDTO.setPendingWithdrawal((double) pendingWithdrawal);
            }
        } else {
            acDTO.setPendingWithdrawal(0.00);
        }

        if (arrangement.has("dividendLastPaidAmount")) {
            Object dividendLastPaidAmount = arrangement.get("dividendLastPaidAmount");
            if (dividendLastPaidAmount instanceof Integer) {
                acDTO.setDividendLastPaidAmount(((Number) dividendLastPaidAmount).doubleValue());
            } else if (dividendLastPaidAmount instanceof String) {
                acDTO.setDividendLastPaidAmount(Double.parseDouble((String) arrangement.get("dividendLastPaidAmount")));
            } else {
                acDTO.setDividendLastPaidAmount((double) arrangement.get("dividendLastPaidAmount"));
            }
        } else {
            acDTO.setDividendLastPaidAmount(0.00);
        }
        // Set Dividend Last Paid Date
        if (arrangement.has("dividendLastPaidDate")) {
            acDTO.setDividendLastPaidDate(arrangement.getString("dividendLastPaidDate"));
        }
        if (arrangement.has("dividentPaidYtd")) {
            Object dividendPaidYTD = arrangement.get("dividentPaidYtd");
            if (dividendPaidYTD instanceof Integer) {
                acDTO.setDividendPaidYTD(((Number) dividendPaidYTD).doubleValue());
            } else if (dividendPaidYTD instanceof String) {
                acDTO.setDividendPaidYTD(Double.parseDouble((String) arrangement.get("dividentPaidYtd")));
            } else {
                acDTO.setDividendPaidYTD((double) arrangement.get("dividendPaidYTD"));
            }
        } else {
            acDTO.setDividendPaidYTD(0.00);
        }

        if (arrangement.has("outstandingOverdraftLimit")) {
            Object outstandingOverdraftLimit = arrangement.get("outstandingOverdraftLimit");
            if (outstandingOverdraftLimit instanceof Integer) {
                acDTO.setOutstandingOverdraftLimit(((Number) outstandingOverdraftLimit).doubleValue());
            } else if (outstandingOverdraftLimit instanceof String) {
                acDTO.setOutstandingOverdraftLimit(
                        Double.parseDouble((String) arrangement.get("outstandingOverdraftLimit")));
            } else {
                acDTO.setOutstandingOverdraftLimit((double) arrangement.get("outstandingOverdraftLimit"));
            }
        } else {
            acDTO.setOutstandingOverdraftLimit(0.00);
        }

        // Set Dividend Rate
        if (arrangement.has("dividendRate")) {
            float dividendRate = Float.parseFloat((String) arrangement.get("dividendRate"));
            acDTO.setDividendRate(dividendRate);
        }

        // Set Last Paid Date
        if (arrangement.has("lastPaymentDate")) {
            acDTO.setLastPaymentDate(arrangement.getString("lastPaymentDate"));
        }

        // Set Swift code and routing number
        if (arrangement.has("swiftCode")) {
            acDTO.setSwiftCode(arrangement.getString("swiftCode"));
        }

        if (arrangement.has("routingNumber")) {
            acDTO.setRoutingNumber(arrangement.getString("routingNumber"));
        }

        if (arrangement.has("totalCreditMonths")) {
            acDTO.setTotalCreditMonths((String) arrangement.getString("totalCreditMonths"));
        }

        if (arrangement.has("openingDate")) {
            acDTO.setOpeningDate(arrangement.getString("openingDate"));
        }

        if (arrangement.has("totalDebitsMonth")) {
            acDTO.setTotalDebitsMonth((String) arrangement.getString("totalDebitsMonth"));
        }

        if (arrangement.has("interestEarned")) {
            Object interestEarned = arrangement.get("interestEarned");
            if (interestEarned instanceof Integer) {
                acDTO.setInterestEarned(((Number) interestEarned).doubleValue());
            } else if (interestEarned instanceof String) {
                acDTO.setInterestEarned(Double.parseDouble((String) arrangement.get("interestEarned")));
            } else {
                acDTO.setInterestEarned((double) arrangement.get("interestEarned"));
            }

        } else {
            acDTO.setInterestEarned(0.00);
        }
        if (arrangement.has("originalAmount")) {
            acDTO.setOriginalAmount(arrangement.getString("originalAmount"));
        }

        if (arrangement.has("paymentTerm")) {
            acDTO.setPaymentTerm(arrangement.getString("paymentTerm"));
        }

        if (arrangement.has("maturityOption")) {
            acDTO.setMaturityOption(arrangement.getString("maturityOption"));
        }

        if (arrangement.has("maturityAmount")) {
            acDTO.setMaturityAmount(arrangement.getString("maturityAmount"));
        }

        if (arrangement.has("currentAmountDue")) {
            acDTO.setCurrentAmountDue(arrangement.getString("currentAmountDue"));
        }

        if (arrangement.has("paymentDue")) {
            acDTO.setPaymentDue(arrangement.getString("paymentDue"));
        }

        // Set Account NickName
        if (arrangement.has("nickName")) {
            acDTO.setNickName(arrangement.getString("nickName"));
        }

        // Set Currency Code
        if (arrangement.has("currencyCode")) {
            acDTO.setCurrencyCode(arrangement.getString("currencyCode"));
        }

        // Set Account Type based on products
        if (arrangement.has("accountType")) {
            acDTO.setTypeDescription(arrangement.getString("accountType"));
        }

        if (arrangement.has("payoffAmount")) {
            acDTO.setPayoffAmount(arrangement.getString("payoffAmount"));
        }

        if (arrangement.has("outstandingBalance")) {
            acDTO.setOutstandingBalance(arrangement.getString("outstandingBalance"));
        }
        if (arrangement.has("principalBalance")) {
            acDTO.setPrincipalBalance(arrangement.getString("principalBalance"));
        }

        // Set Primary and secondary holders
        if (arrangement.has("accountHolder") && StringUtils.isNotBlank(arrangement.getString("accountHolder"))) {
            acDTO.setAccountHolder(arrangement.getString("accountHolder"));
        }

        if (arrangement.has("jointHolders") && StringUtils.isNotBlank(arrangement.getString("jointHolders"))) {
            acDTO.setJointHolders(arrangement.getString("jointHolders"));
        }

        if (arrangement.has("IBAN") && StringUtils.isNotBlank(arrangement.getString("IBAN"))) {
            acDTO.setIBAN(arrangement.getString("IBAN"));
        }
        
        if(arrangement.has("blockedAmount")) {
        	Object blockedAmount = arrangement.get("blockedAmount");
            if (blockedAmount instanceof String &&  StringUtils.isNotBlank(arrangement.getString("blockedAmount"))) {
                acDTO.setBlockedAmount(((String) blockedAmount));
            } else if(blockedAmount instanceof Integer){
                acDTO.setBlockedAmount(String.valueOf(arrangement.get("blockedAmount")));
            }
            else if(blockedAmount instanceof Double){
                acDTO.setBlockedAmount(String.valueOf(arrangement.get("blockedAmount")));
            }
        }

        if (arrangement.has("transferLimit") && StringUtils.isNotBlank(arrangement.getString("transferLimit"))) {
            acDTO.setTransferLimit(arrangement.getString("transferLimit"));
        }
        if (arrangement.has("paidInstallmentsCount")
                && StringUtils.isNotBlank(arrangement.getString("paidInstallmentsCount"))) {
            acDTO.setPaidInstallmentsCount(arrangement.getString("paidInstallmentsCount"));
        }
        if (arrangement.has("overDueInstallmentsCount")
                && StringUtils.isNotBlank(arrangement.getString("overDueInstallmentsCount"))) {
            acDTO.setOverDueInstallmentsCount(arrangement.getString("overDueInstallmentsCount"));
        }
        if (arrangement.has("futureInstallmentsCount")
                && StringUtils.isNotBlank(arrangement.getString("futureInstallmentsCount"))) {
            acDTO.setFutureInstallmentsCount(arrangement.getString("futureInstallmentsCount"));
        }

        // Set Support Flag fields
        acDTO.setSupportBillPay("1");
        acDTO.setSupportChecks("1");
        acDTO.setSupportDeposit("1");
        acDTO.setSupportTransferFrom("1");
        acDTO.setSupportTransferTo("1");
        acDTO.setIsBusinessAccount("true");
        arrangementsDTO.add(acDTO);

        return arrangementsDTO;
    }

    @Override
    public List<ArrangementsDTO> getBusinessUserArrangementPreview(ArrangementsDTO inputPayloadDTO,
            DataControllerRequest request) throws ApplicationException {

        List<ArrangementsDTO> listDTO = new ArrayList<ArrangementsDTO>();
        StringBuilder sb = new StringBuilder();
        String ArrangementResponse = null;
        sb.append("Customer_id").append(" eq ").append(inputPayloadDTO.getCustomerID()).append(" and ")
                .append("Device_id").append(" eq ").append(inputPayloadDTO.getDeviceId()).append(" and ")
                .append("Status_id").append(" eq ").append("SID_DEVICE_REGISTERED");
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        inputMap.put("$filter", sb.toString());
        request.addRequestParam_("$filter", sb.toString());
        try {
            ArrangementResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ArrangementsAPIServices.DBXUSER_GET_ACCOUNTSOVERVIEW.getServiceName())

                    .withOperationId(ArrangementsAPIServices.DBXUSER_GET_ACCOUNTSOVERVIEW.getOperationName())

                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20046);
        }

        if (StringUtils.isNotBlank(ArrangementResponse)) {
            JSONArray responseArray = new JSONArray();
            JSONObject accountPreviewJSONObject = new JSONObject(ArrangementResponse);
            if (accountPreviewJSONObject.has("customer_device_information_view")) {
                responseArray = accountPreviewJSONObject.getJSONArray("customer_device_information_view");
            }
            for (int index = 0; index < responseArray.length(); index++) {
                ArrangementsDTO arrangementsDTO = new ArrangementsDTO();
                JSONObject jsonObject = (JSONObject) responseArray.get(index);
                String channelDescription = (String) jsonObject.get("Channel_Description");
                if (channelDescription != null) {
                    arrangementsDTO.setChannelDescription(channelDescription);
                }
                listDTO.add(arrangementsDTO);
            }

        }
        return listDTO;
    }

    @Override
    public List<ArrangementsDTO> getUserDetailsFromDBX(ArrangementsDTO inputPayloadDTO, DataControllerRequest request)
            throws ApplicationException {
        List<ArrangementsDTO> listDTO = new ArrayList<ArrangementsDTO>();
        StringBuilder sb = new StringBuilder();
        String ArrangementResponse = null;
        sb.append("UserName").append(" eq ").append(inputPayloadDTO.getUserName());
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        inputMap.put("$filter", sb.toString());
        request.addRequestParam_("$filter", sb.toString());
        try {
            ArrangementResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ArrangementsAPIServices.DBXUSER_GET_USERDETAILS.getServiceName())

                    .withOperationId(ArrangementsAPIServices.DBXUSER_GET_USERDETAILS.getOperationName())

                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20046);
        }
        String customerId = null;
        String BackendIdResponse = null;
        ArrangementsDTO arrangementsDTO = new ArrangementsDTO();
        if (StringUtils.isNotBlank(ArrangementResponse)) {
            JSONArray responseArray = new JSONArray();
            JSONObject accountPreviewJSONObject = new JSONObject(ArrangementResponse);
            if (accountPreviewJSONObject.has("customer")) {
                responseArray = accountPreviewJSONObject.getJSONArray("customer");
                JSONObject jsonObject = (JSONObject) responseArray.get(0);
                customerId = (String) jsonObject.get("id");
                String customerType = (String) jsonObject.get("CustomerType_id");
                if (customerId != null) {
                    arrangementsDTO.setCustomerID(customerId);
                    arrangementsDTO.setCustomerType(customerType);
                }
            }
            BackendIdResponse = getBackendIDFromDBX(arrangementsDTO.getCustomerID(), request);
            if (StringUtils.isNotBlank(BackendIdResponse)) {
                responseArray = new JSONArray();
                accountPreviewJSONObject = new JSONObject(BackendIdResponse);
                if (accountPreviewJSONObject.has("backendidentifier")) {
                    responseArray = accountPreviewJSONObject.getJSONArray("backendidentifier");
                }
                JSONObject jsonObject = (JSONObject) responseArray.get(0);
                String backendId = (String) jsonObject.get("BackendId");
                if (backendId != null) {
                    arrangementsDTO.setBackendUserId(backendId);
                }
            }
            listDTO.add(arrangementsDTO);
        }
        return listDTO;
    }

    private String getBackendIDFromDBX(String customerId, DataControllerRequest request) throws ApplicationException {
        StringBuilder s = new StringBuilder();
        String BackendIdentifiers = "";
        s.append("Customer_id").append(" eq ").append(customerId).append(" and ").append("BackendType").append(" eq ")
                .append("T24");
        Map<String, Object> input = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        input.put("$filter", s.toString());
        request.addRequestParam_("$filter", s.toString());
        try {
            BackendIdentifiers = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ArrangementsAPIServices.DBXUSER_GET_BACKENDIDENTIFIERDETAILS.getServiceName())

                    .withOperationId(ArrangementsAPIServices.DBXUSER_GET_BACKENDIDENTIFIERDETAILS.getOperationName())

                    .withRequestParameters(input).withRequestHeaders(headers).withDataControllerRequest(request).build()
                    .getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20046);
        }
        return BackendIdentifiers;
    }

    public HashMap<String, String> getFavouriteAccounts(String dbxCustomerId, DataControllerRequest request)
            throws ApplicationException {

        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        HashMap<String, Object> headerParams = new HashMap<String, Object>();
        inputParams.put("$filter", "User_id eq " + dbxCustomerId);
        request.addRequestParam_("$filter", "User_id eq " + dbxCustomerId);

        String DBXCusAccDetails = "";

        try {
            DBXCusAccDetails = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ArrangementsAPIServices.DBXUSER_GET_DBXCUSTOMERACCOUNTDETAILS.getServiceName())

                    .withOperationId(ArrangementsAPIServices.DBXUSER_GET_DBXCUSTOMERACCOUNTDETAILS.getOperationName())

                    .withRequestParameters(inputParams).withRequestHeaders(headerParams)
                    .withDataControllerRequest(request).build().getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20046);
        }
        Result dbxResult = new Result();
        if (StringUtils.isNotBlank(DBXCusAccDetails)) {
            dbxResult = JSONToResult.convert(new JSONObject(DBXCusAccDetails).toString());
        }

        Dataset getAccountsDS = dbxResult.getDatasetById(TemenosConstants.DS_DB_ACCOUNTS);
        HashMap<String, String> favouriteAccounts = new HashMap<String, String>();

        if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
            alert.prepareError("if true").log();
            for (Record rec : getAccountsDS.getAllRecords()) {
                favouriteAccounts.put(rec.getParamValueByName(TemenosConstants.DB_ACCOUNTID),
                        rec.getParamValueByName(TemenosConstants.DB_FAVOURITESTATUS));
            }
        }
        return favouriteAccounts;
    }

}
