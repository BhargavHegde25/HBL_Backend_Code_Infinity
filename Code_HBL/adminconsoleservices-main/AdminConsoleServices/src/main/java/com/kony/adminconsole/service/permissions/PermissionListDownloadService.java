package com.kony.adminconsole.service.permissions;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

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

public class PermissionListDownloadService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception{
		Result result = new Result();
		try {
		@SuppressWarnings("unchecked")
		Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
		String fileId= queryParamsMap.get("fileId");
		String searchText = (String) MemoryManager.getFromCache("searchText");
		String permissionsViewResponse = (String) MemoryManager.getFromCache(fileId);
		StringBuilder responseCsvBuilder = new StringBuilder();
		CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
                .withHeader("Name", "Description", "Roles", "Users", "Status").print(responseCsvBuilder);
		if(permissionsViewResponse == null) {
        	throw new ApplicationException(ErrorCodeEnum.ERR_20000);
        }
        JSONObject permissionsViewResponseJSON = CommonUtilities.getStringAsJSONObject(permissionsViewResponse);
        JSONArray permissions = new JSONArray();
        if (permissionsViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && permissionsViewResponseJSON.getJSONArray("permissions_view") != null) {
             permissions = permissionsViewResponseJSON.getJSONArray("permissions_view");
		for (int i = 0; i < permissions.length(); ++i) {
            String nameColumn = permissions.getJSONObject(i).optString("Permission_Name");
            String descriptionColumn = permissions.getJSONObject(i).optString("Permission_Desc");
            String usersCountColumn = permissions.getJSONObject(i).optString("Role_Count");
            String permissionCountColumn = permissions.getJSONObject(i).optString("Users_Count");
            String statusColumn = permissions.getJSONObject(i).optString("Status");
            if (searchText == null
                    || (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
                responseCsvPrinter.printRecord(nameColumn, descriptionColumn, usersCountColumn,
                        permissionCountColumn, statusColumn);
            }
        }

        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PERMISSIONS, EventEnum.DOWNLOADFILE,
                ActivityStatusEnum.SUCCESSFUL, "Permissions file download successful");

        Map<String, String> customHeaders = new HashMap<String, String>();
        customHeaders.put("Content-Type", "text/plain; charset=utf-8");
        customHeaders.put("Content-Disposition", "attachment; filename=\"Permissions_List.csv\"");
        responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
                new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
        responseInstance.getHeaders().putAll(customHeaders);
        responseInstance.setStatusCode(HttpStatus.SC_OK);
        MemoryManager.removeFromCache(fileId);
		}
		else {
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PERMISSIONS, EventEnum.DOWNLOADFILE,
                    ActivityStatusEnum.FAILED, "Permissions file download failed");
        }

		}catch (ApplicationException e) {
            alert.prepareError("ApplicationException while downloading permissions file", e).log();
            e.getErrorCodeEnum().setErrorCode(result);
            CommonUtilities.fileDownloadFailure(responseInstance, e.getErrorCodeEnum().getMessage());

        }catch(Exception e) {
			 
			ErrorCodeEnum.ERR_20687.setErrorCode(result);
	        String errorMessage = "Failed to download permissions list. Please contact administrator.";
	        CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);

		}
		return result;
	}
}
