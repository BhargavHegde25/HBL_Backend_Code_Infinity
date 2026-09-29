package com.kony.adminconsole.sca.postprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import javax.naming.spi.DirStateFactory.Result;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class SCAMasterResetPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object execute(com.konylabs.middleware.dataobject.Result result, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		String masterResetResponseStr = ResultToJSON.convert(result);
		diagnostic.prepareDebug("SCA MasterReset service response: "+masterResetResponseStr).log();
		JsonParser jsonParser = new JsonParser();
		JsonObject masterResetResponse = (JsonObject) jsonParser.parse(masterResetResponseStr);
		try {
			if(masterResetResponse.getAsJsonPrimitive("responseCode").getAsInt() == 0) {
				Map<String, Object> payload = new HashMap<>();
				payload.put("Customer_id", request.getParameter("customerId"));
				DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
						.withServiceId("DBPServices")
						.withOperationId("sendActivationCode")
						.withRequestParameters(payload)
						.withPassThroughOutput(true)
						.build();
				String sendActivationCodeResponse = serviceExecutor.getResponse();
				diagnostic.prepareDebug("DBPServices.sendActivationCode response: "+sendActivationCodeResponse).log();
				result.appendJson(sendActivationCodeResponse);
			}else {
				diagnostic.prepareDebug("Not sending activation code as SCA MasterReset service failed").log();
			}
		}catch(Exception e) {
			diagnostic.prepareDebug("Exception: "+e).log();
			alert.prepareError("Error occurred: ", e).log();
		}
		
		return result;
	}
}
