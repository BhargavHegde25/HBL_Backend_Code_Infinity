/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ReceivedGuaranteeAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ReceivedGuaranteesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.ReceivedGuaranteeAmendmentsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERR_12002;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCurrentDateTimeUTF;

public class ReceivedGuaranteeAmendmentsResourceImpl implements ReceivedGuaranteeAmendmentsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final ReceivedGuaranteeAmendmentsBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ReceivedGuaranteeAmendmentsBusinessDelegate.class);
    private final ReceivedGuaranteesBusinessDelegate requestGuaranteeBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ReceivedGuaranteesBusinessDelegate.class);
    private final PaymentAdviceBussinessDelegate paymentAdviceBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);
    private final ReceivedGuaranteeAmendmentsBusinessDelegate amendmentBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ReceivedGuaranteeAmendmentsBusinessDelegate.class);
    private final GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate swiftMessagesBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class);

    @Override
    public Result createReceivedAmendment(ReceivedAmendmentsDTO inputDto, DataControllerRequest request) {
        ReceivedGuaranteesDTO guaranteeDetails = requestGuaranteeBusinessDelegate.getReceivedGuaranteeById(inputDto.getGuaranteeSrmsId(), request);
        ReceivedAmendmentsDTO responseDTO = new ReceivedAmendmentsDTO();
        try {
            if (!guaranteeDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_APPROVED)) {
                return ErrorCodeEnum.ERRTF_29073.setErrorCode(new Result());
            }

            JSONObject lastAmendmentDetails = StringUtils.isNotBlank(guaranteeDetails.getLastAmendmentDetails()) ? new JSONObject(guaranteeDetails.getLastAmendmentDetails()) : new JSONObject();
            int amendmentNo = lastAmendmentDetails.has("amendmentNo") ? lastAmendmentDetails.getInt("amendmentNo") : 0;

            inputDto.setLcType(guaranteeDetails.getLcType());
            inputDto.setProductType(guaranteeDetails.getProductType());
            inputDto.setCurrency(guaranteeDetails.getCurrency());
            inputDto.setAmount(guaranteeDetails.getAmount());
            inputDto.setAmendmentNo(String.valueOf(amendmentNo + 1));
            inputDto.setReceivedOn(getCurrentDateTimeUTF());
            inputDto.setSelfAcceptance(PARAM_STATUS_PENDING);
            inputDto.setStatus(PARAM_STATUS_NEW);
            responseDTO = requestBusinessDelegate.createReceivedAmendment(inputDto, request);

            if (StringUtils.isNotBlank(responseDTO.getAmendmentSrmsId())) {
                lastAmendmentDetails.put("amendmentStatus", PARAM_STATUS_NEW);
                lastAmendmentDetails.put("amendmentSrmsId", responseDTO.getAmendmentSrmsId());
                lastAmendmentDetails.put("amendmentNo", amendmentNo + 1);
                guaranteeDetails.setLastAmendmentDetails(String.valueOf(lastAmendmentDetails));
                requestGuaranteeBusinessDelegate.updateReceivedGuarantee(guaranteeDetails, request);
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating record. ", e).log();
            responseDTO.setDbpErrCode(ErrorCodeEnum.ERR_12002.getErrorCodeAsString());
            responseDTO.setDbpErrMsg(ErrorCodeEnum.ERR_12002.getErrorMessage());
            responseDTO.setErrorMsg(e.getMessage());
        }
        JSONObject response = new JSONObject(responseDTO);
        return JSONToResult.convert(String.valueOf(response));
    }

    @Override
    public Result updateReceivedAmendment(ReceivedAmendmentsDTO inputDTO, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDTO.getAmendmentSrmsId())
                || StringUtils.isBlank(inputDTO.getSelfAcceptance())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        ReceivedAmendmentsDTO amendmentDetails = requestBusinessDelegate.getReceivedAmendmentById(inputDTO.getAmendmentSrmsId(), request);
        if (StringUtils.isNotBlank(amendmentDetails.getDbpErrMsg())) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result(), amendmentDetails.getDbpErrMsg());
        }

        if (amendmentDetails.getSelfAcceptance().equalsIgnoreCase(PARAM_STATUS_ACCEPTED)
                || !amendmentDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_NEW)) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }

        if (inputDTO.getSelfAcceptance().equals(PARAM_STATUS_ACCEPTED)) {
            amendmentDetails.setReasonForSelfRejection("");
            amendmentDetails.setMessageToBank("");
        } else if (inputDTO.getSelfAcceptance().equals(PARAM_STATUS_REJECTED)) {
            if (StringUtils.isBlank(inputDTO.getReasonForSelfRejection())) {
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }
            amendmentDetails.setReasonForSelfRejection(inputDTO.getReasonForSelfRejection());
            if (StringUtils.isNotBlank(inputDTO.getMessageToBank())) {
                amendmentDetails.setMessageToBank(inputDTO.getMessageToBank());
            }
        } else {
            alert.prepareError("Invalid status found in request payload").log();
            return ErrorCodeEnum.ERR_12002.setErrorCode(new Result());
        }

        amendmentDetails.setSelfAcceptanceDate(getCurrentDateTimeUTF());
        amendmentDetails.setSelfAcceptance(inputDTO.getSelfAcceptance());
        amendmentDetails.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        ReceivedAmendmentsDTO responseDto = requestBusinessDelegate.updateReceivedAmendment(amendmentDetails, request);
        Result result = JSONToResult.convert(String.valueOf(new JSONObject(responseDto)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED, responseDto.getAmendmentSrmsId());
        return result;
    }

    @Override
    public Result getReceivedAmendments(FilterDTO filterDto, DataControllerRequest request) {
        List<ReceivedAmendmentsDTO> receivedAmendmentsList = requestBusinessDelegate.getReceivedAmendments(request);
        if (receivedAmendmentsList == null) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result());
        }
        receivedAmendmentsList = filterDto.filter(receivedAmendmentsList);
        JSONObject response = new JSONObject();
        response.put("ReceivedAmendments", receivedAmendmentsList);
        return JSONToResult.convert(response.toString());
    }

    @Override
    public Result getReceivedAmendmentById(DataControllerRequest request) {
        String amendmentSrmsId = request.getParameter("amendmentSrmsId");
        if (StringUtils.isBlank(amendmentSrmsId)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        ReceivedAmendmentsDTO responseDto = requestBusinessDelegate.getReceivedAmendmentById(amendmentSrmsId, request);
        JSONObject responseObj = new JSONObject(responseDto);
        responseObj.put("SwiftMessages", new JSONArray());
        try {
            request.addRequestParam_("orderId", amendmentSrmsId);
            request.addRequestParam_("module", "RGAM");
            List<SwiftsAndAdvisesDTO> swiftMessages = swiftMessagesBusinessDelegate.getGuaranteeSwiftAdvices(request);
            responseObj.put("SwiftMessages", new JSONArray(swiftMessages));
            List<PaymentAdviceDTO> paymentAdvices = paymentAdviceBusinessDelegate.getPaymentAdvice(request);
            responseObj.put("PaymentAdvices", new JSONArray(paymentAdvices));
        } catch (Exception e) {
            alert.prepareError("Failed to get Payment Advices", e).log();
        }
        return JSONToResult.convert(responseObj.toString());
    }

    @Override
    public Result generateReceivedAmendment(DataControllerRequest request) {
        String amendmentId = request.getParameter("amendmentSrmsId");
        if (StringUtils.isBlank(amendmentId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }
        ReceivedAmendmentsDTO amendmentsDTO = amendmentBusinessDelegate.getReceivedAmendmentById(amendmentId, request);
        ReceivedGuaranteesBusinessDelegate receivedGuaranteeBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ReceivedGuaranteesBusinessDelegate.class);
        ReceivedGuaranteesDTO receivedGuaranteeDTO = receivedGuaranteeBusinessDelegate.getReceivedGuaranteeById(amendmentsDTO.getGuaranteeSrmsId(), request);
        byte[] pdfBytes = amendmentBusinessDelegate.generateReceivedAmendment(amendmentsDTO, receivedGuaranteeDTO, request);
        if (ArrayUtils.isEmpty(pdfBytes)) {
            alert.prepareError("Failed to generate report").log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
        }
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(PREFIX_RECEIVED_GUARANTEE_AMENDMENT);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
        Result result = new Result();
        result.addParam("fileId", fileId);
        return result;
    }

    @Override
    public Result updateReceivedAmendmentByBank(ReceivedAmendmentsDTO inputDTO, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        if (StringUtils.isBlank(inputDTO.getAmendmentSrmsId())
                || StringUtils.isBlank(inputDTO.getStatus())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        ReceivedAmendmentsDTO amendmentDetails = requestBusinessDelegate.getReceivedAmendmentById(inputDTO.getAmendmentSrmsId(), request);
        if (StringUtils.isNotBlank(amendmentDetails.getDbpErrMsg())) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result(), amendmentDetails.getDbpErrMsg());
        }
        if (amendmentDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_NEW)
                || amendmentDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_APPROVED)
                || amendmentDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_REJECTED)
                || amendmentDetails.getStatus().equalsIgnoreCase(inputDTO.getStatus())) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }

        switch (inputDTO.getStatus()) {
            case PARAM_STATUS_RETURNED_by_BANK:
                break;
            case PARAM_STATUS_PROCESSING_WITH_BANK:
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_AMENDMENT_PENDING;
                break;
            case PARAM_STATUS_APPROVED:
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_AMENDMENT_APPROVED;
                break;
            case PARAM_STATUS_REJECTED:
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_AMENDMENT_REJECTED;
                break;
            default:
                alert.prepareError("Invalid status found in request payload").log();
                return ERR_12002.setErrorCode(new Result());
        }

        if (inputDTO.getStatus().equals(PARAM_STATUS_APPROVED)
                || inputDTO.getStatus().equals(PARAM_STATUS_REJECTED)
                || inputDTO.getStatus().equals(PARAM_STATUS_PROCESSING_WITH_BANK)) {
            inputDTO.setReasonForReturn("");
        } else if (inputDTO.getStatus().equals(PARAM_STATUS_RETURNED_BY_BANK)) {
            if (StringUtils.isBlank(inputDTO.getReasonForReturn())) {
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }
        } else {
            alert.prepareError("Invalid status found in request payload").log();
            return ERR_12002.setErrorCode(new Result());
        }

        amendmentDetails.setStatus(inputDTO.getStatus());
        amendmentDetails.setLastUpdatedTimeStamp(getCurrentDateTimeUTF());
        ReceivedAmendmentsDTO responseDto = requestBusinessDelegate.updateReceivedAmendment(amendmentDetails, request);
        Result result = JSONToResult.convert(String.valueOf(new JSONObject(responseDto)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, responseDto.getAmendmentSrmsId());
        return result;
    }
}
