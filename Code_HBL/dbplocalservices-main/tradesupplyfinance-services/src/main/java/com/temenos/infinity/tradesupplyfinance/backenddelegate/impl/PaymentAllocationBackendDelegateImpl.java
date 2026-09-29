/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.PaymentAllocationBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.PaymentAllocationDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceDBXDBUtils.getInstance;

/**
 * @author k.meiyazhagan
 */
public class PaymentAllocationBackendDelegateImpl implements PaymentAllocationBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public PaymentAllocationDTO createPaymentAllocation(PaymentAllocationDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(inputDto)
                .addModule("PaymentAllocationModule").makeRequest().getResponse();
        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setPaymentAllocationId(responseObject.getString(PARAM_RECORD_ID));
        } else {
            inputDto = new PaymentAllocationDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }

    @Override
    public List<PaymentAllocationDTO> getPaymentAllocations(DataControllerRequest request) {
        List records = null;
        try {
            records = getInstance().addDTO(PaymentAllocationDTO.class).addDataControllerRequest(request)
                    .addModule("PaymentAllocationModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching records", e).log();
        }
        return records;
    }

    @Override
    public PaymentAllocationDTO getPaymentAllocationById(String paymentAllocationId, DataControllerRequest request) {
        PaymentAllocationDTO responseDto = null;
        try {
            responseDto = (PaymentAllocationDTO) getInstance().addDTO(PaymentAllocationDTO.class).filterByRecordId(paymentAllocationId).addDataControllerRequest(request).
                    getRecord().makeRequest().fetchRecordWithDTO();
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching record", e).log();
        }
        return responseDto;
    }

    @Override
    public PaymentAllocationDTO updatePaymentAllocation(PaymentAllocationDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                .addRecordId(inputDto.getPaymentAllocationId()).addRequestBody(inputDto).makeRequest().getResponse();
        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setPaymentAllocationId(responseObject.getString(PARAM_RECORD_ID));
        } else {
            inputDto = new PaymentAllocationDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
    }
}