package com.temenos.dbx.party.businessdelegate.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.party.businessdelegate.api.PartyCustomerBusinessDelegate;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.AlternateIdentity;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyBackendDelegate;

public class PartyCustomerBusinessDelegateImpl implements PartyCustomerBusinessDelegate {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private LoggerUtil logger;

    @Override
    public DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> map) {

        DBXResult dbxResult = new DBXResult();

        logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, customerDTO.getId());

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, map);
        PartyDTO partyDTO = new PartyDTO();

        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            partyDTO.loadFromJson(jsonObject);
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ", e).log();
        }

        partyDTO = PartyUtils.buildPartyDTO(customerDTO, partyDTO);
        partyDTO = PartyUtils.buildAdditionalData(partyDTO, map);
        //adding Salesforce alternate id if present
		if (map.get("accountId") != null && StringUtils.isNotBlank((String) map.get("accountId"))) {
			AlternateIdentity alternateIdentity = new AlternateIdentity();
			alternateIdentity.setIdentityNumber((String) map.get("accountId"));
			alternateIdentity.setIdentityType("SalesForce");
			partyDTO.setAlternateIdentities(alternateIdentity);
		}
		
		Gson gson = new Gson();
        JsonObject partydata = partyDTO.toStringJson();//.toString();
        
        if(map.containsKey("employments")  && map.get("employments")!=null) {
        	partydata.add("employments",  gson.fromJson(map.get("employments").toString(), JsonElement.class));
        	map.remove("employments");
        }
        
        if(map.containsKey("occupations")  && map.get("occupations")!=null) {
        	partydata.add("occupations", gson.fromJson(map.get("occupations").toString(), JsonElement.class));
        	map.remove("occupations");
        }
        
        String party = partydata.toString();

        diagnostic.prepareDebug("PartyDTO for update Party Service is : " + party).log();

        partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_UPDATE, customerDTO.getId());

        response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyURL, party, map);

        String id = "";
        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if (jsonObject.has("id")) {

                id = jsonObject.get("id").getAsString();
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while updating Party: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        dbxResult.setResponse(id);
        return dbxResult;
    }

    @Override
    public DBXResult saveCustomer(CustomerDTO customerDTO, Map<String, Object> map) {

        DBXResult dbxResult = new DBXResult();

        logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);

        PartyDTO partyDTO = new PartyDTO();

        partyDTO = PartyUtils.buildPartyDTO(customerDTO, partyDTO);
        partyDTO = PartyUtils.buildAdditionalData(partyDTO, map);
        
        //adding Salesforce alternate id if present
		if (map.get("accountId") != null && StringUtils.isNotBlank((String) map.get("accountId"))) {
			AlternateIdentity alternateIdentity = new AlternateIdentity();
			alternateIdentity.setIdentityNumber((String) map.get("accountId"));
			alternateIdentity.setIdentityType("SalesForce");
			partyDTO.setAlternateIdentities(alternateIdentity);
		}
        
        String party = partyDTO.toStringJson().toString();
        diagnostic.prepareDebug("PartyDTO for create Party Service is : " + party).log();
        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_CREATE);

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, partyURL, party, map);

        String id = "";
        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            if (jsonObject.has("id")) {

                id = jsonObject.get("id").getAsString();
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

        dbxResult.setResponse(id);

        return dbxResult;
    }

    @Override
    public DBXResult getReferenceByID(String referenceByID, Map<String, Object> headerMap) {
        PartyBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(PartyBackendDelegate.class);
        return backendDelegate.getReferenceByID(referenceByID, headerMap);
    }

    @Override
    public DBXResult activateCustomer(String id, Map<String, Object> headers) {
        DBXResult dbxResult = new DBXResult();

        logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);

        PartyUtils.addJWTAuthHeader(null, headers, AuthConstants.PRE_LOGIN_FLOW);

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, id);

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headers);
        PartyDTO partyDTO = new PartyDTO();

        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            partyDTO.loadFromJson(jsonObject);
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ", e).log();
        }

        partyDTO.setPartyId(id);
        partyDTO.setPartyStatus("Active");

        String party = partyDTO.toStringJson().toString();

        diagnostic.prepareDebug("PartyDTO for update Party Service is : " + party).log();

        partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_UPDATE, id);

        response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyURL, party, headers);

        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if (jsonObject.has("id")) {

                id = jsonObject.get("id").getAsString();
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while updating Party: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        dbxResult.setResponse(id);
        return dbxResult;
    }

	@Override
	public DBXResult addIdentifier(PartyDTO party, Map<String, Object> headers) {
		DBXResult dbxResult = new DBXResult();
		logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);

		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_IDENTIFIER, party.getPartyId());

		JsonObject payloadJson = new JsonObject();
		
		if(party.getAlternateIdentities() != null && party.getAlternateIdentities().size() >0) {
		    JsonArray jsonArray = new JsonArray();
            for(int i=0; i<party.getAlternateIdentities().size(); i++) {
                jsonArray.add(party.getAlternateIdentities().get(i).toStringJson());
            }
			payloadJson.add("alternateIdentities",jsonArray);
		}
		
		if(party.getPartyIdentifier() != null && party.getPartyIdentifier().size() >0) {
		    JsonArray jsonArray = new JsonArray();
            for(int i=0; i<party.getPartyIdentifier().size(); i++) {
                jsonArray.add(party.getPartyIdentifier().get(i).toStringJson());
            }
            payloadJson.add("partyIdentifiers",jsonArray);
        }
		
		DBXResult response = new DBXResult();
		try {
		    response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, partyURL,
	                payloadJson.toString(), headers);

			JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
			if (!jsonObject.has("id")) {
				if (jsonObject.has("message")) 
					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());		
				if (jsonObject.has("status")) 
					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating Party: ", e).log();
			dbxResult.setDbpErrMsg(response.getDbpErrMsg());
			return dbxResult;
		}
		dbxResult.setResponse(party.getPartyId());
		return dbxResult;
	}
	
	@Override
	public DBXResult addClassification(PartyDTO party, Map<String, Object> headers, boolean isClassificationUpdate) {
		DBXResult dbxResult = new DBXResult();
		logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);

		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_CLASSIFICATIONS, party.getPartyId());

		ObjectMapper mapper = new ObjectMapper();
		JsonObject payloadJson = new JsonObject();
		try {
			payloadJson.add("classifications",
					new JsonParser().parse(mapper.writeValueAsString(party.getClassification())).getAsJsonArray());
		} catch (Exception e) {

		}
		DBXResult response = HTTPOperations.sendHttpRequest(
				isClassificationUpdate ? HTTPOperations.operations.PUT : HTTPOperations.operations.POST, partyURL,
				payloadJson.toString(), headers);

		try {
			JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
			if (!jsonObject.has("id")) {
				if (jsonObject.has("message")) 
					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());		
				if (jsonObject.has("status")) 
					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating Party: ", e).log();
			dbxResult.setDbpErrMsg(response.getDbpErrMsg());
			return dbxResult;
		}
		dbxResult.setResponse(party.getPartyId());
		return dbxResult;
	}

	@Override
	public JSONArray getClassification(String partyId, Map<String, Object> headers) {
		JSONArray result = new JSONArray();
		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_CLASSIFICATIONS, partyId);
		DBXResult dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headers);
		try {
			if (dbxResult.getResponse() != null) {
				result = new JSONObject((String) dbxResult.getResponse()).getJSONArray("classifications");
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching classification party : ", e).log();
			dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
		}
		return result;
	}

	@Override
	public DBXResult addPartyRelation(String partyId, JSONObject payload, Map<String, Object> headers) {
		DBXResult dbxResult = new DBXResult();
		logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);

		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_RELATIONS, partyId);

		DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, partyURL,
				payload.toString(), headers);

		try {
			JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
			if (!jsonObject.has("id")) {
				if (jsonObject.has("message"))
					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
				if (jsonObject.has("status"))
					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating Party: ", e).log();
			dbxResult.setDbpErrMsg(response.getDbpErrMsg());
			return dbxResult;
		}
		dbxResult.setResponse(partyId);
		return dbxResult;
	}

	@Override
	public DBXResult getCustomer(String partyId, Map<String, Object> map) {
		DBXResult dbxResult = new DBXResult();
		try {
			logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);
			String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
					+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyId);
			dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, map);
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching Party: ", e).log();
			dbxResult.setDbpErrMsg("Error while fetching party");
		}
		return dbxResult;
	}
	
	
	@Override
	public DBXResult createEmployments(PartyDTO party, Map<String, Object> headers,boolean isUpdate) {
		DBXResult dbxResult = new DBXResult();
		logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);
		
		Gson gson = new Gson();
        JsonObject partydata = party.toStringJson();//.toString();
        
        if(headers.containsKey("employments")  && headers.get("employments")!=null) {
        	partydata.add("employments",  gson.fromJson(headers.get("employments").toString(), JsonElement.class));
        	headers.remove("employments");
        }
        
        if(headers.containsKey("occupations")  && StringUtils.isNotEmpty(headers.get("occupations").toString())) {
        	partydata.add("occupations", gson.fromJson(headers.get("occupations").toString(), JsonElement.class));
        	headers.remove("occupations");
        }
        if(StringUtils.isNotEmpty(partydata.get("employments").toString())) {
        	String EmploymentData = partydata.toString();
    		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
    				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_EMPLOYMENTDETAILS_V4, party.getPartyId());
    		DBXResult response = HTTPOperations.sendHttpRequest(isUpdate? HTTPOperations.operations.PUT :HTTPOperations.operations.POST, partyURL,
    				EmploymentData, headers);
    		
    		try {
    			JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
    			if (!jsonObject.has("id")) {
    				if (jsonObject.has("message")) 
    					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());		
    				if (jsonObject.has("status")) 
    					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
    			}
    		} catch (Exception e) {
    			alert.prepareError("Caught exception while updating Party: ", e).log();
    			dbxResult.setDbpErrMsg(response.getDbpErrMsg());
    			return dbxResult;
    		}
        }
		dbxResult.setResponse(party.getPartyId());
		return dbxResult;
	}

	@Override
	public DBXResult updateIdentifier(String partyId, JsonArray partyIdentities,Map<String,Object> headers) {
		DBXResult dbxResult = new DBXResult();

        logger = new LoggerUtil(PartyCustomerBusinessDelegateImpl.class);
        JsonObject payloadJson = new JsonObject();	
        payloadJson.add("partyIdentifiers",partyIdentities);
        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_IDENTIFIER_V5, partyId);
		DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT , partyURL,
				payloadJson.toString(), headers);
		try {
			JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
			if (!jsonObject.has("id")) {
				if (jsonObject.has("message")) 
					dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());		
				if (jsonObject.has("status")) 
					dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating Party: ", e).log();
			dbxResult.setDbpErrMsg(response.getDbpErrMsg());
			return dbxResult;
		}
		dbxResult.setResponse(partyId);
		return dbxResult;
	}
}
