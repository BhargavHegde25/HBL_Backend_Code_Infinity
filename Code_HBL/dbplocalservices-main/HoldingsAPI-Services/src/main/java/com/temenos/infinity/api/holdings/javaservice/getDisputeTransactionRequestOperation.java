package com.temenos.infinity.api.holdings.javaservice;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.holdings.resource.api.DisputeTransactionResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class getDisputeTransactionRequestOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
			try {
				//Initializing of CardsResource through Abstract factory method
				DisputeTransactionResource disputeTransactionresource = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(ResourceFactory.class).getResource(DisputeTransactionResource.class);
				result  = disputeTransactionresource.getDisputeTransactionRequests(methodId, inputArray, request, response);
			}
		 catch (Exception e) {
			 alert.prepareError("Exception occured invoking the service " + e).log();
			 return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			 
		}
		return result;
	}
}