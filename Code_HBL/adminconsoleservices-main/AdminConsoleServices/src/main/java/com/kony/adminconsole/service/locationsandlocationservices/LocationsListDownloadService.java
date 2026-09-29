package com.kony.adminconsole.service.locationsandlocationservices;

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
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class LocationsListDownloadService implements JavaService2 {

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception{
		Result result = new Result();
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
			String fileId= queryParamsMap.get("fileId");
			String response = (String) MemoryManager.getFromCache(fileId);
			JSONArray locations= CommonUtilities.getStringAsJSONArray(response);
			String searchText = (String) MemoryManager.getFromCache("searchText");
			StringBuilder responseCsvBuilder = new StringBuilder();
			CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
                    .withHeader("Name", "Code", "Description", "Phone Number", "Type", "Status")
                    .print(responseCsvBuilder);
			if(StringUtils.isNotBlank(response)) {
			for (int i = 0; i < locations.length(); ++i) {

                String nameColumn = locations.getJSONObject(i).optString("Name");
                String codeColumn = locations.getJSONObject(i).optString("Code");
                String descriptionColumn = locations.getJSONObject(i).optString("Description");
                String phoneNumberColumn = locations.getJSONObject(i).optString("PhoneNumber");
                phoneNumberColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(phoneNumberColumn);
                String typeColumn = locations.getJSONObject(i).optString("Type_id");
                String statusColumn = locations.getJSONObject(i).optString("Status_id");

                if (searchText == null
                        || (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
                    responseCsvPrinter.printRecord(nameColumn, codeColumn, descriptionColumn, phoneNumberColumn,
                            typeColumn, statusColumn);
                }
            }

            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.DOWNLOADFILE,
                    ActivityStatusEnum.SUCCESSFUL, "Locations file download successful");

            Map<String, String> customHeaders = new HashMap<String, String>();
            customHeaders.put("Content-Type", "text/plain; charset=utf-8");
            customHeaders.put("Content-Disposition", "attachment; filename=\"Locations_List.csv\"");

            responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
                    new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
            responseInstance.getHeaders().putAll(customHeaders);
            responseInstance.setStatusCode(HttpStatus.SC_OK);
			}else {
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.DOWNLOADFILE,
                        ActivityStatusEnum.FAILED, "Locations file download failed");
            }
			MemoryManager.removeFromCache(fileId);
		}catch(Exception e) {
			 
			ErrorCodeEnum.ERR_20687.setErrorCode(result);
	        String errorMessage = "Failed to download locations list. Please contact administrator.";
	        CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
		}
		return result;
	}
}
