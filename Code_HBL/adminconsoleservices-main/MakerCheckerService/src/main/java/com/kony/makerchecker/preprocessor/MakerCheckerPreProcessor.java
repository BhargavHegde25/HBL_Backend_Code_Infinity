package com.kony.makerchecker.preprocessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.makerchecker.businessdelegate.impl.MakerCheckerBusinessDelegateImpl;
import com.kony.makerchecker.javaservice.LoadMakerCheckerConfigData;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class MakerCheckerPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule(UtilConstants.INFINITY,
			UtilConstants.SPOTLIGHT);
	String recordId = "";
	String createdby = "";
	String module = "";
	String action = "";
	String auditModule = "";
	String auditEvent = "";
	String approvalPermission = "";
	String payloadStr = "";
	String roles = "";
	String legalEntityId = "";

	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
			FabricRequestChain fabricRequestChain) throws Exception {

		JsonElement responseJsonElement = fabricResponseManager.getPayloadHandler().getPayloadAsJson();
		if (responseJsonElement == null) {
			diagnostic.prepareDebug("Initializing response body to empty JSON").log();
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(new JsonObject());
			responseJsonElement = fabricResponseManager.getPayloadHandler().getPayloadAsJson();
		}
		JsonObject responseJsonObject = responseJsonElement.getAsJsonObject();

		// currently record id is an optional param, as we are not blocking another
		// request for same record

		try {
			JsonElement payload = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
			JsonObject payloadJSON = payload.getAsJsonObject();
			if (!approveRequestFlow(payloadJSON, fabricRequestChain)) {

				/*
				 * adding the flag to payload, to store in approvalrequest, so when we hit the
				 * service after approval thorugh approve api, it will bypass the approval
				 * process and executes normally.
				 */
				payloadJSON.addProperty(UtilConstants.IS_APPROVAL_FLOW, true);
				ServicesManager servicesManager = fabricRequestManager.getServicesManager();

				String expApi = getExpApiName(fabricRequestManager, servicesManager);

				createdby = LoggedInUserHandler.getUserDetails(servicesManager).getUserName();
				roles = LoggedInUserHandler.getUserDetails(servicesManager).getRoleId();
				payloadStr = payloadJSON.toString();

				if (expApi.equalsIgnoreCase(UtilConstants.SUSPEND_ACTIVATE_API)) {
					processCustomerSuspendApi(fabricRequestManager, fabricResponseManager, fabricRequestChain, expApi,
							payloadJSON, responseJsonObject);
				} else {
					MakerCheckerBusinessDelegateImpl businessDelegate = new MakerCheckerBusinessDelegateImpl();
					legalEntityId = businessDelegate.parseRequestAndFetchLegalEntityId(payloadStr)
							.replaceAll("\'", "").replaceAll("\"", "");

					diagnostic.prepareDebug("legal entity from payload : " + legalEntityId).log();

					processRequest(fabricRequestManager, fabricResponseManager, fabricRequestChain, responseJsonObject,
							payloadJSON, expApi);
				}
			}
		} catch (Exception e) {

		} finally {
			responseJsonObject.addProperty(UtilConstants.OPSTATUS, 0);
			responseJsonObject.addProperty(UtilConstants.HTTP_STATUS_CODE, 0);
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responseJsonElement);
		}
	}

	private void processRequest(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
			FabricRequestChain fabricRequestChain, JsonObject responseJsonObject, JsonObject payloadJSON, String expApi) throws DBPApplicationException {
		Map<String, String> configMap = populateConfigData(expApi, responseJsonObject);
		if (configMap == null) {
			alert.prepareError("Error while getting config data.").log();
			return;
		} else {
			module = configMap.get(UtilConstants.MODULE);
			action = configMap.get(UtilConstants.ACTION);
			auditModule = configMap.get(UtilConstants.AUDIT_MODULE);
			auditEvent = configMap.get(UtilConstants.AUDIT_EVENT);
			approvalPermission = configMap.get(UtilConstants.APPROVAL_PERMISSION_NAME);
		}

		boolean isApprovalFlow = skipApprovalForSuperAdminRole(fabricRequestManager, fabricResponseManager,
				fabricRequestChain, responseJsonObject);

		if (isApprovalFlow) {
			approvalFlow(fabricRequestManager, fabricResponseManager, fabricRequestChain, responseJsonObject,
					payloadJSON, expApi);
		}
	}

	private boolean skipApprovalForSuperAdminRole(FabricRequestManager fabricRequestManager,
			FabricResponseManager fabricResponseManager, FabricRequestChain fabricRequestChain,
			JsonObject responseJsonObject) {
		List<String> rolesList = Arrays.asList(roles.split(","));
		String superAdminRoles = "";
		try {
			superAdminRoles = EnvironmentConfigurationsHandler
					.getServerAppProperty(UtilConstants.SKIP_MAKER_CHECKER_ROLES);
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching value from server properties.");
		}
		String[] superAdminRolesArr = superAdminRoles.split(",");

		for (String s : superAdminRolesArr) {
			if (rolesList.contains(s)) {
				AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, createdby, null,
						auditModule, "Initiate " + auditEvent, ActivityStatusEnum.SUCCESSFUL,
						"No Approvals required for " + auditEvent + " " + auditModule
								+ ", as request initiated by Super Admin");
				fabricRequestChain.execute();
				populateResponse(fabricResponseManager, responseJsonObject);
				diagnostic.prepareDebug("User has super admin role, so no approval required.").log();
				return false;
			}
		}
		diagnostic.prepareDebug("Request will go for approval, as user is not a super admin.").log();

		return true;
	}

	private String getExpApiName(FabricRequestManager fabricRequestManager, ServicesManager servicesManager) {
		String expApi = "";
		try {
			String serviceName = servicesManager.getOperationData().getServiceId();
			String operationName = servicesManager.getOperationData().getOperationId();
			String object = servicesManager.getOperationData().getObjectId();
			expApi = serviceName + "_" + object + "_" + operationName;
		} catch (Exception e) {
			alert.prepareError("Caught exception while getting experience api name");
		}
		diagnostic.prepareDebug("Exp Api name : " + expApi).log();
		return expApi;
	}

	private void approvalFlow(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
			FabricRequestChain fabricRequestChain, JsonObject responseJsonObject, JsonObject payloadJSON, String expApi) throws DBPApplicationException {

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UtilConstants.EXPAPIOPERATIONNAME, expApi);
		inputMap.put(UtilConstants.LEGAL_ENTITY_ID, legalEntityId);
		inputMap.put(UtilConstants.RECORD_ID, recordId);
		inputMap.put(UtilConstants.PAYLOAD, payloadStr);
		inputMap.put(UtilConstants.CREATED_BY, createdby);
		inputMap.put(UtilConstants.MODULE, module);
		inputMap.put(UtilConstants.ACTION, action);
		inputMap.put(UtilConstants.PERMISSION_NAME, approvalPermission);

		String serviceId = UtilConstants.MAKER_CHECKER_SERVICE;
		String operationId = UtilConstants.IS_APPROVAL_REQ;

		fabricRequestManager.getPayloadHandler().updatePayloadAsJson(payloadJSON);

		String response1 = DBPServiceExecutorBuilder.builder().withServiceId(serviceId).withObjectId(null)
				.withOperationId(operationId).withRequestParameters(inputMap)
				.withFabricRequestManager(fabricRequestManager).build().getResponse();

		diagnostic.prepareDebug("response from isApprovalRequired : " + response1).log();

		JSONObject jsonObj = new JSONObject(response1);

		if (!jsonObj.has(UtilConstants.DBP_ERR_CODE) && !jsonObj.has(UtilConstants.APPROVAL_REQ_ID)) {
			AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, createdby, null, auditModule,
					"Initiate " + auditEvent, ActivityStatusEnum.SUCCESSFUL,
					"No Approvals required for " + auditEvent + " " + auditModule + " operation");
			fabricRequestChain.execute();
			populateResponse(fabricResponseManager, responseJsonObject);
			diagnostic.prepareDebug("Request processed, as no approvals required for this operation.").log();
		} else {
			if (jsonObj.has(UtilConstants.DBP_ERR_CODE)) {
				responseJsonObject.addProperty(UtilConstants.DBP_ERR_CODE,
						ErrorCodesEnum.ERR_10042.getErrorCodeAsString());
				responseJsonObject.addProperty(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10042.getMessage());
				AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, createdby, null,
						auditModule, "Initiate " + auditEvent, ActivityStatusEnum.FAILED,
						"Error occured while checking is approval required for " + auditEvent + " " + auditModule);
				diagnostic.prepareDebug("Error occured while getting response from isApprovalRequired").log();
			} else {
				responseJsonObject.addProperty(UtilConstants.APPROVAL_REQ_ID,
						jsonObj.optString(UtilConstants.APPROVAL_REQ_ID));
				responseJsonObject.addProperty(UtilConstants.IS_APPROVAL_REQ,
						jsonObj.optString(UtilConstants.IS_APPROVAL_REQ));
				responseJsonObject.addProperty("Message", "Request submitted successfully.");
				AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, createdby, null,
						auditModule, "Initiate " + auditEvent, ActivityStatusEnum.SUCCESSFUL,
						"Request for " + auditEvent + " " + auditModule + " submitted with id : "
								+ jsonObj.optString(UtilConstants.APPROVAL_REQ_ID));
				diagnostic.prepareDebug("Approval is required for this operation : " + expApi).log();
			}
		}
	}

	private void populateResponse(FabricResponseManager fabricResponseManager, JsonObject responseJsonObject) {
		JsonElement reponseJsonElement = fabricResponseManager.getPayloadHandler().getPayloadAsJson();
		if (reponseJsonElement != null && reponseJsonElement.getAsJsonObject() != null) {
			JsonObject dataJsonObject = reponseJsonElement.getAsJsonObject();
			if (dataJsonObject != null) {
				Set<String> keys = dataJsonObject.keySet();
				for (String key : keys) {
					responseJsonObject.addProperty(key, dataJsonObject.get(key).getAsString());
				}
			}
		}
	}

	private Map<String, String> populateConfigData(String expApi, JsonObject responseJsonObject) {

		Map<String, String> configMap = new HashMap<>();
		JSONArray makerCheckerData = LoadMakerCheckerConfigData.getMakerCheckerConfigData();
		for (int i = 0; i < makerCheckerData.length(); i++) {
			JSONObject makerChecker = makerCheckerData.optJSONObject(i);
			if (makerChecker != null && makerChecker.optString(UtilConstants.EXPAPIOPNAME).equals(expApi)) {

				if (StringUtils.isBlank(legalEntityId) && UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY
						.equalsIgnoreCase(makerChecker.optString(UtilConstants.COMPANY_LEGAL_UNIT))) {

					legalEntityId = UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY;

				} else if (StringUtils.isNotBlank(legalEntityId) && UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY
						.equalsIgnoreCase(makerChecker.optString(UtilConstants.COMPANY_LEGAL_UNIT))) {

					diagnostic.prepareDebug(ErrorCodesEnum.ERR_10043.getMessage()).log();
					ErrorCodesEnum.ERR_10043.setErrorCode(responseJsonObject);
					return null;

				} else if (StringUtils.isBlank(legalEntityId) && !(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY
						.equalsIgnoreCase(makerChecker.optString(UtilConstants.COMPANY_LEGAL_UNIT)))) {

					diagnostic.prepareDebug(ErrorCodesEnum.ERR_10044.getMessage()).log();
					ErrorCodesEnum.ERR_10044.setErrorCode(responseJsonObject);
					return null;

				}
				if (StringUtils.isNotBlank(legalEntityId)
						&& makerChecker.optString(UtilConstants.COMPANY_LEGAL_UNIT).equals(legalEntityId)) {
					configMap.put(UtilConstants.MODULE, makerChecker.optString(UtilConstants.MODULE));
					configMap.put(UtilConstants.ACTION, makerChecker.optString(UtilConstants.ACTION));
					configMap.put(UtilConstants.AUDIT_MODULE, makerChecker.optString(UtilConstants.AUDIT_MODULE));
					configMap.put(UtilConstants.AUDIT_EVENT, makerChecker.optString(UtilConstants.AUDIT_EVENT));
					configMap.put(UtilConstants.APPROVAL_PERMISSION_NAME,
							makerChecker.optString(UtilConstants.APPROVAL_PERMISSION_NAME));
					break;
				}
			}
		}
		return configMap;
	}

	private boolean approveRequestFlow(JsonObject payloadJSON, FabricRequestChain fabricRequestChain) {
		if (payloadJSON.get(UtilConstants.IS_APPROVAL_FLOW) != null
				&& payloadJSON.get(UtilConstants.IS_APPROVAL_FLOW).getAsBoolean()) {
			fabricRequestChain.execute();
			diagnostic.prepareDebug("Request executed from approve flow, so no furthur approval checks required").log();
			return true;
		} else {
			return false;
		}
	}

	private void processCustomerSuspendApi(FabricRequestManager fabricRequestManager,
			FabricResponseManager fabricResponseManager, FabricRequestChain fabricRequestChain, String expApi,
			JsonObject payloadJSON, JsonObject responseJsonObject)
			throws ApplicationException, DBPApplicationException {

		String legalEntities = payloadJSON.get(UtilConstants.LEGAL_ENTITY_LIST).getAsString();
		legalEntities = legalEntities.replaceAll("\\\\", "");
		diagnostic.prepareDebug("legalEntityList from suspend user payload : " + legalEntities).log();
		JSONArray legalEntityArr = new JSONArray(legalEntities);
		Set<String> responseMessages = new HashSet<>();
		for (int i = 0; i < legalEntityArr.length(); i++) {
			String le = legalEntityArr.getString(i);
			JSONArray leArray = new JSONArray();
			leArray.put(le);
			String lePayload = "[\\\""+le+"\\\"]";
			payloadJSON.addProperty(UtilConstants.LEGAL_ENTITY_LIST, lePayload);
			payloadStr = payloadJSON.toString();
			legalEntityId = le;
			diagnostic.prepareDebug("payload from suspend user payload : " + payloadStr).log();
			processRequest(fabricRequestManager, fabricResponseManager, fabricRequestChain, responseJsonObject,
					payloadJSON, expApi);
			diagnostic.prepareDebug("response from suspend user processRequest : " + responseJsonObject.toString())
					.log();
			if (responseJsonObject.has(UtilConstants.DBP_ERR_CODE)
					&& responseJsonObject.get(UtilConstants.DBP_ERR_CODE).getAsString().equals("10042")) {
				responseMessages.add(UtilConstants.FAILED);
				responseJsonObject.remove(UtilConstants.DBP_ERR_CODE);
			} else if (responseJsonObject.has(UtilConstants.APPROVAL_REQ_ID)) {
				responseMessages.add(UtilConstants.CREATED);
				responseJsonObject.remove(UtilConstants.APPROVAL_REQ_ID);
			} else {
				responseMessages.add(UtilConstants.DISABLED);
			}
		}
		if (responseMessages.contains(UtilConstants.FAILED) && responseMessages.size() == 1) {
			diagnostic.prepareDebug("All requests for suspend/activate user got failed").log();

			responseJsonObject.addProperty(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10042.getErrorCodeAsString());
			return;

		} else if (responseMessages.contains(UtilConstants.DISABLED) && responseMessages.size() == 1) {
			diagnostic.prepareDebug("All requests for suspend/activate user are processd, no approvals required").log();
			return;

		} else if (responseMessages.contains(UtilConstants.CREATED) && responseMessages.size() == 1) {
			diagnostic.prepareDebug("All requests for suspend/activate user successfully submitted for approval.")
					.log();
			responseJsonObject.addProperty("Message", "Requests are Submitted for approval.");

		} else if (responseMessages.size() > 1) {
			removeKeysFromResponse(responseJsonObject, fabricRequestManager);
			if (responseMessages.contains(UtilConstants.FAILED)) {
				diagnostic.prepareDebug("One or more requests for suspend/activate user got failed").log();
				responseJsonObject.addProperty(UtilConstants.DBP_ERR_CODE,
						ErrorCodesEnum.ERR_10042.getErrorCodeAsString());
				responseJsonObject.addProperty(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10042.getMessage());
			} else {
				diagnostic.prepareDebug("Requests are Submitted where approval is required.").log();
				responseJsonObject.addProperty("Message", "Requests are Submitted where approval is required.");
			}
		}
	}

	private void removeKeysFromResponse(JsonObject responseJsonObject, FabricRequestManager fabricResponseManager) {
		Set<String> keys = new HashSet<>(responseJsonObject.keySet());
		for (String key : keys) {
			responseJsonObject.remove(key);
		}
	}
}