/**
 * 
 */
package com.temenos.dbx.product.combinedaccess.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.combinedaccess.resource.api.UserLinkingResource;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * @author muthukumarv
 *
 */
public class UserLinkingOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			UserLinkingResource userLinkingResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(UserLinkingResource.class);
			result = userLinkingResource.userLinkingOperation(methodID, inputArray, request, response);
		} catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
			alert.prepareError("Exception occured while userlinking:" + e.getMessage(), e).log();
		} catch (Exception e) {
			HelperMethods.setValidationMsgwithCode("Linking operation Failed", ErrorCodes.ERROR_UPDATING_RECORD, result);
			alert.prepareError("Exception occured while userlinking:" + e.getMessage(), e).log();
		}

		return result;
	}

}
