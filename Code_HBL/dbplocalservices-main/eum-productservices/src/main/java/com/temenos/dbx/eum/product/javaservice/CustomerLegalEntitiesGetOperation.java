package com.temenos.dbx.eum.product.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.resource.api.ProfileManagementResource;

public class CustomerLegalEntitiesGetOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception  {
		Log4j2Configurator.getInstance();

		 Result result = new Result();
	        try {
	            ProfileManagementResource resource = DBPAPIAbstractFactoryImpl.getResource(ProfileManagementResource.class);
	            result = resource.customerLegalEntitiesGet(methodID, inputArray, dcRequest, dcResponse);
	        }  catch (ApplicationException e) {
	            e.getErrorCodeEnum().setErrorCode(result);
	        } catch (Exception e) {
	        	alert.prepareError("Error occured",e).log();
	            ErrorCodeEnum.ERR_29054.setErrorCode(result);
	        }

	        return result;
	}
	
}