package com.temenos.dbx.product.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.resource.api.CombinedUserResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CombinedUserPermissionsGetOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			CombinedUserResource combinedUserResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(CombinedUserResource.class);
			result = combinedUserResource.getCombinedUserPermissions(methodID, inputArray, dcRequest, dcResponse);
		} catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
			alert.prepareError("Exception occured while fetching combined user permissions", e).log();
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching combined user permissions", e).log();
		}
		return result;
	}
}
