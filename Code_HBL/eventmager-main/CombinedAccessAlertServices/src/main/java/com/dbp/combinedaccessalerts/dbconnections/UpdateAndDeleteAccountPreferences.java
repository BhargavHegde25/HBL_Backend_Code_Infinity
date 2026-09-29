package com.dbp.combinedaccessalerts.dbconnections;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.combinedaccessalerts.service.ActivateCombinedAccess;
import com.dbp.combinedaccessalerts.util.Constants;
import com.dbp.combinedaccessalerts.util.HelperMethods;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;

public class UpdateAndDeleteAccountPreferences {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static boolean updateAndDelete(String newCustomerId, String deactivatedccustomerId,
			DataControllerRequest request) {

		Map<String, Object> inputparams = new HashMap<String, Object>();
		inputparams.put(Constants.NEWCUSTOMERID, newCustomerId);
		inputparams.put(Constants.ACCOUNT_ID_DEL, Constants.STAR);
		inputparams.put(Constants.ACCOUNTTYPE_DEL, Constants.STAR);
		inputparams.put(Constants.DEACTIVATEDCUSTOMERID, deactivatedccustomerId);
		diagnostic.prepareDebug("inputparams " + inputparams).log();
		try {
			
			OperationData operationData = request.getServicesManager().getOperationDataBuilder()
					.withServiceId(Constants.DBSERVICE).withVersion("1.0")
					.withOperationId(HelperMethods.replaceSchemaName(Constants.UPDATEANDDELETEOPERATION, ActivateCombinedAccess.getSchemaName())).build();
			ServiceRequest serviceRequest = request.getServicesManager().getRequestBuilder(operationData)
					.withInputs(inputparams).withHeaders(request.getHeaderMap()).build();
			Result result = serviceRequest.invokeServiceAndGetResult();
			diagnostic.prepareDebug("res " + ResultToJSON.convert(result)).log();

		} catch (Exception e) {
			alert.prepareError("Exception occurred while activating combined access", e).log();
			return false;
		}
		return true;
	}
}
