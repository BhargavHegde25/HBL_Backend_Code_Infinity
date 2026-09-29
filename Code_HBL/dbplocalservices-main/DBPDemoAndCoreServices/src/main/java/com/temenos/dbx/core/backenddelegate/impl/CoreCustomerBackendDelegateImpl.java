package com.temenos.dbx.core.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.temenos.dbx.core.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyAddress;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;

public class CoreCustomerBackendDelegateImpl implements CoreCustomerBackendDelegate {

    Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public DBXResult saveCustomerFromParty(PartyDTO partyDTO, Map<String, Object> inputParams, String coreURL) {

        DBXResult dbxResult = new DBXResult();

        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

        JsonObject jsonObject = getJsonFromInput(partyDTO, inputParams);

        diagnostic.prepareDebug("Input for Core Customer Creation is: " +jsonObject.toString()).log();

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, coreURL , jsonObject.toString(), inputParams);

        try {
            jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if(jsonObject.has("header") && !jsonObject.has("error") && !jsonObject.get("header").isJsonNull()) {
                jsonObject = jsonObject.get("header").getAsJsonObject();
                if(jsonObject.has("status") && !jsonObject.get("status").isJsonNull()) {
                    dbxResult.setResponse(jsonObject.get("id").getAsString());
                    return dbxResult;
                }
            }
            else if(jsonObject.has("error") && !jsonObject.get("error").isJsonNull()){
                jsonObject = jsonObject.get("error").getAsJsonObject();

                if(jsonObject.has("errorDetails") && !jsonObject.get("errorDetails").isJsonNull()) {
                    jsonObject = jsonObject.get("errorDetails").getAsJsonArray().get(0).getAsJsonObject();
                    if(jsonObject.has("code") && !jsonObject.get("code").isJsonNull()) {
                        dbxResult.setDbpErrCode(jsonObject.get("code").getAsString());
                    }

                    if(jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                        dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                    }
                }
                else {
                    dbxResult.setDbpErrMsg(jsonObject.toString());
                }
            }
        }catch (Exception e) {
            alert.prepareError("Caught exception while creating Core customer: ",  e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }


    @Override
    public DBXResult updateCustomerfromParty(PartyDTO partyDTO, Map<String, Object> inputParams,String coreURL) {
        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

        DBXResult dbxResult = new DBXResult();

        JsonObject jsonObject = getJsonFromInput(partyDTO, null);

        if("Active".equalsIgnoreCase(partyDTO.getPartyStatus())) {
            dbxResult.setResponse(partyDTO.getPartyId());
            return dbxResult;
        }

        diagnostic.prepareDebug("Input for Core Customer Creation is: " +jsonObject.toString()).log();

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, coreURL+"/"+partyDTO.getPartyId() , jsonObject.toString(), inputParams);

        try {
            jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if(jsonObject.has("header") && !jsonObject.has("error") && !jsonObject.get("header").isJsonNull()) {
                jsonObject = jsonObject.get("header").getAsJsonObject();
                if(jsonObject.has("status") && !jsonObject.get("status").isJsonNull()) {
                    dbxResult.setResponse(jsonObject.get("id").getAsString());
                    return dbxResult;
                }
            }
            else if(jsonObject.has("error") && !jsonObject.get("error").isJsonNull()){
                jsonObject = jsonObject.get("error").getAsJsonObject();

                if(jsonObject.has("errorDetails") && !jsonObject.get("errorDetails").isJsonNull()) {
                    jsonObject = jsonObject.get("errorDetails").getAsJsonArray().get(0).getAsJsonObject();
                    if(jsonObject.has("code") && !jsonObject.get("code").isJsonNull()) {
                        dbxResult.setDbpErrCode(jsonObject.get("code").getAsString());
                    }

                    if(jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                        dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                    }
                }
                else {
                    dbxResult.setDbpErrMsg(jsonObject.toString());
                }
            }
        }catch (Exception e) {
            alert.prepareError("Caught exception while creating Core customer: ",  e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }


    private JsonObject getJsonFromInput(PartyDTO partyDTO, Map<String, Object> inputParams) {

        Map<String, String> map = new HashMap<String, String>();

        if(partyDTO.getPartyId() != null && inputParams != null) {
            map.put("id", partyDTO.getPartyId());
        }


        if(StringUtils.isNotBlank(partyDTO.getFirstName())) {
            map.put("FirstName", partyDTO.getFirstName());

        }

        if(StringUtils.isNotBlank(partyDTO.getLastName())) {
            map.put("LastName", partyDTO.getLastName());
        }

        if(StringUtils.isNotBlank(partyDTO.getDateOfBirth())){
            map.put("DateOfBirth", partyDTO.getDateOfBirth());
        }

        List<PartyAddress> list = partyDTO.getPartyAddress();

        if(list != null) {

            PartyAddress contactPoint;
            for(int i=0; i< list.size(); i++) {
                contactPoint = list.get(i);
                if(contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC) && contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)) {
                    map.put("Email", contactPoint.getElectronicAddress());
                }
                else if(contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)) {
                    if(StringUtils.isNotBlank(contactPoint.getPhoneNo())) {
                        map.put("Phone", contactPoint.getPhoneNo().replace("-", ""));
                    }

                    if(StringUtils.isNotBlank(contactPoint.getIddPrefixPhone())) {
                        map.put("MobileCountryCode", contactPoint.getIddPrefixPhone().trim());
                    }
                    if(StringUtils.isNotBlank(map.get("MobileCountryCode")) && !map.get("MobileCountryCode").contains("+")){
                        map.put("MobileCountryCode", "+"+map.get("MobileCountryCode"));
                    }
                }
            }
        }

