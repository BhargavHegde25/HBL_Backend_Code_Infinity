package com.kony.dbp.fileprocessingengine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.fileprocessingengine.HelperPackage.HelperMethods;
import com.kony.dbp.fileprocessingengine.ProcessFile.Constants;
import com.kony.dbp.fileprocessingenginedbutils.HikariConfiguration;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.OperationsEnum;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class FetchAndProcessFiles implements JavaService2 {

	private static Map<String, List<String>> fileeventmap = null;

	public static Map<String, List<String>> getFileeventmap() {
		return fileeventmap;
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	private Result fetchFileRecords(DataControllerRequest dcrequest, ServicesManager servicesManager) {

		Result result = new Result();
		try {
			OperationData operationData = servicesManager.getOperationDataBuilder()
					.withServiceId(FileProcessingEngineConstants.FIELPROCESSINGENGINE_SERVICEID)
					.withVersion(FileProcessingEngineConstants.FIELPROCESSINGENGINE_OBJECT_VERSION)
					.withObjectId(FileProcessingEngineConstants.FIELPROCESSINGENGINE_OBJECTID)
					.withOperationId(OperationsEnum.get.name()).build();

			ServiceRequest serviceRequest = servicesManager.getRequestBuilder(operationData).withDCRRequest(dcrequest)
					.build();
			String resultjson = serviceRequest.invokeServiceAndGetJson();
			result.addParam(new Param(ProcessFile.Constants.RESULTJSON.toString(), resultjson, "String"));
		} catch (AppRegistryException arex) {
			alert.prepareError("arex=", arex).log();
			result = HelperMethods.getErrorResult(1012, "Could not access FileProcessing service via app registry");
		} catch (Exception ex) {
			alert.prepareError("ex=", ex).log();
			result = HelperMethods.getErrorResult(ex);
		}
		return result;
	}

	private static Result fetchFileContents(DataControllerRequest dcrequest, ServicesManager servicesManager,
			Map<String, Object> inputMap) {

		Result result = new Result();
		try {
			OperationData operationData = servicesManager.getOperationDataBuilder()
					.withServiceId(FileProcessingEngineConstants.FIELPROCESSINGENGINE_SERVICEID)
					.withVersion(FileProcessingEngineConstants.FIELPROCESSINGENGINE_OBJECT_VERSION)
					.withObjectId(FileProcessingEngineConstants.FIELPROCESSINGENGINE_OBJECTID)
					.withOperationId(FileProcessingEngineConstants.FIELPROCESSINGENGINE_OPERATIONID).build();

			ServiceRequest serviceRequest = servicesManager.getRequestBuilder(operationData).withInputs(inputMap)
					.withDCRRequest(dcrequest).build();
			String resultjson = serviceRequest.invokeServiceAndGetJson();
			result.addParam(new Param(ProcessFile.Constants.RESULTJSON.toString(), resultjson, "String"));
		} catch (AppRegistryException arex) {
			alert.prepareError("arex=", arex).log();
			result = HelperMethods.getErrorResult(1012, "Could not access FileProcessing service via app registry");
		} catch (Exception ex) {
			alert.prepareError("ex=", ex).log();
			result = HelperMethods.getErrorResult(ex);
		}
		return result;
	}

	public static Map<String, String> getListOfFileIdFromRecords(JsonArray records) {
		Map<String, String> filenameidmap = new HashMap<>();
		for (JsonElement record : records) {
			if (record.getAsJsonObject().get(ProcessFile.Constants.FILEID.toString()) != null
					&& record.getAsJsonObject().get(ProcessFile.Constants.FILENAME.toString()).getAsString() != null) {
				filenameidmap.put(record.getAsJsonObject().get(ProcessFile.Constants.FILENAME.toString()).getAsString(),
						record.getAsJsonObject().get(ProcessFile.Constants.FILEID.toString()).getAsString());
			}
		}

		return filterFilesToFetchFromStorage(filenameidmap);
	}

	private static Map<String, JsonObject> fetchFileContentByFileIdProcess(DataControllerRequest dcrequest,
			ServicesManager servicesManager, Map<String, Object> inputMap, Map<String, String> files) {
		Map<String, JsonObject> filenamecotentmap = new HashMap<>();
		for (String filename : files.keySet()) {
			if (inputMap.containsKey(ProcessFile.Constants.FILEID.toString())) {
				inputMap.remove(ProcessFile.Constants.FILEID.toString());
			}
			inputMap.put(ProcessFile.Constants.FILEID.toString(), files.get(filename));

			JsonObject fileobject = new JsonObject();
			fileobject.addProperty(ProcessFile.Constants.FILEID.toString(), files.get(filename));
			fileobject.addProperty(ProcessFile.Constants.FILENAME.toString(), filename);
			alert.prepareError("calling getBinary").log();
			Result res = fetchFileContents(dcrequest, servicesManager, inputMap);

			if (res.getParamValueByName(ProcessFile.Constants.RESULTJSON.toString()) != null) {
				alert.prepareError(
						new JsonParser().parse(res.getParamValueByName(ProcessFile.Constants.RESULTJSON.toString()))
								.getAsJsonObject().toString()).log();
				fileobject = HelperMethods.mergeJsonObjects(fileobject, new JsonParser()
						.parse(res.getParamValueByName(ProcessFile.Constants.RESULTJSON.toString())).getAsJsonObject());
				filenamecotentmap.put(filename, fileobject);
			}
		}
		return filenamecotentmap;
	}

	private static HashMap<String, List<String>> getEventsFromDB() throws SQLException {
		HashMap<String, List<String>> fileeventmap = new HashMap<>();
		try (Connection con = HikariConfiguration.getconnection()) {
			try (PreparedStatement stmt = con
					.prepareStatement(FileProcessingEngineConstants.FIELPROCESSINGENGINE_QUERY_EVENTSUBTYPE)) {
				try (ResultSet res = stmt.executeQuery()) {
					while (res.next()) {
						try {
							if (res.getString(Constants.EVENTTYPEID.toString()) != null
									&& res.getString(FileProcessingEngineConstants.FIELPROCESSINGENGINE_DB_COL) != null
									&& res.getString(Constants.ID.toString()) != null
									&& !res.getString(FileProcessingEngineConstants.FIELPROCESSINGENGINE_DB_COL)
											.equals("")) {
								List<String> event = new ArrayList<>();
								event.add(res.getString(Constants.EVENTTYPEID.toString()));
								event.add(res.getString(Constants.ID.toString()));
								fileeventmap.put(
										res.getString(FileProcessingEngineConstants.FIELPROCESSINGENGINE_DB_COL),
										event);
							}
						} catch (Exception e) {
							alert.prepareError(e).log();
						}
					}
				}
			}
		}
		return fileeventmap;
	}

	private static Map<String, String> filterFilesToFetchFromStorage(Map<String, String> files) {

		Map<String, String> result = new HashMap<>();
		for (String filename : files.keySet()) {
			if (fileeventmap.containsKey(filename))
				result.put(filename, files.get(filename));
		}
		return result;
	}

	public Object invoke(String methodId, Object[] inputparams, DataControllerRequest dcrequest,
			DataControllerResponse dcresponse) throws Exception {
		HikariConfiguration.getDataSource(dcrequest);
		Map<String, Object> inputMap = new HashMap<>();
		if (fileeventmap == null)
			synchronized (FetchAndProcessFiles.class) {
				fileeventmap = getEventsFromDB();

			}

		Result result = fetchFileRecords(dcrequest, dcrequest.getServicesManager());

		String records = "{}";
		if (result.getParamValueByName(ProcessFile.Constants.RESULTJSON.toString()) != null)
			records = result.getParamValueByName(ProcessFile.Constants.RESULTJSON.toString());
		JsonObject recordsjson = new JsonParser().parse(records).getAsJsonObject();
		Map<String, String> files = getListOfFileIdFromRecords(
				recordsjson.get(ProcessFile.Constants.FILECAPS.toString()).getAsJsonArray());

		Map<String, JsonObject> filenamecontentmap = fetchFileContentByFileIdProcess(dcrequest,
				dcrequest.getServicesManager(), inputMap, files);

		result = ProcessFile.processListOfFiles(filenamecontentmap, dcrequest.getServicesManager(), dcrequest);

		return result;
	}

}
