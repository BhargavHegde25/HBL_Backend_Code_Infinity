/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.OutwardCollectionsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.OutwardCollectionsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices.TRANSACT_TF_CREATE_IMPORT_LC;
import static com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices.TRANSACT_TF_CREATE_OUTWARD_COLLECTION;
import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERR_12007;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

/**
 * @author k.meiyazhagan
 */
public class OutwardCollectionsResourceImpl implements OutwardCollectionsResource {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final OutwardCollectionsBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OutwardCollectionsBusinessDelegate.class);
    private final AuthorizationChecksBusinessDelegate requestAuthBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

    @Override
    public Result createCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request) {
        // Mandatory fields check
        if (StringUtils.isBlank(inputDto.getDocumentNo())
                || StringUtils.isBlank(inputDto.getTenorType())
                || StringUtils.isBlank(inputDto.getCurrency())
                || StringUtils.isBlank(inputDto.getAmount())
                || StringUtils.isBlank(inputDto.getDraweeName())
                || StringUtils.isBlank(inputDto.getCollectingBank())
                || StringUtils.isBlank(inputDto.getSwiftOrBicCode())
                || StringUtils.isBlank(inputDto.getUploadDocuments())
                || StringUtils.isBlank(inputDto.getIncoTerms())
                || StringUtils.isBlank(inputDto.getPhysicalDocuments())
                || StringUtils.isBlank(inputDto.getCollectingBankAddress())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        if (StringUtils.equals(inputDto.getTenorType(), PARAM_USANCE)) {
            if (StringUtils.isBlank(inputDto.getUsanceDays())
                    || StringUtils.isBlank(inputDto.getUsanceDetails())
                    || StringUtils.isBlank(inputDto.getAllowUsanceAcceptance())) {
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }
        }

        String customerId = fetchCustomerFromSession(request);
        String creditAccount = inputDto.getCreditAccount();
        if (StringUtils.isNotBlank(creditAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, creditAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Credit Account ending xxx"
                    + (creditAccount.length() > 3 ? creditAccount.substring(creditAccount.length() - 3) : creditAccount));
        }
        String chargesDebitAccount = inputDto.getDebitAccount();
        if (StringUtils.isNotBlank(chargesDebitAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, chargesDebitAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Debit Account ending xxx"
                    + (chargesDebitAccount.length() > 3 ? chargesDebitAccount.substring(chargesDebitAccount.length() - 3) : chargesDebitAccount));
        }

        inputDto.setCreatedOn(getCurrentDateTimeUTF());
        inputDto.setUpdatedOn(inputDto.getCreatedOn());
        inputDto.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);

        OutwardCollectionsDTO responseDTO;
        OutwardCollectionsDTO transactResponseDto = new OutwardCollectionsDTO();
        if (StringUtils.isBlank(inputDto.getCollectionReference())) {
            if (isTransactEnabled()) {
                transactResponseDto = createCollectionInTransact(request, inputDto);
                if (StringUtils.isBlank(transactResponseDto.getTransactReference()))
                    return JSONToResult.convert(String.valueOf(new JSONObject(transactResponseDto)));
            }
            responseDTO = requestBusinessDelegate.createCollection(inputDto, request);
        } else {
            responseDTO = requestBusinessDelegate.getCollectionById(inputDto.getCollectionReference(), request);
            if (StringUtils.isBlank(responseDTO.getCollectionReference())
                    || !StringUtils.equals(responseDTO.getStatus(), PARAM_STATUS_DRAFT)) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            if (isTransactEnabled()) {
                transactResponseDto = createCollectionInTransact(request, responseDTO);
                if (StringUtils.isBlank(transactResponseDto.getTransactReference()))
                    return JSONToResult.convert(String.valueOf(new JSONObject(transactResponseDto)));
            }
            responseDTO = requestBusinessDelegate.updateCollection(inputDto, request);
        }

        if(StringUtils.isNotBlank(transactResponseDto.getTransactReference()))
            responseDTO.setTransactReference(transactResponseDto.getTransactReference());
        JSONObject response = new JSONObject(responseDTO);
        Result result = JSONToResult.convert(String.valueOf(response));
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.OUTWARD_COLLECTION_DOCUMENT_SUBMITTED, responseDTO.getCollectionReference());
        return result;
    }

    @Override
    public Result getCollections(FilterDTO filterDto, DataControllerRequest request) {
        List<OutwardCollectionsDTO> collectionsList = requestBusinessDelegate.getCollections(request);
        if (collectionsList == null) {
            alert.prepareError("Failed in fetching outward collections").log();
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result());
        }
        collectionsList = filterDto.filter(collectionsList);
        JSONObject response = new JSONObject();
        response.put("OutwardCollections", collectionsList);
        return JSONToResult.convert(String.valueOf(response));
    }

    @Override
    public Result getCollectionById(DataControllerRequest request) {
        String collectionReference = request.getParameter("collectionReference");
        if (StringUtils.isBlank(collectionReference)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        OutwardCollectionsDTO responseDto = requestBusinessDelegate.getCollectionById(collectionReference, request);
        JSONObject responseObj = new JSONObject(responseDto);
        return JSONToResult.convert(responseObj.toString());
    }


    @Override
    public Result updateCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request) throws IOException {
        OutwardCollectionsDTO collectionDTO = requestBusinessDelegate.getCollectionById(inputDto.getCollectionReference(), request);
        if (StringUtils.isBlank(collectionDTO.getCollectionReference())
                || !StringUtils.equals(collectionDTO.getStatus(), PARAM_STATUS_RETURNED_by_BANK)) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
        }

        JSONArray returnedHistory = new JSONArray(collectionDTO.getReturnedHistory());
        if (returnedHistory.length() > 4) {
            return ErrorCodeEnum.ERR_12005.setErrorCode(new Result());
        }
        JSONObject lastHistory = returnedHistory.getJSONObject(returnedHistory.length() - 1)
                .put("messageToBank", StringUtils.isNotBlank(inputDto.getMessageToBank()) ? inputDto.getMessageToBank() : "");
        returnedHistory.put(returnedHistory.length() - 1, lastHistory);
        collectionDTO.setReturnedHistory(String.valueOf(returnedHistory));

        String customerId = fetchCustomerFromSession(request);
        String creditAccount = inputDto.getCreditAccount();
        if (StringUtils.isNotBlank(creditAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, creditAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Debit Account ending xxx"
                    + (creditAccount.length() > 3 ? creditAccount.substring(creditAccount.length() - 3) : creditAccount));
        }
        String chargesDebitAccount = inputDto.getDebitAccount();
        if (StringUtils.isNotBlank(chargesDebitAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, chargesDebitAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Debit Account ending xxx"
                    + (chargesDebitAccount.length() > 3 ? chargesDebitAccount.substring(chargesDebitAccount.length() - 3) : chargesDebitAccount));
        }

        inputDto = JSONUtils.parse(TradeFinanceCommonUtils.mergeJSONObjects(new JSONObject(collectionDTO), new JSONObject(inputDto)).toString(), OutwardCollectionsDTO.class);
        inputDto.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        inputDto.setUpdatedOn(getCurrentDateTimeUTF());
        inputDto = this.requestBusinessDelegate.updateCollection(inputDto, request);
        Result result = JSONToResult.convert((new JSONObject(inputDto)).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED, inputDto.getCollectionReference());
        return result;
    }

    @Override
    public Result saveCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request) {
        String customerId = fetchCustomerFromSession(request);
        String creditAccount = inputDto.getCreditAccount();
        if (StringUtils.isNotBlank(creditAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, creditAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Debit Account ending xxx"
                    + (creditAccount.length() > 3 ? creditAccount.substring(creditAccount.length() - 3) : creditAccount));
        }
        String chargesDebitAccount = inputDto.getDebitAccount();
        if (StringUtils.isNotBlank(chargesDebitAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, chargesDebitAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Debit Account ending xxx"
                    + (chargesDebitAccount.length() > 3 ? chargesDebitAccount.substring(chargesDebitAccount.length() - 3) : chargesDebitAccount));
        }

        OutwardCollectionsDTO responseDTO;
        inputDto.setStatus(PARAM_STATUS_DRAFT);
        if (StringUtils.isBlank(inputDto.getCollectionReference())) {
            inputDto.setCreatedOn(getCurrentDateTimeUTF());
            inputDto.setUpdatedOn(inputDto.getCreatedOn());
            responseDTO = requestBusinessDelegate.createCollection(inputDto, request);
        } else {
            responseDTO = requestBusinessDelegate.getCollectionById(inputDto.getCollectionReference(), request);
            if (StringUtils.isBlank(responseDTO.getCollectionReference())
                    || !StringUtils.equals(responseDTO.getStatus(), PARAM_STATUS_DRAFT)) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }
            inputDto.setCreatedOn(responseDTO.getCreatedOn());
            inputDto.setUpdatedOn(getCurrentDateTimeUTF());
            responseDTO = requestBusinessDelegate.updateCollection(inputDto, request);
        }
        JSONObject responseObj = new JSONObject(responseDTO);
        return JSONToResult.convert(String.valueOf(responseObj));
    }

    @Override
    public Result updateCollectionByBank(OutwardCollectionsDTO inputDto, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        OutwardCollectionsDTO collectionDto = requestBusinessDelegate.getCollectionById(inputDto.getCollectionReference(), request);
        switch (inputDto.getStatus()) {
            case PARAM_STATUS_RETURNED_by_BANK:
                collectionDto.setReasonForReturn(inputDto.getReasonForReturn());
                JSONArray returnedHistory = StringUtils.isNotBlank(collectionDto.getReturnedHistory()) ? new JSONArray(collectionDto.getReturnedHistory()) : new JSONArray();
                if (returnedHistory.length() > 4) {
                    return ErrorCodeEnum.ERR_12005.setErrorCode(new Result());
                }
                returnedHistory.put(new JSONObject()
                        .put("returnedCount", returnedHistory.length() + 1)
                        .put("returnedTimeStamp", getCurrentDateTimeUTF())
                        .put("reasonForReturn", StringUtils.isNotBlank(collectionDto.getReasonForReturn()) ? collectionDto.getReasonForReturn() : "")
                        .put("corporateUserName", collectionDto.getDraweeName()));
                collectionDto.setReturnedHistory(String.valueOf(returnedHistory));
                break;
            case PARAM_STATUS_CANCELLED:
                collectionDto.setReasonForCancellation(inputDto.getReasonForCancellation());
                break;
            case PARAM_STATUS_REJECTED:
                collectionDto.setReasonForRejection(inputDto.getReasonForRejection());
                collectionDto.setMaturityDate(inputDto.getMaturityDate());
                collectionDto.setCourierTrackingDetails(inputDto.getCourierTrackingDetails());
                collectionDto.setPaymentStatus(inputDto.getPaymentStatus());
                collectionDto.setMessageFromBank(inputDto.getMessageFromBank());
                alertToPush = StringUtils.isBlank(collectionDto.getReturnedHistory())
                        ? AlertsEnum.OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED : AlertsEnum.OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED;
                break;
            case PARAM_APPROVED:
                collectionDto.setMaturityDate(inputDto.getMaturityDate());
                collectionDto.setCourierTrackingDetails(inputDto.getCourierTrackingDetails());
                collectionDto.setPaymentStatus(inputDto.getPaymentStatus());
                collectionDto.setMessageFromBank(inputDto.getMessageFromBank());
                collectionDto.setDraweeAcknowledgement(inputDto.getDraweeAcknowledgement());
                collectionDto.setDraweeAcceptance(inputDto.getDraweeAcceptance());
                collectionDto.setIsBillExchangeSigned(inputDto.getIsBillExchangeSigned());
                alertToPush = StringUtils.isBlank(collectionDto.getReturnedHistory())
                        ? AlertsEnum.OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED : AlertsEnum.OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED;
                break;
            case PARAM_STATUS_OVERDUE:
                collectionDto.setMaturityDate(inputDto.getMaturityDate());
                collectionDto.setCourierTrackingDetails(inputDto.getCourierTrackingDetails());
                collectionDto.setPaymentStatus(inputDto.getPaymentStatus());
                collectionDto.setBillOfExchangeStatus(inputDto.getBillOfExchangeStatus());
                collectionDto.setMessageFromBank(inputDto.getMessageFromBank());
                collectionDto.setDraweeAcceptance(inputDto.getDraweeAcceptance());
                collectionDto.setIsBillExchangeSigned(inputDto.getIsBillExchangeSigned());
                alertToPush = AlertsEnum.OUTWARD_COLLECTION_OVERDUE;
                break;
            case PARAM_STATUS_SETTLED:
                collectionDto.setMaturityDate(inputDto.getMaturityDate());
                collectionDto.setCourierTrackingDetails(inputDto.getCourierTrackingDetails());
                collectionDto.setPaymentStatus(inputDto.getPaymentStatus());
                collectionDto.setMessageFromBank(inputDto.getMessageFromBank());
                collectionDto.setSettledAmount(inputDto.getSettledAmount());
            case PARAM_STATUS_PROCESSING_by_BANK:
                break;
            default:
                return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
        }
        collectionDto.setUpdatedOn(getCurrentDateTimeUTF());
        collectionDto.setStatus(inputDto.getStatus());
        collectionDto = requestBusinessDelegate.updateCollection(collectionDto, request);
        Result result = JSONToResult.convert((new JSONObject(collectionDto)).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, collectionDto.getCollectionReference());
        return result;
    }

    @Override
    public Result deleteCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request) {
        OutwardCollectionsDTO collectionDto = requestBusinessDelegate.getCollectionById(inputDto.getCollectionReference(), request);
        if (StringUtils.isBlank(collectionDto.getCollectionReference())
                || StringUtils.equals(collectionDto.getStatus(), (PARAM_STATUS_DELETED))
                || !StringUtils.equals(collectionDto.getStatus(), PARAM_STATUS_DRAFT)) {
            return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result(), "Record is already deleted or Invalid status is provided");
        }
        inputDto.setCreatedOn(getCurrentDateTimeUTF());
        inputDto.setUpdatedOn(inputDto.getCreatedOn());
        collectionDto.setStatus(PARAM_STATUS_DELETED);
        collectionDto = this.requestBusinessDelegate.updateCollection(collectionDto, request);
        return JSONToResult.convert((new JSONObject(collectionDto)).toString());
    }

    @Override
    public Result requestCollectionStatus(OutwardCollectionsDTO inputDto, DataControllerRequest request) {
        OutwardCollectionsDTO collectionDto = requestBusinessDelegate.getCollectionById(inputDto.getCollectionReference(), request);
        String requestSelection = request.getParameter("requestSelection");
        if ((collectionDto.getStatus().equals(PARAM_APPROVED) && requestSelection.equals(PARAM_PAYMENT_STATUS)) || (collectionDto.getStatus().equals(PARAM_STATUS_OVERDUE) && requestSelection.equals(PARAM_PAYMENT_STATUS))) {
            collectionDto.setPaymentStatus(PARAM_STATUS_REQUESTED);
        } else if (collectionDto.getStatus().equals(PARAM_STATUS_OVERDUE) && !StringUtils.isNotBlank(collectionDto.getBillOfExchangeStatus()) && requestSelection.equals(PARAM_BILL_OF_EXCHANGE)) {
            collectionDto.setBillOfExchangeStatus(PARAM_STATUS_REQUESTED);
        } else {
            return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result(), "BOE cannot be requested more than once");
        }
        if (StringUtils.isNotBlank(inputDto.getMessageToBank())) {
            collectionDto.setMessageToBank(inputDto.getMessageToBank());
        }
        collectionDto = this.requestBusinessDelegate.updateCollection(collectionDto, request);
        return JSONToResult.convert((new JSONObject(collectionDto)).toString());
    }

    private OutwardCollectionsDTO createCollectionInTransact(DataControllerRequest request, OutwardCollectionsDTO inputDto) {
        Map<String, Object> inputMap;
        Result result;
        OutwardCollectionsDTO responseDTO = new OutwardCollectionsDTO();
        try {
            inputDto.setTenorType(inputDto.getTenorType());
            inputMap = new ObjectMapper().convertValue(inputDto, Map.class);
            inputMap.put("customerId", getCoreCustomerId(request));
            result = CommonUtils.callIntegrationService(request, inputMap, getHeadersMap(request), TRANSACT_TF_CREATE_OUTWARD_COLLECTION.getServiceName(), TRANSACT_TF_CREATE_OUTWARD_COLLECTION.getOperationName(), false);
            Record headerData = result.getRecordById("header");
            if (StringUtils.equals(headerData.getParamValueByName("status"), "success"))
                responseDTO.setTransactReference(headerData.getParamValueByName("id"));
            else if (StringUtils.equals(headerData.getParamValueByName("status"), "failed")) {
                responseDTO.setErrorCode(ERR_12007.getErrorCodeAsString());
                responseDTO.setDbpErrMsg(ERR_12007.getErrorMessage());
                responseDTO.setErrorDetails(result.getRecordById("error").getDatasetById("errorDetails").getAllRecords().toString());
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating invoice in transact", e).log();
            responseDTO.setErrorCode(ERR_12007.getErrorCodeAsString());
            responseDTO.setErrorMsg(ERR_12007.getErrorMessage());
            responseDTO.setErrorDetails("Backend Failed");
        }

        return responseDTO;
    }

}
