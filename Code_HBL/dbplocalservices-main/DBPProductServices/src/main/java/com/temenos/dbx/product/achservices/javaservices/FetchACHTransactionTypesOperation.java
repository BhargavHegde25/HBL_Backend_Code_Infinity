package com.temenos.dbx.product.achservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.resource.api.ACHCommonsResource;

public class FetchACHTransactionTypesOperation implements JavaService2 {
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception
	{
		Log4j2Configurator.getInstance();
		final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
		try 
		{	
			ACHCommonsResource bbTransactionTypesResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(ACHCommonsResource.class);
			Result result = bbTransactionTypesResource.fetchBBTransactionTypes(methodID, inputArray, request, response);
			return result;
 	     } 
		catch(Exception e) 
		{
			alert.prepareError("Error occured while invoking FetchBBTransactionTypesOperation: ",e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
    }
}
