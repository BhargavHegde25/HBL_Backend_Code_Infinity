package com.infinity.dbx.temenos.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.product.utils.InfinityConstants;

public class UpdateSourceOfFundsOperation implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String customerId = inputParams.get(InfinityConstants.id);
			if (StringUtils.isBlank(customerId)) {
				ErrorCodeEnum.ERR_10212.setErrorCode(result, "CustomerId is empty");
				return result;
			}
			
			String partyId = fetchCifId(request,customerId);

			if (StringUtils.isBlank(partyId)) {
				ErrorCodeEnum.ERR_10212.setErrorCode(result, "PartyId is empty");
				return result;
			}
			String type = inputParams.get("type");
			String source = inputParams.get("source");
			String currency = inputParams.get("currency");
			String amount = inputParams.get("amount");
			String fundFrequency = inputParams.get("fundFrequency");
			Map<String, Object> headers = request.getHeaderMap();
			headers = HelperMethods.addMSJWTAuthHeader(request, headers, AuthConstants.PRE_LOGIN_FLOW);
			
			//Get existing asset liabilities
			JSONArray fundsSourceArray = new JSONArray();
			String dueDiligenceURL = URLFinder.getServerRuntimeProperty(UserConstants.DUE_DILIGENCE_MS_BASE_URL)
					+  UserConstants.SOURCE_OF_FUNDS_SERVICE_URL.trim().replace("{party_id}", partyId);
			DBXResult dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, dueDiligenceURL, null, headers);
			try {
				if (dbxResult.getResponse() != null) {
					fundsSourceArray = new JSONObject((String) dbxResult.getResponse()).getJSONArray("sourceOfFunds");
					
				}
			} catch (Exception e) {
				alert.prepareError("Caught exception while fetching T24 dueDiligence Data for update Source of funds", e).log();
				dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
			}
			Optional<JSONObject> existingFundSource = StreamSupport
					.stream(fundsSourceArray.spliterator(), true).map(item -> (JSONObject) item)
					.filter(item -> StringUtils.equals(item.optString("fundsType"), type))
					.findFirst();
			
			JSONObject fundsSource = new JSONObject();
			fundsSource.put("fundsType", type);
			fundsSource.put("declarationDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			fundsSource.put("fundsCurrency", currency);
			fundsSource.put("fundsAmount", amount);
			fundsSource.put("fundsFrequency", fundFrequency);
			fundsSource.put("fundsSource", source);
			
			boolean isUpdate = false;
			if(existingFundSource.isPresent()) {
				fundsSource.put("sourceOfFundsReference", existingFundSource.get().optString("sourceOfFundsReference"));
				isUpdate = true;
			}
			JSONObject fundsSources = new JSONObject();
			fundsSources.put("sourceOfFunds", new JSONArray().put(fundsSource));
			dbxResult = HTTPOperations.sendHttpRequest(
					isUpdate ? HTTPOperations.operations.PUT : HTTPOperations.operations.POST, dueDiligenceURL,
							fundsSources.toString(), headers);
			try {
				JsonObject jsonObject = new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
				if (!jsonObject.has("sourceOfFundsReference")) {
					if (jsonObject.has("message"))
						dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
					if (jsonObject.has("status"))
						dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
				} else {
					dbxResult.setResponse(jsonObject.get("sourceOfFundsReference").getAsString());
				}
			} catch (Exception e) {
				alert.prepareError("Caught exception while createFundsSource: ", e).log();
				dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
			}
			result.addParam("success", "success");
			result.addParam(DTOConstants.PARTYID, partyId);
			return result;
		} catch (Exception e) {
			ErrorCodeEnum.ERR_10213.setErrorCode(result);
		}
		return result;
	}
	
	private String fetchCifId(DataControllerRequest dcRequest, String customerId) throws HttpCallException {
		String cifId = "";
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
				+ DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + DTOConstants.T24;
		Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.BACKENDIDENTIFIER_GET);
		cifId = HelperMethods.getFieldValue(result, DTOConstants.BACKENDID);
		return cifId;
	}

}
