/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.OutwardCollectionAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.OutwardCollectionsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.OutwardCollectionAmendmentsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.fetchCustomerFromSession;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCurrentDateTimeUTF;

/**
 * @author k.meiyazhagan
 */
public class OutwardCollectionAmendmentsResourceImpl implements OutwardCollectionAmendmentsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final OutwardCollectionAmendmentsBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OutwardCollectionAmendmentsBusinessDelegate.class);
    private final OutwardCollectionsBusinessDelegate requestCollectionsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OutwardCollectionsBusinessDelegate.class);
    private final AuthorizationChecksBusinessDelegate requestAuthBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

    @Override
    public Result createAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) {
        String customerId = fetchCustomerFromSession(request);
        OutwardCollectionsDTO collectionsDto = requestCollectionsBusinessDelegate.getCollectionById(inputDto.getCollectionReference(), request);
        if (StringUtils.isBlank(collectionsDto.getCollectionReference())) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result(), "Requested Collection is not available");
        }

        JSONObject lastAmendmentDetails = StringUtils.isNotBlank(collectionsDto.getLastAmendmentDetails()) ? new JSONObject(collectionsDto.getLastAmendmentDetails()) : new JSONObject();
        if (!Arrays.asList(PARAM_STATUS_APPROVED, PARAM_STATUS_OVERDUE).contains(collectionsDto.getStatus())
                || (lastAmendmentDetails.has("amendmentStatus") && !Arrays.asList(PARAM_STATUS_APPROVED, PARAM_STATUS_REJECTED).contains(lastAmendmentDetails.getString("amendmentStatus")))
                || (lastAmendmentDetails.has("cancellationStatus") && !Arrays.asList(PARAM_STATUS_APPROVED, PARAM_STATUS_REJECTED).contains(lastAmendmentDetails.getString("cancellationStatus")))) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result(), "Collection is not eligible to create amendment");
        }
        int amendmentNo = lastAmendmentDetails.has("amendmentNo") ? (int) lastAmendmentDetails.get("amendmentNo") + 1 : 1;

        String creditAccount = inputDto.getChargesDebitAccount();
        if (StringUtils.isNotBlank(creditAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, creditAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Debit Account ending xxx"
                    + (creditAccount.length() > 3 ? creditAccount.substring(creditAccount.length() - 3) : creditAccount));
        }
        String chargesDebitAccount = inputDto.getChargesDebitAccount();
        if (StringUtils.isNotBlank(chargesDebitAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, chargesDebitAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Debit Account ending xxx"
                    + (chargesDebitAccount.length() > 3 ? chargesDebitAccount.substring(chargesDebitAccount.length() - 3) : chargesDebitAccount));
        }

        inputDto.setAmendmentNo(String.valueOf(amendmentNo));
        inputDto.setTenorType(collectionsDto.getTenorType());
        inputDto.setCollectingBank(collectionsDto.getCollectingBank());
        inputDto.setMaturityDate(collectionsDto.getMaturityDate());
        inputDto.setRequestedOn(getCurrentDateTimeUTF());
        inputDto.setUpdatedOn(inputDto.getRequestedOn());
        inputDto.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);

        OutwardCollectionAmendmentsDTO responseDTO = requestBusinessDelegate.createAmendment(inputDto, request);
        if (responseDTO != null && StringUtils.isBlank(responseDTO.getDbpErrMsg()) && StringUtils.isBlank(responseDTO.getDbpErrCode())) {
            lastAmendmentDetails.put("amendmentNo", amendmentNo);
            lastAmendmentDetails.put("amendmentStatus", inputDto.getStatus());
            lastAmendmentDetails.put("amendmentReference", responseDTO.getAmendmentReference());
            if (StringUtils.equals(responseDTO.getCancellationStatus(), PARAM_STATUS_REQUESTED)) {
                lastAmendmentDetails.put("cancellationStatus", PARAM_STATUS_REQUESTED);
            }
            collectionsDto.setLastAmendmentDetails(String.valueOf(lastAmendmentDetails));
            requestCollectionsBusinessDelegate.updateCollection(collectionsDto, request);
        }

        Result result = JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED, responseDTO.getAmendmentReference());
        return result;
    }

    @Override
    public Result updateAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) throws IOException {
        OutwardCollectionAmendmentsDTO existingAmendmentDto = requestBusinessDelegate.getAmendmentById(inputDto.getAmendmentReference(), request);
        if (!existingAmendmentDto.getStatus().equals(PARAM_STATUS_RETURNED_by_BANK)) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }

        OutwardCollectionsDTO collectionsDto = requestCollectionsBusinessDelegate.getCollectionById(existingAmendmentDto.getCollectionReference(), request);
        String customerId = fetchCustomerFromSession(request);
        String creditAccount = inputDto.getCreditAccount();
        if (StringUtils.isNotBlank(creditAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, creditAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Credit Account ending xxx"
                    + (creditAccount.length() > 3 ? creditAccount.substring(creditAccount.length() - 3) : creditAccount));
        }
        String chargesDebitAccount = inputDto.getChargesDebitAccount();
        if (StringUtils.isNotBlank(chargesDebitAccount) && !requestAuthBusinessDelegate.isOneOfMyAccounts(customerId, chargesDebitAccount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "You do not have permission to the Charges Debit Account ending xxx"
                    + (chargesDebitAccount.length() > 3 ? chargesDebitAccount.substring(chargesDebitAccount.length() - 3) : chargesDebitAccount));
        }

        inputDto = JSONUtils.parse(TradeFinanceCommonUtils.mergeJSONObjects(new JSONObject(existingAmendmentDto), new JSONObject(inputDto)).toString(), OutwardCollectionAmendmentsDTO.class);
        inputDto.setUpdatedOn(getCurrentDateTimeUTF());
        inputDto.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);

        OutwardCollectionAmendmentsDTO responseDTO = requestBusinessDelegate.updateAmendment(inputDto, request);
        if (responseDTO != null && StringUtils.isBlank(responseDTO.getDbpErrMsg()) && StringUtils.isBlank(responseDTO.getDbpErrCode())) {
            JSONObject lastAmendmentDetails = StringUtils.isNotBlank(collectionsDto.getLastAmendmentDetails()) ? new JSONObject(collectionsDto.getLastAmendmentDetails()) : new JSONObject();
            JSONArray returnedHistory = StringUtils.isNotBlank(existingAmendmentDto.getReturnedHistory()) ? new JSONArray(existingAmendmentDto.getReturnedHistory()) : new JSONArray();
            if (returnedHistory.length() > 4) {
                return ErrorCodeEnum.ERR_12005.setErrorCode(new Result());
            }
            lastAmendmentDetails.put("amendmentStatus", responseDTO.getStatus());
            collectionsDto.setLastAmendmentDetails(String.valueOf(lastAmendmentDetails));
            requestCollectionsBusinessDelegate.updateCollection(collectionsDto, request);
        }

        Result result = JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED, responseDTO.getAmendmentReference());
        return result;
    }

    @Override
    public Result getAmendments(FilterDTO filterDto, DataControllerRequest request) {
        List<OutwardCollectionAmendmentsDTO> amendmentsList = requestBusinessDelegate.getAmendments(request);
        if (amendmentsList == null) {
            alert.prepareError("Failed in fetching outward collection amendments").log();
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result());
        }
        amendmentsList = filterDto.filter(amendmentsList);
        JSONObject response = new JSONObject();
        response.put("OutwardCollectionAmendments", amendmentsList);
        return JSONToResult.convert(String.valueOf(response));
    }

    @Override
    public Result getAmendmentById(DataControllerRequest request) {
        String amendmentReference = request.getParameter("amendmentReference");
        if (StringUtils.isBlank(amendmentReference)) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        OutwardCollectionAmendmentsDTO responseDto = requestBusinessDelegate.getAmendmentById(amendmentReference, request);
        JSONObject responseObj = new JSONObject(responseDto);
        return JSONToResult.convert(responseObj.toString());
    }

    @Override
    public Result updateAmendmentByBank(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) {
        AlertsEnum alertToPush;
        OutwardCollectionAmendmentsDTO amendmentDto = requestBusinessDelegate.getAmendmentById(inputDto.getAmendmentReference(), request);
        OutwardCollectionsDTO collectionsDto = requestCollectionsBusinessDelegate.getCollectionById(amendmentDto.getCollectionReference(), request);
        amendmentDto.setUpdatedOn(getCurrentDateTimeUTF());

        if (StringUtils.equals(amendmentDto.getStatus(), inputDto.getStatus()) ||
                Arrays.asList(PARAM_STATUS_APPROVED, PARAM_STATUS_REJECTED).contains(amendmentDto.getStatus())) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }
        switch (inputDto.getStatus()) {
            case PARAM_STATUS_APPROVED:
                if (StringUtils.equals(amendmentDto.getCancellationStatus(), PARAM_STATUS_REQUESTED)) {
                    amendmentDto.setCancellationStatus(PARAM_STATUS_CANCELLATION_APPROVED);
                    collectionsDto.setStatus(PARAM_STATUS_CANCELLED);
                }
                alertToPush = AlertsEnum.OUTWARD_COLLECTION_AMENDMENT_ACCEPTED;
                break;
            case PARAM_STATUS_RETURNED_by_BANK:
                amendmentDto.setReasonForReturn(inputDto.getReasonForReturn());
                JSONArray returnedHistory = StringUtils.isNotBlank(amendmentDto.getReturnedHistory()) ? new JSONArray(amendmentDto.getReturnedHistory()) : new JSONArray();
                if (returnedHistory.length() > 4) {
                    return ErrorCodeEnum.ERR_12005.setErrorCode(new Result());
                }
                returnedHistory.put(new JSONObject()
                        .put("returnedTimeStamp", amendmentDto.getUpdatedOn())
                        .put("messageToBank", amendmentDto.getMessageToBank())
                        .put("reasonForReturn", inputDto.getReasonForReturn())
                        .put("corporateUserName", amendmentDto.getCorporateUserName()));
                amendmentDto.setReturnedHistory(String.valueOf(returnedHistory));
                alertToPush = AlertsEnum.OUTWARD_COLLECTION_AMENDMENT_RETURNED;
                break;
            case PARAM_STATUS_REJECTED:
                amendmentDto.setReasonForRejection(inputDto.getReasonForRejection());
                if (StringUtils.equals(amendmentDto.getCancellationStatus(), PARAM_STATUS_REQUESTED)) {
                    amendmentDto.setCancellationStatus(PARAM_STATUS_REJECTED);
                }
                alertToPush = AlertsEnum.OUTWARD_COLLECTION_AMENDMENT_REJECTED;
                break;
            case PARAM_STATUS_PROCESSING_by_BANK:
                alertToPush = AlertsEnum.OUTWARD_COLLECTION_AMENDMENT_PROCESSING;
                break;
            default:
                return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
        }

        amendmentDto.setStatus(inputDto.getStatus());
        amendmentDto = requestBusinessDelegate.updateAmendment(amendmentDto, request);
        if (amendmentDto != null && StringUtils.isBlank(amendmentDto.getDbpErrMsg()) && StringUtils.isBlank(amendmentDto.getDbpErrCode())) {
            collectionsDto.setUpdatedOn(amendmentDto.getUpdatedOn());
            JSONObject lastAmendmentDetails = StringUtils.isNotBlank(collectionsDto.getLastAmendmentDetails()) ? new JSONObject(collectionsDto.getLastAmendmentDetails()) : new JSONObject();
            lastAmendmentDetails.put("amendmentStatus", amendmentDto.getStatus());
            if (StringUtils.isNotBlank(amendmentDto.getCancellationStatus())) {
                lastAmendmentDetails.put("cancellationStatus", amendmentDto.getCancellationStatus());
            }
            collectionsDto.setLastAmendmentDetails(String.valueOf(lastAmendmentDetails));
            requestCollectionsBusinessDelegate.updateCollection(collectionsDto, request);
        }
        Result result = JSONToResult.convert(String.valueOf(new JSONObject(amendmentDto)));
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, amendmentDto.getAmendmentReference());
        return result;
    }
}
