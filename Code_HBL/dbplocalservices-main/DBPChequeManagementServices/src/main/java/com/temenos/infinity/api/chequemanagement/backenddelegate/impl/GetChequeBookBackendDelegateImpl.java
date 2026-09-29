package com.temenos.infinity.api.chequemanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.GetChequeBookBackendDelegate;
import com.temenos.infinity.api.chequemanagement.config.ChequeManagementAPIServices;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementProperties;
import com.temenos.infinity.api.commons.exception.ApplicationException;

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
            throws ApplicationException {
        List<ChequeBook> chequeBookOrders = new ArrayList<>();

        // Load Check Book Request properties
        Properties props = ChequeManagementProperties.loadProps(PARAM_PROPERTY);

        String accountID = chequeBook.getAccountID() != "" ? chequeBook.getAccountID() : "";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("accountId", accountID);
        inputMap.put("type", props.getProperty("chequeBooksType"));
        inputMap.put("subType", props.getProperty("chequeBooksSubType"));
        alert.prepareError("OMS Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        String chequeBookResponse = null;
        JSONObject Response = new JSONObject();
        try {
            chequeBookResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILS.getServiceName())
                    .withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILS.getOperationName())
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

}
