package com.temenos.dbx.usermanagement.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.CoreCustomerBackendDelegateImpl;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class CoreCustomerPartyBackendDelegateImpl implements CoreCustomerBackendDelegate {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	LoggerUtil logger = new LoggerUtil(CoreCustomerPartyBackendDelegateImpl.class);

	private static final String PARTY_SEARCH5 = "/api/v5.0.0/party/parties?";
	private static final String PARTY_GET4 = "/api/v4.0.0/party/parties/";
	private static final String PARTY_GET5 = "/api/v5.0.0/party/parties/";
	private static final String PARTY_RELATIONSHIP = "/relationships";

	/*
	 * This backenddelagate method is created to get the customer details from T24
	 * in bulk
	 */

	@Override
	public DBXResult searchCoreCustomers(MembershipDTO membershipDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		DBXResult responseDTO = new DBXResult();
		Map<String, Object> procParams = new HashMap<>();
		procParams.put("_id", StringUtils.isNotBlank(membershipDTO.getId()) ? membershipDTO.getId() : "");
		procParams.put("_name", StringUtils.isNotBlank(membershipDTO.getName()) ? membershipDTO.getName() : "");
		procParams.put("_email", StringUtils.isNotBlank(membershipDTO.getEmail()) ? membershipDTO.getEmail() : "");
		procParams.put("_phone", StringUtils.isNotBlank(membershipDTO.getPhone()) ? membershipDTO.getPhone() : "");
		procParams.put("_dateOfBirth",
				StringUtils.isNotBlank(membershipDTO.getDateOfBirth()) ? membershipDTO.getDateOfBirth() : "");
		procParams.put("_status", StringUtils.isNotBlank(membershipDTO.getStatus()) ? membershipDTO.getStatus() : "");
		procParams.put("_city",
				(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getCityName()))
						? membershipDTO.getAddress().getCityName()
						: "");
		procParams.put("_country",
				(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getCountry()))
						? membershipDTO.getAddress().getCountry()
						: "");
		procParams.put("_zipCode",
				(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getZipCode()))
						? membershipDTO.getAddress().getZipCode()
						: "");
		if (StringUtils.isNotBlank(membershipDTO.getTaxId())) {
			procParams.put("_taxId", StringUtils.isNotBlank(membershipDTO.getTaxId()) ? membershipDTO.getTaxId() : "");
		}
		if (StringUtils.isNotBlank(membershipDTO.getCompanyLegalUnit())) {
			procParams.put("_legalEntityId",
					StringUtils.isNotBlank(membershipDTO.getCompanyLegalUnit()) ? membershipDTO.getCompanyLegalUnit()
							: "");
		}

		JsonObject response = new JsonObject();
		try {
			response = searchCoreCustomerParty(procParams, headersMap);
			if (JSONUtil.hasKey(response, "records")) {
				if (response.get("records").getAsJsonArray().size() > 0) {
					responseDTO.setResponse(response.get("records").getAsJsonArray());
				}
			} else {
				alert.prepareError("CoreCustomerBackendDelegateImpl : Backend response is not appropriate " + response).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_10756);
			}
		} catch (Exception e) {
			alert.prepareError("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customers"
					+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10756);
		}
		return responseDTO;
	}

	public void addBackendHeaders(Map<String, Object> headersMap, String id, String legalEntityId) {
		HelperMethods.addMSJWTAuthHeader(headersMap, AuthConstants.PRE_LOGIN_FLOW);

		if (StringUtils.isNotBlank(legalEntityId)) {
			headersMap.put("companyId", legalEntityId);
		} else {
			BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setBackendId(id);
			backendIdentifierDTO
					.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
			backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();

			// if (headersMap.get("companyId") != null &&
			// StringUtils.isBlank(headersMap.get("companyId").toString())) {
			if (backendIdentifierDTO != null && StringUtils.isNotBlank(backendIdentifierDTO.getCompanyLegalUnit())) {
				headersMap.put("companyId", backendIdentifierDTO.getCompanyLegalUnit());

			} else {
				/* This is for fall back.. */
				headersMap.put("companyId",
						EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
			}
			// }
		}
	}
	   
	private JsonObject searchCoreCustomerParty(Map<String, Object> procParams, Map<String, Object> headersMap) {
		addBackendHeaders(headersMap, (String) procParams.get("_id"),
				(String) procParams.get("_legalEntityId"));
		if (procParams.containsKey("_combinedids")) {
			procParams.put("_id", procParams.get("_combinedids"));
		}

		JsonObject processedResult = new JsonObject();
		JsonArray recordsArray = new JsonArray();
		String legalEntityId = (String) procParams.get("_legalEntityId");
		String companyId = "";
		String partyId = "";

		if (StringUtils.isNotBlank(legalEntityId)) {
			headersMap.put("companyId", legalEntityId);
			companyId = legalEntityId;
		}

		String queryParams = "";
		String id = (String) procParams.get("_id");
		String phone = "";
		String phonenumber = (String) procParams.get("_phone");
		String[] phoneno = phonenumber.split("-");
		if (phoneno != null && phoneno.length == 2) {
			phone = phoneno[1];

		}

		String name = (String) procParams.get("_name");
		String dateOfBirth = (String) procParams.get("_dateOfBirth");
		String taxId = (String) procParams.get("_taxId");
		String email = (String) procParams.get("_email");
		String zipCode = (String) procParams.get("_zipCode");

		if ((StringUtils.isNotBlank(name)) && (StringUtils.isNotBlank(dateOfBirth))
				&& (StringUtils.isNotBlank(taxId))) {
			queryParams += "lastName=" + name;
			queryParams += "&";
			queryParams += "dateOfBirth=" + dateOfBirth;
			queryParams += "&";
			queryParams += "identifierType=" + DTOConstants.SOCIAL_SECURITY_NUMBER;
			queryParams += "&";
			queryParams += "identifierNumber=" + taxId;
			queryParams = queryParams.replace(" ", "%20");
		}

		else if ((StringUtils.isNotBlank(email)) && (StringUtils.isNotBlank(dateOfBirth))) {
			queryParams += "emailId=" + email;
			queryParams += "&";
			queryParams += "dateOfBirth=" + dateOfBirth;
		}

		else if (StringUtils.isNotBlank(id)) {
			
			partyId = id;
			id = companyId + "-" + id;

			queryParams += "alternateIdentifierNumber=" + id;
			queryParams += "&";
			queryParams += "alternateIdentifierType=BackOfficeIdentifier";
		} else if (StringUtils.isNotBlank(name)) {
			queryParams += "lastName=" + name;
		} else if (StringUtils.isNotBlank(dateOfBirth)) {
			queryParams += "dateOfBirth=" + dateOfBirth;
		} else if (StringUtils.isNotBlank(phone)) {
			queryParams += "contactNumber=" + phone;
		} else if (StringUtils.isNotBlank(email)) {
			queryParams += "emailId=" + email;
		} else if (StringUtils.isNotBlank(zipCode)) {
			queryParams += "lastName=" + name;
			queryParams += "&";
			queryParams += "postcode=" + zipCode;
		} else if (StringUtils.isNotBlank(taxId)) {
			queryParams += "identifierType=" + DTOConstants.SOCIAL_SECURITY_NUMBER;
			queryParams += "&";
			queryParams += "identifierNumber=" + taxId;
			queryParams = queryParams.replace(" ", "%20");
		}

		headersMap = HelperMethods.addJWTAuthHeaderParty(headersMap, AuthConstants.PRE_LOGIN_FLOW);
		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) + PARTY_SEARCH5 + queryParams;

		if(StringUtils.isNotBlank(partyId) && partyId.length()==10) {
			queryParams = partyId;
			partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) + PARTY_GET5 + queryParams;
		}
		
		DBXResult dbxResult1 = new DBXResult();
		dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headersMap);

		JsonArray partyJsonArray = new JsonArray();
		if (dbxResult1.getResponse() != null) {
			JsonElement partyResponse = new JsonParser().parse((String) dbxResult1.getResponse());
			if (partyResponse.isJsonObject()) {
				JsonObject partyResponseObject = partyResponse.getAsJsonObject();
				if (partyResponseObject.has("parties") && partyResponseObject.get("parties").isJsonArray()
						&& partyResponseObject.get("parties").getAsJsonArray().size() > 0) {
					partyJsonArray = partyResponseObject.get("parties").getAsJsonArray();
				}
				else if(!partyResponseObject.isJsonNull()) {
					partyJsonArray.add(partyResponseObject);
				}
			}
		}

		
		if (partyJsonArray.size() > 0) {
			JsonArray customerDetails = new JsonArray();
			for (JsonElement jsonelement : partyJsonArray) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("firstName", JSONUtil.getString(jsonelement.getAsJsonObject(), "firstName"));
				jsonObject.addProperty("name",
						(jsonelement.getAsJsonObject().has("firstName")
								&& !jsonelement.getAsJsonObject().get("firstName").isJsonNull()
								&& StringUtils.isNotBlank(jsonelement.getAsJsonObject().get("firstName").getAsString())
										? jsonelement.getAsJsonObject().get("firstName").getAsString()
										: "")
								+ " "
								+ (jsonelement.getAsJsonObject().has("lastName")
										&& !jsonelement.getAsJsonObject().get("lastName").isJsonNull()
										&& StringUtils
												.isNotBlank(jsonelement.getAsJsonObject().get("lastName").getAsString())
														? jsonelement.getAsJsonObject().get("lastName").getAsString()
														: ""));
				jsonObject.addProperty("lastName", JSONUtil.getString(jsonelement.getAsJsonObject(), "lastName"));
				jsonObject.addProperty("partyId", JSONUtil.getString(jsonelement.getAsJsonObject(), "partyId"));
				jsonObject.addProperty("dateOfBirth", JSONUtil.getString(jsonelement.getAsJsonObject(), "dateOfBirth"));
				jsonObject.addProperty("gender", JSONUtil.getString(jsonelement.getAsJsonObject(), "gender"));
				jsonObject.addProperty("maritalStatus",
						JSONUtil.getString(jsonelement.getAsJsonObject(), "maritalStatus"));

				String taxId1 = "";
				String type = "";
				if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.PARTYIDENTIFIERS)
						&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).isJsonArray() && jsonelement
								.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray().size() > 0) {
					customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray();
					for (JsonElement taxids : customerDetails) {
						type = JSONUtil.getString(taxids.getAsJsonObject(), "type");
						if (type.equalsIgnoreCase("SOCIAL.SECURITY.NO")) {
							taxId1 = JSONUtil.getString(taxids.getAsJsonObject(), "identifierNumber");
							jsonObject.addProperty("taxId", taxId1);
						}
					}
				}

				String employmenttype = "";
				if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.EMPLOYMENTS)
						&& jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).isJsonArray()
						&& jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).getAsJsonArray().size() > 0) {
					customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).getAsJsonArray();
					for (JsonElement employments : customerDetails) {
						employmenttype = JSONUtil.getString(employments.getAsJsonObject(), "type");
						jsonObject.addProperty("employmentStatus", employmenttype);
					}
				}

				if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.ALTERNATEIDENTITIES)
						&& jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).isJsonArray()
						&& jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray()
								.size() > 0) {
					customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES)
							.getAsJsonArray();
					for (JsonElement customerInfos : customerDetails) {
						String identityNumber = JSONUtil.getString(customerInfos.getAsJsonObject(), "identityNumber");
						String[] identityNumberArray = identityNumber.split("-");
						if (identityNumberArray != null && identityNumberArray.length == 2) {
							id = identityNumberArray[1];
							jsonObject.addProperty("id", id);

						}
					}
					
				}
				
				if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.CLASSIFICATIONS)
						&& jsonelement.getAsJsonObject().get(DTOConstants.CLASSIFICATIONS).isJsonArray()
						&& jsonelement.getAsJsonObject().get(DTOConstants.CLASSIFICATIONS).getAsJsonArray()
								.size() > 0) {
					customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.CLASSIFICATIONS)
							.getAsJsonArray();
					for (JsonElement classifications : customerDetails) {
						JsonObject classification = classifications.getAsJsonObject();
						if (classification.has("classificationCode")
								&& classification.get("classificationCode").getAsString().contains("SECTOR")) {
							String sectorId = classification.get("classificationCode").getAsString();
							jsonObject.addProperty("sectorId", sectorId.split("-")[1]);
						}
					}
				}

				String partyType = JSONUtil.getString(jsonelement.getAsJsonObject(), "partyType");
				if (partyType.equalsIgnoreCase("Individual")) {
					jsonObject.addProperty("isBusinessType", "false");
				} else {
					jsonObject.addProperty("isBusinessType", "true");
				}
				String address = "";
				JsonObject addressLine2 = new JsonObject();
				if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.PARTYADDRESS)
						&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).isJsonArray()
						&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).getAsJsonArray().size() > 0) {
					customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).getAsJsonArray();
					for (JsonElement customerInfo : customerDetails) {
						if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
								&& "Electronic".equalsIgnoreCase(
										JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))) {
							String electronicAddress = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"electronicAddress");
							jsonObject.addProperty("email", electronicAddress);
						}
						if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
								&& "Phone".equalsIgnoreCase(
										JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))) {
							String phonePrefix = JSONUtil.getString(customerInfo.getAsJsonObject(), "iddPrefixPhone");
							String phoneNo = JSONUtil.getString(customerInfo.getAsJsonObject(), "phoneNo");
							jsonObject.addProperty("phone", phonePrefix + "" + phoneNo);
						}
						if ("Physical".equalsIgnoreCase(
								JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))
								&& "true".equalsIgnoreCase(
										JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))) {
							String streetName = JSONUtil.getString(customerInfo.getAsJsonObject(), "streetName");
							String town = JSONUtil.getString(customerInfo.getAsJsonObject(), "town");
							String postalOrZipCode = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"postalOrZipCode");
							String country = JSONUtil.getString(customerInfo.getAsJsonObject(), "countryCode");
							String countrySubDivision = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"countrySubdivision");
							String FlatNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "flatNumber");
							String BuildingNumber = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"buildingNumber");
							String BuildingName = JSONUtil.getString(customerInfo.getAsJsonObject(), "buildingName");
							String Floor = JSONUtil.getString(customerInfo.getAsJsonObject(), "floor");
							String postBoxNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "postBoxNumber");
							jsonObject.addProperty("addressLine1", streetName);
							jsonObject.addProperty("addressLine2",
									FlatNumber + BuildingNumber + BuildingName + Floor + postBoxNumber);
							jsonObject.addProperty("cityName", town);
							jsonObject.addProperty("zipCode", postalOrZipCode);
							jsonObject.addProperty("country", country);
							jsonObject.addProperty("state", countrySubDivision);
							if ((FlatNumber == "") && (BuildingNumber == "") && (BuildingName == "") && (Floor == "")
									&& (postBoxNumber == "")) {
								if (customerInfo.getAsJsonObject().has("addressFreeFormat") && customerInfo.getAsJsonObject().get("addressFreeFormat").getAsJsonArray()
										.size() > 0) {
									JsonArray addressfreeformat = customerInfo.getAsJsonObject()
											.get("addressFreeFormat").getAsJsonArray();
									if (!addressfreeformat.isEmpty() && addressfreeformat.size() > 0) {
										addressLine2 = (JsonObject) addressfreeformat.get(0);
									}
									if (!addressLine2.isJsonNull() && addressLine2.size() > 0)
										address = addressLine2.get("addressLine").getAsString();
									}
								}
								jsonObject.addProperty("addressLine2", address);
							}
					}
				}
				recordsArray.add(jsonObject);
			}
			
		}
		processedResult.add("records", recordsArray);

		return processedResult;
	}

	@Override
	public DBXResult getCoreRelativeCustomers(MembershipDTO membershipDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		DBXResult responseDTO = new DBXResult();
		Map<String, Object> procParams = new HashMap<>();
		procParams.put("_id", StringUtils.isNotBlank(membershipDTO.getId()) ? membershipDTO.getId() : "");
		procParams.put("_legalEntityId",
				StringUtils.isNotBlank(membershipDTO.getCompanyLegalUnit()) ? membershipDTO.getCompanyLegalUnit() : "");
		JsonObject response = new JsonObject();
		try {
			response = getCoreRelativeCustomerParty(membershipDTO, headersMap);
			if (JSONUtil.hasKey(response, "records")) {
				if (response.get("records").getAsJsonArray().size() > 0) {
					responseDTO.setResponse(response.get("records").getAsJsonArray());
				}
			} else {
				alert.prepareError("CoreCustomerBackendDelegateImpl : Backend response is not appropriate " + response).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_10759);
			}
		} catch (Exception e) {
			alert.prepareError("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customers"
					+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10759);
		}
		return responseDTO;
	}

	private JsonObject getCoreRelativeCustomerParty(MembershipDTO membershipDTO, Map<String, Object> headersMap) {
		JsonObject response = new JsonObject();
		JsonArray records = new JsonArray();
		String id = "";
		JsonArray customerDetails = new JsonArray();
		addBackendHeaders(headersMap, membershipDTO.getId(), membershipDTO.getCompanyLegalUnit());
		// Map<String, Object> inputParams = new HashMap<>();
		alert.prepareError("id" + id).log();
		String partyId = getPartyId(membershipDTO.getId(), membershipDTO.getCompanyLegalUnit(), headersMap);
		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) + PARTY_GET4 + partyId
				+ PARTY_RELATIONSHIP;

		
		DBXResult result = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headersMap);

		JsonObject jsonObject1 = null;
		try {		
		jsonObject1 = new JsonParser().parse((String) result.getResponse()).getAsJsonObject();
		}
		catch (Exception e) {
			diagnostic.prepareDebug("Error in parsing party Response"+ e).log();
		}

		if (jsonObject1 !=null && !jsonObject1.isJsonNull() && jsonObject1.get("partyRelations").getAsJsonArray().size() > 0) {
			for (JsonElement jsonelement : jsonObject1.get("partyRelations").getAsJsonArray()) {
				JsonObject jsonObject = new JsonObject();
				if (StringUtils.isNotBlank(JSONUtil.getString(jsonelement.getAsJsonObject(), "relatedParty"))) {
					String RelationpartyId = JSONUtil.getString(jsonelement.getAsJsonObject(), "relatedParty");
					String partyURL2 = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) + PARTY_GET5
							+ RelationpartyId;
					DBXResult dbxResult1 = new DBXResult();
					dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL2, null,
							headersMap);
					JsonObject customerJson = new JsonParser().parse((String) dbxResult1.getResponse())
							.getAsJsonObject();
					String partyType = JSONUtil.hasKey(customerJson,"partyType") ?
							JSONUtil.getString(customerJson,"partyType") :
							null;
					if(!StringUtils.equalsIgnoreCase(partyType,"Individual")) continue;
					
					jsonObject.addProperty("firstName", JSONUtil.getString(customerJson, "firstName"));
					jsonObject.addProperty("name",
							(customerJson.has("firstName") && !customerJson.get("firstName").isJsonNull()
									&& StringUtils.isNotBlank(customerJson.get("firstName").getAsString())
											? customerJson.get("firstName").getAsString()
											: "")
									+ " "
									+ (customerJson.has("lastName") && !customerJson.get("lastName").isJsonNull()
											&& StringUtils.isNotBlank(customerJson.get("lastName").getAsString())
													? customerJson.get("lastName").getAsString()
													: ""));
					jsonObject.addProperty("lastName", JSONUtil.getString(customerJson, "lastName"));
					jsonObject.addProperty("dateOfBirth", JSONUtil.getString(customerJson, "dateOfBirth"));
					jsonObject.addProperty("gender", JSONUtil.getString(customerJson, "gender"));
					jsonObject.addProperty("maritalStatus", JSONUtil.getString(customerJson, "maritalStatus"));

					String taxId1 = "";
					String type = "";
					if (JSONUtil.hasKey(customerJson, DTOConstants.PARTYIDENTIFIERS)
							&& customerJson.get(DTOConstants.PARTYIDENTIFIERS).isJsonArray()
							&& customerJson.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray().size() > 0) {
						customerDetails = customerJson.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray();
						for (JsonElement taxids : customerDetails) {
							type = JSONUtil.getString(taxids.getAsJsonObject(), "type");
							if (type.equalsIgnoreCase("SOCIAL.SECURITY.NO")) {
								taxId1 = JSONUtil.getString(taxids.getAsJsonObject(), "identifierNumber");
								jsonObject.addProperty("taxId", taxId1);
							}
						}
					}

					String employmenttype = "";
					if (JSONUtil.hasKey(customerJson, DTOConstants.EMPLOYMENTS)
							&& customerJson.get(DTOConstants.EMPLOYMENTS).isJsonArray()
							&& customerJson.get(DTOConstants.EMPLOYMENTS).getAsJsonArray().size() > 0) {
						customerDetails = customerJson.get(DTOConstants.EMPLOYMENTS).getAsJsonArray();
						for (JsonElement employments : customerDetails) {
							employmenttype = JSONUtil.getString(employments.getAsJsonObject(), "type");
							jsonObject.addProperty("employmentStatus", employmenttype);
						}
					}

					if (JSONUtil.hasKey(customerJson, DTOConstants.ALTERNATEIDENTITIES)
							&& customerJson.get(DTOConstants.ALTERNATEIDENTITIES).isJsonArray()
							&& customerJson.get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray().size() > 0) {
						customerDetails = customerJson.get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray();
						for (JsonElement customerInfos : customerDetails) {
							String identityNumber = JSONUtil.getString(customerInfos.getAsJsonObject(),
									"identityNumber");
							String[] identityNumberArray = identityNumber.split("-");
							if (identityNumberArray != null && identityNumberArray.length == 2) {
								id = identityNumberArray[1];
								jsonObject.addProperty("id", id);

							}
						}
					}
					String address = "";
					if (JSONUtil.hasKey(customerJson, DTOConstants.PARTYADDRESS)
							&& customerJson.get(DTOConstants.PARTYADDRESS).isJsonArray()
							&& customerJson.get(DTOConstants.PARTYADDRESS).getAsJsonArray().size() > 0) {
						customerDetails = customerJson.get(DTOConstants.PARTYADDRESS).getAsJsonArray();
						for (JsonElement customerInfo : customerDetails) {

							String electronicAddress = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"electronicAddress");
							String phonePrefix = JSONUtil.getString(customerInfo.getAsJsonObject(), "iddPrefixPhone");
							String phoneNo = JSONUtil.getString(customerInfo.getAsJsonObject(), "phoneNo");
							String streetName = JSONUtil.getString(customerInfo.getAsJsonObject(), "streetName");
							String town = JSONUtil.getString(customerInfo.getAsJsonObject(), "town");
							String postalOrZipCode = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"postalOrZipCode");
							String country = JSONUtil.getString(customerInfo.getAsJsonObject(), "countryCode");
							String countrySubDivision = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"countrySubdivision");
							String FlatNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "flatNumber");
							String BuildingNumber = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"buildingNumber");
							String BuildingName = JSONUtil.getString(customerInfo.getAsJsonObject(), "buildingName");
							String Floor = JSONUtil.getString(customerInfo.getAsJsonObject(), "floor");
							String postBoxNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "postBoxNumber");

							if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
									&& "Electronic".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(),
											"communicationNature"))) {
								jsonObject.addProperty("email", electronicAddress);
							}
							if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
									&& "Phone".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(),
											"communicationNature"))) {
								jsonObject.addProperty("phone", phonePrefix + "" + phoneNo);
							}
							if ("Physical".equalsIgnoreCase(
									JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))
									&& "Home".equalsIgnoreCase(
											JSONUtil.getString(customerInfo.getAsJsonObject(), "addressType"))) {
								jsonObject.addProperty("addressLine1", streetName);
								jsonObject.addProperty("addressLine2",
										FlatNumber + BuildingNumber + BuildingName + Floor + postBoxNumber);
								jsonObject.addProperty("cityName", town);
								jsonObject.addProperty("zipCode", postalOrZipCode);
								jsonObject.addProperty("country", country);
								jsonObject.addProperty("state", countrySubDivision);
								if ((FlatNumber == "") && (BuildingNumber == "") && (BuildingName == "")
										&& (Floor == "") && (postBoxNumber == "")) {
									if (customerInfo.getAsJsonObject().get("addressFreeFormat").getAsJsonArray()
											.size() > 0) {
										JsonArray addressfreeformat = customerInfo.getAsJsonObject()
												.get("addressFreeFormat").getAsJsonArray();
										address = addressfreeformat.toString();
									}
									jsonObject.addProperty("addressLine2", address);
								}
							}
						}
					}
				}

				jsonObject.addProperty("relationshipId", id);
				jsonObject.addProperty("relationshipName",
						JSONUtil.getString(jsonelement.getAsJsonObject(), "relationshipName"));
				alert.prepareError("json" + jsonObject).log();
				records.add(jsonObject);
			}
		}
		alert.prepareError("rec" + records).log();
		response.add(DBPDatasetConstants.DATASET_RECORDS, records);
		return response;
	}

	@Override
	public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, String legalEntityId,
			Map<String, Object> headerMap) throws ApplicationException {
		MembershipDTO dto = new MembershipDTO();
		try {
			if (StringUtils.isBlank(taxID) || StringUtils.isBlank(companyName)) {
				return null;
			}
			String queryParams = "";
			String id = "";

			HelperMethods.addMSJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

			if (StringUtils.isNotBlank(taxID)) {
				queryParams += "identifierNumber=" + taxID;
				queryParams += "&";
				queryParams += "identifierType=" + DTOConstants.SOCIAL_SECURITY_NUMBER;
				queryParams = queryParams.replace(" ", "%20");
			}
			String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) + PARTY_SEARCH5
					+ queryParams;

			DBXResult dbxResult1 = new DBXResult();
			dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
			JsonArray partyJsonArray = new JsonArray();
			if (dbxResult1.getResponse() != null) {
				JsonElement partyResponse = new JsonParser().parse((String) dbxResult1.getResponse());
				if (partyResponse.isJsonObject()) {
					JsonObject partyResponseObject = partyResponse.getAsJsonObject();
					if (partyResponseObject.has("parties") && partyResponseObject.get("parties").isJsonArray()
							&& partyResponseObject.get("parties").getAsJsonArray().size() > 0) {
						partyJsonArray = partyResponseObject.get("parties").getAsJsonArray();
					}
				}
			}

			if (partyJsonArray.size() > 0) {
				JsonArray customerDetails = new JsonArray();
				for (JsonElement jsonelement : partyJsonArray) {
					if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.ALTERNATEIDENTITIES)
							&& jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).isJsonArray()
							&& jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray()
									.size() > 0) {
						customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES)
								.getAsJsonArray();
						for (JsonElement customerInfos : customerDetails) {
							String identityNumber = JSONUtil.getString(customerInfos.getAsJsonObject(),
									"identityNumber");
							String[] identityNumberArray = identityNumber.split("-");
							id = identityNumberArray[1];
						}
					}

				}
				dto.setId(id);

			}

		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10238);
		}
		return dto;
	}

	private String getPartyId(String id, String legalEntityId, Map<String, Object> headersMap) {

		String queryParams = "";
		id = legalEntityId + "-" + id;
		queryParams += "alternateIdentifierNumber=" + id;
		queryParams += "&";
		queryParams += "alternateIdentifierType=BackOfficeIdentifier";

		String partyId = "";
		headersMap = HelperMethods.addJWTAuthHeaderParty(headersMap, AuthConstants.PRE_LOGIN_FLOW);
		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) + PARTY_SEARCH5 + queryParams;
		DBXResult dbxResult1 = new DBXResult();
		dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headersMap);
		JsonArray partyJsonArray = new JsonArray();
		if (dbxResult1.getResponse() != null) {
			JsonElement partyResponse = new JsonParser().parse((String) dbxResult1.getResponse());
			if (partyResponse.isJsonObject()) {
				JsonObject partyResponseObject = partyResponse.getAsJsonObject();
				if (partyResponseObject.has("parties") && partyResponseObject.get("parties").isJsonArray()
						&& partyResponseObject.get("parties").getAsJsonArray().size() > 0) {
					partyJsonArray = partyResponseObject.get("parties").getAsJsonArray();
				}
			}
		}
		if (partyJsonArray.size() > 0) {
			for (JsonElement jsonelement : partyJsonArray) {
				partyId = JSONUtil.getString(jsonelement.getAsJsonObject(), "partyId");
			}

		}
		return partyId;
	}

	@Override
	public DBXResult searchCoreCustomers(List<MembershipDTO> membershipDTOs, Map<String, Object> headersMap)
			throws ApplicationException {

		DBXResult result = new DBXResult();
		result.setResponse(new JsonArray());
		return searchCoreCustomersPartyHelper(membershipDTOs, headersMap);

	}

	private DBXResult searchCoreCustomersPartyHelper(List<MembershipDTO> membershipDTOs, Map<String, Object> headersMap) throws ApplicationException
	{
		DBXResult customersResult = new DBXResult();
		JsonArray customersResultArray = new JsonArray();
		for (MembershipDTO membershipDTO : membershipDTOs) {
			DBXResult customerResult = new DBXResult();
			customerResult = searchCoreCustomers(membershipDTO, headersMap);
			JsonArray customerArray = (JsonArray) customerResult.getResponse();
			customersResultArray.addAll(customerArray);
		}
		customersResult.setResponse(customersResultArray);

		return customersResult;
	}

	@Override
	public DBXResult getCoreCustomerAccounts(MembershipDTO membershipDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		CoreCustomerBackendDelegateImpl corecustomer = new CoreCustomerBackendDelegateImpl();
		return corecustomer.getCoreCustomerAccounts(membershipDTO, headersMap);
	}

	@Override
	public MembershipDTO getMembershipDetails(String membershipId, String legalEntityId, Map<String, Object> headerMap)
			throws ApplicationException {
		CoreCustomerBackendDelegateImpl corecustomer = new CoreCustomerBackendDelegateImpl();
		return corecustomer.getMembershipDetails(membershipId, legalEntityId, headerMap);

	}

	@Override
	public List<AllAccountsViewDTO> getCoreCustomerAccounts(List<MembershipDTO> membershipList,
			Map<String, Object> headersMap) throws ApplicationException {
		CoreCustomerBackendDelegateImpl corecustomer = new CoreCustomerBackendDelegateImpl();
		return corecustomer.getCoreCustomerAccounts(membershipList, headersMap);
	}
}
