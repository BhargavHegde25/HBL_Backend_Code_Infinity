package com.kony.adminconsole.service.usermanagement.businessdelegate.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeePermissionBusinessDelegate;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class EmployeePermissionBusinessDelegateImpl implements EmployeePermissionBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final String OUTPUT_DBP_ERR_CODE = "dbpErrCode";
	private static final String OUTPUT_DBP_ERR_MESSAGE = "dbpErrMsg";
	private static final String OUTPUT_OPSTATUS = "opstatus";

	@Override
	public JSONObject getEmployeePermissionDetails(Map<String, Object> postParametersMap) throws DBPApplicationException {
		
		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> inputParams =  new HashMap<String, Object>();
		if(postParametersMap.containsKey("permissionId")) {
			inputParams.put("$filter","permissionId eq "+postParametersMap.get("permissionId"));
		}

		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNALPERMISSION_VIEW_GET)
					.withRequestParameters(inputParams)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
			
			if(responseJSON.has("internalpermission_view")) {
				
				inputParams =  new HashMap<String, Object>();
				inputParams.put("legalEntities","*");
				
				JSONObject entityIdsMS = fetchLegalEntityList(inputParams);
				Map<String, JSONObject> entityIdMap =  prepareEntityIdJSONObjectMap(entityIdsMS);
				
				JSONArray jsonArr = responseJSON.getJSONArray("internalpermission_view");
				for(int i=0; i<jsonArr.length(); i++) {
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					if(jsonObj.has("legalEntityIds")) {
						String leIds = jsonObj.getString("legalEntityIds");
						String []values = leIds.split(",");
						Set<String> hashSet = new HashSet<String>(Arrays.asList(values));
						JSONArray entityArr = getLegalEntityDetailsById(hashSet,entityIdMap);
						jsonObj.put("legalEntityIds", entityArr);
					}
				}
				
				if(postParametersMap.containsKey("permissionId")) {
					String masterFeatureResponse = DBPServiceExecutorBuilder.builder()
							.withServiceId(ServiceId.INTERNAL_USER_FEATURE_MANAGEMENT)
							.withOperationId(OperationName.OP_GET_ALL__INTERNAL_FEATURE_ACTIONS)
							.withRequestParameters(null)
							.build()
							.getResponse();
					JSONObject masterFeatures = CommonUtilities.getStringAsJSONObject(masterFeatureResponse);
					
					
					inputParams =  new HashMap<String, Object>();
					inputParams.put("$filter","permissionId eq "+postParametersMap.get("permissionId"));
					String internalPermissionFeatureAction = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
						.withOperationId(OperationName.DB_INTERNALPERMISSION_FEATUREACTION_VIEW_GET)
						.withRequestParameters(inputParams)
						.build()
						.getResponse();
					JSONObject internalPermissionFeatureActionJson = CommonUtilities.getStringAsJSONObject(internalPermissionFeatureAction);
					
					JSONArray permissionFeatureActionArray = internalPermissionFeatureActionJson.getJSONArray("internalpermission_featureaction_view");
					
					permissionFeatureActionArray = prepareFeatureArry(masterFeatures.getJSONArray("features"),permissionFeatureActionArray);
					JSONObject jsonObj =responseJSON.getJSONArray("internalpermission_view").getJSONObject(0);
					jsonObj.put("features", permissionFeatureActionArray);
				}
			}
			
			
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While fetching Permission: "+ e).log();
		}
		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation internalpermission_view. Response" + serviceResponse).log();
		}
		
		return responseJSON;
	}

	@Override
	public JSONObject createEmployeePermission(Map<String, Object> postParametersMap) throws DBPApplicationException {


		JSONObject finalResponse = new JSONObject();
		String permissionId = CommonUtilities.getNewId().toString();
		String loggedInUser= postParametersMap.get("createdby").toString();

		/*
		 * Create Permission
		 */
		try {
			String name = postParametersMap.get("permissionName").toString();
			String description= postParametersMap.get("description").toString();
			String status= postParametersMap.get("status").toString().toString();

			createPermission(permissionId, name, description, status, loggedInUser);
			finalResponse.put("Status_PermissionCreate", "success");
		}catch(Exception exp) {
			finalResponse.append(OUTPUT_DBP_ERR_CODE, "22163");
			finalResponse.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to create Permission");
			return finalResponse;
		}

		/*
		 * Create Legal Entity for Permission
		 */
		try {

			@SuppressWarnings("unchecked")
			Set<String>  legalEntitySet = (Set<String>)postParametersMap.get("legalEntitySet");
			createPermissionLegalEntity(permissionId, legalEntitySet,loggedInUser);
			finalResponse.put("Status_PermissionLE", "success");

		}catch(Exception exp) {
			finalResponse.put("Status_PermissionLE", "failed");
		}

		/*
		 * Create actions for Permission
		 */
		try {
			@SuppressWarnings("unchecked")
			Set<String>  actionSet = (Set<String>)postParametersMap.get("actionSet");
			createPermissionActions(permissionId, actionSet,loggedInUser);
			finalResponse.put("Status_PermissionAction", "success");
		}catch(Exception exp) {
			finalResponse.put("Status_PermissionAction", "failed");
		}
		
		finalResponse.put(OUTPUT_OPSTATUS,0);
		finalResponse.put("permissionId", permissionId);

		return finalResponse;
	}

	@Override
	public JSONObject updateEmployeePermissionDetails(Map<String, Object> postParametersMap) throws DBPApplicationException {


		JSONObject finalResponse = new JSONObject();
		String permissionId = postParametersMap.get("permissionId").toString();
		String loggedInUser= postParametersMap.get("modifiedby").toString();

		try {

			updateEmployeePermissionBasicDetails(postParametersMap);
			finalResponse.put("Status_PermissionLE_Update", "success");

		}catch(Exception exp) {
			finalResponse.put("Status_Permission_Update", "failed");
		}

		/*
		 * Create Legal Entity for Permission
		 */
		try {

			@SuppressWarnings("unchecked")
			Set<String>  legalEntitySet = (Set<String>)postParametersMap.get("legalEntitySet");
			deletePermissionLegalEntity(permissionId);
			createPermissionLegalEntity(permissionId, legalEntitySet,loggedInUser);
			finalResponse.put("Status_PermissionLE_Update", "success");

		}catch(Exception exp) {
			finalResponse.put("Status_PermissionLE_Update", "failed");
		}

		/*
		 * Create actions for Permission
		 */
		try {
			@SuppressWarnings("unchecked")
			Set<String>  actionSet = (Set<String>)postParametersMap.get("actionSet");
			deletePermissionActions(permissionId);
			createPermissionActions(permissionId, actionSet,loggedInUser);
			finalResponse.put("Status_PermissionAction_Update", "success");
		}catch(Exception exp) {
			finalResponse.put("Status_PermissionAction_Update", "failed");
		}

		finalResponse.put(OUTPUT_OPSTATUS,0);
		return finalResponse;
	}

	@Override
	public JSONObject updateEmployeePermissionBasicDetails(Map<String, Object> postParametersMap) throws DBPApplicationException {


		Map<String, Object> inputMap = new HashMap<>();
		String permissionId = postParametersMap.get("permissionId").toString();
		String permissionName = postParametersMap.get("permissionName").toString();
		String description = postParametersMap.get("description").toString();
		String status =  postParametersMap.get("status").toString();
		String loggedInUser= postParametersMap.get("modifiedby").toString();

		inputMap.put("id", permissionId);
		inputMap.put("Name", permissionName);
		inputMap.put("Description", description);
		inputMap.put("Status_id", status);
		inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
		inputMap.put("modifiedby", loggedInUser);
		String serviceResponse = null;
		JSONObject responseJSON = null;
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNALPERMISSION_UPDATE)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While updating Permission status: "+ e).log();
		}

		return responseJSON;

	}


	@Override
	public JSONObject updateEmployeePermissionStatus(Map<String, Object> postParametersMap) throws DBPApplicationException {


		Map<String, Object> inputMap = new HashMap<>();

		String permissionId = postParametersMap.get("permissionId").toString();
		String status =  postParametersMap.get("status").toString();
		String loggedInUser= postParametersMap.get("modifiedby").toString();

		inputMap.put("id", permissionId);
		inputMap.put("Status_id", status);
		inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
		inputMap.put("modifiedby", loggedInUser);
		String serviceResponse = null;
		JSONObject responseJSON = null;
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNALPERMISSION_UPDATE)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While updating Permission status: "+ e).log();
		}

		return responseJSON;

	}

	@Override
	public JSONObject fetchLegalEntityList(Map<String, Object> postParametersMap) throws DBPApplicationException {


		Map<String, Object> inputMap = new HashMap<>();

		if(null != postParametersMap && postParametersMap.containsKey("legalEntities")) {
			inputMap.put("legalEntities", postParametersMap.get("legalEntities").toString());
		}

		if(null != postParametersMap && postParametersMap.containsKey("organisationalUnits")) {
			inputMap.put("organisationalUnits", postParametersMap.get("organisationalUnits").toString());
		}
		
		String serviceResponse = null;
		JSONObject responseJSON = null;
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.FIOS_ORG_STRUCTURE_SERVICE)
					.withOperationId(OperationName.OP_GET_FINANTIAL_INSTITUTION)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While fetching Legal Entity List: "+ e).log();
		}

		return responseJSON;

	}

	private void createPermission(String permissionId, String name, String description, String status, 
			String loggedInUser) throws ApplicationException {

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("id", permissionId);
		inputMap.put("Name", name);
		inputMap.put("Status_id", status);
		inputMap.put("Parent_id", permissionId);
		inputMap.put("Description", description);
		inputMap.put("createdby", loggedInUser);
		inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
		String serviceResponse = null;
		JSONObject responseJSON = null;
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNALPERMISSION_CREATE)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While creating Permission: "+ e).log();
		}


		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20546);
		}

	}

	public void createPermissionActions(String permissionId, Set<String> permissionsSet, String loggedInUser) throws ApplicationException {

		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> inputMap = new HashMap<>();

		for (String permission :  permissionsSet) {
			String id = CommonUtilities.getNewId().toString();
			inputMap.put("id", id);
			inputMap.put("permissionId", permissionId);
			inputMap.put("actionId", permission);
			inputMap.put("createdby", loggedInUser);
			inputMap.put("modifiedby", "NULL");
			inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
			inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
			inputMap.put("synctimestamp", CommonUtilities.getISOFormattedLocalTimestamp());
			inputMap.put("softdeleteflag", "0");

			try {
				serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
						.withOperationId(OperationName.DB_PERMISSIONACTION_CREATE)
						.withRequestParameters(inputMap)
						.build()
						.getResponse();
				responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
			} catch (DBPApplicationException e) {
				alert.prepareError("Exception While creating Permission Actions: "+ e).log();
			}
			if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
					|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
				alert.prepareError("Failed CRUD Operation permissionaction create. Response" + serviceResponse).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20528);
			}
			inputMap.clear();
		}
	}

	public void createPermissionLegalEntity(String permissionId, Set<String> legalEntitySet, String loggedInUser) throws ApplicationException {

		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> inputMap = new HashMap<>();

		for (String le :  legalEntitySet) {
			inputMap.put("permissionId", permissionId);
			inputMap.put("legalEntityId", le);
			inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
			inputMap.put("modifiedby", CommonUtilities.getISOFormattedLocalTimestamp());
			inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

			try {
				serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
						.withOperationId(OperationName.DB_PERMISSIONLEGALENTITY_CREATE)
						.withRequestParameters(inputMap)
						.build()
						.getResponse();
				responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
			} catch (DBPApplicationException e) {
				alert.prepareError("Exception While creating Permission Legal Entity: "+ e).log();
			}
			if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
					|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
				alert.prepareError("Failed CRUD Operation permissionlegalentity create. Response" + serviceResponse).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_22170);
			}
			inputMap.clear();
		}
	}

	private void deletePermissionActions(String permissionId) throws ApplicationException {

		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		requestParameters.put("_permissionId", permissionId);

		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNAL_PERMISSION_ACTION_DELETE_PROC)
					.withRequestParameters(requestParameters)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While creating permission action: "+ e).log();
		}
		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation permissionaction delete. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22173);
		}
	}

	private void deletePermissionLegalEntity(String permissionId) throws ApplicationException {

		String serviceResponse = null;
		JSONObject responseJSON = null;
		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		requestParameters.put("_permissionId", permissionId);
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNAL_PERMISSION_ENTITY_DELETE_PROC)
					.withRequestParameters(requestParameters)
					.build()
					.getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception While deleting Permission Legal Entity: "+ e).log();
		}
		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation permissionlegalentity delete. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22172);
		}
	}
	
	private JSONArray getLegalEntityDetailsById(Set<String> legalEntityIds, Map<String,JSONObject> entityMap) {
		
		JSONArray entityArray = new JSONArray();
		try {
			
			for(String entityId : legalEntityIds) {
				
				if(entityMap.containsKey(entityId)) {
					entityArray.put(entityMap.get(entityId));
				}
			}
		} catch (Exception e) {
			
			alert.prepareError("Encountered exception within getLegalEntityDetailsById").log();
		}
		
		return entityArray;
	}
	
	private Map<String,JSONObject> prepareEntityIdJSONObjectMap(JSONObject entityIdsMS){
		
		Map<String, JSONObject> entityMap = new HashMap<>();
		JSONArray entityArr = entityIdsMS !=null? entityIdsMS.getJSONArray("financialInstitutions") : null;
		if(null !=entityArr && entityArr.length() > 0) {
			
			for(int i=0; i<entityArr.length(); i++) {
				
				JSONObject entityJson = entityArr.getJSONObject(i);
				entityMap.put(entityJson.getString("id"),entityJson);
			}
		}
		
		return entityMap;
	}
	
	private JSONArray prepareFeatureArry(JSONArray masterData, JSONArray featureData) {
		
		JSONArray finalArray = new JSONArray();
		
		if(featureData.length() == 0) {
			JSONObject feature = null;
			JSONArray actions = null;
			for(int i=0; i<masterData.length(); i++) {
				JSONObject featureObject = new JSONObject();
				feature = masterData.getJSONObject(i);
				featureObject.put("isSelected", "false");
				featureObject.put("featureId", feature.getString("id"));
				featureObject.put("featureName", feature.getString("name"));
				featureObject.put("featureDescription", feature.getString("description"));
				featureObject.put("status", feature.getString("status"));
				JSONArray actionArray = new JSONArray();
				actions = feature.getJSONArray("actions");
				for(int j=0 ; j<actions.length(); j++) {
					JSONObject action = actions.getJSONObject(j);
					JSONObject actionObject = new JSONObject();
					actionObject.put("isSelected", "false");
					actionObject.put("actionId", action.getString("id"));
					actionObject.put("actionDescription", action.getString("description"));
					actionObject.put("actionName", action.getString("name"));
					actionObject.put("status", action.getString("status"));
					
					if(action.has("dependentActions")) {
						actionObject.put("dependentActions", action.get("dependentActions"));
					}else {
						actionObject.put("dependentActions", new JSONArray());
					}
					
					actionArray.put(actionObject);
				}
				featureObject.put("actions", actionArray);
				finalArray.put(featureObject);
			}
		} else {
			
			Map<String, Set<String>> featureMap = new HashMap<>();
			for(int i=0; i<featureData.length(); i++) {
				
				JSONObject featureJson = featureData.getJSONObject(i);
				
				if(featureMap.containsKey(featureJson.getString("featureId"))) {
					featureMap.get(featureJson.getString("featureId")).add(featureJson.getString("actionId"));
				}else {
					Set<String> actions = new HashSet<String>();
					actions.add(featureJson.getString("actionId"));
					featureMap.put(featureJson.getString("featureId"), actions);
				}
			}
			
			JSONObject feature = null;
			JSONArray actions = null;
			for(int i=0; i<masterData.length(); i++) {
				JSONObject featureObject = new JSONObject();
				feature = masterData.getJSONObject(i);
				String featureId = feature.getString("id");
				boolean skipActionSelected = true; 
				if(featureMap.containsKey(featureId)) {
					featureObject.put("isSelected", "true");
					skipActionSelected = false;
				}	else {
					featureObject.put("isSelected", "false");
				}
				featureObject.put("featureId", featureId);
				featureObject.put("featureName", feature.getString("name"));
				featureObject.put("featureDescription", feature.getString("description"));
				featureObject.put("status", feature.getString("status"));
				JSONArray actionArray = new JSONArray();
				actions = feature.getJSONArray("actions");
				Set<String> selectedActions = featureMap.get(featureId);
				for(int j=0 ; j<actions.length(); j++) {
					JSONObject action = actions.getJSONObject(j);
					JSONObject actionObject = new JSONObject();
					if(skipActionSelected || !selectedActions.contains(action.getString("id"))) {
						actionObject.put("isSelected", "false");
					}else {
						actionObject.put("isSelected", "true");
					}
					actionObject.put("actionId", action.getString("id"));
					actionObject.put("actionDescription", action.getString("description"));
					actionObject.put("actionName", action.getString("name"));
					actionObject.put("status", action.getString("status"));
					actionArray.put(actionObject);
				}
				featureObject.put("actions", actionArray);
				finalArray.put(featureObject);
			}
			
		}
		
		return finalArray;
	}

	@Override
	public JSONObject getPermissionsByLegalEnities(Map<String, Object> postParametersMap)
			throws DBPApplicationException {
		String serviceResponse = null;
		JSONObject responseJSON = null;
		JSONObject response = new JSONObject();
		Map<String, Object> inputParams = new HashMap<>();

		try {
			JsonArray legalEntities = new JsonParser().parse(postParametersMap.get("legalEntities").toString())
					.getAsJsonArray();
			Set<String> legalEntitiesSet = new HashSet<>();
			for (JsonElement ele : legalEntities) {
				legalEntitiesSet.add(ele.getAsString());
			}
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_INTERNALPERMISSION_VIEW_GET).withRequestParameters(inputParams)
					.build().getResponse();
			responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);

			if (responseJSON.has("internalpermission_view")) {
				JSONArray jsonArr = responseJSON.getJSONArray("internalpermission_view");
				Map<String, JSONArray> legalEntityPermissionMap = prepareLegalEntityPermissionMap(jsonArr);
				for (String leid : legalEntitiesSet) {
					if (legalEntityPermissionMap.containsKey(leid)) {
						response.put(leid, legalEntityPermissionMap.get(leid));
					}
				}

			}

		} catch (DBPApplicationException e) {
			response.put("dbpErrMsg", "Exception While fetching Permission");
			alert.prepareError("Exception While fetching Permission: " , e).log();
		}
		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			response.put("dbpErrMsg", "Failed CRUD Operation internalpermission_view. Response");
			alert.prepareError("Failed CRUD Operation internalpermission_view. Response" + serviceResponse).log();
		}

		return response;
	}

	private Map<String, JSONArray> prepareLegalEntityPermissionMap(JSONArray jsonArr) {
		Map<String, JSONArray> legalEntityPermissionMap = new HashMap<>();
		try {
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				if (jsonObj.has("legalEntityIds")) {
					String leIds = jsonObj.getString("legalEntityIds");
					String[] values = leIds.split(",");
					for (String le : values) {
						JSONObject obj = new JSONObject();
						obj.put("permissionId", jsonObj.get("permissionId"));
						obj.put("permissionName", jsonObj.get("permissionName"));
						if (legalEntityPermissionMap.containsKey(le)) {
							JSONArray arr = legalEntityPermissionMap.get(le);
							arr.put(obj);
							legalEntityPermissionMap.put(le, arr);
						} else {
							JSONArray arr = new JSONArray();
							arr.put(obj);
							legalEntityPermissionMap.put(le, arr);
						}
					}
				}
			}
		} catch (Exception e) {

			alert.prepareError("Exception ", e).log();

		}

		return legalEntityPermissionMap;

	}
}
