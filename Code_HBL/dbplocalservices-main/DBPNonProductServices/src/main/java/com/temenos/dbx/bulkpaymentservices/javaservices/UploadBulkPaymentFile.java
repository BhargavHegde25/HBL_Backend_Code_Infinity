package com.temenos.dbx.bulkpaymentservices.javaservices;


import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.api.events.EventData;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;


public class UploadBulkPaymentFile implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();				
		try {					
			@SuppressWarnings("unchecked")
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);

			String confirmationNumber = String.valueOf(HelperMethods.getIdUsingCurrentTimeStamp());	
			String fileName = inputParams.get("fileName").toString();
			String sysGeneratedFileName = fileName.substring(0, fileName.lastIndexOf('.'))+ "_" + confirmationNumber + fileName.substring(fileName.lastIndexOf('.'));
			inputParams.put("fileId", confirmationNumber);
			inputParams.put("uploadedBy", CustomerSession.getCustomerId(customer));
			inputParams.put("sysGeneratedFileName", sysGeneratedFileName);

			EventData eventData = new EventData("bulkpayments/upload",inputParams);			
			request.getServicesManager().getEventNotifier().notify(eventData);
			result.addParam(new Param("confirmationNumber", confirmationNumber, MWConstants.STRING));
			result.addParam(new Param("sysGeneratedFileName", sysGeneratedFileName, MWConstants.STRING));
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking uploadBulkPaymentFile at Backend: ", e).log();
			return ErrorCodeEnum.ERR_21226.setErrorCode(new Result());
		}
		return result;
	}
}
