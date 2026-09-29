/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetExportLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_TF_BACKEND_SRMS;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class GetExportLetterOfCreditsByIdBackendDelegateImpl implements GetExportLetterOfCreditsByIdBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    public ExportLOCDTO getExportLetterOfCreditById(String exportLcId, DataControllerRequest request) {
        ExportLOCDTO exportLcDTO = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                exportLcDTO = (ExportLOCDTO) invoke().addDTO(ExportLOCDTO.class).addServiceRequestId(exportLcId)
                        .addDataControllerRequest(request).getOrderById().sendRequest().fetchOrderByIdResponse();
            } else {
                exportLcDTO = (ExportLOCDTO) getInstance().addDTO(ExportLOCDTO.class).filterByRecordId(exportLcId)
                        .addDataControllerRequest(request).getRecord().makeRequest().fetchRecordWithDTO();
            }
            exportLcDTO.setExportLCId(exportLcId);
            // exportLcDTO.setCustomerId((String) serviceResponse.get("partyId"));
            // exportLcDTO.setLcCreatedOn(serviceResponse.getString("requestCreatedTime"));
        } catch (Exception e) {
            // request.addRequestParam_("isSrmsFailed", "true");
            alert.prepareError("Error occurred while get by id - export lc", e).log();
            exportLcDTO.setErrorMsg("Failed to fetch record");
        }
        return exportLcDTO;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }

}