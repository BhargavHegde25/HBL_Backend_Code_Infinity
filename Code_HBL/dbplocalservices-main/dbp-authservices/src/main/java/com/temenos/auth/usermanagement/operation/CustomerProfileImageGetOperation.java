package com.temenos.auth.usermanagement.operation;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerImageResource;

public class CustomerProfileImageGetOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		try {
			CustomerImageResource customerImageResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(CustomerImageResource.class);
			result = customerImageResource.getCustomerImage(methodID, inputArray, dcRequest, dcResponse);
		} catch (Exception e) {
			alert.prepareError("Exception occured while calling the customerImageResouce" + e.getMessage()).log();
		}
		return result;
	}

}
