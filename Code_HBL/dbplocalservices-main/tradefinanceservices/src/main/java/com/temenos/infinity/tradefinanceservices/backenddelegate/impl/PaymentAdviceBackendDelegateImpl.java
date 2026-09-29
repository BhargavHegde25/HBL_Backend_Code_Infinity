/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.PaymentAdviceBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class PaymentAdviceBackendDelegateImpl implements PaymentAdviceBackendDelegate, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    public PaymentAdviceDTO createPaymentAdvice(PaymentAdviceDTO paymentAdviceDTO, DataControllerRequest request) {
        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(paymentAdviceDTO).addDataControllerRequest(request)
                    .addTypeAndSubType("PaymentAdviceType", "PaymentAdviceSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(paymentAdviceDTO)
                    .addModule("PaymentAdviceModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            paymentAdviceDTO.setPaymentAdviceReference(responseObject.get(PARAM_UNIQUE_ID).toString());
            paymentAdviceDTO.setStatus("Success");
        } else {
            paymentAdviceDTO = new PaymentAdviceDTO();
            paymentAdviceDTO.setErrorMessage(responseObject.getString(PARAM_DBP_ERR_MSG));
            paymentAdviceDTO.setErrorCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }

        return paymentAdviceDTO;
    }

    @Override
    public List<PaymentAdviceDTO> getPaymentAdvice(DataControllerRequest request) {
        List paymentAdvices = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                paymentAdvices = invoke().addDTO(PaymentAdviceDTO.class).addDataControllerRequest(request)
                        .addTypeAndSubType("PaymentAdviceType", "PaymentAdviceSubType")
                        .getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                paymentAdvices = getInstance().addDTO(PaymentAdviceDTO.class).addDataControllerRequest(request)
                        .addModule("PaymentAdviceModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching payment advices", e).log();
        }
        return paymentAdvices;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}