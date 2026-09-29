package com.temenos.dbx.product.transactionservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.transactionservices.resource.api.BulkWireTransactionsResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2264
 * @version 1.0
 * Java Service end point to fetch all the transactions of a file
 */
public class GetBulkWireTemplateTransactionDetail implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		try {
			//Initializing of BulkWireTransactionsResource through Abstract factory method
			BulkWireTransactionsResource bulkWireTransactionsResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(BulkWireTransactionsResource.class);
			
		    result  = bulkWireTransactionsResource.getBulkWireTemplateTransactionDetail(methodID, inputArray, request, response);
		}
		catch(Exception e)
		{
			alert.prepareError("Error occured while fetching  Bulk wire transactions for the template: ", e).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
}
