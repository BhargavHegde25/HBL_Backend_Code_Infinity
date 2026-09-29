package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.UserManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class SendEmailToResetPasswordCredentialsOnSearchOperation implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

		
		 Result result = new Result();
	        try {
	            UserManagementResource userManagementResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(UserManagementResource.class);
	           result = userManagementResource.sendMailToCustomerOnSearch(methodID, inputArray, dcRequest, dcResponse);
	        } catch(ApplicationException e) {
	        	e.getErrorCodeEnum().setErrorCode(result);
	        	alert.prepareError("Exception occured while sending email to customer",  e).log();
	        }catch (Exception e) {
	            alert.prepareError("Exception occured while sending email to customer",  e).log();
	        }

	        return result;
	}

}
