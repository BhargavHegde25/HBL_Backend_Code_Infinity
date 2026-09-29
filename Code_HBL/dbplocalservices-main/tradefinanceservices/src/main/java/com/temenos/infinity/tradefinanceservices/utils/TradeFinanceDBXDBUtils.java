/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.utils;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERR_12008;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCoreCustomerId;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getHeadersMap;

/**
 * @author k.meiyazhagan
 */
public class TradeFinanceDBXDBUtils<T> {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private String serviceId;
    private String operationId;
    private String response;
    private Class<T> genericDTO;
    private boolean isRequestFailed = false;
    private DataControllerRequest dataControllerRequest;
    private Map<String, Object> inputMap = new HashMap<>();
    private String customerIdAndUserId;

    private TradeFinanceDBXDBUtils() {
    }

    public static synchronized TradeFinanceDBXDBUtils getInstance() {
        return new TradeFinanceDBXDBUtils();
    }

    public TradeFinanceDBXDBUtils<T> addRecord() {
        this.inputMap.put(PARAM_CREATED_BY, customerIdAndUserId);
        this.serviceId = TradeFinanceAPIServices.DBPRBLOCALSERVICES_TF_RECORD_CREATE.getServiceName();
        this.operationId = TradeFinanceAPIServices.DBPRBLOCALSERVICES_TF_RECORD_CREATE.getOperationName();
        return this;
    }

    public TradeFinanceDBXDBUtils<T> updateRecord() {
        this.inputMap.put(PARAM_UPDATED_BY, customerIdAndUserId);
        this.serviceId = TradeFinanceAPIServices.DBPRBLOCALSERVICES_TF_RECORD_UPDATE.getServiceName();
        this.operationId = TradeFinanceAPIServices.DBPRBLOCALSERVICES_TF_RECORD_UPDATE.getOperationName();
        return this;
    }

    public TradeFinanceDBXDBUtils<T> getRecord() {
        this.serviceId = TradeFinanceAPIServices.DBPRBLOCALSERVICES_TF_RECORDS_GET.getServiceName();
        this.operationId = TradeFinanceAPIServices.DBPRBLOCALSERVICES_TF_RECORDS_GET.getOperationName();
        return this;
    }

    public TradeFinanceDBXDBUtils<T> getAllRecords() {
        this.serviceId = TradeFinanceAPIServices.DBPRBLOCALSERVICES_TF_RECORDS_GET.getServiceName();
        this.operationId = TradeFinanceAPIServices.DBPRBLOCALSERVICES_TF_RECORDS_GET.getOperationName();
        return this;
    }

    public TradeFinanceDBXDBUtils<T> addDTO(Class<T> genericDTO) {
        this.genericDTO = genericDTO;
        return this;
    }

    public TradeFinanceDBXDBUtils<T> addModule(String module) {
        inputMap.put(PARAM_MODULE_ID, TradeFinanceHelper.getProperty(module));
        inputMap.put(DB_PARAM_FILTER, PARAM_MODULE_ID + DB_PARAM_EQUALS + inputMap.get(PARAM_MODULE_ID));
        return this;
    }

    public TradeFinanceDBXDBUtils<T> addRequestBody(JSONObject inputBody) {
        inputMap.put(PARAM_TF_RECORDS_JSON, inputBody);
        return this;
    }

    public TradeFinanceDBXDBUtils<T> addRequestBody(String inputBody) {
        inputMap.put(PARAM_TF_RECORDS_JSON, new JSONObject(inputBody));
        return this;
    }

    public TradeFinanceDBXDBUtils<T> addRequestBody(T inputDto) {
        inputMap.put(PARAM_TF_RECORDS_JSON, this.getRequestBody(inputDto));
        return this;
    }

    private String getRequestBody(JSONObject inputJson) {
        String requestBody = null;
        try {
            requestBody = inputJson.toString();
        } catch (NullPointerException e) {
            alert.prepareError("TF :: Error occurred while constructing input payload").log();
        }
        return requestBody;
    }

