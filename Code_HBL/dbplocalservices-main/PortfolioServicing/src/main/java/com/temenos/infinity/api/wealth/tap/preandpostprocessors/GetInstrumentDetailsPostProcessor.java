/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */
public class GetInstrumentDetailsPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			JSONObject instrumentMin = new JSONObject();
			JSONObject instrumentAssets = new JSONObject();
			JSONArray instrumentDet = new JSONArray();

			/*
			 * Record headerRec = result.getRecordById("header"); Record messagesRec =
			 * result.getRecordById("messages"); if (messagesRec != null) { String message =
			 * messagesRec.getParamValueByName("message"); Result errorRes = new Result();
			 * errorRes.addParam("errormessage", message); return errorRes; }
			 */
			
			String[] colArray = new String[] { TemenosConstants.STOCKEXCHANGE, TemenosConstants.INSTRUMENTNAME,
					"priceCurrency", "ISINCode", TemenosConstants.INSTRUMENTID, "id", "lastPrice","instrumentCurrencyId" };

			Record bodyRec = result.getRecordById("body");
			JSONObject instrumentDetails = new JSONObject();
			if(bodyRec != null) {
			instrumentDetails = ResultToJSON.convertRecord(bodyRec);
			for (String key : colArray)
				if (!instrumentDetails.has(key)) {
					instrumentDetails.put(key, "");
				}

			instrumentDetails.put("lastPriceDate", "");
			instrumentDetails.put("quantity", "");
			instrumentDetails.put("RICCode", "");
			//instrumentDetails.put("instrumentCurrencyId", "");

			JSONObject assetTypes = new JSONObject();
			assetTypes.put("country", "");
			assetTypes.put("region", "");
			assetTypes.put("sector", "");
			assetTypes.put("assetType", "");

			if (instrumentDetails.getString("nature").equalsIgnoreCase("Stock")
					|| instrumentDetails.getString("nature").equalsIgnoreCase("Fund Share")
					|| instrumentDetails.getString("nature").equalsIgnoreCase("Fixed Income")) {
				instrumentAssets.put("isSecurityAsset", "true");
			} else {
				instrumentAssets.put("isSecurityAsset", "false");
			}

			instrumentDetails.remove("nature");
			}
			else {
				instrumentAssets.put("isSecurityAsset", "");
				instrumentDetails.remove("RICCode");
			}
			

			instrumentDet.put(instrumentDetails);
			instrumentMin.put("instrumentMinimal", instrumentDet);
			// instrumentAssets.put("assetTypes", assetTypes);

			instrumentMin.put("instrumentAssets", instrumentAssets);

			Result instrumentRes = Utilities.constructResultFromJSONObject(instrumentMin);
			instrumentRes.addOpstatusParam("0");
			instrumentRes.addHttpStatusCodeParam("200");
			instrumentRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return instrumentRes;

		} catch (Exception e) {
			alert.prepareError("Error while invoking GetInstrumentDetailsPostProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return null;
	}

}
