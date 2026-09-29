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
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ExportLetterOfCreditsDrawingsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.ExportLetterOfCreditsDrawingsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetExportLetterOfCreditsByIdBusinessDelegate;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.generateTradeFinanceFileID;

public class ExportLetterOfCreditsDrawingsResourceImpl
        implements ExportLetterOfCreditsDrawingsResource, TradeFinanceConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result createExportDrawing(ExportLCDrawingsDTO inputDTO, DataControllerRequest request) {
    	Result result = new Result();
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());
        GetExportLetterOfCreditsByIdBusinessDelegate requestExportLcBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GetExportLetterOfCreditsByIdBusinessDelegate.class);
        ExportLOCDTO exportLcDTO = requestExportLcBusinessDelegate.getExportLetterOfCreditById(inputDTO.getExportLCId(), request);
        if (StringUtils.isBlank(exportLcDTO.getExportLCId())) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result(), "Requested Export LC is not available");
        }
        if (!StringUtils.equals(exportLcDTO.getStatus(),PARAM_STATUS_APPROVED)){
        	  return ErrorCodeEnum.ERR_12001.setErrorCode(new Result(), "Letter of Credit is not eligible to create Drawing");
        }   
               
        String creditAccount = request.getParameter("creditAccount") != null ? request.getParameter("creditAccount")
                : null;
        String chargesDebitAccount = request.getParameter("chargesDebitAccount") != null
                ? request.getParameter("chargesDebitAccount")
                : null;

        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

        ExportLetterOfCreditsDrawingsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLetterOfCreditsDrawingsBusinessDelegate.class);
        String errorMessage, accountEnding = "";       
        if (StringUtils.isNotBlank(creditAccount)) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, creditAccount)) {
                if (creditAccount.length() > 3)
                    accountEnding = creditAccount.substring(creditAccount.length() - 3);
                errorMessage = "You do not have permission to the Charges Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }
        if (StringUtils.isNotBlank(chargesDebitAccount)) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, chargesDebitAccount)) {
                if (chargesDebitAccount.length() > 3)
                    accountEnding = chargesDebitAccount.substring(chargesDebitAccount.length() - 3);
                errorMessage = "You do not have permission to the Charges Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }

        }
        
        LetterOfCreditsDTO drawingdto = new LetterOfCreditsDTO();
        if (StringUtils.isNotBlank(inputDTO.getExportLCId())) {

            drawingdto.setLcReferenceNo(inputDTO.getExportLCId());
            drawingdto.setSrmsReqOrderID(inputDTO.getExportLCId());
        } else {
            return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
        }

        String previousStatus = "";
        // draft -> create flow
        if (StringUtils.isNotEmpty(inputDTO.getDrawingReferenceNo())) {
            drawingdto.setSrmsReqOrderID(inputDTO.getDrawingReferenceNo());
            try {
                ExportLCDrawingsDTO matchDto = businessDelegate.getExportLetterOfCreditDrawingById(request,
                        inputDTO.getDrawingReferenceNo());
                if (StringUtils.isNotBlank(matchDto.getDrawingReferenceNo())
                        && StringUtils.isNotBlank(matchDto.getStatus())) {
                    String status = matchDto.getStatus();
                    if (StringUtils.isNotBlank(status)) {
                        previousStatus = status;
                    }
                } else {
                    return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
                }
            } catch (Exception e) {
                alert.prepareError("Failed to verify Request ID of drawings request in OMS", e).log();
                return ErrorCodeEnum.ERRTF_29072.setErrorCode(new Result());
            }
        }

        ExportLCDrawingsDTO drawingsDTO = new ExportLCDrawingsDTO();
        inputDTO.setTotalAmount(inputDTO.getDrawingAmount());
        String status = inputDTO.getStatus();
        if (StringUtils.isNotBlank(inputDTO.getDrawingReferenceNo()) && StringUtils.isNotBlank(status)
                && (status.equalsIgnoreCase(PARAM_STATUS_DRAFT) || status.equalsIgnoreCase(PARAM_STATUS_NEW))
                && StringUtils.isNotBlank(previousStatus) && previousStatus.equalsIgnoreCase(PARAM_STATUS_DRAFT)) {

            if (status.equalsIgnoreCase(PARAM_STATUS_NEW)) {
                inputDTO.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
            }
            drawingsDTO = businessDelegate.updateExportLetterOfCreditDrawing(inputDTO, request);
        } else if (StringUtils.isBlank(inputDTO.getDrawingReferenceNo()) && StringUtils.isBlank(previousStatus)
                && StringUtils.isNotBlank(status)
                && (status.equalsIgnoreCase("New") || status.equalsIgnoreCase("Draft"))) {

            if (status.equalsIgnoreCase("New")) {
                //// Add Approval Matrix
                inputDTO.setStatus("Submitted to Bank");
            }
            drawingsDTO = businessDelegate.createExportDrawing(inputDTO, request);
        } else {
            drawingsDTO.setErrorMessage("Unknown Status");
            result.addParam("ErrorMsg", drawingsDTO.getErrorMessage());
            return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result(), drawingsDTO.getErrorMessage());
        }

        if (StringUtils.isNotBlank(drawingsDTO.getErrorMessage())) {
            result.addParam("ErrorMsg", drawingsDTO.getErrorMessage());
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), drawingsDTO.getErrorMessage());
        }

        if (StringUtils.isBlank(drawingsDTO.getDrawingSRMSRequestId())) {
            return ErrorCodeEnum.ERRTF_29052.setErrorCode(new Result());
        }
        JSONObject responseObj = new JSONObject(drawingsDTO);
        result = JSONToResult.convert(responseObj.toString());
        result.addParam("srmsReqOrderId", drawingsDTO.getDrawingSRMSRequestId());
        result.addParam("status", drawingsDTO.getStatus());
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.EXPORT_LC_DRAWING_SUBMITTED, drawingsDTO.getDrawingSRMSRequestId());
        return result;
    }

    public Result updateExportLCDrawing(ExportLCDrawingsDTO updatePayloadDTO, DataControllerRequest request) {
        Result result = new Result();
        String errorMessage;
        ExportLCDrawingsDTO exportDrawing;
        ExportLCDrawingsDTO updateDrawing;
        ExportLetterOfCreditsDrawingsBusinessDelegate exportBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLetterOfCreditsDrawingsBusinessDelegate.class);
        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId)) {
            alert.prepareError("Failed to fetch Customer ID").log();
            return ErrorCodeEnum.ERR_26014.setErrorCode(result);
        }

        if (StringUtils.isBlank(updatePayloadDTO.getStatus())
                || StringUtils.isBlank(updatePayloadDTO.getDrawingReferenceNo())
                || StringUtils.isBlank(updatePayloadDTO.getDiscrepencies())
                || StringUtils.isBlank(updatePayloadDTO.getCurrency())
                || StringUtils.isBlank(updatePayloadDTO.getDrawingAmount())
                || StringUtils.isBlank(updatePayloadDTO.getFinanceBill())
                || (StringUtils.isBlank(updatePayloadDTO.getCreditAccount())
                && StringUtils.isBlank(updatePayloadDTO.getExternalAccount()))
                || StringUtils.isBlank(updatePayloadDTO.getUploadedDocuments())
                || StringUtils.isBlank(updatePayloadDTO.getPhysicalDocuments())
                || StringUtils.isBlank(updatePayloadDTO.getTotalDocuments())
                || StringUtils.isBlank(updatePayloadDTO.getDocumentReference())
                || StringUtils.isBlank(updatePayloadDTO.getChargesDebitAccount())) {
            diagnostic.prepareDebug("Mandatory fields are missing.").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }
        if (StringUtils.isNotBlank(updatePayloadDTO.getCreditAccount())
                && StringUtils.isNotBlank(updatePayloadDTO.getExternalAccount())) {
            errorMessage = "Payload should have either creditAccount or externalAccount details.";
            return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
        }
        updatePayloadDTO.setDrawingSRMSRequestId(updatePayloadDTO.getDrawingReferenceNo());

        try {
            exportDrawing = exportBusinessDelegate.getExportLetterOfCreditDrawingById(request,
                    updatePayloadDTO.getDrawingSRMSRequestId());
            if (exportDrawing == null) {
                alert.prepareError("Error occurred while fetching export drawing details from backend").log();
                return ErrorCodeEnum.ERRTF_29062.setErrorCode(result);
            }
            if (StringUtils.isBlank(exportDrawing.getDrawingSRMSRequestId())) {
                diagnostic.prepareDebug("Requested drawing is not available in SRMS.").log();
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(result);
            }
        } catch (Exception e) {
            diagnostic.prepareDebug("Error occurred while fetching Export Drawing. " + e).log();
            return ErrorCodeEnum.ERRTF_29062.setErrorCode(result);
        }

        String chargesDebitAccount = StringUtils.isNotBlank(updatePayloadDTO.getChargesDebitAccount())
                ? updatePayloadDTO.getChargesDebitAccount()
                : null;

        if (exportDrawing.getStatus().equals(PARAM_STATUS_RETURNED_by_BANK)) {
            exportDrawing.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
            JSONObject discrepanciesHistoryObj = new JSONObject();
            discrepanciesHistoryObj.put("userName", exportDrawing.getApplicant());
            discrepanciesHistoryObj.put("returnedDate", exportDrawing.getReturnedDate());
            discrepanciesHistoryObj.put("totalDocuments", exportDrawing.getTotalDocuments());
            discrepanciesHistoryObj.put("uploadedDocuments", exportDrawing.getUploadedDocuments());
            discrepanciesHistoryObj.put("returnedDocuments", exportDrawing.getReturnedDocuments());
            discrepanciesHistoryObj.put("documentStatus", exportDrawing.getDocumentStatus());
            discrepanciesHistoryObj.put("messageToBank", exportDrawing.getMessageToBank());
            discrepanciesHistoryObj.put("messageFromBank", exportDrawing.getMessageFromBank());
            discrepanciesHistoryObj.put("reasonForReturn", exportDrawing.getReasonForReturn());
            discrepanciesHistoryObj.put("returnMessageToBank", updatePayloadDTO.getReturnMessageToBank());
            discrepanciesHistoryObj.put("discrepancies", updatePayloadDTO.getDiscrepencies());
            String discrepanciesHistory = discrepanciesHistoryObj.toString();
            if (StringUtils.isBlank(exportDrawing.getDiscrepanciesHistory1()))
                exportDrawing.setDiscrepanciesHistory1(discrepanciesHistory);
            else if (StringUtils.isBlank(exportDrawing.getDiscrepanciesHistory2()))
                exportDrawing.setDiscrepanciesHistory2(discrepanciesHistory);
            else if (StringUtils.isBlank(exportDrawing.getDiscrepanciesHistory3()))
                exportDrawing.setDiscrepanciesHistory3(discrepanciesHistory);
            else if (StringUtils.isBlank(exportDrawing.getDiscrepanciesHistory4()))
                exportDrawing.setDiscrepanciesHistory4(discrepanciesHistory);
            else if (StringUtils.isBlank(exportDrawing.getDiscrepanciesHistory5()))
                exportDrawing.setDiscrepanciesHistory5(discrepanciesHistory);
            else {
                errorMessage = "Maximum number of returns limit reached";
                diagnostic.prepareInfo(errorMessage).log();
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }

            exportDrawing.setCurrency(updatePayloadDTO.getCurrency());
            exportDrawing.setDrawingAmount(updatePayloadDTO.getDrawingAmount());
            exportDrawing.setFinanceBill(updatePayloadDTO.getFinanceBill());
            exportDrawing.setUploadedDocuments(updatePayloadDTO.getUploadedDocuments());
            exportDrawing.setPhysicalDocuments(updatePayloadDTO.getPhysicalDocuments());
            exportDrawing.setForwardDocuments(updatePayloadDTO.getForwardDocuments());
            if (!exportDrawing.getChargesDebitAccount().equals(chargesDebitAccount)) {
                if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, chargesDebitAccount)) {
                    String chargesDebitAccountEnding = chargesDebitAccount.length() > 3
                            ? chargesDebitAccount.substring(chargesDebitAccount.length() - 3)
                            : null;
                    errorMessage = "You do not have permission to the Charges Debit Account ending xxx"
                            + chargesDebitAccountEnding;
                    return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
                }
                exportDrawing.setChargesDebitAccount(updatePayloadDTO.getChargesDebitAccount());
            }
            if (StringUtils.isNotBlank(updatePayloadDTO.getMessageToBank()))
                exportDrawing.setMessageToBank(updatePayloadDTO.getMessageToBank());
            else
                exportDrawing.setMessageToBank(null);
            exportDrawing.setDiscrepencies(updatePayloadDTO.getDiscrepencies());
            if (StringUtils.isNotBlank(updatePayloadDTO.getReturnMessageToBank()))
                exportDrawing.setReturnMessageToBank(updatePayloadDTO.getReturnMessageToBank());
            else
                exportDrawing.setReturnMessageToBank(null);
            if (StringUtils.isNotBlank(updatePayloadDTO.getCreditAccount())) {
                String creditAccount = updatePayloadDTO.getCreditAccount();
                if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, creditAccount)) {
                    String creditAccountEnding = creditAccount.length() > 3
                            ? creditAccount.substring(creditAccount.length() - 3)
                            : null;
                    errorMessage = "You do not have permission to the Credit Account ending xxx" + creditAccountEnding;
                    return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
                }
                exportDrawing.setExternalAccount(null);
                exportDrawing.setCreditAccount(updatePayloadDTO.getCreditAccount());
            } else if (StringUtils.isNotBlank(updatePayloadDTO.getExternalAccount())) {
                exportDrawing.setCreditAccount(null);
                exportDrawing.setExternalAccount(updatePayloadDTO.getExternalAccount());
            }
        } else {
            diagnostic.prepareDebug("Requested drawing is already in the status " + exportDrawing.getStatus()).log();
            return ErrorCodeEnum.ERRTF_29073.setErrorCode(result);
        }

        try {
            updateDrawing = exportBusinessDelegate.updateExportLetterOfCreditDrawing(exportDrawing, request);
            if (updateDrawing == null) {
                alert.prepareError("Error occurred while creating export drawing details from backend").log();
                return ErrorCodeEnum.ERRTF_29066.setErrorCode(result);
            }
            if (StringUtils.isNotBlank(updateDrawing.getErrorMessage())) {
                result.addParam("ErrorMsg", updateDrawing.getErrorMessage());
                return ErrorCodeEnum.ERRTF_29073.setErrorCode(result, updateDrawing.getErrorMessage());
            }
            JSONObject responseObj = new JSONObject(updateDrawing);
            result = JSONToResult.convert(responseObj.toString());
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to update Export Drawing from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29066.setErrorCode(result);
        }
    }

    @Override
    public Result getExportLetterOfCreditDrawingById(DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        String drawingSRMSRequestId = request.getParameter("drawingReferenceNo");
        if (StringUtils.isBlank(drawingSRMSRequestId)) {
            alert.prepareError("Drawing reference number is not provided").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        ExportLCDrawingsDTO exportDrawing;
        Result result;
        ExportLetterOfCreditsDrawingsBusinessDelegate exportBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLetterOfCreditsDrawingsBusinessDelegate.class);
        try {
            exportDrawing = exportBusinessDelegate.getExportLetterOfCreditDrawingById(request, drawingSRMSRequestId);
            if (exportDrawing == null) {
                alert.prepareError("Error occurred while fetching export drawing details from backend").log();
                return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(exportDrawing.getLcReferenceNo())) {
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
            }

            JSONObject responseObj = new JSONObject(exportDrawing);
            result = JSONToResult.convert(responseObj.toString());
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to fetch Export Drawing from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
        }
    }

    @Override
    public Result getExportLetterOfCreditDrawings(Object[] inputArray, DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        FilterDTO filterDTO;
        List<ExportLCDrawingsDTO> exportDrawings;
        Result result;
        JSONObject drawingsResult = new JSONObject();
        ExportLetterOfCreditsDrawingsBusinessDelegate exportBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLetterOfCreditsDrawingsBusinessDelegate.class);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];
            filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
            exportDrawings = exportBusinessDelegate.getExportLetterOfCreditDrawings(request);

            if (exportDrawings == null
                    || (exportDrawings.size() > 0 && StringUtils.isNotBlank(exportDrawings.get(0).getErrorMessage()))) {
                alert.prepareError("Error occurred while fetching Export Drawings from backend").log();
            }

            if (request.containsKeyInRequest("exportLcId")) {
                List<ExportLCDrawingsDTO> exportDrawingsforExport = new ArrayList<ExportLCDrawingsDTO>();
                String exportLCid = request.getParameter("exportLcId");
                if (StringUtils.isNotBlank(exportLCid)) {
                    for (ExportLCDrawingsDTO drawing : exportDrawings) {
                        if (drawing.getExportLCId().equalsIgnoreCase(exportLCid)) {
                            exportDrawingsforExport.add(drawing);
                        }
                    }
                    if (exportDrawingsforExport.size() > 0) {
                        List<ExportLCDrawingsDTO> searchedDrawingList = new ArrayList<ExportLCDrawingsDTO>();

                        String searchString = request.getParameter("searchString") != null
                                ? request.getParameter("searchString").toString()
                                : null;

                        if (StringUtils.isNotBlank(searchString)) {
                            for (ExportLCDrawingsDTO ob : exportDrawingsforExport) {
                                StringBuilder values = new StringBuilder();
                                JSONObject jsonContent = new JSONObject(ob);
                                Iterator<String> jsonKey = jsonContent.keys();
                                while (jsonKey.hasNext()) {
                                    String key = jsonKey.next();
                                    values.append(jsonContent.get(key)).append("  ");
                                }
                                if (values.toString().toUpperCase().contains(searchString.toUpperCase())) {
                                    searchedDrawingList.add(ob);
                                }
                            }
                        } else {
                            searchedDrawingList = exportDrawingsforExport;
                        }
                        JSONArray drawingsList = new JSONArray(searchedDrawingList);
                        drawingsResult.put("ExportLCDrawings", drawingsList);
                        result = JSONToResult.convert(drawingsResult.toString());
                        return result;
                    }
                }
            }

            List<ExportLCDrawingsDTO> filteredDrawings = new ArrayList<ExportLCDrawingsDTO>();

            filteredDrawings = filterDTO.filter(exportDrawings);

            JSONArray drawingsList = new JSONArray(filteredDrawings);
            drawingsResult.put("ExportLCDrawings", drawingsList);
            result = JSONToResult.convert(drawingsResult.toString());
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to fetch Export Drawings from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
        }
    }

    public Result generateExportDrawingPdf(DataControllerRequest request) {
        Result result = new Result();
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        String drawingSRMSRequestId = request.getParameter("drawingReferenceNo");
        if (StringUtils.isBlank(drawingSRMSRequestId)) {
            alert.prepareError("SRMSID is not provided").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }

        try {
            byte[] pdfBytes = new byte[0];
            ExportLetterOfCreditsDrawingsBusinessDelegate exportBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(ExportLetterOfCreditsDrawingsBusinessDelegate.class);

            ExportLCDrawingsDTO drawingDetails = exportBusinessDelegate.getExportLetterOfCreditDrawingById(request,
                    drawingSRMSRequestId);
            if (drawingDetails == null) {
                alert.prepareError("Error occurred while fetching drawing details from backend").log();
                return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(drawingDetails.getDrawingReferenceNo())) {
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
            }

            pdfBytes = exportBusinessDelegate.generateExportDrawingPdf(drawingDetails, request);
            if (ArrayUtils.isEmpty(pdfBytes)) {
                alert.prepareError("Error while generating the trade finance file").log();
                return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
            }
            String fileId = generateTradeFinanceFileID(PREFIX_EXPORT_DRAWING);
            MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
            result.addParam("fileId", fileId);
            return result;
        } catch (Exception e) {
            alert.prepareError("Error while generating the trade finance file", e).log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(result);
        }
    }

    @Override
    public Result deleteExportLetterOfCreditDrawing(DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId)) {
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());
        }
        String drawingReferenceNo = request.getParameter("drawingReferenceNo");
        if (StringUtils.isBlank(drawingReferenceNo)) {
            alert.prepareError("Mandatory fields are missing").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        ExportLCDrawingsDTO exportDrawing = new ExportLCDrawingsDTO();
        ExportLetterOfCreditsDrawingsBusinessDelegate exportBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLetterOfCreditsDrawingsBusinessDelegate.class);
        try {
            exportDrawing = exportBusinessDelegate.getExportLetterOfCreditDrawingById(request, drawingReferenceNo);
            if (exportDrawing == null) {
                alert.prepareError("Error occurred while fetching export drawing details from backend").log();
                return ErrorCodeEnum.ERRTF_29062.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(exportDrawing.getLcReferenceNo())) {
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
            }
            exportDrawing.setStatus(PARAM_STATUS_DELETED);
            ExportLCDrawingsDTO exportDrawingsResult = exportBusinessDelegate
                    .updateExportLetterOfCreditDrawing(exportDrawing, request);
            if (StringUtils.isNotBlank(exportDrawingsResult.getErrorMessage())) {
                return ErrorCodeEnum.ERRTF_29074.setErrorCode(new Result(), exportDrawingsResult.getErrorMessage());
            }
            JSONObject returnResult = new JSONObject();
            returnResult.put("drawingReferenceNo", exportDrawingsResult.getDrawingReferenceNo());
            returnResult.put("status", exportDrawingsResult.getStatus());
            returnResult.put("message", "Successfully Deleted");
            Result result = JSONToResult.convert(returnResult.toString());
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to delete Export Drawing from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29074.setErrorCode(new Result());
        }
    }

    public Result updateDrawingByBank(ExportLCDrawingsDTO updatePayloadDTO, DataControllerRequest request) {
        Result result = new Result();
        AlertsEnum alertToPush = null;
        ExportLCDrawingsDTO exportDrawing;
        ExportLCDrawingsDTO updateDrawing;

        ExportLetterOfCreditsDrawingsBusinessDelegate exportBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLetterOfCreditsDrawingsBusinessDelegate.class);
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId)) {
            alert.prepareError("Failed to fetch Customer ID").log();
            return ErrorCodeEnum.ERR_26014.setErrorCode(result);
        }

        if (StringUtils.isBlank(updatePayloadDTO.getStatus())
                || StringUtils.isBlank(updatePayloadDTO.getDrawingSRMSRequestId())
                || StringUtils.isBlank(updatePayloadDTO.getDocumentStatus())
                || StringUtils.isBlank(updatePayloadDTO.getPaymentStatus())) {
            diagnostic.prepareDebug("Mandatory fields are missing.").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }

        try {
            exportDrawing = exportBusinessDelegate.getExportLetterOfCreditDrawingById(request,
                    updatePayloadDTO.getDrawingSRMSRequestId());
            if (exportDrawing == null) {
                alert.prepareError("Error occurred while fetching export drawing details from backend.").log();
                return ErrorCodeEnum.ERRTF_29062.setErrorCode(result);
            }
            if (StringUtils.isBlank(exportDrawing.getDrawingSRMSRequestId())) {
                diagnostic.prepareDebug("Requested drawing is not available in SRMS.").log();
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(result);
            }
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to fetch Export Drawing from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29062.setErrorCode(result);
        }

        if (exportDrawing.getStatus().equals(PARAM_STATUS_SETTLED)) {
            alert.prepareError("Requested drawing is already settled.").log();
            return ErrorCodeEnum.ERRTF_29067.setErrorCode(result);
        }

        exportDrawing.setStatus(updatePayloadDTO.getStatus());
        exportDrawing.setDocumentStatus(updatePayloadDTO.getDocumentStatus());
        exportDrawing.setPaymentStatus(updatePayloadDTO.getPaymentStatus());
        // optional fields
        exportDrawing.setMessageFromBank(updatePayloadDTO.getMessageFromBank());
        exportDrawing.setDiscrepenciesAcceptance(updatePayloadDTO.getDiscrepenciesAcceptance());

        if (StringUtils.isNotBlank(exportDrawing.getDiscrepencies())) {
            JSONArray jsonDiscrepancies = new JSONArray(exportDrawing.getDiscrepencies());
            exportDrawing.setDiscrepencies(jsonDiscrepancies.toString());
        }

        String updationDate = "";
        if (updatePayloadDTO.getPaymentStatus().equalsIgnoreCase(PARAM_STATUS_SETTLED)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = new Date();
            updationDate = formatter.format(date).toString();
        }

        if (StringUtils.isBlank(exportDrawing.getPaymentDate())
                && StringUtils.isNotBlank(updatePayloadDTO.getPaymentStatus())
                && updatePayloadDTO.getPaymentStatus().equals(PARAM_STATUS_SETTLED)) {
            if (StringUtils.isNotBlank(updationDate))
                exportDrawing.setPaymentDate(updationDate);
        }

        switch (updatePayloadDTO.getStatus()) {
            case PARAM_STATUS_SETTLED:
                exportDrawing.setReturnedDate(null);
                if (StringUtils.isNotBlank(updationDate))
                    exportDrawing.setApprovedDate(updationDate);
                exportDrawing.setReturnedDocuments(updatePayloadDTO.getReturnedDocuments());
                break;
            case PARAM_STATUS_RETURNED_by_BANK:
                if (StringUtils.isBlank(updatePayloadDTO.getReasonForReturn())
                        || StringUtils.isBlank(updatePayloadDTO.getReturnedDocuments())) {
                    diagnostic.prepareDebug("Mandatory fields are missing.").log();
                    return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
                }
                exportDrawing.setApprovedDate(null);
                exportDrawing.setReturnedDate(updationDate);
                exportDrawing.setReasonForReturn(updatePayloadDTO.getReasonForReturn());
                exportDrawing.setReturnedDocuments(updatePayloadDTO.getReturnedDocuments());

                JSONArray discrepancies = new JSONArray();
                JSONObject discrepancyResponse = new JSONObject();
                discrepancyResponse.put("userResponse", "");
                discrepancyResponse.put("userComment", "");

                String discrepancy1 = request.getParameter("discrepancy1");
                if (StringUtils.isNotBlank(discrepancy1)) {
                    JSONObject discrepancy = new JSONObject();
                    discrepancy.put(discrepancy1, discrepancyResponse);
                    discrepancies.put(discrepancy);
                }
                String discrepancy2 = request.getParameter("discrepancy2");
                if (StringUtils.isNotBlank(discrepancy2)) {
                    JSONObject discrepancy = new JSONObject();
                    discrepancy.put(discrepancy2, discrepancyResponse);
                    discrepancies.put(discrepancy);
                }
                exportDrawing.setDiscrepencies(discrepancies.toString());
                break;
            case PARAM_APPROVED: 
            	alertToPush = AlertsEnum.EXPORT_LC_DRAWING_APPROVED;
            	break;
            case PARAM_REJECTED:
            	alertToPush = AlertsEnum.EXPORT_LC_DRAWING_REJECTED;
            	break;
            case PARAM_STATUS_PROCESSING_by_BANK:            	
                break;
            default:
                alert.prepareError("Invalid status found in payload.").log();
                return ErrorCodeEnum.ERRTF_29068.setErrorCode(result);
        }

        try {
            updateDrawing = exportBusinessDelegate.updateExportLetterOfCreditDrawing(exportDrawing, request);
            if (updateDrawing == null) {
                alert.prepareError("Error occurred while updating export drawing details from backend").log();
                return ErrorCodeEnum.ERRTF_29066.setErrorCode(result);
            }
            if (StringUtils.isNotBlank(updateDrawing.getErrorMessage())) {
                result.addParam("ErrorMsg", updateDrawing.getErrorMessage());
                return ErrorCodeEnum.ERRTF_29066.setErrorCode(result, updateDrawing.getErrorMessage());
            }
            JSONObject responseObj = new JSONObject(updateDrawing);
            result = JSONToResult.convert(responseObj.toString());
            TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, updateDrawing.getDrawingSRMSRequestId());
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to update Export Drawing from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29066.setErrorCode(result);
        }
    }
}
