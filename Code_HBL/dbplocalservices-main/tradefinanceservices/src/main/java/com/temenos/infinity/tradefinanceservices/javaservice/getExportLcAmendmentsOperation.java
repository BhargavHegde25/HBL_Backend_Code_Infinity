/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.ExportLCAmendmentResource;

public class getExportLcAmendmentsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        ExportLCAmendmentResource drawingsResource = DBPAPIAbstractFactoryImpl
                .getResource(ExportLCAmendmentResource.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];
        FilterDTO filterDTO = null;
        Result result = null;
        try {
            filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
            result = (Result) drawingsResource.getExportAmendments(filterDTO, request, false);
        } catch (IOException e) {
            alert.prepareError("Exception occurred while fetching params: ", e).log();
        }

        return result;
    }

}
