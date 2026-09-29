package com.temenos.infinity.api.chequemanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.GetStopPaymentBackendDelegate;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.chequemanagement.dto.Account;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementProperties;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementUtilities;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.stoppayments.StopCheckPaymentUtils;
import com.temenos.infinity.api.stoppayments.StopPaymentConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetStopPaymentBackendDelegateImpl implements GetStopPaymentBackendDelegate, ChequeManagementConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<StopPayment> getStopPaymentOrdersFromOMS(StopPayment StopPaymentInput, DataControllerRequest request)
            throws ApplicationException {
        List<StopPayment> stopPaymentOrders = new ArrayList<>();

        // Load Check Book Request properties
        Properties props = ChequeManagementProperties.loadProps(PARAM_PROPERTY);

        String accountID = StopPaymentInput.getAccountID() != "" ? StopPaymentInput.getAccountID() : "";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("accountId", accountID);
        inputMap.put("type", props.getProperty("stopPaymentsType"));
        inputMap.put("subType", props.getProperty("stopPaymentsSubType"));
        inputMap.put("operation", "get");
        alert.prepareError("OMS Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        String stopPaymentResponse = null;
        JSONObject Response = new JSONObject();
        try {
            stopPaymentResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId("dbpChequeManagementServices")
                    .withOperationId("GetServiceRequestOperation")
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Unable to get stop payment request orders " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHK_26013);
        }

        if (StringUtils.isNotBlank(stopPaymentResponse)) {
            Response = new JSONObject(stopPaymentResponse);
            alert.prepareError("OMS Response " + stopPaymentResponse).log();
        }
        JSONArray Orders = Response.getJSONArray(PARAM_SERVICE_REQUESTS);
        for (int i = 0; i < Orders.length(); i++) {
            StopPayment order = new StopPayment();
            JSONObject singleOrder = Orders.getJSONObject(i);

            // Set Transaction Id
            String externalId = singleOrder.has(PARAM_EXTERNAL_REFERENCE)
                    ? singleOrder.getString(PARAM_EXTERNAL_REFERENCE) : "";
            String orderId = singleOrder.has(PARAM_SERVICE_REQ_ID) ? singleOrder.getString(PARAM_SERVICE_REQ_ID) : "";
            order.setId(orderId);

            // Set Transaction Date
            if (singleOrder.has(PARAM_ORDER_PROCESSED_TIME)
                    && StringUtils.isNotBlank(singleOrder.getString(PARAM_ORDER_PROCESSED_TIME)))
                order.setTransactionDate(singleOrder.getString(PARAM_ORDER_PROCESSED_TIME).split("T")[0]);

            // Set Status
            if (singleOrder.has(PARAM_SERVICE_REQ_STATUS)
                    && StringUtils.isNotBlank(singleOrder.getString(PARAM_SERVICE_REQ_STATUS)))
                order.setStatusDesc(singleOrder.getString(PARAM_SERVICE_REQ_STATUS));

            // Set Amount
            order.setAmount("0.0");

            // Get Account Details from request Payload
            if (singleOrder.has(PARAM_INPUT_PAYLOAD)) {
                JSONObject requestPayload = (JSONObject) singleOrder.get(PARAM_INPUT_PAYLOAD);
                if (requestPayload.length() != 0) {
                    String fromAccountNumber = "";

                    // Set Account number and nickname
                    if (requestPayload.has("fromAccountNumber")
                            && StringUtils.isNotBlank(requestPayload.getString("fromAccountNumber"))) {
                        fromAccountNumber = requestPayload.getString("fromAccountNumber");
                        order.setFromAccountNumber(fromAccountNumber);
                        ChequeManagementUtilities chequeUtils = new ChequeManagementUtilities();
                        HashMap<String, Account> accounts = chequeUtils.getAccountsMapFromCache(request);
                        if (accounts != null && StringUtils.isNotBlank(fromAccountNumber)) {
                            Account account = accounts.containsKey(fromAccountNumber) ? accounts.get(fromAccountNumber)
                                    : null;
                            if (account != null) {
                                order.setFromAccountNickName(account.getAccountName());
                            }
                        }
                    }

                    // Set Transaction Notes
                    if (requestPayload.has("transactionsNotes")
                            && StringUtils.isNotBlank(requestPayload.getString("transactionsNotes")))
                        order.setNote(requestPayload.getString("transactionsNotes"));

                    // Set Cheque date of issue
                    if (requestPayload.has("checkDateOfIssue")
                            && StringUtils.isNotBlank(requestPayload.getString("checkDateOfIssue")))
                        order.setCheckDateOfIssue(requestPayload.getString("checkDateOfIssue"));

                    // Set cheque Reason
                    if (requestPayload.has("checkReason")
                            && StringUtils.isNotBlank(requestPayload.getString("checkReason"))) {
                        String chequeReason = requestPayload.getString("checkReason");
                        if (StringUtils.isNotEmpty(props.getProperty(chequeReason)))
                            order.setCheckReason(props.getProperty(chequeReason));
                    }

                    // Set Cheque Numbers and cheque request type
                    if (requestPayload.has("checkNumber1")
                            && StringUtils.isNotBlank(requestPayload.getString("checkNumber1"))) {
                        order.setCheckNumber1(requestPayload.getString("checkNumber1"));
                        order.setRequestType("single");
                    }
                    if (requestPayload.has("checkNumber2")
                            && StringUtils.isNotBlank(requestPayload.getString("checkNumber2"))) {
                        order.setCheckNumber2(requestPayload.getString("checkNumber2"));
                        order.setRequestType("series");
                    }
                    if (requestPayload.has("payeeName")
                            && StringUtils.isNotBlank(requestPayload.getString("payeeName"))) {
                        order.setPayeeName(requestPayload.getString("payeeName"));
                    }

                    // Set Amount if available in payload for single cheque
                    if (requestPayload.has("amount") && StringUtils.isNotBlank(requestPayload.getString("amount")))
                        order.setAmount(requestPayload.getString("amount"));
                }
            }
            // Set Fee and payee name from order response 
            if (singleOrder.has(PARAM_RESPONSE_PAYLOAD)
                    && StringUtils.isNotBlank(singleOrder.getString(PARAM_RESPONSE_PAYLOAD))) {
                JSONObject ResponsePayload = new JSONObject(singleOrder.getString(PARAM_RESPONSE_PAYLOAD));

                // Set Fee
                if (ResponsePayload.has("fee") && StringUtils.isNotBlank(ResponsePayload.getString("fee")))
                    order.setFee(ResponsePayload.getString("fee"));

                if (ResponsePayload.has("stops")) {
                    JSONArray responseArray = ResponsePayload.getJSONArray("stops");
                    if (responseArray != null && responseArray.length() != 0) {
                        JSONObject responseObject = responseArray.getJSONObject(0);
                        if (responseObject.has("beneficiaryId")
                                && StringUtils.isNotBlank(responseObject.getString("beneficiaryId")))
                            order.setPayeeName(responseObject.getString("beneficiaryId"));

                    }
                }
            }
            stopPaymentOrders.add(order); 

        }
        return stopPaymentOrders;
    }
    
    @Override
    public List<StopPayment> getStopPaymentOrdersFromT24(StopPayment StopPaymentInput, DataControllerRequest request)
            throws ApplicationException {
    	Result result = new Result();
    	List<StopPayment> stopPaymentOrders = new ArrayList<>();

        String accountID = StopPaymentInput.getAccountID() != "" ? StopPaymentInput.getAccountID() : "";
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountID);
        alert.prepareError("T24 Request" + params.toString()).log();

        // Set Header Map
        HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

        String stopPaymentResponse = null;
        JSONObject Response = new JSONObject();
        try {
        	String operationName = "getAllPaymentStops";
        	String serviceName = StopPaymentConstants.T24_SERVICE_NAME_STOP_PAYMENTS;
        	
            result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName, true);
            stopPaymentResponse = ResultToJSON.convert(result);
            alert.prepareError("********* Stop Payments result: " + stopPaymentResponse).log();;
        } catch (Exception e) {
            alert.prepareError("Unable to get stop payment request orders " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHK_26013);
        }

        if (!StringUtils.isBlank(stopPaymentResponse)) {
            Response = new JSONObject(stopPaymentResponse);
            alert.prepareError("T24 Response " + stopPaymentResponse).log();
        }
        if(Response.has("errmsg") && !StringUtils.isBlank(Response.getString("errmsg"))) {
//        	StopPayment order = new StopPayment();
//        	order.setMessage(Response.getString("errmsg"));
//        	stopPaymentOrders.add(order);
        	throw new ApplicationException(ErrorCodeEnum.ERRCHK_26013);
        }else {
	        JSONArray Orders = Response.getJSONArray(StopPaymentConstants.PARAM_TRANSACTIONS_RESULT);
	        for (int i = 0; i < Orders.length(); i++) {
	            StopPayment order = new StopPayment();
	            JSONObject singleOrder = Orders.getJSONObject(i);
	            
	            String id = singleOrder.has(StopPaymentConstants.PARAM_ID) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_ID)) ? singleOrder.getString(StopPaymentConstants.PARAM_ID) : "-";
	            String transactionDate = singleOrder.has(StopPaymentConstants.PARAM_CHEQUE_TRANSACTION_DATE) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_CHEQUE_TRANSACTION_DATE)) ? singleOrder.getString(StopPaymentConstants.PARAM_CHEQUE_TRANSACTION_DATE) : "";
	            String statusDesc = singleOrder.has(StopPaymentConstants.PARAM_STATUS_DESCRIPTION) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_STATUS_DESCRIPTION)) ? singleOrder.getString(StopPaymentConstants.PARAM_STATUS_DESCRIPTION) : "-";
	            String amount = singleOrder.has(StopPaymentConstants.PARAM_AMOUNT) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_AMOUNT)) ? singleOrder.getString(StopPaymentConstants.PARAM_AMOUNT) : "-";
	            String fromAccountNickName = singleOrder.has(StopCheckPaymentUtils.PARAM_NICK_NAME) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_NICK_NAME)) ? singleOrder.getString(StopCheckPaymentUtils.PARAM_NICK_NAME) : "-";
	            String transactionsNotes = singleOrder.has(StopPaymentConstants.PARAM_TRANSACTION_NOTES) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_TRANSACTION_NOTES)) ? singleOrder.getString(StopPaymentConstants.PARAM_TRANSACTION_NOTES) : "-";
	            String checkDateOfIssue = singleOrder.has(StopPaymentConstants.PARAM_CHECK_DATE_OF_ISSUE) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_CHECK_DATE_OF_ISSUE)) ? singleOrder.getString(StopPaymentConstants.PARAM_CHECK_DATE_OF_ISSUE) : "";
	            String fromAccountNumber = singleOrder.has(StopPaymentConstants.PARAM_PAYMENT_STOP_ACCOUNT) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_PAYMENT_STOP_ACCOUNT)) ? singleOrder.getString(StopPaymentConstants.PARAM_PAYMENT_STOP_ACCOUNT) : "-";
	            String checkReason = singleOrder.has(StopPaymentConstants.CHECK_REASON) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.CHECK_REASON)) ? singleOrder.getString(StopPaymentConstants.CHECK_REASON) : "-";
	            String requestType = singleOrder.has(StopPaymentConstants.PARAM_REQUEST_TYPE) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_REQUEST_TYPE)) ? singleOrder.getString(StopPaymentConstants.PARAM_REQUEST_TYPE) : "-";
	            String checkNumber1 = singleOrder.has(StopPaymentConstants.PARAM_CHEQUE_NUMBER) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_CHEQUE_NUMBER)) ? singleOrder.getString(StopPaymentConstants.PARAM_CHEQUE_NUMBER) : "-";
	            String checkNumber2 = singleOrder.has(StopPaymentConstants.PARAM_CHEQUE_NUMBER_TWO) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_CHEQUE_NUMBER_TWO)) ? singleOrder.getString(StopPaymentConstants.PARAM_CHEQUE_NUMBER_TWO) : "";
	            String payeeName = singleOrder.has(StopPaymentConstants.PARAM_PAYEE_NAME) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_PAYEE_NAME)) ? singleOrder.getString(StopPaymentConstants.PARAM_PAYEE_NAME) : "-";
	            String fee = singleOrder.has(StopPaymentConstants.PARAM_FEE) && !StringUtils.isBlank(singleOrder.getString(StopPaymentConstants.PARAM_FEE)) ? singleOrder.getString(StopPaymentConstants.PARAM_FEE) : "-";
	            String transactionCurrency = singleOrder.has("transactionCurrency") && !StringUtils.isBlank(singleOrder.getString("transactionCurrency")) ? singleOrder.getString("transactionCurrency") : "-";
	            
	            order.setId(id);
	            order.setTransactionDate(transactionDate);
	            order.setStatusDesc(statusDesc);
	            order.setAmount(amount);
	            order.setFromAccountNickName(fromAccountNickName);
	            order.setNote(transactionsNotes);
	            order.setTransactionsNotes(transactionsNotes);
	            order.setCheckDateOfIssue(checkDateOfIssue);
	            order.setFromAccountNumber(fromAccountNumber);
	            order.setCheckReason(checkReason);
	            order.setRequestType(requestType);
	            order.setCheckNumber1(checkNumber1);
	            order.setCheckNumber2(checkNumber2);
	            order.setPayeeName(payeeName);
	            order.setFee(fee);
	            order.setTransactionCurrency(transactionCurrency);
	            
	            stopPaymentOrders.add(order); 
	
	        }
        }
        
        return stopPaymentOrders;
    }
}
