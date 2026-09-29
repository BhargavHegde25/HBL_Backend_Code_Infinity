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
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.GetChequeBookBackendDelegate;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementProperties;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.stoppayments.StopPaymentConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetChequeBookBackendDelegateImpl implements GetChequeBookBackendDelegate, ChequeManagementConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<ChequeBook> getChequeBookOrdersFromOMS(ChequeBook chequeBook, DataControllerRequest request)
            throws Exception {
    	
    	String CHEQUE_BACKEND = EnvironmentConfigurationsHandler.getServerAppProperty("CHEQUE_BACKEND");
        List<ChequeBook> chequeBookOrders = new ArrayList<>();

        // Load Check Book Request properties
        Properties props = ChequeManagementProperties.loadProps(PARAM_PROPERTY);

        String accountID = chequeBook.getAccountID() != "" ? chequeBook.getAccountID() : "";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("accountId", accountID);
        inputMap.put("type", props.getProperty("chequeBooksType"));
        inputMap.put("subType", props.getProperty("chequeBooksSubType"));
        inputMap.put("operation", "get");
        alert.prepareError("OMS Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        String chequeBookResponse = null;
        JSONObject Response = new JSONObject();
        try {
            chequeBookResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId("dbpChequeManagementServices")
                    .withOperationId("GetServiceRequestOperation")
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Unable to create cheque book request order " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26004);
        }

        if (StringUtils.isNotBlank(chequeBookResponse)) {
            Response = new JSONObject(chequeBookResponse);
            alert.prepareError("OMS Response " + chequeBookResponse).log();
        }

        JSONArray Orders = Response.getJSONArray(ChequeManagementConstants.PARAM_SERVICE_REQUESTS);
        for (int i = 0; i < Orders.length(); i++) {
            ChequeBook order = new ChequeBook();
            JSONObject singleOrder = Orders.getJSONObject(i);
            if (singleOrder.has(PARAM_INPUT_PAYLOAD)) {
                JSONObject payload = singleOrder.getJSONObject(PARAM_INPUT_PAYLOAD);
                String note = payload.has(PARAM_NOTE) ? payload.getString(PARAM_NOTE) : "";
              //  String accountId = payload.has(PARAM_ACCOUNTID) ? payload.getString(PARAM_ACCOUNTID) : "";
                String fees = payload.has(PARAM_FEES) ? payload.getString(PARAM_FEES) : "";
                String address = payload.has(PARAM_ADDRESS) ? payload.getString(PARAM_ADDRESS) : "";
                String deliveryType = payload.has(PARAM_DELIVERYTYPE) ? payload.getString(PARAM_DELIVERYTYPE) : "";
                String numberOfLeaves = payload.has(PARAM_NUMBEROFLEAVES) ? payload.getString(PARAM_NUMBEROFLEAVES) : "";
                String numberOfChequeBooks = payload.has(PARAM_NUMBEROFCHEQUEBOOKS) ? payload.getString(PARAM_NUMBEROFCHEQUEBOOKS) : "";
				if (StringUtils.isNotBlank(note)) {
					order.setNote(note);
				}
				
				if (StringUtils.isNotBlank(fees)) {
					order.setFees(fees);
				}
				if (StringUtils.isNotBlank(address)) {
					order.setAddress(address);
				}
				if (StringUtils.isNotBlank(deliveryType)) {
					order.setDeliveryType(deliveryType);
				}
				if (StringUtils.isNotBlank(numberOfLeaves)) {
					order.setNumberOfLeaves(numberOfLeaves);
				}
				if (StringUtils.isNotBlank(numberOfChequeBooks)) {
					order.setNumberOfChequeBooks(numberOfChequeBooks);
				}             
            }
            if (singleOrder.has(PARAM_ORDER_PROCESSED_TIME)
                    && StringUtils.isNotBlank(singleOrder.getString(PARAM_ORDER_PROCESSED_TIME)))
                order.setIssueDate(singleOrder.getString(PARAM_ORDER_PROCESSED_TIME).split("T")[0].replace("-", ""));

            if (singleOrder.has(PARAM_RESPONSE_PAYLOAD)
                    && StringUtils.isNotBlank(singleOrder.getString(PARAM_RESPONSE_PAYLOAD))) {
                JSONObject ResponsePayload = new JSONObject(singleOrder.getString(PARAM_RESPONSE_PAYLOAD));
                if (ResponsePayload.has("ChequeBookRequests")) {
                    JSONArray responseArray = ResponsePayload.getJSONArray("ChequeBookRequests");
                    if (responseArray != null && responseArray.length() != 0) {
                        JSONObject responseObject = responseArray.getJSONObject(0);

                        /*if (responseObject.has("requestDate")
                                && StringUtils.isNotBlank(responseObject.getString("requestDate")))
                            order.setIssueDate(responseObject.getString("requestDate"));*/

                        if (responseObject.has("description")
                                && StringUtils.isNotBlank(responseObject.getString("description")))
                            order.setDescription(responseObject.getString("description")); 

                        if (responseObject.has("chequeNumberStart")
                                && StringUtils.isNotBlank(responseObject.getString("chequeNumberStart")))
                            order.setChequeNumberStart(responseObject.getString("chequeNumberStart"));

                        if (responseObject.has("fee") && StringUtils.isNotBlank(responseObject.getString("fee")))
                            order.setFee(responseObject.getString("fee"));

                        String chequeIssueId = responseObject.has("chequeIssueId")
                                ? responseObject.getString("chequeIssueId") : "";
                    }
                }
            }
            String chequeStatus = singleOrder.has(PARAM_SERVICE_REQ_STATUS)
                    ? singleOrder.getString(PARAM_SERVICE_REQ_STATUS) : "";
            String orderId = singleOrder.has(PARAM_SERVICE_REQ_ID) ? singleOrder.getString(PARAM_SERVICE_REQ_ID) : "";
            String externalOrderRef = singleOrder.has(PARAM_EXTERNAL_REFERENCE)
                    ? singleOrder.getString(PARAM_EXTERNAL_REFERENCE) : "";
            String accountId = singleOrder.has(PARAM_ACCOUNTID) ? singleOrder.getString(PARAM_ACCOUNTID) : "";

            order.setChequeStatus(chequeStatus);
            order.setChequeIssueId(orderId);
            if (StringUtils.isNotBlank(accountId)) {
				order.setAccountID(accountId);
			}
            

            chequeBookOrders.add(order);
        }

        return chequeBookOrders;
    }

    @Override
    public List<ChequeBook> getChequeBookOrdersFromT24(ChequeBook chequeBook, DataControllerRequest request)
            throws Exception {
    	
    	Result result = new Result();
    	
        List<ChequeBook> chequeBookOrders = new ArrayList<>();

        String accountID = chequeBook.getAccountID() != "" ? chequeBook.getAccountID() : "";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("accountId", accountID);
        alert.prepareError("T24 Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

        String chequebookRequestResponse = null;
        JSONObject Response = new JSONObject();
        try {
        	String operationName = "getChequeBookRequests";
        	String serviceName = StopPaymentConstants.T24_SERVICE_NAME_STOP_PAYMENTS;
        	
            result = CommonUtils.callIntegrationService(request, inputMap, serviceHeaders, serviceName, operationName, true);
            chequebookRequestResponse = ResultToJSON.convert(result);
            alert.prepareError("********* Chequebook request result: " + chequebookRequestResponse).log();;
        } catch (Exception e) {
        	alert.prepareError("Unable to get stop payment request orders " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHK_26013);
        }
        
        if (StringUtils.isNotBlank(chequebookRequestResponse)) {
            Response = new JSONObject(chequebookRequestResponse);
            alert.prepareError("T24 Response " + Response).log();
        }

		JSONArray Orders = Response.getJSONArray("ChequeBookRequests");
		for (int i = 0; i < Orders.length(); i++) {
			ChequeBook order = new ChequeBook();
			JSONObject singleOrder = Orders.getJSONObject(i);
			String note = singleOrder.has(PARAM_NOTE) && !StringUtils.isBlank(singleOrder.getString(PARAM_NOTE)) ? singleOrder.getString(PARAM_NOTE) : "-";
			String fees = singleOrder.has(PARAM_FEES) && !StringUtils.isBlank(singleOrder.getString(PARAM_FEES)) ? singleOrder.getString(PARAM_FEES) : "-";
			String address = singleOrder.has(PARAM_ADDRESS) && !StringUtils.isBlank(singleOrder.getString(PARAM_ADDRESS)) ? singleOrder.getString(PARAM_ADDRESS) : "-";
			String deliveryType = singleOrder.has(PARAM_DELIVERYTYPE) && !StringUtils.isBlank(singleOrder.getString(PARAM_DELIVERYTYPE)) ? singleOrder.getString(PARAM_DELIVERYTYPE) : "-";
			String numberOfLeaves = singleOrder.has(PARAM_NUMBEROFLEAVES) && !StringUtils.isBlank(singleOrder.getString(PARAM_NUMBEROFLEAVES)) ? singleOrder.getString(PARAM_NUMBEROFLEAVES) : "-";
			String numberOfChequeBooks = singleOrder.has(PARAM_NUMBEROFCHEQUEBOOKS) && !StringUtils.isBlank(singleOrder.getString(PARAM_NUMBEROFCHEQUEBOOKS))
					? singleOrder.getString(PARAM_NUMBEROFCHEQUEBOOKS)
					: "-";
			String requestDate = singleOrder.has("requestDate") && !StringUtils.isBlank(singleOrder.getString("requestDate")) ? singleOrder.getString("requestDate").replace("-", "") : "-";
			String issueDate = singleOrder.has("issueDate") && !StringUtils.isBlank(singleOrder.getString("issueDate")) ? singleOrder.getString("issueDate") : "-";
			String chequeNumberStart = singleOrder.has("chequeNumberStart") && !StringUtils.isBlank(singleOrder.getString("chequeNumberStart")) ? singleOrder.getString("chequeNumberStart") : "-";
			String description = singleOrder.has("description") && !StringUtils.isBlank(singleOrder.getString("description")) ? singleOrder.getString("description") : "-";
			String chequeIssueId = singleOrder.has("chequeIssueId") && !StringUtils.isBlank(singleOrder.getString("chequeIssueId")) ? singleOrder.getString("chequeIssueId") : "-";
			String chequeStatus = singleOrder.has("chequeStatus") && !StringUtils.isBlank(singleOrder.getString("chequeStatus")) ? singleOrder.getString("chequeStatus") : "-";
			String accountIdentinifcation = singleOrder.has("accountID") && !StringUtils.isBlank(singleOrder.getString("accountID")) ? singleOrder.getString("accountID") : "-";

			order.setNote(note);
			order.setFees(fees);
			order.setAddress(address);
			order.setDeliveryType(deliveryType);
			order.setNumberOfLeaves(numberOfLeaves);
			order.setNumberOfChequeBooks(numberOfChequeBooks);
			order.setRequestDate(requestDate);
			order.setIssueDate(issueDate);
			order.setChequeNumberStart(chequeNumberStart);
			order.setDescription(description);
			order.setChequeIssueId(chequeIssueId);
			order.setChequeStatus(chequeStatus);
			order.setAccountID(accountIdentinifcation);

			chequeBookOrders.add(order);
		}

        return chequeBookOrders;
    }
}
