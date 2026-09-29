/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.AnchorFundingRequestBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getScfBackend;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceSRMSUtils.invoke;

/**
 * @author k.meiyazhagan
 */
public class AnchorFundingRequestBackendDelegateImpl implements AnchorFundingRequestBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String SCF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public AnchorFundingRequestDTO createAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(inputDto)
                    .addDataControllerRequest(request).addTypeAndSubType("AnchorFundingRequestType", "AnchorFundingRequestSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(inputDto)
                    .addModule("AnchorFundingRequestModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setFundingRequestId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new AnchorFundingRequestDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public AnchorFundingRequestDTO getAnchorFundingRequestById(String fundingRequestId, DataControllerRequest
            request) {
        AnchorFundingRequestDTO responseDTO = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
                responseDTO = (AnchorFundingRequestDTO) invoke().addDTO(AnchorFundingRequestDTO.class).addServiceRequestId(fundingRequestId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                responseDTO = (AnchorFundingRequestDTO) getInstance().addDTO(AnchorFundingRequestDTO.class).filterByRecordId(fundingRequestId).addDataControllerRequest(request).
                        getRecord().makeRequest().fetchRecordWithDTO();
            }
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching funding request", e).log();
        }
        return responseDTO;
    }

    @Override
    public AnchorFundingRequestDTO updateAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getFundingRequestId())
                    .addRequestBody(inputDto).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getFundingRequestId()).addRequestBody(inputDto).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setFundingRequestId(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new AnchorFundingRequestDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<AnchorFundingRequestDTO> getAllFundingRequest(DataControllerRequest request) {
        List fundingRequests = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
                fundingRequests = invoke().addDTO(AnchorFundingRequestDTO.class).addDataControllerRequest(request)
                        .addTypeAndSubType("AnchorFundingRequestType", "AnchorFundingRequestSubType").getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                fundingRequests = getInstance().addDTO(AnchorFundingRequestDTO.class).addDataControllerRequest(request)
                        .addModule("AnchorFundingRequestModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching Funding Requests", e).log();

        }
        return fundingRequests;
    }

    private void _loadBackendType() {
        SCF_BACKEND = getScfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(SCF_BACKEND);
    }
}