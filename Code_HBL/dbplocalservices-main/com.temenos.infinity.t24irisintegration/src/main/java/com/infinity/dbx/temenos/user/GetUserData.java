package com.infinity.dbx.temenos.user;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetUserData implements JavaService2, AccountsConstants, TemenosConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

  @Override
  public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
    DataControllerResponse response) throws Exception {

    Result result = new Result();
    String res = "";
    try {

      if (StringUtils.isBlank(request.getParameter("coreCustomerID"))) {
        result.addOpstatusParam(0);
        result.addHttpStatusCodeParam(200);
        result.addErrMsgParam("Misssing input param coreCustomerID");
      }
      String coreCustomerId = request.getParameter("coreCustomerID");
      HashMap < String, Object > params = new HashMap < String, Object > ();
      params.put("coreCustomerID", coreCustomerId);
      params.put("partyID", coreCustomerId);
      request.addRequestParam_(CUSTOMER_CORE_ID, coreCustomerId);
      request.addRequestParam_("partyID", coreCustomerId);
      Authentication authentication = Authentication.getInstance();
      res = CommonUtils.callPassthroughIntegrationService(request, params, request.getHeaderMap(), UserConstants.SERVICE_ID_ONBOARDING_USER, UserConstants.OP_GET_T24_ORIGINATION_DATA, true);

    } catch (Exception e) {
      alert.prepareError("Unexpected error", e).log();
      result.addParam(new Param("FailureReason", e.getMessage()));
      result.addParam(new Param("dbpErrCode", "20001", "int"));
      result.addParam(new Param("dbpErrMsg", "Internal Error", "string"));
    }

    try {

      JsonParser parser = new JsonParser();
      JsonObject completeData = (JsonObject) parser.parse(res);
      JsonObject T24Data = (JsonObject) parser.parse(res).getAsJsonObject().get("body").getAsJsonObject();
      JsonObject T24Header = (JsonObject) parser.parse(res).getAsJsonObject().get("header").getAsJsonObject();
      if (T24Header.has("id")) {

        if (!T24Data.toString().equals("{}")) {
          JsonObject T24Info = new JsonObject();

          T24Info.addProperty("PartyId", T24Header.get("id").getAsString());
          String str = "";
          JsonObject PersonalInfo = new JsonObject();
          str = T24Data.has("givenName") ? T24Data.get("givenName").getAsString() : "";
          PersonalInfo.addProperty("firstName", str);
          str = T24Data.has("lastName") ? T24Data.get("lastName").getAsString() : "";
          PersonalInfo.addProperty("lastName", str);
          str = T24Data.has("dateOfBirth") ? T24Data.get("dateOfBirth").getAsString() : "";
          PersonalInfo.addProperty("dateOfBirth", str);

          if (T24Data.has("communicationDevices")) {
            JsonObject emailObj = T24Data.get("communicationDevices").getAsJsonArray().get(0).getAsJsonObject();
            if (emailObj.has("email")) {
              PersonalInfo.addProperty("email", emailObj.get("email").getAsString());
            } else
              PersonalInfo.addProperty("email", "");
          } else {
            PersonalInfo.addProperty("email", "");
          }

          JsonArray IndentityJsonArray = new JsonArray();
          JsonObject IdentityInfoObj = new JsonObject();
          if (T24Data.has("legalDetails")) {
            JsonArray partyIdentifiersArray = T24Data.get("legalDetails").getAsJsonArray();
            for (int i = 0; i < partyIdentifiersArray.size(); i++) {
              JsonObject partyIdentifiersArrayObj = partyIdentifiersArray.get(i).getAsJsonObject();
              str = partyIdentifiersArrayObj.has("legalDocumentName") ? partyIdentifiersArrayObj.get("legalDocumentName").getAsString() : "";
              IdentityInfoObj.addProperty("identityType", str);
              str = partyIdentifiersArrayObj.has("legalId") ? partyIdentifiersArrayObj.get("legalId").getAsString() : "";
              IdentityInfoObj.addProperty("identifierNumber", str);
              str = partyIdentifiersArrayObj.has("legalIssueDate") ? partyIdentifiersArrayObj.get("legalIssueDate").getAsString() : "";
              IdentityInfoObj.addProperty("issuedDate", str);
              str = partyIdentifiersArrayObj.has("legalExpiredDate") ? partyIdentifiersArrayObj.get("legalExpiredDate").getAsString() : "";
              IdentityInfoObj.addProperty("expiryDate", str);
              IdentityInfoObj.addProperty("issuingCountry", "");
              IdentityInfoObj.addProperty("issuingState", "");
            }
          }
          IndentityJsonArray.add(IdentityInfoObj);       
   
          
          JsonArray EmploymentJsonArray = new JsonArray();
          JsonArray addressJsonArray = new JsonArray();
          JsonObject EmploymentObj = new JsonObject();
          if (T24Data.has("employDetails")) {
            JsonArray employmentArray = T24Data.get("employDetails").getAsJsonArray();
            for (int i = 0; i < employmentArray.size(); i++) {
              JsonObject EmploymentArrayObj = employmentArray.get(i).getAsJsonObject();

              str = EmploymentArrayObj.has("employStatus") ? EmploymentArrayObj.get("employStatus").getAsString() : "";
              EmploymentObj.addProperty("type", str);
              str = EmploymentArrayObj.has("incomeFrequency") ? EmploymentArrayObj.get("incomeFrequency").getAsString() : "";
              EmploymentObj.addProperty("salaryFrequency", str);
              str = EmploymentArrayObj.has("salaryAmount") ? EmploymentArrayObj.get("salaryAmount").getAsString() : "";
              EmploymentObj.addProperty("salary", str);
              str = EmploymentArrayObj.has("employerName") ? EmploymentArrayObj.get("employerName").getAsString() : "";
              EmploymentObj.addProperty("employerName", str);
              str = EmploymentArrayObj.has("occupation") ? EmploymentArrayObj.get("occupation").getAsString() : "";
              EmploymentObj.addProperty("occupationType", str);
              EmploymentObj.addProperty("hoursPerWeek", "");
              EmploymentObj.addProperty("montlyIncome", "");

              if (EmploymentArrayObj.has("address")) {
                JsonObject AddressObject = new JsonObject();
                String address = "";
                JsonArray addressArray = EmploymentArrayObj.get("address").getAsJsonArray();
                for (int j = 0; j < addressArray.size(); j++) {
                  if (j == 0) {
                    address = "buildingName";
                    str = addressArray.get(j).getAsJsonObject().has("address") ? addressArray.get(j).getAsJsonObject().get("address").getAsString() : "";
                    AddressObject.addProperty("streetName", str);
                  }
                  if (j == 1)
                    address = "town";
                  if (j == 2)
                    address = "state";
                  if (j == 3)
                    address = "country";
                  if (j == 4)
                    address = "postalOrZipCode";
                  str = addressArray.get(j).getAsJsonObject().has("address") ? addressArray.get(j).getAsJsonObject().get("address").getAsString() : "";
                  AddressObject.addProperty(address, str);
                }
                AddressObject.addProperty("addressType", "Office");
                addressJsonArray.add(AddressObject);
              }

            }
          } else {
            addressJsonArray.add(new JsonObject());
          }

          
          if (T24Data.has("contactTypes")) {
            JsonArray addressesArray = T24Data.get("contactTypes").getAsJsonArray();
            for (int i = 0; i < addressesArray.size(); i++) {
              JsonObject AddressObj = addressesArray.get(i).getAsJsonObject();

              if (AddressObj.get("contactType") != null && AddressObj.get("contactType").getAsString().equals("MOBILE")) {
                str = AddressObj.has("countryCode") ? AddressObj.get("countryCode").getAsString() : "";
                PersonalInfo.addProperty("countryCode", str);
                str = AddressObj.has("contactData") ? AddressObj.get("contactData").getAsString() : "";
                PersonalInfo.addProperty("phoneNo", str);
              }
              if (AddressObj.get("contactType") != null && AddressObj.get("contactType").getAsString().equals("OFFICE")) {
                JsonObject AddressObject = new JsonObject();
                str = AddressObj.has("contactData") ? AddressObj.get("contactData").getAsString() : "";
                EmploymentObj.addProperty("employerOfficePhone", str);
                str = AddressObj.has("countryCode") ? AddressObj.get("countryCode").getAsString() : "";
                EmploymentObj.addProperty("employerCountryCode", str);
              }
            }
          } else {
        	  PersonalInfo.addProperty("countryCode", "");
        	  PersonalInfo.addProperty("phoneNo", "");
        	  EmploymentObj.addProperty("employerOfficePhone", "");
        	  EmploymentObj.addProperty("employercountryCode", "");
          }

          EmploymentJsonArray.add(EmploymentObj);
          T24Info.addProperty("PersonalInfo", PersonalInfo.toString());
          T24Info.addProperty("Identity", IndentityJsonArray.toString());
          T24Info.addProperty("Employment", EmploymentJsonArray.toString());
          T24Info.addProperty("Addresses", addressJsonArray.toString());
          result = JSONToResult.convert(T24Info.toString());

        }
      } else {
        if (T24Data.has("message")) {
          result.addStringParam("dbpErrMsg", "Data fetching failed");
        }

        if (T24Data.has("status")) {
          result.addStringParam("dbpErrMsg", "Data fetching failed");
        }
      }

    } catch (Exception e) {
      alert.prepareError("Caught exception while getting Customer: ", e).log();
      return result;
    }

    return result;
  }
}