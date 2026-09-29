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
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetT24CustomerDetails implements JavaService2, AccountsConstants, TemenosConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		String res = "";
		try {
			if (StringUtils.isBlank(request.getParameter("coreCustomerID"))) {
				result.addOpstatusParam(-1);
				result.addHttpStatusCodeParam(500);
				result.addErrMsgParam("Misssing input param coreCustomerID");
			}
			String coreCustomerId = request.getParameter("coreCustomerID");
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("coreCustomerID", coreCustomerId);
			//params.put("partyID", coreCustomerId);
			request.addRequestParam_(CUSTOMER_CORE_ID, coreCustomerId);
			//request.addRequestParam_("partyID", coreCustomerId);
			Authentication authentication = Authentication.getInstance();
			res = CommonUtils.callPassthroughIntegrationService(request, params, request.getHeaderMap(),
					UserConstants.SERVICE_ID_ONBOARDING_USER, UserConstants.OP_GET_T24_ORIGINATION_DATA, true);

		} catch (Exception e) {
			alert.prepareError("Error while invoking GetUserData", e).log();
			result.addParam(new Param("FailureReason", e.getMessage()));
			result.addParam(new Param("dbpErrCode", "20001", "int"));
			result.addParam(new Param("dbpErrMsg", "Internal Error", "string"));
		}

		try {

			JsonParser parser = new JsonParser();
			JsonObject T24Data = (JsonObject) parser.parse(res).getAsJsonObject().get("body").getAsJsonObject();
			JsonObject T24Header = (JsonObject) parser.parse(res).getAsJsonObject().get("header").getAsJsonObject();
			
			if (T24Header.has("id")) {
				if (!T24Data.toString().equals("{}")) {
					JsonObject T24Info = new JsonObject();
					JsonObject party = new JsonObject();
					JsonObject partyJsonObject = new JsonObject();
					JsonArray partyJsonArray = new JsonArray();
   					JsonArray EmploymentJsonArray = new JsonArray();
   					JsonArray occupationsJsonArray = new JsonArray();
   					JsonObject occupationsObj = new JsonObject();   					
   					String str = "";

   					partyJsonObject.addProperty("partyId", T24Header.get("id").getAsString());
   					
   					//Employment and Occupations
   					if (T24Data.has("employDetails")) {
   						JsonArray employmentArray = T24Data.get("employDetails").getAsJsonArray();
   						for (int i = 0; i < employmentArray.size(); i++) {
   							
   							JsonObject EmploymentObj = new JsonObject();
   							JsonObject EmploymentArrayObj = employmentArray.get(i).getAsJsonObject();

   							//Get occupation from first Employment object
   							if(i == 0) {
   							str = EmploymentArrayObj.has("occupation")
   									? EmploymentArrayObj.get("occupation").getAsString() : "";
   							occupationsObj.addProperty("occupationType", str);
   							}
   							
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
   								String addressKey = "";
   								JsonArray addressArray = EmploymentArrayObj.get("address").getAsJsonArray();
   								for (int j = 0; j < addressArray.size(); j++) {
   									if (j == 0)
   										addressKey = "buildingName";
   									if (j == 1)
   										addressKey = "streetName";
   									if (j == 2)
   										addressKey = "town";
   									if (j == 3)
   										addressKey = "postalOrZipCode";
   									if (j == 4)
   										addressKey = "countrySubdivision";
   									if (j == 5)
   										addressKey = "country";
   									
   									str = addressArray.get(j).getAsJsonObject().has("address")
   											? addressArray.get(j).getAsJsonObject().get("address").getAsString()
   											: "";
   								    //If AddressLine2 is stored as NA in Transact, make it empty
   									if(j == 1) {
   										if(StringUtils.equalsIgnoreCase(str, "NA"))
   											str = "";
   									}
   									EmploymentObj.addProperty(addressKey, str);
   								}
   								EmploymentObj.addProperty("addressType", "Office");
   							}
   							    EmploymentJsonArray.add(EmploymentObj);
   						}
   					}

   					//EmploymentJsonArray.add(EmploymentObj);
   					occupationsJsonArray.add(occupationsObj);
   					partyJsonObject.add("employments", EmploymentJsonArray);
   					partyJsonObject.add("occupations", occupationsJsonArray);
   					
   					partyJsonArray.add(partyJsonObject);
   					party.add("parties", partyJsonArray);

   					result = JSONToResult.convert(party.toString());			

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
			result.addStringParam("dbpErrMsg", "GetT24CustomerDetails failed");
			result.addOpstatusParam(-1);
			result.addHttpStatusCodeParam(500);
			return result;
		}

		return result;
	}

}
