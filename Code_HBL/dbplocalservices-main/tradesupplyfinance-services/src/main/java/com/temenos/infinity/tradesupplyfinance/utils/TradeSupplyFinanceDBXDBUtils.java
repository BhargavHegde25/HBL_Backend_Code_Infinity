/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.utils;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.config.TradeSupplyFinanceAPIServices;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum.ERR_30003;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getCoreCustomerId;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getHeadersMap;

/**
 * @author k.meiyazhagan
 */
public class TradeSupplyFinanceDBXDBUtils<T> {
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

    private TradeSupplyFinanceDBXDBUtils() {
    }

    public static synchronized TradeSupplyFinanceDBXDBUtils getInstance() {
        return new TradeSupplyFinanceDBXDBUtils();
    }

    public TradeSupplyFinanceDBXDBUtils<T> addRecord() {
        this.inputMap.put(PARAM_CREATED_BY, customerIdAndUserId);
        this.serviceId = TradeSupplyFinanceAPIServices.DBPRBLOCALSERVICES_SCF_RECORD_CREATE.getServiceName();
        this.operationId = TradeSupplyFinanceAPIServices.DBPRBLOCALSERVICES_SCF_RECORD_CREATE.getOperationName();
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> updateRecord() {
        this.inputMap.put(PARAM_UPDATED_BY, customerIdAndUserId);
        this.serviceId = TradeSupplyFinanceAPIServices.DBPRBLOCALSERVICES_SCF_RECORD_UPDATE.getServiceName();
        this.operationId = TradeSupplyFinanceAPIServices.DBPRBLOCALSERVICES_SCF_RECORD_UPDATE.getOperationName();
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> getRecord() {
        this.serviceId = TradeSupplyFinanceAPIServices.DBPRBLOCALSERVICES_SCF_RECORDS_GET.getServiceName();
        this.operationId = TradeSupplyFinanceAPIServices.DBPRBLOCALSERVICES_SCF_RECORDS_GET.getOperationName();
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> getAllRecords() {
        this.serviceId = TradeSupplyFinanceAPIServices.DBPRBLOCALSERVICES_SCF_RECORDS_GET.getServiceName();
        this.operationId = TradeSupplyFinanceAPIServices.DBPRBLOCALSERVICES_SCF_RECORDS_GET.getOperationName();
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> addDTO(Class<T> genericDTO) {
        this.genericDTO = genericDTO;
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> addModule(String module) {
        inputMap.put(PARAM_MODULE_ID, TradeSupplyFinanceProperties.getProperty(module));
        inputMap.put(DB_PARAM_FILTER, PARAM_MODULE_ID + DB_PARAM_EQUALS + inputMap.get(PARAM_MODULE_ID));
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> addRequestBody(T inputDto) {
        inputMap.put(PARAM_SCF_RECORDS_JSON, this.getRequestBody(inputDto));
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

    public TradeSupplyFinanceDBXDBUtils<T> addDataControllerRequest(DataControllerRequest dataControllerRequest) {
        this.dataControllerRequest = dataControllerRequest;
        String customerId = getCoreCustomerId(dataControllerRequest);
        String userId = HelperMethods.getUserIdFromSession(dataControllerRequest);
        this.customerIdAndUserId = StringUtils.join(Arrays.asList(userId, customerId), SEPARATOR_USER_CUSTOMER_ID);
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> addRecordId(String recordId) {
        inputMap.put(PARAM_RECORD_ID, recordId);
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> filterByRecordId(String recordId) {
        inputMap.put(DB_PARAM_FILTER, PARAM_RECORD_ID + DB_PARAM_EQUALS + recordId);
        return this;
    }

    public TradeSupplyFinanceDBXDBUtils<T> makeRequest() {
        diagnostic.prepareInfo("SCF :: Input to Backend" + inputMap).log();
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceId)
                    .withOperationId(operationId)
                    .withRequestParameters(inputMap)
                    .withRequestHeaders(getHeadersMap(dataControllerRequest))
                    .withDataControllerRequest(dataControllerRequest)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("SCF :: Exception occurred while sending request", e, response).log();
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
            if (responseObject.has(PARAM_SCF_RECORDS)) {
                responseObject = responseObject.getJSONArray(PARAM_SCF_RECORDS).getJSONObject(0);
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
        JSONArray records = new JSONObject(response).getJSONArray(PARAM_SCF_RECORDS);
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
        JSONArray records = new JSONObject(response).getJSONArray(PARAM_SCF_RECORDS);
        return JSONUtils.parse(getRecordJson(records.getJSONObject(0)).toString(), genericDTO);
    }

    private JSONObject getRecordJson(JSONObject recordJson) {
        return new JSONObject(recordJson.getString(PARAM_SCF_RECORDS_JSON)).put(PARAM_RECORD_ID, recordJson.getString(PARAM_RECORD_ID));
    }

    private void _addErrMsg(JSONObject jsonObject) {
        isRequestFailed = true;
        alert.prepareError("Error occurred while sending scf request", jsonObject).log();
        jsonObject.put(PARAM_DBP_ERR_MSG, ERR_30003.getErrorMessage());
        jsonObject.put(PARAM_DBP_ERR_CODE, ERR_30003.getErrorCodeAsString());
    }
}