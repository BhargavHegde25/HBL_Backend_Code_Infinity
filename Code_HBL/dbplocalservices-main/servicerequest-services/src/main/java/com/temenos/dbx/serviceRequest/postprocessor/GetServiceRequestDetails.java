package com.temenos.dbx.serviceRequest.postprocessor;

import java.util.ArrayList;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import org.json.JSONObject;

public class GetServiceRequestDetails implements DataPostProcessor2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		//to mask the cardpin number 
		ArrayList<Record> record =result.getDatasetById("serviceReqs").getRecords();
		for (int i = 0; i < record.size(); i++) {
			String requestConfigId=record.get(i).getParamValueByName("requestConfigId");
			if(requestConfigId.equalsIgnoreCase("ApplyDebitCard")) {
				Record cardDetails=record.get(i).getRecordById("serviceReqRequestIn");
				if(StringUtils.isNotBlank(cardDetails.toString())) {
					if(cardDetails.hasParamByName("pinNumber")) {
						cardDetails.addParam("pinNumber", cardDetails.getParamValueByName("pinNumber").replaceAll("[0-9]", "*"));
					}
				}
			}
			
		}
		return result;
	}

}