        JsonObject inputBody = new JsonObject();

        inputBody.add("header", new JsonObject());

        JsonObject  body = new JsonObject();

        if(map.containsKey("id")) {
            JsonArray array = new JsonArray();
            JsonObject externalId = new JsonObject();
            externalId.addProperty("externalSystemId", "PARTY");
            externalId.addProperty("externalCustomerId", map.get("id"));
            array.add(externalId);
            body.add("externalId", array);
        }

        if(map.containsKey("FirstName")) {
            JsonArray displayNames = new JsonArray();
            JsonObject displayName = new JsonObject();
            displayName.addProperty("displayName", map.get("FirstName"));
            displayNames.add(displayName);
            body.add("displayNames", displayNames);
            JsonArray customerNames = new JsonArray();
            JsonObject customerName = new JsonObject();
            customerName.addProperty("customerName", map.get("FirstName"));
            customerNames.add(customerName);
            body.add("customerNames", customerNames);    

            body.addProperty("givenName", map.get("FirstName"));

        }

        if(map.containsKey("Phone")) {
            JsonArray contactTypes = new JsonArray();
            JsonObject contactType = new JsonObject();
            contactType.addProperty("contactType", "MOBILE");
            contactType.addProperty("countryCode", StringUtils.isNotBlank(map.get("MobileCountryCode"))? map.get("MobileCountryCode") : "+1");
            contactType.addProperty("contactData", map.get("Phone"));
            contactTypes.add(contactType);
            body.add("contactTypes", contactTypes);
        }


        if (map.containsKey("Email") || map.containsKey("Phone")) {
            JsonArray communicationDevices = new JsonArray();
            JsonObject communicationDevice = new JsonObject();
            communicationDevice.addProperty("email", map.get("Email"));
            if (map.get("Phone").contains("+")) {
                communicationDevice.addProperty("smsNumber", map.get("Phone"));
                communicationDevice.addProperty("phoneNumber", map.get("Phone"));
            } else {
                communicationDevice.addProperty("smsNumber", map.get("MobileCountryCode") + map.get("Phone"));
                communicationDevice.addProperty("phoneNumber", map.get("MobileCountryCode") + "-" + map.get("Phone"));
            }
            communicationDevices.add(communicationDevice);
            body.add("communicationDevices", communicationDevices);
        }


