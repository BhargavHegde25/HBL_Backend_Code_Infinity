package com.temenos.dbx.product.payeeservices.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.payeeservices.resource.api.IntraBankPayeeResource;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class DeleteIntraBankPayeeOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();

        try {
            //Initializing of IntraBankPayeeResource through Abstract factory method
        	IntraBankPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(IntraBankPayeeResource.class);

            result  = payeeResource.deletePayee(methodID, inputArray, request, response);
        }
        catch(Exception e) {
            alert.prepareError("Error occured while invoking fetchAllMyPayees: ", e).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }

        return result;
    }
}
