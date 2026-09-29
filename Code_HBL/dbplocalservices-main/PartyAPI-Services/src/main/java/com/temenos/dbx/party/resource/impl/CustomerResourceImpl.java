package com.temenos.dbx.party.resource.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.StatusEnum;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.party.businessdelegate.api.PartyCustomerBusinessDelegate;
import com.temenos.dbx.party.resource.api.CustomerResource;
import com.temenos.dbx.party.utils.PartyConfiguration;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.AlternateIdentity;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyClassification;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.utils.CustomerUtils;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class CustomerResourceImpl implements CustomerResource {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private LoggerUtil logger;

	@Override
	public Result save(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {

		String partyEventData = HelperMethods.getInputParamMap(inputArray).get(DTOConstants.PARTYEVENTDATA);
		if (StringUtils.isBlank(partyEventData)) {
			partyEventData = dcRequest.getParameter(DTOConstants.PARTYEVENTDATA);
		}
		logger = new LoggerUtil(CustomerResourceImpl.class);
		diagnostic.prepareDebug("Saving customer Data in Party as PartyEvent data is not available in input or dcRequest").log();
		return saveInParty(inputArray, dcRequest, dcResponse);
	}

	@Override
	public Result createPartyClassification(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse, boolean isUpdate) {

		diagnostic.prepareDebug("Saving Party Classification Data").log();
		Result result = new Result();
		PartyCustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PartyCustomerBusinessDelegate.class);
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String id = inputParams.get("partyID");
		String userType = inputParams.get("userType");
		String configurationCode = null;
		Map<String, Object> headers = dcRequest.getHeaderMap();
		headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
		headers.put("companyid", inputParams.get("companyId"));
		if (StringUtils.isNotEmpty(id) && StringUtils.isNotBlank(userType)
				&& StringUtils.equalsAny(userType, "Retail", "SME", "Company")) {
			PartyDTO party = new PartyDTO();
			PartyClassification classification = new PartyClassification();
			List<PartyClassification> classificationList = new ArrayList<>();
			if (StringUtils.equalsIgnoreCase(userType, "Retail")) {

				classification.setClassificationCode(PartyConfiguration.getConfiguration(userType + ".classification"));
				classification.setClassificationDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				classificationList.add(classification);
				party.setClassification(classificationList);
				party.setPartyId(id);
			} else {
				String industryType = inputParams.get("industryType");
				String subIndustryType = inputParams.get("subIndustryType");
				if(StringUtils.isNotBlank(industryType)) {
					configurationCode = industryType + ".classification";
					classification.setClassificationCode(
							PartyConfiguration.getConfiguration(StringUtils.replace(configurationCode, " ", "_")));
					classification.setClassificationDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					classificationList.add(classification);
				}
				if(StringUtils.isNotBlank(subIndustryType)) {
					classification = new PartyClassification();
					configurationCode = subIndustryType + ".classification";
					classification.setClassificationCode(
							PartyConfiguration.getConfiguration(StringUtils.replace(configurationCode, " ", "_")));
					classification.setClassificationDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					classificationList.add(classification);
				}
				
				party.setClassification(classificationList);
				party.setPartyId(id);
			}

			boolean isClassificationUpdate = false;
			// For update case
			if (isUpdate) {
				JSONArray classificationArray = customerDelegate.getClassification(id, headers);
				for (int i = 0; i < classificationArray.length(); i++) {
					JSONObject obj = classificationArray.getJSONObject(i);
					if (obj.getString("classificationCode").equals(classification.getClassificationCode())) {
						isClassificationUpdate = true;
						break;
					}
				}
			}
			DBXResult classificationRes = customerDelegate.addClassification(party, headers, isClassificationUpdate);
			if (classificationRes.getResponse() != null) {
				diagnostic.prepareDebug("Party classification creation is succesful for id -> " + id).log();
				result.addParam("success", "success");
				result.addParam(DTOConstants.PARTYID, id);
			} else {
				diagnostic.prepareDebug("Party classification creation is failed").log();
				result.addParam(DTOConstants.BACKEND_ERR_CODE, classificationRes.getDbpErrCode());
				result.addParam(DTOConstants.BACKEND_ERR_MESSAGE, classificationRes.getDbpErrMsg());
				ErrorCodeEnum.ERR_10212.setErrorCode(result);
			}
		}

		return result;
	}

	private Result saveInParty(Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		CustomerDTO customerDTO = CustomerUtils.buildCustomerDTO(HelperMethods.getNumericId() + "",
				HelperMethods.getInputParamMap(inputArray), dcRequest);
		diagnostic.prepareDebug("CustomerDTO build for input and requst params : "
				+ DTOUtils.getJsonObjectFromObjectPrint(customerDTO).toString()).log();

		PartyCustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PartyCustomerBusinessDelegate.class);

		DBXResult response = new DBXResult();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String customerId = inputParams.get(InfinityConstants.id);
		String companyId = inputParams.get(InfinityConstants.companyId);
		if (StringUtils.isBlank(customerId)) {
			result = new Result();
			diagnostic.prepareDebug("Prospect creation is failed").log();
			ErrorCodeEnum.ERR_10212.setErrorCode(result, "CustomerId is empty");
			return result;
		}

		if (StringUtils.isBlank(companyId)) {
			result = new Result();
			diagnostic.prepareDebug("Prospect creation is failed").log();
			ErrorCodeEnum.ERR_10212.setErrorCode(result, "companyId is blank");
			return result;
		}

		Map<String, Object> headers = dcRequest.getHeaderMap();
		headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
		headers.put("accountId", inputParams.get("accountId"));
		if (inputParams.containsKey("employerAddresses") && inputParams.get("employerAddresses") != null) {
			JsonArray employerAddressArray = new JsonParser().parse(inputParams.get("employerAddresses").toString())
					.getAsJsonArray();
			if (employerAddressArray.size() > 0) {
				JsonObject employerAddress = employerAddressArray.get(0).getAsJsonObject();
				AddressDTO addressDTO = new AddressDTO();
				CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();
				if (StringUtils.isNotBlank(employerAddress.get("buildingName").toString())) {
					addressDTO.setAddressLine1(employerAddress.get("buildingName").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("flatNumber").toString())) {
					addressDTO.setAddressLine2(employerAddress.get("flatNumber").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("postalOrZipCode").toString())) {
					addressDTO.setZipCode(employerAddress.get("postalOrZipCode").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("town").toString())) {
					addressDTO.setCityName(employerAddress.get("town").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("countryCode").toString())) {
					addressDTO.setCountry(employerAddress.get("countryCode").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("countrySubdivision").toString())) {
					addressDTO.setState(employerAddress.get("countrySubdivision").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("addresstype").toString())) {
					customerAddressDTO.setType_id(employerAddress.get("addresstype").getAsString());
				}
				customerAddressDTO.setIsPrimary(false);
				customerAddressDTO.setAddressDTO(addressDTO);
				customerDTO.setCustomerAddress(customerAddressDTO);
			}
		}

		if (inputParams.containsKey("entityName")) {
			headers.put("entityName", inputParams.get("entityName"));
			headers.put("organisationLegalType", inputParams.get("organisationLegalType"));
			headers.put("incorporationCountry", inputParams.get("incorporationCountry"));
			headers.put("numberOfEmployees", inputParams.get("numberOfEmployees"));
			headers.put("dateOfIncorporation", inputParams.get("dateOfIncorporation"));
		}

		if (inputParams.containsKey("registrationDetails")) {
			headers.put("registrationDetails", inputParams.get("registrationDetails"));
		}
		
		if (inputParams.containsKey("userType")) {
			headers.put("userType", inputParams.get("userType"));
		}
		
		if(inputParams.containsKey("companyId")) {
			headers.put("companyid", companyId);
		}
		response = customerDelegate.saveCustomer(customerDTO, headers);
		String id = (String) response.getResponse();

		if (StringUtils.isNotBlank(id)) {
			result.addParam("success", "success");
			result.addParam(DTOConstants.PARTYID, id);
			
			addBackendIdentifierTableEntry(id, customerId, dcRequest, companyId);
			/* These calls have been added in the infinity prospect creation
			 * 
				addCustomerLegalEntityTableEntry(customerId,dcRequest,companyId);
				updateCustomerTableEntry(customerId,dcRequest,companyId);
			
			*/
			// result.addParam(DTOConstants.ID, id);
			diagnostic.prepareDebug("Party creation is successful for id -> " + id).log();
			makeAuditEntry(inputArray, dcRequest, result, dcResponse, true, DTOConstants.PARTY);
			return result;
		}

		result.addParam(DTOConstants.BACKEND_ERR_CODE, response.getDbpErrCode());
		result.addParam(DTOConstants.BACKEND_ERR_MESSAGE, response.getDbpErrMsg());
		diagnostic.prepareDebug("Party creation is failed").log();
		makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PARTY);
		ErrorCodeEnum.ERR_10212.setErrorCode(result);
		return result;

	}
	
	private void addCustomerLegalEntityTableEntry(String id, DataControllerRequest dcRequest,
			String companyId) {

		CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setId(UUID.randomUUID().toString());
		customerLegalEntityDTO.setCustomer_id(id);
		customerLegalEntityDTO.setLegalEntityId(companyId);
		customerLegalEntityDTO.setNew(true);
		customerLegalEntityDTO.setStatus_id(StatusEnum.SID_CUS_NEW.name());
		Map<String, Object> input = DTOUtils.getParameterMap(customerLegalEntityDTO, true);
		customerLegalEntityDTO.persist(input, new HashMap<String, Object>());
	}
	
	private void updateCustomerTableEntry(String id, DataControllerRequest dcRequest,
			String companyId) {

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setId(id);
		customerDTO.setIsNew(false);
		customerDTO.setIsChanged(true);
		customerDTO.setHomeLegalEntity(companyId);
		Map<String, Object> input = DTOUtils.getParameterMap(customerDTO, true);
		customerDTO.persist(input, new HashMap<String, Object>());
	}

	private void addBackendIdentifierTableEntry(String coreCustomerId, String id, DataControllerRequest dcRequest,
			String companyId) {

		BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
		backendIdentifierDTO.setId(UUID.randomUUID().toString());
		backendIdentifierDTO.setCustomer_id(id);
		backendIdentifierDTO.setBackendId(coreCustomerId);
		backendIdentifierDTO.setCompanyId(companyId);
		backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
		backendIdentifierDTO.setIdentifier_name(InfinityConstants.customerId);
		backendIdentifierDTO.setSequenceNumber("2");
		backendIdentifierDTO.setIsNew(true);
		backendIdentifierDTO.setCompanyLegalUnit(companyId);
		Map<String, Object> input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
		backendIdentifierDTO.persist(input, new HashMap<String, Object>());
	}

	@Override
	public Result update(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {

		String partyEventData = HelperMethods.getInputParamMap(inputArray).get(DTOConstants.PARTYEVENTDATA);
		if (StringUtils.isBlank(partyEventData)) {
			partyEventData = dcRequest.getParameter(DTOConstants.PARTYEVENTDATA);
		}
		logger = new LoggerUtil(CustomerResourceImpl.class);

		return updateInParty(inputArray, dcRequest, dcResponse);

	}

	private Result updateInParty(Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {

		Result result = new Result();

		Map<String, String> inputParamMap = HelperMethods.getInputParamMap(inputArray);

		String customerID = inputParamMap.get(DTOConstants.ID);
		if (StringUtils.isBlank(customerID)) {
			customerID = dcRequest.getParameter(DTOConstants.ID);
		}

		String partyID = inputParamMap.get(DTOConstants.PARTYID);
		if (StringUtils.isBlank(partyID)) {
			partyID = dcRequest.getParameter(DTOConstants.PARTYID);
		}

		if (StringUtils.isEmpty(customerID) && StringUtils.isEmpty(partyID)) {
			diagnostic.prepareDebug("Customer ID is empty : customerID -> " + customerID).log();
			ErrorCodeEnum.ERR_10209.setErrorCode(result);
			makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PARTY);
			return result;
		}

		if (StringUtils.isEmpty(partyID)) {
			String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID + DBPUtilitiesConstants.AND
					+ DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + DTOConstants.PARTY;

			try {
				result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
						URLConstants.BACKENDIDENTIFIER_GET);
			} catch (HttpCallException e) {

				alert.prepareError("Caught exception while creating Party: ", e).log();
			}

			if (HelperMethods.hasRecords(result)) {

				partyID = HelperMethods.getFieldValue(result, DTOConstants.BACKENDID);
			}

			result = new Result();
			if (StringUtils.isEmpty(partyID)) {
				diagnostic.prepareDebug("PartyID information is not available : customerID -> " + customerID + " : partyID -> "
						+ partyID).log();
				ErrorCodeEnum.ERR_10209.setErrorCode(result);
				makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PARTY);
				return result;
			}

			diagnostic.prepareDebug("partyID -> " + partyID + " is found for csutomer ID -> " + customerID).log();

		} else {
			String filter = DTOConstants.BACKENDID + DBPUtilitiesConstants.EQUAL + partyID + DBPUtilitiesConstants.AND
					+ DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + DTOConstants.PARTY;

			try {
				result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
						URLConstants.BACKENDIDENTIFIER_GET);
			} catch (HttpCallException e) {

				alert.prepareError("Caught exception while creating Party: ", e).log();
			}

			if (HelperMethods.hasRecords(result)) {

				customerID = HelperMethods.getFieldValue(result, "Customer_id");
			}

			result = new Result();
			if (StringUtils.isEmpty(customerID)) {
				diagnostic.prepareDebug("customerID information is not available : customerID -> " + customerID + " : partyID -> "
						+ partyID).log();
				ErrorCodeEnum.ERR_10209.setErrorCode(result);
				makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PARTY);
				return result;
			}

			diagnostic.prepareDebug("partyID -> " + partyID + " is found for csutomer ID -> " + customerID).log();
		}

		dcRequest.addRequestParam_("partyID", partyID);
		CustomerDTO customerDTO = CustomerUtils.buildCustomerDTOforUpdate(customerID, inputParamMap, dcRequest, true,
				true);
		customerDTO.setId(partyID);
		diagnostic.prepareDebug("InputParams for update operation is :" + inputParamMap).log();

		try {
			diagnostic.prepareDebug("CustomerDTO build for input and requst params : " + (logger.isDebugModeEnabled()
					? new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(customerDTO)
					: null)).log();
		} catch (JsonProcessingException e) {

			alert.prepareError("Caught exception while printing Party: ", e).log();
		}

		PartyCustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PartyCustomerBusinessDelegate.class);

		DBXResult response = new DBXResult();
		Map<String, Object> headers = dcRequest.getHeaderMap();
		headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
		headers.put("accountId", inputParamMap.get("accountId"));
		headers.put("companyid", inputParamMap.get("companyId"));
		JsonObject employment = new JsonObject();
		if (inputParamMap.containsKey("employments") && inputParamMap.get("employments") != null) {
			JsonArray employmentArray = new JsonParser().parse(inputParamMap.get("employments").toString())
					.getAsJsonArray();
			if (employmentArray.size() > 0) {
				JsonObject employmentObject = employmentArray.get(0).getAsJsonObject();
				employment.add("type", employmentObject.get("type"));
				employment.add("employerName", employmentObject.get("employerName"));
				employment.add("salaryFrequency", employmentObject.get("salaryFrequency"));
				employment.add("salary", employmentObject.get("salary"));
				employment.add("employerOfficePhone", employmentObject.get("employerOfficePhone"));
				employment.add("employerOfficePhoneIdd", employmentObject.get("employerOfficePhoneCode"));

				JsonArray extensionData = employmentObject.getAsJsonArray("extensionData");
				if (extensionData.size() > 0) {
					JsonObject extensionDataObject = extensionData.get(0).getAsJsonObject();
					JsonObject extensionObject = new JsonObject();
					extensionObject.add("monthlyIncome", extensionDataObject.get("monthlyIncome"));
					employment.add("extensionData", extensionObject);
				}
				JsonArray empArray = new JsonArray();
				empArray.add(employment);
				headers.put("employments", empArray.toString());
			}
		}

		if (inputParamMap.containsKey("occupations") && inputParamMap.get("occupations") != null) {
			JsonArray occupationsArray = new JsonParser().parse(inputParamMap.get("occupations").toString())
					.getAsJsonArray();
			headers.put("occupations", occupationsArray.toString());
		}
		
		if (inputParamMap.containsKey("employerAddresses") && inputParamMap.get("employerAddresses") != null) {
			JsonArray employerAddressArray = new JsonParser().parse(inputParamMap.get("employerAddresses").toString())
					.getAsJsonArray();
			if (employerAddressArray.size() > 0) {
				JsonObject employerAddress = employerAddressArray.get(0).getAsJsonObject();
				AddressDTO addressDTO = new AddressDTO();
				CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();
				if (StringUtils.isNotBlank(employerAddress.get("buildingName").toString())) {
					addressDTO.setAddressLine1(employerAddress.get("buildingName").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("flatNumber").toString())) {
					addressDTO.setAddressLine2(employerAddress.get("flatNumber").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("postalOrZipCode").toString())) {
					addressDTO.setZipCode(employerAddress.get("postalOrZipCode").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("town").toString())) {
					addressDTO.setCityName(employerAddress.get("town").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("countryCode").toString())) {
					addressDTO.setCountry(employerAddress.get("countryCode").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("countrySubdivision").toString())) {
					addressDTO.setState(employerAddress.get("countrySubdivision").getAsString());
				}
				if (StringUtils.isNotBlank(employerAddress.get("addresstype").toString())) {
					customerAddressDTO.setType_id(employerAddress.get("addresstype").getAsString());
				}
				customerAddressDTO.setIsPrimary(false);
				customerAddressDTO.setAddressDTO(addressDTO);
				customerDTO.setCustomerAddress(customerAddressDTO);
			}
		}

		if (inputParamMap.containsKey("entityName")) {
			headers.put("entityName", inputParamMap.get("entityName"));
			headers.put("incorporationCountry", inputParamMap.get("incorporationCountry"));
			headers.put("numberOfEmployees", inputParamMap.get("numberOfEmployees"));
			headers.put("dateOfIncorporation", inputParamMap.get("dateOfIncorporation"));
		}

		response = customerDelegate.updateCustomer(customerDTO, headers);

		String id = (String) response.getResponse();

		if (StringUtils.isNotBlank(id)) {
			result.addParam("success", "success");
			result.addParam(DTOConstants.PARTYID, partyID);
			diagnostic.prepareDebug("Party update is successful for ID -> " + partyID).log();
			if (StringUtils.isNotBlank(customerID)) {
				result.addParam(DTOConstants.ID, customerID);
			}
			makeAuditEntry(inputArray, dcRequest, result, dcResponse, true, DTOConstants.PARTY);
			return result;
		}

		result.addParam(DTOConstants.BACKEND_ERR_CODE, response.getDbpErrCode());
		result.addParam(DTOConstants.BACKEND_ERR_MESSAGE, response.getDbpErrMsg());
		diagnostic.prepareDebug("Party update is failed for ID -> " + partyID).log();
		ErrorCodeEnum.ERR_10213.setErrorCode(result);
		makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PARTY);
		return result;

	}

	private void makeAuditEntry(Object[] inputArray, DataControllerRequest dcRequest, Result result,
			DataControllerResponse dcResponse, boolean isSuccess, String eventType) {
		try {

			String enableEvents = URLFinder.getPathUrl("ENABLE_EVENTS", dcRequest);

			alert.prepareError("ENABLE_EVENTS=  " + enableEvents).log();

			String partyEventData = HelperMethods.getInputParamMap(inputArray).get(DTOConstants.PARTYEVENTDATA);
			if (StringUtils.isBlank(partyEventData)) {
				partyEventData = dcRequest.getParameter(DTOConstants.PARTYEVENTDATA);
			}

			JsonObject customParams = getJsonFromInput(HelperMethods.getInputParamMap(inputArray), dcRequest);

			String operation = null;
			try {
				operation = dcRequest.getServicesManager().getOperationData().getOperationId();
			} catch (AppRegistryException e) {
				alert.prepareError("Error while getting Operation from service Manager", e).log();
			}

			String eventSubType = "";

			if (enableEvents != null && enableEvents.equals("true")) {

				if (operation.toLowerCase().contains("create")) {
					eventSubType = eventType + "_CREATE";
				} else {
					eventSubType = eventType + "_UPDATE";
				}

				if (StringUtils.isNotBlank(result.getParamValueByName(DTOConstants.ID))) {
					customParams.addProperty("customerId", result.getParamValueByName(DTOConstants.ID));
				}

				if (StringUtils.isNotBlank(result.getParamValueByName(DTOConstants.PARTYID))) {
					customParams.addProperty(DTOConstants.PARTYID, result.getParamValueByName(DTOConstants.PARTYID));
				}

				if (StringUtils.isNotBlank(result.getParamValueByName(DTOConstants.CORECUSTOMERID))) {
					customParams.addProperty(DTOConstants.CORECUSTOMERID,
							result.getParamValueByName(DTOConstants.CORECUSTOMERID));
				}

				alert.prepareError("eventsArray:" + customParams.toString()).log();

				String StatusId;
				if (isSuccess) {
					StatusId = "SID_EVENT_SUCCESS";
				} else {
					StatusId = "SID_EVENT_FAILURE";
				}

				EventsDispatcher.dispatch(dcRequest, dcResponse, eventType, eventSubType,
						CustomerResourceImpl.class.getName(), StatusId, null, null, customParams);
			}

		} catch (Exception ex) {
			alert.prepareError("exception occured while sending alert for transfer=", ex).log();
		}
	}

	private JsonObject getJsonFromInput(Map<String, String> inputParamMap, DataControllerRequest dcRequest) {

		JsonObject jsonObject = new JsonObject();
		for (String key : inputParamMap.keySet()) {
			if (StringUtils.isNotBlank(inputParamMap.get(key))) {
				jsonObject.addProperty(key, inputParamMap.get(key));
			}
		}

		Iterator<String> iterator = dcRequest.getParameterNames();

		while (iterator.hasNext()) {
			String key = iterator.next();
			if (StringUtils.isNotBlank(dcRequest.getParameter(key))) {
				jsonObject.addProperty(key, dcRequest.getParameter(key));
			}
		}

		return jsonObject;
	}

	public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = getElementFromJsonObject(object, key, required);
		return element == null ? null : element.getAsString();
	}

	public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = object.get(key);
		if ((element == null) && (required)) {
			throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
		}
		return element;
	}

	public static JsonObject getJsonObjectFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = getElementFromJsonObject(object, key, required);
		if (element == null) {
			return null;
		}
		if (!element.isJsonObject()) {
			throw new IllegalArgumentException("Value for attribute '" + key + "' is not a JSON object");
		}
		return element.getAsJsonObject();
	}

	@Override
	public Result getReferenceByID(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {

		Result result = new Result();

		String referenceByID = HelperMethods.getInputParamMap(inputArray).get("referenceID");

		PartyCustomerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PartyCustomerBusinessDelegate.class);
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		DBXResult dbxResult = businessDelegate.getReferenceByID(referenceByID, dcRequest.getHeaderMap());

		if (dbxResult.getResponse() != null) {
			result = JSONToResult.convert((String) dbxResult.getResponse());
		} else {
			HelperMethods.addError(result, dbxResult);
		}

		return result;
	}

	@Override
	public Result createIdentifier(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		logger = new LoggerUtil(CustomerResourceImpl.class);
		Result result = new Result();
		Map<String, String> inputParamMap = HelperMethods.getInputParamMap(inputArray);
		String partyID = inputParamMap.get(DTOConstants.PARTYID);
		if (StringUtils.isBlank(partyID)) {
			partyID = dcRequest.getParameter(DTOConstants.PARTYID);
		}

		if (StringUtils.isEmpty(partyID)) {
			diagnostic.prepareDebug("PartyID information is not available").log();
			ErrorCodeEnum.ERR_10209.setErrorCode(result);
			return result;
		}

		String identityType = inputParamMap.get("identityType");
		if (StringUtils.isBlank(identityType)) {
			identityType = dcRequest.getParameter("identityType");
		}

		String identityNumber = inputParamMap.get("identityNumber");
		if (StringUtils.isBlank(identityNumber)) {
			identityNumber = dcRequest.getParameter("identityNumber");
		}

		PartyDTO party = new PartyDTO();
		AlternateIdentity identity = new AlternateIdentity();
		identity.setIdentityType(identityType);
		identity.setIdentityNumber(identityNumber);
		party.setPartyId(partyID);
		party.setAlternateIdentities(identity);

		Map<String, Object> headers = dcRequest.getHeaderMap();
		headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);

		PartyCustomerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PartyCustomerBusinessDelegate.class);
		DBXResult response = businessDelegate.addIdentifier(party, headers);
		String id = (String) response.getResponse();
		if (StringUtils.isNotBlank(id)) {
			result.addParam("success", "success");
			result.addParam(DTOConstants.PARTYID, partyID);
			diagnostic.prepareDebug("Party update is successful for ID -> " + partyID).log();
			return result;
		}
		result.addParam(DTOConstants.BACKEND_ERR_CODE, response.getDbpErrCode());
		result.addParam(DTOConstants.BACKEND_ERR_MESSAGE, response.getDbpErrMsg());
		diagnostic.prepareDebug("Party update is failed for ID -> " + partyID).log();
		return result;
	}
	
	@Override
	public Result createPartyRelationship(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {

		diagnostic.prepareDebug("Saving Party Relationship Data").log();
		Result result = new Result();

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String id = inputParams.get("partyID");
		String relationship = inputParams.get("relationship");
		if (StringUtils.isNotBlank(relationship)) {
			PartyCustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(PartyCustomerBusinessDelegate.class);
			Map<String, Object> headers = dcRequest.getHeaderMap();
			headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
			headers.put("companyid", inputParams.get("companyId"));
			JSONObject relationObj = new JSONObject(relationship);
			if (relationObj.has("partyId")) {
				JSONObject payload = new JSONObject();
				JSONArray dataArray = new JSONArray();
				JSONObject dataObj = new JSONObject();
				dataObj.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				dataObj.put("relatedParty", relationObj.getString("partyId"));
				dataObj.put("relationType", PartyConfiguration.getConfiguration(
						relationObj.getString("partyRelationship").replaceAll(" ", "") + ".relationship.code"));
				if (StringUtils.isNotBlank(relationObj.optString("hierarchyType"))) {
					dataObj.put("hierarchyType", relationObj.getString("hierarchyType"));
					if (relationObj.getString("hierarchyType").equalsIgnoreCase("Child")) {
						dataObj.put("relationType", PartyConfiguration.getConfiguration(
										relationObj.getString("partyRelationship").replaceAll(" ", "") + ".relationship.code")+ "Of");
					}
				}
				if(StringUtils.isNotBlank(relationObj.optString("ownershipPercentage"))) {
					dataObj.put("ownershipPercentage", relationObj.getString("ownershipPercentage"));
				}
				dataArray.put(dataObj);
				payload.put("partyRelations", dataArray);
				DBXResult response = customerDelegate.addPartyRelation(id, payload, headers);
				if (response.getResponse() != null) {
					result.addParam("success", "success");
					result.addParam(DTOConstants.PARTYID, id);
				} else {
					result.addParam("errormessage", "Unable to add Party Relationship");
					result.addParam(DTOConstants.PARTYID, id);
				}
			}

		} else {
			result.addParam("success", "success");
			result.addParam(DTOConstants.PARTYID, id);
		}

		return result;
	}
	
	@Override
	public Result get(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		
		Result result = new Result();
		String partyID = "";
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String customerId = inputParams.get(InfinityConstants.id);
		if (StringUtils.isBlank(customerId)) {
			result = new Result();
			diagnostic.prepareDebug("[GetCustomerDetailsOperation] CustomerId is empty").log();
			ErrorCodeEnum.ERR_10212.setErrorCode(result, "CustomerId is empty");
			return result;
		}
		
		Result partyIdentifier = new Result();
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
				+ DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + DTOConstants.PARTY;
		try {
			 partyIdentifier = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.BACKENDIDENTIFIER_GET);
		} catch (HttpCallException e) {
			alert.prepareError("Caught exception while fetching Party: ", e).log();
		}
		
		if (HelperMethods.hasRecords(partyIdentifier)) {
			partyID = HelperMethods.getFieldValue(partyIdentifier, DTOConstants.BACKENDID);
		}

		if (StringUtils.isEmpty(partyID)) {
			result = new Result();
			diagnostic.prepareDebug(
					"PartyID information is not available : customerID -> " + customerId + " : partyID -> " + partyID).log();
			ErrorCodeEnum.ERR_10209.setErrorCode(result);
			return result;
		}
		
		PartyCustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PartyCustomerBusinessDelegate.class);
		Map<String, Object> headers = dcRequest.getHeaderMap();
		headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
		DBXResult dbxResult = customerDelegate.getCustomer(partyID, headers);
		if(StringUtils.isNotBlank(dbxResult.getDbpErrMsg())) {
			result = new Result();
			diagnostic.prepareDebug("Party information is not available : customerID -> " + customerId + " : partyID -> " + partyID).log();
			ErrorCodeEnum.ERR_10209.setErrorCode(result);
			return result;
		}
		
		JSONObject party = new JSONObject((String)dbxResult.getResponse());		
		JSONObject customerDetails = new JSONObject();
		customerDetails.put("firstName", party.optString("firstName"));
		customerDetails.put("lastName", party.optString("lastName"));
		customerDetails.put("dateOfBirth", party.optString("dateOfBirth"));
		customerDetails.put("partyStatus", party.optString("partyStatus"));
		
		//Identity
		JSONArray partyIdentifiers = party.optJSONArray("partyIdentifiers");
		
		JSONArray identities = new JSONArray();
		
		partyIdentifiers.forEach(identifierItem -> {
			JSONObject identifier = (JSONObject) identifierItem;
			JSONObject identityEle = new JSONObject();
			if (StringUtils.equals(identifier.optString("type"), DTOConstants.PARTY_CREATE_SSN)) {
				String taxId = identifier.optString("identifierNumber");
				customerDetails.put("taxId", taxId);
			} else {
				identityEle.put("identityType", identifier.optString("type"));
				identityEle.put("identityNumber", identifier.optString("identifierNumber"));
				identityEle.put("issueDate", identifier.optString("issuedDate"));
				identityEle.put("expiryDate", identifier.optString("expiryDate"));
				identityEle.put("issuingCountry", identifier.optString("issuingCountry"));
				identityEle.put("issuingState", identifier.optString("issuingState"));
				identities.put(identityEle);
			}
		});
		customerDetails.put("identities", identities.toString());
		
		//Address and contact details
		JSONArray partyAddresses = party.optJSONArray("addresses");
		
		JSONArray addresses = new JSONArray();
		JSONArray emails = new JSONArray();
		JSONArray phoneNumbers = new JSONArray();
		partyAddresses.forEach(addressItem -> {
			JSONObject address = (JSONObject) addressItem;
			if (StringUtils.equals(address.optString("communicationNature"), "Physical")) {
				JSONObject addressEle = new JSONObject();
				addressEle.put("addressType", address.optString("addressType"));
				addressEle.put("addressLine1", address.optString("buildingName"));
				addressEle.put("addressLine2", address.optString("streetName"));
				addressEle.put("city", address.optString("town"));
				addressEle.put("state", address.optString("countrySubdivision"));
				addressEle.put("country", address.optString("countryCode"));
				addressEle.put("zipCode", address.optString("postalOrZipCode"));
				addresses.put(addressEle);
			} else if (StringUtils.equals(address.optString("communicationNature"), "Phone")) {
				JSONObject phoneEle = new JSONObject();
				phoneEle.put("addressType", address.optString("addressType"));
				phoneEle.put("countryCode", address.optString("iddPrefixPhone"));
				phoneEle.put("phoneNumber", address.optString("phoneNo"));
				phoneNumbers.put(phoneEle);
			} else if (StringUtils.equals(address.optString("communicationNature"), "Electronic")) {
				JSONObject emailEle = new JSONObject();
				emailEle.put("email", address.optString("electronicAddress"));
				emails.put(emailEle);
			}
		});
		customerDetails.put("addresses", addresses.toString());
		customerDetails.put("phoneNumbers", phoneNumbers.toString());
		customerDetails.put("emails", emails.toString());

		//Employment
		JSONArray partyEmployments = party.optJSONArray("employments");

		JSONArray employments = new JSONArray();
		partyEmployments.forEach(employmentItem -> {
			JSONObject employment = (JSONObject) employmentItem;
			JSONObject employmentEle = new JSONObject();
			employmentEle.put("type", employment.optString("type"));
			employmentEle.put("employerName", employment.optString("employerName"));
			employmentEle.put("employerOfficePhoneIdd", employment.optString("employerOfficePhoneIdd"));
			employmentEle.put("employerOfficePhone", employment.optString("employerOfficePhone"));
			employmentEle.put("country", employment.optString("country"));
			employmentEle.put("startDate", employment.optString("startDate"));
			employmentEle.put("buildingName", employment.optString("buildingName"));
			employmentEle.put("streetName", employment.optString("streetName"));
			employmentEle.put("town", employment.optString("town"));
			employmentEle.put("postalOrZipCode", employment.optString("postalOrZipCode"));
			employmentEle.put("countrySubdivision", employment.optString("countrySubdivision"));
			if (employment.has("extensionData")) {
				JSONObject extension = employment.optJSONObject("extensionData");
				if (extension.has("monthlyIncome")) {
					employmentEle.put("monthlyIncome", extension.optString("monthlyIncome"));
				}
			}
			employments.put(employmentEle);
		});
		customerDetails.put("employments", employments.toString());
		
		//Occupation
		JSONArray partyOccupations = party.optJSONArray("occupations");
		
		if (partyOccupations.length() > 0) {
			JSONObject occupation = partyOccupations.getJSONObject(0);
			customerDetails.put("occupation",
					(new JSONObject().put("occupationType", occupation.optString("occupationType")).toString()));
		}
		result = JSONToResult.convert(customerDetails.toString());
		return result;
	}
	
	@Override
	public Result createEmployments(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse, boolean isUpdate) throws HttpCallException {
		logger = new LoggerUtil(CustomerResourceImpl.class);
		diagnostic.prepareDebug("Saving Party Employment Data").log();
		Result result = new Result();
		PartyCustomerBusinessDelegate customerDelegate = (PartyCustomerBusinessDelegate) DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PartyCustomerBusinessDelegate.class);
		Map<String, String> inputParamMap = HelperMethods.getInputParamMap(inputArray);
		String customerId = inputParamMap.get(DTOConstants.ID);
		if (StringUtils.isBlank(customerId)) {
			customerId = dcRequest.getParameter(DTOConstants.ID);
		}
		if (StringUtils.isBlank(customerId)) {
			ErrorCodeEnum.ERR_10212.setErrorCode(result, "CustomerId is empty");
			return result;
		}
		String partyId = fetchPartyId(dcRequest, customerId);
		if (StringUtils.isBlank(partyId)) {
			ErrorCodeEnum.ERR_10212.setErrorCode(result, "PartyId is empty");
			return result;
		}
		Map<String, Object> headers = dcRequest.getHeaderMap();
		headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, "PreLogin");		
		JsonArray empArray = new JsonArray();
		JsonArray oldempArray = new JsonArray();
		if (inputParamMap.containsKey("employments") && inputParamMap.get("employments") != null) {
			JsonArray employmentArray = (new JsonParser()).parse(((String) inputParamMap.get("employments")).toString())
					.getAsJsonArray();
			for(int i = 0; i < employmentArray.size(); i++) {
				JsonObject employmentObject = employmentArray.get(i).getAsJsonObject();
				JsonObject employment = new JsonObject();
				employment.add("type", employmentObject.get("type"));
				employment.add("employerName", employmentObject.get("employerName"));
				employment.add("startDate", employmentObject.get("startDate"));
				if(employmentObject.has("endDate") && employmentObject.get("endDate") != null 
						&& employmentObject.get("endDate").getAsString().length() > 0)
					employment.add("endDate", employmentObject.get("endDate"));
				employment.add("employerOfficePhone", employmentObject.get("employerOfficePhone"));
				employment.add("employerOfficePhoneIdd", employmentObject.get("employerOfficePhoneCode"));
				employment.addProperty("primaryEmployment",
						Boolean.valueOf(employmentObject.get("primaryEmployment").getAsString()));
				employment.add("buildingName", employmentObject.get("addressLine1"));
				employment.add("streetName", employmentObject.get("addressLine2"));
				employment.add("town", employmentObject.get("city"));
				employment.add("countrySubdivision", employmentObject.get("state"));
				employment.add("country", employmentObject.get("country"));
				employment.add("postalOrZipCode", employmentObject.get("zipCode"));
				if(employmentObject.get("employerSegment")!=null && employmentObject.get("employerSegment").getAsString().length()>0)
					employment.add("employerSegment", employmentObject.get("employerSegment"));
				if(employmentObject.get("jobTitle")!=null && employmentObject.get("jobTitle").getAsString().length()>0)
					employment.add("jobTitle", employmentObject.get("jobTitle"));
				if(employmentObject.has("employmentReference") && employmentObject.get("employmentReference") != null 
						&& employmentObject.get("employmentReference").getAsString().length() > 0) {
					employment.add("employmentReference", employmentObject.get("employmentReference"));
					oldempArray.add((JsonElement) employment);
				} else {
					empArray.add((JsonElement) employment);
				}
				
			}
		}

		JsonArray occArray = new JsonArray();
		if (inputParamMap.containsKey("occupations") && inputParamMap.get("occupations") != null) {
			if (StringUtils.isNotEmpty(inputParamMap.get("occupations").toString())) {
				JsonArray occupationsArray = (new JsonParser())
						.parse(((String) inputParamMap.get("occupations")).toString()).getAsJsonArray();
				JsonObject occupationObject = occupationsArray.get(0).getAsJsonObject();
				JsonObject occupation = new JsonObject();
				occupation.add("occupationType", occupationObject.get("occupationType"));
				occArray.add(occupation);
			}
		}
		headers.put("occupations", occArray);
		PartyDTO party = new PartyDTO();
		party.setPartyId(partyId);
		DBXResult EmploymentRes = null;
		if(oldempArray.size()>0) {
			headers.put("employments", oldempArray);
			EmploymentRes = customerDelegate.createEmployments(party, headers,true);
			if (EmploymentRes.getResponse() != null) {
				diagnostic.prepareDebug("Party Employment creation is succesful for id -> " + partyId).log();
				result.addParam("success", "success");
				result.addParam("partyID", partyId);
			} else {
				diagnostic.prepareDebug("Party Employment creation is failed").log();
				result.addParam("BackEndErrorCode", EmploymentRes.getDbpErrCode());
				result.addParam("BackEndErrorMessage", EmploymentRes.getDbpErrMsg());
				ErrorCodeEnum.ERR_10212.setErrorCode(result);
			}
		}
		if(empArray.size()>0) {
			headers.put("employments", empArray);
			EmploymentRes = customerDelegate.createEmployments(party, headers,false);
			if (EmploymentRes.getResponse() != null) {
				diagnostic.prepareDebug("Party Employment creation is succesful for id -> " + partyId).log();
				result.addParam("success", "success");
				result.addParam("partyID", partyId);
			} else {
				diagnostic.prepareDebug("Party Employment creation is failed").log();
				result.addParam("BackEndErrorCode", EmploymentRes.getDbpErrCode());
				result.addParam("BackEndErrorMessage", EmploymentRes.getDbpErrMsg());
				ErrorCodeEnum.ERR_10212.setErrorCode(result);
			}
		}
		return result;
	}
	
	private String fetchPartyId(DataControllerRequest dcRequest, String customerId) throws HttpCallException {
		String partyID = "";
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
				+ DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + DTOConstants.PARTY;
		Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.BACKENDIDENTIFIER_GET);
		partyID = HelperMethods.getFieldValue(result, DTOConstants.BACKENDID);
		return partyID;
	}

	@Override
	public Result updateIdentifier(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		
		logger = new LoggerUtil(CustomerResourceImpl.class);
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String partyID = inputParams.get("partyID");
		
		PartyCustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PartyCustomerBusinessDelegate.class);
		Map<String, Object> headers = dcRequest.getHeaderMap();
		headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
		headers.put("companyid", inputParams.get("companyId"));
		JsonArray partyIdentities = new JsonArray();
		
		if (inputParams.containsKey("identities") && inputParams.get("identities") != null) {
			JsonArray identitesArray = (new JsonParser()).parse(((String) inputParams.get("identities")).toString())
					.getAsJsonArray();
			for(int i = 0; i<identitesArray.size();i++) {
				JsonObject identityObject = identitesArray.get(i).getAsJsonObject();
				JsonObject identity = new JsonObject();
				identity.add("type", identityObject.get("IdentityType"));
		        identity.add("identifierNumber",identityObject.get("IdentityNumber"));
		        identity.add("issuedDate",identityObject.get("IssueDate"));
		        identity.add("expiryDate",identityObject.get("ExpiryDate"));
		        identity.add("issuingCountry",identityObject.get("IssuingCountry"));
		        identity.add("issuingCountrySubdivision",identityObject.get("IssuingState"));
		        partyIdentities.add(identity);
			}				
		}
		
		DBXResult response = customerDelegate.updateIdentifier(partyID,partyIdentities,headers);
		
		if (response.getResponse() != null) {
			diagnostic.prepareDebug("Party Employment creation is succesful for id -> " + partyID).log();
			result.addParam("success", "success");
			result.addParam("partyID", partyID);
		} else {
			diagnostic.prepareDebug("Party Employment creation is failed").log();
			result.addParam("BackEndErrorCode", response.getDbpErrCode());
			result.addParam("BackEndErrorMessage", response.getDbpErrMsg());
			ErrorCodeEnum.ERR_10212.setErrorCode(result);
		}
		return result;

	}

}
