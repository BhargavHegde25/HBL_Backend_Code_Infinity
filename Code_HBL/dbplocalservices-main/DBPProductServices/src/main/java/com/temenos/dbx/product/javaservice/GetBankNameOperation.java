package com.temenos.dbx.product.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.resource.api.BankResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetBankNameOperation implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		
		BankResource bankResource = DBPAPIAbstractFactoryImpl.getResource(BankResource.class);
		
		return bankResource.getBankName(methodID, inputArray, dcRequest, dcResponse);
		
	}

}
