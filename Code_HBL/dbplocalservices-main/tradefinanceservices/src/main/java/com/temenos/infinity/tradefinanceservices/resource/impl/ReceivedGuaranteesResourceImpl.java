/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ReceivedGuaranteesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.ReceivedGuaranteesResource;
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

public class ReceivedGuaranteesResourceImpl implements ReceivedGuaranteesResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final ReceivedGuaranteesBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ReceivedGuaranteesBusinessDelegate.class);
    private final PaymentAdviceBussinessDelegate paymentAdviceBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);
    private final GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate swiftMessagesBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class);

    @Override
    public Result createReceivedGuarantee(ReceivedGuaranteesDTO inputDTO, DataControllerRequest request) throws DBPApplicationException {
        inputDTO.setReceivedOn(getCurrentDateTimeUTF());
        inputDTO.setSelfAcceptance(PARAM_STATUS_PENDING);
        inputDTO.setStatus(PARAM_STATUS_NEW);
        inputDTO.setUtilizedAmount("0");
        inputDTO.setReleasedAmount("0");
        ReceivedGuaranteesDTO responseDTO = requestBusinessDelegate.createReceivedGuarantee(inputDTO, request);
        JSONObject response = new JSONObject(responseDTO);
        return JSONToResult.convert(String.valueOf(response));
    }

    @Override
    public Result getReceivedGuarantees(FilterDTO filterDto, DataControllerRequest request) {
        List<ReceivedGuaranteesDTO> receivedGuaranteesList = requestBusinessDelegate.getReceivedGuarantees(request);
        if (receivedGuaranteesList == null) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result());
        }
        if (StringUtils.isNotBlank(filterDto.get_searchString())) {
            receivedGuaranteesList = TradeFinanceCommonUtils.searchBy(receivedGuaranteesList, filterDto.get_searchString());
            filterDto.set_searchString("");
        }
        receivedGuaranteesList = filterDto.filter(receivedGuaranteesList);
        JSONObject response = new JSONObject();
        response.put("Guarantees", receivedGuaranteesList);
        return JSONToResult.convert(String.valueOf(response));
    }

    @Override
    public Result getReceivedGuaranteeById(DataControllerRequest request) {
        String guaranteeSrmsId = request.getParameter("guaranteeSrmsId");
        if (StringUtils.isBlank(guaranteeSrmsId)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        ReceivedGuaranteesDTO responseDto = requestBusinessDelegate.getReceivedGuaranteeById(guaranteeSrmsId, request);
        JSONObject responseObj = new JSONObject(responseDto);
        try {
            request.addRequestParam_("orderId", guaranteeSrmsId);
            request.addRequestParam_("module", "RGUA");
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
    public Result updateReceivedGuarantee(ReceivedGuaranteesDTO inputDTO, DataControllerRequest request) {
        if (StringUtils.isBlank(inputDTO.getGuaranteeSrmsId())
                || StringUtils.isBlank(inputDTO.getSelfAcceptance())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        ReceivedGuaranteesDTO guaranteeDetails = requestBusinessDelegate.getReceivedGuaranteeById(inputDTO.getGuaranteeSrmsId(), request);
        if (StringUtils.isNotBlank(guaranteeDetails.getDbpErrMsg())) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result(), guaranteeDetails.getDbpErrMsg());
        }

        if (!guaranteeDetails.getSelfAcceptance().equalsIgnoreCase(PARAM_STATUS_PENDING)) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }

        guaranteeDetails.setSelfAcceptanceDate(getCurrentDateTimeUTF());
        if (inputDTO.getSelfAcceptance().equals(PARAM_STATUS_ACCEPTED)) {
            guaranteeDetails.setReasonForSelfRejection("");
            guaranteeDetails.setMessageToBank("");
        } else if (inputDTO.getSelfAcceptance().equals(PARAM_STATUS_REJECTED)) {
            if (StringUtils.isBlank(inputDTO.getReasonForSelfRejection())) {
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }
            JSONArray rejectionHistories = StringUtils.isNotBlank(guaranteeDetails.getSelfRejectionHistory()) ? new JSONArray(guaranteeDetails.getSelfRejectionHistory()) : new JSONArray();
            if (rejectionHistories.length() == 5) {
                return ErrorCodeEnum.ERRTF_29073.setErrorCode(new Result());
            }
            JSONObject rejectionHistory = new JSONObject();
            rejectionHistory.put("rejectionDate", guaranteeDetails.getSelfAcceptanceDate());
            rejectionHistory.put("rejectionCount", rejectionHistories.length() + 1);
            guaranteeDetails.setReasonForSelfRejection(inputDTO.getReasonForSelfRejection());
            rejectionHistory.put("reasonForSelfRejection", guaranteeDetails.getReasonForSelfRejection());
            if (StringUtils.isNotBlank(inputDTO.getMessageToBank())) {
                guaranteeDetails.setMessageToBank(inputDTO.getMessageToBank());
                rejectionHistory.put("messageToBank", inputDTO.getMessageToBank());
            }
            rejectionHistories.put(rejectionHistory);
            guaranteeDetails.setSelfRejectionHistory(String.valueOf(rejectionHistories));
        } else {
            alert.prepareError("Invalid status found in request payload").log();
            return ERR_12002.setErrorCode(new Result());
        }

        guaranteeDetails.setSelfAcceptance(inputDTO.getSelfAcceptance());
        guaranteeDetails.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        ReceivedGuaranteesDTO responseDto = requestBusinessDelegate.updateReceivedGuarantee(guaranteeDetails, request);
        Result result = JSONToResult.convert(String.valueOf(new JSONObject(responseDto)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.GUARANTEES_RECEIVED_SUBMITTED, responseDto.getGuaranteeSrmsId());
        return result;
    }

    @Override
    public Result updateReceivedGuaranteeByBank(ReceivedGuaranteesDTO inputDto, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        if (StringUtils.isBlank(inputDto.getGuaranteeSrmsId())
                || StringUtils.isBlank(inputDto.getStatus())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        ReceivedGuaranteesDTO guaranteeDetails = requestBusinessDelegate.getReceivedGuaranteeById(inputDto.getGuaranteeSrmsId(), request);
        if (guaranteeDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_NEW)
                || guaranteeDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_CLAIMHONOURED)
                || guaranteeDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_REJECTED)
                || guaranteeDetails.getStatus().equalsIgnoreCase(inputDto.getStatus())) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }

        switch (inputDto.getStatus()) {
            case PARAM_STATUS_RETURNED_BY_BANK:
                break;
            case PARAM_STATUS_PROCESSING_WITH_BANK:
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_PENDING;
                break;
            case PARAM_STATUS_APPROVED:
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_APPROVED;
                break;
            case PARAM_STATUS_REJECTED:
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_REJECETED;
                break;
            case PARAM_STATUS_CLAIMHONOURED:
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED;
                break;
            default:
                alert.prepareError("Invalid status found in request payload").log();
                return ERR_12002.setErrorCode(new Result());
        }

        guaranteeDetails.setStatus(inputDto.getStatus());
        ReceivedGuaranteesDTO responseDto = requestBusinessDelegate.updateReceivedGuarantee(guaranteeDetails, request);

        Result result = JSONToResult.convert(String.valueOf(new JSONObject(responseDto)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, responseDto.getGuaranteeSrmsId());
        return result;
    }

    @Override
    public Result generateReceivedGuarantee(DataControllerRequest request) {
        String guaranteeSrmsId = request.getParameter("guaranteeSrmsId");
        if (StringUtils.isBlank(guaranteeSrmsId)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        ReceivedGuaranteesDTO guaranteeDetailsDTO = requestBusinessDelegate.getReceivedGuaranteeById(guaranteeSrmsId, request);
        byte[] pdfBytes = requestBusinessDelegate.generateReceivedGuarantee(guaranteeDetailsDTO, request);
        if (ArrayUtils.isEmpty(pdfBytes)) {
            alert.prepareError("Failed to generate report").log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
        }
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(PREFIX_RECEIVED_GUARANTEE);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
        Result result = new Result();
        result.addParam("fileId", fileId);
        return result;
    }

    public Result releaseLiability(DataControllerRequest request) {
        if (StringUtils.isBlank(request.getParameter("guaranteeSrmsId"))
                || StringUtils.isBlank(request.getParameter("releaseDate"))
                || StringUtils.isBlank(request.getParameter("amountToRelease"))
                || StringUtils.isBlank(request.getParameter("liabilityDetails"))) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        ReceivedGuaranteesDTO guaranteeDetails = requestBusinessDelegate.getReceivedGuaranteeById(request.getParameter("guaranteeSrmsId"), request);
        if (StringUtils.isBlank(guaranteeDetails.getGuaranteeSrmsId())) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result());
        }
        if (!(guaranteeDetails.getStatus().equals(PARAM_STATUS_APPROVED)
                || guaranteeDetails.getStatus().equals(PARAM_STATUS_CLAIMHONOURED)
                || guaranteeDetails.getStatus().equals(PARAM_STATUS_CLAIMEXTENDED)
                || guaranteeDetails.getStatus().equals(PARAM_STATUS_CLAIMREJECTED))) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }

        double amountToRelease = Double.parseDouble(request.getParameter("amountToRelease"));
        if (Double.parseDouble(guaranteeDetails.getAmount()) - Double.parseDouble(guaranteeDetails.getReleasedAmount()) <= amountToRelease) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result(), "Invalid Amount to Release");
        }
        guaranteeDetails.setReleasedAmount(String.valueOf(Double.parseDouble(guaranteeDetails.getReleasedAmount()) + amountToRelease));

        JSONArray liabilitiesList = StringUtils.isNotBlank(guaranteeDetails.getLiabilityDetails()) ? new JSONArray(guaranteeDetails.getLiabilityDetails()) : new JSONArray();

        JSONObject liabilityDetails = new JSONObject();
        liabilityDetails.put("liabilityNo", liabilitiesList.length() + 1);
        liabilityDetails.put("currency", guaranteeDetails.getCurrency());
        liabilityDetails.put("releaseDate", request.getParameter("releaseDate"));
        liabilityDetails.put("amountToRelease", request.getParameter("amountToRelease"));
        liabilityDetails.put("liabilityDetails", request.getParameter("liabilityDetails"));
        liabilityDetails.put("messageToBank", StringUtils.isNotBlank(request.getParameter("messageToBank")) ? request.getParameter("messageToBank") : "");
        liabilityDetails.put("createdDate", getCurrentDateTimeUTF());
        liabilityDetails.put("status", PARAM_STATUS_REQUESTED);

        liabilitiesList.put(liabilityDetails);
        guaranteeDetails.setLiabilityDetails(String.valueOf(liabilitiesList));

        ReceivedGuaranteesDTO responseDto = requestBusinessDelegate.updateReceivedGuarantee(guaranteeDetails, request);
        JSONObject response = new JSONObject(responseDto);
        response.put("status", StringUtils.isBlank(responseDto.getErrorMsg()) ? "success" : "failure");
        return JSONToResult.convert(String.valueOf(response));
    }

}
