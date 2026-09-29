/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.OutwardCollectionAmendmentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionAmendmentsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

/**
 * @author k.meiyazhagan
 */
public class OutwardCollectionAmendmentsBackendDelegateImpl implements OutwardCollectionAmendmentsBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public OutwardCollectionAmendmentsDTO createAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(inputDto).addDataControllerRequest(request)
                    .addTypeAndSubType("OutwardCollectionAmendmentsType", "OutwardCollectionAmendmentsSubType")
                    .sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(inputDto)
                    .addModule("OutwardCollectionAmendmentsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setAmendmentReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new OutwardCollectionAmendmentsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<OutwardCollectionAmendmentsDTO> getAmendments(DataControllerRequest request) {
        List amendments = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendments = invoke().addDTO(OutwardCollectionAmendmentsDTO.class).addDataControllerRequest(request)
                        .addTypeAndSubType("OutwardCollectionAmendmentsType", "OutwardCollectionAmendmentsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                amendments = getInstance().addDTO(OutwardCollectionAmendmentsDTO.class).addDataControllerRequest(request)
                        .addModule("OutwardCollectionAmendmentsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching outward collection amendments", e).log();
        }
        return amendments;
    }

    @Override
    public OutwardCollectionAmendmentsDTO getAmendmentById(String amendmentReference, DataControllerRequest request) {
        OutwardCollectionAmendmentsDTO amendmentDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentDto = (OutwardCollectionAmendmentsDTO) invoke().addDTO(OutwardCollectionAmendmentsDTO.class).
                        addServiceRequestId(amendmentReference).addDataControllerRequest(request).
                        getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                amendmentDto = (OutwardCollectionAmendmentsDTO) getInstance().addDTO(OutwardCollectionAmendmentsDTO.class).filterByRecordId(amendmentReference)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching outward collection amendment", e).log();
        }
        return amendmentDto;
    }

    @Override
    public OutwardCollectionAmendmentsDTO updateAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getAmendmentReference()).addRequestBody(inputDto).
                    addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getAmendmentReference()).addRequestBody(inputDto).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setAmendmentReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new OutwardCollectionAmendmentsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}
