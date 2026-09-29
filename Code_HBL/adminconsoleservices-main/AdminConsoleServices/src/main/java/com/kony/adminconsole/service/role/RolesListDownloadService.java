package com.kony.adminconsole.service.role;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class RolesListDownloadService implements JavaService2{
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception{
		Result result = new Result();
		try {
		@SuppressWarnings("unchecked")
		Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
		String fileId= queryParamsMap.get("fileId");
		String response = (String) MemoryManager.getFromCache(fileId);
		JSONArray roles= CommonUtilities.getStringAsJSONArray(response);
		String searchText = (String) MemoryManager.getFromCache("searchText");
		StringBuilder responseCsvBuilder = new StringBuilder(); // Contains the text for response CSV file
        CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
                .withHeader("Name", "Description", "Users", "Permissions", "Status").print(responseCsvBuilder);
        if(StringUtils.isNotBlank(response)) {
		for (int i = 0; i < roles.length(); ++i) {

            String nameColumn = roles.getJSONObject(i).optString("role_Name");
            String descriptionColumn = roles.getJSONObject(i).optString("role_Desc");
            String usersCountColumn = roles.getJSONObject(i).optString("Users_Count");
            String permissionCountColumn = roles.getJSONObject(i).optString("permission_Count");
            String statusColumn = roles.getJSONObject(i).optString("Status");

            if (searchText == null
                    || (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
                responseCsvPrinter.printRecord(nameColumn, descriptionColumn, usersCountColumn,
                        permissionCountColumn, statusColumn);
            }
        }
		Map<String, String> customHeaders = new HashMap<String, String>();
        customHeaders.put("Content-Type", "text/plain; charset=utf-8");
        customHeaders.put("Content-Disposition", "attachment; filename=\"Roles_List.csv\"");
        responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
                new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
        responseInstance.getHeaders().putAll(customHeaders);
        responseInstance.setStatusCode(HttpStatus.SC_OK);
        MemoryManager.removeFromCache(fileId);
        }
        else {
        	 AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, EventEnum.DOWNLOADFILE,
                     ActivityStatusEnum.FAILED, "Roles file download failed");
        	 
        
        }
		}catch (Exception e) {
            //alert.prepareError("Exception while downloading roles list", e).log();
            ErrorCodeEnum.ERR_20687.setErrorCode(result);

            String errorMessage = "Failed to download roles list. Please contact administrator.";
            CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
        }
		

		return result;
	}
}
