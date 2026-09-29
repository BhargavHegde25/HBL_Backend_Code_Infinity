package com.infinity.dbx.temenos.bulkpaymentservices.javaservices;


import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
public class UploadBulkPaymentFileOperation implements JavaService2{

		
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {	
		Log4j2Configurator.getInstance();

		return null;
	}

}


