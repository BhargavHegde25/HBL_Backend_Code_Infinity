/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.utils;

import static com.temenos.infinity.tradelending.constants.ErrorCodeEnum.ERR_30003;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.DB_PARAM_EQUALS;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.DB_PARAM_FILTER;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_CREATED_BY;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_DBP_ERR_CODE;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_DBP_ERR_MSG;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_MODULE_ID;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_LD_RECORDS;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_LD_RECORDS_JSON;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_UPDATED_BY;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.SEPARATOR_USER_CUSTOMER_ID;
import static com.temenos.infinity.tradelending.utils.TradeLendingCommonUtils.getCoreCustomerId;
import static com.temenos.infinity.tradelending.utils.TradeLendingCommonUtils.getHeadersMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.config.TradeLendingAPIServices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class TradeLendingDBXDBUtils<T> {

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

    private TradeLendingDBXDBUtils() {
    }

    public static synchronized TradeLendingDBXDBUtils getInstance() {
        return new TradeLendingDBXDBUtils();
    }

    public TradeLendingDBXDBUtils<T> addRecord() {
        this.inputMap.put(PARAM_CREATED_BY, customerIdAndUserId);
        this.serviceId = TradeLendingAPIServices.DBPRBLOCALSERVICES_LD_RECORD_CREATE.getServiceName();
        this.operationId = TradeLendingAPIServices.DBPRBLOCALSERVICES_LD_RECORD_CREATE.getOperationName();
        return this;
    }

    public TradeLendingDBXDBUtils<T> updateRecord() {
        this.inputMap.put(PARAM_UPDATED_BY, customerIdAndUserId);
        this.serviceId = TradeLendingAPIServices.DBPRBLOCALSERVICES_LD_RECORD_UPDATE.getServiceName();
        this.operationId = TradeLendingAPIServices.DBPRBLOCALSERVICES_LD_RECORD_UPDATE.getOperationName();
        return this;
    }

    public TradeLendingDBXDBUtils<T> getRecord() {
        this.serviceId = TradeLendingAPIServices.DBPRBLOCALSERVICES_LD_RECORDS_GET.getServiceName();
        this.operationId = TradeLendingAPIServices.DBPRBLOCALSERVICES_LD_RECORDS_GET.getOperationName();
        return this;
    }

    public TradeLendingDBXDBUtils<T> getAllRecords() {
        this.serviceId = TradeLendingAPIServices.DBPRBLOCALSERVICES_LD_RECORDS_GET.getServiceName();
        this.operationId = TradeLendingAPIServices.DBPRBLOCALSERVICES_LD_RECORDS_GET.getOperationName();
        return this;
    }

    public TradeLendingDBXDBUtils<T> addDTO(Class<T> genericDTO) {
        this.genericDTO = genericDTO;
        return this;
    }

    public TradeLendingDBXDBUtils<T> addModule(String module) {
        inputMap.put(PARAM_MODULE_ID, TradeLendingProperties.getProperty(module));
        inputMap.put(DB_PARAM_FILTER, PARAM_MODULE_ID + DB_PARAM_EQUALS + inputMap.get(PARAM_MODULE_ID));
        return this;
    }

    public TradeLendingDBXDBUtils<T> addRequestBody(T inputDto) {
        inputMap.put(PARAM_LD_RECORDS_JSON, this.getRequestBody(inputDto));
        return this;
    }

    private String getRequestBody(T inputDto) {
        String requestBody = null;
        try {
            requestBody = new ObjectMapper().writeValueAsString(inputDto);
        } catch (JsonProcessingException e) {
            alert.prepareError("SCF :: Error occurred while constructing input payload").log();
        }
        return requestBody;
    }

    public TradeLendingDBXDBUtils<T> addDataControllerRequest(DataControllerRequest dataControllerRequest) {
        this.dataControllerRequest = dataControllerRequest;
        String customerId = getCoreCustomerId(dataControllerRequest);
        String userId = HelperMethods.getUserIdFromSession(dataControllerRequest);
        this.customerIdAndUserId = StringUtils.join(Arrays.asList(userId, customerId), SEPARATOR_USER_CUSTOMER_ID);
        return this;
    }

    public TradeLendingDBXDBUtils<T> addRecordId(String recordId) {
        inputMap.put(PARAM_RECORD_ID, recordId);
        return this;
    }

    public TradeLendingDBXDBUtils<T> filterByRecordId(String recordId) {
        inputMap.put(DB_PARAM_FILTER, PARAM_RECORD_ID + DB_PARAM_EQUALS + recordId);
        return this;
    }

    public TradeLendingDBXDBUtils<T> makeRequest() {
        diagnostic.prepareInfo("Lending :: Input to Backend" + inputMap).log();
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceId)
                    .withOperationId(operationId)
                    .withRequestParameters(inputMap)
                    .withRequestHeaders(getHeadersMap(dataControllerRequest))
                    .withDataControllerRequest(dataControllerRequest)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Lending :: Exception occurred while sending request", e, response).log();
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
            if (responseObject.has(PARAM_LD_RECORDS)) {
                responseObject = responseObject.getJSONArray(PARAM_LD_RECORDS).getJSONObject(0);
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
        JSONArray records = new JSONObject(response).getJSONArray(PARAM_LD_RECORDS);
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
        JSONArray records = new JSONObject(response).getJSONArray(PARAM_LD_RECORDS);
        return JSONUtils.parse(getRecordJson(records.getJSONObject(0)).toString(), genericDTO);
    }

    private JSONObject getRecordJson(JSONObject recordJson) {
        return new JSONObject(recordJson.getString(PARAM_LD_RECORDS_JSON)).put(PARAM_RECORD_ID, recordJson.getString(PARAM_RECORD_ID));
    }

    private void _addErrMsg(JSONObject jsonObject) {
        isRequestFailed = true;
        alert.prepareError("Error occurred while sending scf request", jsonObject).log();
        jsonObject.put(PARAM_DBP_ERR_MSG, ERR_30003.getErrorMessage());
        jsonObject.put(PARAM_DBP_ERR_CODE, ERR_30003.getErrorCodeAsString());
    }
}
