package com.infinity.dbx.temenos.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
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

public class UpdateAssetLiabilitiesOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	//AssetLiab Default values
	public static final String ASSETLIAB_DEFAULT_INTEREST_RATE = "0";
	public static final String ASSETLIAB_DEFAULT_PROVIDER_NAME = "";
	public static final String ASSETLIAB_DEFAULT_TERM_REMAINING = "0";
	
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
			String currency = inputParams.get("currency");
			String amount = inputParams.get("amount");
			Map<String, Object> headers = request.getHeaderMap();
			headers = HelperMethods.addMSJWTAuthHeader(request, headers, AuthConstants.PRE_LOGIN_FLOW);
			
			//Get existing asset liabilities

			JSONArray assetLiabArray = new JSONArray();
			String dueDiligenceURL = URLFinder.getServerRuntimeProperty(UserConstants.DUE_DILIGENCE_MS_BASE_URL)
					+ UserConstants.ASSET_LIABILITIES_SERVICE_URL.trim().replace("{party_id}", partyId);
			DBXResult dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, dueDiligenceURL, null, headers);
			try {
				if (dbxResult.getResponse() != null) {
					assetLiabArray = new JSONObject((String) dbxResult.getResponse()).getJSONArray("assetLiabDetails");
				}
			} catch (Exception e) {
				alert.prepareError("Caught exception while fetching T24 dueDiligence Data for Assest Liabilites details: ", e).log();
				dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
			}
			
			boolean recordExists = StreamSupport
					.stream(assetLiabArray.spliterator(), true).map(item -> (JSONObject) item)
					.filter(item -> StringUtils.equals(item.optString("type"), type))
					.filter(item -> StringUtils
							.equals(item.optString("asOfDate"), new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
					.count() > 0;
			
			JSONObject assetLiability = new JSONObject();
			assetLiability.put("type", type);
			assetLiability.put("asOfDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			assetLiability.put("currency", currency);
			assetLiability.put("amount", amount);
			assetLiability.put("interestRate", inputParams.get("interestRate") != null ? (String) inputParams.get("interestRate")
					: ASSETLIAB_DEFAULT_INTEREST_RATE);
			assetLiability.put("sourceOrProviderName", inputParams.get("sourceOrProviderName") != null
					? (String) inputParams.get("sourceOrProviderName")
					: ASSETLIAB_DEFAULT_PROVIDER_NAME);
			assetLiability.put("termRemaining", inputParams.get("termRemaining") != null ? (String) inputParams.get("termRemaining")
					: ASSETLIAB_DEFAULT_TERM_REMAINING);
			JSONObject assetLiabiltyDetails = new JSONObject();
			assetLiabiltyDetails.put("assetLiabDetails", new JSONArray().put(assetLiability));
			dbxResult = HTTPOperations.sendHttpRequest(
					recordExists ? HTTPOperations.operations.PUT : HTTPOperations.operations.POST, dueDiligenceURL,
					assetLiabiltyDetails.toString(), headers);

			try {
				JsonObject jsonObject = new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
				if (!jsonObject.has("partyId")) {
					if (jsonObject.has("message"))
						dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
					if (jsonObject.has("status"))
						dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
				} else {
					dbxResult.setResponse(jsonObject.get("partyId").getAsString());
				}
			} catch (Exception e) {
				alert.prepareError("Error in createAssetLiabiltyDetailsDetails : "
						+ e.toString()).log();
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
