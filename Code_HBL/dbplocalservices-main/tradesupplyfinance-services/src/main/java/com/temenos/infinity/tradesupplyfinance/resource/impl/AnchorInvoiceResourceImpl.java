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
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.AnchorInvoiceBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.AnchorInvoiceResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradesupplyfinance.config.TradeSupplyFinanceAPIServices.TRANSACT_SCF_CREATE_INVOICE;
import static com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum.ERR_30027;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceStatus.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.*;

/**
 * @author k.meiyazhagan
 */
public class AnchorInvoiceResourceImpl implements AnchorInvoiceResource {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result saveAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        AnchorInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorInvoiceBusinessDelegate.class);
        inputDto.setUpdatedDate(getCurrentDateTimeUTF());

        AnchorInvoiceDTO responseDTO;
        if (StringUtils.isBlank(inputDto.getInvoiceReference())) {
            inputDto.setCreatedDate(inputDto.getUpdatedDate());
            inputDto.setStatus(PARAM_STATUS_DRAFT);
            responseDTO = requestBusiness.createAnchorInvoice(inputDto, request);
        } else {
            responseDTO = requestBusiness.getAnchorInvoiceById(inputDto.getInvoiceReference(), request);
            if (StringUtils.isBlank(responseDTO.getInvoiceReference())
                    || !StringUtils.equals(responseDTO.getStatus(), PARAM_STATUS_DRAFT)) {
                return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
            }
            inputDto.setCreatedDate(responseDTO.getCreatedDate());
            inputDto.setStatus(responseDTO.getStatus());
            responseDTO = requestBusiness.updateAnchorInvoice(inputDto, request);
        }

        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
    }

    @Override
    public Result deleteAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDto.getInvoiceReference())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        AnchorInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorInvoiceBusinessDelegate.class);
        AnchorInvoiceDTO invoiceDto = requestBusiness.getAnchorInvoiceById(inputDto.getInvoiceReference(), request);
        if (StringUtils.isBlank(invoiceDto.getInvoiceReference())
                || !StringUtils.equals(invoiceDto.getStatus(), PARAM_STATUS_DRAFT)) {
            return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
        }
        invoiceDto.setUpdatedDate(getCurrentDateTimeUTF());
        invoiceDto.setStatus(PARAM_STATUS_DELETED);
        invoiceDto = requestBusiness.updateAnchorInvoice(invoiceDto, request);

        return JSONToResult.convert(String.valueOf(new JSONObject(invoiceDto)));
    }

    @Override
    public Result getAllAnchorInvoices(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        AnchorInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorInvoiceBusinessDelegate.class);
        List<AnchorInvoiceDTO> invoices = requestBusiness.getAllAnchorInvoices(request);
        return JSONToResult.convert((new JSONObject()).put("AnchorInvoices", invoices).toString());
    }

    @Override
    public Result submitAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDto.getInvoiceReference())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        AnchorInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorInvoiceBusinessDelegate.class);
        AnchorInvoiceDTO invoiceDto = requestBusiness.getAnchorInvoiceById(inputDto.getInvoiceReference(), request);
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
            AnchorInvoiceDTO responseDTO = createInvoiceInTransact(request, invoiceDto);
            if (StringUtils.isBlank(responseDTO.getInvoiceReference()))
                return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
            // TODO - transact ref to srms
            // invoiceDto.setInvoiceReference(responseDTO.getInvoiceReference());
        }

        invoiceDto = requestBusiness.updateAnchorInvoice(invoiceDto, request);
        return JSONToResult.convert(String.valueOf(new JSONObject(invoiceDto)));
    }

    @Override
    public Result rejectAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDto.getInvoiceReference())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        AnchorInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorInvoiceBusinessDelegate.class);
        AnchorInvoiceDTO invoiceDto = requestBusiness.getAnchorInvoiceById(inputDto.getInvoiceReference(), request);
        if (StringUtils.isBlank(invoiceDto.getInvoiceReference())
                || !StringUtils.equals(invoiceDto.getStatus(), PARAM_STATUS_PENDING_APPROVAL)) {
            Result result = new Result();
            result.addParam(PARAM_INVOICE_REFERENCE, invoiceDto.getInvoiceReference());
            return ErrorCodeEnum.ERR_30003.setErrorCode(result);
        }
        invoiceDto.setUpdatedDate(getCurrentDateTimeUTF());
        invoiceDto.setStatus(PARAM_STATUS_REJECTED);
        invoiceDto = requestBusiness.updateAnchorInvoice(invoiceDto, request);

        return JSONToResult.convert(String.valueOf(new JSONObject(invoiceDto)));
    }

    @Override
    public Result approveAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDto.getInvoiceReference())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        AnchorInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorInvoiceBusinessDelegate.class);
        AnchorInvoiceDTO invoiceDto = requestBusiness.getAnchorInvoiceById(inputDto.getInvoiceReference(), request);
        if (StringUtils.isBlank(invoiceDto.getInvoiceReference())
                || !StringUtils.equals(invoiceDto.getStatus(), PARAM_STATUS_PENDING_APPROVAL)) {
            Result result = new Result();
            result.addParam(PARAM_INVOICE_REFERENCE, invoiceDto.getInvoiceReference());
            return ErrorCodeEnum.ERR_30003.setErrorCode(result);
        }
        invoiceDto.setUpdatedDate(getCurrentDateTimeUTF());
        invoiceDto.setStatus(PARAM_STATUS_APPROVED);

        // Call Transact if status is approved
        AnchorInvoiceDTO responseDTO = createInvoiceInTransact(request, invoiceDto);
        if (StringUtils.isBlank(responseDTO.getInvoiceReference()))
            return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
        // TODO - transact ref to srms
        // invoiceDto.setInvoiceReference(responseDTO.getInvoiceReference());

        invoiceDto = requestBusiness.updateAnchorInvoice(invoiceDto, request);
        return JSONToResult.convert(String.valueOf(new JSONObject(invoiceDto)));
    }

    @Override
    public Result createAnchorInvoice(AnchorInvoiceDTO invoiceDto, DataControllerRequest request) {
        AnchorInvoiceBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorInvoiceBusinessDelegate.class);
        invoiceDto.setStatus((StringUtils.equals(invoiceDto.getRole(), PARAM_ROLE_SUPPLIER) && isInvoiceApprovalRequired(request, invoiceDto.getUploadedFrom(), invoiceDto.getRole()))
                ? PARAM_STATUS_PENDING_APPROVAL : PARAM_STATUS_APPROVED);
        invoiceDto.setCreatedDate(getCurrentDateTimeUTF());
        invoiceDto.setUpdatedDate(invoiceDto.getCreatedDate());

        // Call Transact if status is approved
        if (StringUtils.equals(invoiceDto.getStatus(), PARAM_STATUS_APPROVED)) {
            AnchorInvoiceDTO responseDTO = createInvoiceInTransact(request, invoiceDto);
            if (StringUtils.isBlank(responseDTO.getInvoiceReference()))
                return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
            // TODO - transact ref to srms
            // invoiceDto.setInvoiceReference(responseDTO.getInvoiceReference());
        }
        AnchorInvoiceDTO responseDTO = requestBusiness.createAnchorInvoice(invoiceDto, request);
        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
    }

    private AnchorInvoiceDTO createInvoiceInTransact(DataControllerRequest request, AnchorInvoiceDTO invoiceDTO) {
        Map<String, Object> inputMap;
        Result result;
        AnchorInvoiceDTO responseDTO = new AnchorInvoiceDTO();
        try {
            invoiceDTO.setBillType(invoiceDTO.getBillType().toUpperCase());
            inputMap = new ObjectMapper().convertValue(invoiceDTO, Map.class);
            HashMap<String, Object> headerMap = new HashMap<>();
            headerMap.put(HTTP_HEADER_X_KONY_AUTHORIZATION, request.getHeader(HTTP_HEADER_X_KONY_AUTHORIZATION));
            headerMap.put(HTTP_HEADER_X_KONY_REPORTING_PARAMS, request.getHeader(HTTP_HEADER_X_KONY_REPORTING_PARAMS));
            result = CommonUtils.callIntegrationService(request, inputMap, headerMap, TRANSACT_SCF_CREATE_INVOICE.getServiceName(), TRANSACT_SCF_CREATE_INVOICE.getOperationName(), false);
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