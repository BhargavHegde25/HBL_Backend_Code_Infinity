/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.IssuedGuaranteeClaimsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.dto.IssuedGuaranteeClaimsDTO;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.IssuedGuaranteeClaimsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.fetchCustomerFromSession;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCurrentDateTimeUTF;

public class IssuedGuaranteeClaimsResourceImpl implements IssuedGuaranteeClaimsResource, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final IssuedGuaranteeClaimsBusinessDelegate claimsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(IssuedGuaranteeClaimsBusinessDelegate.class);
    private final GuaranteesBusinessDelegate guaranteesBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesBusinessDelegate.class);
    private final PaymentAdviceBussinessDelegate paymentAdviceBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);
    private final GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate swiftMessagesBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class);
    private final AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

    public Result createClaim(String methodId, HashMap<String, Object> inputParams, DataControllerRequest request) throws IOException {
        IssuedGuaranteeClaimsDTO guaranteeClaimsDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IssuedGuaranteeClaimsDTO.class);
        guaranteeClaimsDTO.setReceivedOn(getCurrentDateTimeUTF());
        guaranteeClaimsDTO.setClaimStatus(PARAM_STATUS_NEW);
        guaranteeClaimsDTO = claimsBusinessDelegate.createClaim(guaranteeClaimsDTO, request);
        return JSONToResult.convert(new JSONObject(guaranteeClaimsDTO).toString());
    }

    public Result updateClaim(String methodId, HashMap<String, Object> inputParams, DataControllerRequest request) throws IOException {
        IssuedGuaranteeClaimsDTO inputDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IssuedGuaranteeClaimsDTO.class);
        if (StringUtils.isBlank(inputDTO.getClaimsSRMSId())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        IssuedGuaranteeClaimsDTO claimsDTO = claimsBusinessDelegate.getClaimById(inputDTO.getClaimsSRMSId(), request);
        if (StringUtils.isNotBlank(claimsDTO.getDbpErrMsg())) {
            return ErrorCodeEnum.ERRTF_29093.setErrorCode(new Result(), claimsDTO.getDbpErrMsg());
        }

        claimsDTO.setClaimAcceptance("");
        claimsDTO.setDebitAccount("");
        claimsDTO.setRequestedOverdraft("");
        claimsDTO.setReasonForRejection("");
        claimsDTO.setDiscrepancyAcceptance("");
        claimsDTO.setMessageToBank("");
        if (claimsDTO.getClaimType().equals(PARAM_DEMAND)) {
            if (StringUtils.isBlank(inputDTO.getClaimAcceptance()))
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            claimsDTO.setClaimAcceptance(inputDTO.getClaimAcceptance());
            switch (inputDTO.getClaimAcceptance()) {
                case PARAM_ACCEPTED_TO_PAY:
                    if (StringUtils.isBlank(inputDTO.getDebitAccount()))
                        return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                    claimsDTO.setDebitAccount(inputDTO.getDebitAccount());
                    if (StringUtils.isNotBlank(inputDTO.getRequestedOverdraft()))
                        claimsDTO.setRequestedOverdraft(inputDTO.getRequestedOverdraft());
                    break;
                case PARAM_ACCEPTED_TO_EXTEND:
                    // update claim acceptance only
                    break;
                case PARAM_REJECTED:
                    if (StringUtils.isBlank(inputDTO.getReasonForRejection()))
                        return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                    claimsDTO.setReasonForRejection(inputDTO.getReasonForRejection());
                    break;
                default:
                    return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
            }
        } else if (claimsDTO.getClaimType().equals(PARAM_PRESENTATION)) {
            if (claimsDTO.getDocumentStatus().equals(PARAM_DISCREPANT)) {
                if (StringUtils.isBlank(inputDTO.getDiscrepancyAcceptance()))
                    return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                claimsDTO.setDiscrepancyAcceptance(inputDTO.getDiscrepancyAcceptance());
                if (inputDTO.getDiscrepancyAcceptance().equals(PARAM_STATUS_ACCEPTED)) {
                    if (StringUtils.isBlank(inputDTO.getDebitAccount()))
                        return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                    claimsDTO.setDebitAccount(inputDTO.getDebitAccount());
                    if (StringUtils.isNotBlank(inputDTO.getRequestedOverdraft()))
                        claimsDTO.setRequestedOverdraft(inputDTO.getRequestedOverdraft());
                } else if (inputDTO.getDiscrepancyAcceptance().equals(PARAM_REJECTED)) {
                    if (StringUtils.isBlank(inputDTO.getReasonForRejection()))
                        return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                    claimsDTO.setReasonForRejection(inputDTO.getReasonForRejection());
                } else {
                    return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
                }
            } else {
                if (StringUtils.isBlank(inputDTO.getDebitAccount()))
                    return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                claimsDTO.setDebitAccount(inputDTO.getDebitAccount());
                if (StringUtils.isNotBlank(inputDTO.getRequestedOverdraft()))
                    claimsDTO.setRequestedOverdraft(inputDTO.getRequestedOverdraft());
            }
        } else {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "Enter valid claim type");
        }

        String customerId = fetchCustomerFromSession(request);
        String debitAccount = claimsDTO.getDebitAccount();
        if (StringUtils.isNotBlank(debitAccount) && !this.authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, debitAccount)) {
            String accountEnding = "";
            if (debitAccount.length() > 3)
                accountEnding = debitAccount.substring(debitAccount.length() - 3);
            String errorMessage = "You do not have permission to the Charges Account ending xxx" + accountEnding + ".";
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), errorMessage);
        }
        if (StringUtils.isNotBlank(inputDTO.getMessageToBank()))
            claimsDTO.setMessageToBank(inputDTO.getMessageToBank());
        if (claimsDTO.getClaimStatus().equalsIgnoreCase(PARAM_STATUS_RETURNED_BY_BANK)) {
            JSONObject discrepancyHistory = claimsDTO.getDiscrepancyHistory() != null ? new JSONObject(claimsDTO.getDiscrepancyHistory()) : new JSONObject();
            int returnCount = claimsDTO.getReturnCount() != null ? Integer.parseInt(claimsDTO.getReturnCount()) + 1 : 1;
            claimsDTO.setReturnCount(String.valueOf(returnCount));
            JSONObject returnHistory = new JSONObject();
            if (StringUtils.isNotBlank(claimsDTO.getMessageFromBank()))
                returnHistory.put("reasonForReturned", claimsDTO.getMessageFromBank());
            if (StringUtils.isNotBlank(inputDTO.getMessageToBank()))
                returnHistory.put("returnMessageToBank", inputDTO.getMessageToBank());
            if (StringUtils.isNotBlank(inputDTO.getCorporateUserName()))
                returnHistory.put("corporateUserName", inputDTO.getCorporateUserName());
            if (StringUtils.isNotBlank(claimsDTO.getReturnedTime()))
                returnHistory.put("returnedTime", claimsDTO.getReturnedTime());
            discrepancyHistory.put("returnHistory" + returnCount, returnHistory);
            claimsDTO.setDiscrepancyHistory(discrepancyHistory.toString());
        }
        if (StringUtils.isNotBlank(claimsDTO.getDocuments()))
            claimsDTO.setDocuments(new JSONArray(claimsDTO.getDocuments()).toString());
        if (StringUtils.isNotBlank(claimsDTO.getDiscrepancyHistory()))
            claimsDTO.setDiscrepancyHistory(new JSONObject(claimsDTO.getDiscrepancyHistory()).toString());
        if (StringUtils.isNotBlank(claimsDTO.getDiscrepancyDetails()))
            claimsDTO.setDiscrepancyDetails(new JSONArray(claimsDTO.getDiscrepancyDetails()).toString());
        if (!_validateClaimFields(claimsDTO))
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        claimsDTO.setClaimStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        claimsDTO = claimsBusinessDelegate.updateClaim(claimsDTO, null, false, request);

        Result result = JSONToResult.convert(new JSONObject(claimsDTO).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.GUARANTEES_ISSUED_CLAIM_SUBMITTED, claimsDTO.getClaimsSRMSId());
        return result;
    }

    public Result updateClaimByBank(String methodId, HashMap<String, Object> inputParams, DataControllerRequest request) throws IOException {
        AlertsEnum alertToPush = null;
        IssuedGuaranteeClaimsDTO guaranteeClaimsDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IssuedGuaranteeClaimsDTO.class);
        if (StringUtils.isBlank(guaranteeClaimsDTO.getClaimsSRMSId())
                || StringUtils.isBlank(guaranteeClaimsDTO.getClaimStatus())) {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        switch (guaranteeClaimsDTO.getClaimStatus()) {
            case PARAM_STATUS_RETURNED_by_BANK:
                guaranteeClaimsDTO.setReturnedTime(getCurrentDateTimeUTF());
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_CLAIM_RETURNED;
                break;
            case PARAM_STATUS_REJECTED:
                guaranteeClaimsDTO.setRejectedDate(getCurrentDateTimeUTF());
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_CLAIM_REJECTED;
                break;
            case PARAM_STATUS_PROCESSING_by_BANK:
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_CLAIM_PROCESSING;
                break;
            case PARAM_STATUS_CLAIMHONOURED:
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_CLAIM_HONOURED;
                break;
            case PARAM_STATUS_CLAIMEXTENDED:
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT;
                break;
            case PARAM_STATUS_CLAIMHONOURED_APPLICANTREJECTED:
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT;
                break;
            case PARAM_STATUS_CLAIMHONOURED_PENDINGCONSENT:
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT;
                break;
            case PARAM_STATUS_SUBMITTED_TO_BANK:
                // do nothing
                break;
            default:
                return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }

        IssuedGuaranteeClaimsDTO previousGuaranteeClaimsDTO = claimsBusinessDelegate.getClaimById(guaranteeClaimsDTO.getClaimsSRMSId(), request);
        if (StringUtils.isNotBlank(previousGuaranteeClaimsDTO.getDocuments()))
            previousGuaranteeClaimsDTO.setDocuments(new JSONArray(previousGuaranteeClaimsDTO.getDocuments()).toString());
        if (StringUtils.isNotBlank(previousGuaranteeClaimsDTO.getDiscrepancyHistory()))
            previousGuaranteeClaimsDTO.setDiscrepancyHistory(new JSONObject(previousGuaranteeClaimsDTO.getDiscrepancyHistory()).toString());
        if (StringUtils.isNotBlank(previousGuaranteeClaimsDTO.getDiscrepancyDetails()))
            previousGuaranteeClaimsDTO.setDiscrepancyDetails(new JSONArray(previousGuaranteeClaimsDTO.getDiscrepancyDetails()).toString());
        guaranteeClaimsDTO = claimsBusinessDelegate.updateClaim(guaranteeClaimsDTO, previousGuaranteeClaimsDTO, true, request);

        Result result = JSONToResult.convert(new JSONObject(guaranteeClaimsDTO).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, guaranteeClaimsDTO.getClaimsSRMSId());
        return result;
    }

    public Result getClaims(HashMap<String, Object> inputParams, DataControllerRequest request) {
        Result result;
        List<IssuedGuaranteeClaimsDTO> claimsList = claimsBusinessDelegate.getClaims(request);
        FilterDTO filterDTO;
        try {
            filterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
            if (StringUtils.isNotBlank(filterDTO.get_filterByParam()) && StringUtils.isNotBlank(filterDTO.get_filterByValue())) {
                claimsList = TradeFinanceCommonUtils.filterBy(claimsList, filterDTO.get_filterByParam(), filterDTO.get_filterByValue());
                filterDTO.set_filterByValue("");
                filterDTO.set_filterByParam("");
            }
            claimsList = filterDTO.filter(claimsList);
        } catch (Exception e) {
            alert.prepareError("Error occurred while filtering response ", e).log();
        }
        result = JSONToResult.convert((new JSONObject()).put("ClaimsReceived", claimsList).toString());
        return result;
    }

    public Result getClaimById(HashMap<String, Object> inputParams, DataControllerRequest request) {
        JSONObject resultObject = new JSONObject();
        Result result = new Result();
        String claimsId = (String) inputParams.get("claimsSRMSId");
        if (StringUtils.isBlank(claimsId)) {
            diagnostic.prepareDebug("Mandatory fields are missing.").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }

        IssuedGuaranteeClaimsDTO claimsDTO = claimsBusinessDelegate.getClaimById(claimsId, request);
        if (claimsDTO == null || StringUtils.isBlank(claimsDTO.getClaimsSRMSId())) {
            diagnostic.prepareDebug("Requested claim is not available in SRMS").log();
            return ErrorCodeEnum.ERRTF_29057.setErrorCode(result);
        }

        try {
            resultObject = new JSONObject(claimsDTO);
            request.addRequestParam_("orderId", claimsId);
            request.addRequestParam_("module", "IGCL");
            List<SwiftsAndAdvisesDTO> swiftMessages = swiftMessagesBusinessDelegate.getGuaranteeSwiftAdvices(request);
            resultObject.put("swiftMessages", new JSONArray(swiftMessages).toString());
            List<PaymentAdviceDTO> paymentAdvices = paymentAdviceBusinessDelegate.getPaymentAdvice(request);
            resultObject.put("paymentAdvices", new JSONArray(paymentAdvices).toString());
        } catch (Exception e) {
            diagnostic.prepareDebug("Error occurred while fetching the claim" + e).log();
        }
        return JSONToResult.convert(resultObject.toString());
    }

    public Result generateIssuedGuaranteeClaim(DataControllerRequest request) {
        String claimsId = request.getParameter("claimsSRMSId");
        if (StringUtils.isBlank(claimsId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }
        IssuedGuaranteeClaimsDTO claimsDTO = claimsBusinessDelegate.getClaimById(claimsId, request);
        GuaranteesDTO guaranteeDTO = guaranteesBusiness.getGuaranteesById(claimsDTO.getGuaranteesSRMSId(), request);
        byte[] pdfBytes = claimsBusinessDelegate.generateIssuedGuaranteeClaim(claimsDTO, guaranteeDTO, request);
        if (ArrayUtils.isEmpty(pdfBytes)) {
            alert.prepareError("Failed to generate report").log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
        }
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(PREFIX_ISSUED_GUARANTEE_CLAIM);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
        Result result = new Result();
        result.addParam("fileId", fileId);
        return result;
    }

    private boolean _validateClaimFields(IssuedGuaranteeClaimsDTO claimsDTO) {
        Pattern alphaNumericWithSpaceMax200 = Pattern.compile("^[a-zA-Z0-9., ]{0,200}$");
        Pattern numericMax35 = Pattern.compile("^[0-9]+([.][0-9]+?){0,35}$");
        if ((StringUtils.isNotBlank(claimsDTO.getDiscrepancyAcceptance()) && !Arrays.asList(PARAM_STATUS_ACCEPTED, PARAM_REJECTED).contains(claimsDTO.getDiscrepancyAcceptance()))
                || (StringUtils.isNotBlank(claimsDTO.getRequestedOverdraft()) && !Arrays.asList("Yes", "No").contains(claimsDTO.getRequestedOverdraft()))
                || (StringUtils.isNotBlank(claimsDTO.getClaimAcceptance()) && !Arrays.asList(PARAM_ACCEPTED_TO_PAY, PARAM_ACCEPTED_TO_EXTEND, PARAM_REJECTED).contains(claimsDTO.getClaimAcceptance()))
                || (StringUtils.isNotBlank(claimsDTO.getMessageToBank()) && !alphaNumericWithSpaceMax200.matcher(claimsDTO.getMessageToBank()).matches())
                || (StringUtils.isNotBlank(claimsDTO.getReasonForRejection()) && !alphaNumericWithSpaceMax200.matcher(claimsDTO.getReasonForRejection()).matches())
                || (StringUtils.isNotBlank(claimsDTO.getClaimAmount()) && !numericMax35.matcher(claimsDTO.getClaimAmount()).matches())) {
            return false;
        }
        return true;
    }

}