        if(map.containsKey("DateOfBirth")) {
            body.addProperty("dateOfBirth", map.get("DateOfBirth"));
        }

        if(inputParams != null) {
            for(Entry<String, Object> entry : inputParams.entrySet()) {
                if(!entry.getKey().equals("partyEventData") && !entry.getKey().equals("Authorization")) {
                    try {
                        body.addProperty(entry.getKey(), Integer.parseInt((String) entry.getValue()));
                    }catch (Exception e) {
                        body.addProperty(entry.getKey(), (String) entry.getValue());
                    }
                }
            }
        }

        //        if(StringUtils.isBlank(partyDTO.getPartyId())) {
        //            body.addProperty("customerType","PROSPECT");
        //            body.addProperty("language", 1);
        //            body.addProperty("customerStatus", 4);
        //            body.addProperty("customerMnemonic", "CUSONE");
        //            body.addProperty("nationalityId", "US");
        //            body.addProperty("residenceId", "US");
        //            body.addProperty("accountOfficerId", 26);
        //            body.addProperty("industryId", 1000);
        //            body.addProperty("target", 4);
        //            body.addProperty("sectorId", 1000);
        //            body.addProperty("gender", "MALE");
        //            body.addProperty("postCode", 560021);
        //        }

        if(map.containsKey("LastName")) {
            body.addProperty("lastName", map.get("LastName"));
        }


