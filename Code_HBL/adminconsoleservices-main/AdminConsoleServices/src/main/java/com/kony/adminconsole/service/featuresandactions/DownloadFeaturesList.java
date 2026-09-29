package com.kony.adminconsole.service.featuresandactions;

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

public class DownloadFeaturesList implements JavaService2 {

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
            String type = queryParamsMap.containsKey("type") ? queryParamsMap.get("type") : null;
            String selectedLegalEntity = queryParamsMap.containsKey("companyLegalUnit") ? queryParamsMap.get("companyLegalUnit") : null;

            // ** Reading entries from 'feature_action_roles_view' view **
            Map<String, String> featureViewMap = new HashMap<String, String>();
            featureViewMap.put(ODataQueryConstants.SELECT, "feature_name, feature_code, feature_type_id, action_name, action_code, action_role_type_id, category, actionlevelId, accesspolicyId, status,companyLegalUnit");

            StringBuilder filterString = new StringBuilder();

            if (status != null) {
                String[] statuses = status.split(",");
                filterString.append("(");
                for (int i = 0; i < statuses.length - 1; ++i) {
                    filterString.append("feature_status_id eq '" + statuses[i] + "'");
                    filterString.append(" or ");
                }
                filterString.append("feature_status_id eq '" + statuses[statuses.length - 1] + "')");
            }
            if (selectedLegalEntity != null) {
            	
            	if(filterString != null && filterString.length() !=0) {
            	filterString.append(" and ");
            	filterString.append("(");
            	filterString.append("companyLegalUnit eq '" + selectedLegalEntity+ "')");
            	}
            	else {
            	filterString.append("companyLegalUnit eq '" + selectedLegalEntity);
            	}
            }

            if (filterString != null) {
                featureViewMap.put(ODataQueryConstants.FILTER, filterString.toString());
            }
            
            featureViewMap.put(ODataQueryConstants.ORDER_BY, "feature_name");
            String featureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_ACTION_ROLES_VIEW_READ, featureViewMap, null,
                    requestInstance);
            JSONObject featureResponseJSON = CommonUtilities.getStringAsJSONObject(featureResponse);

            if (featureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && featureResponseJSON.getJSONArray("feature_action_roles_view") != null) {
            		
                StringBuilder responseCsvBuilder = new StringBuilder(); // Contains the text for response CSV file
                CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
                        .withHeader("Feature Name", "Feature Code", "Feature Type", "Action Name", "Action Code", "Action Type","Category", "Action Level","Access Level","Status","companyLegalUnit")
                        .print(responseCsvBuilder);

                JSONArray features = featureResponseJSON.getJSONArray("feature_action_roles_view");

                for (int i = 0; i < features.length(); ++i) {

                    String featureNameColumn = features.getJSONObject(i).optString("feature_name");
                    String featureCodeColumn = features.getJSONObject(i).optString("feature_code");
                    String featureTypeColumn = features.getJSONObject(i).optString("feature_type_id");
                    String actionNameColumn = features.getJSONObject(i).optString("action_name");
                    String actionCodeColumn = features.getJSONObject(i).optString("action_code");
                    String actionTypeColumn = features.getJSONObject(i).optString("action_role_type_id");
                    String categoryColumn = features.getJSONObject(i).optString("category");
                    String actionLevelColumn = features.getJSONObject(i).optString("actionlevelId");
                    String accessLevelColumn = features.getJSONObject(i).optString("accesspolicyId");
                    String statusColumn = features.getJSONObject(i).optString("status");
                    String companyLegalUnit = features.getJSONObject(i).optString("companyLegalUnit");
                    
                    if (searchText == null
                            || (searchText != null && (featureNameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
                        if (type == null)
                            responseCsvPrinter.printRecord(featureNameColumn, featureCodeColumn, featureTypeColumn, actionNameColumn, actionCodeColumn, actionTypeColumn, categoryColumn, actionLevelColumn, accessLevelColumn, statusColumn,companyLegalUnit);
                        else {
                            String[] types = type.split(",");
                            for (int j = 0; j < types.length; j++) {
                                if (featureTypeColumn.contains(types[j])) {
                                    responseCsvPrinter.printRecord(featureNameColumn, featureCodeColumn, featureTypeColumn, actionNameColumn, actionCodeColumn, actionTypeColumn, categoryColumn, actionLevelColumn, accessLevelColumn, statusColumn,companyLegalUnit);
                                    break;
                                }
                            }
                        }
                    }
                }
                
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FEATURES, EventEnum.DOWNLOADFILE,
                        ActivityStatusEnum.SUCCESSFUL, "Features file download successful");

                Map<String, String> customHeaders = new HashMap<String, String>();
                customHeaders.put("Content-Type", "text/plain; charset=utf-8");
                customHeaders.put("Content-Disposition", "attachment; filename=\"Features_List.csv\"");

                responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
                        new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
                responseInstance.getHeaders().putAll(customHeaders);
                responseInstance.setStatusCode(HttpStatus.SC_OK);
            } else {
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SERVICES, EventEnum.DOWNLOADFILE,
                        ActivityStatusEnum.FAILED, "Features file download failed");
            }

        } catch (Exception e) {
            alert.prepareError("Failed while downloading features list", e).log();
            ErrorCodeEnum.ERR_20687.setErrorCode(result);

            String errorMessage = "Failed to download features list. Please contact administrator.";
            CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
        }
        return result;
    }
}