/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.utils;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERRTF_29071;
import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERR_12004;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.fetchCustomerFromSession;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getHeadersMap;

public class TradeFinanceSRMSUtils<T> {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private DataControllerRequest dataControllerRequest;
    private Map<String, Object> inputMap = new HashMap<>();
    private String serviceId;
    private String operationId;
    private boolean isCreate = false;
    private boolean isUpdate = false;
    private boolean serviceRequestFailed = false;
    private String response;
    private Class<T> tradeDTO;
    private List<String> getMap = null;


    private TradeFinanceSRMSUtils() {
    }

    public static TradeFinanceSRMSUtils invoke() {
        return new TradeFinanceSRMSUtils();
    }

    public boolean getIsServiceRequestFailed() {
        return serviceRequestFailed;
    }

    public void setIsServiceRequestFailed(boolean serviceRequestFailed) {
        this.serviceRequestFailed = serviceRequestFailed;
    }

    public TradeFinanceSRMSUtils<T> createOrder() {
        isCreate = true;
        serviceId = TradeFinanceAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName();
        operationId = TradeFinanceAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName();
        return this;
    }

    public TradeFinanceSRMSUtils<T> getOrders() {
        inputMap.put(PARAM_PAGE_SIZE, DEFAULT_PAGE_SIZE);
        serviceId = TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILS.getServiceName();
        operationId = TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILS.getOperationName();
        return this;
    }

    public TradeFinanceSRMSUtils<T> getOrderById() {
        serviceId = TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETSERVICEREQUESTBYID.getServiceName();
        operationId = TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETSERVICEREQUESTBYID.getOperationName();
        return this;
    }

    public TradeFinanceSRMSUtils<T> updateOrder() {
        isUpdate = true;
        serviceId = TradeFinanceAPIServices.SERVICEREQUESTJAVA_UPDATEORDER.getServiceName();
        operationId = TradeFinanceAPIServices.SERVICEREQUESTJAVA_UPDATEORDER.getOperationName();
        return this;
    }

    public TradeFinanceSRMSUtils<T> addDTO(Class<T> tradeDTO) {
        this.tradeDTO = tradeDTO;
        return this;
    }

    public TradeFinanceSRMSUtils<T> addTypeAndSubType(String type, String subType) {
        inputMap.put(PARAM_TYPE, TradeFinanceHelper.getProperty(type));
        inputMap.put(isCreate ? PARAM_subtype : PARAM_subType, TradeFinanceHelper.getProperty(subType));
        return this;
    }

    public TradeFinanceSRMSUtils<T> addRequestBody(T inputDto) {
        inputMap.put(PARAM_REQUEST_BODY, getRequestBody(inputDto));
        return this;
    }

    public TradeFinanceSRMSUtils<T> addRequestBody(String requestBody) {
        inputMap.put(PARAM_REQUEST_BODY, requestBody);
        return this;
    }

    private String getRequestBody(T inputDto) {
        String requestBody = null;
        try {
            requestBody = new ObjectMapper().writeValueAsString(inputDto).replaceAll("\"", "'");
        } catch (JsonProcessingException e) {
            alert.prepareError("Error occurred while constructing input payload").log();
        }
        return requestBody;
    }

    public TradeFinanceSRMSUtils<T> addServiceRequestId(String serviceRequestId) {
        inputMap.put(isUpdate ? PARAM_SERVICE_REQUEST_ID : PARAM_SERVICE_REQUEST_IDS, serviceRequestId);
        return this;
    }

    public TradeFinanceSRMSUtils<T> addDataControllerRequest(DataControllerRequest dataControllerRequest) {
        this.dataControllerRequest = dataControllerRequest;
        return this;
    }

    public TradeFinanceSRMSUtils<T> addGetParams(List<String> getMap) {
        this.getMap = getMap;
        return this;
    }

