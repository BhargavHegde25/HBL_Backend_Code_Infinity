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
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InwardCollectionAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InwardCollectionsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.InwardCollectionAmendmentsResource;
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

public class InwardCollectionAmendmentsResourceImpl implements InwardCollectionAmendmentsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final PaymentAdviceBussinessDelegate paymentAdviceBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);
    private final GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate swiftMessagesBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class);
    private final InwardCollectionAmendmentsBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InwardCollectionAmendmentsBusinessDelegate.class);
    private final InwardCollectionsBusinessDelegate collectionsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InwardCollectionsBusinessDelegate.class);

    @Override
    public Result createInwardCollectionAmendment(InwardCollectionAmendmentsDTO inputDTO, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        InwardCollectionAmendmentsDTO responseDTO;
        if (StringUtils.isBlank(inputDTO.getCollectionSrmsId())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        InwardCollectionsDTO collectionDetails = collectionsBusinessDelegate.getInwardCollectionById(inputDTO.getCollectionSrmsId(), request);
        try {
            if (!collectionDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_APPROVED)) {
                return ErrorCodeEnum.ERRTF_29073.setErrorCode(new Result());
            }

            if (StringUtils.equals(inputDTO.getCancellationStatus(), PARAM_STATUS_REQUESTED)) {
                alertToPush = AlertsEnum.INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED;
            }

            JSONObject lastAmendmentDetails = StringUtils.isNotBlank(collectionDetails.getAmendmentDetails()) ? new JSONObject(collectionDetails.getAmendmentDetails()) : new JSONObject();
            if (lastAmendmentDetails.has("amendmentStatus") && !lastAmendmentDetails.getString("amendmentStatus").equals(PARAM_STATUS_APPROVED)) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result(), "Collection is not eligible to create amendment");
            }

            int amendmentNo = lastAmendmentDetails.has("amendmentNo") ? (int) lastAmendmentDetails.get("amendmentNo") + 1 : 1;
            lastAmendmentDetails.put("amendmentStatus", PARAM_STATUS_NEW);
            lastAmendmentDetails.put("amendmentNo", amendmentNo);

            inputDTO.setAmendmentNo(String.valueOf(amendmentNo));
            inputDTO.setMaturityDate(collectionDetails.getMaturityDate());
            inputDTO.setCurrency(collectionDetails.getCurrency());
            inputDTO.setTenorType(collectionDetails.getTenorType());
            inputDTO.setRemittingBank(collectionDetails.getRemittingBank());
            inputDTO.setCreatedDate(getCurrentDateTimeUTF());
            inputDTO.setStatus(PARAM_STATUS_NEW);
            inputDTO.setDraweeAcknowledgement(PARAM_STATUS_PENDING);
            responseDTO = requestBusinessDelegate.createInwardCollectionAmendment(inputDTO, request);

            if (StringUtils.isNotBlank(responseDTO.getAmendmentSrmsId())) {
                lastAmendmentDetails.put("amendmentSrmsId", responseDTO.getAmendmentSrmsId());
                collectionDetails.setAmendmentDetails(String.valueOf(lastAmendmentDetails));
                collectionsBusinessDelegate.updateInwardCollection(collectionDetails, request);
            }
        } catch (Exception e) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result(), "Requested Collection is not found");
        }

        Result result = JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, responseDTO.getAmendmentSrmsId());
        return result;
    }

    @Override
    public Result getInwardCollectionAmendments(FilterDTO filterDto, DataControllerRequest request) {
        List<InwardCollectionAmendmentsDTO> inwardCollections = requestBusinessDelegate.getInwardCollectionAmendments(request);
        if (inwardCollections == null) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result());
        }
        inwardCollections = filterDto.filter(inwardCollections);
        JSONObject response = new JSONObject();
        response.put("InwardCollectionAmendments", inwardCollections);
        return JSONToResult.convert(String.valueOf(response));
    }

    @Override
    public Result getInwardCollectionAmendmentById(DataControllerRequest request) {
        String amendmentSrmsId = request.getParameter("amendmentSrmsId");
        if (StringUtils.isBlank(amendmentSrmsId)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        InwardCollectionAmendmentsDTO responseDto = requestBusinessDelegate.getInwardCollectionAmendmentById(amendmentSrmsId, request);
        JSONObject responseObj = new JSONObject(responseDto);
        try {
            request.addRequestParam_("orderId", amendmentSrmsId);
            request.addRequestParam_("module", "INCA");
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
    public Result updateInwardCollectionAmendment(InwardCollectionAmendmentsDTO inputDTO, DataControllerRequest request) {
        if (inputDTO.getAmendmentSrmsId() == null)
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        InwardCollectionAmendmentsDTO amendmentsDTO = requestBusinessDelegate.getInwardCollectionAmendmentById(inputDTO.getAmendmentSrmsId(), request);
        amendmentsDTO.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        amendmentsDTO.setDraweeAcknowledgement(inputDTO.getDraweeAcknowledgement());
        amendmentsDTO.setMessageToBank(inputDTO.getMessageToBank());
        amendmentsDTO.setReasonForRejection(inputDTO.getReasonForRejection());
        amendmentsDTO.setDraweeAcknowledgementDate(getCurrentDateTimeUTF());
        inputDTO = this.requestBusinessDelegate.updateInwardCollectionAmendment(amendmentsDTO, request);
        if (inputDTO != null && StringUtils.isBlank(inputDTO.getDbpErrMsg()) && StringUtils.isBlank(inputDTO.getDbpErrCode())) {
            InwardCollectionsDTO collectionsDTO = collectionsBusinessDelegate.getInwardCollectionById(inputDTO.getCollectionSrmsId(), request);
            JSONObject lastAmendmentDetails = new JSONObject(collectionsDTO.getAmendmentDetails());
            lastAmendmentDetails.put("amendmentStatus", amendmentsDTO.getStatus());
            collectionsDTO.setAmendmentDetails(String.valueOf(lastAmendmentDetails));
            collectionsBusinessDelegate.updateInwardCollection(collectionsDTO, request);
        }
        Result result = JSONToResult.convert((new JSONObject(inputDTO)).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED, inputDTO.getAmendmentSrmsId());
        return result;
    }

    @Override
    public Result updateInwardCollectionAmendmentByBank(InwardCollectionAmendmentsDTO amendmentDetails, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        if (StringUtils.isBlank(amendmentDetails.getAmendmentSrmsId())
                || StringUtils.isBlank(amendmentDetails.getStatus())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        InwardCollectionAmendmentsDTO amendmentDTO = requestBusinessDelegate.getInwardCollectionAmendmentById(amendmentDetails.getAmendmentSrmsId(), request);
        InwardCollectionsDTO collectionsDTO = collectionsBusinessDelegate.getInwardCollectionById(amendmentDTO.getCollectionSrmsId(), request);
        if (amendmentDTO.getStatus().equals(amendmentDetails.getStatus())
                || amendmentDTO.getStatus().equals(PARAM_STATUS_NEW)
                || amendmentDTO.getStatus().equals(PARAM_STATUS_REJECTED)
                || amendmentDTO.getStatus().equals(PARAM_STATUS_APPROVED)) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }
        switch (amendmentDetails.getStatus()) {
            case PARAM_STATUS_PROCESSING_by_BANK:
            case PARAM_STATUS_REJECTED:
                amendmentDTO.setReasonForReturn("");
                if (StringUtils.equals(amendmentDTO.getCancellationStatus(), PARAM_STATUS_REQUESTED))
                    alertToPush = AlertsEnum.INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED;
                break;
            case PARAM_STATUS_RETURNED_by_BANK:
                if (StringUtils.isBlank(amendmentDetails.getReasonForReturn())) {
                    return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                }
                amendmentDTO.setReasonForReturn(amendmentDetails.getReasonForReturn());
                break;
            case PARAM_STATUS_APPROVED:
                amendmentDTO.setReasonForReturn("");
                if (StringUtils.isNotBlank(amendmentDTO.getCancellationStatus())
                        && amendmentDTO.getCancellationStatus().equals(PARAM_STATUS_REQUESTED)) {
                    amendmentDTO.setCancellationStatus(PARAM_STATUS_CANCELLATION_APPROVED);
                    collectionsDTO.setStatus(PARAM_STATUS_CANCELLED);
                    alertToPush = AlertsEnum.INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED;
                }
                break;
            default:
                alert.prepareError("Invalid status found in request payload").log();
                return ERR_12002.setErrorCode(new Result());
        }
        amendmentDTO.setStatus(amendmentDetails.getStatus());
        amendmentDetails = this.requestBusinessDelegate.updateInwardCollectionAmendment(amendmentDTO, request);
        if (amendmentDetails != null && StringUtils.isBlank(amendmentDetails.getDbpErrMsg()) && StringUtils.isBlank(amendmentDetails.getDbpErrCode())) {
            JSONObject lastAmendmentDetails = new JSONObject(collectionsDTO.getAmendmentDetails());
            lastAmendmentDetails.put("amendmentStatus", amendmentDetails.getStatus());
            collectionsDTO.setAmendmentDetails(String.valueOf(lastAmendmentDetails));
            collectionsBusinessDelegate.updateInwardCollection(collectionsDTO, request);
        }
        Result result = JSONToResult.convert((new JSONObject(amendmentDetails)).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, amendmentDetails.getAmendmentSrmsId());
        return result;
    }

    @Override
    public Result generateInwardCollectionAmendment(DataControllerRequest request) {
        String amendmentSrmsId = request.getParameter("amendmentSrmsId");
        if (StringUtils.isBlank(amendmentSrmsId)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        InwardCollectionAmendmentsDTO amendmentDTO = requestBusinessDelegate.getInwardCollectionAmendmentById(amendmentSrmsId, request);
        if (StringUtils.isBlank(amendmentDTO.getAmendmentSrmsId())) {
            return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
        }
        InwardCollectionsDTO collectionDTO = collectionsBusinessDelegate.getInwardCollectionById(amendmentDTO.getCollectionSrmsId(), request);

        byte[] pdfBytes = requestBusinessDelegate.generateInwardCollectionAmendment(amendmentDTO, collectionDTO, request);
        if (ArrayUtils.isEmpty(pdfBytes)) {
            alert.prepareError("Failed to generate report").log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
        }
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(PREFIX_INWARD_COLLECTION_AMENDMENT);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
        Result result = new Result();
        result.addParam("fileId", fileId);
        return result;
    }

}