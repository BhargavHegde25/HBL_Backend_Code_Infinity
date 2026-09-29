/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class GetAllocationMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		diagnostic.prepareDebug("==========> GetAllocationMockPostProcessor Mock - Entered ").log();
		JSONObject responseVal = new JSONObject();
		JSONArray asset = new JSONArray();
		JSONArray region = new JSONArray();
		JSONArray currency = new JSONArray();

		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);

		String[] assetTitle = null, aValue = null, aWeight = null, assetTotal = null, acurrency = null,
				currencyTotal = null, rcurrency = null, ccurrency = null, regionTitle = null, rWeight = null,
				rValue = null, regionTotal = null, currencyTitle = null, cValue = null, cWeight = null;

		if (portfolioId.equalsIgnoreCase("100777-1")) {
			assetTitle = new String[] { "Cash", "Fund", "Share" };
			aValue = new String[] { "15328.61", "5631.12", "28612.70" };
			aWeight = new String[] { "30.92", "11.36", "57.72" };
			assetTotal = new String[] { "49572.43", "49572.43", "49572.43" };
			acurrency = new String[] { "USD", "USD", "USD" };

			regionTitle = new String[] { "EU", "US" };
			rValue = new String[] { "2987.70", "46584.73" };
			rWeight = new String[] { "6.03", "93.97" };
			regionTotal = new String[] { "49572.43", "49572.43" };
			rcurrency = new String[] { "USD", "USD", "USD" };

			currencyTitle = new String[] { "EUR", "USD" };
			cValue = new String[] { "2987.70", "46584.73" };
			cWeight = new String[] { "6.03", "93.97" };
			currencyTotal = new String[] { "49572.43", "49572.43" };
			ccurrency = new String[] { "USD", "USD", "USD" };

		} else if (portfolioId.equalsIgnoreCase("100777-2")) {

			assetTitle = new String[] { "Cash", "Share" };
			aValue = new String[] { "308.98", "16543.15" };
			aWeight = new String[] { "1.83", "98.17" };
			assetTotal = new String[] { "16852.13", "16852.13" };
			acurrency = new String[] { "USD", "USD" };

			regionTitle = new String[] { "US" };
			rWeight = new String[] { "100" };
			rValue = new String[] { "16852.13" };
			regionTotal = new String[] { "16852.13" };
			rcurrency = new String[] { "USD" };

			currencyTitle = new String[] { "USD" };
			cWeight = new String[] { "100" };
			cValue = new String[] { "16852.13" };
			currencyTotal = new String[] { "16852.13" };
			ccurrency = new String[] { "USD", "USD" };

		} else if (portfolioId.equalsIgnoreCase("100777-4")) {

			assetTitle = new String[] { "Cash", "Fund", "Share" };
			aValue = new String[] { "15328.61", "37050.00", "527102.20" };
			aWeight = new String[] { "2.65", "6.39", "90.96" };
			assetTotal = new String[] { "579480.81", "579480.81", "579480.81" };
			acurrency = new String[] { "USD", "USD", "USD" };

			regionTitle = new String[] { "Asia", "EU", "UK", "US" };
			rValue = new String[] { "24345", "22407.75", "38359.4", "494368.66" };
			rWeight = new String[] { "4.20", "3.87", "6.62", "85.31" };
			regionTotal = new String[] { "579480.81", "579480.81", "579480.81", "579480.81" };
			rcurrency = new String[] { "USD", "USD", "USD", "USD" };

			currencyTitle = new String[] { "EUR", "GBP", "HKD", "USD" };
			cValue = new String[] { "22407.75", "38359.4", "24345", "494368.66" };
			cWeight = new String[] { "3.87", "6.62", "4.20", "85.31" };
			currencyTotal = new String[] { "579480.81", "579480.81", "579480.81", "579480.81" };
			ccurrency = new String[] { "USD", "USD", "USD", "USD" };

		} else if (portfolioId.equalsIgnoreCase("100777-5")) {

			assetTitle = new String[] { "Cash", "Share" };
			aValue = new String[] { "308.98", "278449.43" };
			aWeight = new String[] { "0.11", "99.89" };
			assetTotal = new String[] { "278758.41", "278758.41" };
			acurrency = new String[] { "USD", "USD" };

			regionTitle = new String[] { "Asia", "EU", "UK", "US" };
			rValue = new String[] { "24345", "8554", "26478.3", "219381.11" };
			rWeight = new String[] { "8.73", "3.07", "9.50", "78.70" };
			regionTotal = new String[] { "278758.41", "278758.41", "278758.41", "278758.41" };
			rcurrency = new String[] { "USD", "USD", "USD", "USD" };

			currencyTitle = new String[] { "EUR", "GBP", "HKD", "USD" };
			cValue = new String[] { "8554", "26478.3", "24345", "219381.11" };
			cWeight = new String[] { "3.07", "9.50", "8.73", "78.70" };
			currencyTotal = new String[] { "278758.41", "278758.41", "278758.41", "278758.41" };
			ccurrency = new String[] { "USD", "USD", "USD", "USD" };
		} else {

			assetTitle = new String[] { "Fund", "Share" };
			aValue = new String[] { "531.12", "2612.70" };
			aWeight = new String[] { "6.44", "8.56" };
			assetTotal = new String[] { "343.82", "343.82" };
			acurrency = new String[] { "USD", "USD" };

			regionTitle = new String[] { "EU", "US" };
			rValue = new String[] { "27.70", "36.12" };
			rWeight = new String[] { "8.72", "1.28" };
			regionTotal = new String[] { "343.82", "343.82" };
			rcurrency = new String[] { "USD", "USD", "USD" };

			currencyTitle = new String[] { "EUR", "USD" };
			cValue = new String[] { "27.70", "356.12" };
			cWeight = new String[] { "8.72", "9.28" };
			currencyTotal = new String[] { "343.82", "343.82" };
			ccurrency = new String[] { "USD", "USD" };
		}

		for (int i = 0; i < assetTitle.length; i++) {
			JSONObject cashObj = new JSONObject();
			cashObj.put("totalValue", assetTotal[i]);
			cashObj.put("baseCurrency", acurrency[i]);
			cashObj.put("assetClass", assetTitle[i]);
			cashObj.put("valueByAssetClass", aValue[i]);
			cashObj.put("wieghtByAssetClass", aWeight[i]);
			asset.put(cashObj);
		}

		diagnostic.prepareDebug("==========> GetAllocationMockPostProcessor Mock -  No. of assets returned: "+asset.length()).log();
		for (int i = 0; i < regionTitle.length; i++) {
			JSONObject cashObj = new JSONObject();
			cashObj.put("totalValue", regionTotal[i]);
			cashObj.put("baseCurrency", rcurrency[i]);
			cashObj.put("region", regionTitle[i]);
			cashObj.put("valueByRegion", rValue[i]);
			cashObj.put("wieghtByRegion", rWeight[i]);
			region.put(cashObj);
		}

		diagnostic.prepareDebug("==========> GetAllocationMockPostProcessor Mock -  No. of regions returned: "+region.length()).log();

		for (int i = 0; i < currencyTitle.length; i++) {
			JSONObject cashObj = new JSONObject();
			cashObj.put("totalValue", currencyTotal[i]);
			cashObj.put("baseCurrency", ccurrency[i]);
			cashObj.put("sectorCurrency", currencyTitle[i]);
			cashObj.put("valueByCurrency", cValue[i]);
			cashObj.put("weightByCurrency", cWeight[i]);
			currency.put(cashObj);
		}

		diagnostic.prepareDebug("==========> GetAllocationMockPostProcessor Mock -  No. of currency returned: "+currency.length()).log();	
		responseVal.put("portfolioID", portfolioId);
		responseVal.put("httpStatusCode", "200");
		responseVal.put("asset", asset);
		responseVal.put("currency", currency);
		responseVal.put("region", region);
		responseVal.put("opstatus", "0");
		responseVal.put("httpStatusCode", "200");

		Result final_result = Utilities.constructResultFromJSONObject(responseVal);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetAllocationMockPostProcessor Mock - Exited").log();
		return final_result;

	}

}
