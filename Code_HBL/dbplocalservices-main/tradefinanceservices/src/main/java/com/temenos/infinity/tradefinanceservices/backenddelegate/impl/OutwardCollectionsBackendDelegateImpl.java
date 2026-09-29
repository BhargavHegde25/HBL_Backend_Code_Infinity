/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.OutwardCollectionsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;
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
public class OutwardCollectionsBackendDelegateImpl implements OutwardCollectionsBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public OutwardCollectionsDTO createCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(inputDto).addDataControllerRequest(request)
                    .addTypeAndSubType("OutwardCollectionsType", "OutwardCollectionsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(inputDto)
                    .addModule("OutwardCollectionsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setCollectionReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            alert.prepareError("Error occurred while creating outward collection", responseObject).log();
            inputDto = new OutwardCollectionsDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<OutwardCollectionsDTO> getCollections(DataControllerRequest request) {
        List collections = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                collections = invoke().addDTO(OutwardCollectionsDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("OutwardCollectionsType", "OutwardCollectionsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                collections = getInstance().addDTO(OutwardCollectionsDTO.class).addDataControllerRequest(request)
                        .addModule("OutwardCollectionsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching outward collections", e).log();
        }
        return collections;
    }

    @Override
    public OutwardCollectionsDTO getCollectionById(String collectionReference, DataControllerRequest request) {
        OutwardCollectionsDTO collectionDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                collectionDto = (OutwardCollectionsDTO) invoke().addDTO(OutwardCollectionsDTO.class).addServiceRequestId(collectionReference)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                collectionDto = (OutwardCollectionsDTO) getInstance().addDTO(OutwardCollectionsDTO.class).filterByRecordId(collectionReference)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            collectionDto.setCollectionReference(collectionReference);
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching outward collection", e).log();
        }
        return collectionDto;
    }

    @Override
    public OutwardCollectionsDTO updateCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getCollectionReference())
                    .addRequestBody(inputDto).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getCollectionReference()).addRequestBody(inputDto).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setCollectionReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            alert.prepareError("Error occurred while updating outward collections", responseObject).log();
            inputDto = new OutwardCollectionsDTO();
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
