/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.resource.api.GuaranteeLCAmendmentsResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetGuaranteeLCAmendmentByIdOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		GuaranteeLCAmendmentsResource letterOfCreditsResource = DBPAPIAbstractFactoryImpl
				.getResource(GuaranteeLCAmendmentsResource.class);
		Result result = new Result();
		try {
			result = letterOfCreditsResource.getGuaranteeLCAmendmentById(request);
			return result;
		} catch (Exception e) {
			alert.prepareError("Error occurred while invoking GetGuaranteeLCAmendmentByIdOperation. Error: ", e).log();
			return ErrorCodeEnum.ERRTF_29054.setErrorCode(result);
		}
	}

}
