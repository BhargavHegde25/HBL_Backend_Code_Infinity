package com.kony.adminconsole.service.productmanagement.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.productmanagement.resource.api.ProductResource;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class LoadProductsService implements JavaService2{

	
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
	 Log4j2Configurator.getInstance();
	 
		ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
        Result result = productResource.loadProductInformation(methodID, inputArray, requestInstance,
                responseInstance);
	 
	 return  result;
	}
}