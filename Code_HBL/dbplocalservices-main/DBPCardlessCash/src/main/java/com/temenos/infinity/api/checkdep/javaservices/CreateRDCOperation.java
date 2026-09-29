package com.temenos.infinity.api.checkdep.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.checkdep.resource.api.RDCResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateRDCOperation implements JavaService2{

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		
		Result result = null;
		
		try {
		
			RDCResource rdcResource = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
					.getResource(RDCResource.class);
			result = rdcResource.createRDC(methodID,inputArray,request,response);
			
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured in CreateRDCOpertaion", exp).log();
			return ErrorCodeEnum.ERR_12611.setErrorCode(new Result());
		}
		
		return result;
		
	}

}
