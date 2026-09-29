package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class CheckProspectStatusOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();

		String applicationId = request.getParameter("applicationId");
		String applicationType = request.getParameter("applicationType");
		if (StringUtils.isAnyBlank(applicationId, applicationType)) {
			ErrorCodeEnum.ERR_22085.setErrorCode(result);
			return result;
		}
		String entityDefinitionCode = applicationType.equalsIgnoreCase("SME")
				? EnvironmentConfiguration.SME_ONBOARDING_ENTITY_DEFINTION.getValue(request)
				: EnvironmentConfiguration.ONBOARDING_ENTITY_DEFINTION.getValue(request);
		JSONObject applicationDetails = getAllEntityItems(entityDefinitionCode, applicationId);
		JSONArray entityItems = applicationDetails.getJSONArray("entityItems");
		if (entityItems !=null && entityItems.length() > 0) {
			JSONObject metaData = new JSONObject();
			metaData = CommonUtilities.getEntityItemEntry("MetaData", entityItems, "MetaData");
			String requestId = "";
			if ((metaData != null) && metaData.has("entry")) {
				JSONObject coApplicantMetaDataEntry = new JSONObject(metaData.optString("entry"));
				if (coApplicantMetaDataEntry != null) {
					requestId = coApplicantMetaDataEntry.optString("RequestId");
				}
			}
			if (StringUtils.isAnyBlank(requestId)) {
				result.addStringParam("isVerifiedParty", "false");
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				return result;
			}
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("requestId", requestId);
			JSONObject completeRequestOverview = getCompleteRequestOverview(requestMap);
			if (completeRequestOverview != null && completeRequestOverview.length() > 0) {
				String partyId = "";
				partyId = completeRequestOverview.optJSONObject("requestOverView").optString("partyId");
				if (!partyId.contains(ACConstants.NON_VERIFIED_PROSPECT_PREFIX) && !partyId.contains(ACConstants.NON_VERIFIED_EXISTING_CUSTOMER_PREFIX)) {
					result.addStringParam("isVerifiedParty", "true");
					result.addOpstatusParam(0);
					result.addHttpStatusCodeParam(200);
				} else {
					result.addStringParam("isVerifiedParty", "false");
					result.addOpstatusParam(0);
					result.addHttpStatusCodeParam(200);
				}
			} else {
				result.addStringParam("message", ACConstants.NO_RECORD_FOR_REQUEST + requestId);
				result.addOpstatusParam(-1);
				result.addHttpStatusCodeParam(400);
			}
		} else {
			result.addStringParam("message", ACConstants.NO_RECORD_FOR_APPLICATION + applicationId);
			result.addOpstatusParam(-1);
			result.addHttpStatusCodeParam(400);
		}
		return result;
	}

	//
	//
	private JSONObject getAllEntityItems(String entityDefinitionCode, String applicationId)
			throws DBPApplicationException, Exception {
		String response = "";
		try {
			Map<String, Object> mapPayload = new HashMap<String, Object>();
			mapPayload.put("trackingCode", applicationId);
			mapPayload.put("entityDefinitionCode", entityDefinitionCode);
			response = DBPServiceExecutorBuilder.builder().withServiceId("SpotlightDataStorageAPIs")
					.withOperationId("GetAllEntityItems").withRequestParameters(mapPayload).build().getResponse();
		} catch (DBPApplicationException exception) {
			alert.prepareError("Failed to fetch all entity items").log();
		}
		return new JSONObject(response);
	}

	//
	public JSONObject getCompleteRequestOverview(Map<String, Object> requestMap)
			throws DBPApplicationException, Exception {
		Map<String, Object> customerRespMap = null;
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("AssistJSONService")
					.withFabricAuthToken((String) requestMap.get("xKonyAuthorization"))
					.withOperationId("getRequestOverview").withRequestParameters(requestMap).build();
			Result result = serviceExecutor.getResult();
			if (CommonUtilities.isBackendResponseSuccess(result, "httpStatusCode")) {
				ObjectMapper objeMapper = new ObjectMapper();
				customerRespMap = objeMapper.readValue(ResultToJSON.convert(result), Map.class);
			}
		} catch (DBPApplicationException e) {
			
			alert.prepareError("Exception", e).log();
		}

		return new JSONObject(customerRespMap);
	}
}
