/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetAmendmentsLetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_TF_BACKEND_SRMS;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class GetAmendmentsLetterOfCreditsBackendDelegateImpl implements GetAmendmentsLetterOfCreditsBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;

    @Override
    public List<LetterOfCreditsAmendmentDTO> getamendLetterOfCreditsFromSRMS(DataControllerRequest request) {
        List amendmentsList = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentsList = invoke().addDTO(LetterOfCreditsAmendmentDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("LetterOfCreditsAmendmentType", "LetterOfCreditsAmendmentSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                amendmentsList = getInstance().addDTO(LetterOfCreditsAmendmentDTO.class).addDataControllerRequest(request)
                        .addModule("LetterOfCreditsAmendmentModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
            // setAmendmentReference
            // setAmendmentDate
            // setAmendmentApprovedDate
        } catch (Exception e) {
            // ERRTF_29046
            alert.prepareError("Unable to get amendment requests ", e).log();
        }
        return amendmentsList;
    }

    public LetterOfCreditsAmendmentDTO getAmendmentsById(String amendmentReference, DataControllerRequest request) {
        LetterOfCreditsAmendmentDTO amendmentDto = new LetterOfCreditsAmendmentDTO();
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                amendmentDto = (LetterOfCreditsAmendmentDTO) invoke().addDTO(LetterOfCreditsAmendmentDTO.class).addServiceRequestId(amendmentReference)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                amendmentDto = (LetterOfCreditsAmendmentDTO) getInstance().addDTO(LetterOfCreditsAmendmentDTO.class).filterByRecordId(amendmentReference)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            amendmentDto.setAmendmentReference(amendmentReference);
            // setAmendmentDate, setAmendmentApprovedDate <- requestCreatedTime
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching amendment id", e).log();
            request.addRequestParam_("isSrmsFailed", "true");
            amendmentDto.setMsg("Failed to fetch record");
        }
        return amendmentDto;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
    }

}