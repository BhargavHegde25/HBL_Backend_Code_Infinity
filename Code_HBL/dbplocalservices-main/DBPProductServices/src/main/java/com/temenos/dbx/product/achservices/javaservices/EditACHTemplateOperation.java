package com.temenos.dbx.product.achservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class EditACHTemplateOperation implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		try {
			//Initializing of ACHTemplateResource through Abstract factory method
			ACHTemplateResource achResource = DBPAPIAbstractFactoryImpl.getResource(ACHTemplateResource.class);
			result  = achResource.editACHTemplate(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking updateACHTemplate: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}

}
