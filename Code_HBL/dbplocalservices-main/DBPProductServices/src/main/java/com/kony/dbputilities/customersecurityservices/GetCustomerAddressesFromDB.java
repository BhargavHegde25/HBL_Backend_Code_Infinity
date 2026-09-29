package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerAddressResource;

public class GetCustomerAddressesFromDB implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		CustomerAddressResource addressResource = DBPAPIAbstractFactoryImpl.getResource(CustomerAddressResource.class);
		return addressResource.getCustomerAddress(methodID, inputArray, dcRequest, dcResponse);
	}
}