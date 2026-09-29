package com.temenos.infinity.smartbanking.advisory.backenddelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.apache.commons.lang3.StringUtils;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbx.util.Constants;
import com.kony.dbx.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.smartbanking.advisory.utils.BackendCommonUtils;
import com.temenos.infinity.smartbanking.advisory.utils.CommonUtils;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.temenos.infinity.smartbanking.advisory.utils.DTOUtils;
import com.temenos.dbx.usermanagement.dto.PartySearchDTO;
import com.temenos.infinity.smartbanking.advisory.backenddelegate.api.SmartBankingAdvisoryBackendDelegate;
import com.temenos.infinity.smartbanking.advisory.constants.CommonConstants;
import com.temenos.infinity.smartbanking.advisory.errorhandling.SBAException;

import com.konylabs.middleware.controller.DataControllerRequest;

public class SmartBankingAdvisoryBackendDelegateImpl implements SmartBankingAdvisoryBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String  residence = null;

	@Override
	public Map<String, Object> getBusinessScoreAndDriversDetails(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("VCOO_Dashboard")
					.withOperationId("getScoreAndDrivers").withRequestParameters(payloadMap).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getBusinessScoreAndDriversDetails" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	
	@Override
	public Map<String, Object> getEnrollmentStatus(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("AnalyticsJSONServices")
					.withOperationId("getEnrollmentStatus").withRequestParameters(payloadMap).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "opstatus")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class); 
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getEnrollmentStatus" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	
	@Override
	public Map<String, Object> getAccountingData(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("Enrolment")
					.withOperationId("getAccountingData").withRequestParameters(payloadMap).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getAccountingData" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	@Override
	public Map<String, Object> startProcess(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("Enrolment")
					.withOperationId("postCompanyDataSourceConnection").withRequestParameters(payloadMap).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode") && result.hasParamByName("errmsg_postConnection")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
			}
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
				responseMap.put("isEnrollmentSucess","true");
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : startProcess" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	@Override
	public Map<String, Object> get12MonthCashFlow(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("VCOO_Dashboard")
					.withOperationId("getCashflow12").withRequestParameters(payloadMap).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
			responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : get12MonthCashFlow" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	@Override
	public Map<String, Object> getAnalyticsSimulationDetails(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> analyticsPayload = new HashMap<>();
		try {
			analyticsPayload.put("Subscriber", payloadMap.get("Subscriber"));
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("AnalyticsJSONServices")
					.withOperationId("CashFlowProtoFeatures").withRequestParameters(analyticsPayload).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getAnalyticsSimulationDetails" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	@Override
	public Map<String, Object> getXaiSimulationDetails(Map<String, Object> xaiPayload) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();

		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("Simulator")
					.withOperationId("getCashflowSimulationPlan").withRequestParameters(xaiPayload).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getXaiSimulationDetails" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	@Override
	public Map<String, Object> getSBAEnrolmentStatus(Map<String, Object> payloadMap, DataControllerRequest request) throws SBAException {
		Map<String, Object> responseMap = new HashMap<>();
		DBXResult partyResponse = new DBXResult();
		JsonArray partyJsonArray = new JsonArray();
		Map<String, Object> payload = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		Result result = new Result();
		String integrationName = StringUtils
				.upperCase(EnvironmentConfigurationsHandler.getValue("INTEGRATION_NAME", request));
		
		try {
			// Commenting the below Code as we are not depending on Party
			/*	if ("Party".equalsIgnoreCase(integrationName)) {

					payload.put("alternateIdentifierNumber",
							payloadMap.get("legalEntityId").toString() + "-" + payloadMap.get("coreCustomerID").toString());
					payload.put("alternateIdentifierType", "BackOfficeIdentifier");
					PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
							.getFactoryInstance(BusinessDelegateFactory.class)
							.getBusinessDelegate(PartyUserManagementBusinessDelegate.class);
					PartySearchDTO searchDTO = buildSearchPartyDTO(payload);
					diagnostic.prepareDebug(
							"createInfinityUser : alternateIdentifierNumber " + payloadMap.get("alternateIdentifierNumber"))
							.log();
					headerMap = PartyUtils.addJWTAuthHeader(null, headerMap, "PreLogin");
					diagnostic.prepareDebug("headerMap=" + headerMap).log();
					headerMap.put("companyid", payloadMap.get("legalEntityId"));
					partyResponse = managementBusinessDelegate.searchParty(searchDTO, headerMap);

					if (partyResponse.getResponse() != null) {
						JsonObject party = (JsonObject) partyResponse.getResponse();
						partyJsonArray = party.has("parties") && party.get("parties").isJsonArray()
								? party.get("parties").getAsJsonArray()
								: new JsonArray();
					}

					if (partyJsonArray.size() > 0) {
						JsonObject party = partyJsonArray.get(0).isJsonObject() ? partyJsonArray.get(0).getAsJsonObject()
								: new JsonObject();
						JsonObject extensionData = party.getAsJsonObject("extensionData");
						if (party.has("partyId") && !party.get("partyId").isJsonNull()) {
							if (extensionData.has("sbaEnrolmentStatus")) {
								responseMap.put("partyId", party.get("partyId").getAsString());
								responseMap.put("coreCustomerID", payloadMap.get("coreCustomerID").toString());
								responseMap.put("sbaEnrolmentStatus",
										extensionData.get("sbaEnrolmentStatus").getAsString());
							} else {
								responseMap.put("statusMsg", "Customer is not Enrolled for Smart Banking");
								responseMap.put("partyId", party.get("partyId").getAsString());
								responseMap.put("coreCustomerID", payloadMap.get("coreCustomerID").toString());
	                		}
	              }
	            }
			}   
				 catch (JSONException e) {
				alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getSBASEnrolmentStatusFromParty" + e.getMessage()).log();
							}
						}
					}
				} else { */
					Map<String, Object> userInfo = HelperMethods.getIdentityServiceInfo(request);
					String customer_id = userInfo.get("customer_id").toString();
					String filter = "id eq " + customer_id;
					String schema = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME", request);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(Constants.PARAM_DOLLAR_FILTER, filter);
					result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
							CommonConstants.Db_Operation, schema + CommonConstants.Customer_Get);
					String sbaEnrolmentStatus = null;
					Dataset customerGetDs = result != null ? result.getDatasetById("customer") : null;
					if (customerGetDs != null && !customerGetDs.getAllRecords().isEmpty()) {
						sbaEnrolmentStatus = customerGetDs.getAllRecords().get(0).getParamValueByName("sbaEnrolmentStatus");
						if (StringUtils.isBlank(sbaEnrolmentStatus)) {
							responseMap.put("statusMsg", "Customer is not Enrolled for Smart Banking");
							responseMap.put("customerId", customer_id);
							responseMap.put("coreCustomerID", payloadMap.get("coreCustomerID").toString());
						} else {
							responseMap.put("customerId", customer_id);
							responseMap.put("coreCustomerID", payloadMap.get("coreCustomerID").toString());
							responseMap.put("sbaEnrolmentStatus", sbaEnrolmentStatus);
						}
					}
				//}
		}catch (JSONException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getSBASEnrolmentStatus"
					+ e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	private PartySearchDTO buildSearchPartyDTO(Map<String, Object> inputMap) {
        PartySearchDTO partySearchDTO = new PartySearchDTO();
        DTOUtils.loadInputIntoDTO(partySearchDTO, inputMap, false);
        return partySearchDTO;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> updateSBAStatus(Map<String, Object> Payload, DataControllerRequest request) throws SBAException {

		Map<String, Object> headerMap = new HashMap<String, Object>();
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> extensionDataMap = new HashMap<>();
		extensionDataMap.put("sbaEnrolmentStatus", Payload.get("sbaEnrolmentStatus").toString());
		JsonObject jsonObject = new JsonObject();
		Gson gson = new Gson();
		Result result = new Result();
		String integrationName = StringUtils
				.upperCase(EnvironmentConfigurationsHandler.getValue("INTEGRATION_NAME", request));
		
		try {
			
			// Commenting the below Code as we are not depending on Party 
			/*if ("Party".equalsIgnoreCase(integrationName)) {
						 
		PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET5, Payload.get("partyId").toString());
		DBXResult partyResponse = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null,
				headerMap);

		try {
			
			JsonElement jsonElement = new JsonParser().parse((String) partyResponse.getResponse());

			jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
					: jsonElement.getAsJsonArray().get(0).getAsJsonObject();
			if (jsonObject.has(PartyConstants.partyId)) {
				jsonObject.add("extensionData", gson.toJsonTree(extensionDataMap));
			}
			if (jsonObject.has("title") && jsonObject.get("title").getAsString() != null
					&& !isFirstLetterCapitalized(jsonObject.get("title").getAsString())) {
				String newTitle = conversionToCapitalizedCase(jsonObject.get("title").getAsString());
				jsonObject.addProperty("title", newTitle);
			}
			
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(jsonObject.toString());
				JsonNode residences = jsonNode.get("residences");
				if (residences.isArray()) {
					for (JsonNode residence : residences) {
						if (residence.isObject() && residence.has("type") && residence.get("type") != null
								&& !isFirstLetterCapitalized(residence.get("type").asText())) {
							String type = residence.get("type").asText();
							String camelCaseType = conversionToCapitalizedCase(type);
							((ObjectNode) residence).put("type", camelCaseType);

						}
					}
				}
				if (jsonNode.has("residences")) {
					((ObjectNode) jsonNode).set("residences", residences);
					String updatedJson = jsonNode.toString();
					jsonObject = JsonParser.parseString(updatedJson).getAsJsonObject();
				}
			} catch (Exception e) {
				alert.prepareError(
						"Error in SmartBankingAdvisoryBackendDelegateImpl in updating Residence Type: partyUpdateSBAStatus"
								+ e.getMessage())
						.log();
			}
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBackendDelegateImpl in Get Party Data: partyUpdateSBAStatus"
							+ e.getMessage())
					.log();
		}

		String party = jsonObject.toString();

		String partyPUTURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_UPDATE, Payload.get("partyId").toString());
		DBXResult responsePUT = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyPUTURL, party,
				headerMap);

		try {
			JsonElement jsonElementPUT = new JsonParser().parse((String) responsePUT.getResponse());
			JsonObject jsonObjectPUT;

			jsonObjectPUT = jsonElementPUT.isJsonObject() ? jsonElementPUT.getAsJsonObject()
					: jsonElementPUT.getAsJsonArray().get(0).getAsJsonObject();
			if (jsonObjectPUT.has("id")) {
				responseMap.put("id", jsonObjectPUT.get(PartyConstants.id).getAsString());
				responseMap.put("message", jsonObjectPUT.get("message").getAsString());

			} else {
				if (jsonObjectPUT.has("message")) {
					responseMap.put("message", jsonObjectPUT.get("message").getAsString());
				}

				if (jsonObjectPUT.has("status")) {
					responseMap.put("status", jsonObjectPUT.get("status").getAsString());
				}
			}
		} catch (JSONException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBackendDelegateImpl : partyUpdateSBAStatus" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		} 
		}
		else {
			*/
			String schema = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME", request);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", Payload.get("customerId").toString());
			map.put("sbaEnrolmentStatus", Payload.get("sbaEnrolmentStatus").toString());
			result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
					CommonConstants.Db_Operation, schema + CommonConstants.Customer_Update);
			if (result.hasParamByName("updatedRecords")) {
				responseMap.put("message", "Sucessfully enrolled for Smart Banking");
				responseMap.put("sbaEnrolmentStatus", Payload.get("sbaEnrolmentStatus").toString());
				responseMap.put("customerId", Payload.get("customerId").toString());
			} else {
				responseMap.put("message", "Unable to enroll for Smart Banking");
			}
		}
			catch (JSONException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBackendDelegateImpl : updateSBAStatus" + 
			e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	private static boolean isFirstLetterCapitalized(String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		return input.matches("^[A-Z][a-z]*$") || input.matches("^[A-Z]$");
	}

	public static String conversionToCapitalizedCase(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
	}
	
	@Override
	public Result getAccountsReceivable(Map<String, Object> payloadMap) throws SBAException {
		Result result = new Result();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
					.withServiceId("AnalyticsJSONServices").withOperationId("GetDetails")
					.withRequestParameters(payloadMap).build();
			result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				return result;
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBackendDelegateImpl : getAccountsReceivable" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return result;
	}
	
	@Override
	public Result getAccountsPayable(Map<String, Object> payloadMap) throws SBAException {
		Result result = new Result();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
					.withServiceId("AnalyticsJSONServices").withOperationId("getPayableData")
					.withRequestParameters(payloadMap).build();
			result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				return result;
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getAccountsPayable" + e.getMessage())
					.log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return result;
	}
	
	@Override
	public Result getReceivablesDebtorDaysReq(Map<String, Object> payloadMap) throws SBAException {
		Result result = new Result();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
					.withServiceId("AnalyticsJSONServices").withOperationId("getReceivablesDebtorDaysReq")
					.withRequestParameters(payloadMap).build();
			result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				return result;
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getReceivablesDebtorDaysReq" + e.getMessage())
					.log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return result;
	}
	
	@Override
	public Map<String, Object> getAccountsReceivableByCustomer(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
					.withServiceId("AnalyticsJSONServices").withOperationId("ByCustomer")
					.withRequestParameters(payloadMap).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBackendDelegateImpl : getAccountsReceivableByCustomer" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	@Override
	public Map<String, Object> getPayablesSummaryBySupplier(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> responseMap1 = new HashMap<>();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
					.withServiceId("AnalyticsJSONServices").withOperationId("getPayableDataBySupplier")
					.withRequestParameters(payloadMap).build();
			Result result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				responseMap = objMapper.readValue(ResultToJSON.convert(result), Map.class);
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException | JSONException | IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getPayablesSummaryBySupplier" + e.getMessage())
					.log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return responseMap;
	}
	
	@Override
	public Result getPayablesDebtorDaysReq(Map<String, Object> payloadMap) throws SBAException {
		Result result = new Result();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
					.withServiceId("AnalyticsJSONServices").withOperationId("getAvgCreditorDays")
					.withRequestParameters(payloadMap).build();
			result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "httpStatusCode")) {
				return result;
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBackendDelegateImpl : getAvgCreditorDays" + e.getMessage())
					.log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return result;
	}
	
	@Override
	public Result getSBAFeaturesActions(Map<String, Object> payloadMap) throws SBAException {
		Result result = new Result();
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("dbpRbLocalServicesdb")
					.withOperationId("dbxdb_user_sba_securityattributes_get_proc").withRequestParameters(payloadMap).build();
			result = serviceExecutor.getResult();
			if (BackendCommonUtils.isBackendResponseSuccess(result, "opstatus")) {
				return result;
			} else {
				CommonUtils.constructAndThrowBackendException();
			}
		} catch (DBPApplicationException e) {
			alert.prepareError("Error in SmartBankingAdvisoryBackendDelegateImpl : getSBAFeaturesActions" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(e.getMessage());
		}
		return result;
	}
	
}
