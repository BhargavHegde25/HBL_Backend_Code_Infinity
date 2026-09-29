
package com.temenos.dbx.product.achservices.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetACHTemplateTypeOperation implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputParams, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
		Result result;
		try 
		{	
			ACHTemplateResource achResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(ACHTemplateResource.class);
			result = achResource.getACHTemplateType(inputParams, request);
		} 
		catch(Exception e) 
		{
			alert.prepareError("Error occured while invoking getBBTemplatesType: ",e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

}
