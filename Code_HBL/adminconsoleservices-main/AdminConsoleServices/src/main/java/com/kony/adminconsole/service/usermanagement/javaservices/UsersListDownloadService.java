package com.kony.adminconsole.service.usermanagement.javaservices;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UsersListDownloadService implements JavaService2 {

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception{
		Log4j2Configurator.getInstance();
		Result result =new Result();
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
			String fileId= queryParamsMap.get("fileId");
			String searchText = (String) MemoryManager.getFromCache("searchText");
			String response = (String) MemoryManager.getFromCache(fileId);
			JSONArray internalUsers= CommonUtilities.getStringAsJSONArray(response);
			boolean isKeyCloakEnabled = Boolean
					.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance));
			if (isKeyCloakEnabled) {
				StringBuilder responseCsvBuilder = new StringBuilder();
				CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT.withHeader("Name", "Username", "Email", "Status")
						.print(responseCsvBuilder);
				if(StringUtils.isNotBlank(response)) {
					for (int i = 0; i < internalUsers.length(); ++i) {

						String nameColumn = internalUsers.getJSONObject(i).optString("FirstName") + " "
								+ internalUsers.getJSONObject(i).optString("LastName");
						String userNameColumn = internalUsers.getJSONObject(i).optString("Username");
						String emailColumn = internalUsers.getJSONObject(i).optString("Email");
						String statusColumn = getStatusDes(internalUsers.getJSONObject(i).optString("Status_id"),
								requestInstance);
						if (searchText == null
								|| (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())
										|| userNameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
							responseCsvPrinter.printRecord(nameColumn, userNameColumn, emailColumn, statusColumn);
						}
					}
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.DOWNLOADFILE,
							ActivityStatusEnum.SUCCESSFUL, "Users file download successful");

					Map<String, String> customHeaders = new HashMap<String, String>();
					customHeaders.put("Content-Type", "text/plain; charset=utf-8");
					customHeaders.put("Content-Disposition", "attachment; filename=\"Users_List.csv\"");

					responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
							new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
					responseInstance.getHeaders().putAll(customHeaders);
					responseInstance.setStatusCode(HttpStatus.SC_OK);
				}else {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.DOWNLOADFILE,
								ActivityStatusEnum.FAILED, "Users file download failed");
					}
				}
			else {
				StringBuilder responseCsvBuilder = new StringBuilder(); // Contains the text for response CSV file
				CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
						.withHeader("Name", "Username", "Email", "Role", "No. of Permissions", "Status")
						.print(responseCsvBuilder);
				if(StringUtils.isNotBlank(response)) {
				for (int i = 0; i < internalUsers.length(); ++i) {

					String nameColumn = internalUsers.getJSONObject(i).optString("FirstName") + " "
							+ internalUsers.getJSONObject(i).optString("LastName");
					String userNameColumn = internalUsers.getJSONObject(i).optString("Username");
					String emailColumn = internalUsers.getJSONObject(i).optString("Email");
					String roleColumn = internalUsers.getJSONObject(i).optString("Role_Name");
					String permissionCountColumn = internalUsers.getJSONObject(i).optString("Permission_Count");
					String statusColumn = internalUsers.getJSONObject(i).optString("Status_Desc");

					if (searchText == null
							|| (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())
									|| userNameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
						responseCsvPrinter.printRecord(nameColumn, userNameColumn, emailColumn, roleColumn,
								permissionCountColumn, statusColumn);
					}
				}

				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.DOWNLOADFILE,
						ActivityStatusEnum.SUCCESSFUL, "Users file download successful");

				Map<String, String> customHeaders = new HashMap<String, String>();
				customHeaders.put("Content-Type", "text/plain; charset=utf-8");
				customHeaders.put("Content-Disposition", "attachment; filename=\"Users_List.csv\"");

				responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
						new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
				responseInstance.getHeaders().putAll(customHeaders);
				responseInstance.setStatusCode(HttpStatus.SC_OK);
			} else {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.DOWNLOADFILE,
						ActivityStatusEnum.FAILED, "Users file download failed");
			}
			}
			MemoryManager.removeFromCache(fileId);
		}catch(Exception e) {
			 
			ErrorCodeEnum.ERR_20687.setErrorCode(result);
	        String errorMessage = "Failed to download locations list. Please contact administrator.";
	        CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
		}
		return result;
	}
	public String getStatusDes(String Status_id, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + Status_id + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.STATUS_READ, postParametersMap, null,
				requestInstance);
		JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readEndpointResponse);
		String attributeValue = responseJSON.getJSONArray("status").getJSONObject(0).getString("Description");
		return attributeValue;

	}
}
