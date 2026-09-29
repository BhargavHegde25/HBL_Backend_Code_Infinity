package com.temenos.dbx.usermanagement.resource.impl;

import java.util.Map;

import com.kony.dbputilities.util.LegalEntityUtil;
import org.apache.commons.lang3.StringUtils;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.dto.PartySearchDTO;
import com.temenos.dbx.usermanagement.resource.api.PartyUserManagementResource;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class PartyUserManagementResourceImpl implements PartyUserManagementResource {
    
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result partyGet(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        JsonObject party = new JsonObject();
        PartyDTO partyDTO = new PartyDTO();

        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);

        String partyIP = URLFinder.getPathUrl(URLConstants.PARTY_HOST_URL, dcRequest);
        
        String integration_name = EnvironmentConfigurationsHandler.getServerProperty(URLConstants.INTEGRATION_NAME);

		// Party Integration
       if (StringUtils.equalsIgnoreCase(integration_name, "party")) {
        String companyId = inputMap.get("companyId");
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        if (StringUtils.isNotBlank(partyIP)) {
            Map<String, Object> headers = dcRequest.getHeaderMap();
            headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
            headers.put("companyId", companyId);
            String partyId = inputMap.get("partyId");
            String id = inputMap.get("id");
            PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(PartyUserManagementBusinessDelegate.class);
            DBXResult response = new DBXResult();
            JsonArray partyJsonArray = new JsonArray();
            if (StringUtils.isNotBlank(partyId)) {
                BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
                BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
                backendIdentifierDTO.setCustomer_id(id);
                backendIdentifierDTO
                        .setBackendType(PartyConstants.PARTY);

                DBXResult dbxResult = backendDelegateimpl.get(backendIdentifierDTO, dcRequest.getHeaderMap());
                if (dbxResult.getResponse() != null) {
                    backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                    partyId = backendIdentifierDTO.getBackendId();
                }
            }
            if (StringUtils.isNotBlank(partyId)) {
                partyDTO.setPartyId(inputMap.get("partyId"));
                response = managementBusinessDelegate.get(partyDTO, headers);
                if (response.getResponse() != null) {
                    JsonObject jsonObject = (JsonObject) response.getResponse();
                    partyJsonArray.add(jsonObject);
                    party.add(PartyConstants.parties, partyJsonArray);
                }
            } else {
                PartySearchDTO searchDTO = buildSearchPartyDTO(inputMap);
                response = managementBusinessDelegate.searchParty(searchDTO, headers);
                if (response.getResponse() != null) {
                    party = (JsonObject) response.getResponse();
                    partyJsonArray = party.has(PartyConstants.parties) && party.get(PartyConstants.parties).isJsonArray() ? party.get(PartyConstants.parties).getAsJsonArray() : new JsonArray(); 
                }
            }
            
            if (partyJsonArray.size() > 0) {
                diagnostic.prepareDebug("party respose " + partyJsonArray.toString()).log();
                formatPartyResponse(partyJsonArray, dcRequest.getHeaderMap());
               return JSONToResult.convert(party.toString());
            }

            result.addParam("dbpErrCode", response.getDbpErrCode());
            result.addParam("dbpErrMsg", response.getDbpErrMsg());
            return result;
        } else {
            result.addParam("dbpErrMsg", "PartyHostURL not found");
        }
       }
       
      // T24 Integration
      else if (StringUtils.equalsIgnoreCase(integration_name, "t24")) {
    	   
   		String coreCustomerId = "";
   		if(inputMap.containsKey("coreCustomerID")) {
   			coreCustomerId = inputMap.get("coreCustomerID");
   		} else if (inputMap.containsKey("partyId")){
   			//core Id is sent in partyId key
   			coreCustomerId = inputMap.get("partyId");
   		}
   		
   		if(StringUtils.isNotBlank(coreCustomerId)){
   		  PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
   		  		.getFactoryInstance(BusinessDelegateFactory.class)
   		  		.getBusinessDelegate(PartyUserManagementBusinessDelegate.class);
   		  String responseStr = managementBusinessDelegate.getT24Customer(coreCustomerId, dcRequest);

   		try {
   			JsonArray partyJsonArray = new JsonArray();
   			JsonObject partyJsonObject = new JsonObject();
   			JsonParser parser = new JsonParser();
   			JsonObject T24Data = (JsonObject) parser.parse(responseStr).getAsJsonObject().get("body")
   					.getAsJsonObject();
   			JsonObject T24Header = (JsonObject) parser.parse(responseStr).getAsJsonObject().get("header")
   					.getAsJsonObject();
   			
   			if (T24Header.has("id")) {
   				if (!T24Data.toString().equals("{}")) {
   					JsonObject T24Info = new JsonObject();
   					JsonArray EmploymentJsonArray = new JsonArray();
   					JsonObject EmploymentObj = new JsonObject();
   					JsonArray occupationsJsonArray = new JsonArray();
   					JsonObject occupationsObj = new JsonObject();
   					String str = "";
   					
   					//Employment and Occupations
   					if (T24Data.has("employDetails")) {
   						JsonArray employmentArray = T24Data.get("employDetails").getAsJsonArray();
   						for (int i = 0; i < employmentArray.size(); i++) {
   							JsonObject EmploymentArrayObj = employmentArray.get(i).getAsJsonObject();

   							str = EmploymentArrayObj.has("occupation")
   									? EmploymentArrayObj.get("occupation").getAsString() : "";
   							occupationsObj.addProperty("occupationType", str);
   							
   							EmploymentObj.addProperty("PartyId", T24Header.get("id").getAsString());

   							str = EmploymentArrayObj.has("employerName")
   									? EmploymentArrayObj.get("employerName").getAsString() : "";
   							EmploymentObj.addProperty("employerName", str);

   							str = EmploymentArrayObj.has("employStatus")
   									? EmploymentArrayObj.get("employStatus").getAsString() : "";
   							
   						    //get T24-PartyMS mapping for Employment Status
   							if (StringUtils.isNotBlank(str)) {
   							  String employmentStatusMappingStr = TemenosUtils.readContentFromFile("EmploymentStatusReverseMapping.json");
   							  JsonObject employmentStatusMapping = new JsonParser().parse(employmentStatusMappingStr).getAsJsonObject();
   							  EmploymentObj.addProperty("type", employmentStatusMapping.get(str).getAsString());
   							} else {
   								EmploymentObj.addProperty("type", "");
   							}

   							str = EmploymentArrayObj.has("employStartDate")
   									? EmploymentArrayObj.get("employStartDate").getAsString() : "";
   							EmploymentObj.addProperty("startDate", str);

   							if (EmploymentArrayObj.has("address")) {
   								String address = "";
   								JsonArray addressArray = EmploymentArrayObj.get("address").getAsJsonArray();
   								for (int j = 0; j < addressArray.size(); j++) {
   									if (j == 0) {
   										address = "buildingName";
   										str = addressArray.get(j).getAsJsonObject().has("address")
   												? addressArray.get(j).getAsJsonObject().get("address").getAsString() : "";
   										EmploymentObj.addProperty("streetName", str);
   									}
   									if (j == 1)
   										address = "town";
   									if (j == 2)
   										address = "countrySubdivision";
   									if (j == 3)
   										address = "country";
   									if (j == 4)
   										address = "postalOrZipCode";
   									str = addressArray.get(j).getAsJsonObject().has("address")
   											? addressArray.get(j).getAsJsonObject().get("address").getAsString()
   											: "";
   									EmploymentObj.addProperty(address, str);
   								}
   								EmploymentObj.addProperty("addressType", "Office");
   							}
   						}
   					}

   					if (T24Data.has("contactTypes")) {
   						JsonArray addressesArray = T24Data.get("contactTypes").getAsJsonArray();
   						for (int i = 0; i < addressesArray.size(); i++) {
   							JsonObject AddressObj = addressesArray.get(i).getAsJsonObject();

   							if (AddressObj.get("contactType") != null
   									&& AddressObj.get("contactType").getAsString().equals("OFFICE")) {

   								str = AddressObj.has("contactData") ? AddressObj.get("contactData").getAsString() : "";
   								EmploymentObj.addProperty("employerOfficePhone", str);

   								str = AddressObj.has("countryCode") ? AddressObj.get("countryCode").getAsString() : "";
   								EmploymentObj.addProperty("employerOfficePhoneIdd", str);
   							}
   						}
   					} else {
   						EmploymentObj.addProperty("employerOfficePhone", "");
   						EmploymentObj.addProperty("employerOfficePhoneIdd", "");
   					}

   					EmploymentJsonArray.add(EmploymentObj);
   					occupationsJsonArray.add(occupationsObj);
   					partyJsonObject.add("employments", EmploymentJsonArray);
   					partyJsonObject.add("occupations", occupationsJsonArray);
   					
   					
   					//COMPANY INFO
   					
   					partyJsonObject.addProperty("partyId", T24Header.get("id").getAsString());
   					//entityName/CustomerName
   					JsonArray customerNamesJSONArr = T24Data.has("customerNames") ? T24Data.get("customerNames").getAsJsonArray() : null;
   					JsonObject customerNamesObj = customerNamesJSONArr!=null ? customerNamesJSONArr.get(0).getAsJsonObject() : new JsonObject();
   					String customerName = customerNamesObj.has("customerName") ? customerNamesObj.get("customerName").getAsString() : "";
   					partyJsonObject.addProperty("entityName", customerName);
   					
   				    //organisationLegalType/customerNameAdditional
   					String customerNameAdditional = customerNamesObj.has("customerNameAdditional") ? customerNamesObj.get("customerNameAdditional").getAsString() : "";
   					partyJsonObject.addProperty("organisationLegalType", customerNameAdditional);
   					
   					//incorporationCountry/country
   					JsonArray countriesJSONArr = T24Data.has("countries") ? T24Data.get("countries").getAsJsonArray() : null;
   					JsonObject countriesObj = countriesJSONArr!=null ? countriesJSONArr.get(0).getAsJsonObject() : new JsonObject();
   					String countryOfInc = countriesObj.has("country") ? countriesObj.get("country").getAsString() : "";
   					partyJsonObject.addProperty("incorporationCountry", countryOfInc);
   					
   					//classifications.name / industryId
   					JsonArray classificationsJsonArray = new JsonArray();
   					JsonObject classificationsJsonObj =  new JsonObject();
   					String industryMappedValue = "";
   					
   					String industryT24Id = T24Data.has("industryId") ? T24Data.get("industryId").getAsString() : "";
						if (StringUtils.isNotBlank(industryT24Id)) {
						  String industryMappingStr = TemenosUtils.readContentFromFile("IndustryReverseMapping.json");
						  JsonObject industryMapping = new JsonParser().parse(industryMappingStr).getAsJsonObject();
						  industryMappedValue = industryMapping.has(industryT24Id) ? 
								                    industryMapping.get(industryT24Id).getAsString() : "";
						} else {
							industryMappedValue = "";
						}
						classificationsJsonObj.addProperty("name", industryMappedValue);
						classificationsJsonObj.addProperty("code", industryT24Id);
						classificationsJsonObj.addProperty("partyId", T24Header.get("id").getAsString());
						classificationsJsonArray.add(classificationsJsonObj);
						partyJsonObject.add("classifications", classificationsJsonArray);
   					
					//PARTY IDENTIFIERS / REGN DETAILS
					JsonArray partyIdentifiersJsonArray = new JsonArray();
	   				JsonObject partyIdentifiersJsonObj = new JsonObject();
	   				
	   				JsonArray legalDetailsJSONArr = T24Data.has("legalDetails") ? T24Data.get("legalDetails").getAsJsonArray() : null;
	   				JsonObject legalDetailsObj = legalDetailsJSONArr!=null ? legalDetailsJSONArr.get(0).getAsJsonObject() : new JsonObject();
	   				
	   			    //partyIdentifiers.type / legalDocumentName
	   				String legalDocumentName = legalDetailsObj.has("legalDocumentName") ? legalDetailsObj.get("legalDocumentName").getAsString() : "";
	   				
	   			    //partyIdentifiers.identifierNumber / legalId
	   				String legalId = legalDetailsObj.has("legalId") ? legalDetailsObj.get("legalId").getAsString() : "";
	   				String legalIssueDate = legalDetailsObj.has("legalIssueDate") ? legalDetailsObj.get("legalIssueDate").getAsString() : "";
	   				
	   			    //partyIdentifiers.expiryDate / legalExpiredDate
	   				String legalExpiredDate = legalDetailsObj.has("legalExpiredDate") ? legalDetailsObj.get("legalExpiredDate").getAsString() : "";
   					
	   				partyIdentifiersJsonObj.addProperty("type", legalDocumentName);
	   				partyIdentifiersJsonObj.addProperty("identifierNumber", legalId);
	   				partyIdentifiersJsonObj.addProperty("issuedDate", legalIssueDate);
	   				partyIdentifiersJsonObj.addProperty("expiryDate", legalExpiredDate);
					partyIdentifiersJsonArray.add(partyIdentifiersJsonObj);
					partyJsonObject.add("partyIdentifiers", partyIdentifiersJsonArray);
					
					//Date of Incorporation/ legalIssueDate
					partyJsonObject.addProperty("dateOfIncorporation", legalIssueDate);
					
					//taxDetails.taxId / taxId
					JsonArray taxIdsJSONArr = T24Data.has("taxIds") ? T24Data.get("taxIds").getAsJsonArray() : null;
   					JsonObject taxIdsObj = taxIdsJSONArr!=null ? taxIdsJSONArr.get(0).getAsJsonObject() : new JsonObject();
   					String taxId = taxIdsObj.has("taxId") ? taxIdsObj.get("taxId").getAsString() : "";

	   				JsonArray taxDetailsJsonArray = new JsonArray();
					JsonObject taxDetailsJsonObj = new JsonObject();
					taxDetailsJsonObj.addProperty("taxId", taxId);
					taxDetailsJsonObj.addProperty("partyId", T24Header.get("id").getAsString());
					taxDetailsJsonArray.add(taxDetailsJsonObj);
					partyJsonObject.add("taxDetails", taxDetailsJsonArray);
					
					//CONTACT ADDRESS
					//contactAddress.buildingName / street
					JsonArray streetsJSONArr = T24Data.has("streets") ? T24Data.get("streets").getAsJsonArray() : null;
   					JsonObject streetsObj = streetsJSONArr!=null ? streetsJSONArr.get(0).getAsJsonObject() : new JsonObject();
   					String street = streetsObj.has("street") ? streetsObj.get("street").getAsString() : "";
   					
					//contactAddress.town / addressCity
   					JsonArray addressCitiesJSONArr = T24Data.has("addressCities") ? T24Data.get("addressCities").getAsJsonArray() : null;
   					JsonObject addressCitiesObj = addressCitiesJSONArr!=null ? addressCitiesJSONArr.get(0).getAsJsonObject() : new JsonObject();
   					String addressCity = addressCitiesObj.has("addressCity") ? addressCitiesObj.get("addressCity").getAsString() : "";
   					
					//contactAddress.countrySubdivision / state
   					String state = T24Data.has("state") ? T24Data.get("state").getAsString() : "";
   					
					//contactAddress.countryCode / country
   					JsonArray countryJSONArr = T24Data.has("countries") ? T24Data.get("countries").getAsJsonArray() : null;
   					JsonObject countryObj = countryJSONArr!=null ? countryJSONArr.get(0).getAsJsonObject() : new JsonObject();
   					String country = countryObj.has("country") ? countryObj.get("country").getAsString() : "";
   					
					//contactAddress.postalOrZipCode / postCode
   					String postCode = T24Data.has("postCode") ? T24Data.get("postCode").getAsString() : "";

	   				JsonArray contactAddressJsonArray = new JsonArray();
					JsonObject contactAddressJsonObj = new JsonObject();
					contactAddressJsonObj.addProperty("buildingName", street);
					contactAddressJsonObj.addProperty("town", addressCity);
					contactAddressJsonObj.addProperty("countrySubdivision", state);
					contactAddressJsonObj.addProperty("countryCode", country);
					contactAddressJsonObj.addProperty("postalOrZipCode", postCode);
					contactAddressJsonObj.addProperty("communicationNature", "Physical");
					contactAddressJsonObj.addProperty("communicationType", "MailingAddress");
					contactAddressJsonObj.addProperty("addressType", "Office");
					contactAddressJsonObj.addProperty("primary", "true");
					contactAddressJsonArray.add(contactAddressJsonObj);
					partyJsonObject.add("contactAddress", contactAddressJsonArray);
					
					//Contact Phone Number and Country Code
					JsonArray contactTypesJSONArr = T24Data.has("contactTypes") ? T24Data.get("contactTypes").getAsJsonArray() : null;
   					JsonObject contactType = contactTypesJSONArr!=null ? contactTypesJSONArr.get(0).getAsJsonObject() : new JsonObject();
   					String countryCode = contactType.has("countryCode") ? contactType.get("countryCode").getAsString() : "";
   					String contactData = contactType.has("contactData") ? contactType.get("contactData").getAsString() : "";
					
   					JsonArray phoneAddressJsonArray = new JsonArray();
					JsonObject phoneAddressJsonObj = new JsonObject();
					phoneAddressJsonObj.addProperty("iddPrefixPhone", countryCode);
					phoneAddressJsonObj.addProperty("phoneNo", contactData);
					phoneAddressJsonObj.addProperty("communicationNature", "Phone");
					phoneAddressJsonObj.addProperty("communicationType", "Mobile");
					phoneAddressJsonObj.addProperty("addressType", "Office");
					phoneAddressJsonObj.addProperty("primary", "true");
					phoneAddressJsonArray.add(phoneAddressJsonObj);
					partyJsonObject.add("phoneAddress", phoneAddressJsonArray);
   					
   					partyJsonArray.add(partyJsonObject);
   					party.add(PartyConstants.parties, partyJsonArray);

   					result = JSONToResult.convert(party.toString());
   				}

   			} else {
   				if (T24Data.has("message")) {
   					result.addStringParam("dbpErrMsg", "T24 Data fetching failed");
   				}
   				if (T24Data.has("status")) {
   					result.addStringParam("dbpErrMsg", "T24 Data fetching failed");
   				}
   			}
   		} catch (Exception e) {
   			alert.prepareError("Error while getting Customer from Transact: ", e).log();
   			result = JSONToResult.convert(responseStr.toString());
   			result.addOpstatusParam(0);
   			result.addHttpStatusCodeParam(200);
   		}
   	  } else {
   		    result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			result.addParam("dbpErrMsg", "PartyId/CustomerId is missing from payload");
   	  }
    }

        return result;
    }

    private PartySearchDTO buildSearchPartyDTO(Map<String, String> inputMap) {
        PartySearchDTO partySearchDTO = new PartySearchDTO();
        DTOUtils.loadInputIntoDTO(partySearchDTO, inputMap, false);
        return partySearchDTO;
    }

    @Override
    public Result partyUpdate(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();

        PartyDTO partyDTO = new PartyDTO();

        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);

        partyDTO.setPartyId(inputMap.get("partyId"));

        if (StringUtils.isBlank(partyDTO.getPartyId())) {
            partyDTO.setPartyId(inputMap.get("id"));
        }

        PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(PartyUserManagementBusinessDelegate.class);

        String partyIP = URLFinder.getPathUrl(URLConstants.PARTY_HOST_URL, dcRequest);
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        if (StringUtils.isNotBlank(partyIP)) {
            Map<String, Object> headers = dcRequest.getHeaderMap();
            headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);

            DBXResult response = managementBusinessDelegate.get(partyDTO, headers);

            if (response.getResponse() == null) {
                result.addParam("dbpErrCode", response.getDbpErrCode());
                result.addParam("dbpErrMsg", response.getDbpErrMsg());
                return result;
            }

            JsonObject jsonObject = (JsonObject) response.getResponse();

            partyDTO = PartyUtils.buildPartyfromInput(inputMap, jsonObject);

            if(StringUtils.isBlank(partyDTO.getFirstName()) && StringUtils.isBlank(partyDTO.getLastName())) {
                result.addParam("dbpErrMsg", "Invalid InputParams, Mandatory params firstName or lastName are empty");
                result.addParam("createStatus", "fail");
                return result;
            }
            
            response = managementBusinessDelegate.update(partyDTO, headers);

            if (response.getResponse() != null) {
                String id = (String) response.getResponse();
                result.addParam("updateStatus", "success");
                return result;
            }

            result.addParam("dbpErrCode", response.getDbpErrCode());
            result.addParam("dbpErrMsg", response.getDbpErrMsg());
            result.addParam("updateStatus", "fail");
            return result;
        } else {
            result.addParam("dbpErrMsg", "PartyHostURL not found");
        }

        return result;
    }

    @Override
    public Result partyCreate(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) 
    {
        Result result = new Result();

        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);

        PartyDTO partyDTO = new PartyDTO();

        partyDTO = PartyUtils.buildPartyfromInput(inputMap, null);

        if(StringUtils.isBlank(partyDTO.getFirstName()) && StringUtils.isBlank(partyDTO.getLastName())) {
            result.addParam("dbpErrMsg", "Invalid InputParams, Mandatory params firstName or lastName are empty");
            result.addParam("createStatus", "fail");
            return result;
        }

        PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(PartyUserManagementBusinessDelegate.class);

        String partyIP = URLFinder.getPathUrl(URLConstants.PARTY_HOST_URL, dcRequest);
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        if (StringUtils.isNotBlank(partyIP)) {
            Map<String, Object> headers = dcRequest.getHeaderMap();
            headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
            DBXResult response = managementBusinessDelegate.create(partyDTO, headers);

            if (response.getResponse() != null) {
                String id = (String) response.getResponse();
                result.addParam("createStatus", "success");
                result.addParam("partyId", id);
                return result;
            }

            result.addParam("dbpErrCode", response.getDbpErrCode());
            result.addParam("dbpErrMsg", response.getDbpErrMsg());
            result.addParam("createStatus", "fail");

            return result;

        } else {
            result.addParam("dbpErrMsg", "PartyHostURL not found");
        }

        result.addParam("createStatus", "fail");

        return result;

    }

    private void formatPartyResponse(JsonArray partyJsonArray, Map<String, Object> headerMap) {
        for (JsonElement element : partyJsonArray) {
            JsonObject party = element.getAsJsonObject();
            JsonArray contactAddress = new JsonArray();
            JsonArray phoneAddress = new JsonArray();
            JsonArray electronicAddress = new JsonArray();
            if (party.has(PartyConstants.addresses) && party.get(PartyConstants.addresses).isJsonArray()) {
                for (JsonElement element1 : party.get(PartyConstants.addresses).getAsJsonArray()) {
                    JsonObject jsonObject = element1.getAsJsonObject();
                    if (jsonObject.has(PartyConstants.communicationNature)
                            && !jsonObject.get(PartyConstants.communicationNature).isJsonNull()) {
                        if (PartyConstants.Physical
                                .equals(jsonObject.get(PartyConstants.communicationNature).getAsString())) {
                            contactAddress.add(jsonObject);
                        } else if (PartyConstants.Phone
                                .equals(jsonObject.get(PartyConstants.communicationNature).getAsString())) {
                            phoneAddress.add(jsonObject);
                        } else if (PartyConstants.Electronic
                                .equals(jsonObject.get(PartyConstants.communicationNature).getAsString())) {
                            electronicAddress.add(jsonObject);
                        }
                    }
                }
            }

            party.add(PartyConstants.contactAddress, contactAddress);
            party.add(PartyConstants.phoneAddress, phoneAddress);
            party.add(PartyConstants.electronicAddress, electronicAddress);
            
            getBackOfficeIdentifier(party);
            getInfinityIdentifier(party, headerMap);
        }
    }

    private void getInfinityIdentifier(JsonObject party, Map<String, Object> headerMap) {
        // TODO Auto-generated method stub
        BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setBackendId(party.get(PartyConstants.partyId).getAsString());
        backendIdentifierDTO
                .setBackendType(PartyConstants.PARTY);

        DBXResult dbxResult = backendDelegateimpl.get(backendIdentifierDTO, headerMap);
        if (dbxResult.getResponse() != null) {
            backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
            String id = backendIdentifierDTO.getCustomer_id();
            party.addProperty(PartyConstants.id, id);
            CustomerDTO customerDTO = (CustomerDTO) new CustomerDTO().loadDTO(id);
            boolean isEnrolled = false;
            if(customerDTO != null) {
                isEnrolled =  customerDTO.getIsEnrolled();
            }
            party.addProperty(PartyConstants.isEnrolled, isEnrolled);
        }
    }

    private void getBackOfficeIdentifier(JsonObject party) {
        if (party.has(PartyConstants.alternateIdentities) && party.get(PartyConstants.alternateIdentities).isJsonArray()) {
            for(JsonElement element : party.get(PartyConstants.alternateIdentities).getAsJsonArray()) {
                JsonObject alternateIdentity = element.getAsJsonObject();
                if(alternateIdentity.has(PartyConstants.identityType)&&!alternateIdentity.get(PartyConstants.identityType).isJsonNull()) {
                    String identitySource = alternateIdentity.get(PartyConstants.identitySource).getAsString();
                    String identityType =  alternateIdentity.get(PartyConstants.identityType).getAsString();
                    if(PartyConstants.BackOfficeIdentifier.equals(identityType) && (identitySource != null && PartyConstants.TransactT24.equals(identitySource))) {
                        party.add(PartyConstants.coreCustomerId, alternateIdentity.get(PartyConstants.identityNumber));
                        break;
                    }
                }
            }
        }
    }

	@Override
    public Result GetPartyData(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        JsonObject party = new JsonObject();
        PartyDTO partyDTO = new PartyDTO();
        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);

        String partyIP = URLFinder.getPathUrl(URLConstants.PARTY_HOST_URL, dcRequest);

        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        if (StringUtils.isNotBlank(partyIP)) {
         
            Map<String, Object> headers = dcRequest.getHeaderMap();
            
            headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
            String partyId = inputMap.get("partyID");
            PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(PartyUserManagementBusinessDelegate.class);
            DBXResult response = new DBXResult();
            if (StringUtils.isNotBlank(partyId)) {
                partyDTO.setPartyId(inputMap.get("partyID"));
                response = managementBusinessDelegate.GetPartyData(partyDTO, headers);
                if (response.getResponse() != null) {
                    JsonObject jsonObject = (JsonObject) response.getResponse();
                    diagnostic.prepareDebug("party respose " + jsonObject.toString()).log();
                    return JSONToResult.convert(jsonObject.toString());
                }
            }
            result.addParam("dbpErrCode", response.getDbpErrCode());
            result.addParam("dbpErrMsg", response.getDbpErrMsg());
            return result;
        } else {
            result.addParam("dbpErrMsg", "PartyHostURL not found");
        }

        return result;
    }
}
