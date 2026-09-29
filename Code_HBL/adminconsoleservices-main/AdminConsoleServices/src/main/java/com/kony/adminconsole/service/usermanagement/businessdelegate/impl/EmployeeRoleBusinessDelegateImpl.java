package com.kony.adminconsole.service.usermanagement.businessdelegate.impl;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeePermissionBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeeRoleBusinessDelegate;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class EmployeeRoleBusinessDelegateImpl implements EmployeeRoleBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final String DEFAULT_ROLE_TYPE_ID = "ROLE_TYPE_1";
	private static final String OUTPUT_DBP_ERR_CODE = "dbpErrCode";
	private static final String OUTPUT_DBP_ERR_MESSAGE = "dbpErrMsg";
	private static final String OUTPUT_OPSTATUS = "opstatus";
	
	private static final String PARAM_OUID =  "ouId";
	private static final String PARAM_PERMISSION_ID =  "permissionId";
	
	EmployeePermissionBusinessDelegate employeePermissionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(EmployeePermissionBusinessDelegate.class);

	@Override
	public JSONObject getEmployeeRoles(Map<String, Object> postParametersMap) throws DBPApplicationException {
		
		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> inputParams =  new HashMap<String, Object>();
//		if(postParametersMap.containsKey("roleId")) {
//			inputParams.put("$filter","id eq "+postParametersMap.get("roleId"));
//		}

		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNALROLE_VIEW_GET)
					.withRequestParameters(inputParams)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
			
			if(responseJSON.has("internalrole_view")) {
				
				inputParams =  new HashMap<String, Object>();
				inputParams.put("legalEntities","*");
				inputParams.put("organisationalUnits","*");
				
				JSONObject entityIdsMS = employeePermissionBusinessDelegate.fetchLegalEntityList(inputParams);
				Map<String, JSONObject> ouLeJsonMap =  prepareOuLeJsonMap(entityIdsMS);
				
				JSONArray jsonArr = responseJSON.getJSONArray("internalrole_view");
				for(int i=0; i<jsonArr.length(); i++) {
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					if(jsonObj.has("ouIds") && StringUtils.isNotBlank(jsonObj.getString("ouIds"))) {
						
						String []ouIds = jsonObj.getString("ouIds").split(",");
						JSONArray leArr = new JSONArray();
						for(int j=0; j<ouIds.length; j++ ) {
							leArr.put(ouLeJsonMap.get(ouIds[j]));
						}
						jsonObj.put("legalEntityIds", leArr);
					}
				}
			}
			
			
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While fetching Role: "+ e).log();
		}
		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation internalrole_view. Response" + serviceResponse).log();
		}
		
		return responseJSON;
	}
	
	@Override
	public JSONObject getEmployeeRoleDetails(Map<String, Object> postParametersMap) throws DBPApplicationException {
		
		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> inputParams =  new HashMap<String, Object>();
		if(postParametersMap.containsKey("roleId")) {
			inputParams.put("$filter","roleId eq "+postParametersMap.get("roleId"));
		}

		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNALROLE_VIEW_GET)
					.withRequestParameters(inputParams)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
			
			if(responseJSON.has("internalrole_view")) {
				
				
				
				JSONArray jsonArr = responseJSON.getJSONArray("internalrole_view");
				JSONObject jsonObj = jsonArr.getJSONObject(0);
				if(jsonObj.has("ouIds") && StringUtils.isNotBlank(jsonObj.getString("ouIds"))) {
					
					inputParams =  new HashMap<String, Object>();
					inputParams.put("legalEntities","*");
					inputParams.put("organisationalUnits","*");
					
					JSONObject entityIdsMS = employeePermissionBusinessDelegate.fetchLegalEntityList(inputParams);
//					Map<String, JSONObject> ouLeJsonMap =  prepareOuLeJsonMap(entityIdsMS);
					
					
					String []ouIdArr = jsonObj.getString("ouIds").split(",");
					String []permissionIdArr = jsonObj.getString("permissionIds").split(",");
					String []permissionNameArr = jsonObj.getString("permissionNames").split(",");
					
					Map<String, JSONObject> ouPermissionMap = new HashMap<String, JSONObject>();
					for(int i=0; i<ouIdArr.length;i++) {
						String ouId = ouIdArr[i];
						String permissionId = permissionIdArr[i];
						String permissionName = permissionNameArr[i];
						JSONObject permissionJson = new JSONObject();
						permissionJson.put("permmissionId", permissionId);
						permissionJson.put("permissionName", permissionName);
						ouPermissionMap.put(ouId, permissionJson);
						
					}
					
					JSONArray accessControl = prepareJsonArrForRole(entityIdsMS, ouPermissionMap);
					jsonObj.put("accessControl", accessControl);
				}
				

			}
			
			
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While fetching Role: "+ e).log();
		}
		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation internalrole_view. Response" + serviceResponse).log();
		}
		
		return responseJSON;
	}

	@Override
	public JSONObject createEmployeeRole(Map<String, Object> postParametersMap) throws DBPApplicationException {


		JSONObject finalResponse = new JSONObject();
		String roleId = CommonUtilities.getNewId().toString();
		String loggedInUser= postParametersMap.get("createdby").toString();

		/*
		 * Create Role
		 */
		try {
			String name = postParametersMap.get("roleName").toString();
			String description= postParametersMap.get("description").toString();
			String status= postParametersMap.get("status").toString().toString();

			createRole(roleId, name, description, status, loggedInUser);
			finalResponse.put("Status_RoleCreate", "success");
		}catch(Exception exp) {
			finalResponse.append(OUTPUT_DBP_ERR_CODE, "22184");
			finalResponse.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to create role");
			return finalResponse;
		}

		/*
		 * Create OUs Permission for Role
		 */
		try {

			JSONArray accessControlArr = (JSONArray)postParametersMap.get("accessControl");
			createRoleOUs(roleId, accessControlArr,loggedInUser);
			finalResponse.put("Status_RoleOU", "success");

		}catch(Exception exp) {
			finalResponse.put("Status_RoleOU", "failed");
		}
		
		finalResponse.put(OUTPUT_OPSTATUS,0);
		finalResponse.put("roleId", roleId);

		return finalResponse;
	}

	@Override
	public JSONObject updateEmployeeRoleDetails(Map<String, Object> postParametersMap) throws DBPApplicationException {


		JSONObject finalResponse = new JSONObject();
		String roleId = postParametersMap.get("roleId").toString();
		String loggedInUser= postParametersMap.get("modifiedby").toString();

		try {

			updateEmployeeRoleBasicDetails(postParametersMap);
			finalResponse.put("Status_Role_Update", "success");

		}catch(Exception exp) {
			finalResponse.put("Status_Role_Update", "failed");
		}

		/*
		 * Create Legal Entity for Role
		 */
		try {
			JSONArray accessControlArr = (JSONArray)postParametersMap.get("accessControl");
			deleteRolePermissionOU(roleId);
			createRoleOUs(roleId, accessControlArr,loggedInUser);
			finalResponse.put("Status_RolePermissionOU_Update", "success");

		}catch(Exception exp) {
			finalResponse.put("Status_RolePermissionOU_Update", "failed");
		}

		finalResponse.put(OUTPUT_OPSTATUS,0);
		return finalResponse;
	}

	@Override
	public JSONObject updateEmployeeRoleBasicDetails(Map<String, Object> postParametersMap) throws DBPApplicationException {


		Map<String, Object> inputMap = new HashMap<>();
		String roleId = postParametersMap.get("roleId").toString();
		String roleName = postParametersMap.get("roleName").toString();
		String description = postParametersMap.get("description").toString();
		String status =  postParametersMap.get("status").toString();
		String loggedInUser= postParametersMap.get("modifiedby").toString();

		inputMap.put("id", roleId);
		inputMap.put("Name", roleName);
		inputMap.put("description", description);
		inputMap.put("Status_id", status);
		inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
		inputMap.put("modifiedby", loggedInUser);
		String serviceResponse = null;
		JSONObject responseJSON = null;
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_ROLE_UPDATE)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While updating Role status: "+ e).log();
		}

		return responseJSON;

	}


	@Override
	public JSONObject updateEmployeeRoleStatus(Map<String, Object> postParametersMap) throws DBPApplicationException {


		Map<String, Object> inputMap = new HashMap<>();

		String roleId = postParametersMap.get("roleId").toString();
		String status =  postParametersMap.get("status").toString();
		String loggedInUser= postParametersMap.get("modifiedby").toString();

		inputMap.put("id", roleId);
		inputMap.put("Status_id", status);
		inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
		inputMap.put("modifiedby", loggedInUser);
		String serviceResponse = null;
		JSONObject responseJSON = null;
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_ROLE_UPDATE)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While updating Role status: "+ e).log();
		}

		return responseJSON;

	}

	private void createRole(String roleId, String name, String description, String status, 
			String loggedInUser) throws ApplicationException {

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("id", roleId);
		inputMap.put("Name", name);
		inputMap.put("Status_id", status);
		inputMap.put("Parent_id", roleId);
		inputMap.put("Description", description);
		inputMap.put("createdby", loggedInUser);
		inputMap.put("Type_id", DEFAULT_ROLE_TYPE_ID);
		inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
		String serviceResponse = null;
		JSONObject responseJSON = null;
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_ROLE_CREATE)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While creating Role: "+ e).log();
		}


		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20546);
		}

	}

	public void createRoleOUs(String roleId, JSONArray accessControlArr, String loggedInUser) throws ApplicationException {

		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> inputMap = new HashMap<>();

		for (int i=0; i<accessControlArr.length(); i++) {
			inputMap.put("roleId", roleId);
			inputMap.put("ouId", accessControlArr.getJSONObject(i).getString(PARAM_OUID));
			inputMap.put("permissionId", accessControlArr.getJSONObject(i).getString(PARAM_PERMISSION_ID));
			inputMap.put("createdby", loggedInUser);
			inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
			inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

			try {
				serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
						.withOperationId(OperationName.DB_ROLEPERMISSIONOU_CREATE)
						.withRequestParameters(inputMap)
						.build()
						.getResponse();
				responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
			} catch (DBPApplicationException e) {
				alert.prepareError("Exception While creating RolePermissionOU: "+ e).log();
			}
			if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
					|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
				alert.prepareError("Failed CRUD Operation RolePermissionOU create. Response" + serviceResponse).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_22187);
			}
			inputMap.clear();
		}
	}


	private void deleteRolePermissionOU(String roleId) throws ApplicationException {

		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		requestParameters.put("_roleId", roleId);
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_ROLE_PERMISSION_OU_DELETE_PROC)
					.withRequestParameters(requestParameters)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While deleting RolePermissionOu: "+ e).log();
		}
		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation rolepermissionou delete. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22188);
		}
	}
	
	private Map<String,JSONObject> prepareOuLeJsonMap(JSONObject entityIdsMS){
		
		Map<String, JSONObject> entityMap = new HashMap<>();
		JSONArray entityArr = entityIdsMS !=null? entityIdsMS.getJSONArray("financialInstitutions") : null;
		if(null !=entityArr && entityArr.length() > 0) {
			
			for(int i=0; i<entityArr.length(); i++) {
				
				JSONObject entityJson = entityArr.getJSONObject(i);
				if(entityJson.has("organisationalUnits") && 
						entityJson.getJSONArray("organisationalUnits").length()>0) {
					JSONArray ouArr = entityJson.getJSONArray("organisationalUnits");
					entityJson.remove("organisationalUnits");
					for(int j=0; j<ouArr.length(); j++) {
						
						JSONObject ouJson = ouArr.getJSONObject(j);
						entityMap.put(ouJson.getString("id"),entityJson);
					}
					
				}
			}
		}
		
		return entityMap;
	}
	
	private JSONArray prepareJsonArrForRole(JSONObject entityIdsMS, Map<String, JSONObject> ouPermissionMap){
		
		JSONArray finalJsonArr = new JSONArray();
		JSONArray entityArr = entityIdsMS !=null? entityIdsMS.getJSONArray("financialInstitutions") : null;
		if(null !=entityArr && entityArr.length() > 0) {
			
			for(int i=0; i<entityArr.length(); i++) {
				
				JSONObject entityJson = entityArr.getJSONObject(i);
				JSONObject newJson = new JSONObject();
				newJson.put("legalEntityId", entityJson.getString("id"));
				newJson.put("legalEntityName", entityJson.getString("name"));
				if(entityJson.has("organisationalUnits") && 
						entityJson.getJSONArray("organisationalUnits").length()>0) {
					JSONArray ouArr = entityJson.getJSONArray("organisationalUnits");
					entityJson.remove("organisationalUnits");
					JSONArray newOUArr = new JSONArray();
					
					for(int j=0; j<ouArr.length(); j++) {
						
						JSONObject newOuJson = new JSONObject();
						JSONObject ouJson = ouArr.getJSONObject(j);
						newOuJson.put("ouId", ouJson.getString("id"));
						newOuJson.put("ouName", ouJson.getString("name"));
						newOuJson.put("classificationId", ouJson.getString("classificationId"));
						newOuJson.put("classificationDescription", ouJson.getString("classificationDescription"));
						if(ouPermissionMap.containsKey(ouJson.getString("id"))){
							newOuJson.put("permission", ouPermissionMap.get(ouJson.getString("id")));
							newOuJson.put("isSelected", "true");
						}else {
							newOuJson.put("isSelected", "false");
						}
						newOUArr.put(newOuJson);
					}
					
					newJson.put("ouIds", newOUArr);
					
				}
				
				finalJsonArr.put(newJson);
			}
		}
		
		return finalJsonArr;
	}
}
