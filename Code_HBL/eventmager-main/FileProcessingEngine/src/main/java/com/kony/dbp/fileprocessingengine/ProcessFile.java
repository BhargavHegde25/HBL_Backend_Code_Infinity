package com.kony.dbp.fileprocessingengine;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.fileprocessingengine.HelperPackage.EnvironmentConfigurationsHandler;
import com.kony.dbp.fileprocessingengine.HelperPackage.HelperMethods;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class ProcessFile {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public enum Constants {
		EVENTDISPATCHERSERVERDATE("eventDispatcherServerDate"), CUSTOMPARAMS("customParams"), EVENTTYPE(
				"eventType"), EVENTSUBTYPE("eventSubType"), STATUS("status"), OTHERDATA("otherData"), EVENTDATA(
						"eventData"), TOKEN("token"), EVENTS("events"), PRODUCER("producer"), FILENAME(
								"file_name"), FILEID("file_id"), DATA("data"), EVENTTYPEID("eventtypeid"), ID(
										"id"), FILE("file"), RESULTJSON("resultjson"), FILECAPS("File"), DOLLARFILTER(
												"$filter"), BATCHSUCCESSCOUNT("batchSuccessCount"), BATCHFAILURECOUNT(
														"batchFailureCount"), BATCHSIZE("batchSize"), DELETEDFILESID(
																"deletedFilesID"), EQUALSTO(" eq "), TOTALRECORDS(
																		"totalRecords"), SUCCESS(
																				"success"), TRUE("true");
		private String name;

		Constants(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static String covertBase64ToRaw(String encodedString) {

		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		String decodedString = new String(decodedBytes);

		return decodedString;
	}

	public static JsonArray createPayload(String eventtype, String eventsubtype, String decodeddata,
			Map<String, JsonObject> decodedfilesmap, String filename) {

		JsonArray payloadarray = new JsonArray();
		String[] rows = decodeddata.split("\n");

		int colcount = 0;
		if (rows.length > 1)
			colcount = rows[0].split(",").length;

		String[][] filedata = new String[rows.length][colcount];

		for (int i = 0; i < rows.length; i++)
			filedata[i] = rows[i].split(",");

		for (int i = 1; i < rows.length; i++) {
			JsonObject eventdetails = new JsonObject();
			JsonObject eventdata = new JsonObject();
			eventdata.addProperty(Constants.FILEID.toString(),
					decodedfilesmap.get(filename).getAsJsonObject().get(Constants.FILEID.toString()).getAsString());
			eventdata.addProperty(Constants.FILENAME.toString(), filename);
			eventdetails.addProperty(Constants.EVENTTYPE.toString(), eventtype);
			eventdetails.addProperty(Constants.EVENTSUBTYPE.toString(), eventsubtype);
			eventdetails.add(Constants.EVENTDATA.toString(), eventdata);
			JsonObject otherdata = new JsonObject();
			for (int j = 0; j < colcount; j++) {
				String key = filedata[0][j].replaceAll("\r|\t|\n", "");
				String value = filedata[i][j].replaceAll("\r|\t|\n", "");
				otherdata.addProperty(key, value);
			}
			eventdetails.add(Constants.OTHERDATA.toString(), otherdata);
			payloadarray.add(eventdetails);
		}
		return payloadarray;
	}

	private static Result addQueueMasterPayloadAndCall(JsonArray alldatapayload, ServicesManager servicesmanager) {
		HashMap<String, Object> inputmap = new HashMap<>();
		HashMap<String, Object> headermap = new HashMap<>();
		inputmap.put(Constants.EVENTS.toString(), alldatapayload);
		inputmap.put(Constants.TOKEN.toString(), HelperMethods.deriveToken(servicesmanager, alldatapayload.toString()));
		inputmap.put(Constants.PRODUCER.toString(), ProcessFile.class);
		return DispatchEvents.callQueueMaster(servicesmanager, inputmap, headermap);
	}

	public static Map<String, JsonArray> getDispatchPayload(Map<String, JsonObject> decodedfilesmap) {
		Map<String, List<String>> fileeventmap = FetchAndProcessFiles.getFileeventmap();
		Map<String, JsonArray> idpayloadsmap = new HashMap<>();

		for (String filename : decodedfilesmap.keySet()) {
			String eventtype = fileeventmap.get(filename).get(0);
			String eventsubtype = fileeventmap.get(filename).get(1);
			JsonArray payloadarray = createPayload(eventtype, eventsubtype,
					decodedfilesmap.get(filename).getAsJsonObject().get(Constants.DATA.toString()).getAsString(),
					decodedfilesmap, filename);
			idpayloadsmap.put(
					decodedfilesmap.get(filename).getAsJsonObject().get(Constants.FILEID.toString()).getAsString(),
					payloadarray);
		}
		return idpayloadsmap;
	}

	private static Result processBatchAndCallQueueMaster(Map<String, JsonArray> idpayloadarraymap,
			ServicesManager servicesmanager, DataControllerRequest dcrequest) {
		int batchLimit = 100;
		try {
			batchLimit = Integer.parseInt(EnvironmentConfigurationsHandler
					.getValue(FileProcessingEngineConstants.FILE_PROCESSING_ENGINE_BATCHLIMIT, servicesmanager));
		} catch (Exception e) {
		}
		String deletedfiles = "";
		int count = 0;
		int successcount = 0;
		int failcount = 0;

		JsonArray batchPayload = new JsonArray();

		for (String fileid : idpayloadarraymap.keySet()) {
			int tempfailflag = 0;
			for (JsonElement element : idpayloadarraymap.get(fileid)) {
				count += 1;
				batchPayload.add(element.getAsJsonObject());
				if (count == batchLimit) {
					Result res = addQueueMasterPayloadAndCall(batchPayload, servicesmanager);
					count = 0;
					batchPayload = new JsonArray();
					if (res.getParamByName(Constants.SUCCESS.toString()) != null
							&& res.getParamValueByName(Constants.SUCCESS.toString()) != null
							&& res.getParamValueByName(Constants.SUCCESS.toString())
									.equalsIgnoreCase(Constants.TRUE.toString()))
						successcount += 1;
					else {
						failcount += 1;
						tempfailflag = 1;
					}
				}
			}
			if (count > 0) {
				Result res = addQueueMasterPayloadAndCall(batchPayload, servicesmanager);
				if (res.getParamByName(Constants.SUCCESS.toString()) != null
						&& res.getParamValueByName(Constants.SUCCESS.toString()) != null
						&& res.getParamValueByName(Constants.SUCCESS.toString())
								.equalsIgnoreCase(Constants.TRUE.toString()))
					successcount += 1;
				else {
					failcount += 1;
					tempfailflag = 1;
				}
			}
			if (tempfailflag == 0) {
				Result res = DeleteFiles.deleteFileWithInputMap(dcrequest, servicesmanager, fileid);
				if (res.getParamValueByName(Constants.TOTALRECORDS.toString()) != null
						&& res.getParamValueByName(Constants.TOTALRECORDS.toString()).equals("1")) {
					deletedfiles += (fileid + ", ");
				}
			}
		}

		Result result = new Result();
		result.addStringParam(Constants.BATCHSUCCESSCOUNT.toString(), Integer.toString(successcount));
		result.addStringParam(Constants.BATCHFAILURECOUNT.toString(), Integer.toString(failcount));
		result.addStringParam(Constants.BATCHSIZE.toString(), Integer.toString(batchLimit));
		result.addStringParam(Constants.DELETEDFILESID.toString(), deletedfiles);
		return result;
	}

	public static Result processListOfFiles(Map<String, JsonObject> filenamecontentmap, ServicesManager servicesmanager,
			DataControllerRequest dcrequest) {
		Map<String, JsonObject> decodedfilesmap = new HashMap<>();
		for (String key : filenamecontentmap.keySet()) {
			JsonObject encodedobj = filenamecontentmap.get(key).getAsJsonObject();
			if (encodedobj.get(Constants.DATA.toString()) != null) {
				String decodeddata = covertBase64ToRaw(encodedobj.get(Constants.DATA.toString()).getAsString());
				encodedobj.remove(Constants.DATA.toString());
				encodedobj.addProperty(Constants.DATA.toString(), decodeddata);
				decodedfilesmap.put(key, encodedobj);
			}
		}
		alert.prepareError("decodedfiles=" + decodedfilesmap.toString()).log();
		Map<String, JsonArray> idpayloadarraymap = getDispatchPayload(decodedfilesmap);
		return processBatchAndCallQueueMaster(idpayloadarraymap, servicesmanager, dcrequest);
	}

	public static void main(String[] args) {
		String encoded = "";
		System.out.println(covertBase64ToRaw(encoded));
		String fileoutput = covertBase64ToRaw(encoded);
		// JsonArray payload = createPayload("LOGIN", "LOGIN_ATTEMPT", fileoutput,
		// null);
		// for (JsonElement ele : payload) {
		// System.out.println(ele);
		// }
	}
}
