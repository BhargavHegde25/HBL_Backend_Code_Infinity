package com.kony.adminconsole.service.currency;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CurrencyConversionService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String INPUT_AMOUNT = "amount";
	private static final String INPUT_FROM_CURRENCY = "fromCurrency";
	private static final String INPUT_TO_CURRENCY = "toCurrency";
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result =new Result();
		String amount = StringUtils.EMPTY;
		String fromCurrency = StringUtils.EMPTY;
		String toCurrency = StringUtils.EMPTY;
		try {
			
			amount = request.getParameter(INPUT_AMOUNT);
			fromCurrency = request.getParameter(INPUT_FROM_CURRENCY);
			toCurrency = request.getParameter(INPUT_TO_CURRENCY);
			if (StringUtils.isAnyBlank(amount, fromCurrency, toCurrency)) {
	            ErrorCodeEnum.ERR_20541.setErrorCode(result);
	            return result;
	        }
			
			String serviceResponse =
	                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CURRENCY)
	                        .withOperationId(OperationName.OP_GET_CONVERSION_RATE).withRequestHeaders(null)
	                        .withRequestParameters(null).build()
	                        .getResponse();
			JSONObject conversionRateJsonObj = CommonUtilities.getStringAsJSONObject(serviceResponse);
			
			String conversionRateKey = fromCurrency.toUpperCase() + "To" + toCurrency.toUpperCase();
			
			if(null != conversionRateJsonObj && conversionRateJsonObj.has("CurrencyConversionRate") 
					&& conversionRateJsonObj.getJSONObject("CurrencyConversionRate").has(conversionRateKey)	) {
				String  conversionRate = conversionRateJsonObj.getJSONObject("CurrencyConversionRate").getString(conversionRateKey);
				double actualAmount = Double.parseDouble(amount);
				double rate = Double.parseDouble(conversionRate);
				double convertedAmount = actualAmount * rate;
				String convertedAmountStr = String.format("%100.2f", convertedAmount).trim();
				result.addParam("convertedAmount", convertedAmountStr);
			}else {
				
				alert.prepareError("Currency conversion failed: "+conversionRateJsonObj).log();
				ErrorCodeEnum.ERR_22123.setErrorCode(result);
			}
		
		} catch(Exception exp) {
			alert.prepareError("Enountered Exception while trying to covert the currency: "+exp).log();
			ErrorCodeEnum.ERR_22123.setErrorCode(result);
		}
		
		return result;
	}

}
