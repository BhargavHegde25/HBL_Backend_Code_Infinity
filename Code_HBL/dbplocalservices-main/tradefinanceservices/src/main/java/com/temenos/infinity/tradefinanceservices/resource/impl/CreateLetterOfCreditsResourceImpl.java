package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.CreateLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetAmendmentsLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetLetterOfCreditsByIDBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.UpdateLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.Constants;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.*;
import com.temenos.infinity.tradefinanceservices.resource.api.CreateLetterOfCreditsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices.TRANSACT_TF_CREATE_IMPORT_LC;
import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERR_12007;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class CreateLetterOfCreditsResourceImpl implements CreateLetterOfCreditsResource {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private boolean isAlertEligible = false;
    static String[] keys = new String[]{"availableWith1", "availableWith2", "availableWith3", "availableWith4",
            "documentCharges", "lcReferenceNo", "expiryPlace", "beneficiaryName", "beneficiaryAddressLine1",
            "beneficiaryAddressLine2", "beneficiaryBankPostCode", "beneficiaryBank", "beneficiaryBankAdressLine1",
            "beneficiaryBankAdressLine2", "beneficiaryBankCountry", "placeOfTakingIncharge", "portOfLoading",
            "portOfDischarge", "placeOfFinalDelivery", "additionalConditionsCode", "supportDocuments", "messageToBank",
            "lcAmount", "maximumCreditAmount", "chargesPaid", "creditAmount"};
    static Integer[] values = new Integer[]{50, 50, 50, 50, 35, 35, 35, 35, 50, 50, 35, 35, 50, 50, 35, 35, 35, 35,
            35, 35, 35, 200, 35, 35, 35, 35};
    static Map<String, Integer> validationMap = IntStream.range(0, keys.length).boxed()
            .collect(Collectors.toMap(i -> keys[i], i -> values[i]));
    GetAmendmentsLetterOfCreditsBusinessDelegate amendmentBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GetAmendmentsLetterOfCreditsBusinessDelegate.class);
    AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
    CreateLetterOfCreditsBusinessDelegate lcCreateBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CreateLetterOfCreditsBusinessDelegate.class);
    UpdateLetterOfCreditsBusinessDelegate lcUpdateBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(UpdateLetterOfCreditsBusinessDelegate.class);
    GetLetterOfCreditsByIDBusinessDelegate lcGetBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GetLetterOfCreditsByIDBusinessDelegate.class);

    @SuppressWarnings("unused")
    @Override
    public Result createLetterOfCredits(LetterOfCreditsDTO letterOfCredit, DataControllerRequest request) {
        LetterOfCreditsDTO responseDto;
        Result result = new Result();
        LetterOfCreditsDTO isSRMSID_match;
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);

        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        // checking fields
        String lcReferenceNo = letterOfCredit.getLcReferenceNo();
        String issueDate = request.getParameter("issueDate") != null ? request.getParameter("issueDate") : "";
        String expiryDate = request.getParameter("expiryDate") != null ? request.getParameter("expiryDate") : "";
        String latestShippingDate = request.getParameter("latestShippingDate") != null ? request.getParameter("latestShippingDate") : "";
        String chargesAccount = request.getParameter("chargesAccount") != null ? request.getParameter("chargesAccount") : "";
        String marginAccount = request.getParameter("marginAccount") != null ? request.getParameter("marginAccount") : "";
        String commisionAccount = request.getParameter("commisionAccount") != null ? request.getParameter("commisionAccount") : "";
        Map<String, String> inputMap = null;
        try {
            inputMap = JSONUtils.parseAsMap((new JSONObject(letterOfCredit)).toString(), String.class, String.class);
        } catch (IOException e) {
            alert.prepareError("Failed to create letter of credits", e).log();
        }
        boolean isValid = validateLcInputParams(letterOfCredit, inputMap);
        if (!isValid) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }

        if (StringUtils.isNotBlank(issueDate)) {
            if (!(_isDateValidate(issueDate))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }

        if (StringUtils.isNotBlank(expiryDate)) {
            if (!(_isDateValidate(expiryDate))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }

        if (StringUtils.isNotBlank(latestShippingDate)) {
            if (!(_isDateValidate(latestShippingDate))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }

        // Check whether user has the permission to the account
        String accountEnding = "";
        String errorMessage = "";
        if (StringUtils.isNotBlank(chargesAccount)) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, chargesAccount)) {
                if (chargesAccount.length() > 3)
                    accountEnding = chargesAccount.substring(chargesAccount.length() - 3);
                errorMessage = "You do not have permission to the Charges Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }
        if (StringUtils.isNotBlank(marginAccount)) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, marginAccount)) {
                if (marginAccount.length() > 3)
                    accountEnding = marginAccount.substring(marginAccount.length() - 3);
                errorMessage = "You do not have permission to the Margin Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }
        if (StringUtils.isNotBlank(commisionAccount)) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, commisionAccount)) {
                if (commisionAccount.length() > 3)
                    accountEnding = commisionAccount.substring(commisionAccount.length() - 3);
                errorMessage = "You do not have permission to the Commission Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }

        // Mandatory Flow type - for Final submit
        LetterOfCreditsDTO transactResponse = new LetterOfCreditsDTO();
        if (letterOfCredit.getFlowType().equalsIgnoreCase("finalSubmit")) {
            if (StringUtils.isNotBlank(letterOfCredit.getLcReferenceNo())
                    && StringUtils.isNotBlank(String.valueOf(letterOfCredit.getLcAmount()))
                    && StringUtils.isNotBlank(letterOfCredit.getLcCurrency())
                    && StringUtils.isNotBlank(letterOfCredit.getPaymentTerms())
                    && StringUtils.isNotBlank(letterOfCredit.getAvailableWith1())
                    && StringUtils.isNotBlank(letterOfCredit.getIssueDate())
                    && StringUtils.isNotBlank(letterOfCredit.getExpiryDate())
                    && StringUtils.isNotBlank(letterOfCredit.getExpiryPlace())
                    && StringUtils.isNotBlank(letterOfCredit.getBeneficiaryName())
                    && StringUtils.isNotBlank(letterOfCredit.getBeneficiaryAddressLine1())
                    && StringUtils.isNotBlank(letterOfCredit.getBeneficiaryBank())
                    && StringUtils.isNotBlank(letterOfCredit.getIncoTerms())
                    && StringUtils.isNotBlank(letterOfCredit.getDocumentCharges())
                    && StringUtils.isNotBlank(letterOfCredit.getConfirmationInstruction())) {
                if (isTransactEnabled()) {
                    transactResponse = createImportLcInTransact(request, letterOfCredit);
                    if (StringUtils.isBlank(transactResponse.getTransactReference()))
                        return JSONToResult.convert(String.valueOf(new JSONObject(transactResponse)));
                }
                if (StringUtils.isNotBlank(letterOfCredit.getSrmsReqOrderID())) {
                    try {
                        isSRMSID_match = lcGetBusinessDelegate.getImportLCById(letterOfCredit.getSrmsReqOrderID(), request);
                        if (isSRMSID_match.isReferenceNomatch()) {
                            try {
                                responseDto = lcUpdateBusinessDelegate.updateLetterOfCredits(letterOfCredit, request);
                                diagnostic.prepareInfo("Finalsubmit LOC has been updated").log();
                            } catch (Exception e) {
                                alert.prepareError("Failed to Update letter of credits request in OMS", e).log();
                                return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
                            }
                        } else {
                            try {
                                responseDto = lcCreateBusinessDelegate.createLetterOfCredits(letterOfCredit, request);
                                diagnostic.prepareInfo("Finalsubmit LOC has been created").log();
                            } catch (Exception e) {
                                alert.prepareError("Failed to Create letter of credits request in OMS", e).log();
                                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                            }
                        }
                    } catch (Exception e) {
                        alert.prepareError("Failed to verify Request ID of letter of credits request in OMS", e).log();
                        return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
                    }
                } else {
                    try {
                        responseDto = lcCreateBusinessDelegate.createLetterOfCredits(letterOfCredit, request);
                    } catch (Exception e) {
                        alert.prepareError("Failed to create letter of credits request in OMS", e).log();
                        return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
                    }

                }
                isAlertEligible = StringUtils.isNotBlank(responseDto.getSrmsReqOrderID());
            } else {
                alert.prepareError("Mandatory fields are missing").log();
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }
        }

        // Draft Flow type
        else {
            if (StringUtils.isNotBlank(lcReferenceNo)) {
                // In case of Draft to updated
                if (StringUtils.isNotBlank(letterOfCredit.getSrmsReqOrderID())) {
                    try {
                        isSRMSID_match = lcGetBusinessDelegate.getImportLCById(letterOfCredit.getSrmsReqOrderID(), request);
                        if (isSRMSID_match.isReferenceNomatch()) {
                            try {
                                responseDto = lcUpdateBusinessDelegate.updateLetterOfCredits(letterOfCredit, request);
                                diagnostic.prepareInfo("Draft LOC has been updated").log();
                            } catch (Exception e) {
                                alert.prepareError("Update lc failed").log();
                                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
                            }
                        } else {
                            return ErrorCodeEnum.ERRTF_29051.setErrorCode(new Result(), "SRMSID fetch failed");
                        }
                    } catch (Exception e) {
                        alert.prepareError("Failed to verify Request ID of letter of credits request in OMS", e).log();
                        return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
                    }

                }
                // In case of Draft to Created
                else {

                    try {
                        responseDto = lcCreateBusinessDelegate.createLetterOfCredits(letterOfCredit, request);
                    } catch (Exception e) {
                        alert.prepareError("Failed to create letter of credits request in OMS", e).log();
                        return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
                    }
                }
            } else {
                alert.prepareError("Field ReferenceNo is Blank").log();
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }
        }

        if (StringUtils.isBlank(responseDto.getSrmsReqOrderID())) {
            return ErrorCodeEnum.ERRTF_29052.setErrorCode(new Result());
        }
        if (StringUtils.isNotBlank(responseDto.getMsg())) {
            result.addParam("ErrorMsg", responseDto.getMsg());
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result(), responseDto.getMsg());
        }

        if (StringUtils.isNotBlank(transactResponse.getTransactReference())) {
            responseDto.setTransactReference(transactResponse.getTransactReference());
        }
        JSONObject responseObj = new JSONObject(responseDto);
        result = JSONToResult.convert(responseObj.toString());
        result.addParam("srmsReqOrderId", responseDto.getSrmsReqOrderID());
        result.addParam("status", responseDto.getStatus());
        if (isAlertEligible) {
            TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.IMPORT_LC_CREATED, responseDto.getSrmsReqOrderID());
        }
        return result;
    }

    @Override
    public Result executeLetterOfCreditsRequest(String methodId, Object[] inputArray, DataControllerRequest request,
                                                DataControllerResponse response) {
        Result result = new Result();
        try {
            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
            String serviceRequestId = request.getParameter("serviceRequestId") != null
                    ? request.getParameter("serviceRequestId").toString()
                    : null;
            String requestId = request.getParameter("requestId") != null ? request.getParameter("requestId").toString()
                    : "";
            String comments = inputParams.get("comments") != null ? inputParams.get("comments").toString() : "";
            String signatoryApproved = inputParams.get("signatoryApproved") != null
                    ? inputParams.get("signatoryApproved").toString()
                    : "";

            LetterOfCreditsActionDTO lcOrder = new LetterOfCreditsActionDTO();
            lcOrder.setRequestId(serviceRequestId);
            lcOrder.setComments(comments);
            lcOrder.setSignatoryAction(signatoryApproved);

            CreateLetterOfCreditsBusinessDelegate letterOfCreditBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CreateLetterOfCreditsBusinessDelegate.class);
            BBRequestDTO bbrequest = letterOfCreditBusinessDelegate.getAccountId(requestId);

            LetterOfCreditStatusDTO letterOfCreditDTO = new LetterOfCreditStatusDTO();
            letterOfCreditDTO.setAccountId(bbrequest.getAccountId());
            letterOfCreditDTO.setRequestId(requestId);
            letterOfCreditDTO.setCustomerId(customerId);
            letterOfCreditDTO = letterOfCreditBusinessDelegate.validateForApprovals(letterOfCreditDTO, request);

            if (letterOfCreditDTO == null) {
                alert.prepareError("Error occurred while validating for approvals").log();
                return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
            }
            if (letterOfCreditDTO.getStatus().equalsIgnoreCase("sent")) {

                try {

                    lcOrder = letterOfCreditBusinessDelegate.executeLetterOfCredits(lcOrder, request);

                    JSONObject letterOfOrderDTO = new JSONObject(lcOrder);
                    result = JSONToResult.convert(letterOfOrderDTO.toString());

                } catch (Exception e) {
                    alert.prepareError(e.toString()).log();
                    diagnostic.prepareDebug("Failed to create cheque book request in OMS " + e).log();
                    return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
                }
            } else if (letterOfCreditDTO.getStatus().equalsIgnoreCase("pending")) {
                // return pending status;

            }

        } catch (Exception e) {
            alert.prepareError("Caught exception at approve method: " + e).log();
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(result);
        }
        return result;
    }

    @Override
    public Result amendLetterOfCredits(LetterOfCreditsAmendmentDTO inputDto, DataControllerRequest request) {
        LetterOfCreditsAmendmentDTO amendmentResponse;
        LetterOfCreditsDTO lcData;
        Result result = new Result();
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);

        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        try {
            lcData = lcGetBusinessDelegate.getImportLCById(inputDto.getLcSRMSId(), request);
            if (!StringUtils.equals(lcData.getStatus(), PARAM_STATUS_APPROVED)) {
                return ErrorCodeEnum.ERR_12006.setErrorCode(new Result());
            }
        } catch (Exception e) {
            alert.prepareError("Unable to get amend Letter Of Credit " + e).log();
            return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
        }
        Map<String, String> inputMap = null;
        try {
            inputMap = JSONUtils.parseAsMap((new JSONObject(inputDto)).toString(), String.class, String.class);
        } catch (IOException e) {
            alert.prepareError("Failed to create letter of credits", e).log();
        }
        // Validations Check
        boolean isValid = validateAmendmentInputParams(inputDto, inputMap);
        if (!isValid) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }
        String expiryDate = request.getParameter("expiryDate") != null ? request.getParameter("expiryDate") : "";
        String latestShippingDate = request.getParameter("latestShippingDate") != null
                ? request.getParameter("latestShippingDate")
                : "";
        String amendmentExpiryDate = request.getParameter("amendmentExpiryDate") != null
                ? request.getParameter("amendmentExpiryDate")
                : "";
        String chargesAccount = request.getParameter("chargesAccount") != null ? request.getParameter("chargesAccount")
                : "";
        String issueDate = request.getParameter("issueDate") != null ? request.getParameter("issueDate") : "";
        String lcAmount = request.getParameter("lcAmount") != null ? request.getParameter("lcAmount") : "";
        String lcSRMSId = request.getParameter("lcSRMSId") != null ? request.getParameter("lcSRMSId") : "";
        String lcrefno = request.getParameter("lcReferenceNo") != null ? request.getParameter("lcReferenceNo") : "";
        String paymentTerms = request.getParameter("paymentTerms") != null ? request.getParameter("paymentTerms") : "";
        String benefeciaryName = request.getParameter("beneficiaryName") != null
                ? request.getParameter("beneficiaryName")
                : "";

        // Validate values with the importLC values
        if ((!StringUtils.equals(lcrefno, lcData.getLcReferenceNo()))
                || (!StringUtils.equals(paymentTerms, lcData.getPaymentTerms()))
                || (!StringUtils.equals(benefeciaryName, lcData.getBeneficiaryName()))) {
            return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
        }
        // Validate issue date with the importLC issueDate
        try {
            String issuedate1 = HelperMethods.changeDateFormat(issueDate, Constants.TIMESTAMP_FORMAT);
            if (!(lcData.getIssueDate().equals(issuedate1))) {
                alert.prepareError("Issue Date does not match with the importLC issue Date").log();
                return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
            }
        } catch (Exception e) {
            alert.prepareError("Error" + e).log();
        }

        if (!StringUtils.isNumeric(lcAmount)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }

        if (StringUtils.isNotBlank(issueDate)) {
            if (!(_isDateValidate(issueDate))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }

        if (StringUtils.isNotBlank(expiryDate)) {
            if (!(_isDateValidate(expiryDate))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(amendmentExpiryDate)) {
            if (!(_isDateValidate(amendmentExpiryDate))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(latestShippingDate)) {
            if (!(_isDateValidate(latestShippingDate))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isBlank(lcSRMSId)) {
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }
        // Set amendmentApproveDate
        inputDto.setAmendmentDate(getCurrentDateTimeUTF());
        inputDto.setAmendStatus(PARAM_STATUS_SUBMITTED_TO_BANK);

        String accountEnding = "";
        String errorMessage = "";
        if (StringUtils.isNotBlank(chargesAccount)) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, chargesAccount)) {
                if (chargesAccount.length() > 3)
                    accountEnding = chargesAccount.substring(chargesAccount.length() - 3);
                errorMessage = "You do not have permission to the Charges Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }
        LetterOfCreditsDTO drawingdto = new LetterOfCreditsDTO();
        if (StringUtils.isNotBlank(inputDto.getLcSRMSId())) {
            drawingdto.setSrmsReqOrderID(inputDto.getLcSRMSId());
        } else {
            return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
        }
        // Call BusinessDelegateClass
        try {
            amendmentResponse = lcCreateBusinessDelegate.amendLetterOfCredit(inputDto, request);
        } catch (ApplicationException e) {
            alert.prepareError("Failed to amend letter of credits in OMS").log();
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
        }

        result = JSONToResult.convert(new JSONObject(amendmentResponse).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL, amendmentResponse.getAmendmentReference());
        return result;
    }

    @Override
    public Result updateImportLCAmendmentByBank(LetterOfCreditsAmendmentDTO inputDto, DataControllerRequest request) {
        Result result;
        AlertsEnum alertToPush;
        try {
            LetterOfCreditsAmendmentDTO amendmentDto = amendmentBusinessDelegate.getAmendmentsById(inputDto.getAmendmentReference(), request);
            switch (inputDto.getAmendStatus()) {
                case PARAM_STATUS_PROCESSING_by_BANK:
                    alertToPush = AlertsEnum.IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL;
                    break;
                case PARAM_STATUS_APPROVED:
                    alertToPush = AlertsEnum.IMPORT_LC_AMENDMENT_APPROVED;
                    break;
                case PARAM_STATUS_REJECTED:
                    alertToPush = AlertsEnum.IMPORT_LC_AMENDMENT_REJECTED;
                    break;
                default:
                    return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
            }

            amendmentDto.setAmendStatus(inputDto.getAmendStatus());
            amendmentDto.setChargesPaid(inputDto.getChargesPaid());
            LetterOfCreditsAmendmentDTO letterOfCredits = lcCreateBusinessDelegate.updateAmendLC(amendmentDto, request);
            result = JSONToResult.convert(new JSONObject(letterOfCredits).toString());
            TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, letterOfCredits.getAmendmentReference());
            result.addParam("message", "Record updated Successfully");
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to Update amend letter of credits by Bank from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
        }
        return result;
    }

    private LetterOfCreditsDTO createImportLcInTransact(DataControllerRequest request, LetterOfCreditsDTO inputDto) {
        Map<String, Object> inputMap;
        Result result;
        LetterOfCreditsDTO responseDTO = new LetterOfCreditsDTO();
        try {
            if (StringUtils.isNotBlank(inputDto.getLatestShippingDate()))
                inputDto.setLatestShippingDate(formatDate2(inputDto.getLatestShippingDate()).replace("-", ""));
            inputMap = new ObjectMapper().convertValue(inputDto, Map.class);
            result = CommonUtils.callIntegrationService(request, inputMap, getHeadersMap(request), TRANSACT_TF_CREATE_IMPORT_LC.getServiceName(), TRANSACT_TF_CREATE_IMPORT_LC.getOperationName(), false);
            Record headerData = result.getRecordById("header");
            if (StringUtils.equals(headerData.getParamValueByName("status"), "success"))
                responseDTO.setTransactReference(headerData.getParamValueByName("id"));
            else if (StringUtils.equals(headerData.getParamValueByName("status"), "failed")) {
                responseDTO.setErrorCode(ERR_12007.getErrorCodeAsString());
                responseDTO.setErrorMessage(ERR_12007.getErrorMessage());
                responseDTO.setErrorDetails(result.getRecordById("error").getDatasetById("errorDetails").getAllRecords().toString());
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating invoice in transact", e).log();
            responseDTO.setErrorCode(ERR_12007.getErrorCodeAsString());
            responseDTO.setErrorMessage(ERR_12007.getErrorMessage());
            responseDTO.setErrorDetails("Backend Failed");
        }

        return responseDTO;
    }

    public boolean validateLcInputParams(LetterOfCreditsDTO letterOfCreditDto, Map<String, String> inputParams) {
        String alphaNumericMax35Chars = "^[a-zA-Z0-9]{0,35}$";
        String alphaNumericMax35CharsWithSapce = "^[a-zA-Z0-9 ]{0,35}$";
        String alphaNumericMax200Chars = "^[a-zA-Z0-9]{0,200}$";
        String alphaNumericMax200CharsWithSpace = "^[a-zA-Z0-9 ]{0,200}$";
        String specialCharUnacceptable = "[<>=*]";
        Matcher matcher;
        boolean isValid = false;
        Pattern alphaNumericMax35CharsPattern = Pattern.compile(alphaNumericMax35Chars);
        Pattern alphaNumericMax200CharsPattern = Pattern.compile(alphaNumericMax200Chars);
        Pattern alphaNumericMax35CharsWithSapcePattern = Pattern.compile(alphaNumericMax35CharsWithSapce);
        Pattern alphaNumericMax200CharsWithSpacePattern = Pattern.compile(alphaNumericMax200CharsWithSpace);
        Pattern specialCharUnacceptablePattern = Pattern.compile(specialCharUnacceptable);

        if (StringUtils.isNotBlank(letterOfCreditDto.getAdditionalAmountPayable())) {
            try {
                Double.parseDouble(letterOfCreditDto.getAdditionalAmountPayable().trim());
            } catch (Exception e) {
                return false;
            }
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getBeneficiaryCountry())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getBeneficiaryCountry());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getPresentationPeriod())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getPresentationPeriod());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getTransshipment())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(letterOfCreditDto.getTransshipment());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getPartialShipments())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(letterOfCreditDto.getPartialShipments());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getIncoTerms())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(letterOfCreditDto.getIncoTerms());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getModeOfShipment())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(letterOfCreditDto.getModeOfShipment());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getDescriptionOfGoods())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(letterOfCreditDto.getDescriptionOfGoods());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getDocumentsRequired())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getDocumentsRequired());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getConfirmationInstruction())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(letterOfCreditDto.getConfirmationInstruction());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getTransferable())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getTransferable());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getStandByLC())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getStandByLC());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getLcCurrency())) {
            matcher = alphaNumericMax200CharsPattern.matcher(letterOfCreditDto.getLcCurrency());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getPaymentTerms()) && !Arrays.asList("Sight", "Deferred", "Acceptance", "Negotiation Sight", "Negotiation Acceptance").contains(letterOfCreditDto.getPaymentTerms())) {
            return false;
        }
        List<String> currencyList = Arrays.asList("INR", "EUR", "GBP", "USD");

        if (StringUtils.isNotBlank(letterOfCreditDto.getLcCurrency()) && !currencyList.contains(letterOfCreditDto.getLcCurrency())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getAdditionalPayableCurrency()) && !currencyList.contains(letterOfCreditDto.getAdditionalPayableCurrency())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getTransshipment()) && !Arrays.asList("Allowed", "Not Allowed").contains(letterOfCreditDto.getTransshipment())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getPartialShipments()) && !Arrays.asList("Allowed", "Not Allowed", "Conditional").contains(letterOfCreditDto.getPartialShipments())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getIncoTerms()) && !Arrays.asList("CIF", "CFR", "FOB", "FCA", "FAS", "CPT", "CIP", "DAP", "DDP", "DDU", "DES", "DEQ", "EXW").contains(letterOfCreditDto.getIncoTerms())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getModeOfShipment()) && !Arrays.asList("Air", "Sea", "Road").contains(letterOfCreditDto.getModeOfShipment())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getConfirmationInstruction()) && !Arrays.asList("Confirm", "May Add", "Without").contains(letterOfCreditDto.getConfirmationInstruction())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getTransferable()) && !Arrays.asList("Yes", "No").contains(letterOfCreditDto.getTransferable())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getStandByLC()) && !Arrays.asList("Yes", "No").contains(letterOfCreditDto.getStandByLC())) {
            return false;
        }

        if (StringUtils.isNotBlank(letterOfCreditDto.getTolerancePercentage())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(letterOfCreditDto.getTolerancePercentage());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getAmountType())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getAmountType());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getOtherAmendments())) {
            matcher = alphaNumericMax200CharsWithSpacePattern.matcher(letterOfCreditDto.getOtherAmendments());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getAmendCharges())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getAmendCharges());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getAmendStatus())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getAmendStatus());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(letterOfCreditDto.getLcSRMSId())) {
            matcher = alphaNumericMax35CharsPattern.matcher(letterOfCreditDto.getLcSRMSId());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (inputParams != null) {
            for (String key : inputParams.keySet()) {
                String value = inputParams.get(key);
                if ((validationMap.containsKey(key)) && value.length() > validationMap.get(key))
                    return false;

                matcher = specialCharUnacceptablePattern.matcher(value);
                if (matcher.find())
                    return false;
            }
        }

        return isValid;

    }

    public boolean validateAmendmentInputParams(LetterOfCreditsAmendmentDTO amendmentDto, Map<String, String> inputParams) {
        String alphaNumericMax35Chars = "^[a-zA-Z0-9]{0,35}$";
        String alphaNumericMax35CharsWithSapce = "^[a-zA-Z0-9 ]{0,35}$";
        String alphaNumericMax200Chars = "^[a-zA-Z0-9]{0,200}$";
        String alphaNumericMax200CharsWithSpace = "^[a-zA-Z0-9 ]{0,200}$";
        String specialCharUnacceptable = "[<>=*]";
        Matcher matcher;
        boolean isValid = false;
        Pattern alphaNumericMax35CharsPattern = Pattern.compile(alphaNumericMax35Chars);
        Pattern alphaNumericMax200CharsPattern = Pattern.compile(alphaNumericMax200Chars);
        Pattern alphaNumericMax35CharsWithSapcePattern = Pattern.compile(alphaNumericMax35CharsWithSapce);
        Pattern alphaNumericMax200CharsWithSpacePattern = Pattern.compile(alphaNumericMax200CharsWithSpace);
        Pattern specialCharUnacceptablePattern = Pattern.compile(specialCharUnacceptable);

        if (StringUtils.isNotBlank(amendmentDto.getAdditionalAmountPayable())) {
            try {
                Double.parseDouble(amendmentDto.getAdditionalAmountPayable().trim());
            } catch (Exception e) {
                return false;
            }
        }

        if (StringUtils.isNotBlank(amendmentDto.getBeneficiaryCountry())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getBeneficiaryCountry());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getPresentationPeriod())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getPresentationPeriod());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getTransshipment())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(amendmentDto.getTransshipment());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getPartialShipments())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(amendmentDto.getPartialShipments());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getIncoTerms())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(amendmentDto.getIncoTerms());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getModeOfShipment())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(amendmentDto.getModeOfShipment());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getDescriptionOfGoods())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(amendmentDto.getDescriptionOfGoods());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getDocumentsRequired())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getDocumentsRequired());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getConfirmationInstruction())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(amendmentDto.getConfirmationInstruction());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getTransferable())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getTransferable());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getStandByLC())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getStandByLC());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getLcCurrency())) {
            matcher = alphaNumericMax200CharsPattern.matcher(amendmentDto.getLcCurrency());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getPaymentTerms()) && !Arrays.asList("Sight", "Deferred", "Acceptance", "Negotiation Sight", "Negotiation Acceptance").contains(amendmentDto.getPaymentTerms())) {
            return false;
        }
        List<String> currencyList = Arrays.asList("INR", "EUR", "GBP", "USD");

        if (StringUtils.isNotBlank(amendmentDto.getLcCurrency()) && !currencyList.contains(amendmentDto.getLcCurrency())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getAdditionalPayableCurrency()) && !currencyList.contains(amendmentDto.getAdditionalPayableCurrency())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getTransshipment()) && !Arrays.asList("Allowed", "Not Allowed").contains(amendmentDto.getTransshipment())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getPartialShipments()) && !Arrays.asList("Allowed", "Not Allowed", "Conditional").contains(amendmentDto.getPartialShipments())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getIncoTerms()) && !Arrays.asList("CIF", "CFR", "FOB", "FCA", "FAS", "CPT", "CIP", "DAP", "DDP", "DDU", "DES", "DEQ", "EXW").contains(amendmentDto.getIncoTerms())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getModeOfShipment()) && !Arrays.asList("Air", "Sea", "Road").contains(amendmentDto.getModeOfShipment())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getConfirmationInstruction()) && !Arrays.asList("Confirm", "May Add", "Without").contains(amendmentDto.getConfirmationInstruction())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getTransferable()) && !Arrays.asList("Yes", "No").contains(amendmentDto.getTransferable())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getStandByLC()) && !Arrays.asList("Yes", "No").contains(amendmentDto.getStandByLC())) {
            return false;
        }

        if (StringUtils.isNotBlank(amendmentDto.getTolerancePercentage())) {
            matcher = alphaNumericMax35CharsWithSapcePattern.matcher(amendmentDto.getTolerancePercentage());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getAmountType())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getAmountType());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getOtherAmendments())) {
            matcher = alphaNumericMax200CharsWithSpacePattern.matcher(amendmentDto.getOtherAmendments());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getAmendCharges())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getAmendCharges());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getAmendStatus())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getAmendStatus());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (StringUtils.isNotBlank(amendmentDto.getLcSRMSId())) {
            matcher = alphaNumericMax35CharsPattern.matcher(amendmentDto.getLcSRMSId());
            isValid = matcher.matches();
            if (!isValid)
                return false;
        }
        if (inputParams != null) {
            for (String key : inputParams.keySet()) {
                String value = inputParams.get(key);
                if ((validationMap.containsKey(key)) && value.length() > validationMap.get(key))
                    return false;

                matcher = specialCharUnacceptablePattern.matcher(value);
                if (matcher.find())
                    return false;
            }
        }

        return isValid;

    }

    public boolean _isDateValidate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            formatter.parse(date);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}