package com.kony.adminconsole.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class BusinessTypeHandler {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	public static Result removeSignatoryTypes(String id, DataControllerRequest requestInstance) {
		Result result = new Result();
		Param result_param = new Param();
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.clear();
		postParametersMap.put(ODataQueryConstants.FILTER,"BusinessType_id eq '" + id + "'");
		String removeSignatoryTypeResponse = Executor.invokeService(ServiceURLEnum.BUSINESSSIGNATORY_DELETE,
				postParametersMap, null, requestInstance);
		JSONObject removeSignatoryTypeResponseJSON = CommonUtilities.getStringAsJSONObject(removeSignatoryTypeResponse);

		if (removeSignatoryTypeResponseJSON != null && removeSignatoryTypeResponseJSON.has(FabricConstants.OPSTATUS)
				&& removeSignatoryTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.DELETE,
					ActivityStatusEnum.SUCCESSFUL, "Signatory type update successful. id '" + id + "'.");
			result_param = new Param("Group_id", id.toString(), FabricConstants.STRING);
			result.addParam(result_param);
		} else {
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21339.setErrorCode(result);
			alert.prepareError("Error in deleting signatory types.").log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.DELETE,
					ActivityStatusEnum.FAILED, "Signatories delete failed");
		}
		return result;
	}

	public static Map<String, String> getSignatoryNameMapping(DataControllerRequest requestInstance)
			throws ApplicationException {
		Result result = new Result();
		Map<String, String> postParametersMap = new HashMap<String, String>();
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.SIGNATORYTYPE_READ, postParametersMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("signatorytype")) {
			result.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			alert.prepareError("Failed to read signatory types").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21859);
		}
		Map<String, String> resultMap = new HashMap<>();
		JSONArray signatories = readResponse.getJSONArray("signatorytype");
		for (int i = 0; i < signatories.length(); i++) {
			JSONObject record = signatories.getJSONObject(i);
			String key = record.getString("name");
			resultMap.put(key, record.getString("id"));
		}
		return resultMap;
	}

	private static String getId(String key, Map<String, String> existingMap) {
		if (existingMap.containsKey(key)) {
			return existingMap.get(key);
		}
		return String.valueOf(CommonUtilities.getNewId());
	}

	public static Result updateSignatoryType(String id, String name, String authToken,
			DataControllerRequest requestInstance) {
		Result result = new Result();
		Param result_param = new Param();
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.clear();
		postParametersMap.put("id", id);
		postParametersMap.put("name", name);
		String updateSignatoryTypeResponse = Executor.invokeService(ServiceURLEnum.SIGNATORYTYPE_UPDATE,
				postParametersMap, null, requestInstance);
		JSONObject updateSignatoryTypeResponseJSON = CommonUtilities.getStringAsJSONObject(updateSignatoryTypeResponse);
		int getOpStatusCode = updateSignatoryTypeResponseJSON.getInt(FabricConstants.OPSTATUS);
		if (getOpStatusCode == 0) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.UPDATE,
					ActivityStatusEnum.SUCCESSFUL, "Signatory type update successful.  Name: '" + name + "'.");
			result_param = new Param("Group_id", id.toString(), FabricConstants.STRING);
			result.addParam(result_param);
			return result;
		} else {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Signatory type update failed. Group Name: '" + name + "'.");
			ErrorCodeEnum.ERR_20402.setErrorCode(result);
			return result;
		}
	}

	private static ServiceURLEnum getServiceURL(String key, Map<String, String> existingMap) {
		if (existingMap.containsKey(key)) {
			return ServiceURLEnum.SIGNATORYTYPE_UPDATE;
		}
		return ServiceURLEnum.SIGNATORYTYPE_CREATE;
	}

	public static Result createBusinessType(String name, JSONArray signatoryTypesArray, String minAuthSignatories,
			String maxAuthSignatories, String authToken, DataControllerRequest requestInstance)
			throws ApplicationException {
		return updateBusinessType(null, name, signatoryTypesArray, minAuthSignatories, maxAuthSignatories, null,
				authToken, requestInstance);
	}

	public static Result updateBusinessType(String id, String name, JSONArray signatoryTypesArray,
			String minAuthSignatories, String maxAuthSignatories, String defaultRole, String authToken,
			DataControllerRequest requestInstance) throws ApplicationException {

		Result result = new Result();
		Param result_param = new Param();
		ServiceURLEnum url; 
		if(StringUtils.isBlank(id)) {
			id = String.valueOf(CommonUtilities.getNewId());
			url = ServiceURLEnum.BUSINESSTYPE_CREATE;
		}else {
			url = ServiceURLEnum.BUSINESSTYPE_UPDATE;			
		}
		
		Map<String, String> postParametersMap = new HashMap<String, String>();
		if (StringUtils.isNotBlank(defaultRole)) {
			String businessType_id = id;
			postParametersMap.clear();
			postParametersMap.put("_businessTypeId", businessType_id);
			postParametersMap.put("_groupId", defaultRole);
			postParametersMap.put("_isDefault", "1");
			String updateBusinessTypeGroupResponse = Executor.invokeService(
					ServiceURLEnum.BUSINESSTYPE_DEFAULTGROUP_UPDATE_PROC, postParametersMap, null, requestInstance);

			JSONObject updateBusinessTypeGroupsResponseJSON = CommonUtilities
					.getStringAsJSONObject(updateBusinessTypeGroupResponse);
			if ((updateBusinessTypeGroupsResponseJSON != null)
					&& updateBusinessTypeGroupsResponseJSON.has(FabricConstants.OPSTATUS)
					&& updateBusinessTypeGroupsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				diagnostic.prepareDebug("Business type default group updated successfully.").log();
			} else {
				alert.prepareError("Business type default group update failed with business type name '" + name + "'.").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21367.setErrorCode(result);
				return result;
			}
		}
		Map<String, String> existingMap = getSignatoryNameMapping(requestInstance);
		for (int i = 0; i < signatoryTypesArray.length(); i++) {
			String signatorytype_name = signatoryTypesArray.getString(i);
			String key = signatorytype_name;
			postParametersMap.clear();
			postParametersMap.put("id", getId(key, existingMap));
			postParametersMap.put("name", signatorytype_name);
			ServiceURLEnum serviceURL = getServiceURL(key, existingMap);
			if(serviceURL == ServiceURLEnum.SIGNATORYTYPE_CREATE)
			{
				JSONObject createResponse = CommonUtilities.getStringAsJSONObject(
						Executor.invokeService(serviceURL, postParametersMap, null, requestInstance));
				if (createResponse == null || !createResponse.has(FabricConstants.OPSTATUS)
						|| createResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Signatory type update failed with business type name '" + name + "'.");
					ErrorCodeEnum.ERR_20404.setErrorCode(result);
					return result;
				}
			}
		}
		postParametersMap.clear();
		postParametersMap.put("id", id);
		postParametersMap.put("name", name);
		postParametersMap.put("minAuthSignatory", minAuthSignatories);
		postParametersMap.put("maxAuthSignatory", StringUtils.isBlank(maxAuthSignatories)?"":maxAuthSignatories);
		String updateBusinessTypeResponse = Executor.invokeService(url,
				postParametersMap, null, requestInstance);
		JSONObject updateBusiessTypeResponseJSON = CommonUtilities.getStringAsJSONObject(updateBusinessTypeResponse);
		if (updateBusiessTypeResponseJSON != null && updateBusiessTypeResponseJSON.has(FabricConstants.OPSTATUS)
				&& updateBusiessTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.UPDATE,
					ActivityStatusEnum.SUCCESSFUL, "Business type updated successfully. Business Type Name: " + name);
			result_param = new Param("businessType_id", id.toString(), FabricConstants.STRING);
			result.addParam(result_param);			
		} else {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Business type update failed with business type name '" + name + "'.");
			ErrorCodeEnum.ERR_20404.setErrorCode(result);
			return result;
		}
		existingMap = getSignatoryNameMapping(requestInstance);
		for (int i = 0; i < signatoryTypesArray.length(); i++) {
			String signatorytype_name = signatoryTypesArray.getString(i);
			String key = signatorytype_name;			
			postParametersMap.clear();
			postParametersMap.put("BusinessType_id", id.toString());
			postParametersMap.put("Signatory_id", existingMap.get(key));
			String updateBusinessSignatoryResponse = Executor.invokeService(ServiceURLEnum.BUSINESSSIGNATORY_CREATE,
					postParametersMap, null, requestInstance);
			JSONObject updateBusiessSignatoryResponseJSON = CommonUtilities.getStringAsJSONObject(updateBusinessSignatoryResponse);
			if (updateBusiessSignatoryResponseJSON != null && updateBusiessSignatoryResponseJSON.has(FabricConstants.OPSTATUS)
					&& updateBusiessSignatoryResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.UPDATE,
						ActivityStatusEnum.SUCCESSFUL, "Business signatory updated successfully. Business Type Name: " + name);
				result_param = new Param("businessType_id", id.toString(), FabricConstants.STRING);
				result.addParam(result_param);			
			} else {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSTYPE, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED, "Business type update failed with business type name '" + name + "'.");
				ErrorCodeEnum.ERR_20404.setErrorCode(result);
				return result;
			}
		}
		return result;
	}
	public static Map<String, String> getBusinessTypeCustomers(String businessType_id, DataControllerRequest requestInstance) {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.clear();
		postParametersMap.put(ODataQueryConstants.FILTER,"BusinessType_id eq '" + businessType_id + "'");
		
		String readBusinessTypeCustomersResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERBUSINESSTYPE_READ,
				postParametersMap, null, requestInstance);
		JSONObject readBusinessTypeCustomersResponseJSON = CommonUtilities
				.getStringAsJSONObject(readBusinessTypeCustomersResponse);

		if (readBusinessTypeCustomersResponseJSON != null && readBusinessTypeCustomersResponseJSON.has(FabricConstants.OPSTATUS)
				&& readBusinessTypeCustomersResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
			Map<String, String> resultMap = new HashMap<>();
			JSONArray businessTypeCustomers = readBusinessTypeCustomersResponseJSON.getJSONArray("customerbusinesstype");			
			for (int i = 0; i < businessTypeCustomers.length(); i++) {
				JSONObject record = businessTypeCustomers.getJSONObject(i);
				String key = record.getString("BusinessType_id");
				key += record.getString("SignatoryType_id");
				resultMap.put(key, "1");
			}
			return resultMap;
		}
		return null;
	}
	public static Dataset getAuthorizedSignatories(String businessType_id, DataControllerRequest requestInstance) {

		Dataset signatoriesDataSet = new Dataset();
		Map<String, String> businessTypeCustomersMap = getBusinessTypeCustomers(businessType_id,requestInstance);

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.clear();
		postParametersMap.put(ODataQueryConstants.FILTER,"BusinessType_id eq '" + businessType_id + "'");

		String readBusinessSignatoryResponse = Executor.invokeService(ServiceURLEnum.BUSINESSSIGNATORY_READ,
				postParametersMap, null, requestInstance);
		JSONObject readBusinessSignatoryResponseJSON = CommonUtilities
				.getStringAsJSONObject(readBusinessSignatoryResponse);

		if (readBusinessSignatoryResponseJSON != null && readBusinessSignatoryResponseJSON.has(FabricConstants.OPSTATUS)
				&& readBusinessSignatoryResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readBusinessSignatoryResponseJSON.has("businesssignatory")) {
			JSONArray readBusinessSignatoryJSONArray = readBusinessSignatoryResponseJSON
					.getJSONArray("businesssignatory");
			signatoriesDataSet.setId("Signatories");
			for (int indexVar = 0; indexVar < readBusinessSignatoryJSONArray.length(); indexVar++) {
				JSONObject currJSONObject = readBusinessSignatoryJSONArray.getJSONObject(indexVar);
				Record currRecord = new Record();
				Param Signatory_id_Param = new Param("signatorytype_id", currJSONObject.getString("Signatory_id"),
						FabricConstants.STRING);
				String key = businessType_id;
				key += currJSONObject.getString("Signatory_id");
				currRecord.addParam(Signatory_id_Param);							
				postParametersMap.clear();
				postParametersMap.put(ODataQueryConstants.FILTER,
						"id eq '" + Signatory_id_Param.getValue().toString() + "'");
				String readSignatoryResponse = Executor.invokeService(ServiceURLEnum.SIGNATORYTYPE_READ,
						postParametersMap, null, requestInstance);
				JSONObject readSignatoryResponseJSON = CommonUtilities.getStringAsJSONObject(readSignatoryResponse);
				if (readSignatoryResponseJSON != null && readSignatoryResponseJSON.has(FabricConstants.OPSTATUS)
						&& readSignatoryResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
					JSONArray readSignatoryJSONArray = readSignatoryResponseJSON.getJSONArray("signatorytype");
					if (readSignatoryJSONArray.length() != 0) {
						JSONObject currSignatoryJSONObject = readSignatoryJSONArray.getJSONObject(0);
						String name_String = "";
						if (currSignatoryJSONObject.has("name")) {
							name_String = currSignatoryJSONObject.getString("name");
						}
						Param name_Param = new Param("name", name_String, FabricConstants.STRING);
						currRecord.addParam(name_Param);
						Param customers_Param = new Param("customers", businessTypeCustomersMap.get(key)==null?"0":"1", FabricConstants.STRING);
						currRecord.addParam(customers_Param);						
					}
				}
				signatoriesDataSet.addRecord(currRecord);
			}
		}
		return signatoriesDataSet;
	}

}
