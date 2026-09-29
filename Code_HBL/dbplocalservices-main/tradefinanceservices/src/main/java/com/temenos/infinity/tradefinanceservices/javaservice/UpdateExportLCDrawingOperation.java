/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import java.util.HashMap;
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
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.ExportLetterOfCreditsDrawingsResource;

public class UpdateExportLCDrawingOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		ExportLetterOfCreditsDrawingsResource drawingsResource = DBPAPIAbstractFactoryImpl
				.getResource(ExportLetterOfCreditsDrawingsResource.class);
		Result result = new Result();
		ExportLCDrawingsDTO updatePayloadDTO = new ExportLCDrawingsDTO();
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> requestParameters = (HashMap<String, Object>) inputArray[1];
			updatePayloadDTO = JSONUtils.parse(new JSONObject(requestParameters).toString(), ExportLCDrawingsDTO.class);
			result = drawingsResource.updateExportLCDrawing(updatePayloadDTO, request);
			return result;
		} catch (Exception e) {
			diagnostic.prepareDebug("Failed to Update" + e).log();
			return ErrorCodeEnum.ERRTF_29075.setErrorCode(result);
		}
	}

}
