package com.infinity.dbx.temenos.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.utils.ConvertJsonToResult;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetFullUserDetailsPostProcessor extends BasePostProcessor {

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		Record body = result.getRecordById("body");

		JsonObject records = new JsonObject();
		records.addProperty("dateOfBirth", body.getParamValueByName("dateOfBirth"));
		records.addProperty("FirstName",
				body.getDatasetById("customerNames").getRecord(0).getParamValueByName("customerName"));
		records.addProperty("LastName", body.getParamValueByName("lastName"));

		JsonObject contactNumbers = new JsonObject();
		contactNumbers.addProperty("phoneCountryCode",
				body.getDatasetById("contactTypes").getRecord(0).getParamValueByName("countryCode"));
		contactNumbers.addProperty("Value",
				body.getDatasetById("contactTypes").getRecord(0).getParamValueByName("contactData"));
		JsonArray contactNumbersArray = new JsonArray();
		contactNumbersArray.add(contactNumbers);
		records.add("ContactNumbers", contactNumbersArray);

		JsonObject email = new JsonObject();
		email.addProperty("Value",
				body.getDatasetById("communicationDevices").getRecord(0).getParamValueByName("email"));
		JsonArray emailArray = new JsonArray();
		emailArray.add(email);
		records.add("EmailIds", emailArray);

		JsonArray updatedResult = new JsonArray();
		updatedResult.add(records);

		JsonObject updatedResultObj = new JsonObject();
		updatedResultObj.add("user", updatedResult);
		Result newResult = ConvertJsonToResult.convert(updatedResultObj);
		newResult.addOpstatusParam(0);
		newResult.addHttpStatusCodeParam(200);
		return newResult;

	}
}
