package com.kony.adminconsole.campaign.businessdelegate.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.campaign.businessdelegate.api.DataStorageBusinessDelegate;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class DataStorageBusinessDelegateImpl implements DataStorageBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public JSONObject getCustomerApplications(JSONObject payload,Boolean isSME, String deploymentPlatform)
			throws ApplicationException, JSONException, DBPApplicationException, MiddlewareException {

		try {
			String searchPath = null;
				searchPath = "${digitalProfileId}";
			JSONObject query = new JSONObject();
			query.put("value", payload.optString("customerId"));
			query.put("searchPath", searchPath);
			query.put("entityDefinitionCode", payload.optString("entityDefinitionCode"));
			String encodedQuery = URLEncoder.encode(query.toString(), StandardCharsets.UTF_8.toString());
			Map<String, Object> mapPayload = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(deploymentPlatform)) {
				if (StringUtils.equals(deploymentPlatform, "azure"))
					encodedQuery = URLEncoder.encode(encodedQuery.toString(), StandardCharsets.UTF_8.toString());
			}
			mapPayload.put("query", encodedQuery);
			String responseStr = DBPServiceExecutorBuilder.builder()
					.withServiceId(ServiceURLEnum.GET_CUSTOMER_APPLICATIONS.getServiceName())
					.withOperationId(ServiceURLEnum.GET_CUSTOMER_APPLICATIONS.getOperationName())
					.withRequestParameters(mapPayload).build().getResponse();
			diagnostic.prepareInfo("CustomerManagementResourceImpl responseStr :"+responseStr).log();
			diagnostic.prepareInfo("CustomerManagementResourceImpl encodedQuery :"+encodedQuery).log();

			JSONObject response = new JSONObject(responseStr);
			if (!response.has("searchExtracts"))
				return null;

			JSONObject customerApplications = constructCustomerApplicationResult(
					response.optJSONArray("searchExtracts"), payload, isSME);
			return customerApplications;
		} catch (UnsupportedEncodingException | DBPApplicationException exception) {
			String errorMsg = "Error : " + exception.toString() + "..." + exception.getStackTrace()[0].toString();
			alert.prepareError(errorMsg).log();
		}
		return null;
	}

	public JSONObject constructCustomerApplicationResult(JSONArray searchResponse, JSONObject payload, Boolean isSME)
			throws ApplicationException {

		String[] submittedStatuses = { "UnderReview", "Submitted",  "AutoApproved", "AutoDenied", "Denied", "Approved" };
		JSONObject customerApplications = new JSONObject();
		
		JSONArray responseArr = new JSONArray();
		StreamSupport.stream(searchResponse.spliterator(), true)
		.map(item -> ((JSONObject)item))
				.filter(item -> (isSME
						? StringUtils.equalsAnyIgnoreCase(
								new JSONObject(item.getString("entityItemEntry")).getString("Type"), "Owner", "Co-Borrower", "Guarantor", "AUTH.SIGNER", "Non Signer", "Primary Card Holder", "Additional Card Holder", "")
						: StringUtils.equalsIgnoreCase("Owner",
								new JSONObject(item.getString("entityItemEntry")).getString("Type"))))
		.forEach(itemObj -> {
			JSONObject dataObj = new JSONObject();
			JSONObject entry = null;
			if (itemObj.has("key")) {
				try {
					entry = getSection(payload.optString("entityDefinitionCode"), itemObj.getString("key"), "MetaData");
				} catch (JSONException | MiddlewareException | ApplicationException e) {
					System.out.println(e.getMessage());
					alert.prepareError("Failed to fetch entity Item").log();
				}
			}
			JSONObject entryMetaData = new JSONObject(entry.getString("entry"));

			if ( payload.getBoolean("isCDPFlow") || (payload.optString("applicationStatus").equalsIgnoreCase("started")
					&& entryMetaData.optString("Status").equalsIgnoreCase("InProgress"))
					|| (payload.optString("applicationStatus").equalsIgnoreCase("submitted") && Arrays
							.stream(submittedStatuses).anyMatch(entryMetaData.optString("Status")::equalsIgnoreCase))) {
				dataObj.put("lastmodifiedts", entry.optString("updatedDate"));
				dataObj.put("createdts", entry.optString("createdDate"));
				dataObj.put("ApplicationId", itemObj.optString("key"));
				dataObj.put("Customer_id", payload.optString("customerId"));
				dataObj.put("ApplicationStatus", entryMetaData.optString("Status"));
				dataObj.put("ApplicationType", entryMetaData.optString("ApplicationType"));
				dataObj.put("ProductType", entryMetaData.optString("ProductType"));
				dataObj.put("RequestId", entryMetaData.optString("RequestId"));
				responseArr.put(dataObj);
			}
		});
		customerApplications.put("customerapplications", responseArr);
		return customerApplications;
	}
	
	public JSONObject getEntityItem(String entityDefintionCode, String trackingCode, String entityItem)
			throws ApplicationException, JSONException, MiddlewareException {
		try {
			Map<String, Object> mapPayload = new HashMap<String, Object>();
			mapPayload.put("key", trackingCode);
			mapPayload.put("name", entityItem);
			mapPayload.put("type", "JSON");
			mapPayload.put("entityDefinitionCode", entityDefintionCode);
			JSONObject obj = new JSONObject(DBPServiceExecutorBuilder.builder()
					.withServiceId(ServiceURLEnum.GET_ODMS_ENTITY_ITEM_BY_KEY_NAME_VERSION_TYPE.getServiceName())
					.withOperationId(ServiceURLEnum.GET_ODMS_ENTITY_ITEM_BY_KEY_NAME_VERSION_TYPE.getOperationName())
					.withRequestParameters(mapPayload).build().getResponse());
			return obj;
		} catch (DBPApplicationException exception) {
			alert.prepareError("Failed to fetch entity Item by Id").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22084);
		}
	}
	
	public JSONObject getSection(String entityDefCode, String trackingCode, String sectionName)
			throws ApplicationException, JSONException, MiddlewareException {
		JSONObject responseObj = getEntityItem(entityDefCode, trackingCode, sectionName);
		if (responseObj.has("entityItems"))
			if (responseObj.getJSONArray("entityItems").length() != 0) 
				return responseObj.getJSONArray("entityItems").getJSONObject(0);
		return new JSONObject();
	}

	public JSONObject getEntityItemEntry(String entityDefinitionId) throws ApplicationException {
		Map<String, Object> mapPayload = new HashMap<String, Object>();
		mapPayload.put("entityDefintionId", entityDefinitionId);
		try {
			String responseStr = DBPServiceExecutorBuilder.builder()
					.withServiceId(ServiceURLEnum.GET_ODMS_ENTITY_ITEM_BY_ID.getServiceName())
					.withOperationId(ServiceURLEnum.GET_ODMS_ENTITY_ITEM_BY_ID.getOperationName())
					.withRequestParameters(mapPayload).build().getResponse();
			return new JSONObject(responseStr);
		} catch (JSONException | DBPApplicationException | MiddlewareException exception) {
			alert.prepareError("Failed to fetch entity Item by Id :" + entityDefinitionId).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22084);
		}
	}
	
	public JSONObject getEntityItemByKeyNameVersionType(String entityDefinitionCode, String key,  String name,  String version, String type)
			throws DBPApplicationException, Exception {
		try {
			Map<String, Object> mapPayload = new HashMap<String, Object>();
			mapPayload.put("key", key);
			mapPayload.put("name", name);
			mapPayload.put("version", version);
			mapPayload.put("type", type);
			mapPayload.put("entityDefinitionCode", entityDefinitionCode);
			JSONObject obj = new JSONObject(DBPServiceExecutorBuilder.builder()
					.withServiceId(ServiceURLEnum.GET_ODMS_ENTITY_ITEM_BY_KEY_NAME_VERSION_TYPE.getServiceName())
					.withOperationId(ServiceURLEnum.GET_ODMS_ENTITY_ITEM_BY_KEY_NAME_VERSION_TYPE.getOperationName())
					.withRequestParameters(mapPayload).build().getResponse());
			if (obj.optInt("opstatus") != 0)
				throw new ApplicationException(ErrorCodeEnum.ERR_22084);
			else {
				JSONObject response = new JSONObject(obj.getJSONArray("entityItems").get(0).toString());
				return new JSONObject(response.optString("entry"));
			}
		} catch (JSONException | DBPApplicationException | MiddlewareException exception) {
			alert.prepareError("Failed to fetch entity Item for key->:" + key + "  name->:" + name + "  type->:" + type).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22084);
		}
	}

}
