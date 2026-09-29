/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetLetterOfCreditsByIDBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.LetterOfCreditDrawingsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.LetterOfCreditDrawingsResource;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class LetterOfCreditDrawingsResourceImpl implements LetterOfCreditDrawingsResource, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result getImportDrawingDetailsById(Object[] inputArray, DataControllerRequest request) {

        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        String drawingsSrmsReqOrderID = request.getParameter("drawingsSrmsReqOrderID");
        if (StringUtils.isBlank(drawingsSrmsReqOrderID)) {
            alert.prepareError("SRMSID is not provided").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        try {
            LetterOfCreditDrawingsBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(LetterOfCreditDrawingsBusinessDelegate.class);
            DrawingsDTO drawingDetails = requestBusinessDelegate.getImportDrawingDetailsById(request,
                    drawingsSrmsReqOrderID);
            if (drawingDetails == null) {
                alert.prepareError("Error occurred while fetching drawing details from backend").log();
                return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(customerId) || !customerId.equalsIgnoreCase(drawingDetails.getCustomerId())) {
                return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(drawingDetails.getDrawingReferenceNo())) {
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
            }
            Result result;
            JSONObject responseObj = new JSONObject();
            List<DrawingsDTO> drawing = new ArrayList<>();
            drawing.add(drawingDetails);
            responseObj.put("Drawing", drawing);
            result = JSONToResult.convert(responseObj.toString());
            return result;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch drawing from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
        }
    }

    @SuppressWarnings("null")
    @Override
    public Result getImportDrawings(Object[] inputArray, DrawingsDTO drawingsDTO, DataControllerRequest request) {

        TradeFinanceCommonUtils tradeFinanceCommonUtils = new TradeFinanceCommonUtils();
        List<DrawingsDTO> drawings;
        Result result;

        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        try {
            FilterDTO filterDTO = null;
            @SuppressWarnings("unchecked")
            Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];

            try {
                filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
            } catch (IOException e) {
                alert.prepareError("Exception occurred while fetching params: ", e).log();
            }

            LetterOfCreditDrawingsBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(LetterOfCreditDrawingsBusinessDelegate.class);
            drawings = orderBusinessDelegate.getImportDrawings(drawingsDTO, request);

            if (drawings == null) {
                alert.prepareError("Error occurred while fetching Drawings from backend").log();
            }

            //filter
            List<DrawingsDTO> filteredList = drawings;
            if (StringUtils.isNotEmpty(filterDTO.get_filterByParam()) && StringUtils.isNotEmpty(filterDTO.get_filterByValue())) {
                filteredList = tradeFinanceCommonUtils.filterBy(drawings, filterDTO.get_filterByParam(), filterDTO.get_filterByValue());
                filterDTO.set_filterByParam("");
                filterDTO.set_filterByValue("");
            }

            filteredList = filterDTO.filter(filteredList);
            JSONArray DrawingsFiles = new JSONArray(filteredList);
            JSONObject responseObj = new JSONObject();
            responseObj.put("drawings", DrawingsFiles);
            result = JSONToResult.convert(responseObj.toString());

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch Drawings from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result generateImportDrawingPdf(DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        String drawingsSrmsReqOrderID = request.getParameter("drawingsSrmsReqOrderID");
        if (StringUtils.isBlank(drawingsSrmsReqOrderID)) {
            alert.prepareError("SRMSID is not provided").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        try {
            Result result = new Result();
            LetterOfCreditDrawingsBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(LetterOfCreditDrawingsBusinessDelegate.class);

            /* Getting the DTO for the specified srms id */
            DrawingsDTO drawingDetails = requestBusinessDelegate.getImportDrawingDetailsById(request,
                    drawingsSrmsReqOrderID);
            if (drawingDetails == null) {
                alert.prepareError("Error occurred while fetching drawing details from backend").log();
                return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(customerId) || !customerId.equalsIgnoreCase(drawingDetails.getCustomerId())) {
                return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(drawingDetails.getDrawingReferenceNo())) {
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
            }

            /* Generating file from bytes for the DTO */
            byte[] pdfBytes = requestBusinessDelegate.generateImportDrawingPdf(drawingDetails, request);
            if (ArrayUtils.isEmpty(pdfBytes)) {
                alert.prepareError("Error while generating the trade finance file").log();
                return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
            }

            String fileId = generateTradeFinanceFileID(PREFIX_IMPORT_DRAWING);
            MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
            result.addParam("fileId", fileId);
            return result;
        } catch (Exception e) {
            alert.prepareError("Error while generating the trade finance file", e).log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
        }
    }

    public Result createLetterOfCreditDrawings(DrawingsDTO drawings, DataControllerRequest request) {
        DrawingsDTO drawingsOrder;
        Result result;
        drawings.setStatus(PARAM_STATUS_NEW);
        drawings.setPaymentStatus(PARAM_STATUS_NEW);
        LetterOfCreditDrawingsBusinessDelegate createDrawingsBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(LetterOfCreditDrawingsBusinessDelegate.class);
        GetLetterOfCreditsByIDBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GetLetterOfCreditsByIDBusinessDelegate.class);

        if (StringUtils.isBlank(drawings.getLcSrmsReqOrderID()))
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result(), "Enter Letter Of Credit Order ID");
        // inputValidations
        boolean validate = _validate(drawings);
        if (!validate) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }

        // LetterOfCredits Part
        LetterOfCreditsDTO letterOfCredit;
        try {
            letterOfCredit = requestBusinessDelegate.getImportLCById(drawings.getLcSrmsReqOrderID(), request);
        } catch (ApplicationException e) {
            alert.prepareError("Create Order Failed" + e).log();
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }

        if (StringUtils.isNotBlank(letterOfCredit.getErrorMessage())) {
            alert.prepareError("get LOC Order Failed SRMS ErrCode :" + letterOfCredit.getErrorCode() + " ErrMessage : "
                    + letterOfCredit.getErrorMessage()).log();
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }

        _setLCValues(letterOfCredit, drawings);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date CurrentDate = new Date();
        String CurrDate = formatter.format(CurrentDate);
        drawings.setDrawingCreationDate(CurrDate);
        // Drawings Create flow
        if (StringUtils.isNotBlank(drawings.getDrawingReferenceNo())
                && StringUtils.isNotBlank(drawings.getBeneficiaryName())
                && StringUtils.isNotBlank(drawings.getDocumentStatus())
                && StringUtils.isNotBlank(drawings.getDrawingCurrency())
                && StringUtils.isNotBlank(drawings.getDrawingAmount())
                && StringUtils.isNotBlank(drawings.getPresentorReference())
                && StringUtils.isNotBlank(drawings.getPresentorName())
                && StringUtils.isNotBlank(drawings.getDocumentsReceived())
                && StringUtils.isNotBlank(drawings.getForwardContact())
                && StringUtils.isNotBlank(drawings.getShippingGuaranteeReference())
                && StringUtils.isNotBlank(drawings.getTotalDocuments())
                && StringUtils.isNotBlank(drawings.getDocumentName())
                && StringUtils.isNotBlank(drawings.getTotalAmountToBePaid())
                && StringUtils.isNotBlank(drawings.getMessageFromBank())
                && StringUtils.isNotBlank(drawings.getDiscrepancies())) {
            if (letterOfCredit.getStatus().equalsIgnoreCase(PARAM_STATUS_APPROVED)) {
                if (drawings.getStatus().equalsIgnoreCase(PARAM_STATUS_NEW)) {
                    drawings.setDrawingCreationDate(CurrDate);
                    try {
                        drawingsOrder = createDrawingsBusinessDelegate.createDrawings(drawings, request);
                    } catch (Exception e) {
                        alert.prepareError("Create Order Failed " + e).log();
                        return ErrorCodeEnum.ERRTF_29063.setErrorCode(new Result());
                    }
                } else {
                    return ErrorCodeEnum.ERRTF_29068.setErrorCode(new Result());
                }
                if (StringUtils.isNotBlank(drawings.getErrorMessage())) {
                    alert.prepareError("Create Drawings Order Failed SRMS ErrCode :" + drawings.getErrorCode() + " ErrMessage : "
                            + drawings.getErrorMessage()).log();
                    return ErrorCodeEnum.ERRTF_29063.setErrorCode(new Result(), drawings.getErrorMessage());
                }
            } else {
                return ErrorCodeEnum.ERRTF_29063.setErrorCode(new Result(), "Drawing Requested for the LetterOfCredits is not Approved");
            }
        } else {
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        result = JSONToResult.convert(new JSONObject(drawingsOrder).toString());
        return result;
    }

    @Override
    public Result submitLetterOfCreditDrawings(DrawingsDTO drawings, DataControllerRequest request) {
        DrawingsDTO drawingsOrder;
        Result result = new Result();
        DrawingsDTO updatedDrawings = new DrawingsDTO();

        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
        LetterOfCreditDrawingsBusinessDelegate drawingsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(LetterOfCreditDrawingsBusinessDelegate.class);

        //customer
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        //inputValidations
        if (drawings.getMessageToBank() != null && drawings.getMessageToBank().contains("\n"))
            drawings.setMessageToBank(drawings.getMessageToBank().replaceAll("\n", "||"));

        if (!_validate(drawings)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "Input Validation Failed");
        }

        //Check whether user has the permission to the account
        String accountToBeDebited = request.getParameter("accountToBeDebited") != null ? request.getParameter("accountToBeDebited") : "";
        String accountEnding = "";
        if (StringUtils.isNotBlank(accountToBeDebited)) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, accountToBeDebited)) {
                if (accountToBeDebited.length() > 3)
                    accountEnding = accountToBeDebited.substring(accountToBeDebited.length() - 3);
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, "You do not have permission to the Account ending xxx" + accountEnding + ".");
            }
        }

        if (StringUtils.isBlank(drawings.getDrawingsSrmsReqOrderID()))
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "Enter the Drawing Request OrderID");

        try {
            updatedDrawings = drawingsBusinessDelegate.getImportDrawingDetailsById(request,
                    drawings.getDrawingsSrmsReqOrderID());
        } catch (ApplicationException | com.temenos.infinity.api.commons.exception.ApplicationException e) {
            alert.prepareError("Error occurred while updating drawing update operation. " + e).log();
        }

        if (StringUtils.isBlank(updatedDrawings.getLcReferenceNo()))
            return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());

        if (!updatedDrawings.getStatus().equalsIgnoreCase(PARAM_STATUS_NEW))
            return ErrorCodeEnum.ERRTF_29067.setErrorCode(new Result(), "Requested Drawing status is in " + updatedDrawings.getPaymentStatus());
        if (StringUtils.isBlank(drawings.getAcceptance())
                || StringUtils.isBlank(drawings.getMessageToBank()))
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        if (drawings.getAcceptance().equalsIgnoreCase(PARAM_STATUS_REJECTED)) drawings.setAccountToBeDebited(null);
        else if (drawings.getAcceptance().equalsIgnoreCase(PARAM_STATUS_APPROVED) && StringUtils.isBlank(drawings.getAccountToBeDebited()))
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "Input Validation Failed. Account not Selected");
        else if (!(drawings.getAcceptance().equalsIgnoreCase(PARAM_STATUS_REJECTED) || drawings.getAcceptance().equalsIgnoreCase(PARAM_STATUS_APPROVED)))
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "Discrepancies acceptance should be either Approved or Rejected");
        updatedDrawings.setPaymentStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        updatedDrawings.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        updatedDrawings.setAccountToBeDebited(drawings.getAccountToBeDebited());
        updatedDrawings.setAcceptance(drawings.getAcceptance());
        updatedDrawings.setPaymentDate(null);
        updatedDrawings.setReasonForRejection(null);
        updatedDrawings.setMessageToBank(drawings.getMessageToBank());

        try {
            if (StringUtils.isNotBlank(updatedDrawings.getMessageToBank()) && updatedDrawings.getMessageToBank().contains("\n"))
                updatedDrawings.setMessageToBank(updatedDrawings.getMessageToBank().replaceAll("\n", "||"));
            drawingsOrder = drawingsBusinessDelegate.updateDrawings(updatedDrawings, request);
        } catch (Exception e) {
            alert.prepareError("update Order Failed in resources" + e).log();
            return ErrorCodeEnum.ERRTF_29063.setErrorCode(new Result());
        }
        if (StringUtils.isNotBlank(drawings.getErrorMessage())) {
            alert.prepareError("update Drawings Order Failed SRMS ErrCode :" + drawings.getErrorCode() + " ErrMessage : "
                    + drawings.getErrorMessage()).log();
            return ErrorCodeEnum.ERRTF_29066.setErrorCode(new Result(), drawings.getErrorMessage());
        }
        JSONObject responseObj = new JSONObject(drawingsOrder);
        result = JSONToResult.convert(responseObj.toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.IMPORT_LC_DRAWING_SUBMITTED, drawingsOrder.getDrawingsSrmsReqOrderID());
        return result;
    }

    private boolean updateUtilizedAmount(String letterOfCreditsId, String amount, DataControllerRequest request) {
        double dAmount = Double.parseDouble(amount);
        JSONObject Response = new JSONObject();
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("serviceRequestIds", letterOfCreditsId);

        String response = null;
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETSERVICEREQUESTBYID.getServiceName())
                    .withOperationId(
                            TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETSERVICEREQUESTBYID.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(getHeadersMap(request)).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            request.addRequestParam_("isSrmsFailed", "true");
            alert.prepareError("Unable to GET SRMS ID " + e).log();
        }
        boolean isSrmsFailed = false;
        if (StringUtils.isNotBlank(response)) {
            Response = new JSONObject(response);
            diagnostic.prepareInfo("OMS Response " + response).log();
            if (Response.has("dbpErrMsg") || Response.has("dbpErrCode")) {
                alert.prepareError("Something went wrong while updating utilized amount" + Response.get("dbpErrMsg")
                        + "  " + Response.get("dbpErrCode")).log();
                isSrmsFailed = true;
            }
        } else {
            alert.prepareError("Failed to fetch Letter of credit by id").log();
        }
        if (!isSrmsFailed) {
            try {
                JSONArray Orders = Response.getJSONArray("serviceReqs");
                JSONObject serviceResponse = Orders.getJSONObject(0);
                if (serviceResponse.has("serviceReqId")) {
                    if (serviceResponse.has("serviceReqRequestIn")) {
                        JSONObject inputPayload = serviceResponse.getJSONObject("serviceReqRequestIn");
                        double previousUtilizedAmount = inputPayload.has("utilizedAmount")
                                ? Double.parseDouble((String) inputPayload.get("utilizedAmount")) : 0;
                        inputPayload.put("utilizedAmount", String.valueOf(dAmount + previousUtilizedAmount));
                        inputMap.put("serviceRequestId", letterOfCreditsId);
                        String requestBody = inputPayload.toString().replaceAll("\"", "'");
                        inputMap.put("requestBody", requestBody);


                        String updateResponse = null;
                        JSONObject Response1;
                        try {
                            updateResponse = DBPServiceExecutorBuilder.builder()
                                    .withServiceId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_UPDATEORDER.getServiceName())
                                    .withOperationId(
                                            TradeFinanceAPIServices.SERVICEREQUESTJAVA_UPDATEORDER.getOperationName())
                                    .withRequestParameters(inputMap).withRequestHeaders(getHeadersMap(request))
                                    .withDataControllerRequest(request).build().getResponse();
                        } catch (Exception e) {
                            request.addRequestParam_("isSrmsFailed", "true");
                            alert.prepareError("Unable to update Drawings request order " + e).log();
                            isSrmsFailed = true;
                        }
                        if (StringUtils.isNotBlank(updateResponse)) {
                            Response1 = new JSONObject(updateResponse);
                            diagnostic.prepareInfo("OMS Response " + Response).log();
                            if (Response1.has("dbpErrMsg") || Response1.has("dbpErrCode")) {
                                alert.prepareError("Something went wrong while updating utilized amount" + Response1.get("dbpErrMsg")
                                        + "  " + Response1.get("dbpErrCode")).log();
                                isSrmsFailed = true;
                            }
                        }
                    }
                } else {
                    diagnostic.prepareInfo("OMS Response " + response).log();
                }
            } catch (Exception e) {
                diagnostic.prepareInfo("OMS Response " + response).log();
                alert.prepareError("Unable to GET SRMS ID " + e).log();
                isSrmsFailed = true;
            }
        }
        return !isSrmsFailed;
    }

    private void _setLCValues(LetterOfCreditsDTO letterOfCredits, DrawingsDTO drawings) {
        drawings.setLcAmount(Double.toString(letterOfCredits.getLcAmount()));
        drawings.setLcCurrency(letterOfCredits.getLcCurrency());
        drawings.setLcExpiryDate(letterOfCredits.getExpiryDate());
        drawings.setLcIssueDate(letterOfCredits.getIssueDate());
        drawings.setLcReferenceNo(letterOfCredits.getLcReferenceNo());
        drawings.setLcType(letterOfCredits.getPaymentTerms());
    }

    private boolean _validate(DrawingsDTO drawings) {
        String alphaNumericMax35Chars = "^[a-zA-Z0-9]{0,35}$";
        String NumericMax35Chars = "^[0-9]{0,35}$";
        String alphaNumericMax35CharsWithSapceSymbols = "^[a-zA-Z0-9 \\.<>?:\\/\\'\\-+=,;()\\\\ \\|]{0,35}$";
        String alphaNumericMax50CharsWithSapceSymbols = "^[a-zA-Z0-9 \\.<>?:\\/\\'\\-+=,;()\\\\ \\|]{0,50}$";
        Matcher matcher;
        boolean isValid = false;
        Pattern NumericMax35CharsPattern = Pattern.compile(NumericMax35Chars);
        Pattern alphaNumericMax35CharsPattern = Pattern.compile(alphaNumericMax35Chars);
        Pattern alphaNumericMax50CharsWithSpaceSymbolsPattern = Pattern.compile(alphaNumericMax50CharsWithSapceSymbols);
        Pattern alphaNumericMax35CharsWithSpaceSymbolsPattern = Pattern.compile(alphaNumericMax35CharsWithSapceSymbols);

        if (drawings.getAccountToBeDebited() != null && drawings.getAccountToBeDebited().trim().length() != 0) {
            matcher = alphaNumericMax35CharsPattern.matcher(drawings.getAccountToBeDebited());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getMessageFromBank() != null && drawings.getMessageFromBank().trim().length() != 0) {
            matcher = alphaNumericMax50CharsWithSpaceSymbolsPattern.matcher(drawings.getMessageFromBank());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getLcReferenceNo() != null && drawings.getLcReferenceNo().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getLcReferenceNo());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getLcType() != null && drawings.getLcType().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getLcType());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getDrawingReferenceNo() != null && drawings.getDrawingReferenceNo().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getDrawingReferenceNo());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getBeneficiaryName() != null && drawings.getBeneficiaryName().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getBeneficiaryName());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getDocumentStatus() != null && drawings.getDocumentStatus().trim().length() != 0) {
            if ((!(drawings.getDocumentStatus().equalsIgnoreCase("Discrepent") || drawings.getDocumentStatus().equalsIgnoreCase("Clean"))) && (drawings.getDocumentStatus().equalsIgnoreCase("Discrepent") && StringUtils.isBlank(drawings.getDiscrepancies())))
                return false;
            if (isValid == false)
                return false;
        }
        if (drawings.getDrawingCurrency() != null && drawings.getDrawingCurrency().trim().length() != 0) {
            matcher = alphaNumericMax35CharsPattern.matcher(drawings.getDrawingCurrency());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getDrawingAmount() != null && drawings.getDrawingAmount().trim().length() != 0) {
            isValid = drawings.getDrawingAmount().matches("([0-9]{0,35}).([0-9]{0,2})") || drawings.getDrawingAmount().matches("([0-9]{35})");
            if (isValid == false)
                return false;
        }
        if (drawings.getLcAmount() != null && drawings.getLcAmount().trim().length() != 0) {
            matcher = NumericMax35CharsPattern.matcher(drawings.getLcAmount());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getLcCurrency() != null && drawings.getLcCurrency().trim().length() != 0) {
            matcher = alphaNumericMax35CharsPattern.matcher(drawings.getLcCurrency());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getPaymentTerms() != null && drawings.getPaymentTerms().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getPaymentTerms());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getPresentorReference() != null && drawings.getPresentorReference().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getPresentorReference());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getPresentorName() != null && drawings.getPresentorName().trim().length() != 0) {
            matcher = alphaNumericMax50CharsWithSpaceSymbolsPattern.matcher(drawings.getPresentorName());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }

        if (drawings.getDocumentsReceived() != null && drawings.getDocumentsReceived().trim().length() != 0) {
            if (!(drawings.getDocumentsReceived().equalsIgnoreCase("yes") || drawings.getDocumentsReceived().equalsIgnoreCase("no")))
                return false;
        }
        if (drawings.getForwardContact() != null && drawings.getForwardContact().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getForwardContact());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getShippingGuaranteeReference() != null && drawings.getShippingGuaranteeReference().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getShippingGuaranteeReference());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getApprovalDate() != null && drawings.getApprovalDate().trim().length() != 0) {
            matcher = alphaNumericMax50CharsWithSpaceSymbolsPattern.matcher(drawings.getApprovalDate());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getTotalDocuments() != null && drawings.getTotalDocuments().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getTotalDocuments());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getDocumentName() != null && drawings.getDocumentName().trim().length() != 0) {
            String[] documents = drawings.getDocumentName().split("\\|\\|");
            if (!(documents.length == Integer.parseInt(drawings.getTotalDocuments()))) return false;
            for (int i = 0; i < Integer.parseInt(drawings.getTotalDocuments()); i++) {
                if (StringUtils.isNotBlank(documents[i])) {
                    matcher = alphaNumericMax50CharsWithSpaceSymbolsPattern.matcher(documents[i].trim());
                    isValid = matcher.matches();
                    if (isValid == false)
                        return false;
                }
            }
        }
        if (drawings.getDiscrepancyDescription() != null && drawings.getDiscrepancyDescription().trim().length() != 0) {
            matcher = alphaNumericMax50CharsWithSpaceSymbolsPattern.matcher(drawings.getDiscrepancyDescription());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getPaymentStatus() != null && drawings.getPaymentStatus().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getPaymentStatus());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getTotalAmountToBePaid() != null && drawings.getTotalAmountToBePaid().trim().length() != 0) {
            isValid = drawings.getTotalAmountToBePaid().matches("([0-9]{0,35}).([0-9]{0,2})") || drawings.getTotalAmountToBePaid().matches("([0-9]{0,35})");
            ;
            if (isValid == false)
                return false;
        }
        if (drawings.getAccountToBeDebited() != null && drawings.getAccountToBeDebited().trim().length() != 0) {
            matcher = alphaNumericMax50CharsWithSpaceSymbolsPattern.matcher(drawings.getAccountToBeDebited());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getMessageFromBank() != null && drawings.getMessageFromBank().trim().length() != 0) {
            matcher = alphaNumericMax50CharsWithSpaceSymbolsPattern.matcher(drawings.getMessageFromBank());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getMessageToBank() != null && drawings.getMessageToBank().trim().length() != 0) {
            matcher = alphaNumericMax50CharsWithSpaceSymbolsPattern.matcher(drawings.getMessageToBank());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getTotalPaidAmount() != null && drawings.getTotalPaidAmount().trim().length() != 0) {
            isValid = drawings.getTotalPaidAmount().matches("([0-9]{0,35}).([0-9]{0,2})") || drawings.getTotalPaidAmount().matches("([0-9]{0,35})");
            ;
            if (isValid == false)
                return false;
        }
        if (drawings.getReasonForRejection() != null && drawings.getReasonForRejection().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getReasonForRejection());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (drawings.getDiscrepancies() != null && drawings.getDiscrepancies().trim().length() != 0) {
            String[] discrepancies = drawings.getDiscrepancies().split("\\|\\|");
            for (int i = 0; i < discrepancies.length; i++) {
                matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(discrepancies[i].trim());
                isValid = matcher.matches();
                if (isValid == false)
                    return false;
            }
        }
        if (drawings.getAcceptance() != null && drawings.getAcceptance().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpaceSymbolsPattern.matcher(drawings.getAcceptance());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        return true;
    }

    @Override
    public Result updateImportLCDrawingByBank(DrawingsDTO drawings, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        Result result;
        try {
            LetterOfCreditDrawingsBusinessDelegate drawingsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(LetterOfCreditDrawingsBusinessDelegate.class);
            DrawingsDTO drawingsDTO = drawingsBusinessDelegate.getImportDrawingDetailsById(request, drawings.getDrawingsSrmsReqOrderID());
            switch (drawings.getStatus()) {
                case PARAM_STATUS_REJECTED:
                    drawingsDTO.setStatus(drawings.getStatus());
                    drawingsDTO.setRejectedDate(getCurrentDateTimeUTF());
                    drawingsDTO.setReasonForRejection(drawings.getReasonForRejection());
                    drawingsDTO.setPaymentStatus(PARAM_STATUS_REJECTED);
                    drawingsDTO.setPaymentDate(null);
                    alertToPush = AlertsEnum.IMPORT_LC_DRAWING_REJECTED;
                    break;
                case PARAM_STATUS_SETTLED:
                    drawingsDTO.setRejectedDate(null);
                    drawingsDTO.setReasonForRejection(null);
                    drawingsDTO.setPaymentStatus(PARAM_STATUS_SETTLED);
                    drawingsDTO.setPaymentDate(getCurrentDateTimeUTF());
                    alertToPush = AlertsEnum.IMPORT_LC_DRAWING_APPROVED;
                    break;
                case PARAM_STATUS_PROCESSING_by_BANK:
                    break;
                default:
                    return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
            }

            drawingsDTO.setStatus(drawings.getStatus());
            DrawingsDTO updatedDrawingDTO = drawingsBusinessDelegate.updateDrawings(drawingsDTO, request);
            if (StringUtils.equals(updatedDrawingDTO.getStatus(), PARAM_STATUS_SETTLED)
                    && updateUtilizedAmount(drawingsDTO.getLcSrmsReqOrderID(), drawingsDTO.getDrawingAmount(), request)) {
                diagnostic.prepareInfo("Utilized amount successfully updated").log();
            } else {
                alert.prepareError("Failed to update utilized amount").log();
            }
            result = JSONToResult.convert((new JSONObject(updatedDrawingDTO)).toString());
            result.addParam("message", "Record updated Successfully");
            TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, updatedDrawingDTO.getDrawingsSrmsReqOrderID());
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to Update letter of credit drawings by Bank from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
        }
        return result;
    }

}
