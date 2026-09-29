package com.kony.adminconsole.licensing.businessdelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.licensing.backenddelegate.api.LicensingBackendDelegate;
import com.kony.adminconsole.licensing.businessdelegate.api.LicensingBusinessDelegate;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.konylabs.middleware.dataobject.Result;

public class LicensingBusinessDelegateImpl implements LicensingBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	/*
	 * getExternalUsersCount - to get the count of External Users External Users -
	 * Retail EndUsers, Business EndUsers, Wealth EndUsers
	 */

	LicensingBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(LicensingBackendDelegate.class);

	@Override
	public Result getExternalUsersCount() {

		Result result = new Result();
		int RetailEndUsers = 0;
		int BussinessEndUsers = 0;
		int WealthEndUsers = 0;
		JSONObject customerResponseJSON = backendDelegate.getCustomerIds();
		if (customerResponseJSON != null && customerResponseJSON.has(FabricConstants.OPSTATUS)
				&& customerResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
				&& customerResponseJSON.has(ACConstants.CUSTOMER)) {
			JSONArray customerJSONArray = customerResponseJSON.getJSONArray(ACConstants.CUSTOMER);
			if (customerJSONArray.length() > 0) {
				for (int i = 0; i < customerJSONArray.length(); i++) {
					JSONObject currCustomerObject = customerJSONArray.getJSONObject(i);
					if (currCustomerObject.has(ACConstants.ID_LOWERCASE)
							&& currCustomerObject.has(ACConstants.COMPANY_LEGAL_UNIT)) {

						String customerId = currCustomerObject.optString(ACConstants.ID_LOWERCASE);
						String legalEntityId = currCustomerObject.optString(ACConstants.COMPANY_LEGAL_UNIT);
						ArrayList<String> featureIds = backendDelegate.getUserActions(customerId, legalEntityId);
						if (featureIds.size() > 0) {

							JSONObject typeidsJSON = backendDelegate.getTypeidForFeature(featureIds);
							JSONArray typeIDJSONArray=new JSONArray();
							if(typeidsJSON==null||!typeidsJSON.has(FabricConstants.OPSTATUS)
					                || typeidsJSON.getInt(FabricConstants.OPSTATUS) != 0)
							{
								alert.prepareError("Failed to fetch data from feature_view").log();
								return ErrorCodeEnum.ERR_22210.setErrorCode(new Result());
							}
							typeIDJSONArray=typeidsJSON.getJSONArray("feature_view");
							List<String> typeidsList = getTypeIdsList(typeIDJSONArray);
							if (typeidsList.contains(ACConstants.TYPE_ID_BUSINESS)) {
								BussinessEndUsers++;
							} else if (typeidsList.contains(ACConstants.TYPE_ID_WEALTH)) {
								WealthEndUsers++;
							} else if (typeidsList.contains(ACConstants.TYPE_ID_RETAIL)) {
								RetailEndUsers++;
							}
						}
					}
				}
			}
		}

		result.addParam("RetailEndUsers", "" + RetailEndUsers);
		result.addParam("BusinessEndUsers", "" + BussinessEndUsers);
		result.addParam("WealthEndUsers", "" + WealthEndUsers);
		return result;

	}

	public Result getInternalUsersCount() throws DBPApplicationException {
		String userListResponse = null;
		String serviceName = ServiceId.USERMANAGEMENT;
		String operationName = OperationName.GETUSERLIST;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		userListResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
				.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();

		JSONObject usersResponseObj = new JSONObject(userListResponse);

		if (usersResponseObj == null || !usersResponseObj.has(FabricConstants.OPSTATUS)
				|| usersResponseObj.getInt(FabricConstants.OPSTATUS) != 0|| usersResponseObj.optString("internalusers_view").length()<1) {
			alert.prepareError("Failed to get response from getUserList").log();
			return ErrorCodeEnum.ERR_22204.setErrorCode(new Result());
		}
		JSONObject userresultjson = new JSONObject();
		int retailUsers = 0;
		int bussinessUsers = 0;
		int wealthUsers = 0;
		JSONObject rolesJSON = new JSONObject();
		JSONArray roleDetailsArray=new JSONArray();
		JSONArray servicedefinitionsArray = new JSONArray();

		// Getting RoleIds from Role Table.
		rolesJSON = backendDelegate.getRoleIds();
		if(rolesJSON==null||!rolesJSON.has(FabricConstants.OPSTATUS)
                 || rolesJSON.getInt(FabricConstants.OPSTATUS) != 0)
		{
			alert.prepareError("Failed to fetch data from role table").log();
			return ErrorCodeEnum.ERR_22207.setErrorCode(new Result());
		}
		JSONObject roleInfo=new JSONObject();
		ArrayList<String> roleIds=new ArrayList<String>();
		roleDetailsArray = rolesJSON.getJSONArray("role");
		for (int i = 0; i < roleDetailsArray.length(); i++) {
			roleInfo = (JSONObject) roleDetailsArray.get(i);
			String id = roleInfo.getString("id");
			roleIds.add(id);
		}
		JSONObject servicedefinitionsJSON=new JSONObject();
		// Getting serviceDefinitions that the role has access to, from userroleservicedefinition table.
		servicedefinitionsJSON = backendDelegate.getServiceDefinitions(roleIds);
		if(servicedefinitionsJSON==null||!servicedefinitionsJSON.has(FabricConstants.OPSTATUS)
                || servicedefinitionsJSON.getInt(FabricConstants.OPSTATUS) != 0)
		{
			alert.prepareError("Failed to fetch data from userroleservicedefinition table").log();
			return ErrorCodeEnum.ERR_22208.setErrorCode(new Result());
		}
		servicedefinitionsArray=servicedefinitionsJSON.getJSONArray("userroleservicedefinition");
		
		Map<String, List<String>> roletoServiceDefMapping = new HashMap();
		JSONObject info = new JSONObject();
		ArrayList<String> servicedefinitions = new ArrayList<String>();

		// Mapping servicedefinition to role.
		for (int i = 0; i < servicedefinitionsArray.length(); i++) {
			info = (JSONObject) ((JSONArray) servicedefinitionsArray).get(i);
			String role = info.has("UserRole_id") ? info.getString("UserRole_id") : "";
			String servicedef = info.has("servicedefinitionId") ? info.getString("servicedefinitionId") : "";
			List<String> tempList = new ArrayList<String>();
			tempList = roletoServiceDefMapping.get(role);
			if (tempList == null) {
				tempList = new ArrayList<String>();
				tempList.add(servicedef);
			} else
				tempList.add(servicedef);
			roletoServiceDefMapping.put(role, tempList);
			servicedefinitions.add(servicedef);

		}
		diagnostic.prepareDebug("RoleToServiceDefintion Mapping=" + roletoServiceDefMapping).log();
		JSONArray featuresArray = new JSONArray();
		JSONObject featuresJSON=new JSONObject();
		// Getting the features associated to each servicedefinition
		featuresJSON = backendDelegate.getFeaturesforServiceDefinitions(servicedefinitions);
		if(featuresJSON==null||!featuresJSON.has(FabricConstants.OPSTATUS)
                || featuresJSON.getInt(FabricConstants.OPSTATUS) != 0)
		{
			alert.prepareError("Failed to fetch data from servicedefinition_features_actions_view").log();
			return ErrorCodeEnum.ERR_22209.setErrorCode(new Result());
		}
		featuresArray= featuresJSON.getJSONArray("servicedefinition_features_actions_view");
		Map<String, List<String>> serviceDefToFeatureMapping = new HashMap<>();
		ArrayList<String> featureIdsArray = new ArrayList<String>();

		// Mapping features to servicedefinition.
		for (int i = 0; i < featuresArray.length(); i++) {
			info = (JSONObject) ((JSONArray) featuresArray).get(i);
			String servicedefinition = info.has("serviceDefinitionId") ? info.getString("serviceDefinitionId") : "";
			List<String> tempList = new ArrayList<>();
			tempList = serviceDefToFeatureMapping.get(servicedefinition);
			if (tempList == null) {
				tempList = new ArrayList<String>();
				tempList.add(info.getString("featureId"));

			} else
				tempList.add(info.getString("featureId"));
			serviceDefToFeatureMapping.put(servicedefinition, tempList);
		}

		Map<String, String> serviceDefToCategoryMapping = new HashMap();
		List<String> typeidsList = new ArrayList<String>();
		String category = null;
		JSONArray typeidsArray = new JSONArray();
		JSONObject typeidsJSON=new JSONObject();

		// Mapping category to servicedefinition.
		for (Map.Entry<String, List<String>> servicedefToFeature : serviceDefToFeatureMapping.entrySet()) {
			String servicedefinition = servicedefToFeature.getKey();
			featureIdsArray = (ArrayList<String>) servicedefToFeature.getValue();

			// Getting Typeids of Features from feature_view
			typeidsJSON = backendDelegate.getTypeidForFeature(featureIdsArray);
			if(typeidsJSON==null||!typeidsJSON.has(FabricConstants.OPSTATUS)
	                || typeidsJSON.getInt(FabricConstants.OPSTATUS) != 0)
			{
				alert.prepareError("Failed to fetch data from feature_view").log();
				return ErrorCodeEnum.ERR_22210.setErrorCode(new Result());
			}
			typeidsArray=typeidsJSON.getJSONArray("feature_view");
			typeidsList = getTypeIdsList(typeidsArray);

			category = getCategory(typeidsList);
			serviceDefToCategoryMapping.put(servicedefinition, category);
		}
		diagnostic.prepareDebug("ServiceDefinitionToCategory Mapping=" + serviceDefToCategoryMapping).log();

		// Mapping category to role.
		Map<String, String> roleToCategoryMapping = new HashMap();
		for (Map.Entry<String, List<String>> roleToServicedef : roletoServiceDefMapping.entrySet()) {
			List<String> servicedefinition = roleToServicedef.getValue();
			String roleid = roleToServicedef.getKey();
			Set<String> categories = new HashSet<String>();
			for (Map.Entry<String, String> servicedefToCategory : serviceDefToCategoryMapping.entrySet()) {
				String serviceDefinition = servicedefToCategory.getKey();
				if (servicedefinition.contains(serviceDefinition)) {
					String categoryId = servicedefToCategory.getValue();
					categories.add(categoryId);
				}
			}
			if (categories.contains(ACConstants.TYPE_ID_BUSINESS))
				roleToCategoryMapping.put(roleid, ACConstants.TYPE_ID_BUSINESS);
			else if (categories.contains(ACConstants.TYPE_ID_WEALTH))
				roleToCategoryMapping.put(roleid, ACConstants.TYPE_ID_WEALTH);
			else
				roleToCategoryMapping.put(roleid, ACConstants.TYPE_ID_RETAIL);
		}
		diagnostic.prepareDebug("RoleToCategory Mapping=" + roleToCategoryMapping).log();
		JSONObject internalusersdetails = new JSONObject();
		JSONArray roleDetails = new JSONArray();
		String usercategory = null;
		JSONArray internalusersArray = usersResponseObj.optJSONArray("internalusers_view");
		for (int i = 0; i < internalusersArray.length(); i++) {
			internalusersdetails = (JSONObject) internalusersArray.get(i);
			roleDetails = (JSONArray) internalusersdetails.get("roleDetails");
			if (roleDetails.length() > 0) {
				usercategory = getRoleCategoryDetails(roleDetails, roleToCategoryMapping);
				if (usercategory.equalsIgnoreCase(ACConstants.TYPE_ID_BUSINESS))
					bussinessUsers += 1;
				else if (usercategory.equalsIgnoreCase(ACConstants.TYPE_ID_WEALTH))
					wealthUsers += 1;
				else
					retailUsers += 1;

			} else
				retailUsers += 1;
		}
		userresultjson.put("RetailClientUsers", retailUsers);
		userresultjson.put("BusinessClientUsers", bussinessUsers);
		userresultjson.put("WealthClientUsers", wealthUsers);
		Result result = CommonUtilities.constructResultFromJSONObject(userresultjson);
		return result;
	}

	public List<String> getTypeIdsList(JSONArray typeidsArray) {
		JSONObject tempTypeIDsJSONObject = new JSONObject();
		List<String> tempTypeIdsInputList = new ArrayList<String>();
		List<String> finalTypeIds = new ArrayList<String>();

		for (int i = 0; i < typeidsArray.length(); i++) {

			String tempTypeId = "";
			String tempTypeIdsInput = "";
			tempTypeIDsJSONObject = typeidsArray.getJSONObject(i);

			tempTypeIdsInput = tempTypeIDsJSONObject.optString(ACConstants.TYPE_ID);
			tempTypeIdsInputList = Arrays.asList(tempTypeIdsInput.split(","));

			if (tempTypeIdsInputList.contains(ACConstants.TYPE_ID_RETAIL)) {
				tempTypeId = ACConstants.TYPE_ID_RETAIL;

			} else if (tempTypeIdsInputList.contains(ACConstants.TYPE_ID_WEALTH)) {
				tempTypeId = ACConstants.TYPE_ID_WEALTH;

			} else if (tempTypeIdsInputList.contains(ACConstants.TYPE_ID_BUSINESS)) {
				tempTypeId = ACConstants.TYPE_ID_BUSINESS;
			}

			if (!finalTypeIds.contains(tempTypeId))
				finalTypeIds.add(tempTypeId);
		}

		return finalTypeIds;
	}

	public String getCategory(List<String> typeIds) {
		if (typeIds.size() > 0) {
			if (typeIds.contains(ACConstants.TYPE_ID_BUSINESS))
				return ACConstants.TYPE_ID_BUSINESS;
			else if (typeIds.contains(ACConstants.TYPE_ID_WEALTH))
				return ACConstants.TYPE_ID_WEALTH;
			else
				return ACConstants.TYPE_ID_RETAIL;
		} else
			return ACConstants.TYPE_ID_RETAIL;

	}

	public String getRoleCategoryDetails(JSONArray roleDetails, Map<String, String> roleToCategoryMapping) {
		JSONObject roleinfo = new JSONObject();
		String roleId = null;
		List<String> typeIds = new ArrayList<String>();
		for (int i = 0; i < roleDetails.length(); i++) {
			roleinfo = (JSONObject) roleDetails.get(i);
			roleId = (String) roleinfo.get("roleId");
			if (roleToCategoryMapping.containsKey(roleId)) {
				String userCategory = roleToCategoryMapping.get(roleId);
				typeIds.add(userCategory);
			}
		}
		String category = getCategory(typeIds);
		return category;

	}


}
