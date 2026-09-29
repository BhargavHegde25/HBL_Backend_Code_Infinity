/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.CounterPartyInvoiceBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.dto.CounterPartyInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.CounterPartyInvoiceResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradesupplyfinance.config.TradeSupplyFinanceAPIServices.TRANSACT_SCF_CREATE_INVOICE;
import static com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum.ERR_30027;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.PARAM_INVOICE_REFERENCE;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.PARAM_ROLE_SUPPLIER;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceStatus.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.*;

/**
 * @author k.meiyazhagan
 */
public class CounterPartyInvoiceResourceImpl implements CounterPartyInvoiceResource {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result saveCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request) {
        CounterPartyInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CounterPartyInvoiceBusinessDelegate.class);
        inputDto.setUpdatedDate(getCurrentDateTimeUTF());

        CounterPartyInvoiceDTO responseDTO;
        if (StringUtils.isBlank(inputDto.getInvoiceReference())) {
            inputDto.setCreatedDate(inputDto.getUpdatedDate());
            inputDto.setStatus(PARAM_STATUS_DRAFT);
            responseDTO = requestBusiness.createCounterPartyInvoice(inputDto, request);
        } else {
            responseDTO = requestBusiness.getCounterPartyInvoiceById(inputDto.getInvoiceReference(), request);
            if (StringUtils.isBlank(responseDTO.getInvoiceReference())
                    || !StringUtils.equals(responseDTO.getStatus(), PARAM_STATUS_DRAFT)) {
                return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
            }
            inputDto.setCreatedDate(responseDTO.getCreatedDate());
            inputDto.setStatus(responseDTO.getStatus());
            responseDTO = requestBusiness.updateCounterPartyInvoice(inputDto, request);
        }

        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
    }

    @Override
    public Result deleteCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDto.getInvoiceReference())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        CounterPartyInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CounterPartyInvoiceBusinessDelegate.class);
        CounterPartyInvoiceDTO invoiceDto = requestBusiness.getCounterPartyInvoiceById(inputDto.getInvoiceReference(), request);
        if (StringUtils.isBlank(invoiceDto.getInvoiceReference())
                || !StringUtils.equals(invoiceDto.getStatus(), PARAM_STATUS_DRAFT)) {
            return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
        }
        invoiceDto.setUpdatedDate(getCurrentDateTimeUTF());
        invoiceDto.setStatus(PARAM_STATUS_DELETED);
        invoiceDto = requestBusiness.updateCounterPartyInvoice(invoiceDto, request);

        return JSONToResult.convert(String.valueOf(new JSONObject(invoiceDto)));
    }

    @Override
    public Result getAllCounterPartyInvoices(CounterPartyInvoiceDTO inputDto, DataControllerRequest request) {
        CounterPartyInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CounterPartyInvoiceBusinessDelegate.class);
        List<CounterPartyInvoiceDTO> invoices = requestBusiness.getAllCounterPartyInvoices(request);
        return JSONToResult.convert((new JSONObject()).put("CounterPartyInvoices", invoices).toString());
    }

    @Override
    public Result submitCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDto.getInvoiceReference())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        CounterPartyInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CounterPartyInvoiceBusinessDelegate.class);
        CounterPartyInvoiceDTO invoiceDto = requestBusiness.getCounterPartyInvoiceById(inputDto.getInvoiceReference(), request);
        if (StringUtils.isBlank(invoiceDto.getInvoiceReference())
                || !StringUtils.equals(invoiceDto.getStatus(), PARAM_STATUS_DRAFT)) {
            Result result = new Result();
            result.addParam(PARAM_INVOICE_REFERENCE, invoiceDto.getInvoiceReference());
            return ErrorCodeEnum.ERR_30008.setErrorCode(result);
        }
        invoiceDto.setUpdatedDate(getCurrentDateTimeUTF());
        invoiceDto.setStatus((StringUtils.equals(invoiceDto.getRole(), PARAM_ROLE_SUPPLIER) && isInvoiceApprovalRequired(request, invoiceDto.getUploadedFrom(), invoiceDto.getRole()))
                ? PARAM_STATUS_PENDING_APPROVAL : PARAM_STATUS_APPROVED);
        // Call Transact if status is approved
        if (StringUtils.equals(invoiceDto.getStatus(), PARAM_STATUS_APPROVED)) {
            CounterPartyInvoiceDTO responseDTO = createInvoiceInTransact(request, invoiceDto);
            if (StringUtils.isBlank(responseDTO.getInvoiceReference()))
                return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
            // TODO - transact ref to srms
            // invoiceDto.setInvoiceReference(responseDTO.getInvoiceReference());
        }
        invoiceDto = requestBusiness.updateCounterPartyInvoice(invoiceDto, request);

        return JSONToResult.convert(String.valueOf(new JSONObject(invoiceDto)));
    }

    @Override
    public Result createCounterPartyInvoice(CounterPartyInvoiceDTO invoiceDto, DataControllerRequest request) {
        CounterPartyInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CounterPartyInvoiceBusinessDelegate.class);
        invoiceDto.setStatus((StringUtils.equals(invoiceDto.getRole(), PARAM_ROLE_SUPPLIER) && isInvoiceApprovalRequired(request, invoiceDto.getUploadedFrom(), invoiceDto.getRole()))
                ? PARAM_STATUS_PENDING_APPROVAL : PARAM_STATUS_APPROVED);
        invoiceDto.setCreatedDate(getCurrentDateTimeUTF());
        invoiceDto.setUpdatedDate(invoiceDto.getCreatedDate());

        // Call Transact if status is approved
        if (StringUtils.equals(invoiceDto.getStatus(), PARAM_STATUS_APPROVED)) {
            CounterPartyInvoiceDTO responseDTO = createInvoiceInTransact(request, invoiceDto);
            if (StringUtils.isBlank(responseDTO.getInvoiceReference()))
                return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
            // TODO - transact ref to srms
            // invoiceDto.setInvoiceReference(responseDTO.getInvoiceReference());
        }
        CounterPartyInvoiceDTO responseDTO = requestBusiness.createCounterPartyInvoice(invoiceDto, request);
        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
    }

    private CounterPartyInvoiceDTO createInvoiceInTransact(DataControllerRequest request, CounterPartyInvoiceDTO invoiceDTO) {
        Map<String, Object> inputMap;
        Result result;
        CounterPartyInvoiceDTO responseDTO = new CounterPartyInvoiceDTO();
        try {
            invoiceDTO.setBillType(invoiceDTO.getBillType().toUpperCase());
            inputMap = new ObjectMapper().convertValue(invoiceDTO, Map.class);
            result = CommonUtils.callIntegrationService(request, inputMap, getHeadersMap(request), TRANSACT_SCF_CREATE_INVOICE.getServiceName(), TRANSACT_SCF_CREATE_INVOICE.getOperationName(), false);
            Record headerData = result.getRecordById("header");
            if (StringUtils.equals(headerData.getParamValueByName("status"), "success"))
                responseDTO.setInvoiceReference(headerData.getParamValueByName("id"));
            else if (StringUtils.equals(headerData.getParamValueByName("status"), "failed")) {
                responseDTO.setDbpErrCode(ERR_30027.getErrorCodeAsString());
                responseDTO.setDbpErrMsg(ERR_30027.getErrorMessage());
                responseDTO.setErrorDetails(result.getRecordById("error").getDatasetById("errorDetails").getAllRecords().toString());
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating invoice in transact", e).log();
            responseDTO.setDbpErrCode(ERR_30027.getErrorCodeAsString());
            responseDTO.setDbpErrMsg(ERR_30027.getErrorMessage());
            responseDTO.setErrorDetails("Backend Failed");
        }

        return responseDTO;
    }
}