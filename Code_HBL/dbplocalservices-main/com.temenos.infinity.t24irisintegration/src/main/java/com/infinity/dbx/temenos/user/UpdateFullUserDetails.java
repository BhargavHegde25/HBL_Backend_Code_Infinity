package com.infinity.dbx.temenos.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateFullUserDetails implements JavaService2, AccountsConstants, TemenosConstants, UserConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			// Address details
			JsonArray addresses = new JsonParser().parse(request.getParameter("addresses")).getAsJsonArray();
			request.addRequestParam_("AddressLine1", addresses.get(0).getAsJsonObject().get("addrLine1").getAsString());
			request.addRequestParam_("AddressLine2", addresses.get(0).getAsJsonObject().get("addrLine2").getAsString());
			request.addRequestParam_("City", addresses.get(0).getAsJsonObject().get("City_id").getAsString());
			request.addRequestParam_("State", addresses.get(0).getAsJsonObject().get("Region_id").getAsString());
			request.addRequestParam_("ZipCode", addresses.get(0).getAsJsonObject().get("ZipCode").getAsString());
			request.addRequestParam_("Country", addresses.get(0).getAsJsonObject().get("countryCode").getAsString());

			// Phone number
			JsonArray phoneNumbers = new JsonParser().parse(request.getParameter("phoneNumbers")).getAsJsonArray();
			request.addRequestParam_("MobileCountryCode",
					phoneNumbers.get(0).getAsJsonObject().get("phoneCountryCode").getAsString());
			request.addRequestParam_("MobileNumber",
					phoneNumbers.get(0).getAsJsonObject().get("phoneNumber").getAsString());

			// Email
			JsonArray emailIds = new JsonParser().parse(request.getParameter("emailIds")).getAsJsonArray();
			request.addRequestParam_("Email", emailIds.get(0).getAsJsonObject().get("value").getAsString());

			// Date of birth
			request.addRequestParam_("dateOfBirth",
					TemenosUtils.convertDateFormat((String) request.getParameter("dateOfBirth"), MMDDYYYY, YYYYMMDD));

			// Identity details
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
			request.addRequestParam_("ExpiryDate", TemenosUtils.convertDateFormat(expiryDate, MMDDYYYY, YYYYMMDD));
			if (StringUtils.isEmpty(issueDate)) {
				String period = CommonUtils.getProperty(USER_PROPERTIES, "ISSUEDATE", "DEFAULT", "PERIOD");
				issueDate = LocalDate.now().minusDays(Integer.parseInt(period))
						.format(DateTimeFormatter.ofPattern(MMDDYYYY));
			}
			request.addRequestParam_("IssueDate", TemenosUtils.convertDateFormat(issueDate, MMDDYYYY, YYYYMMDD));

			result = CommonUtils.callIntegrationService(request, null, null, UserConstants.SERVICE_ID_ONBOARDING_USER,
					UserConstants.OP_UPDATE_FULL_USER_DETAILS, true);

		} catch (Exception exception) {
			alert.prepareError("Error in UpdateFullUserDetails:" + exception.toString()).log();
			result.addParam(new Param("dbpErrCode", "10213"));
			result.addParam(new Param("dbpErrMsg", "Update party failed"));
		}
		return result;
	}

}
