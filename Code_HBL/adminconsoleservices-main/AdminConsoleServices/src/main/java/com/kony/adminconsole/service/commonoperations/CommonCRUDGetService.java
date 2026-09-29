package com.kony.adminconsole.service.commonoperations;

import java.util.HashMap;
import java.util.Map;

import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import org.apache.commons.lang.NullArgumentException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.handler.PaginationHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author Aditya Mankal, Akhil Alahari
 * 
 * 
 *         Generic Service to retrieve records. Supports Pagination.
 * 
 */
public class CommonCRUDGetService implements JavaService2 {

    private static final String READ_OPERATION_SUFFIX = "_READ";
    private static final String APPROVAL_SUFFIX = "_APPROVAL";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
        String entityName = methodID.substring(4);
        return get(authToken, requestInstance, entityName);
    }

    private Result get(String authToken, DataControllerRequest requestInstance, String entityName) {

        ServiceURLEnum serviceURLEnum = null;
        
        Map<String, String> postParametersMap = new HashMap<String, String>();
        Map<String, String> headerParametersMap = new HashMap<String, String>();
        String filterInRequestPayload = "";
		String filterInQueryParams = "";

        @SuppressWarnings("unchecked")
        Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
        /*
         * Reading the Query Parameter Map for OData Query Inputs. The Post Body is later processed with a higher
         * precedence. Any common value is overwritten by that in the Post Body
         */

        boolean approvalContext = false;
        try{
            String isMakerCheckerAvailable = EnvironmentConfigurationsHandler.getServerAppPropertyValue("IS_MAKER_CHECKER_AVAILABLE",requestInstance);
            if(isMakerCheckerAvailable.equalsIgnoreCase("true")){
                if(queryParamsMap.containsKey("ExperienceAPIContext")&& queryParamsMap.get("ExperienceAPIContext").equalsIgnoreCase("Approval")) {
                    approvalContext = true;
                }
            }
        }catch (Exception e){
            approvalContext = false;
        }
        
        try {
        	if(approvalContext) {
        	    serviceURLEnum = ServiceURLEnum.valueOf(entityName.toUpperCase() + APPROVAL_SUFFIX + READ_OPERATION_SUFFIX);
        	}else {
        		serviceURLEnum = ServiceURLEnum.valueOf(entityName.toUpperCase() + READ_OPERATION_SUFFIX);
        	}
            
        } catch (NullArgumentException iae) {
            alert.prepareError(iae.toString()).log();
            Result processedResult = new Result();
            ErrorCodeEnum.ERR_20874.setErrorCode(processedResult);
            return processedResult;
        }
        diagnostic.prepareDebug("Resolved URL:" + serviceURLEnum.getServiceURL()).log();
        
        if (queryParamsMap != null && !queryParamsMap.isEmpty()) {
            if (queryParamsMap.containsKey(ODataQueryConstants.SELECT))
                postParametersMap.put(ODataQueryConstants.SELECT, queryParamsMap.get(ODataQueryConstants.SELECT));
			if (queryParamsMap.containsKey(ODataQueryConstants.FILTER)) {
				filterInQueryParams += queryParamsMap.get(ODataQueryConstants.FILTER);

			}
			if (approvalContext && queryParamsMap.containsKey("RequestId")) {
				if (filterInQueryParams.isEmpty()) {
					filterInQueryParams += "RequestIdForApprovalContext eq " + queryParamsMap.get("RequestId");
				} else {
					filterInQueryParams += " and RequestIdForApprovalContext eq " + queryParamsMap.get("RequestId");
				}
			}
            if (queryParamsMap.containsKey("legalEntityId")) {
                String[] legalEntityIds = queryParamsMap.get("legalEntityId").trim().split(",");
                if (legalEntityIds.length != 0) {
                    String filterLegalEntityIds = "";
                    for (int index = 0; index < legalEntityIds.length; index++) {
                        String legalEntityId = legalEntityIds[index];
                        if (legalEntityId != null && !legalEntityId.trim().isEmpty() && legalEntityId.trim().length() != 0) {
                            filterLegalEntityIds = filterLegalEntityIds + "companyLegalUnit eq '" + legalEntityId.trim() + "'";
                            if (index != legalEntityIds.length - 1) {
                                filterLegalEntityIds = filterLegalEntityIds + " or ";
                            }
                        }
                    }
                    if(!filterLegalEntityIds.isEmpty()) {
                        filterLegalEntityIds = "(" + filterLegalEntityIds + ")";
                        if (!filterInQueryParams.isEmpty()) {
                            filterInQueryParams = filterInQueryParams + " and " + filterLegalEntityIds;
                        } else {
                            filterInQueryParams = filterLegalEntityIds;
                        }
                    }
                }
            }
			if (!filterInQueryParams.isEmpty())
				postParametersMap.put(ODataQueryConstants.FILTER, filterInQueryParams);

            if (queryParamsMap.containsKey(ODataQueryConstants.ORDER_BY)&& isValidParam(queryParamsMap.get(ODataQueryConstants.ORDER_BY)))
                postParametersMap.put(ODataQueryConstants.ORDER_BY, queryParamsMap.get(ODataQueryConstants.ORDER_BY));
            if (queryParamsMap.containsKey(ODataQueryConstants.TOP))
                postParametersMap.put(ODataQueryConstants.TOP, queryParamsMap.get(ODataQueryConstants.TOP));
            if (queryParamsMap.containsKey(ODataQueryConstants.SKIP))
                postParametersMap.put(ODataQueryConstants.SKIP, queryParamsMap.get(ODataQueryConstants.SKIP));
            if (queryParamsMap.containsKey(ODataQueryConstants.EXPAND))
                postParametersMap.put(ODataQueryConstants.EXPAND, queryParamsMap.get(ODataQueryConstants.EXPAND));
        }

        if (requestInstance.containsKeyInRequest(ODataQueryConstants.SELECT))
            postParametersMap.put(ODataQueryConstants.SELECT, requestInstance.getParameter(ODataQueryConstants.SELECT));
		if (requestInstance.containsKeyInRequest(ODataQueryConstants.FILTER)) {
			filterInRequestPayload += requestInstance.getParameter(ODataQueryConstants.FILTER);
		}
		if (approvalContext && requestInstance.containsKeyInRequest("RequestId")) {
			if (filterInRequestPayload.isEmpty()) {
				filterInRequestPayload += "RequestIdForApprovalContext eq " + requestInstance.getParameter("RequestId");
			} else {
				filterInRequestPayload += " and RequestIdForApprovalContext eq "
						+ requestInstance.getParameter("RequestId");
			}
		}
		
        if (requestInstance.containsKeyInRequest("legalEntityId") && requestInstance.getParameter("legalEntityId") != null) {
            String[] legalEntityIds = requestInstance.getParameter("legalEntityId").trim().split(",");
            if (legalEntityIds.length != 0) {
                String filterLegalEntityId = "";
                for (int index = 0; index < legalEntityIds.length; index++) {
                    String legalEntityId = legalEntityIds[index];
                    if (legalEntityId != null && !legalEntityId.trim().isEmpty() && legalEntityId.trim().length() != 0) {
                        filterLegalEntityId = filterLegalEntityId + "companyLegalUnit eq '" + legalEntityId.trim() + "'";
                        if (index != legalEntityIds.length - 1) {
                            filterLegalEntityId = filterLegalEntityId + " or ";
                        }
                    }
                }
                if(!filterLegalEntityId.isEmpty()) {
                    filterLegalEntityId = "(" + filterLegalEntityId + ")";
                    if (!filterInRequestPayload.isEmpty()) {
                        filterInRequestPayload = filterInRequestPayload + " and " + filterLegalEntityId;
                    } else {
                        filterInRequestPayload = filterLegalEntityId;
                    }
                }    
            }
        }

		if (!filterInRequestPayload.isEmpty())
			postParametersMap.put(ODataQueryConstants.FILTER, filterInRequestPayload);
        if (requestInstance.containsKeyInRequest(ODataQueryConstants.ORDER_BY)&& isValidParam(requestInstance.getParameter(ODataQueryConstants.ORDER_BY)))
            postParametersMap.put(ODataQueryConstants.ORDER_BY,
                    requestInstance.getParameter(ODataQueryConstants.ORDER_BY));
        if (requestInstance.containsKeyInRequest(ODataQueryConstants.TOP))
            postParametersMap.put(ODataQueryConstants.TOP, requestInstance.getParameter(ODataQueryConstants.TOP));
        if (requestInstance.containsKeyInRequest(ODataQueryConstants.SKIP))
            postParametersMap.put(ODataQueryConstants.SKIP, requestInstance.getParameter(ODataQueryConstants.SKIP));
        if (requestInstance.containsKeyInRequest(ODataQueryConstants.EXPAND))
            postParametersMap.put(ODataQueryConstants.EXPAND, requestInstance.getParameter(ODataQueryConstants.EXPAND));

        JSONObject backendRepsonse = PaginationHandler.getPaginatedData(serviceURLEnum,
				postParametersMap, headerParametersMap, requestInstance);
        
        if (backendRepsonse != null && backendRepsonse.has(FabricConstants.OPSTATUS)
				&& backendRepsonse.getInt(FabricConstants.OPSTATUS) == 0 && backendRepsonse.has("roles_view")) {
        	backendRepsonse = getRolesGroupingForLegalEntityIds(backendRepsonse);
		}
        
        if(approvalContext) {
        	return CommonUtilities.getResultObjectFromJSONObject(approvalResponseParser(entityName, backendRepsonse));
        } else {
        	return CommonUtilities.getResultObjectFromJSONObject(backendRepsonse);
        }
        
    }
    
    /* Function to rename response data set to method id
     * 
     * If the method id is get_roleuser_view, entity name is roleuser_view
     * 
     * In case of Approval, the entity is coming as roleuser_view_approval
     * 
     * Given the method name & object data model name is roleuser_view only, the below function 
     * 
     * renames the roleuser_view_approval Dataset to roleuser_view so that object Datamodel return the response as records..
     * 
     */
    
    private JSONObject approvalResponseParser(String entityName, JSONObject response) {
    	
    	String entityNameForApproval = entityName+APPROVAL_SUFFIX.toLowerCase();
    	
    	if(response.has(entityNameForApproval)){
    		response.put(entityName, response.get(entityNameForApproval));
    		response.remove(entityNameForApproval);
    		return response;
    	}else {
    		return response;
    	}
    }

	public boolean isValidParam(String s) {
    	if(s.contains("\"")||s.contains("<")||s.contains(">")) {
    		return false;
    	}
    	return true;
    }
	
	private JSONObject getRolesGroupingForLegalEntityIds(JSONObject responseJSON) {
		JSONArray rolesViewList = responseJSON.getJSONArray("roles_view");
		JSONArray rolesViewListRes = new JSONArray();
		Map<String, Integer> roleIdsMap = new HashMap<String, Integer>();
		for (int index = 0, roleIndex = 0; index < rolesViewList.length(); index++) {
			JSONObject roleViewRecord = rolesViewList.getJSONObject(index);
			String roleId = roleViewRecord.getString("role_id");
            String legalEntityId = roleViewRecord.getString("companyLegalUnit");
            roleViewRecord.put("legalEntityId",legalEntityId);
            roleViewRecord.remove("companyLegalUnit");
			if (!roleIdsMap.containsKey(roleId)) {
				JSONObject roleViewJSON = new JSONObject();
				JSONArray roleLegalEntitiesList = new JSONArray();
				roleLegalEntitiesList.put(roleViewRecord);
                roleViewJSON.put("role_id",roleId);
				roleViewJSON.put("legalEntitiesRoleInfo", roleLegalEntitiesList);
				rolesViewListRes.put(roleViewJSON);
				roleIdsMap.put(roleId, roleIndex++);
			} else {
				JSONObject roleViewJSON = rolesViewListRes.getJSONObject(roleIdsMap.get(roleId));
				JSONArray roleLegalEntitiesList = roleViewJSON.getJSONArray("legalEntitiesRoleInfo");
				roleLegalEntitiesList.put(roleViewRecord);
				roleViewJSON.put("legalEntitiesRoleInfo", roleLegalEntitiesList);
				rolesViewListRes.put(roleIdsMap.get(roleId), roleViewJSON);
			}
		}
		responseJSON.put("roles_view", rolesViewListRes);
		return responseJSON;
	}

}