package com.temenos.infinity.tradefinanceservices.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import  com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
//import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GetLetterOfCreditsResource;


/**
 *
 *
 */
public class GetLetterOfCreditsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
    	try {
            GetLetterOfCreditsResource letterOfCreditsResource = DBPAPIAbstractFactoryImpl
                    .getResource(GetLetterOfCreditsResource.class);
            LetterOfCreditsDTO letterOfCredits = new LetterOfCreditsDTO();                      
            Result result = letterOfCreditsResource.getLetterOfCredits(inputArray,letterOfCredits, request);
            return result;
        } catch (Exception e) { 
            alert.prepareError("Unable to get Letter Of Credits Requests from OMS: "+e).log();
            return ErrorCodeEnum.ERRTF_29046.setErrorCode(new Result()); 
        }
    }
}