    public TradeFinanceSRMSUtils<T> sendRequest() {
        diagnostic.prepareInfo("Input to Backend" + inputMap).log();
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceId)
                    .withOperationId(operationId)
                    .withRequestParameters(inputMap)
                    .withRequestHeaders(getHeadersMap(dataControllerRequest))
                    .withDataControllerRequest(dataControllerRequest)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("TradeFinance, Exception occurred while sending request, Exception : {}, Response : {}", e, response).log();
            serviceRequestFailed = true;
        }
        return this;
    }

    public JSONObject fetchResponse() {
        JSONObject responseObject = new JSONObject();
        if (!serviceRequestFailed) {
            try {
                responseObject = new JSONObject(response);
                if ((responseObject.has(PARAM_DBP_ERR_MSG) && StringUtils.isNotEmpty(responseObject.getString(PARAM_DBP_ERR_MSG))
                        || (responseObject.has(PARAM_DBP_ERR_CODE) && StringUtils.isNotEmpty(String.valueOf(responseObject.get(PARAM_DBP_ERR_CODE)))))) {
                    _addErrMsg(responseObject);
                    return responseObject;
                }
                if (responseObject.has(PARAM_ORDER_ID))
                    responseObject.put(PARAM_SERVICE_REQ_ID, responseObject.get(PARAM_ORDER_ID));
                if (responseObject.has(BACKEND_MESSAGE))
                    responseObject.put(MESSAGE, responseObject.get(BACKEND_MESSAGE));
            } catch (Exception e) {
                _addErrMsg(responseObject);
            }
        } else {
            _addErrMsg(responseObject);
        }
        return responseObject;
    }

    public List<T> fetchOrdersResponseWithDTO() throws IOException {
        List<T> list = new LinkedList<>();
        if (serviceRequestFailed)
            return list;
        JSONArray Orders = new JSONObject(response).getJSONArray(PARAM_SERVICE_REQUESTS);
        for (int i = 0; i < Orders.length(); i++) {
            JSONObject singleOrder = Orders.getJSONObject(i);
            JSONObject inputPayload = singleOrder.getJSONObject(PARAM_SERVICE_REQ_REQUEST_IN);
            inputPayload.put(PARAM_SRMSID, singleOrder.get(PARAM_SERVICE_REQ_ID));
            inputPayload.put(PARAM_CUSTOMER_ID, singleOrder.get(PARAM_PARTY_ID));
            inputPayload.put(PARAM_LASTUPDATEDTIMESTAMP, singleOrder.has(PARAM_ORDER_PROCESSED_TIME) ? singleOrder.getString(PARAM_ORDER_PROCESSED_TIME) : "");
            list.add(JSONUtils.parse(inputPayload.toString(), tradeDTO));
        }
        return list;
    }

    public T fetchOrderByIdResponse() throws IOException {
        if (serviceRequestFailed)
            return null;
        JSONArray Orders = new JSONObject(response).getJSONArray(PARAM_SERVICE_REQUESTS);
        JSONObject singleOrder = Orders.getJSONObject(0);
        JSONObject inputPayload = new JSONObject();
        if (!singleOrder.has(PARAM_PARTY_ID)
                || !singleOrder.get(PARAM_PARTY_ID).equals(fetchCustomerFromSession(this.dataControllerRequest))) {
            inputPayload.put(PARAM_DBP_ERR_MSG, ERRTF_29071.getErrorMessage());
            inputPayload.put(PARAM_DBP_ERR_CODE, ERRTF_29071.getErrorCodeAsString());
            return JSONUtils.parse(inputPayload.toString(), tradeDTO);
        }
        inputPayload = singleOrder.getJSONObject(PARAM_SERVICE_REQ_REQUEST_IN);
        inputPayload.put(PARAM_SRMSID, singleOrder.get(PARAM_SERVICE_REQ_ID));
        inputPayload.put(PARAM_CUSTOMER_ID, singleOrder.get(PARAM_PARTY_ID));
        inputPayload.put(PARAM_LASTUPDATEDTIMESTAMP, singleOrder.has(PARAM_ORDER_PROCESSED_TIME) ? singleOrder.getString(PARAM_ORDER_PROCESSED_TIME) : "");
        return JSONUtils.parse(inputPayload.toString(), tradeDTO);
    }


    private void _addErrMsg(JSONObject jsonObject) {
        serviceRequestFailed = true;
        alert.prepareError("Error occurred while sending trade finance service request, Response : {}", jsonObject).log();
        jsonObject.put(PARAM_DBP_ERR_MSG, ERR_12004.getErrorMessage());
        jsonObject.put(PARAM_DBP_ERR_CODE, ERR_12004.getErrorCodeAsString());
    }
}

