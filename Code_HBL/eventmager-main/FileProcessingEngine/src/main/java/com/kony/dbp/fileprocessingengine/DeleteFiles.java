package com.kony.dbp.fileprocessingengine;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbp.fileprocessingengine.HelperPackage.HelperMethods;
import com.kony.dbp.fileprocessingengine.httputils.HttpCallException;
import com.kony.dbp.fileprocessingengine.httputils.HttpHelperMethods;
import com.google.gson.JsonObject;
import com.kony.dbp.fileprocessingengine.ProcessFile.Constants;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.OperationsEnum;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class DeleteFiles {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static Result callDeleteFileService(DataControllerRequest dcrequest, ServicesManager servicesManager,
			Map<String, Object> inputMap, Map<String, Object> headerMap) {

		Result result = new Result();
		try {
			OperationData operationData = servicesManager.getOperationDataBuilder()
					.withServiceId(FileProcessingEngineConstants.FIELPROCESSINGENGINE_SERVICEID)
					.withVersion(FileProcessingEngineConstants.FIELPROCESSINGENGINE_OBJECT_VERSION)
					.withObjectId(FileProcessingEngineConstants.FIELPROCESSINGENGINE_OBJECTID)
					.withOperationId(OperationsEnum.delete.name()).build();

			ServiceRequest serviceRequest = servicesManager.getRequestBuilder(operationData).withInputs(inputMap)
					.withDCRRequest(dcrequest).build();
			result = serviceRequest.invokeServiceAndGetResult();
		} catch (AppRegistryException arex) {
			alert.prepareError("arex=", arex).log();
			result = HelperMethods.getErrorResult(1012, "Could not access FileProcessing service via app registry");
		} catch (Exception ex) {
			alert.prepareError("ex=", ex).log();
			result = HelperMethods.getErrorResult(ex);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public static JsonObject deleteFileServiceExternal(Map<String, Object> inputMap, Map<String, Object> headerMap,
			DataControllerRequest dcrequest) throws HttpCallException {

		String baseurl = HttpHelperMethods.getBaseURL(dcrequest);
		String authtoken = HttpHelperMethods.getAuthenticationKey(dcrequest);
		if (baseurl == null || authtoken == null)
			return new JsonObject();
		String url = baseurl + FileProcessingEngineConstants.FILEPROCESSINGENGINE_DELETE_URL;
		url = url + "?" + Constants.DOLLARFILTER.toString() + "="
				+ URLEncoder.encode(inputMap.get(Constants.DOLLARFILTER.toString()).toString());
		inputMap.clear();
		JsonObject res = new JsonObject();
		headerMap.put("X-Kony-Authorization", authtoken);
		headerMap.put("Content-Type", "application/json");
		try {
			res = HttpHelperMethods.callhttpDeleteApi(inputMap, headerMap, url);
		} catch (Exception ex) {
			alert.prepareError("Error cooured", ex).log();
		}
		return res;
	}

	public static Result deleteFileWithInputMap(DataControllerRequest dcrequest, ServicesManager servicesmanager,
			String fileid) {
		
		Map<String, Object> inputmap = new HashMap<String, Object>();
		Map<String, Object> headermap = new HashMap<String, Object>();
		String fileidst = Constants.FILEID.toString() + Constants.EQUALSTO.toString() + fileid;
		inputmap.put(Constants.DOLLARFILTER.toString(), fileidst);
		// If service manager able to handle file deletion, we have to call this function
		/*Result result = new Result();
		result = callDeleteFileService(dcrequest, servicesmanager, inputmap,headermap);
		*/
		JsonObject resobj = new JsonObject();
		try {
			resobj = deleteFileServiceExternal(inputmap, headermap, dcrequest);
		} catch (Exception e) {
			alert.prepareError("error occured", e).log();
		}
		return JSONToResult.convert(resobj.toString());
	}
}
