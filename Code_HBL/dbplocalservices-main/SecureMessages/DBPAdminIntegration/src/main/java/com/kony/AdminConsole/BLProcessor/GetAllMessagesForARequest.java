package com.kony.AdminConsole.BLProcessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAllMessagesForARequest implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		ServicesManager serviceManager = ServicesManagerHelper.getServicesManager();
		ConfigurableParametersHelper configurableParametersHelper = serviceManager.getConfigurableParametersHelper();
		String isDMSIntegrationEnabled = configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED");
		if (StringUtils.isBlank(isDMSIntegrationEnabled)) {
			isDMSIntegrationEnabled = "false";
		}
		String request_id = requestInstance.getParameter("request_id");
		Map<String, Object> inputparams = CommonUtilities.getInputMapFromInputArray(inputArray);
		String userName = (String) inputparams.get("username");
		JSONObject getResponse = getAllMessagesForARequest(userName, request_id, requestInstance);

		if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
			String authToken = AdminConsoleOperations.login(requestInstance);
			ServiceConfig.setValue("Auth_Token", authToken);
			getResponse = getAllMessagesForARequest(userName, request_id, requestInstance);
		}
		if (isDMSIntegrationEnabled.equalsIgnoreCase("true")) {
			fetchMediaFromDMS(getResponse, requestInstance);
		}
		Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
		return processedResult;

	}

	public void fetchMediaFromDMS(JSONObject messagesResponse, DataControllerRequest requestInstance) {
		JSONArray messagesArray = messagesResponse.getJSONArray("messages");

		for (int i = 0; i < messagesArray.length(); i++) {
			String messageId = messagesArray.getJSONObject(i).getString("Message_id");
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("referenceId", messageId);
			String searchAttachmentsResult = HelperMethods.invokeServiceAndGetString(requestInstance, dataMap,
					HelperMethods.getHeaders(requestInstance), "DocumentStorageSearch");
			if (StringUtils.isNotBlank(searchAttachmentsResult)) {
				JSONObject AttachmentObject = new JSONObject(searchAttachmentsResult);
				if (AttachmentObject.has("documentsList")) {
					JSONArray attachments = new JSONArray();
					JSONArray docAttachments = null;
					if (AttachmentObject.has("documentsList")) {
						docAttachments = new JSONArray(AttachmentObject.get("documentsList").toString());
					}
					if (docAttachments != null) {
						for (int j = 0; j < docAttachments.length(); j++) {
							JSONObject attachment = new JSONObject();
							JSONObject singleAttachementObject = docAttachments.getJSONObject(j);
							String fileInfo = singleAttachementObject.has("fileInfo")
									? singleAttachementObject.getString("fileInfo")
									: "{}";
							JSONObject fileInfoObject = new JSONObject(fileInfo);
							if (fileInfoObject.has("size")) {
								attachment.put("Size", fileInfoObject.get("size"));
							}
							if (fileInfoObject.has("type")) {
								attachment.put("type", getFileType(fileInfoObject.get("type").toString()));
							}
							if (singleAttachementObject.has("documentId")) {
								attachment.put("media_Id", singleAttachementObject.get("documentId"));
							}
							if (singleAttachementObject.has("fileName")) {
								attachment.put("Name", singleAttachementObject.get("fileName"));
							}
							attachments.put(attachment);
						}
					}
					messagesArray.getJSONObject(i).put("attachments", attachments);
					messagesArray.getJSONObject(i).put("totalAttachments", attachments.length());
				}
			}
		}
	}

	private String getFileType(String type) {
		String fileType = null;
		switch (type) {
		case "application/pdf":
		case "pdf":
			fileType = "ATTACH_TYPE_PDF";
			break;
		case "text/plain":
		case "txt":
			fileType = "ATTACH_TYPE_TXT";
			break;
		case "image/jpeg":
		case "jpeg":
		case "jpg":
			fileType = "ATTACH_TYPE_JPEG";
			break;
		case "application/msword":
		case "doc":
			fileType = "ATTACH_TYPE_DOC";
			break;
		case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
		case "docx":
			fileType = "ATTACH_TYPE_DOCX";
			break;
		case "image/png":
		case "png":
			fileType = "ATTACH_TYPE_PNG";
			break;
		}
		return fileType;
	}

	public JSONObject getAllMessagesForARequest(String username, String request_id, DataControllerRequest dcRequest) {

		Map<String, Object> postParametersMap = new HashMap<>();
		postParametersMap.put("requestid", request_id);
		postParametersMap.put("username", username);
		String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
				new HashMap<>(), "GetAllMessagesURL");
		return CommonUtilities.getStringAsJSONObject(getResponseString);
	}

}
