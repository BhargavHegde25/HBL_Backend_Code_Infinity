/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetExportLetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getTfBackend;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getUniqueIdParamName;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceDBXDBUtils.getInstance;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceSRMSUtils.invoke;

public class GetExportLetterOfCreditsBackendDelegateImpl implements GetExportLetterOfCreditsBackendDelegate, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String TF_BACKEND;
    private static String PARAM_UNIQUE_ID;

    @Override
    public List<ExportLOCDTO> getExportLetterOfCredits(DataControllerRequest request) {
        List letterOfCredits = null;
        try {
            this._loadBackendType();
            if (StringUtils.equals(TF_BACKEND, PARAM_TF_BACKEND_SRMS)) {
                letterOfCredits = invoke().addDTO(ExportLOCDTO.class).
                        addDataControllerRequest(request).addTypeAndSubType("ExportLetterOfCreditsType", "ExportLetterOfCreditsSubType").
                        getOrders().sendRequest().fetchOrdersResponseWithDTO();
            } else {
                letterOfCredits = getInstance().addDTO(ExportLOCDTO.class).addDataControllerRequest(request)
                        .addModule("ExportLetterOfCreditsModule").getAllRecords().makeRequest().fetchOrdersWithDTO();
            }
            // setExportLCId
            // requestCreatedTime
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching outward collections", e).log();
        }
        return letterOfCredits;
    }

    private void _loadBackendType() {
        TF_BACKEND = getTfBackend();
        PARAM_UNIQUE_ID = getUniqueIdParamName(TF_BACKEND);
    }
}