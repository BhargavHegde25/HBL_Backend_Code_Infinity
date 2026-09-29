/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ReceivedGuaranteeClaimsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteeClaimsDTO;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;


public class ReceivedGuaranteeClaimsBackendDelegateImpl implements ReceivedGuaranteeClaimsBackendDelegate, TradeFinanceConstants {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    public ReceivedGuaranteeClaimsDTO createClaim(ReceivedGuaranteeClaimsDTO inputDto, HashMap<String, Object> inputParams, DataControllerRequest request) {
        JSONObject responseObject;
        inputDto.setServiceRequestTime(getCurrentDateTimeUTF());
        inputDto.setCreatedOn(getCurrentDateTimeUTF());
        String requestBody = getRequestBody(inputDto);
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("ReceivedGuaranteeClaimsType", "ReceivedGuaranteeClaimsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("ReceivedGuaranteeClaimsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setClaimsSRMSId(responseObject.get(PARAM_UNIQUE_ID).toString());
            inputDto.setMessage("Service Request Created successfully");
        } else {
            alert.prepareError("Error occurred while creating creating claim ", responseObject).log();
            request.addRequestParam_("isSrmsFailed", "true");
            inputDto = new ReceivedGuaranteeClaimsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    public ReceivedGuaranteeClaimsDTO updateGuaranteeClaims(ReceivedGuaranteeClaimsDTO inputDto, boolean isMergeRequired, ReceivedGuaranteeClaimsDTO initiatedClaimsDTO, DataControllerRequest request) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestBody = isMergeRequired ? getRequestBody(inputDto, initiatedClaimsDTO) : new JSONObject(inputDto);
            String payloadRequest = getRequestBody(JSONUtils.parse(requestBody.toString(), ReceivedGuaranteeClaimsDTO.class));
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getClaimsSRMSId())
                        .addRequestBody(payloadRequest).addDataControllerRequest(request).sendRequest().fetchResponse();
            } else {
                responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                        .addRecordId(inputDto.getClaimsSRMSId()).addRequestBody(payloadRequest).makeRequest().getResponse();
            }
            inputDto.setClaimsSRMSId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } catch (Exception e) {
            alert.prepareError("Unable to update guarantees request order ", e).log();
            request.addRequestParam_("isSrmsFailed", "true");
            inputDto = new ReceivedGuaranteeClaimsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    private JSONObject getRequestBody(ReceivedGuaranteeClaimsDTO inputClaimsDTO, ReceivedGuaranteeClaimsDTO backendClaimsDTO) {
        JSONObject inputClaimsObject = new JSONObject(inputClaimsDTO);
        if (backendClaimsDTO.getDocumentInformation() != null)
            backendClaimsDTO.setDocumentInformation(new JSONArray(backendClaimsDTO.getDocumentInformation()).toString());
        if (backendClaimsDTO.getPhysicalDocuments() != null)
            backendClaimsDTO.setPhysicalDocuments(new JSONArray(backendClaimsDTO.getPhysicalDocuments()).toString());
        if (backendClaimsDTO.getReturnedHistory() != null)
            backendClaimsDTO.setReturnedHistory(backendClaimsDTO.getReturnedHistory().replaceAll("\'", "\""));

        return TradeFinanceCommonUtils.mergeJSONObjects(new JSONObject(backendClaimsDTO), inputClaimsObject);
    }

    public List<ReceivedGuaranteeClaimsDTO> getClaims(DataControllerRequest request) {
        List claimsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                claimsList = invoke().addDTO(ReceivedGuaranteeClaimsDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("ReceivedGuaranteeClaimsType", "ReceivedGuaranteeClaimsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                claimsList = getInstance().addDTO(ReceivedGuaranteeClaimsDTO.class).addDataControllerRequest(request)
                        .addModule("ReceivedGuaranteeClaimsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching received guarantee claims", e).log();
        }
        return claimsList;
    }

    public ReceivedGuaranteeClaimsDTO getClaimsById(String claimsSRMSId, DataControllerRequest request) {
        ReceivedGuaranteeClaimsDTO claimDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                claimDto = (ReceivedGuaranteeClaimsDTO) invoke().addDTO(ReceivedGuaranteeClaimsDTO.class).addServiceRequestId(claimsSRMSId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                claimDto = (ReceivedGuaranteeClaimsDTO) getInstance().addDTO(ReceivedGuaranteeClaimsDTO.class).filterByRecordId(claimsSRMSId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            claimDto.setClaimsSRMSId(claimsSRMSId);
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching claims ", e).log();
        }
        return claimDto;
    }

    private String getRequestBody(ReceivedGuaranteeClaimsDTO inputDto) {
        String requestBody = null;
        try {
            requestBody = new ObjectMapper().writeValueAsString(inputDto).replace("\"", "'");
        } catch (JsonProcessingException e) {
            alert.prepareError("Error in processing input payload", e).log();
        }
        return requestBody;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}