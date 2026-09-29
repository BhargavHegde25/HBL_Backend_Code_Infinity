package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class GetLetterOfCreditsByIdBackendDelegateImpl implements GetLetterOfCreditsByIdBackendDelegate, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;

    public LetterOfCreditsDTO getImportLCById(String importLcId, DataControllerRequest request) {
        LetterOfCreditsDTO responseDto = new LetterOfCreditsDTO();
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                responseDto = (LetterOfCreditsDTO) invoke().addDTO(LetterOfCreditsDTO.class).addServiceRequestId(importLcId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                responseDto = (LetterOfCreditsDTO) getInstance().addDTO(LetterOfCreditsDTO.class).filterByRecordId(importLcId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            responseDto.setLcSRMSId(importLcId);
            responseDto.setReferenceNomatch(StringUtils.isNotBlank(responseDto.getStatus()));
            request.addRequestParam_("isSrmsFailed", "false");
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching outward collection", e).log();
            request.addRequestParam_("isSrmsFailed", "true");
            responseDto.setErrorMsgSRMSmatch(e.getMessage());
        }
        return responseDto;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
    }

}