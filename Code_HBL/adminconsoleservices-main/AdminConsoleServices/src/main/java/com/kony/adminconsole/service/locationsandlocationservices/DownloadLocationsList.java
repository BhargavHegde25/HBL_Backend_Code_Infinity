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
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
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

public class DownloadLocationsList implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();

        try {

        	String authToken = CommonUtilities.getAuthToken(requestInstance);
        	if(StringUtils.isBlank(authToken)) {
        		throw new ApplicationException(ErrorCodeEnum.ERR_20000);
        	}

            Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
            String searchText = queryParamsMap.containsKey("searchText") ? queryParamsMap.get("searchText") : null;
            String type = queryParamsMap.containsKey("type") ? queryParamsMap.get("type") : null;
            String status = queryParamsMap.containsKey("status") ? queryParamsMap.get("status") : null;

            // ** Reading entries from 'location_view' view **
            Map<String, String> locationViewMap = new HashMap<String, String>();
            locationViewMap.put(ODataQueryConstants.SELECT, "Name, Code, Description, PhoneNumber, Type_id, Status_id");

            StringBuilder filterString = new StringBuilder();

            if (type != null) {
                String[] types = type.split("_");
                filterString.append("(");
                for (int i = 0; i < types.length - 1; ++i) {
                    filterString.append("Type_id eq '" + types[i] + "'");
                    filterString.append(" or ");
                }
                filterString.append("Type_id eq '" + types[types.length - 1] + "')");
            }

            if (status != null) {
                if (filterString != null) {
                    filterString.append(" and ");
                }
                String[] statuses = status.split("_");
                filterString.append("(");
                for (int i = 0; i < statuses.length - 1; ++i) {
                    filterString.append("Status_id eq '" + statuses[i] + "'");
                    filterString.append(" or ");
                }
                filterString.append("Status_id eq '" + statuses[statuses.length - 1] + "')");
            }

            if (filterString != null) {
                locationViewMap.put(ODataQueryConstants.FILTER, filterString.toString());
            }

            String locationResponse = Executor.invokeService(ServiceURLEnum.LOCATION_VIEW_READ, locationViewMap, null,
                    requestInstance);
            if(locationResponse == null) {
            	throw new ApplicationException(ErrorCodeEnum.ERR_20000);
            }
            JSONObject locationResponseJSON = CommonUtilities.getStringAsJSONObject(locationResponse);
            
            if (locationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && locationResponseJSON.getJSONArray("location_view") != null) {

                StringBuilder responseCsvBuilder = new StringBuilder(); // Contains the text for response CSV file
                CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
                        .withHeader("Name", "Code", "Description", "Phone Number", "Type", "Status")
                        .print(responseCsvBuilder);

                JSONArray locations = locationResponseJSON.getJSONArray("location_view");

                for (int i = 0; i < locations.length(); ++i) {

                    String nameColumn = locations.getJSONObject(i).optString("Name");
                    nameColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(nameColumn);
                    String codeColumn = locations.getJSONObject(i).optString("Code");
                    String descriptionColumn = locations.getJSONObject(i).optString("Description");
                    descriptionColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(descriptionColumn);
                    String phoneNumberColumn = locations.getJSONObject(i).optString("PhoneNumber");
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
            } else {
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.DOWNLOADFILE,
                        ActivityStatusEnum.FAILED, "Locations file download failed");
            }

        } catch (ApplicationException e) {
            alert.prepareError("ApplicationException while downloading locations list", e).log();
            e.getErrorCodeEnum().setErrorCode(result);
            CommonUtilities.fileDownloadFailure(responseInstance, e.getErrorCodeEnum().getMessage());
        } catch (Exception e) {
            alert.prepareError("Exception while downloading locations list", e).log();
            ErrorCodeEnum.ERR_20687.setErrorCode(result);

            String errorMessage = "Failed to download locations list. Please contact administrator.";
            CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
        }
        return result;
    }
}
