package com.kony.adminconsole.licensing.backenddelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.licensing.backenddelegate.api.LicensingBackendDelegate;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.JSONUtil;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class LicensingBackendDelegateImpl implements LicensingBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	/*
	 * getUserId - method to fetch the details of the customer from customer table
	 */

	@Override
	public JSONObject getCustomerIds() {
		JSONObject responseObj = new JSONObject();
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_CUSTOMER_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.SELECT, "id,companyLegalUnit");
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			responseObj = new JSONObject(response);

		} catch (Exception e) {
			alert.prepareError("Caught exception at getUserId : ", e).log();

		}
		return responseObj;

	}

	/*
	 * getUserActions - method to fetch features which the given customer id is
	 * having access to
	 * 
	 * @params - customerId,legalEntityId
	 */

	public ArrayList<String> getUserActions(String customerId, String legalEntityId) {

		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_USER_SECURITYATTRIBUTES_GET_PROC;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("_userId", customerId);
		requestParameters.put("_legalEntityId", legalEntityId);

		String readUserActionResponseResponse = null;
		ArrayList<String> featureIds = new ArrayList<>();
		Map<String, Set<String>> securityAttributesMap = new HashMap<>();

		try {
			readUserActionResponseResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withObjectId(null).withOperationId(operationName).withRequestParameters(requestParameters).build()
					.getResponse();

			JsonParser parser = new JsonParser();
			JsonObject securityAttributesJson = parser.parse(readUserActionResponseResponse).getAsJsonObject();
			JsonArray featuresArray = null;

			if (securityAttributesJson != null && securityAttributesJson.has(ACConstants.DATASET_RECORDS)
					&& securityAttributesJson.get(ACConstants.DATASET_RECORDS) != null
					&& securityAttributesJson.has(ACConstants.DATASET_RECORDS1)
					&& securityAttributesJson.get(ACConstants.DATASET_RECORDS1) != null) {
				featuresArray = securityAttributesJson.get(ACConstants.DATASET_RECORDS1).getAsJsonArray();

				JsonElement featuresElement = featuresArray.size() > 0 ? featuresArray.get(0) : new JsonObject();

				if (featuresElement.isJsonObject()) {

					JsonObject feature = featuresElement.getAsJsonObject();
					if (feature != null && JSONUtil.hasKey(feature, ACConstants.FEATURES)) {

						Set<String> featuresSet = new HashSet<>();
						featuresSet = new HashSet<>(Arrays.asList(StringUtils
								.split(feature.get(ACConstants.FEATURES).getAsString(), ACConstants.COMMA_SEPERATOR)));
						securityAttributesMap.put(ACConstants.FEATURES, featuresSet);

					}
				}

			}

			Set<String> customerFeatureIds = securityAttributesMap.get(ACConstants.FEATURES);

			if (customerFeatureIds != null && !customerFeatureIds.isEmpty()) {
				featureIds = new ArrayList<String>(customerFeatureIds);
			}

			return featureIds;

		} catch (Exception exp) {

			diagnostic.prepareDebug("USER_SECURITYATTRIBUTES_GET_PROC response: " + readUserActionResponseResponse).log();
			diagnostic.prepareDebug("Encounter Exception while trying to fetch Customer Entitlement: ", exp).log();

		}
		return featureIds;

	}

	public JSONObject getServiceDefinitions(ArrayList<String> roles) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_USERROLESERVICEDEFINITION_GET;
		String filterQuery = "";
		filterQuery += "(" + "UserRole_id" + " eq " + String.join(" or " + "UserRole_id" + " eq ", roles) + ")";
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.FILTER, filterQuery);
		requestParameters.put(ODataQueryConstants.SELECT, "UserRole_id,servicedefinitionId");
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			return responseObj;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitions : ", e).log();
			return null;

		}
	}

	public JSONObject getFeaturesforServiceDefinitions(ArrayList<String> servicedefinitions) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITION_FEATURES_ACTIONS_VIEW_GET;
		String filterQuery = "";
		filterQuery += "(" + "serviceDefinitionId" + " eq "
				+ String.join(" or " + "serviceDefinitionId" + " eq ", servicedefinitions) + ")";
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.FILTER, filterQuery);
		requestParameters.put(ODataQueryConstants.SELECT, "serviceDefinitionId,featureId");
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			return responseObj;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getFeaturesForServiceDefinitions : ", e).log();
			return null;

		}
	}

	public JSONObject getTypeidForFeature(ArrayList<String> featureIds) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FEATURE_VIEW_GET;
		String filterQuery = "";
		filterQuery += "(" + "Code" + " eq " + String.join(" or " + "Code" + " eq ", featureIds) + ")";
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.FILTER, filterQuery);
		requestParameters.put(ODataQueryConstants.SELECT, "Type_Id");
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			return responseObj;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getTypeIds : ", e).log();
			return null;

		}

	}

	public JSONObject getRoleIds() {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_ROLE_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.SELECT, "id");
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			return responseObj;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getRoleIds : ", e).log();
			return null;

		}

	}

}
