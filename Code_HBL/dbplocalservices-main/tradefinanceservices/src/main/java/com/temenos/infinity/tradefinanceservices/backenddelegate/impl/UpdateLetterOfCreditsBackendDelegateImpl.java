package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.UpdateLetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
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


public class UpdateLetterOfCreditsBackendDelegateImpl implements UpdateLetterOfCreditsBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    public LetterOfCreditsDTO updateLetterOfCreditsOrder(LetterOfCreditsDTO inputDto, DataControllerRequest request) throws ApplicationException {
        GetLetterOfCreditsByIdBackendDelegate getLetterOfCreditsByIdBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(GetLetterOfCreditsByIdBackendDelegate.class);
        JSONObject requestBodyJson = new JSONObject();
        if (inputDto.getStatus().equalsIgnoreCase("Delete")) {
            inputDto = getLetterOfCreditsByIdBackendDelegate.getImportLCById(inputDto.getSrmsReqOrderID(), request);
            inputDto.setStatus("Delete");
            inputDto.setFlowType("");
            inputDto.setIsDraft("");
        }
        requestBodyJson.put("lcReferenceNo", inputDto.getLcReferenceNo());
        requestBodyJson.put("srmsReqOrderID", inputDto.getSrmsReqOrderID());
        requestBodyJson.put("lcAmount", String.valueOf(inputDto.getLcAmount()));
        requestBodyJson.put("lcCurrency", inputDto.getLcCurrency());
        requestBodyJson.put("tolerancePercentage", inputDto.getTolerancePercentage());
        requestBodyJson.put("maximumCreditAmount", String.valueOf(inputDto.getMaximumCreditAmount()));
        requestBodyJson.put("additionalAmountPayable", inputDto.getAdditionalAmountPayable());
        requestBodyJson.put("paymentTerms", inputDto.getPaymentTerms());
        requestBodyJson.put("availableWith1", inputDto.getAvailableWith1());
        requestBodyJson.put("availableWith2", inputDto.getAvailableWith2());
        requestBodyJson.put("availableWith3", inputDto.getAvailableWith3());
        requestBodyJson.put("availableWith4", inputDto.getAvailableWith4());
        requestBodyJson.put("issueDate", inputDto.getIssueDate());
        requestBodyJson.put("expiryDate", inputDto.getExpiryDate());
        requestBodyJson.put("expiryPlace", inputDto.getExpiryPlace());
        requestBodyJson.put("chargesAccount", inputDto.getChargesAccount());
        requestBodyJson.put("commisionAccount", inputDto.getCommisionAccount());
        requestBodyJson.put("marginAccount", inputDto.getMarginAccount());
        requestBodyJson.put("messageToBank", inputDto.getMessageToBank());
        requestBodyJson.put("beneficiaryName", inputDto.getBeneficiaryName());
        requestBodyJson.put("beneficiaryAddressLine1", inputDto.getBeneficiaryAddressLine1());
        requestBodyJson.put("beneficiaryAddressLine2", inputDto.getBeneficiaryAddressLine2());
        requestBodyJson.put("beneficiaryPostCode", inputDto.getBeneficiaryPostCode());
        requestBodyJson.put("beneficiaryCountry", inputDto.getBeneficiaryCountry());
        requestBodyJson.put("beneficiaryCity", inputDto.getBeneficiaryCity());
        requestBodyJson.put("beneficiaryState", inputDto.getBeneficiaryState());
        requestBodyJson.put("beneficiaryBank", inputDto.getBeneficiaryBank());
        requestBodyJson.put("beneficiaryBankAdressLine1", inputDto.getBeneficiaryBankAdressLine1());
        requestBodyJson.put("beneficiaryBankAdressLine2", inputDto.getBeneficiaryBankAdressLine2());
        requestBodyJson.put("beneficiaryBankPostCode", inputDto.getBeneficiaryBankPostCode());
        requestBodyJson.put("beneficiaryBankCountry", inputDto.getBeneficiaryBankCountry());
        requestBodyJson.put("beneficiaryBankCity", inputDto.getBeneficiaryBankCity());
        requestBodyJson.put("beneficiaryBankState", inputDto.getBeneficiaryBankState());
        requestBodyJson.put("placeOfTakingIncharge", inputDto.getPlaceOfTakingIncharge());
        requestBodyJson.put("portOfLoading", inputDto.getPortOfLoading());
        requestBodyJson.put("portOfDischarge", inputDto.getPortOfDischarge());
        requestBodyJson.put("placeOfFinalDelivery", inputDto.getPlaceOfFinalDelivery());
        requestBodyJson.put("latestShippingDate", inputDto.getLatestShippingDate());
        requestBodyJson.put("presentationPeriod", inputDto.getPresentationPeriod());
        requestBodyJson.put("transshipment", inputDto.getTransshipment());
        requestBodyJson.put("partialShipments", inputDto.getPartialShipments());
        requestBodyJson.put("incoTerms", inputDto.getIncoTerms());
        requestBodyJson.put("modeOfShipment", inputDto.getModeOfShipment());
        requestBodyJson.put("descriptionOfGoods", inputDto.getDescriptionOfGoods());
        requestBodyJson.put("documentsRequired", inputDto.getDocumentsRequired());
        requestBodyJson.put("additionalConditionsCode", inputDto.getAdditionalConditionsCode());
        requestBodyJson.put("otherAdditionalConditions", inputDto.getOtherAdditionalConditions());
        requestBodyJson.put("documentCharges", inputDto.getDocumentCharges());
        requestBodyJson.put("supportDocuments", inputDto.getSupportDocuments());
        requestBodyJson.put("fileToUpload", inputDto.getFileToUpload());
        requestBodyJson.put("confirmationInstruction", inputDto.getConfirmationInstruction());
        requestBodyJson.put("transferable", inputDto.getTransferable());
        requestBodyJson.put("standByLC", inputDto.getStandByLC());
        requestBodyJson.put("lcCreatedOn", getCurrentDateTimeUTF());
        requestBodyJson.put("screenNumber", inputDto.getScreenNumber());

