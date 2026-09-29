/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import java.util.HashMap;

import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.resource.api.CreateLetterOfCreditsResource;
import org.json.JSONObject;

public class AmendLetterOfCreditOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
        Log4j2Configurator.getInstance();
        try {
            HashMap<String, Object> requestParameters = (HashMap<String, Object>) inputArray[1];
            LetterOfCreditsAmendmentDTO letterOfCredit = JSONUtils.parse(new JSONObject(requestParameters).toString(), LetterOfCreditsAmendmentDTO.class);
            return DBPAPIAbstractFactoryImpl.getResource(CreateLetterOfCreditsResource.class).amendLetterOfCredits(letterOfCredit, request);
        } catch (Exception e) {
            alert.prepareError("Unable to amend Letter Of Credit " + e).log();
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
        }

    }

}