        inputBody.add("body", body);            
        return inputBody;
    }


    @Override
    public DBXResult saveCustomerFromDBX(CustomerDTO customerDTO, Map<String, Object> map, String coreURL) {
        DBXResult dbxResult = new DBXResult();

        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

        JsonObject jsonObject = getJsonFromInput(customerDTO, map);

        diagnostic.prepareDebug("Input for Core Customer Creation is: " +jsonObject.toString()).log();

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, coreURL , jsonObject.toString(), map);

        try {
            jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if(jsonObject.has("header") && !jsonObject.has("error") && !jsonObject.get("header").isJsonNull()) {
                jsonObject = jsonObject.get("header").getAsJsonObject();
                if(jsonObject.has("status") && !jsonObject.get("status").isJsonNull()) {
                    dbxResult.setResponse(jsonObject.get("id").getAsString());
                    return dbxResult;
                }
            }
            else if(jsonObject.has("error") && !jsonObject.get("error").isJsonNull()){
                jsonObject = jsonObject.get("error").getAsJsonObject();

                if(jsonObject.has("errorDetails") && !jsonObject.get("errorDetails").isJsonNull()) {
                    jsonObject = jsonObject.get("errorDetails").getAsJsonArray().get(0).getAsJsonObject();
                    if(jsonObject.has("code") && !jsonObject.get("code").isJsonNull()) {
                        dbxResult.setDbpErrCode(jsonObject.get("code").getAsString());
                    }

                    if(jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                        dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                    }
                }
                else {
                    dbxResult.setDbpErrMsg(jsonObject.toString());
                }
            }
            else {
                if(jsonObject.has("code") && !jsonObject.get("code").isJsonNull()) {
                    dbxResult.setDbpErrCode(jsonObject.get("code").getAsString());
                }

                if(jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }
            }
        }catch (Exception e) {
            alert.prepareError("Caught exception while creating Core customer: ",  e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }


    private JsonObject getJsonFromInput(CustomerDTO customerDTO, Map<String, Object> inputParams) {
        Map<String, String> map = new HashMap<String, String>();

        if(StringUtils.isNotBlank(customerDTO.getFirstName())){
            map.put("FirstName", customerDTO.getFirstName());
        }

        if(StringUtils.isNotBlank(customerDTO.getLastName())){
            map.put("LastName", customerDTO.getLastName());
        }

        if(StringUtils.isNotBlank(customerDTO.getDateOfBirth())){
            map.put("DateOfBirth", customerDTO.getDateOfBirth());
        }

		if (StringUtils.isNotBlank(customerDTO.getStreet())) {
			map.put("street", customerDTO.getStreet());
		}
        
        List<CustomerCommunicationDTO> list = customerDTO.getCustomerCommuncation();

        if(list != null) {

            CustomerCommunicationDTO communicationDTO;
            for(int i=0; i< list.size(); i++) {
                communicationDTO = list.get(i);
                if(communicationDTO.getType_id().equals(HelperMethods.getCommunicationTypes().get("Email"))) {
                    map.put("Email", communicationDTO.getValue());
                }
                else {
                    if(communicationDTO.getValue().contains("-")) {
                        map.put("Phone", communicationDTO.getValue().substring(communicationDTO.getValue().indexOf('-')+1));
                    }
                    else {
                        map.put("Phone", communicationDTO.getValue());
                    }
                    map.put("MobileCountryCode", communicationDTO.getPhoneCountryCode());
                    if(StringUtils.isNotBlank(map.get("MobileCountryCode")) && !map.get("MobileCountryCode").contains("+")){
                        map.put("MobileCountryCode", "+"+map.get("MobileCountryCode"));
                    }
                }
            }
        }

        JsonObject inputBody = new JsonObject();

        inputBody.add("header", new JsonObject());

        JsonObject  body = new JsonObject();

        if(map.containsKey("FirstName")) {
            JsonArray displayNames = new JsonArray();
            JsonObject displayName = new JsonObject();
            displayName.addProperty("displayName", map.get("FirstName"));
            displayNames.add(displayName);
            body.add("displayNames", displayNames);
            JsonArray customerNames = new JsonArray();
            JsonObject customerName = new JsonObject();
            customerName.addProperty("customerName", map.get("FirstName"));
            customerNames.add(customerName);
            body.add("customerNames", customerNames);    

            body.addProperty("givenName", map.get("FirstName"));

        }

        if(map.containsKey("Phone")) {
            JsonArray contactTypes = new JsonArray();
            JsonObject contactType = new JsonObject();
            contactType.addProperty("contactType", "MOBILE");
            contactType.addProperty("countryCode", StringUtils.isNotBlank(map.get("MobileCountryCode"))? map.get("MobileCountryCode") : "+1");
            contactType.addProperty("contactData", map.get("Phone"));
            contactTypes.add(contactType);
            body.add("contactTypes", contactTypes);
        }


		if (map.containsKey("Email") || map.containsKey("Phone")) {
			JsonArray communicationDevices = new JsonArray();
			JsonObject communicationDevice = new JsonObject();
			communicationDevice.addProperty("email", map.get("Email"));
			if (map.get("Phone").contains("+")) {
				communicationDevice.addProperty("smsNumber", map.get("Phone"));
				communicationDevice.addProperty("phoneNumber", map.get("Phone"));
			} else {
				communicationDevice.addProperty("smsNumber", map.get("MobileCountryCode") + map.get("Phone"));
				communicationDevice.addProperty("phoneNumber", map.get("MobileCountryCode") + "-" + map.get("Phone"));
			}
			communicationDevices.add(communicationDevice);
			body.add("communicationDevices", communicationDevices);
		}
        

        if(map.containsKey("street")) {
            JsonArray streets = new JsonArray();
            JsonObject street = new JsonObject();
           // street.addProperty("street", "streets");
            street.addProperty("street", map.get("street"));
            streets.add(street);
            body.add("streets", streets);
        }


        if(map.containsKey("DateOfBirth")) {
            body.addProperty("dateOfBirth", map.get("DateOfBirth"));
        }

        if(inputParams != null) {
            for(Entry<String, Object> entry : inputParams.entrySet()) {
                if(!entry.getKey().equals("partyEventData") && !entry.getKey().equals("Authorization")) {
                    try {
                        body.addProperty(entry.getKey(), Integer.parseInt((String) entry.getValue()));
                    }catch (Exception e) {
                        body.addProperty(entry.getKey(), (String) entry.getValue());
                    }
                }
            }
        }

        if(map.containsKey("LastName")) {
            body.addProperty("lastName", map.get("LastName"));
        }


        inputBody.add("body", body);
        
        return inputBody;
    }


    @Override
    public DBXResult updateCustomerfromDBX(CustomerDTO customerDTO, Map<String, Object> map, String coreURL) {
        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

        DBXResult dbxResult = new DBXResult();

        JsonObject jsonObject = getJsonFromInput(customerDTO, null);

        if("SID_CUS_ACTIVE".equalsIgnoreCase(customerDTO.getStatus_id())) {
            jsonObject.get("body").getAsJsonObject().addProperty("customerType", "ACTIVE");
        }
        

        diagnostic.prepareDebug("Input for Core Customer Creation is: " +jsonObject.toString()).log();

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, coreURL+"/"+customerDTO.getId() , jsonObject.toString(), map);

        try {
            jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if(jsonObject.has("header") && !jsonObject.has("error") && !jsonObject.get("header").isJsonNull()) {
                jsonObject = jsonObject.get("header").getAsJsonObject();
                if(jsonObject.has("status") && !jsonObject.get("status").isJsonNull()) {
                    dbxResult.setResponse(jsonObject.get("id").getAsString());
                    return dbxResult;
                }
            }
            else if(jsonObject.has("error") && !jsonObject.get("error").isJsonNull()){
                jsonObject = jsonObject.get("error").getAsJsonObject();

                if(jsonObject.has("errorDetails") && !jsonObject.get("errorDetails").isJsonNull()) {
                    jsonObject = jsonObject.get("errorDetails").getAsJsonArray().get(0).getAsJsonObject();
                    if(jsonObject.has("code") && !jsonObject.get("code").isJsonNull()) {
                        dbxResult.setDbpErrCode(jsonObject.get("code").getAsString());
                    }

                    if(jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                        dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                    }
                }
                else {
                    dbxResult.setDbpErrMsg(jsonObject.toString());
                }
            }
        }catch (Exception e) {
            alert.prepareError("Caught exception while creating Core customer: ",  e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }


    @Override
    public DBXResult activateCustomer(CustomerDTO customerDTO, Map<String, Object> map, String coreURL) {
        DBXResult dbxResult = new DBXResult();

        JsonObject jsonObject = getJsonFromInput(customerDTO, null);

        jsonObject.get("body").getAsJsonObject().addProperty("customerType", "ACTIVE");
        

        diagnostic.prepareDebug("Input for Core Customer Creation is: " +jsonObject.toString()).log();

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, coreURL+"/"+customerDTO.getId() , jsonObject.toString(), map);

        try {
            jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if(jsonObject.has("header") && !jsonObject.has("error") && !jsonObject.get("header").isJsonNull()) {
                jsonObject = jsonObject.get("header").getAsJsonObject();
                if(jsonObject.has("status") && !jsonObject.get("status").isJsonNull()) {
                    dbxResult.setResponse(jsonObject.get("id").getAsString());
                    return dbxResult;
                }
            }
            else if(jsonObject.has("error") && !jsonObject.get("error").isJsonNull()){
                jsonObject = jsonObject.get("error").getAsJsonObject();

                if(jsonObject.has("errorDetails") && !jsonObject.get("errorDetails").isJsonNull()) {
                    jsonObject = jsonObject.get("errorDetails").getAsJsonArray().get(0).getAsJsonObject();
                    if(jsonObject.has("code") && !jsonObject.get("code").isJsonNull()) {
                        dbxResult.setDbpErrCode(jsonObject.get("code").getAsString());
                    }

                    if(jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                        dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                    }
                }
                else {
                    dbxResult.setDbpErrMsg(jsonObject.toString());
                }
            }
        }catch (Exception e) {
            alert.prepareError("Caught exception while creating Core customer: ",  e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }


}
