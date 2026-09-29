package com.temenos.infinity.api.arrangements.postprocessors;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;


/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */

public class CreatePayOffSimulationPostProcessor extends TemenosBasePostProcessor  {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        String transactionStatus = result.getParamValueByName("transactionStatus");
        
        if("Error".equalsIgnoreCase(transactionStatus)) {
        	//Might be override
        		Dataset overridesDS = result.getDatasetById("overrideDetails");
        		if(overridesDS!=null) {
        			 List<Record> overrideRecords =  overridesDS.getAllRecords();
        			 StringBuffer overrideMessage = new StringBuffer();
        			 for (Record override : overrideRecords) {
        				 String code = override.getParamValueByName("code");
        				 String description = override.getParamValueByName("description");
        				 overrideMessage.append(description+". ");
        			 }
        			 result.addParam("overrideMessage", overrideMessage.toString());
        		}

        }

		return result;
    }
  
}