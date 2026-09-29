/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ExportLetterOfCreditsDrawingsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.PaymentAdviceResource;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCurrentDateTimeUTF;

public class PaymentAdviceResourceImpl implements PaymentAdviceResource, TradeFinanceConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result createPaymentAdvice(PaymentAdviceDTO paymentAdvice, DataControllerRequest request) {
        PaymentAdviceDTO paymentAdviceResponse;
        Result result = new Result();
        PaymentAdviceBussinessDelegate paymentsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);
        ExportLetterOfCreditsDrawingsBackendDelegate drawingsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ExportLetterOfCreditsDrawingsBackendDelegate.class);

        // customer
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId)) return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        if (StringUtils.isBlank(paymentAdvice.getOrderId())
                || StringUtils.isBlank(paymentAdvice.getAdviceName())
                || StringUtils.isBlank(paymentAdvice.getBeneficiary())
                || StringUtils.isBlank(paymentAdvice.getDrawingAmount())
                || StringUtils.isBlank(paymentAdvice.getPaymentDate())
                || StringUtils.isBlank(paymentAdvice.getCurrency())
                || (StringUtils.isBlank(paymentAdvice.getCreditedAmount()) && StringUtils.isBlank(paymentAdvice.getDebitedAmount()))
                || (StringUtils.isBlank(paymentAdvice.getCreditedAccount()) && StringUtils.isBlank(paymentAdvice.getDebitedAccount()))
                || StringUtils.isBlank(paymentAdvice.getCharges())
                || (StringUtils.isBlank(paymentAdvice.getAdvisingBank()) && StringUtils.isBlank(paymentAdvice.getIssuingBank()))
                || StringUtils.isBlank(paymentAdvice.getMessage())) {
            diagnostic.prepareDebug("Mandatory fields are missing.").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }

        try {
            if (StringUtils.isNotBlank(paymentAdvice.getDrawingReferenceNo())) {
                ExportLCDrawingsDTO drawings = drawingsBackendDelegate.getExportLetterOfCreditDrawingById(request, paymentAdvice.getDrawingReferenceNo());
                if (!drawings.getStatus().equals(PARAM_STATUS_COMPLETED) && !drawings.getPaymentStatus().equals(PARAM_STATUS_SETTLED)) {
                    return ErrorCodeEnum.ERRTF_29078.setErrorCode(result);
                }
            }
            paymentAdviceResponse = paymentsBusinessDelegate.createPaymentAdvice(paymentAdvice, request);
        } catch (Exception e) {
            alert.prepareError("Create Order Failed in resources" + e).log();
            return ErrorCodeEnum.ERRTF_29064.setErrorCode(result, "Failed to Create payment advices");
        }

        JSONObject responseObj = new JSONObject(paymentAdviceResponse);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    public Result getPaymentAdvice(DataControllerRequest request) {
        List<PaymentAdviceDTO> paymentAdvice;
        Result result = new Result();

        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        PaymentAdviceBussinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);
        try {
            paymentAdvice = businessDelegate.getPaymentAdvice(request);
        } catch (Exception e) {
            alert.prepareError("Failed to get Payment Advices" + e).log();
            return ErrorCodeEnum.ERRTF_29069.setErrorCode(result, "Failed to get Payment Advices");
        }

        JSONObject responseObj = new JSONObject();
        responseObj.put("PaymentAdvices", paymentAdvice);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

}
