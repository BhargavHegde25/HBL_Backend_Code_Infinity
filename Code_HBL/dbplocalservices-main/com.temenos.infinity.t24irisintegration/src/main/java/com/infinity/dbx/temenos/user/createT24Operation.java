package com.infinity.dbx.temenos.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.auth.AuthenticationWithoutSession;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class createT24Operation implements JavaService2, AccountsConstants, TemenosConstants, UserConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String authToken = TokenUtils.getT24AuthToken(request);
			Map<String,Object> headerMap = request.getHeaderMap();
			String userType = request.getParameter("userType");
			JsonObject config = new JsonObject();
			if(StringUtils.equals(userType, RETAIL_USER) ) {
				String retailSpotlightConfig =
		                BundleConfigurationHandler.fetchConfigurationValueOnKey(BundleConfigurationHandler.BUNDLEID_RETAIL,
		                        BundleConfigurationHandler.RETAIL_CUSTOMER_CONFIG, headerMap);
				config = new JsonParser().parse(retailSpotlightConfig).getAsJsonObject();
			}
			else if(StringUtils.equals(userType, SME_USER)|| StringUtils.equalsIgnoreCase(userType, COMPANY)||StringUtils.equalsIgnoreCase(userType, RELATED_COMPANY)) {
				String smeSpotlightConfig =
		                BundleConfigurationHandler.fetchConfigurationValueOnKey(BundleConfigurationHandler.BUNDLEID_SME,
		                        BundleConfigurationHandler.SME_CUSTOMER_CONFIG, headerMap);
				config = new JsonParser().parse(smeSpotlightConfig).getAsJsonObject();
			}
			// Address details
			JsonArray addresses = new JsonParser().parse(request.getParameter("addresses")).getAsJsonArray();
			for(int i=0;i<addresses.size();i++) {
				String addressType = addresses.get(i).getAsJsonObject().get("Addr_type").getAsString();
				if(addressType.equalsIgnoreCase("Home") || addressType.equalsIgnoreCase("Office")) {
					request.addRequestParam_("AddressLine1", addresses.get(i).getAsJsonObject().get("addrLine1").getAsString());
					request.addRequestParam_("AddressLine2", addresses.get(i).getAsJsonObject().get("addrLine2").getAsString());
					request.addRequestParam_("City", addresses.get(i).getAsJsonObject().get("City_id").getAsString());
					request.addRequestParam_("State", addresses.get(i).getAsJsonObject().get("Region_id").getAsString());
					request.addRequestParam_("ZipCode", addresses.get(i).getAsJsonObject().get("ZipCode").getAsString());
					request.addRequestParam_("Country", addresses.get(i).getAsJsonObject().get("countryCode").getAsString());
					if(addresses.get(i).getAsJsonObject().has("phoneNo") && StringUtils.isNotBlank(addresses.get(i).getAsJsonObject().get("phoneNo").getAsString())) {
						request.addRequestParam_("PhoneNo", addresses.get(i).getAsJsonObject().get("phoneNo").getAsString());
					}
					if(addresses.get(i).getAsJsonObject().has("electronicAddress") && StringUtils.isNotBlank(addresses.get(i).getAsJsonObject().get("electronicAddress").getAsString())) {
						request.addRequestParam_("ElectronicAddress", addresses.get(i).getAsJsonObject().get("electronicAddress").getAsString());
					}
				}
			}
			
			// To save additional fields in Transact in case of Company
			if (StringUtils.equalsIgnoreCase(userType, COMPANY)) {
				request.addRequestParam_("lastName", request.getParameter("organisationLegalType").toString());
				String indusType = request.getParameter("industryType").toString().replaceAll("\\s", "");
				String indusId = CommonUtils.getProperty(INDUSTRY_TYPES, indusType, "industry", "type");
				request.addRequestParam_("industryId", indusId);
				request.addRequestParam_("email", request.getParameter("companyEmailAddress").toString());
				
				if(StringUtils.isNotBlank(request.getParameter("PhoneNumbers"))) {
					JSONArray phoneNumbers = new JSONArray(request.getParameter("PhoneNumbers").toString());
					Optional<Object> companyPrimePhoneObj = StreamSupport.stream(phoneNumbers.spliterator(), true)
							.filter(n -> (StringUtils.equalsIgnoreCase("Office",
									((JSONObject) n).optString("Extension"))
									&& StringUtils.equalsIgnoreCase("1", ((JSONObject) n).optString("isPrimary"))))
							.findAny();
					
					JSONObject companyPrimePhoneJSON = new JSONObject();
					if (companyPrimePhoneObj.isPresent()) {
						companyPrimePhoneJSON = (JSONObject) companyPrimePhoneObj.get();
						JSONObject obj = new JSONObject(companyPrimePhoneJSON.toString());
						request.addRequestParam_("MobileCountryCode" , obj.optString("phoneCountryCode"));
						request.addRequestParam_("MobileNumber" , obj.optString("phoneNumber"));
					}
					
				}
				
			}
			
			// Phone number
			if(StringUtils.isNotBlank(request.getParameter("PhoneNumbers"))) {
				JsonArray phoneNumbers = new JsonParser().parse(request.getParameter("PhoneNumbers")).getAsJsonArray();
				for(int i=0;i<phoneNumbers.size();i++) {
					String Extension = phoneNumbers.get(i).getAsJsonObject().get("Extension").getAsString();
					if(Extension.equalsIgnoreCase("Home")) {
						request.addRequestParam_("MobileCountryCode",
								phoneNumbers.get(0).getAsJsonObject().get("phoneCountryCode").getAsString());
						request.addRequestParam_("MobileNumber",
								phoneNumbers.get(0).getAsJsonObject().get("phoneNumber").getAsString());
					}
				}
			}
			// Date of birth
			request.addRequestParam_("dateOfBirth",request.getParameter("dateOfBirth"));
			
			//gender
			String gender = request.getParameter("gender");
			if(StringUtils.isNotBlank(gender))
				request.addRequestParam_("gender", gender.toUpperCase());
			else
				request.addRequestParam_("gender", "");
			
			// Identity details
			if(StringUtils.isNotBlank(request.getParameter("identities"))) {
				JsonArray identities = new JsonParser().parse(request.getParameter("identities")).getAsJsonArray();
				String identityType = identities.get(0).getAsJsonObject().get("IdentityType").getAsString();
				String identityNumber = identities.get(0).getAsJsonObject().get("IdentityNumber").getAsString();
				String expiryDate = identities.get(0).getAsJsonObject().get("ExpiryDate").getAsString();
				JsonElement issueDateEle = identities.get(0).getAsJsonObject().get("IssueDate");
				String issueDate = null;
				if(issueDateEle!=null){
					issueDate = issueDateEle.getAsString();
				}
				String identityMappingStr = TemenosUtils.readContentFromFile(IDENTITY_MAPPING_JSON);
				JsonObject identityMapping = new JsonParser().parse(identityMappingStr).getAsJsonObject();
				request.addRequestParam_("IdentityType", identityMapping.get(identityType).getAsString());
				request.addRequestParam_("IdentityNumber", identityNumber);
				request.addRequestParam_("ExpiryDate", expiryDate);
				if (StringUtils.isEmpty(issueDate)) {
					String period = CommonUtils.getProperty(USER_PROPERTIES, "ISSUEDATE", "DEFAULT", "PERIOD");
					issueDate = LocalDate.now().minusDays(Integer.parseInt(period))
							.format(DateTimeFormatter.ofPattern(MMDDYYYY));
					issueDate = TemenosUtils.convertDateFormat(issueDate, MMDDYYYY, YYYYMMDD);
				}
				request.addRequestParam_("IssueDate", issueDate);
			}
			// Income Employment Details
			JsonArray employments = new JsonArray();
			JsonArray address = new JsonArray();
			JsonArray occupationDetails = new JsonArray();
			JSONObject occupationType = new JSONObject();
			if(StringUtils.isNotBlank(request.getParameter("employments")))
				employments = new JsonParser().parse(request.getParameter("employments")).getAsJsonArray();
			if(StringUtils.isNotBlank(request.getParameter("employerAddresses")))
				address = new JsonParser().parse(request.getParameter("employerAddresses")).getAsJsonArray();
			if(StringUtils.isNotBlank(request.getParameter("occupations")))
				occupationDetails = new JsonParser().parse(request.getParameter("occupations")).getAsJsonArray();
			if (employments.size() > 0) {
				JsonObject employmentObject = employments.get(0).getAsJsonObject();
				JsonArray extensionData = employmentObject.get("extensionData").getAsJsonArray();
				if(extensionData.size() > 0) {
					JsonObject incomeObject = employmentObject.get("extensionData").getAsJsonArray().get(0).getAsJsonObject();
					String monthlyIncome = incomeObject.get("monthlyIncome").getAsString();
					request.addRequestParam_("monthlyIncome", monthlyIncome);
				}
				if (StringUtils.isNotBlank(employmentObject.get("type").getAsString())) {
					String employmentStatusType = employmentObject.get("type").getAsString();
					String employmentStatusMappingStr = TemenosUtils.readContentFromFile(EMPLOYMENT_STATUS_MAPPING_JSON);
					JsonObject employmentStatusMapping = new JsonParser().parse(employmentStatusMappingStr).getAsJsonObject();
					request.addRequestParam_("employmentStatus", employmentStatusMapping.get(employmentStatusType).getAsString());
				}
				if (employmentObject.has("employerName") && StringUtils.isNotEmpty(employmentObject.get("employerName").toString())) {
					request.addRequestParam_("employerName", employmentObject.get("employerName").getAsString());
				}
					
				if (employmentObject.has("employerOfficePhone") && StringUtils.isNotEmpty(employmentObject.get("employerOfficePhone").toString())) {
					request.addRequestParam_("employerOfficePhone", employmentObject.get("employerOfficePhone").getAsString());
				}
					
				if (employmentObject.has("employerOfficePhoneCode") && StringUtils.isNotEmpty(employmentObject.get("employerOfficePhoneCode").toString())) {
					String employerOfficePhoneCode = employmentObject.get("employerOfficePhoneCode").getAsString();
					if(!employerOfficePhoneCode.contains("+")) {
						employerOfficePhoneCode = "+"+employerOfficePhoneCode.trim();
					}
					request.addRequestParam_("employerCountryCode",employerOfficePhoneCode);
				}
				request.addRequestParam_("incomeFrequency", "M0101");
				request.addRequestParam_("salaryCurrency", "USD");
				if (occupationDetails.size() > 0) {
					JsonObject occupationObject = occupationDetails.get(0).getAsJsonObject();
					if (occupationObject.has("occupationType") && StringUtils.isNotEmpty(occupationObject.get("occupationType").getAsString())) {
						occupationType.put("occupationType" , occupationObject.get("occupationType").getAsString());
						request.addRequestParam_("occupation", occupationObject.get("occupationType").getAsString());
					}
				}
				if (address.size() > 0) {
					JsonObject addressObject = address.get(0).getAsJsonObject();
					String addressLine1 = null;
					String addressLine2 = null;
					if (StringUtils.isNotBlank(addressObject.get("flatNumber").getAsString()))
						addressLine1 = addressObject.get("flatNumber").getAsString();
					if (StringUtils.isNotBlank(addressObject.get("buildingName").getAsString()))
						addressLine2 = addressObject.get("buildingName").getAsString();
					request.addRequestParam_("employerAddressLines", addressLine1 + " " + addressLine2);
					if (StringUtils.isNotBlank(addressObject.get("countrySubdivision").getAsString()))
						request.addRequestParam_("employerState", addressObject.get("countrySubdivision").getAsString());
					if (StringUtils.isNotBlank(addressObject.get("town").getAsString()))
						request.addRequestParam_("employerCity", addressObject.get("town").getAsString());
					if (StringUtils.isNotBlank(addressObject.get("countryCode").getAsString()))
						request.addRequestParam_("employerCountry", addressObject.get("countryCode").getAsString());
					if (StringUtils.isNotBlank(addressObject.get("postalOrZipCode").getAsString()))
						request.addRequestParam_("employerZipCode", addressObject.get("postalOrZipCode").getAsString());
				}
			}
			
			List<Map<String, Object>> employmentPayload = new ArrayList<Map<String, Object>>();
			String employmentStatusMappingStr = TemenosUtils.readContentFromFile(EMPLOYMENT_STATUS_MAPPING_JSON);
			JsonObject employmentStatusMapping = new JsonParser().parse(employmentStatusMappingStr).getAsJsonObject();
			if (StringUtils.isNotBlank(request.getParameter("employDetails"))) {
				JSONArray employDetails = new JSONArray(request.getParameter("employDetails").toString());

				StreamSupport.stream(employDetails.spliterator(), true)
						.map(employDetailObj -> (JSONObject) employDetailObj).forEach((employDetailObj) -> {

							List<Map<String, Object>> arr = new ArrayList<Map<String, Object>>();
							Map<String, Object> obj = new HashMap<String, Object>();
							List<String> addressFields = new ArrayList<String>() {
								{
									add("addressLine1");
									add("addressLine2");
									add("city");
									add("zipCode");
									add("state");
									add("country");
								}
							};
							
							addressFields.forEach(addressField -> updateEmploymentList(
									StringUtils.isNotEmpty(employDetailObj.optString(addressField))
											? employDetailObj.optString(addressField)
											: "NA",
									arr));

							obj.put("employerName", employDetailObj.optString("employerName"));
							obj.put("employStartDate", employDetailObj.optString("startDate"));
							obj.put("employStatus", employmentStatusMapping.get(employDetailObj.optString("type")).getAsString());
							obj.put("occupationType", occupationType.optString("occupationType"));
							obj.put("address", arr);

							employmentPayload.add(obj);

						});

			}

			request.addRequestParam_("employDetails", JSONUtils.stringify(employmentPayload));
			
			String customerId = request.getParameter("id");
			String companyId = request.getParameter("companyId");
			String partyId = request.getParameter("partyID");
			
			 if (StringUtils.isBlank(customerId)) {
			        result = new Result();
			        diagnostic.prepareDebug("createT24Operation is failed").log();
			        throw new Exception("customerId is empty");
			}
			
			if (StringUtils.isBlank(companyId)) {
			    result = new Result();
			    diagnostic.prepareDebug("createT24Operation is failed\"").log();
			    throw new Exception("companyId is empty");
			}
			
			if(StringUtils.isNotBlank(partyId)) {
				request.addRequestParam_("externalCustomerId", partyId);
			}
			//Storing DigitalProfileId into T24
			if(StringUtils.isNotBlank(customerId)) {
				request.addRequestParam_("digitalProfileId", customerId);
			}
			String entityName = request.getParameter("entityName");
			if (StringUtils.isNotBlank(entityName)) {
				request.addRequestParam_("firstName", entityName);
				request.addRequestParam_("Country", request.getParameter("incorporationCountry"));
				String dateOfIncorporation = request.getParameter("dateOfIncorporation");
				if (StringUtils.isNotBlank(dateOfIncorporation)) {
					request.addRequestParam_("IssueDate", dateOfIncorporation);
					request.addRequestParam_("dateOfBirth", dateOfIncorporation);
					request.addRequestParam_("birthIncorpDate", dateOfIncorporation);
					if(StringUtils.isNotBlank(request.getParameter("registrationDetails")))
					{
						JsonObject registrationDetails = new JsonParser().parse(request.getParameter("registrationDetails")).getAsJsonObject();
						request.addRequestParam_("IdentityNumber",
								registrationDetails.get("registrationNumber").getAsString());
						request.addRequestParam_("ExpiryDate", registrationDetails.get("expiryDate").getAsString());
					}
					request.addRequestParam_("IdentityType", COMPANY_REG_NO);
				}
			}
			
			String relationship = request.getParameter("relationship");
			if (StringUtils.isNotBlank(relationship)) {
				JsonObject relationshipObj = new JsonParser().parse(relationship).getAsJsonObject();
				if (StringUtils.isNotBlank(relationshipObj.get("cif").getAsString())) {
					String propertyRelation = CommonUtils.getProperty(USER_PROPERTIES,
							relationshipObj.get("partyRelationship").getAsString().replaceAll(" ", ""), "relationship",
							"code");
					if (StringUtils.isNotBlank(propertyRelation)) {
						request.addRequestParam_("jointCustomer", relationshipObj.get("cif").getAsString());
						request.addRequestParam_("jointRelationCode", propertyRelation);
					}
				}
			}
			
			//UserType
			String sectorId = "";
			if(StringUtils.equals(userType, RETAIL_USER))
			{
				sectorId =config.get("sectorTypeRetail").getAsString();
				request.addRequestParam_("sectorId", sectorId);
			}
			else if(StringUtils.equals(userType, SME_USER)) {
				sectorId =config.get("sectorTypeSme").getAsString();
				request.addRequestParam_("sectorId", sectorId);
			}
			else if(StringUtils.equalsIgnoreCase(userType, COMPANY)) {
				sectorId =config.get("sectorTypeCompany").getAsString();
				request.addRequestParam_("sectorId", sectorId);
			}
			else if(StringUtils.equalsIgnoreCase(userType, RELATED_COMPANY)) {
				sectorId =config.get("sectorTypeRelatedCompany").getAsString();
				request.addRequestParam_("sectorId",sectorId);
			}
			
			if(request.getParameter("industryId") == null) {
				String indusId = config.get("industryId").getAsString();
				request.addRequestParam_("industryId", indusId);
			}
			String extSystemId = config.get("externalSystemId").getAsString();
			String infSystemId = config.get("infinitySystemId").getAsString();
			request.addRequestParam_("infinitySystemId", infSystemId);
			request.addRequestParam_("externalSystemId", extSystemId);
			String lang = config.get("language").getAsString();
			String accountOffId = config.get("accountOfficerId").getAsString();
			String target = config.get("target").getAsString();
			String cusStatus = config.get("customerStatus").getAsString();
			request.addRequestParam_("language", lang);
			request.addRequestParam_("accountOfficerId", accountOffId);
			request.addRequestParam_("target", target);
			request.addRequestParam_("customerStatus", cusStatus);
			request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);
			headerMap.put(TemenosConstants.PARAM_AUTHORIZATION, authToken);
			result = CommonUtils.callIntegrationService(request, null, headerMap, UserConstants.SERVICE_ID_ONBOARDING_USER,
					UserConstants.OP_CREATE_T24, true);
			String id = result.getAllRecords().get(0).getParamValueByName("id");
			addBackendIdentifierTableEntry(id, customerId, companyId, request);
		} catch (Exception exception) {
			alert.prepareError("Error in createt24Operation:" + exception.toString()).log();
			result.addParam(new Param("dbpErrCode", "10213"));
			result.addParam(new Param("dbpErrMsg", "Create T24 failed"));
		}
		return result;
	}
	
	private void addBackendIdentifierTableEntry(String id, String coreCustomerId, String companyId,
			DataControllerRequest request) {
		String schema = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME", request);
		Result result = new Result();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", UUID.randomUUID().toString());
		map.put("Customer_id", coreCustomerId);
		map.put("BackendId", id);
		map.put("CompanyId", companyId);
		map.put("BackendType", UserConstants.T24);
		map.put("identifier_name", UserConstants.customerId);
		map.put("sequenceNumber", 1);
		map.put("companyLegalUnit", companyId);
		result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
				UserConstants.DB_SERVICE, schema + UserConstants.OP_CREATE_BACKENDIDENTIFIER);

	}
	
	private void updateEmploymentList(String value,List al) {		
		Map<String,Object> hm = new HashMap<String,Object>();
		hm.put("address",value);		
		al.add(hm);		
	}

}