/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditDrawingsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class LetterOfCreditDrawingsBackendDelegateImpl implements LetterOfCreditDrawingsBackendDelegate, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public DrawingsDTO getImportDrawingDetailsById(DataControllerRequest request, String srmsRequestOrderId) {
        DrawingsDTO drawingDto = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                drawingDto = (DrawingsDTO) invoke().addDTO(DrawingsDTO.class).addServiceRequestId(srmsRequestOrderId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                drawingDto = (DrawingsDTO) getInstance().addDTO(DrawingsDTO.class).filterByRecordId(srmsRequestOrderId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            drawingDto.setDrawingsSrmsReqOrderID(srmsRequestOrderId);
            // setDrawingsData(drawingDetailsResponse)
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching drawing", e).log();
            request.addRequestParam_("isSrmsFailed", "true");
        }
        return drawingDto;
    }

    public List<DrawingsDTO> getImportDrawingsFromSRMS(DrawingsDTO drawingsDTO, DataControllerRequest request) throws ApplicationException {
        List drawingsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                drawingsList = invoke().addDTO(DrawingsDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("LetterOfCreditsDrawingsType", "LetterOfCreditsDrawingsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                drawingsList = getInstance().addDTO(DrawingsDTO.class).addDataControllerRequest(request)
                        .addModule("LetterOfCreditsDrawingsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (Exception e) {
            // ERRTF_29061
            alert.prepareError("Error occurred while fetching drawings", e).log();
        }
        return drawingsList;
    }

    public DrawingsDTO setDrawingsData(JSONObject drawingDetailsResponse) {
        DrawingsDTO drawingDetails = new DrawingsDTO();
        JSONObject payload = drawingDetailsResponse.getJSONObject(PARAM_INPUT_PAYLOAD);
        String lcReferenceNo = payload.has(PARAM_LC_REFERENCE_NO)
                ? payload.getString(TradeFinanceConstants.PARAM_LC_REFERENCE_NO)
                : "";
        String lcType = payload.has(PARAM_LCTYPE) ? payload.getString(TradeFinanceConstants.PARAM_LCTYPE) : "";
        String drawingReferenceNo = payload.has(PARAM_DRAWING_REFERENCENO)
                ? payload.getString(TradeFinanceConstants.PARAM_DRAWING_REFERENCENO)
                : "";
        String beneficiaryName = payload.has(PARAM_BENEFICIARYNAME)
                ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYNAME)
                : "";
        String documentStatus = payload.has(PARAM_DOCUMENTSTATUS)
                ? payload.getString(TradeFinanceConstants.PARAM_DOCUMENTSTATUS)
                : "";
        String drawingCreationDate = payload.has(PARAM_DRAWING_CREATIONDATE)
                ? payload.getString(TradeFinanceConstants.PARAM_DRAWING_CREATIONDATE)
                : "";
        String drawingCurrency = payload.has(PARAM_DRAWING_CURRENCY)
                ? payload.getString(TradeFinanceConstants.PARAM_DRAWING_CURRENCY)
                : "";
        String drawingAmount = payload.has(PARAM_DRAWING_AMOUNT)
                ? payload.getString(TradeFinanceConstants.PARAM_DRAWING_AMOUNT)
                : "";
        String drawingStatus = payload.has(PARAM_DRAWING_STATUS)
                ? payload.getString(TradeFinanceConstants.PARAM_DRAWING_STATUS)
                : "";
        String lcAmount = payload.has(PARAM_LC_AMOUNT) ? payload.getString(TradeFinanceConstants.PARAM_LC_AMOUNT) : "";
        String lcCurrency = payload.has(PARAM_LC_CURRENCY) ? payload.getString(TradeFinanceConstants.PARAM_LC_CURRENCY)
                : "";
        String lcIssueDate = payload.has(PARAM_LC_ISSUEDATE)
                ? payload.getString(TradeFinanceConstants.PARAM_LC_ISSUEDATE)
                : "";
        String lcExpiryDate = payload.has(PARAM_LC_EXPIRYDATE)
                ? payload.getString(TradeFinanceConstants.PARAM_LC_EXPIRYDATE)
                : "";
        String paymentTerms = payload.has(PARAM_PAYMENTTERMS)
                ? payload.getString(TradeFinanceConstants.PARAM_PAYMENTTERMS)
                : "";
        String presentorReference = payload.has(PARAM_PRESENTORREFERENCE)
                ? payload.getString(TradeFinanceConstants.PARAM_PRESENTORREFERENCE)
                : "";
        String presentorName = payload.has(PARAM_PRESENTORNAME)
                ? payload.getString(TradeFinanceConstants.PARAM_PRESENTORNAME)
                : "";
        String documentsReceived = payload.has(PARAM_DOCUMENTS_RECEIVED)
                ? payload.getString(TradeFinanceConstants.PARAM_DOCUMENTS_RECEIVED)
                : "";
        String forwardContact = payload.has(PARAM_FORWARDCONTACT)
                ? payload.getString(TradeFinanceConstants.PARAM_FORWARDCONTACT)
                : "";
        String shippingGuaranteeReference = payload.has(PARAM_SHIPPING_GUARANTEE_REFERENCE)
                ? payload.getString(TradeFinanceConstants.PARAM_SHIPPING_GUARANTEE_REFERENCE)
                : "";
        String approvalDate = payload.has(PARAM_APPROVALDATE)
                ? payload.getString(TradeFinanceConstants.PARAM_APPROVALDATE)
                : "";
        String totalDocuments = payload.has(PARAM_TOTALDOCUMENTS)
                ? payload.getString(TradeFinanceConstants.PARAM_TOTALDOCUMENTS)
                : "";
        String documentName = payload.has(PARAM_DOCUMENTNAME)
                ? payload.getString(TradeFinanceConstants.PARAM_DOCUMENTNAME)
                : "";
        String discrepancyDescription = payload.has(PARAM_DISCREPANCY_DESCRIPTION)
                ? payload.getString(TradeFinanceConstants.PARAM_DISCREPANCY_DESCRIPTION)
                : "";
        String paymentStatus = payload.has(PARAM_PAYMENTSTATUS)
                ? payload.getString(TradeFinanceConstants.PARAM_PAYMENTSTATUS)
                : "";
        String rejectedDate = payload.has(PARAM_REJECTEDDATE)
                ? payload.getString(TradeFinanceConstants.PARAM_REJECTEDDATE)
                : "";
        String totalAmountToBePaid = payload.has(PARAM_TOTALAMOUNT_TO_BE_PAID)
                ? payload.getString(TradeFinanceConstants.PARAM_TOTALAMOUNT_TO_BE_PAID)
                : "";
        String accountToBeDebited = payload.has(PARAM_ACCOUNT_TO_BE_DEBITED)
                ? payload.getString(TradeFinanceConstants.PARAM_ACCOUNT_TO_BE_DEBITED)
                : "";
        String messageFromBank = payload.has(PARAM_MESSAGEFROMBANK)
                ? payload.getString(TradeFinanceConstants.PARAM_MESSAGEFROMBANK)
                : "";
        String messageToBank = payload.has(PARAM_MESSAGETOBANK)
                ? payload.getString(TradeFinanceConstants.PARAM_MESSAGETOBANK)
                : "";
        String totalPaidAmount = payload.has(PARAM_TOTAL_PAID_AMOUNT)
                ? payload.getString(TradeFinanceConstants.PARAM_TOTAL_PAID_AMOUNT)
                : "";
        String paymentDate = payload.has(PARAM_PAYMENTDATE) ? payload.getString(TradeFinanceConstants.PARAM_PAYMENTDATE)
                : "";
        String reasonForRejection = payload.has(PARAM_REASON_FOR_REJECTION)
                ? payload.getString(TradeFinanceConstants.PARAM_REASON_FOR_REJECTION)
                : "";
        String discrepancies = payload.has(PARAM_DISCREPANCIES)
                ? payload.getString(TradeFinanceConstants.PARAM_DISCREPANCIES)
                : "";
        String acceptance = payload.has(PARAM_ACCEPTANCE) ? payload.getString(TradeFinanceConstants.PARAM_ACCEPTANCE)
                : "";
        String messageType = payload.has(PARAM_MESSAGETYPE) ? payload.getString(TradeFinanceConstants.PARAM_MESSAGETYPE)
                : "";
        String deliveryDestination = payload.has(PARAM_DELIVERYDESTINATION)
                ? payload.getString(TradeFinanceConstants.PARAM_DELIVERYDESTINATION)
                : "";
        String messageDate = payload.has(PARAM_MESSAGEDATE) ? payload.getString(TradeFinanceConstants.PARAM_MESSAGEDATE)
                : "";
        String messageCategory = payload.has(PARAM_MESSAGECATEGORY)
                ? payload.getString(TradeFinanceConstants.PARAM_MESSAGECATEGORY)
                : "";
        String lcSrmsReqOrderID = payload.has(PARAM_LCSRMSREQORDERID)
                ? payload.getString(TradeFinanceConstants.PARAM_LCSRMSREQORDERID)
                : "";
        String status = payload.has(PARAM_ORDER_STATUS) ? payload.getString(TradeFinanceConstants.PARAM_ORDER_STATUS)
                : "";
        if (StringUtils.isNotBlank(lcReferenceNo)) {
            drawingDetails.setLcReferenceNo(lcReferenceNo);
        }
        if (StringUtils.isNotBlank(lcType)) {
            drawingDetails.setLcType(lcType);
        }
        if (StringUtils.isNotBlank(drawingReferenceNo)) {
            drawingDetails.setDrawingReferenceNo(drawingReferenceNo);
        }
        if (StringUtils.isNotBlank(beneficiaryName)) {
            drawingDetails.setBeneficiaryName(beneficiaryName);
        }
        if (StringUtils.isNotBlank(documentStatus)) {
            drawingDetails.setDocumentStatus(documentStatus);
        }
        if (StringUtils.isNotBlank(drawingCreationDate)) {
            drawingDetails.setDrawingCreationDate(drawingCreationDate);
        }
        if (StringUtils.isNotBlank(drawingCurrency)) {
            drawingDetails.setDrawingCurrency(drawingCurrency);
        }
        if (StringUtils.isNotBlank(drawingAmount)) {
            drawingDetails.setDrawingAmount(drawingAmount);
        }
        if (StringUtils.isNotBlank(drawingStatus)) {
            drawingDetails.setDrawingStatus(drawingStatus);
        }
        if (StringUtils.isNotBlank(lcAmount)) {
            drawingDetails.setLcAmount(lcAmount);
        }
        if (StringUtils.isNotBlank(lcCurrency)) {
            drawingDetails.setLcCurrency(lcCurrency);
        }
        if (StringUtils.isNotBlank(lcIssueDate)) {
            drawingDetails.setLcIssueDate(lcIssueDate);
        }
        if (StringUtils.isNotBlank(lcExpiryDate)) {
            drawingDetails.setLcExpiryDate(lcExpiryDate);
        }
        if (StringUtils.isNotBlank(paymentTerms)) {
            drawingDetails.setPaymentTerms(paymentTerms);
        }
        if (StringUtils.isNotBlank(presentorReference)) {
            drawingDetails.setPresentorReference(presentorReference);
        }
        if (StringUtils.isNotBlank(presentorName)) {
            drawingDetails.setPresentorName(presentorName);
        }
        if (StringUtils.isNotBlank(documentsReceived)) {
            drawingDetails.setDocumentsReceived(documentsReceived);
        }
        if (StringUtils.isNotBlank(forwardContact)) {
            drawingDetails.setForwardContact(forwardContact);
        }
        if (StringUtils.isNotBlank(shippingGuaranteeReference)) {
            drawingDetails.setShippingGuaranteeReference(shippingGuaranteeReference);
        }
        if (StringUtils.isNotBlank(approvalDate)) {
            drawingDetails.setApprovalDate(approvalDate);
        }
        if (StringUtils.isNotBlank(totalDocuments)) {
            drawingDetails.setTotalDocuments(totalDocuments);
        }
        if (StringUtils.isNotBlank(documentName)) {
            drawingDetails.setDocumentName(documentName);
        }
        if (StringUtils.isNotBlank(discrepancyDescription)) {
            drawingDetails.setDiscrepancyDescription(discrepancyDescription);
        }
        if (StringUtils.isNotBlank(paymentStatus)) {
            drawingDetails.setPaymentStatus(paymentStatus);
        }
        if (StringUtils.isNotBlank(rejectedDate)) {
            drawingDetails.setRejectedDate(rejectedDate);
        }
        if (StringUtils.isNotBlank(totalAmountToBePaid)) {
            drawingDetails.setTotalAmountToBePaid(totalAmountToBePaid);
        }
        if (StringUtils.isNotBlank(accountToBeDebited)) {
            drawingDetails.setAccountToBeDebited(accountToBeDebited);
        }
        if (StringUtils.isNotBlank(messageFromBank)) {
            drawingDetails.setMessageFromBank(messageFromBank);
        }
        if (StringUtils.isNotBlank(messageToBank)) {
            if (messageToBank.contains("||")) messageToBank = messageToBank.replaceAll("\\|\\|", "\n");
            drawingDetails.setMessageToBank(messageToBank);
        }
        if (StringUtils.isNotBlank(totalPaidAmount)) {
            drawingDetails.setTotalPaidAmount(totalPaidAmount);
        }
        if (StringUtils.isNotBlank(paymentDate)) {
            drawingDetails.setPaymentDate(paymentDate);
        }
        if (StringUtils.isNotBlank(reasonForRejection)) {
            drawingDetails.setReasonForRejection(reasonForRejection);
        }
        if (StringUtils.isNotBlank(discrepancies)) {
            drawingDetails.setDiscrepancies(discrepancies);
        }
        if (StringUtils.isNotBlank(acceptance)) {
            drawingDetails.setAcceptance(acceptance);
        }
        if (StringUtils.isNotBlank(messageType)) {
            drawingDetails.setMessageType(messageType);
        }
        if (StringUtils.isNotBlank(deliveryDestination)) {
            drawingDetails.setDeliveryDestination(deliveryDestination);
        }
        if (StringUtils.isNotBlank(messageDate)) {
            drawingDetails.setMessageDate(messageDate);
        }
        if (StringUtils.isNotBlank(messageCategory)) {
            drawingDetails.setMessageCategory(messageCategory);
        }
        if (StringUtils.isNotBlank(lcSrmsReqOrderID)) {
            drawingDetails.setLcSrmsReqOrderID(lcSrmsReqOrderID);
        }
        if (StringUtils.isNotBlank(status)) {
            drawingDetails.setStatus(status);
        } else {
            drawingDetails.setStatus("");
        }
        if (drawingDetailsResponse.has(PARAM_REQUEST_DATE_TIME)
                && StringUtils.isNotBlank(drawingDetailsResponse.getString(PARAM_REQUEST_DATE_TIME)))
            drawingDetails.setDrawingCreationDate(drawingDetailsResponse.getString(PARAM_REQUEST_DATE_TIME));
        if (drawingDetailsResponse.has(PARAM_SERVICE_REQ_ID)
                && StringUtils.isNotBlank(drawingDetailsResponse.getString(PARAM_SERVICE_REQ_ID)))
            drawingDetails.setDrawingsSrmsReqOrderID(drawingDetailsResponse.getString(PARAM_SERVICE_REQ_ID));
        if (drawingDetailsResponse.has("partyId") && StringUtils.isNotBlank(drawingDetailsResponse.getString("partyId"))) {
            drawingDetails.setCustomerId(drawingDetailsResponse.getString("partyId"));
        }
        return drawingDetails;
    }

    @Override
    public DrawingsDTO createDrawingsOrder(DrawingsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructRequestPayload(inputDto);
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("LetterOfCreditsDrawingsType", "LetterOfCreditsDrawingsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("LetterOfCreditsDrawingsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setDrawingsSrmsReqOrderID(responseObject.get(PARAM_UNIQUE_ID).toString());
            request.addRequestParam_("isSrmsFailed", "false");
        } else {
            alert.prepareError("Error occurred while creating the drawing", responseObject).log();
            inputDto = new DrawingsDTO();
            inputDto.setErrorMessage(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setErrorCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            request.addRequestParam_("isSrmsFailed", "true");
        }
        return inputDto;
    }

    @Override
    public DrawingsDTO updateDrawingsOrder(DrawingsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        String requestBody = constructRequestPayload(inputDto);
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getDrawingsSrmsReqOrderID())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getDrawingsSrmsReqOrderID()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setDrawingsSrmsReqOrderID(responseObject.get(PARAM_UNIQUE_ID).toString());
            request.addRequestParam_("isSrmsFailed", "false");
        } else {
            alert.prepareError("Error occurred while updating the drawing", responseObject).log();
            inputDto = new DrawingsDTO();
            inputDto.setErrorMessage(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setErrorCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            request.addRequestParam_("isSrmsFailed", "true");
        }
        return inputDto;
    }

    public String constructRequestPayload(DrawingsDTO drawings) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("lcReferenceNo", drawings.getLcReferenceNo());
        requestBody.put("lcType", drawings.getLcType());
        requestBody.put("drawingReferenceNo", drawings.getDrawingReferenceNo());
        requestBody.put("beneficiaryName", drawings.getBeneficiaryName());
        if (StringUtils.isNotBlank(drawings.getDocumentStatus()) && drawings.getDocumentStatus().equalsIgnoreCase(PARAM_DISCREPANT))
            drawings.setDocumentStatus(PARAM_DISCREPANT);
        else if (StringUtils.isNotBlank(drawings.getDocumentStatus()) && drawings.getDocumentStatus().equalsIgnoreCase(PARAM_CLEAN))
            drawings.setDocumentStatus(PARAM_CLEAN);
        requestBody.put("documentStatus", drawings.getDocumentStatus());
        requestBody.put("drawingCreationDate", drawings.getDrawingCreationDate());
        requestBody.put("drawingCurrency", drawings.getDrawingCurrency());
        requestBody.put("drawingAmount", drawings.getDrawingAmount());
        requestBody.put("lcAmount", drawings.getLcAmount());
        requestBody.put("lcCurrency", drawings.getLcCurrency());
        requestBody.put("lcIssueDate", drawings.getLcIssueDate());
        requestBody.put("lcExpiryDate", drawings.getLcExpiryDate());
        requestBody.put("presentorReference", drawings.getPresentorReference());
        requestBody.put("presentorName", drawings.getPresentorName());
        if (StringUtils.isNotBlank(drawings.getDocumentsReceived()) && drawings.getDocumentsReceived().equalsIgnoreCase(PARAM_YES))
            drawings.setDocumentsReceived(PARAM_YES);
        else if (StringUtils.isNotBlank(drawings.getDocumentsReceived()) && drawings.getDocumentsReceived().equalsIgnoreCase(PARAM_NO))
            drawings.setDocumentsReceived(PARAM_NO);
        requestBody.put("documentsReceived", drawings.getDocumentsReceived());
        requestBody.put("forwardContact", drawings.getForwardContact());
        requestBody.put("shippingGuaranteeReference", drawings.getShippingGuaranteeReference());
        requestBody.put("approvalDate", drawings.getApprovalDate());
        requestBody.put("totalDocuments", drawings.getTotalDocuments());
        requestBody.put("documentName", drawings.getDocumentName());
        requestBody.put("discrepancies", drawings.getDiscrepancies());
        if (StringUtils.isNotBlank(drawings.getAcceptance()) && drawings.getAcceptance().equalsIgnoreCase(PARAM_APPROVED))
            drawings.setAcceptance(PARAM_APPROVED);
        else if (StringUtils.isNotBlank(drawings.getAcceptance()) && drawings.getAcceptance().equalsIgnoreCase(PARAM_REJECTED))
            drawings.setAcceptance(PARAM_REJECTED);
        requestBody.put("acceptance", drawings.getAcceptance());
        requestBody.put("totalAmountToBePaid", drawings.getTotalAmountToBePaid());
        requestBody.put("accountToBeDebited", drawings.getAccountToBeDebited());
        requestBody.put("messageFromBank", drawings.getMessageFromBank());
        requestBody.put("messageToBank", drawings.getMessageToBank());
        requestBody.put("totalPaidAmount", drawings.getTotalPaidAmount());
        requestBody.put("paymentDate", drawings.getPaymentDate());
        requestBody.put("reasonForRejection", drawings.getReasonForRejection());
        requestBody.put("paymentStatus", drawings.getPaymentStatus());
        requestBody.put("rejectedDate", drawings.getRejectedDate());
        requestBody.put("lcSrmsReqOrderID", drawings.getLcSrmsReqOrderID());
        requestBody.put("paymentStatus", drawings.getPaymentStatus());
        requestBody.put("status", drawings.getStatus());
        return requestBody.toString().replaceAll("\"", "'");
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }

}