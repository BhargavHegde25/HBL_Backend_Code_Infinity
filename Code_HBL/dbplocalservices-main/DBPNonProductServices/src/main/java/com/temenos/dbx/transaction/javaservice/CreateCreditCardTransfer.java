package com.temenos.dbx.transaction.javaservice;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateCreditCardTransfer implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		//Log4j2Configurator.getInstance();

		// dummy Mock service until integrate with CMS.
        double amount=Double.parseDouble(request.getParameter("amount"));
		if(amount<=0)
			return ErrorCodeEnum. ERR_12301.setErrorCode(new Result());

		Result result = new Result();
        result.addParam(new Param("referenceId", String.format("%06d", new java.util.Date().getTime()%1000000)));
        result.addParam(new Param("status", "success"));
        result.addParam(new Param("message", "Success! Your transaction has been completed"));

        return result;
	}
}
