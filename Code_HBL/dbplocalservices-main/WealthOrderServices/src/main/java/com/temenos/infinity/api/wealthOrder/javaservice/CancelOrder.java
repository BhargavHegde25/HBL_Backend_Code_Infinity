/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthOrder.resource.api.CancelOrderResource;

/**
 * @author balaji.kk
 *
 */
public class CancelOrder implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			CancelOrderResource cancelOrderResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(CancelOrderResource.class);
			result = cancelOrderResource.cancelOrder(methodId, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Exception in invoke of GetOrdersDetails:",e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
		return result;
	}

}
