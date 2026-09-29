/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.IssuedGuaranteeClaimsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.IssuedGuaranteeClaimsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class IssuedGuaranteeClaimsBackendDelegateImpl implements IssuedGuaranteeClaimsBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public IssuedGuaranteeClaimsDTO createClaim(IssuedGuaranteeClaimsDTO claimDTO, DataControllerRequest request) {
        JSONObject responseObject;
        String payload = getRequestBody(claimDTO);
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(payload).addDataControllerRequest(request)
                    .addTypeAndSubType("IssuedGuaranteeClaimsType", "IssuedGuaranteeClaimsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(claimDTO)
                    .addModule("IssuedGuaranteeClaimsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            claimDTO.setClaimsSRMSId(responseObject.get(PARAM_UNIQUE_ID).toString());
            claimDTO.setMessage("Service Request Created successfully");
        } else {
            claimDTO = new IssuedGuaranteeClaimsDTO();
            claimDTO.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            claimDTO.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            alert.prepareError("Unable to create Guarantee claims request order").log();
        }
        return claimDTO;
    }

    public IssuedGuaranteeClaimsDTO updateClaim(IssuedGuaranteeClaimsDTO claimDTO, IssuedGuaranteeClaimsDTO oldClaimDTO,
                                                boolean isMergeRequired, DataControllerRequest request) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject reqJson = isMergeRequired ? mergeJSONObjects(new JSONObject(oldClaimDTO), new JSONObject(claimDTO)) : new JSONObject(claimDTO);
            String reqBody = getRequestBody(JSONUtils.parse(reqJson.toString(), IssuedGuaranteeClaimsDTO.class));

            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                responseObject = invoke().updateOrder().addServiceRequestId(claimDTO.getClaimsSRMSId())
                        .addRequestBody(reqBody).addDataControllerRequest(request).sendRequest().fetchResponse();
            } else {
                responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                        .addRecordId(claimDTO.getClaimsSRMSId()).addRequestBody(reqJson).makeRequest().getResponse();
            }
        } catch (Exception e) {
            alert.prepareError("Exception occurred while updating issued guarantee claim", e).log();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            claimDTO.setClaimsSRMSId(responseObject.get(PARAM_UNIQUE_ID).toString());
            claimDTO.setMessage("Service Request Updated successfully");
        } else {
            claimDTO = new IssuedGuaranteeClaimsDTO();
            claimDTO.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            claimDTO.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return claimDTO;
    }

    public IssuedGuaranteeClaimsDTO getClaimById(String claimsSRMSId, DataControllerRequest request) {
        IssuedGuaranteeClaimsDTO claimDTO = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                claimDTO = (IssuedGuaranteeClaimsDTO) invoke().addDTO(IssuedGuaranteeClaimsDTO.class).addServiceRequestId(claimsSRMSId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                claimDTO = (IssuedGuaranteeClaimsDTO) getInstance().addDTO(IssuedGuaranteeClaimsDTO.class).filterByRecordId(claimsSRMSId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
//            claimsDTO.setClaimsSRMSId(singleOrder.getString(PARAM_SERVICE_REQ_ID));
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching claims", e).log();
        }
        return claimDTO;
    }

    public List<IssuedGuaranteeClaimsDTO> getClaims(DataControllerRequest request) {
        List claimsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                claimsList = invoke().addDTO(IssuedGuaranteeClaimsDTO.class).addDataControllerRequest(request)
                        .addTypeAndSubType("IssuedGuaranteeClaimsType", "IssuedGuaranteeClaimsSubType")
                        .getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                claimsList = getInstance().addDTO(IssuedGuaranteeClaimsDTO.class).addDataControllerRequest(request)
                        .addModule("IssuedGuaranteeClaimsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
//            claimsDTO.setClaimsSRMSId(singleOrder.getString(PARAM_SERVICE_REQ_ID));
        } catch (Exception e) {
            alert.prepareError("Unable to get issued guarantee claim requests " + e).log();
        }
        return claimsList;
    }

    private String getRequestBody(IssuedGuaranteeClaimsDTO inputDto) {
        // TODO: remove getRequestBody
        String requestBody = null;
        try {
            requestBody = new ObjectMapper().writeValueAsString(inputDto).replaceAll("\"", "'");
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