/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InwardCollectionsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.InwardCollectionsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCurrentDateTimeUTF;

public class InwardCollectionsResourceImpl implements InwardCollectionsResource {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final PaymentAdviceBussinessDelegate paymentAdviceBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);
    private final GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate swiftMessagesBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class);
    private final InwardCollectionsBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InwardCollectionsBusinessDelegate.class);
    AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
            .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
            .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

    @Override
    public Result createInwardCollection(InwardCollectionsDTO inputDTO, DataControllerRequest request) {
        inputDTO.setCreatedDate(getCurrentDateTimeUTF());
        inputDTO.setStatus(PARAM_STATUS_NEW);
        inputDTO.setDraweeAcknowledgement(PARAM_STATUS_PENDING);
        InwardCollectionsDTO responseDTO = requestBusinessDelegate.createInwardCollection(inputDTO, request);
        Result result = JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.INWARD_COLLECTION_SUBMITTED, responseDTO.getCollectionSrmsId());
        return result;
    }

    @Override
    public Result getInwardCollections(FilterDTO filterDto, DataControllerRequest request) {
        List<InwardCollectionsDTO> inwardCollections = requestBusinessDelegate.getInwardCollections(request);
        if (inwardCollections == null) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result());
        }
        inwardCollections = filterDto.filter(inwardCollections);
        JSONObject response = new JSONObject();
        response.put("InwardCollections", inwardCollections);
        return JSONToResult.convert(String.valueOf(response));
    }

    @Override
    public Result getInwardCollectionById(DataControllerRequest request) {
        String collectionSrmsId = request.getParameter("collectionSrmsId");
        if (StringUtils.isBlank(collectionSrmsId)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        InwardCollectionsDTO responseDto = requestBusinessDelegate.getInwardCollectionById(collectionSrmsId, request);
        JSONObject responseObj = new JSONObject(responseDto);
        try {
            request.addRequestParam_("orderId", collectionSrmsId);
            request.addRequestParam_("module", "INCL");
            List<SwiftsAndAdvisesDTO> swiftMessages = swiftMessagesBusinessDelegate.getGuaranteeSwiftAdvices(request);
            responseObj.put("SwiftMessages", new JSONArray(swiftMessages));
            List<PaymentAdviceDTO> paymentAdvices = paymentAdviceBusinessDelegate.getPaymentAdvice(request);
            responseObj.put("PaymentAdvices", new JSONArray(paymentAdvices));
        } catch (Exception e) {
            alert.prepareError("Failed to get Payment Advices", e).log();
        }
        return JSONToResult.convert(responseObj.toString());
    }

    public Result updateInwardCollection(InwardCollectionsDTO collectionDetails, DataControllerRequest request) {
        AlertsEnum alertToPush = AlertsEnum.INWARD_COLLECTION_CONSENT_SUBMITTED;
        Result result = new Result();
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isNotBlank(collectionDetails.getDebitAmountFrom())) {
            String checkAccount = collectionDetails.getDebitAmountFrom();
            if (!this.authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, checkAccount)) {
                String accountEnding = "";
                if (checkAccount.length() > 3)
                    accountEnding = checkAccount.substring(checkAccount.length() - 3);
                String errorMessage = "You do not have permission to the Charges Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }
        if (StringUtils.isNotBlank(collectionDetails.getChargesDebitFrom())) {
            String checkAccount = collectionDetails.getChargesDebitFrom();
            if (!this.authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, checkAccount)) {
                String accountEnding = "";
                if (checkAccount.length() > 3)
                    accountEnding = checkAccount.substring(checkAccount.length() - 3);
                String errorMessage = "You do not have permission to the Charges Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }
        collectionDetails.setPaymentStatus(PARAM_STATUS_PENDING);
        collectionDetails.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        collectionDetails.setCreatedDate(getCurrentDateTimeUTF());
        collectionDetails = this.requestBusinessDelegate.updateInwardCollection(collectionDetails, request);
        result = JSONToResult.convert((new JSONObject(collectionDetails)).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, collectionDetails.getCollectionSrmsId());
        return result;
    }

    public Result updateInwardCollectionByBank(InwardCollectionsDTO bankResponse, DataControllerRequest request) {
        AlertsEnum alertToPush;
        InwardCollectionsDTO collectionDetails = this.requestBusinessDelegate.getInwardCollectionById(bankResponse.getCollectionSrmsId(), request);
        switch (bankResponse.getStatus()) {
            case PARAM_STATUS_PROCESSING_by_BANK:
                alertToPush = AlertsEnum.INWARD_COLLECTION_PROCESSING;
                break;
            case PARAM_STATUS_RETURNED_by_BANK:
                collectionDetails.setReasonForReturn(bankResponse.getReasonForReturn());
                alertToPush = AlertsEnum.INWARD_COLLECTION_RETURNED;
                break;
            case PARAM_STATUS_REJECTED:
                collectionDetails.setReasonForRejection(bankResponse.getReasonForRejection());
                alertToPush = AlertsEnum.INWARD_COLLECTION_REJECTED;
                break;
            case PARAM_STATUS_OVERDUE:
                alertToPush = AlertsEnum.INWARD_COLLECTION_PAYDUE_OR_OVERDUE;
                break;
            case PARAM_STATUS_PAYDUE:
                alertToPush = AlertsEnum.INWARD_COLLECTION_PAYDUE;
                break;
            case PARAM_STATUS_SETTLED:
                alertToPush = StringUtils.equals(collectionDetails.getStatus(), PARAM_STATUS_PAYDUE) ? AlertsEnum.INWARD_COLLECTION_SETTLED_FROM_PAYDUE : AlertsEnum.INWARD_COLLECTION_SETTLED;
                collectionDetails.setPaymentStatus(PARAM_STATUS_SETTLED);
                collectionDetails.setSettledDate(getCurrentDateTimeUTF());
                break;
            case PARAM_STATUS_APPROVED:
                alertToPush = AlertsEnum.INWARD_COLLECTION_APPROVED;
                break;
            case PARAM_STATUS_CANCELLED:
                alertToPush = AlertsEnum.INWARD_COLLECTION_CANCELLED;
                break;
            default:
                return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
        }
        collectionDetails.setStatus(bankResponse.getStatus());
        collectionDetails = requestBusinessDelegate.updateInwardCollection(collectionDetails, request);
        Result result = JSONToResult.convert((new JSONObject(collectionDetails)).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, collectionDetails.getCollectionSrmsId());
        return result;
    }

    public Result generateInwardCollection(DataControllerRequest request) {
        String collectionId = request.getParameter("collectionSrmsId");
        if (StringUtils.isBlank(collectionId)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        InwardCollectionsDTO responseDto = requestBusinessDelegate.getInwardCollectionById(collectionId, request);
        byte[] pdfBytes = requestBusinessDelegate.generateInwardCollection(responseDto, request);
        if (ArrayUtils.isEmpty(pdfBytes)) {
            alert.prepareError("Failed to generate report").log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
        }
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(PREFIX_INWARD_COLLECTIONS);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
        Result result = new Result();
        result.addParam("fileId", fileId);
        return result;
    }

}