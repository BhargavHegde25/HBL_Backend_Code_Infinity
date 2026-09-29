/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradelending.businessdelegate.api.PaymentRequestBusinessDelegate;
import com.temenos.infinity.tradelending.constants.ErrorCodeEnum;
import com.temenos.infinity.tradelending.dto.PaymentRequestDTO;
import com.temenos.infinity.tradelending.resource.api.PaymentRequestResource;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import static com.temenos.infinity.tradelending.utils.TradeLendingCommonUtils.getCurrentDateTimeUTF;

public class PaymentRequestResourceImpl implements PaymentRequestResource {

    @Override
    public Result submitPaymentRequest(PaymentRequestDTO inputDto, DataControllerRequest request) {
        PaymentRequestBusinessDelegate paymentReqBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentRequestBusinessDelegate.class);
        PaymentRequestDTO responseDTO;

        inputDto.setCreatedDate(getCurrentDateTimeUTF());
        responseDTO = paymentReqBusinessDelegate.createPaymentRequest(inputDto, request);
        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
    }

    @Override
    public PaymentRequestDTO getPaymentRequestById(PaymentRequestDTO inputDto, DataControllerRequest request) {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        if (StringUtils.isBlank(inputDto.getPaymentRequestId())) {
            paymentRequest.setErrorDetails(ErrorCodeEnum.ERR_30004);
            return paymentRequest;
        }
        PaymentRequestBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentRequestBusinessDelegate.class);
        paymentRequest = businessDelegate.getPaymentRequestById(inputDto.getPaymentRequestId(), request);
        return paymentRequest;
    }

}