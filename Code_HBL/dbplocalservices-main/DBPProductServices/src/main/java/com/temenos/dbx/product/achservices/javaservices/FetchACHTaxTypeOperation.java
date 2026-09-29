package com.temenos.dbx.product.achservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.achservices.resource.api.ACHCommonsResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchACHTaxTypeOperation implements JavaService2{

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		// TODO Auto-generated method stub
		final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		try 
		{	
			ACHCommonsResource achTaxTypeResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(ACHCommonsResource.class);
			Result result = achTaxTypeResource.fetchACHTaxType(methodID, inputArray, request, response);
			return result;
 	     } 
		catch(Exception e) 
		{
			alert.prepareError("Error occured while invoking FetchACHTaxTypeOperation: ",e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
	}

}
