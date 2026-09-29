/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.AnchorInvoiceBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;
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
public class AnchorInvoiceBackendDelegateImpl implements AnchorInvoiceBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String SCF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public AnchorInvoiceDTO createAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(inputDto)
                    .addDataControllerRequest(request).addTypeAndSubType("AnchorInvoiceType", "AnchorInvoiceSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(inputDto)
                    .addModule("AnchorInvoiceModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setInvoiceReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new AnchorInvoiceDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public AnchorInvoiceDTO getAnchorInvoiceById(String invoiceReference, DataControllerRequest request) {
        AnchorInvoiceDTO collectionDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
                collectionDto = (AnchorInvoiceDTO) invoke().addDTO(AnchorInvoiceDTO.class).addServiceRequestId(invoiceReference)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                collectionDto = (AnchorInvoiceDTO) getInstance().addDTO(AnchorInvoiceDTO.class).filterByRecordId(invoiceReference).addDataControllerRequest(request).
                        getRecord().makeRequest().fetchRecordWithDTO();
            }
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching record", e).log();
        }
        return collectionDto;
    }

    @Override
    public AnchorInvoiceDTO updateAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getInvoiceReference())
                    .addRequestBody(inputDto).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getInvoiceReference()).addRequestBody(inputDto).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setInvoiceReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new AnchorInvoiceDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<AnchorInvoiceDTO> getAllAnchorInvoices(DataControllerRequest request) {
        List bills = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
                bills = invoke().addDTO(AnchorInvoiceDTO.class).addDataControllerRequest(request)
                        .addTypeAndSubType("AnchorInvoiceType", "AnchorInvoiceSubType").getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                bills = getInstance().addDTO(AnchorInvoiceDTO.class).addDataControllerRequest(request)
                        .addModule("AnchorInvoiceModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching records", e).log();
        }
        return bills;
    }

    private void _loadBackendType() {
        SCF_BACKEND = getScfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(SCF_BACKEND);
    }
}