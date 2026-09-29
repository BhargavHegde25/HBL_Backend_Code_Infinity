package com.temenos.dbx.usermanagement.backenddelegate.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.infinity.dbx.temenos.user.UserConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyUserManagementBackendDelegate;
import com.temenos.dbx.usermanagement.dto.PartySearchDTO;

public class PartyUserManagementBackendDelegateImpl implements PartyUserManagementBackendDelegate {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static LoggerUtil logger = new LoggerUtil(PartyUserManagementBackendDelegateImpl.class);

    @Override
    public DBXResult update(PartyDTO partyDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        String party = partyDTO.toStringJson().toString();

        diagnostic.prepareDebug("PartyDTO for update Party Service is : " + party).log();

        // DBXResult response = new DBXResult();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_UPDATE, partyDTO.getPartyId());
        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyURL, party, headerMap);
        String id = "";
        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();
            if (jsonObject.has(PartyConstants.id)) {
                id = jsonObject.get(PartyConstants.id).getAsString();
                dbxResult.setResponse(id);
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;

    }

    @Override
    public DBXResult get(PartyDTO partyDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        String party = partyDTO.toStringJson().toString();

        diagnostic.prepareDebug("PartyDTO for get Party Service is : " + party).log();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyDTO.getPartyId());
        if(!headerMap.containsKey("Authorization")) {
        
        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        } 
        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);

        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();
            if (jsonObject.has(PartyConstants.partyId)) {
            	JsonArray classifications = new JsonArray();
				try {
					if (jsonObject.has(PartyConstants.classifications)
							&& !jsonObject.get(PartyConstants.classifications).isJsonNull()) {
						classifications  = jsonObject.get(PartyConstants.classifications).getAsJsonArray();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				Map<String, String> map = getReferenceMap(headerMap);
				for (JsonElement element : classifications) {
					JsonObject jsonObject1 = element.getAsJsonObject();
					String classificationCode = jsonObject1.has(PartyConstants.classificationCode) && !jsonObject1.get(PartyConstants.classificationCode).isJsonNull() ? jsonObject1.get(PartyConstants.classificationCode).getAsString() : "";
					if(StringUtils.isNotBlank(classificationCode)) {
						jsonObject1.addProperty(PartyConstants.name, map.get(classificationCode));
					}
				}    
                                dbxResult.setResponse(jsonObject);
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }
      
    private Map<String, String> getReferenceMap(Map<String, Object> headerMap) {
 		JsonArray references = new JsonArray();
 		Map<String, String> map = new HashMap<String, String>();
 		PartyBackendDelegateImpl impl = new PartyBackendDelegateImpl();
 		DBXResult dbxResult1 = impl.getReferenceByID("ClassificationCode", headerMap);
 		try {
 			JsonObject jsonObject = new JsonParser().parse((String) dbxResult1.getResponse()).getAsJsonObject();
 			if (jsonObject.has(PartyConstants.REFERENCES) && !jsonObject.get(PartyConstants.REFERENCES).isJsonNull()) {
 				jsonObject = jsonObject.get(PartyConstants.REFERENCES).getAsJsonObject();
 				if (jsonObject.has(PartyConstants.ClassificationCode)
 						&& !jsonObject.get(PartyConstants.ClassificationCode).isJsonNull()) {
 					references = jsonObject.get(PartyConstants.ClassificationCode).getAsJsonArray();
 				}
 			}

 			for (JsonElement element : references) {
 				jsonObject = element.getAsJsonObject();
 				try {
 					if (jsonObject.has(PartyConstants.id) && !jsonObject.get(PartyConstants.id).isJsonNull()) {
 						String id = jsonObject.get(PartyConstants.id).getAsString();
 						String name = jsonObject.has(PartyConstants.name)
 								&& !jsonObject.get(PartyConstants.name).isJsonNull()
 										? jsonObject.get(PartyConstants.name).getAsString()
 										: "";
 						String description = jsonObject.has(PartyConstants.description)
 								&& !jsonObject.get(PartyConstants.description).isJsonNull()
 										? jsonObject.get(PartyConstants.description).getAsString()
 										: "";
 						if (StringUtils.isNotBlank(id)) {
 							if (StringUtils.isNotBlank(name)) {
 								map.put(id, name);
 							} else {
 								map.put(id, description);
 							}
 						}
 					}
 				} catch (Exception e) {
 					// TODO: handle exception
 				}
 			}
 		} catch (Exception e) {
 			// TODO: handle exception
 		}

 		return map;
 	}

    @Override
    public DBXResult create(PartyDTO partyDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        String party = partyDTO.toStringJson().toString();

        diagnostic.prepareDebug("PartyDTO for create Party Service is : " + party).log();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_CREATE);

        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, partyURL, party, headerMap);

        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();
            if (jsonObject.has(PartyConstants.id)) {
                String id = jsonObject.get(PartyConstants.id).getAsString();
                dbxResult.setResponse(id);
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while parsing the save Party response: ", e).log();
            dbxResult.setDbpErrMsg("Party create Failed");
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }

    @Override
    public DBXResult searchParty(PartySearchDTO searchDTO, Map<String, Object> headers) {
        DBXResult dbxResult = new DBXResult();
        JsonObject party = searchDTO.toStringJson();

        diagnostic.prepareDebug("PartyDTO for get Party Service is : " + party.toString()).log();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_SEARCH) + getRelativeURL(party);
        if(!headers.containsKey("Authorization")) {
        	 PartyUtils.addJWTAuthHeader(headers, AuthConstants.PRE_LOGIN_FLOW);
        }
        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headers);

        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();
            
            if (jsonObject.has(PartyConstants.parties)) {
                 dbxResult.setResponse(jsonObject);
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }
        } catch (Exception e) {

            alert.prepareError("Caught exception while getting Party: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }

    private String getRelativeURL(JsonObject party) {
        StringBuilder builder = new StringBuilder();
        for (Entry<String, JsonElement> entry : party.entrySet()) {
            try {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue().getAsString(), "UTF-8"));
                builder.append("&");
            } catch (UnsupportedEncodingException e) {
            	alert.prepareError("Exception", e).log();
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
    
    @Override
	public DBXResult GetPartyData(PartyDTO partyDTO, Map < String, Object > headerMap) {
		  DBXResult dbxResult = new DBXResult();
		  String party = partyDTO.toStringJson().toString();

		  diagnostic.prepareDebug("PartyDTO for get Party Service is : " + party).log();

		  String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
		    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyDTO.getPartyId());
		  PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
		  DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);

		  try {
		    JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
		    JsonObject jsonObject;

		    jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() :
		      jsonElement.getAsJsonArray().get(0).getAsJsonObject();
		    if (jsonObject.has(PartyConstants.partyId)) {

		      if (!jsonObject.toString().equals("{}")) {
		        JsonObject PartyInfo = new JsonObject();
		        String str = "";

		        JsonObject PersonalInfo = new JsonObject();
		        str = jsonObject.has("firstName") ? jsonObject.get("firstName").getAsString() : "";
		        PersonalInfo.addProperty("firstName", str);
		        str = jsonObject.has("lastName") ? jsonObject.get("lastName").getAsString() : "";
		        PersonalInfo.addProperty("lastName", str);
		        str = jsonObject.has("dateOfBirth") ? jsonObject.get("dateOfBirth").getAsString() : "";
		        PersonalInfo.addProperty("dateOfBirth", str);

		        JsonArray IndentityJsonArray = new JsonArray();
		        if (jsonObject.has("partyIdentifiers")) {
		          JsonArray partyIdentifiersArray = jsonObject.get("partyIdentifiers").getAsJsonArray();
		          for (int i = 0; i < partyIdentifiersArray.size(); i++) {
		            JsonObject IdentityInfoObj = new JsonObject();
		            JsonObject partyIdentifiersArrayObj = partyIdentifiersArray.get(i).getAsJsonObject();
		            str = partyIdentifiersArrayObj.has("type") ? partyIdentifiersArrayObj.get("type").getAsString() : "";
		            IdentityInfoObj.addProperty("identityType", str);
		            str = partyIdentifiersArrayObj.has("identifierNumber") ? partyIdentifiersArrayObj.get("identifierNumber").getAsString() : "";
		            IdentityInfoObj.addProperty("identifierNumber", str);
		            str = partyIdentifiersArrayObj.has("issuingCountry") ? partyIdentifiersArrayObj.get("issuingCountry").getAsString() : "";
		            IdentityInfoObj.addProperty("issuingCountry", str);
		            str = partyIdentifiersArrayObj.has("issuingState") ? partyIdentifiersArrayObj.get("issuingState").getAsString() : "";
		            IdentityInfoObj.addProperty("issuingState", str);
		            str = partyIdentifiersArrayObj.has("issuedDate") ? partyIdentifiersArrayObj.get("issuedDate").getAsString() : "";
		            IdentityInfoObj.addProperty("issuedDate", str);
		            str = partyIdentifiersArrayObj.has("expiryDate") ? partyIdentifiersArrayObj.get("expiryDate").getAsString() : "";
		            IdentityInfoObj.addProperty("expiryDate", str);
		            IndentityJsonArray.add(IdentityInfoObj);
		          }
		        } else {
		          IndentityJsonArray.add(new JsonObject());
		        }

		        JsonArray EmploymentJsonArray = new JsonArray();
		        if (jsonObject.has("employments")) {
		          JsonArray employmentArray = jsonObject.get("employments").getAsJsonArray();
		          for (int i = 0; i < employmentArray.size(); i++) {
		            JsonObject EmploymentObj = new JsonObject();
		            JsonObject EmploymentArrayObj = employmentArray.get(i).getAsJsonObject();

		            str = EmploymentArrayObj.has("partyId") ? EmploymentArrayObj.get("partyId").getAsString() : "";
		            EmploymentObj.addProperty("partyId", str);
		            PartyInfo.addProperty("PartyId", str);
		            str = EmploymentArrayObj.has("type") ? EmploymentArrayObj.get("type").getAsString() : "";
		            EmploymentObj.addProperty("type", str);
		            str = EmploymentArrayObj.has("salaryFrequency") ? EmploymentArrayObj.get("salaryFrequency").getAsString() : "";
		            EmploymentObj.addProperty("salaryFrequency", str);
		            str = EmploymentArrayObj.has("salary") ? EmploymentArrayObj.get("salary").getAsString() : "";
		            EmploymentObj.addProperty("salary", str);
		            str = EmploymentArrayObj.has("employerOfficePhone") ? EmploymentArrayObj.get("employerOfficePhone").getAsString() : "";
		            EmploymentObj.addProperty("employerOfficePhone", str);
		            str = EmploymentArrayObj.has("employerName") ? EmploymentArrayObj.get("employerName").getAsString() : "";
		            EmploymentObj.addProperty("employerName", str);
		            str = EmploymentArrayObj.has("employerOfficePhoneIdd") ? EmploymentArrayObj.get("employerOfficePhoneIdd").getAsString() : "";
		            EmploymentObj.addProperty("countryCode", str);
		            
		            if (jsonObject.has("occupations")) {
				            JsonArray occupationArray = jsonObject.get("occupations").getAsJsonArray();
				            if(i<occupationArray.size()) {
					            JsonObject occupationArrayObj = occupationArray.get(i).getAsJsonObject();
					            str = occupationArrayObj.has("occupationType") ? occupationArrayObj.get("occupationType").getAsString() : "";
					            EmploymentObj.addProperty("occupationType", str);
				            }else {
				            	EmploymentObj.addProperty("occupationType", "");
				            }
		            }
		            else
		            	EmploymentObj.addProperty("occupationType", "");

		            if (EmploymentArrayObj.has("extensionData")) {
		              str = EmploymentArrayObj.get("extensionData").getAsJsonObject().has("hoursPerWeek") ?
		                EmploymentArrayObj.get("extensionData").getAsJsonObject().get("hoursPerWeek").getAsString() : "";
		              EmploymentObj.addProperty("hoursPerWeek", str);
		            } else
		              EmploymentObj.addProperty("hoursPerWeek", "");

		            if (EmploymentArrayObj.has("extensionData")) {
		              str = EmploymentArrayObj.get("extensionData").getAsJsonObject().has("montlyIncome") ?
		                EmploymentArrayObj.get("extensionData").getAsJsonObject().get("montlyIncome").getAsString() : "";
		              EmploymentObj.addProperty("montlyIncome", str);
		            } else
		              EmploymentObj.addProperty("montlyIncome", "");
		            EmploymentJsonArray.add(EmploymentObj);

		          }
		        } else {
		          EmploymentJsonArray.add(new JsonObject());
		        }
		        
		        JsonArray addressJsonArray = new JsonArray();
		        if (jsonObject.has("addresses")) {
		          JsonArray addressesArray = jsonObject.get("addresses").getAsJsonArray();
		          for (int i = 0; i < addressesArray.size(); i++) {
		            JsonObject AddressObj = addressesArray.get(i).getAsJsonObject();
		            if (AddressObj.get("communicationType") != null && AddressObj.get("communicationType").getAsString().equals("Email")) {
		              str = AddressObj.has("electronicAddress") ? AddressObj.get("electronicAddress").getAsString() : "";
		              PersonalInfo.addProperty("email", str);
		            }
		            if (AddressObj.get("communicationType") != null && AddressObj.get("communicationType").getAsString().equals("Mobile")) {
		              str = AddressObj.has("iddPrefixPhone") ? AddressObj.get("iddPrefixPhone").getAsString() : "";
		              PersonalInfo.addProperty("countryCode", str);
		              str = AddressObj.has("phoneNo") ? AddressObj.get("phoneNo").getAsString() : "";
		              PersonalInfo.addProperty("phoneNo", str);
		            }
		            if (AddressObj.get("communicationType") != null && (AddressObj.get("communicationType").getAsString().equals("MailingAddress"))) {
		              JsonObject AddressObject = new JsonObject();
		              str = AddressObj.has("buildingName") ? AddressObj.get("buildingName").getAsString() : "";
		              AddressObject.addProperty("buildingName", str);
		              str = AddressObj.has("streetName") ? AddressObj.get("streetName").getAsString() : "";
		              AddressObject.addProperty("streetName", str);
		              str = AddressObj.has("countryCode") ? AddressObj.get("countryCode").getAsString() : "";
		              AddressObject.addProperty("country", str);
		              str = AddressObj.has("postalOrZipCode") ? AddressObj.get("postalOrZipCode").getAsString() : "";
		              AddressObject.addProperty("postalOrZipCode", str);
		              str = AddressObj.has("countrySubdivision") ? AddressObj.get("countrySubdivision").getAsString() : "";
		              AddressObject.addProperty("state", str);
		              str = AddressObj.has("town") ? AddressObj.get("town").getAsString() : "";
		              AddressObject.addProperty("town", str);
		              str = AddressObj.has("addressType") ? AddressObj.get("addressType").getAsString() : "";
		              AddressObject.addProperty("addressType", str);
		              addressJsonArray.add(AddressObject);
		            }
		          }
		        } else {
		          addressJsonArray.add(new JsonObject());
		        }

		        PartyInfo.addProperty("PersonalInfo", PersonalInfo.toString());
		        PartyInfo.addProperty("Identity", IndentityJsonArray.toString());
		        PartyInfo.addProperty("Employment", EmploymentJsonArray.toString());
		        PartyInfo.addProperty("Addresses", addressJsonArray.toString());
		        dbxResult.setResponse(PartyInfo);

		      }
		    } else {
		      if (jsonObject.has("message")) {
		        dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
		      }

		      if (jsonObject.has("status")) {
		        dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
		      }
		    }

		  } catch (Exception e) {
		    alert.prepareError("Caught exception while getting Party: ", e).log();
		    dbxResult.setDbpErrMsg(response.getDbpErrMsg());
		    return dbxResult;
		  }

		  return dbxResult;
	}

	
	public String getT24Customer(String coreCustomerId, DataControllerRequest request) {
		String response = null;

		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("coreCustomerID", coreCustomerId);
		
	 	try {
	 		response = CommonUtils.callPassthroughIntegrationService(request, inputParams, request.getHeaderMap(),
	 				UserConstants.SERVICE_ID_ONBOARDING_USER, UserConstants.OP_GET_T24_ORIGINATION_DATA, true);
	 	}
	 	catch (Exception e) {
	 		alert.prepareError("Error occured in PartyUserManagementBackendDelegate", e).log();
            return null;
        }
	 	
		return response;
	}
}
