package com.temenos.infinity.api.srmstransactions.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.srmstransactions.backenddelegate.api.SrmsTransactionsAPIBackendDelegate;
import com.temenos.infinity.api.srmstransactions.config.SrmsTransactionsAPIServices;
import com.temenos.infinity.api.srmstransactions.constants.ErrorCodeEnum;
import com.temenos.infinity.api.srmstransactions.dto.SRMSTransactionDTO;
import com.temenos.infinity.api.srmstransactions.dto.SessionMap;
import com.temenos.infinity.api.srmstransactions.resource.impl.SrmsTransactionsResourceImpl;
import com.temenos.infinity.api.srmstransactions.utils.MemoryManagerUtils;

public class SrmsTransactionsAPIBackendDelegateImpl implements SrmsTransactionsAPIBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    final String INTERNAL_BANK_ACCOUNTS = "INTERNAL_BANK_ACCOUNTS";
    final String PARAM_CUSTOMER_ID = "customer_id";

    @Override
    public List<SRMSTransactionDTO> getOneTimeTransactions(SRMSTransactionDTO inputDTO, DataControllerRequest request)
            throws ApplicationException {
        // TODO Auto-generated method stub
        List<SRMSTransactionDTO> oneTimeTransfers = new ArrayList<>();

        String accountID = inputDTO.getAccountId() != null ? inputDTO.getAccountId() : "";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("accountId", accountID);
        inputMap.put("type", inputDTO.getType());
        inputMap.put("dateFrom", inputDTO.getDateFrom());
        inputMap.put("dateTo", inputDTO.getDateTo());
        inputMap.put("pageSize", inputDTO.getLastRecordNumber());
        diagnostic.prepareDebug("SRMS Get Transactions Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        String Response = null;
        JSONObject srmsResponse = new JSONObject();
        try {
            Response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(
                            SrmsTransactionsAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILSBYCATEGORY.getServiceName())
                    .withOperationId(
                            SrmsTransactionsAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILSBYCATEGORY.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Exception Occurred at get SRMS transactions " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_32003);
        }

        if (StringUtils.isNotBlank(Response)) {
            srmsResponse = new JSONObject(Response);
            diagnostic.prepareDebug("SRMS Get Transactions Response " + Response).log();
        }

        // Process SRMS Response
        if (srmsResponse.has("serviceReqs")) {
            JSONArray oneTimeTransactions = srmsResponse.getJSONArray("serviceReqs");
            // Get Customer Id
            String customerId = "";
            try {
                customerId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
                        .get(PARAM_CUSTOMER_ID);
            } catch (Exception e) {
                alert.prepareError("Unable to fetch the customer id from session" + e).log();
            }
            JSONObject intAccounts = getInternalBankAccountsFromSession(customerId);
            if (oneTimeTransactions != null && oneTimeTransactions.length() > 0) {
                for (Object oneTimeTxn : oneTimeTransactions) {
                    JSONObject transaction = new JSONObject(oneTimeTxn.toString());
                    oneTimeTransfers.add(processSRMSResponse(transaction, intAccounts));
                }
            }
        }

        return oneTimeTransfers;
    }

    public SRMSTransactionDTO processSRMSResponse(JSONObject transaction, JSONObject intAccounts) {
        SRMSTransactionDTO srmsTransaction = new SRMSTransactionDTO();
        String chargeBearer = "";
        // Set Params from Request object
        if (transaction.has("serviceReqRequestIn")
                && StringUtils.isNotBlank(transaction.get("serviceReqRequestIn").toString())) {
            JSONObject inputPayload = transaction.getJSONObject("serviceReqRequestIn");
            // Parse Request to DTO
            try {
                srmsTransaction = JSONUtils.parse(inputPayload.toString(), SRMSTransactionDTO.class);
            } catch (IOException e) {
                alert.prepareError("Unable to parse request input to srms transaction DTO" + e).log();
            }

            srmsTransaction.setChargeBearer(chargeBearer);
            srmsTransaction.setFromAccountName(getAccountName(intAccounts, srmsTransaction.getFromAccountNumber()));
            if (StringUtils.isNotBlank(srmsTransaction.getServiceName())
                    && srmsTransaction.getServiceName().equalsIgnoreCase("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE")) {
                srmsTransaction.setToAccountName(getAccountName(intAccounts, srmsTransaction.getToAccountNumber()));
            }
            if (StringUtils.isNotBlank(srmsTransaction.getIsScheduled())) {
                String isScheduled = srmsTransaction.getIsScheduled();
                if (isScheduled.equalsIgnoreCase("0") || isScheduled.equalsIgnoreCase("false"))
                    srmsTransaction.setIsScheduled("false");
                else
                    srmsTransaction.setIsScheduled("true");
            }
            if (StringUtils.isNotBlank(srmsTransaction.getNumberOfAuthorisers())) {
                srmsTransaction.setPendingApproval(true);
            }
            if (StringUtils.isNotBlank(srmsTransaction.getPersonId())) {
                srmsTransaction.setToAccountNumber(srmsTransaction.getPersonId());
            }
            if (StringUtils.isNotBlank(srmsTransaction.getBeneficiaryName())) {
                srmsTransaction.setToAccountName(srmsTransaction.getBeneficiaryName());
            }
            if (StringUtils.isNotBlank(srmsTransaction.getTransactionCurrency())) {
                srmsTransaction.setPaymentCurrencyId(srmsTransaction.getTransactionCurrency());
            }
            if (inputPayload.has("userId")) {
                srmsTransaction.setOrderingCustomerId(inputPayload.getString("userId"));
            }
            if (inputPayload.has("e2eReference")) {
                srmsTransaction.setEndToEndReference(inputPayload.getString("e2eReference"));
            }
            if (inputPayload.has("transactionsNotes")) {
                srmsTransaction.setDescription(inputPayload.getString("transactionsNotes"));
            }
            if (StringUtils.isBlank(chargeBearer) && inputPayload.has("paidBy")) {
                chargeBearer = inputPayload.getString("paidBy");
            }
            // Set Params from Response object
            if (transaction.has("responseAttributes")
                    && StringUtils.isNotBlank(transaction.get("responseAttributes").toString())) {
                JSONObject outputPayload = transaction.getJSONObject("responseAttributes");
                if (outputPayload.has("chargeBearer")) {
                    chargeBearer = outputPayload.getString("chargeBearer");
                }
                if (outputPayload.has("charges")) {
                    srmsTransaction.setCharges(outputPayload.getString("charges"));
                }
                if (outputPayload.has("referenceId")) {
                    srmsTransaction.setReferenceId(outputPayload.getString("referenceId"));
                }
            }
        }

        if (transaction.has("serviceReqStatus")) { // Set Current status
            srmsTransaction.setCurrentStatus(transaction.getString("serviceReqStatus"));
        }

        if (transaction.has("serviceReqId")) { // Set Transaction Id
            srmsTransaction.setTransactionId(transaction.getString("serviceReqId"));
        }

        if (srmsTransaction.getCurrentStatus().equalsIgnoreCase("Request Initiated")
                || srmsTransaction.getCurrentStatus().equalsIgnoreCase("Request Placed"))
            srmsTransaction.setStatusDescription("Pending");
        else
            srmsTransaction.setStatusDescription("Failed");

        return srmsTransaction;
    }

    @Override
    public List<SRMSTransactionDTO> getOneTimeTransactionById(SRMSTransactionDTO inputDTO,
            DataControllerRequest request) throws ApplicationException {
        List<SRMSTransactionDTO> oneTimeTransfers = new ArrayList<>();

        String transactionId = inputDTO.getTransactionId() != null ? inputDTO.getTransactionId() : "";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("serviceRequestIds", transactionId);
        diagnostic.prepareDebug("SRMS Get Transactions Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        String Response = null;
        JSONObject srmsResponse = new JSONObject();
        try {
            Response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(
                            SrmsTransactionsAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILSBYID.getServiceName())
                    .withOperationId(
                            SrmsTransactionsAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILSBYID.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Exception Occurred at get SRMS transactions " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_32003);
        }

        if (StringUtils.isNotBlank(Response)) {
            srmsResponse = new JSONObject(Response);
            diagnostic.prepareDebug("SRMS Get Transactions Response " + Response).log();
        }

        // Process SRMS Response
        if (srmsResponse.has("serviceReqs")) {
            JSONArray oneTimeTransactions = srmsResponse.getJSONArray("serviceReqs");
            // Get Customer Id
            String customerId = "";
            try {
                customerId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
                        .get(PARAM_CUSTOMER_ID);
            } catch (Exception e) {
                alert.prepareError("Unable to fetch the customer id from session" + e).log();
            }
            JSONObject intAccounts = getInternalBankAccountsFromSession(customerId);
            if (oneTimeTransactions != null && oneTimeTransactions.length() > 0) {
                for (Object oneTimeTxn : oneTimeTransactions) {
                    JSONObject transaction = new JSONObject(oneTimeTxn.toString());
                    oneTimeTransfers.add(processSRMSResponse(transaction, intAccounts));
                }
            }
        }

        return oneTimeTransfers;
    }
    
    public JSONObject getInternalBankAccountsFromSession(String customerId) {
        SessionMap internalAccntsMap = (SessionMap) MemoryManagerUtils.retrieve(INTERNAL_BANK_ACCOUNTS + customerId);
        if (StringUtils.isNotBlank(internalAccntsMap.toString()))
            return new JSONObject(internalAccntsMap.toString());
        else
            return new JSONObject();
    }
    public JSONObject getAccountsFromCache(String key) {
        Object internalAccntsMap = MemoryManagerUtils.getFromCache("ACCOUNTS");
        if (StringUtils.isNotBlank(internalAccntsMap.toString()))
            return new JSONObject(internalAccntsMap.toString());
        else
            return new JSONObject();
    }
    
    
    
    public String getAccountName(JSONObject internalAccounts, String accountId) {
        String accountName = "";
        if (internalAccounts.has(accountId)) {
            JSONObject account = new JSONObject(internalAccounts.get(accountId).toString());
            if (account.has("accountName")) {
                return account.getString("accountName");
            }
        }
        return accountName;
    }
}