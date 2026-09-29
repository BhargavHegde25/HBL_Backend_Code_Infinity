/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.CounterPartyInvoiceBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.CounterPartyInvoiceDTO;
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
public class CounterPartyInvoiceBackendDelegateImpl implements CounterPartyInvoiceBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String SCF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public CounterPartyInvoiceDTO createCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(inputDto)
                    .addDataControllerRequest(request).addTypeAndSubType("CounterPartyInvoiceType", "CounterPartyInvoiceSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(inputDto)
                    .addModule("CounterPartyInvoiceModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setInvoiceReference(responseObject.get(PARAM_UNIQUE_ID).toString());
        } else {
            inputDto = new CounterPartyInvoiceDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public CounterPartyInvoiceDTO getCounterPartyInvoiceById(String invoiceReference, DataControllerRequest request) {
        CounterPartyInvoiceDTO collectionDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
                collectionDto = (CounterPartyInvoiceDTO) invoke().addDTO(CounterPartyInvoiceDTO.class).addServiceRequestId(invoiceReference)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                collectionDto = (CounterPartyInvoiceDTO) getInstance().addDTO(CounterPartyInvoiceDTO.class).filterByRecordId(invoiceReference).addDataControllerRequest(request).
                        getRecord().makeRequest().fetchRecordWithDTO();
            }
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching record", e).log();
        }
        return collectionDto;
    }

    @Override
    public CounterPartyInvoiceDTO updateCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request) {
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
            inputDto = new CounterPartyInvoiceDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<CounterPartyInvoiceDTO> getAllCounterPartyInvoices(DataControllerRequest request) {
        List bills = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(SCF_BACKEND, PARAM_SCF_BACKEND_SRMS)) {
                bills = invoke().addDTO(CounterPartyInvoiceDTO.class).addDataControllerRequest(request)
                        .addTypeAndSubType("CounterPartyInvoiceType", "CounterPartyInvoiceSubType").getOrders().sendRequest().fetchOrdersResponseWithDTO();

            } else {
                bills = getInstance().addDTO(CounterPartyInvoiceDTO.class).addDataControllerRequest(request)
                        .addModule("CounterPartyInvoiceModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
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