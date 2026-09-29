/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.PaymentAllocationBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.dto.PaymentAllocationDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.PaymentAllocationResource;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.PARAM_ROLE_ANCHOR;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.PARAM_ROLE_COUNTERPARTY;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceStatus.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.*;

/**
 * @author k.meiyazhagan
 */
public class PaymentAllocationResourceImpl implements PaymentAllocationResource {
    private static final JSONArray allowedDocTypes = new JSONArray(Arrays.toString(new String[]{"pdf", "jpeg", "xls", "csv"}));

    @Override
    public PaymentAllocationDTO createPaymentAllocation(PaymentAllocationDTO inputDto, DataControllerRequest request) {
        PaymentAllocationBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAllocationBusinessDelegate.class);
        String customerId = getCoreCustomerId(request);
        List<String> possibleStatus = Arrays.asList(PARAM_STATUS_REQUESTED, PARAM_STATUS_PENDING, PARAM_STATUS_SUBMITTED);

        if (StringUtils.isBlank(inputDto.getSenderId())
                || StringUtils.isBlank(inputDto.getSenderName())
                || StringUtils.isBlank(inputDto.getBeneficiaryId())
                || StringUtils.isBlank(inputDto.getBeneficiaryName())
                || !Arrays.asList(inputDto.getBeneficiaryId(), inputDto.getSenderId()).contains(customerId)
                || StringUtils.isBlank(inputDto.getTransactionId())
                || StringUtils.isBlank(inputDto.getCurrency())
                || StringUtils.isBlank(inputDto.getReceiptAmount())
                || StringUtils.isBlank(inputDto.getOriginalAmount())
                || StringUtils.isBlank(inputDto.getValueDate())
                || StringUtils.isBlank(inputDto.getStatus())
                || !possibleStatus.contains(inputDto.getStatus())) {
            inputDto = new PaymentAllocationDTO();
            inputDto.setDbpErrCode(ErrorCodeEnum.ERR_30001.getErrorCodeAsString());
            inputDto.setDbpErrMsg(ErrorCodeEnum.ERR_30001.getErrorMessage());
            return inputDto;
        }


        if (StringUtils.equals(inputDto.getStatus(), PARAM_STATUS_SUBMITTED)) {
            // submitted status records must have funding request documents
            if (StringUtils.isBlank(inputDto.getFundingDocuments())
                    || !areValidDocuments(inputDto.getFundingDocuments(), allowedDocTypes)
                    || StringUtils.isBlank(inputDto.getUploadedFrom())) {
                inputDto = new PaymentAllocationDTO();
                inputDto.setDbpErrCode(ErrorCodeEnum.ERR_30001.getErrorCodeAsString());
                inputDto.setDbpErrMsg(ErrorCodeEnum.ERR_30001.getErrorMessage());
            }
            inputDto.setUploadedBy(StringUtils.equals(inputDto.getSenderId(), customerId) ? customerId : inputDto.getBeneficiaryId());
        } else {
            inputDto.setUploadedBy(null);
            inputDto.setUploadedFrom(null);
            inputDto.setFundingDocuments(null);
        }

        inputDto.setCreatedDate(getCurrentDateTimeUTF());
        inputDto.setUpdatedDate(inputDto.getCreatedDate());
        return requestBusiness.createPaymentAllocation(inputDto, request);
    }

    @Override
    public Result getPaymentAllocations(DataControllerRequest request) {
        PaymentAllocationBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAllocationBusinessDelegate.class);
        List<PaymentAllocationDTO> allRecords = requestBusiness.getPaymentAllocations(request);
        String backendId = getCoreCustomerId(request);
        List<PaymentAllocationDTO> filteredRecords = new LinkedList<>();
        for (PaymentAllocationDTO record : allRecords) {
            if (StringUtils.equals(record.getSenderId(), backendId)
                    || StringUtils.equals(record.getBeneficiaryId(), backendId))
                filteredRecords.add(record);
        }
        return JSONToResult.convert(new JSONObject().put("PaymentAllocations", filteredRecords).toString());
    }

    @Override
    public Result requestPaymentAllocationDocuments(PaymentAllocationDTO inputDto, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDto.getPaymentAllocationId())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        PaymentAllocationBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAllocationBusinessDelegate.class);
        PaymentAllocationDTO responseDto = requestBusiness.getPaymentAllocationById(inputDto.getPaymentAllocationId(), request);
        if (StringUtils.isBlank(responseDto.getStatus())
                || !StringUtils.equals(responseDto.getStatus(), PARAM_STATUS_PENDING)) {
            return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
        }

        responseDto.setStatus(PARAM_STATUS_REQUESTED);
        responseDto.setUpdatedDate(getCurrentDateTimeUTF());
        responseDto = requestBusiness.updatePaymentAllocation(responseDto, request);
        return JSONToResult.convert(String.valueOf(new JSONObject(responseDto)));
    }

    @Override
    public Result submitPaymentAllocationDocuments(PaymentAllocationDTO inputDto, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDto.getPaymentAllocationId())
                || StringUtils.isBlank(inputDto.getFundingDocuments())
                || !areValidDocuments(inputDto.getFundingDocuments(), allowedDocTypes)) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        PaymentAllocationBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAllocationBusinessDelegate.class);
        PaymentAllocationDTO responseDto = requestBusiness.getPaymentAllocationById(inputDto.getPaymentAllocationId(), request);
        if (StringUtils.isBlank(responseDto.getStatus())) {
            return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
        }
        if (StringUtils.isEmpty(responseDto.getUploadedFrom())) {
            responseDto.setUploadedFrom(StringUtils.equals(responseDto.getStatus(), PARAM_STATUS_PENDING) ? PARAM_ROLE_ANCHOR : PARAM_ROLE_COUNTERPARTY);
            responseDto.setUploadedBy(getCoreCustomerId(request));
        }
        responseDto.setFundingDocuments(inputDto.getFundingDocuments());
        responseDto.setStatus(PARAM_STATUS_SUBMITTED);
        responseDto.setUpdatedDate(getCurrentDateTimeUTF());
        responseDto = requestBusiness.updatePaymentAllocation(responseDto, request);
        return JSONToResult.convert(String.valueOf(new JSONObject(responseDto)));
    }
}