        String status;
        if (inputDto.getFlowType().equalsIgnoreCase("finalSubmit")) {
            requestBodyJson.put("isDraft", "false");
            status = inputDto.getSignatoryApprovalRequired() == "true" ? "Pending" : TradeFinanceConstants.PARAM_STATUS_SUBMITTED_TO_BANK;
        } else if (inputDto.getFlowType().equalsIgnoreCase("BankUpdate")) {
            requestBodyJson.put("isDraft", "false");
            status = inputDto.getStatus();
        } else {
            requestBodyJson.put("isDraft", "true");
            status = TradeFinanceConstants.PARAM_STATUS_DRAFT;
        }
        if (inputDto.getStatus().equalsIgnoreCase("Delete")) {
            status = TradeFinanceConstants.PARAM_STATUS_DELETE;
        }
        requestBodyJson.put("status", status);

        requestBodyJson.put("additionalPayableCurrency", inputDto.getAdditionalPayableCurrency());
        String requestBody = requestBodyJson.toString().replace("\"", "'");

        JSONObject responseObject;
        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().updateOrder().addServiceRequestId(inputDto.getSrmsReqOrderID())
                    .addRequestBody(requestBody).addDataControllerRequest(request).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).updateRecord()
                    .addRecordId(inputDto.getSrmsReqOrderID()).addRequestBody(requestBody).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setSrmsReqOrderID(responseObject.get(PARAM_UNIQUE_ID).toString());
            request.addRequestParam_("isSrmsFailed", "false");
            request.addRequestParam_("backendEndId", inputDto.getSrmsReqOrderID());
        } else {
            alert.prepareError("Error occurred while updating import lc amendment", responseObject).log();
            inputDto = new LetterOfCreditsDTO();
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