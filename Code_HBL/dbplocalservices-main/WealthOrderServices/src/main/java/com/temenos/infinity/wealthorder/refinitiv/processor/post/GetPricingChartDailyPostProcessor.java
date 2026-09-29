/**
 * 
 */
package com.temenos.infinity.wealthorder.refinitiv.processor.post;

import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * (INFO) Returns the Result object constructed.
 * 
 * @author himaja.sridhar
 *
 */
public class GetPricingChartDailyPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetPricingChartDailyPostProcessor Refinitiv - Entered ").log();
		try {
			String errMsg = result.getParamValueByName("errmsg");
			if (errMsg != null && !errMsg.equals("")) {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.removeParamByName("errmsg");
				diagnostic.prepareDebug("==========> GetPricingChartDailyPostProcessor Refinitiv - Exiting with success").log();
				return result;
			} else {
				JSONObject historicalDataJSON = new JSONObject();
				List<Param> dParams = result.getAllParams();
				JSONArray historicalDataArr = new JSONArray();
				JSONArray dataSet = new JSONArray();
				historicalDataArr = Utilities.convertStringToJSONArray(dParams.get(0).getObjectValue().toString());

				if (historicalDataArr != null && historicalDataArr.length() > 0) {
				
					for (int i = 0; i < historicalDataArr.length(); i++)
					{
						if (historicalDataArr.getJSONObject(i) != null && historicalDataArr.getJSONObject(i).has(TemenosConstants.TIMESTAMP)
								&& historicalDataArr.getJSONObject(i).get(TemenosConstants.TIMESTAMP) != null
								&& historicalDataArr.getJSONObject(i).has(TemenosConstants.CLOSE) && historicalDataArr.getJSONObject(i).get(TemenosConstants.CLOSE) != null) {
						JSONObject jsonobj = new JSONObject();
						String timestamp = historicalDataArr.getJSONObject(i).get(TemenosConstants.TIMESTAMP).toString();
						String[] split = timestamp.split("\\+");
						jsonobj.put(TemenosConstants.TIMESTAMP, split[0]);
						jsonobj.put(TemenosConstants.CLOSE, historicalDataArr.getJSONObject(i).get(TemenosConstants.CLOSE));
						dataSet.put(jsonobj);
						}
					}
				}else {
					dataSet = new JSONArray();
					diagnostic.prepareDebug("==========> GetPricingChartDailyPostProcessor Refinitiv - with empty array").log();
				}

				historicalDataJSON.put("historicalData", dataSet.toString());
				Result historicalDataResult = Utilities.constructResultFromJSONObject(historicalDataJSON);
				historicalDataResult.addOpstatusParam("0");
				historicalDataResult.addHttpStatusCodeParam("200");
				historicalDataResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetPricingChartDailyPostProcessor Refinitiv -  Exiting with success ").log();
				return historicalDataResult;
			}
		} catch (Exception e) {

			diagnostic.prepareDebug("==========> GetPricingChartDailyPostProcessor Refinitiv - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> GetPricingChartDailyPostProcessor Refinitiv - Exiting with success ").log();
		return null;
	}

}
