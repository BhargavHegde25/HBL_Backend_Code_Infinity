/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.backenddelegate.impl;

import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_DBP_ERR_CODE;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_DBP_ERR_MSG;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_LD_BACKEND_DBXDB;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradelending.utils.TradeLendingDBXDBUtils.getInstance;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.backenddelegate.api.DrawdownRequestBackendDelegate;
import com.temenos.infinity.tradelending.dto.DrawdownRequestDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class DrawdownRequestBackendDelegateImpl implements DrawdownRequestBackendDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static String SCF_BACKEND = "DBXDB";
    private static String PARAM_UNIQUE_ID = PARAM_RECORD_ID;

	@Override
	public DrawdownRequestDTO createDrawdownRequest(DrawdownRequestDTO inputDto, DataControllerRequest request) {
		
		JSONObject responseObject = null;
        if (StringUtils.equals(SCF_BACKEND, PARAM_LD_BACKEND_DBXDB)) {
            responseObject = getInstance().addDataControllerRequest(request).addRecord().addRequestBody(inputDto)
                    .addModule("DrawdownRequestModule").makeRequest().getResponse();
        }

        if (!responseObject.has(PARAM_DBP_ERR_MSG)) {
        	inputDto.setDrawdownRequestId(responseObject.get(PARAM_UNIQUE_ID).toString());
            
        } else {
            inputDto = new DrawdownRequestDTO();
            inputDto.setDbpErrMsg(responseObject.getString(PARAM_DBP_ERR_MSG));
            inputDto.setDbpErrCode(responseObject.getString(PARAM_DBP_ERR_CODE));
        }
        return inputDto;
	}


}
