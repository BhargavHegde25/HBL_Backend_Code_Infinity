/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.infinity.tradefinanceservices.resource.api.ExportLCAmendmentResource;
import net.minidev.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.IOException;
import java.util.HashMap;

public class CreateExportLetterOfCreditsAmendment implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            ExportLCAmendmentResource exportLCAmendmentResource = DBPAPIAbstractFactoryImpl
                    .getResource(ExportLCAmendmentResource.class);
            ExportLCAmendmentsDTO letterOfCredit = constructPayload(inputArray);
            Result result = exportLCAmendmentResource.amendExportLetterOfCredits(letterOfCredit, request);
            return result;
        } catch (Exception e) {
            alert.prepareError("Unable to amend Letter Of Credit " + e).log();
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
        }

    }

    public static ExportLCAmendmentsDTO constructPayload(Object[] inputArray) {
        ExportLCAmendmentsDTO letterOfCredit = new ExportLCAmendmentsDTO();

        @SuppressWarnings("unchecked")
        HashMap<String, Object> requestParameters = (HashMap<String, Object>) inputArray[1];

        try {
            ExportLCAmendmentsDTO parseInput = JSONUtils.parse(new JSONObject(requestParameters).toString(),
                    ExportLCAmendmentsDTO.class);
            return parseInput;
        } catch (IOException e) {
            letterOfCredit.setErrorMessage("Error occurred while parsing the input.");
        }
        return letterOfCredit;

    }
}
