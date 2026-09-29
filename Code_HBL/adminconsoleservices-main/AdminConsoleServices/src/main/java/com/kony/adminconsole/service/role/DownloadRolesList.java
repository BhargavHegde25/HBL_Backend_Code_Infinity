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

public class DownloadRolesList implements JavaService2 {

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
            String status = queryParamsMap.containsKey("status") ? queryParamsMap.get("status") : null;
            String selectedLegalEntity = queryParamsMap.containsKey("legalEntityId") ? queryParamsMap.get("legalEntityId") : null;

            // ** Reading entries from 'roles_view' view **
            Map<String, String> rolesViewMap = new HashMap<String, String>();
            rolesViewMap.put(ODataQueryConstants.SELECT, "role_Name, role_Desc, Users_Count, permission_Count, Status, companyLegalUnit");

            StringBuilder filterString = new StringBuilder();

            if (status != null) {
                String[] statuses = status.split("_");
                filterString.append("(");
                for (int i = 0; i < statuses.length - 1; ++i) {
                    filterString.append("Status eq '" + statuses[i] + "'");
                    filterString.append(" or ");
                }
                filterString.append("Status eq '" + statuses[statuses.length - 1] + "')");
            }
             if (selectedLegalEntity != null) { 	
            	 String[] legalEntities = selectedLegalEntity.split("_");
                 if(filterString.length() > 0) {
                	 filterString.append(" and ");
                 }
            	 filterString.append("(");
                 for (int i = 0; i < legalEntities.length - 1; ++i) {
                     filterString.append("companyLegalUnit eq '" + legalEntities[i] + "'");
                     filterString.append(" or ");
                 }
                 filterString.append("companyLegalUnit eq '" + legalEntities[legalEntities.length - 1] + "')");
            }

            if (filterString != null) {
                rolesViewMap.put(ODataQueryConstants.FILTER, filterString.toString());
            }

            String rolesViewResponse = Executor.invokeService(ServiceURLEnum.ROLES_VIEW_READ, rolesViewMap, null,
                    requestInstance);
            if(rolesViewResponse == null) {
            	throw new ApplicationException(ErrorCodeEnum.ERR_20000);
            }
            JSONObject rolesViewResponseJSON = CommonUtilities.getStringAsJSONObject(rolesViewResponse);

            if (rolesViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && rolesViewResponseJSON.getJSONArray("roles_view") != null) {

                StringBuilder responseCsvBuilder = new StringBuilder(); // Contains the text for response CSV file
                CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
                        .withHeader("Name", "Description", "Users", "Permissions", "Status", "companyLegalUnit").print(responseCsvBuilder);

                JSONArray roles = rolesViewResponseJSON.getJSONArray("roles_view");

                for (int i = 0; i < roles.length(); ++i) {

                    String nameColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(roles.getJSONObject(i).optString("role_Name"));
                    String descriptionColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(roles.getJSONObject(i).optString("role_Desc"));
                    String usersCountColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(roles.getJSONObject(i).optString("Users_Count"));
                    String permissionCountColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(roles.getJSONObject(i).optString("permission_Count"));
                    String statusColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(roles.getJSONObject(i).optString("Status"));
                    String companyLegalUnit = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(roles.getJSONObject(i).optString("companyLegalUnit"));
                    if (searchText == null
                            || (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
                        responseCsvPrinter.printRecord(nameColumn, descriptionColumn, usersCountColumn,
                                permissionCountColumn, statusColumn, companyLegalUnit);
                    }
                }

                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, EventEnum.DOWNLOADFILE,
                        ActivityStatusEnum.SUCCESSFUL, "Roles file download successful");

                Map<String, String> customHeaders = new HashMap<String, String>();
                customHeaders.put("Content-Type", "text/plain; charset=utf-8");
                customHeaders.put("Content-Disposition", "attachment; filename=\"Roles_List.csv\"");

                responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
                        new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
                responseInstance.getHeaders().putAll(customHeaders);
                responseInstance.setStatusCode(HttpStatus.SC_OK);
            } else {
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, EventEnum.DOWNLOADFILE,
                        ActivityStatusEnum.FAILED, "Roles file download failed");
            }

        } catch (ApplicationException e) {
            alert.prepareError(" ApplicationException while downloading roles file", e).log();
            e.getErrorCodeEnum().setErrorCode(result);
            CommonUtilities.fileDownloadFailure(responseInstance, e.getErrorCodeEnum().getMessage());
        } catch (Exception e) {
            alert.prepareError("Exception while downloading roles list", e).log();
            ErrorCodeEnum.ERR_20687.setErrorCode(result);

            String errorMessage = "Failed to download roles list. Please contact administrator.";
            CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
        }
        return result;
    }
}