    private String getRequestBody(T inputDto) {
        String requestBody = null;
        try {
            requestBody = new ObjectMapper().writeValueAsString(inputDto);
        } catch (JsonProcessingException e) {
            alert.prepareError("TF :: Error occurred while constructing input payload").log();
        }
        return requestBody;
    }

    public TradeFinanceDBXDBUtils<T> addDataControllerRequest(DataControllerRequest dataControllerRequest) {
        this.dataControllerRequest = dataControllerRequest;
        String customerId = getCoreCustomerId(dataControllerRequest);
        String userId = HelperMethods.getUserIdFromSession(dataControllerRequest);
        this.customerIdAndUserId = StringUtils.join(Arrays.asList(userId, customerId), SEPARATOR_USER_CUSTOMER_ID);
        return this;
    }

    public TradeFinanceDBXDBUtils<T> addRecordId(String recordId) {
        inputMap.put(PARAM_RECORD_ID, recordId);
        return this;
    }

    public TradeFinanceDBXDBUtils<T> filterByRecordId(String recordId) {
        inputMap.put(DB_PARAM_FILTER, PARAM_RECORD_ID + DB_PARAM_EQUALS + recordId);
        return this;
    }

    public TradeFinanceDBXDBUtils<T> makeRequest() {
        diagnostic.prepareInfo("TF :: Input to Backend" + inputMap).log();
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceId)
                    .withOperationId(operationId)
                    .withRequestParameters(inputMap)
                    .withRequestHeaders(getHeadersMap(dataControllerRequest))
                    .withDataControllerRequest(dataControllerRequest)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("TF :: Exception occurred while sending request", e, response).log();
            isRequestFailed = true;
        }
        return this;
    }

    public JSONObject getResponse() {
        JSONObject responseObject = new JSONObject();
        if (isRequestFailed) {
            _addErrMsg(responseObject);
            return responseObject;
        }

        try {
            responseObject = new JSONObject(response);
            if (responseObject.has(PARAM_TF_RECORDS)) {
                responseObject = responseObject.getJSONArray(PARAM_TF_RECORDS).getJSONObject(0);
            } else {
                _addErrMsg(responseObject);
            }
        } catch (Exception e) {
            _addErrMsg(responseObject);
        }
        return responseObject;
    }

    public List<T> fetchOrdersWithDTO() throws IOException {
        List<T> list = new LinkedList<>();
        if (isRequestFailed)
            return list;
        JSONArray records = new JSONObject(response).getJSONArray(PARAM_TF_RECORDS);
        JSONObject recordJson;
        for (int i = 0; i < records.length(); i++) {
            recordJson = getRecordJson(records.getJSONObject(i));
            list.add(JSONUtils.parse(recordJson.toString(), genericDTO));
        }
        return list;
    }

    public T fetchRecordWithDTO() throws IOException {
        if (isRequestFailed)
            return null;
        JSONArray records = new JSONObject(response).getJSONArray(PARAM_TF_RECORDS);
        return JSONUtils.parse(getRecordJson(records.getJSONObject(0)).toString(), genericDTO);
    }

    private JSONObject getRecordJson(JSONObject recordJson) {
        return new JSONObject(recordJson.getString(PARAM_TF_RECORDS_JSON))
                .put(PARAM_RECORD_ID, recordJson.getString(PARAM_RECORD_ID))
                .put(PARAM_LASTUPDATEDTIMESTAMP, recordJson.has(PARAM_UPDATED_DATE) ? recordJson.get(PARAM_UPDATED_DATE) : "");
    }

    private void _addErrMsg(JSONObject jsonObject) {
        isRequestFailed = true;
        alert.prepareError("Error occurred while sending tf request", jsonObject).log();
        jsonObject.put(PARAM_DBP_ERR_MSG, ERR_12008.getErrorMessage());
        jsonObject.put(PARAM_DBP_ERR_CODE, ERR_12008.getErrorCodeAsString());
    }
}