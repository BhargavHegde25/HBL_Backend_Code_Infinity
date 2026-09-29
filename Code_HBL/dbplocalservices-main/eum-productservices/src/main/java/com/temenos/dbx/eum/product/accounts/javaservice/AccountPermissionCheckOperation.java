package com.temenos.dbx.eum.product.accounts.javaservice;


import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.accounts.resource.api.AccountsResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * @author kaushik.mondal(KH2691)
 *
 */

public class AccountPermissionCheckOperation implements JavaService2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = null;
		
		try {
			
            AccountsResource accountsResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(AccountsResource.class);
            result = accountsResource.checkIfAccountPermissionEnabled(methodId, inputArray, request, response);
        
		}catch (Exception e) {
        	
            alert.prepareError("Exception occured while checking permission of the given account "+ e.getMessage()).log();
            result =new Result();
            ErrorCodeEnum.ERR_29009.setErrorCode(result);
        }
		
		
		return result;
	}

}
