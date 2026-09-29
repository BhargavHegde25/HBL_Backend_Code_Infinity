/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
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
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ReceivedGuaranteeClaimsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ReceivedGuaranteesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteeClaimsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.ReceivedGuaranteeClaimsResource;
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

public class ReceivedGuaranteeClaimsResourceImpl implements ReceivedGuaranteeClaimsResource, TradeFinanceConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final PaymentAdviceBussinessDelegate paymentAdviceBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);
    private final GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate swiftMessagesBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class);
    private final ReceivedGuaranteeClaimsBusinessDelegate claimsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ReceivedGuaranteeClaimsBusinessDelegate.class);
    private final ReceivedGuaranteesBusinessDelegate guaranteeBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ReceivedGuaranteesBusinessDelegate.class);
    private final AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

    public Result createClaim(String methodId, HashMap<String, Object> inputParams, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        Result result = new Result();
        ReceivedGuaranteeClaimsDTO claimsDTO = new ReceivedGuaranteeClaimsDTO();
        ReceivedGuaranteesDTO guaranteeDetails = new ReceivedGuaranteesDTO();
        try {
            claimsDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), ReceivedGuaranteeClaimsDTO.class);
            if (StringUtils.isNotBlank(claimsDTO.getGuaranteesSRMSId())) {
                guaranteeDetails = guaranteeBusinessDelegate.getReceivedGuaranteeById(claimsDTO.getGuaranteesSRMSId(), request);
                if (StringUtils.isBlank(guaranteeDetails.getGuaranteeSrmsId()))
                    return ErrorCodeEnum.ERRTF_29051.setErrorCode(new Result());
                claimsDTO.setClaimCurrency(guaranteeDetails.getCurrency());
                claimsDTO.setProductType(guaranteeDetails.getProductType());
                claimsDTO.setGuaranteeAndSBLCType(guaranteeDetails.getLcType());
            }
        } catch (IOException e) {
            alert.prepareError("Error occurred while parsing input request params ", e).log();
        }

        String customerId = fetchCustomerFromSession(request);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        if (StringUtils.isNotBlank(claimsDTO.getClaimCreditAccount())) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, claimsDTO.getClaimCreditAccount())) {
                String accountEnding = "";
                if (claimsDTO.getClaimCreditAccount().length() > 3)
                    accountEnding = claimsDTO.getClaimCreditAccount()
                            .substring(claimsDTO.getClaimCreditAccount().length() - 3);
                String errorMessage = "You do not have permission to the Charges Account ending xxx"
                        + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }
        if (StringUtils.isNotBlank(claimsDTO.getChargesDebitAccount())) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId,
                    claimsDTO.getChargesDebitAccount())) {
                String accountEnding = "";
                if (claimsDTO.getChargesDebitAccount().length() > 3)
                    accountEnding = claimsDTO.getChargesDebitAccount()
                            .substring(claimsDTO.getChargesDebitAccount().length() - 3);
                String errorMessage = "You do not have permission to the Charges Account ending xxx"
                        + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }

        if (!_validateClaimDetails(claimsDTO))
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());

        switch (methodId) {
            case CREATERECEIVEDGUARANTEECLAIM_METHODID:
                claimsDTO.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
                if (claimsDTO.getClaimsSRMSId() == null)
                    claimsDTO = claimsBusinessDelegate.createClaim(claimsDTO, inputParams, request);
                else
                    claimsDTO = claimsBusinessDelegate.updateGuaranteeClaims(claimsDTO, false, null, request);
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_CLAIM_SUBMITTED;
                break;
            case SAVERECEIVEDGUARANTEECLAIM_METHODID:
                claimsDTO.setStatus(PARAM_STATUS_DRAFT);
                if (claimsDTO.getClaimsSRMSId() == null)
                    claimsDTO = claimsBusinessDelegate.createClaim(claimsDTO, inputParams, request);
                else
                    claimsDTO = claimsBusinessDelegate.updateGuaranteeClaims(claimsDTO, false, null, request);
                break;
            case UPDATERECEIVEDGUARANTEECLAIM_METHODID:
                claimsDTO = updateClaim(claimsDTO, request);
                alertToPush = StringUtils.isNotBlank(claimsDTO.getReturnedCount()) ? AlertsEnum.GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL
                        : AlertsEnum.GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL;
                break;
            case DELETERECEIVEDGUARANTEECLAIM_METHODID:
                claimsDTO.setStatus(PARAM_STATUS_DELETED);
                claimsDTO = claimsBusinessDelegate.updateGuaranteeClaims(claimsDTO, false, null, request);
                break;
        }

        if (StringUtils.isBlank(claimsDTO.getDbpErrMsg()) && StringUtils.isBlank(claimsDTO.getErrorMsg()) && StringUtils.isBlank(claimsDTO.getErrorCode())) {
            guaranteeDetails.setClaimInformation(getUpdatedClaimInfo(guaranteeDetails, claimsDTO));
            guaranteeBusinessDelegate.updateReceivedGuarantee(guaranteeDetails, request);
        }

        result = JSONToResult.convert(new JSONObject(claimsDTO).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, claimsDTO.getClaimsSRMSId());
        return result;
    }

    private ReceivedGuaranteeClaimsDTO updateClaim(ReceivedGuaranteeClaimsDTO inputClaimsDTO, DataControllerRequest request) {
        ReceivedGuaranteeClaimsDTO initiatedClaimsDTO = claimsBusinessDelegate.getClaimsById(inputClaimsDTO.getClaimsSRMSId(), request);
        JSONArray returnHistory = initiatedClaimsDTO.getReturnedHistory() != null ? new JSONArray(initiatedClaimsDTO.getReturnedHistory()) : new JSONArray();
        JSONObject discrepancyHistory = new JSONObject();
        discrepancyHistory.put("returnCount", returnHistory.length() + 1);
        if (StringUtils.isNotBlank(inputClaimsDTO.getDiscrepancies()))
            discrepancyHistory.put("discrepancies", new JSONArray(inputClaimsDTO.getDiscrepancies()));
        if (StringUtils.isNotBlank(initiatedClaimsDTO.getReturnedTime()))
            discrepancyHistory.put("returnedTime", initiatedClaimsDTO.getReturnedTime());
        if (StringUtils.isNotBlank(inputClaimsDTO.getReasonForReturn()))
            discrepancyHistory.put("reasonForReturn", inputClaimsDTO.getReasonForReturn());
        returnHistory.put(discrepancyHistory);
        initiatedClaimsDTO.setReturnedHistory(returnHistory.toString());
        initiatedClaimsDTO.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        inputClaimsDTO = claimsBusinessDelegate.updateGuaranteeClaims(inputClaimsDTO, true, initiatedClaimsDTO, request);
        inputClaimsDTO.setGuaranteesSRMSId(initiatedClaimsDTO.getGuaranteesSRMSId());

        if (StringUtils.isBlank(inputClaimsDTO.getDbpErrMsg()) && StringUtils.isBlank(inputClaimsDTO.getErrorMsg()) && StringUtils.isBlank(inputClaimsDTO.getErrorCode())) {
            ReceivedGuaranteesDTO guaranteeDetails = guaranteeBusinessDelegate.getReceivedGuaranteeById(inputClaimsDTO.getGuaranteesSRMSId(), request);
            guaranteeDetails.setClaimInformation(getUpdatedClaimInfo(guaranteeDetails, inputClaimsDTO));
            guaranteeBusinessDelegate.updateReceivedGuarantee(guaranteeDetails, request);
        }

        return inputClaimsDTO;
    }

    public Result updateClaimByBank(ReceivedGuaranteeClaimsDTO claimsDTO, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        ReceivedGuaranteeClaimsDTO initiatedClaimsDTO = claimsBusinessDelegate.getClaimsById(claimsDTO.getClaimsSRMSId(), request);
        ReceivedGuaranteesDTO guaranteesDTO = guaranteeBusinessDelegate.getReceivedGuaranteeById(initiatedClaimsDTO.getGuaranteesSRMSId(), request);
        if (initiatedClaimsDTO.getStatus().equalsIgnoreCase(PARAM_STATUS_DRAFT)
                || initiatedClaimsDTO.getStatus().equals(PARAM_STATUS_CLAIMEXTENDED)
                || initiatedClaimsDTO.getStatus().equals(PARAM_STATUS_RETURNED_by_BANK)
                || initiatedClaimsDTO.getStatus().equals(PARAM_STATUS_CLAIMHONOURED)) {
            diagnostic.prepareDebug("Requested record is in the status - " + initiatedClaimsDTO.getStatus()).log();
            return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
        }

        switch (claimsDTO.getStatus()) {
            case PARAM_STATUS_CLAIMEXTENDED:
                guaranteesDTO.setStatus(PARAM_STATUS_CLAIMEXTENDED);
                break;
            case PARAM_STATUS_CLAIMHONOURED:
                double utilizedAmount = Double.parseDouble(guaranteesDTO.getUtilizedAmount()) + Double.parseDouble(initiatedClaimsDTO.getClaimAmount());
                guaranteesDTO.setUtilizedAmount(String.valueOf(utilizedAmount));
                if (utilizedAmount == Double.parseDouble(guaranteesDTO.getAmount())) {
                    guaranteesDTO.setStatus(PARAM_STATUS_CLAIMHONOURED);
                }
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED;
                break;
            case PARAM_STATUS_RETURNED_by_BANK:
                if (StringUtils.isBlank(claimsDTO.getReasonForReturn())) {
                    return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
                }
                break;
            case PARAM_STATUS_REJECTED_by_BANK:
                alertToPush = StringUtils.isBlank(initiatedClaimsDTO.getReturnedCount()) ? AlertsEnum.GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED
                        : AlertsEnum.GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED;
                break;
            case PARAM_STATUS_PROCESSING_WITH_BANK:
                alertToPush = AlertsEnum.GUARANTEES_RECEIVED_CLAIM_ACTIVE;
                break;
            default:
                diagnostic.prepareDebug("Invalid Status found in payload").log();
                return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
        }
        claimsDTO.setReturnedTime(getCurrentDateTimeUTF());
        claimsDTO.setReturnedCount(initiatedClaimsDTO.getReturnedCount() != null ? String.valueOf(Integer.parseInt(initiatedClaimsDTO.getReturnedCount()) + 1) : "1");
        if (StringUtils.isNotBlank(initiatedClaimsDTO.getDocumentInformation()))
            initiatedClaimsDTO.setDocumentInformation(new JSONArray(initiatedClaimsDTO.getDocumentInformation()).toString());
        if (StringUtils.isNotBlank(initiatedClaimsDTO.getDiscrepancies()))
            initiatedClaimsDTO.setDiscrepancies(new JSONArray(initiatedClaimsDTO.getDiscrepancies()).toString());
        if (StringUtils.isNotBlank(initiatedClaimsDTO.getReturnedHistory()))
            initiatedClaimsDTO.setReturnedHistory(new JSONArray(initiatedClaimsDTO.getReturnedHistory()).toString());
        claimsDTO = claimsBusinessDelegate.updateGuaranteeClaims(claimsDTO, true, initiatedClaimsDTO, request);
        claimsDTO.setGuaranteesSRMSId(initiatedClaimsDTO.getGuaranteesSRMSId());

        if (StringUtils.isBlank(claimsDTO.getDbpErrMsg()) && StringUtils.isBlank(claimsDTO.getErrorMsg()) && StringUtils.isBlank(claimsDTO.getErrorCode())) {
            guaranteesDTO.setClaimInformation(getUpdatedClaimInfo(guaranteesDTO, claimsDTO));
            guaranteeBusinessDelegate.updateReceivedGuarantee(guaranteesDTO, request);
        }

        Result result = JSONToResult.convert(new JSONObject(claimsDTO).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, claimsDTO.getClaimsSRMSId());
        return result;
    }

    public Result getClaims(HashMap<String, Object> inputParams, DataControllerRequest request) {
        Result result;

        List<ReceivedGuaranteeClaimsDTO> claimsList = claimsBusinessDelegate.getClaims(request);
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
            alert.prepareError("Error occurred while parsing response ", e).log();
        }
        result = JSONToResult.convert((new JSONObject()).put("GuaranteeClaims", claimsList).toString());

        return result;
    }

    public Result getClaimsById(HashMap<String, Object> inputParams, DataControllerRequest request) {

        String claimsId = (String) inputParams.get("claimsSRMSId");
        if (StringUtils.isBlank(claimsId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }
        ReceivedGuaranteeClaimsDTO claimsDTO = claimsBusinessDelegate.getClaimsById(claimsId, request);
        JSONObject resultObject = new JSONObject(claimsDTO);
        try {
            request.addRequestParam_("orderId", claimsId);
            request.addRequestParam_("module", "RGCL");
            List<SwiftsAndAdvisesDTO> swiftMessages = swiftMessagesBusinessDelegate.getGuaranteeSwiftAdvices(request);
            resultObject.put("SwiftMessages", new JSONArray(swiftMessages));
            List<PaymentAdviceDTO> paymentAdvices = paymentAdviceBusinessDelegate.getPaymentAdvice(request);
            resultObject.put("PaymentAdvices", new JSONArray(paymentAdvices));
        } catch (Exception e) {
            alert.prepareError("Failed to get Payment Advices", e).log();
        }
        return JSONToResult.convert(resultObject.toString());
    }

    public Result generateReceivedGuaranteeClaim(DataControllerRequest request) {
        String claimsId = request.getParameter("claimsSRMSId");
        if (StringUtils.isBlank(claimsId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }
        ReceivedGuaranteeClaimsDTO claimsDTO = claimsBusinessDelegate.getClaimsById(claimsId, request);
        byte[] pdfBytes = claimsBusinessDelegate.generateReceivedGuaranteeClaim(claimsDTO, request);
        if (ArrayUtils.isEmpty(pdfBytes)) {
            alert.prepareError("Failed to generate report").log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
        }
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(PREFIX_RECEIVED_GUARANTEE_CLAIM);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
        Result result = new Result();
        result.addParam("fileId", fileId);
        return result;
    }

    private String getUpdatedClaimInfo(ReceivedGuaranteesDTO guaranteeDTO, ReceivedGuaranteeClaimsDTO claimsDTO) {
        JSONObject claimInfo = StringUtils.isNotBlank(guaranteeDTO.getClaimInformation()) ? new JSONObject(guaranteeDTO.getClaimInformation()) : new JSONObject();
        claimInfo.remove(claimsDTO.getClaimsSRMSId());
        claimInfo.put(claimsDTO.getClaimsSRMSId(), claimsDTO.getStatus());
        return String.valueOf(claimInfo);
    }

    private boolean _validateClaimDetails(ReceivedGuaranteeClaimsDTO claimsDTO) {
        Pattern alphaNumericWithSpaceMax200 = Pattern.compile("^[a-zA-Z0-9 .,-]{0,200}$");
        Pattern numericMax35 = Pattern.compile("^[0-9]+([.][0-9]+?){0,35}$");
        if ((StringUtils.isNotBlank(claimsDTO.getDemandType()) && !Arrays.asList(PARAM_PAY, PARAM_PAY_OR_EXTEND).contains(claimsDTO.getDemandType()))
                || (StringUtils.isNotBlank(claimsDTO.getForwardDocuments()) && !Arrays.asList("Selected", "Unselected").contains(claimsDTO.getForwardDocuments()))
                || (StringUtils.isNotBlank(claimsDTO.getMessageToBank()) && !alphaNumericWithSpaceMax200.matcher(claimsDTO.getMessageToBank()).matches())
                || (StringUtils.isNotBlank(claimsDTO.getOtherDemandDetails()) && !alphaNumericWithSpaceMax200.matcher(claimsDTO.getOtherDemandDetails()).matches())
                || (StringUtils.isNotBlank(claimsDTO.getClaimAmount()) && !numericMax35.matcher(claimsDTO.getClaimAmount()).matches())) {
            return false;
        }

        try {
            if (StringUtils.isNotBlank(claimsDTO.getPhysicalDocuments())) {
                JSONArray documents = new JSONArray(claimsDTO.getPhysicalDocuments());
                for (int i = 0; i < documents.length(); i++) {
                    JSONObject document = documents.getJSONObject(i);
                    if (!numericMax35.matcher(document.getString("copiesCount")).matches()
                            || !numericMax35.matcher(document.getString("originalsCount")).matches()
                            || !alphaNumericWithSpaceMax200.matcher(document.getString("documentTitle")).matches()) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            alert.prepareError("Failed while validating physical documents", e).log();
            return false;
        }

        return true;
    }

}
