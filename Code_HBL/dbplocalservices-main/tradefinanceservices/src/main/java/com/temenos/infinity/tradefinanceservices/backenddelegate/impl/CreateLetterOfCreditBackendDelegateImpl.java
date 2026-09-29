package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.CreateLetterOfCreditBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_DBP_ERR_MSG;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_TF_BACKEND_SRMS;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class CreateLetterOfCreditBackendDelegateImpl implements CreateLetterOfCreditBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public LetterOfCreditsDTO createLetterOfCreditsOrder(LetterOfCreditsDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        JSONObject requestBodyJson = constructLcPayload(inputDto);
        String requestBody = requestBodyJson.toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("letterOfCreditsType", "letterOfCreditsSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBodyJson)
                    .addModule("letterOfCreditsModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setSrmsReqOrderID(responseObject.get(PARAM_UNIQUE_ID).toString());
            request.addRequestParam_("backendEndId", inputDto.getSrmsReqOrderID());
            request.addRequestParam_("isSrmsFailed", "false");
        } else {
            alert.prepareError("Error occurred while creating import lc", responseObject).log();
            inputDto = new LetterOfCreditsDTO();
            inputDto.setMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            // inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            request.addRequestParam_("isSrmsFailed", "true");
        }
        return inputDto;
    }

    private JSONObject constructLcPayload(LetterOfCreditsDTO letterOfCredit) {
        String status = "";
        JSONObject requestBody = new JSONObject();
        requestBody.put("lcReferenceNo", letterOfCredit.getLcReferenceNo());
        requestBody.put("lcAmount", String.valueOf(letterOfCredit.getLcAmount()));
        requestBody.put("lcCurrency", letterOfCredit.getLcCurrency());
        requestBody.put("tolerancePercentage", letterOfCredit.getTolerancePercentage());
        requestBody.put("maximumCreditAmount", String.valueOf(letterOfCredit.getMaximumCreditAmount()));
        requestBody.put("additionalAmountPayable", letterOfCredit.getAdditionalAmountPayable());
        requestBody.put("paymentTerms", letterOfCredit.getPaymentTerms());
        requestBody.put("availableWith1", letterOfCredit.getAvailableWith1());
        requestBody.put("availableWith2", letterOfCredit.getAvailableWith2());
        requestBody.put("availableWith3", letterOfCredit.getAvailableWith3());
        requestBody.put("availableWith4", letterOfCredit.getAvailableWith4());
        requestBody.put("issueDate", letterOfCredit.getIssueDate());
        requestBody.put("expiryDate", letterOfCredit.getExpiryDate());
        requestBody.put("expiryPlace", letterOfCredit.getExpiryPlace());
        requestBody.put("chargesAccount", letterOfCredit.getChargesAccount());
        requestBody.put("commisionAccount", letterOfCredit.getCommisionAccount());
        requestBody.put("marginAccount", letterOfCredit.getMarginAccount());
        requestBody.put("messageToBank", letterOfCredit.getMessageToBank());
        requestBody.put("beneficiaryName", letterOfCredit.getBeneficiaryName());
        requestBody.put("beneficiaryAddressLine1", letterOfCredit.getBeneficiaryAddressLine1());
        requestBody.put("beneficiaryAddressLine2", letterOfCredit.getBeneficiaryAddressLine2());
        requestBody.put("beneficiaryPostCode", letterOfCredit.getBeneficiaryPostCode());
        requestBody.put("beneficiaryCountry", letterOfCredit.getBeneficiaryCountry());
        requestBody.put("beneficiaryCity", letterOfCredit.getBeneficiaryCity());
        requestBody.put("beneficiaryState", letterOfCredit.getBeneficiaryState());
        requestBody.put("beneficiaryBank", letterOfCredit.getBeneficiaryBank());
        requestBody.put("beneficiaryBankAdressLine1", letterOfCredit.getBeneficiaryBankAdressLine1());
        requestBody.put("beneficiaryBankAdressLine2", letterOfCredit.getBeneficiaryBankAdressLine2());
        requestBody.put("beneficiaryBankPostCode", letterOfCredit.getBeneficiaryBankPostCode());
        requestBody.put("beneficiaryBankCountry", letterOfCredit.getBeneficiaryBankCountry());
        requestBody.put("beneficiaryBankCity", letterOfCredit.getBeneficiaryBankCity());
        requestBody.put("beneficiaryBankState", letterOfCredit.getBeneficiaryBankState());
        requestBody.put("placeOfTakingIncharge", letterOfCredit.getPlaceOfTakingIncharge());
        requestBody.put("portOfLoading", letterOfCredit.getPortOfLoading());
        requestBody.put("portOfDischarge", letterOfCredit.getPortOfDischarge());
        requestBody.put("placeOfFinalDelivery", letterOfCredit.getPlaceOfFinalDelivery());
        requestBody.put("latestShippingDate", letterOfCredit.getLatestShippingDate());
        requestBody.put("presentationPeriod", letterOfCredit.getPresentationPeriod());
        requestBody.put("transshipment", letterOfCredit.getTransshipment());
        requestBody.put("partialShipments", letterOfCredit.getPartialShipments());
        requestBody.put("incoTerms", letterOfCredit.getIncoTerms());
        requestBody.put("modeOfShipment", letterOfCredit.getModeOfShipment());
        requestBody.put("descriptionOfGoods", letterOfCredit.getDescriptionOfGoods());
        requestBody.put("documentsRequired", letterOfCredit.getDocumentsRequired());
        requestBody.put("additionalConditionsCode", letterOfCredit.getAdditionalConditionsCode());
        requestBody.put("otherAdditionalConditions", letterOfCredit.getOtherAdditionalConditions());
        requestBody.put("documentCharges", letterOfCredit.getDocumentCharges());
        requestBody.put("supportDocuments", letterOfCredit.getSupportDocuments());
        requestBody.put("fileToUpload", letterOfCredit.getFileToUpload());
        requestBody.put("confirmationInstruction", letterOfCredit.getConfirmationInstruction());

        requestBody.put("standByLC", letterOfCredit.getStandByLC());
        requestBody.put("transferable", letterOfCredit.getTransferable());
        requestBody.put("screenNumber", letterOfCredit.getScreenNumber());
        if (letterOfCredit.getFlowType().equalsIgnoreCase("finalSubmit")) {
            requestBody.put("isDraft", "false");
            status = StringUtils.equals(letterOfCredit.getSignatoryApprovalRequired(), "true") ? "Pending"
                    : TradeFinanceConstants.PARAM_STATUS_SUBMITTED_TO_BANK;
        } else {
            requestBody.put("isDraft", "true");
            status = TradeFinanceConstants.PARAM_STATUS_DRAFT;
        }
        requestBody.put("status", status);
//        requestBody.put("lcCreatedOn", getCurrentDateTimeUTF());
        requestBody.put("additionalPayableCurrency", letterOfCredit.getAdditionalPayableCurrency());
        return requestBody;
    }


    public LetterOfCreditsAmendmentDTO amendLetterOfCredit(LetterOfCreditsAmendmentDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        JSONObject requestBodyJson = constructAmendmentPayload(inputDto);
        String requestBody = requestBodyJson.toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType("LetterOfCreditsAmendmentType", "LetterOfCreditsAmendmentSubType").sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule("LetterOfCreditsAmendmentModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            // setAmendmentDate
            inputDto.setSrmsReqOrderID(responseObject.get(PARAM_UNIQUE_ID).toString());
            inputDto.setAmendmentReference(responseObject.get(PARAM_UNIQUE_ID).toString());
            request.addRequestParam_("backendEndId", inputDto.getSrmsReqOrderID());
            request.addRequestParam_("isSrmsFailed", "false");
        } else {
            alert.prepareError("Error occurred while creating import amendment", responseObject).log();

            inputDto = new LetterOfCreditsAmendmentDTO();
            inputDto.setMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            request.addRequestParam_("isSrmsFailed", "true");
        }
        return inputDto;
    }


    public JSONObject constructAmendmentPayload(LetterOfCreditsAmendmentDTO letterOfCredit) {
        JSONObject requestBody = new JSONObject();
        if (StringUtils.isNotEmpty(letterOfCredit.getLcReferenceNo())) {
            requestBody.put("lcReferenceNo", letterOfCredit.getLcReferenceNo());
        }

        if (StringUtils.isNotEmpty(letterOfCredit.getLcCurrency())) {
            requestBody.put("lcCurrency", letterOfCredit.getLcCurrency());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getPaymentTerms())) {
            requestBody.put("paymentTerms", letterOfCredit.getPaymentTerms());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getExpiryDate())) {
            requestBody.put("expiryDate", letterOfCredit.getExpiryDate());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getLatestShippingDate())) {
            requestBody.put("latestShippingDate", letterOfCredit.getLatestShippingDate());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getPresentationPeriod())) {
            requestBody.put("presentationPeriod", letterOfCredit.getPresentationPeriod());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getChargesAccount())) {
            requestBody.put("chargesAccount", letterOfCredit.getChargesAccount());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getChargesPaid())) {
            requestBody.put("chargesPaid", letterOfCredit.getChargesPaid());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getBeneficiaryName())) {
            requestBody.put("beneficiaryName", letterOfCredit.getBeneficiaryName());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getIssueDate())) {
            requestBody.put("issueDate", letterOfCredit.getIssueDate());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmountType())) {
            requestBody.put("amountType", letterOfCredit.getAmountType());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getOtherAmendments())) {
            requestBody.put("otherAmendments", letterOfCredit.getOtherAmendments());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendCharges())) {
            requestBody.put("amendCharges", letterOfCredit.getAmendCharges());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getCreditAmount())) {
            requestBody.put("creditAmount", letterOfCredit.getCreditAmount());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendmentExpiryDate())) {
            requestBody.put("amendmentExpiryDate", letterOfCredit.getAmendmentExpiryDate());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getAmendStatus())) {
            requestBody.put("amendStatus", letterOfCredit.getAmendStatus());
        }
        if (StringUtils.isNotEmpty(letterOfCredit.getLcSRMSId())) {
            requestBody.put("lcSRMSId", letterOfCredit.getLcSRMSId());
        }
        requestBody.put("lcAmount", String.valueOf(letterOfCredit.getLcAmount()));
        return requestBody;
    }

    @Override
    public LetterOfCreditsAmendmentDTO updateAmendLC(LetterOfCreditsAmendmentDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        JSONObject requestBodyJson = constructAmendmentPayload(inputDto);
        String requestBody = requestBodyJson.toString().replace("\"", "'");
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getAmendmentReference())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getAmendmentReference()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setSrmsReqOrderID(responseObject.get(PARAM_UNIQUE_ID).toString());
            request.addRequestParam_("backendEndId", inputDto.getSrmsReqOrderID());
            request.addRequestParam_("isSrmsFailed", "false");
        } else {
            alert.prepareError("Error occurred while updating import amendments", responseObject).log();
            inputDto = new LetterOfCreditsAmendmentDTO();
            inputDto.setMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            // inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            request.addRequestParam_("isSrmsFailed", "true");
        }
        return inputDto;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}
