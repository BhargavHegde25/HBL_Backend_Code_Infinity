package com.temenos.infinity.api.transactionadviceapi.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.transactionadviceapi.config.TransactionAdviceAPIServices;

public class TransactionAdviceUtilities {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");


	/**
	 * <p>
	 * Fetch the backendT24 customerId
	 * </p>
	 * 
	 * @param request
	 * @return backendId
	 */
	public static String getT24BackendId(DataControllerRequest request) {
		ServicesManager servicesManager;
		String backendIdentifiers = null;
		try {
			servicesManager = request.getServicesManager();
			backendIdentifiers = (String) servicesManager.getIdentityHandler().getUserAttributes()
					.get("backendIdentifiers");
		} catch (Exception e) {
			return new String();
		}

		String backendUserId = null;
		if (StringUtils.isNotBlank(backendIdentifiers)) {
			backendUserId = getBackendId(backendIdentifiers, "T24");
			return backendUserId;
		} else
			return new String();

	}
	/**
	 * <p>
	 * Fetch the username,password for AutoForm from APP properties
	 * </p>
	 * 
	 * @param request
	 * @return backendId
	 */
	public static String getCredsForAutoForm(DataControllerRequest request) {
		ServicesManager servicesManager;
		String backendIdentifiers = null;
		try {
			servicesManager = request.getServicesManager();
			backendIdentifiers = (String) servicesManager.getIdentityHandler().getUserAttributes()
					.get("backendIdentifiers");
		} catch (Exception e) {
			return new String();
		}

		String backendUserId = null;
		if (StringUtils.isNotBlank(backendIdentifiers)) {
			backendUserId = getBackendId(backendIdentifiers, "T24");
			return backendUserId;
		} else
			return new String();

	}
	
	public static String getUserAttributeFromIdentity(DataControllerRequest request, String attribute) {
		// TODO Auto-generated method stub
		try {
			if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
				Map<String, Object> userMap = request.getServicesManager().getIdentityHandler().getUserAttributes();
				if (userMap.get(attribute) != null) {
					String attributeValue = userMap.get(attribute) + "";
					alert.prepareError("value of " + attribute + "from identity is " + attributeValue).log();
					return attributeValue;
				}
				alert.prepareError("value of " + attribute + "from identity is null").log();
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}

		return "";
	}
	private static String getBackendId(String backendIdentifiers, String templateName) {
		String BackendId = null;
		BackendId = getCoreId(backendIdentifiers, templateName, "customerId", "1");
		return BackendId;
	}

	private static String getCoreId(String backendIdentifiers, String BackendType, String IdentifierName,
			String SequenceNumber) {
		String BackendId = null;
		if (StringUtils.isNotBlank(backendIdentifiers)) {
			JSONObject backendIdentifiersJSON = new JSONObject(backendIdentifiers);
			if (backendIdentifiersJSON.has(BackendType)) {
				JSONArray templateIdentifiers = backendIdentifiersJSON.getJSONArray(BackendType);

				for (int i = 0; i < templateIdentifiers.length(); i++) {
					String identifier_name = templateIdentifiers.getJSONObject(i).getString("identifier_name");
					String sequenceNumber = templateIdentifiers.getJSONObject(i).getString("sequence_number");

					if (BackendType.equals("T24")) {
						if (IdentifierName.equals(identifier_name) && SequenceNumber.equals(sequenceNumber)) {
							BackendId = templateIdentifiers.getJSONObject(0).getString("BackendId");
						}
					} else {
						if (IdentifierName.equals(identifier_name)) {
							BackendId = templateIdentifiers.getJSONObject(0).getString("BackendId");
						}
					}
				}
			}
		}
		return BackendId;
	}

	/**
	 * <p>
	 * Fetch the backendT24 customerId from GetUserAttributesDetails
	 * </p>
	 * 
	 * @param authToken
	 * @return backendId
	 */
	public static String getT24BackendIdUsingAuth(String authToken) {
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();

			if (!StringUtils.isBlank(authToken)) {
				diagnostic.prepareDebug("Using Auth Token from Param-Login").log();
				headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER, authToken);
			}
			String serviceResponse = "";
			serviceResponse= DBPServiceExecutorBuilder.builder().withServiceId(TransactionAdviceAPIServices.DBPUSERATTRIBUTES_GETUSERATTRIBUTEDETAILS.getServiceName())
					.withOperationId(TransactionAdviceAPIServices.DBPUSERATTRIBUTES_GETUSERATTRIBUTEDETAILS.getOperationName())
					.withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withFabricAuthToken(authToken).build().getResponse();	
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
			if (serviceResponseJSON == null)
				diagnostic.prepareDebug("failed").log();
			else if (serviceResponseJSON.has("BackendId")
					&& StringUtils.isNotEmpty(serviceResponseJSON.getString("BackendId"))) {
				String backendUserId = null;
				if (StringUtils.isNotBlank(serviceResponseJSON.getString("BackendId"))) {
					backendUserId = getBackendId(serviceResponseJSON.getString("BackendId"), "T24");
					return backendUserId;
				} else
					return new String();
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			return new String();
		}
		return new String();

	}
	
	/**
	 * <p>
	 * Fetch the custom attribute from GetUserAttributesDetails
	 * </p>
	 * 
	 * @param authToken
	 * @param attributeKey
	 * @return attributValue
	 */
	public static String getUserAttributeValue(String authToken,String key) {
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();

			if (!StringUtils.isBlank(authToken)) {
				diagnostic.prepareDebug("Using Auth Token from Param-Login").log();
				headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER, authToken);
			}
			String serviceResponse = "";
			serviceResponse= DBPServiceExecutorBuilder.builder().withServiceId(TransactionAdviceAPIServices.DBPUSERATTRIBUTES_GETUSERATTRIBUTEDETAILS.getServiceName())
					.withOperationId(TransactionAdviceAPIServices.DBPUSERATTRIBUTES_GETUSERATTRIBUTEDETAILS.getOperationName())
					.withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withFabricAuthToken(authToken).build().getResponse();	
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
			if (serviceResponseJSON == null)
				diagnostic.prepareDebug("failed").log();
			else if(serviceResponseJSON.has(key) && StringUtils.isNotEmpty(serviceResponseJSON.getString(key)))
			{
				String value = serviceResponseJSON.getString(key);
				return value;
			}			
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			return new String();
		}
		return new String();

	}
}