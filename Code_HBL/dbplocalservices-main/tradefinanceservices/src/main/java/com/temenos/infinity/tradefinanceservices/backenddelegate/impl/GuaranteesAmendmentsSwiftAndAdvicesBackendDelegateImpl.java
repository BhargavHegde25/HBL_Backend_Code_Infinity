/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class GuaranteesAmendmentsSwiftAndAdvicesBackendDelegateImpl implements GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public SwiftsAndAdvisesDTO createSwiftsAndAdvises(SwiftsAndAdvisesDTO inputDto, DataControllerRequest request) {
        JSONObject responseObject;
        JSONObject requestBodyJson = constructSRMSParams(inputDto);
        String requestBody = requestBodyJson.toString().replace("\"", "\'");
        String[] moduleConfig = GUARANTEE_SWIFT_MESSAGE_CONFIG[inputDto.getModule().equalsIgnoreCase("GUAM") ? 0 : 1];

        this._loadBackendType();
        if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
            responseObject = invoke().createOrder().addRequestBody(requestBody).addDataControllerRequest(request)
                    .addTypeAndSubType(moduleConfig[0], moduleConfig[1]).sendRequest().fetchResponse();
        } else {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(requestBody)
                    .addModule(moduleConfig[2]).makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
            inputDto.setSwiftsAndAdvicesSrmsRequestOrderID(responseObject.get(PARAM_UNIQUE_ID).toString());
            inputDto.setStatus("Success");
            request.addRequestParam_("isSrmsFailed", "false");
        } else {
            alert.prepareError("Error occurred while creating guarantee swifts and messages", responseObject).log();
            inputDto = new SwiftsAndAdvisesDTO();
            inputDto.setErrorMessage(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setErrorCode(responseObject.getString(PARAM_DBP_ERR_CODE));
            request.addRequestParam_("isSrmsFailed", "true");
        }
        return inputDto;
    }

    public List<SwiftsAndAdvisesDTO> getGuaranteeSwiftAdvices(DataControllerRequest request) {
        String[] moduleConfig = GUARANTEE_SWIFT_MESSAGE_CONFIG[request.getParameter("module").equalsIgnoreCase("GUAM") ? 0 : 1];
        List<SwiftsAndAdvisesDTO> records = new LinkedList();
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                records = invoke().addDTO(SwiftsAndAdvisesDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType(moduleConfig[0], moduleConfig[1]).
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                records = getInstance().addDTO(SwiftsAndAdvisesDTO.class).addDataControllerRequest(request)
                        .addModule(moduleConfig[2]).getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching guarantee swifts and messages", e).log();
        }

        String orderId = request.getParameter("orderId");
        if (StringUtils.isNotBlank(orderId)) {
            for (SwiftsAndAdvisesDTO record : records) {
                if (!StringUtils.equals(record.getOrderId(), orderId)) {
                    records.remove(record);
                }
            }
        }

        return records;
    }

    private JSONObject constructSRMSParams(SwiftsAndAdvisesDTO inputDTO) {
        JSONObject reqBody = new JSONObject(inputDTO);

        reqBody.remove("module");
        if (StringUtils.isNotBlank(inputDTO.getNewSequence()))
            reqBody.put("newSequence", inputDTO.getNewSequence().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getRequestedDateOfIssue()))
            reqBody.put("requestedDateOfIssue", inputDTO.getRequestedDateOfIssue().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getFormOfUndertaking()))
            reqBody.put("formOfUndertaking", inputDTO.getFormOfUndertaking().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getApplicableRules()))
            reqBody.put("applicableRules", inputDTO.getApplicableRules().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getTypeOfUndertaking()))
            reqBody.put("typeOfUndertaking", inputDTO.getTypeOfUndertaking().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getExpiryType()))
            reqBody.put("expiryType", inputDTO.getExpiryType().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getDateOfExpiry()))
            reqBody.put("dateOfExpiry", inputDTO.getDateOfExpiry().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getExpiryConditionOrEvent()))
            reqBody.put("expiryConditionOrEvent", inputDTO.getExpiryConditionOrEvent().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getApplicant()))
            reqBody.put("applicant", inputDTO.getApplicant().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getObligorOrInstructingParty()))
            reqBody.put("obligorOrInstructingParty", inputDTO.getObligorOrInstructingParty().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getIssuer()))
            reqBody.put("issuer", inputDTO.getIssuer().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getBeneficiary()))
            reqBody.put("beneficiary", inputDTO.getBeneficiary().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getUndertakingAmount()))
            reqBody.put("undertakingAmount", inputDTO.getUndertakingAmount().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAdditionalAmountInformation()))
            reqBody.put("additionalAmountInformation", inputDTO.getAdditionalAmountInformation().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAvailableWith()))
            reqBody.put("availableWith", inputDTO.getAvailableWith().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getCharges()))
            reqBody.put("charges", inputDTO.getCharges().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getDocumentAndPresentationInstructions()))
            reqBody.put("documentAndPresentationInstructions", inputDTO.getDocumentAndPresentationInstructions().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getRequestedLocalUndertakingTermsAndConditions()))
            reqBody.put("requestedLocalUndertakingTermsAndConditions", inputDTO.getRequestedLocalUndertakingTermsAndConditions().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getStandardWordingRequired()))
            reqBody.put("standardWordingRequired", inputDTO.getStandardWordingRequired().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getStandardWordingRequestedLanguage()))
            reqBody.put("standardWordingRequestedLanguage", inputDTO.getStandardWordingRequestedLanguage().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getGoverningLawAndOrPlaceOfJurisdiction()))
            reqBody.put("governingLawAndOrPlaceOfJurisdiction", inputDTO.getGoverningLawAndOrPlaceOfJurisdiction().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAutomaticExtensionPeriod()))
            reqBody.put("automaticExtensionPeriod", inputDTO.getAutomaticExtensionPeriod().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAutomaticExtensionNonExtensionPeriod()))
            reqBody.put("automaticExtensionNonExtensionPeriod", inputDTO.getAutomaticExtensionNonExtensionPeriod().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAutomaticExtensionNotificationPeriod()))
            reqBody.put("automaticExtensionNotificationPeriod", inputDTO.getAutomaticExtensionNotificationPeriod().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getAutomaticExtensionFinalExpiryDate()))
            reqBody.put("automaticExtensionFinalExpiryDate", inputDTO.getAutomaticExtensionFinalExpiryDate().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getDemandIndicator()))
            reqBody.put("demandIndicator", inputDTO.getDemandIndicator().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getTransferIndicator()))
            reqBody.put("transferIndicator", inputDTO.getTransferIndicator().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getTransferConditions()))
            reqBody.put("transferConditions", inputDTO.getTransferConditions().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getUnderlyingTransactionDetails()))
            reqBody.put("underlyingTransactionDetails", inputDTO.getUnderlyingTransactionDetails().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getDeliveryOfLocalUndertaking()))
            reqBody.put("deliveryOfLocalUndertaking", inputDTO.getDeliveryOfLocalUndertaking().replaceAll("\'", "\""));
        if (StringUtils.isNotBlank(inputDTO.getDeliveryToOrCollectionBy()))
            reqBody.put("deliveryToOrCollectionBy", inputDTO.getDeliveryToOrCollectionBy().replaceAll("\'", "\""));

        return reqBody;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }

}