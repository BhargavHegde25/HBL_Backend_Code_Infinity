package com.temenos.dbx.product.transactionservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.transactionservices.resource.api.IntraBankFundTransferResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author KH2626 
 * @version 1.0
 * Java service end-point to update any transaction entry in intrabanktransaction table.
 **/

public class UpdateIntraBankTransactionStatusAtDBXOperation implements JavaService2  {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		
		Result result = new Result();
		
		try {
			//Initialize resource required to update the intrabanktransfers table
			IntraBankFundTransferResource intrabankTransactionResource = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
					.getResource(IntraBankFundTransferResource.class);
			result = intrabankTransactionResource.updateStatus(methodID,inputArray,request,response);
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while updating intrabanktransaction",exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
}