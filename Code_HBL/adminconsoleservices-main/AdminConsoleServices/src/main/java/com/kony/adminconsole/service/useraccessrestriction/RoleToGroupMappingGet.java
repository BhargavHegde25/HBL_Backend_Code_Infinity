package com.kony.adminconsole.service.useraccessrestriction;

import java.util.HashMap;
import java.util.Map;

import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class RoleToGroupMappingGet implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String INPUT_INTERNAL_ROLE_ID = "InternalRole_id";

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();

        boolean approvalContext = false;
        try {
            String isMakerCheckerAvailable = EnvironmentConfigurationsHandler.getServerAppPropertyValue("IS_MAKER_CHECKER_AVAILABLE", requestInstance);

            if (isMakerCheckerAvailable.equalsIgnoreCase("true")) {
                String paramValue = requestInstance.containsKeyInRequest("ExperienceAPIContext") ? requestInstance.getParameter("ExperienceAPIContext") : "";
                approvalContext = paramValue.equalsIgnoreCase("Approval");
            }
        } catch (Exception e) {
            approvalContext = false;
        }
        String inputCurrentInternalRole = requestInstance.getParameter(INPUT_INTERNAL_ROLE_ID);

        if (StringUtils.isBlank(inputCurrentInternalRole)) {
            ErrorCodeEnum.ERR_21596.setErrorCode(processedResult);
            return processedResult;
        }

        String legalEntityId = requestInstance.getParameter("legalEntityId");

        try {

            Map<String, String> postParametersMap = new HashMap<String, String>();
            String filter = "InternalRole_id eq '" + inputCurrentInternalRole + "'";
            if(approvalContext && requestInstance.containsKeyInRequest("RequestId")) {
            	filter += " and RequestIdForApprovalContext eq " +requestInstance.getParameter("RequestId");
            }
            if(!StringUtils.isBlank(legalEntityId)){
                legalEntityId = legalEntityId.trim();
                String[] legalEntityIds = legalEntityId.split(",");
                if (legalEntityIds.length != 0) {
                    String filterLegalEntityIds = "";
                    for (int index = 0; index < legalEntityIds.length; index++) {
                        String legalEntityID = legalEntityIds[index];
                        if (!StringUtils.isBlank(legalEntityID)) {
                            filterLegalEntityIds = filterLegalEntityIds + "companyLegalUnit eq '" + legalEntityID.trim() + "'";
                            if (index != legalEntityIds.length - 1) {
                                filterLegalEntityIds = filterLegalEntityIds + " or ";
                            }
                        }
                    }
                    if(!filterLegalEntityIds.isEmpty()) {
                        filterLegalEntityIds = "(" + filterLegalEntityIds + ")";
                        if (!filter.isEmpty()) {
                            filter = filter + " and " + filterLegalEntityIds;
                        } else {
                            filter = filterLegalEntityIds;
                        }
                    }
                }
            }
            postParametersMap.put(ODataQueryConstants.FILTER,filter);
            String readEndpointResponse = "";

            if (approvalContext) {
                readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNAL_ROLE_TO_SERVICEDEFINITION_MAPPING_APPROVAL_VIEW_READ, postParametersMap, null, requestInstance);
            } else {
                readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNAL_ROLE_TO_SERVICEDEFINITION_MAPPING_VIEW_READ, postParametersMap, null, requestInstance);
            }

            JSONObject readResponse = CommonUtilities.getStringAsJSONObject(readEndpointResponse);

            if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS) || readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !(readResponse.has("internal_role_to_servicedefinition_mapping_view") || readResponse.has("internal_role_to_servicedefinition_mapping_view_approval"))) {

                processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
                ErrorCodeEnum.ERR_21599.setErrorCode(processedResult);
                return processedResult;
            }

            Dataset responseDataset = new Dataset();
            if (approvalContext) {
                responseDataset = CommonUtilities.constructDatasetFromJSONArray(readResponse.getJSONArray("internal_role_to_servicedefinition_mapping_view_approval"));
                responseDataset.setId("internal_role_to_servicedefinition_mapping_approval");
            } else {
                responseDataset = CommonUtilities.constructDatasetFromJSONArray(readResponse.getJSONArray("internal_role_to_servicedefinition_mapping_view"));
                responseDataset.setId("internal_role_to_servicedefinition_mapping");
            }
            processedResult.addDataset(responseDataset);

            return processedResult;
        } catch (Exception e) {
            alert.prepareError("Exception occurred while processing roles to groups mapping get service ", e).log();
            ErrorCodeEnum.ERR_21599.setErrorCode(processedResult);
            return processedResult;
        }
    }
}
