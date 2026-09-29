package com.temenos.dbx.forexservices.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.forexservices.dbservices.ForexCurrencyDBServices;

public class FetchForexRates implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		
		Result result;
			try {
				ForexCurrencyDBServices forexCurrencyDBServices = new ForexCurrencyDBServices();
				Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
				String baseCurrencyCode = inputParams.get("baseCurrencyCode") != null ? inputParams.get("baseCurrencyCode").toString() : null;
				String quoteCurrencyCode = inputParams.get("quoteCurrencyCode") != null ? inputParams.get("quoteCurrencyCode").toString() : null;
				String market = inputParams.get("market") != null ? inputParams.get("market").toString() : null;
				result = forexCurrencyDBServices.fetchRates(baseCurrencyCode,quoteCurrencyCode,market,request);
			}
			catch(Exception e) {
				alert.prepareError("Error occured while invoking FetchForexRates", e).log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		return result;
	}

}
