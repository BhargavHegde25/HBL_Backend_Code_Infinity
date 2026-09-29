/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthservices.util.T24ErrorAndOverrideHandling;

/**
 * (INFO) Builds the Result in the desired format for the create Order services
 * 
 * @author balaji.kk
 *
 */
public class GetPeriodicFeeEstimatesPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			T24ErrorAndOverrideHandling errorHandlingutil = T24ErrorAndOverrideHandling.getInstance();
			if (errorHandlingutil.isErrorResult(result)) {
				return result;
			}
			Record headerRec = result.getRecordById("header");
			String statusVal = headerRec.getParamValueByName("status");
			JSONObject response1 = new JSONObject();
			if (statusVal.equals("success")) {
				Dataset body = result.getDatasetById("body");
				if (body != null) {
					JSONObject bodyObj = new JSONObject();
					JSONObject resultObj = new JSONObject();
					bodyObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(body).toString()));
					JSONArray feeArray = bodyObj.getJSONArray("Field");

					if (feeArray != null && feeArray.length() > 0) {
						JSONObject feesObj = new JSONObject();
						JSONObject fabricObj = feeArray.getJSONObject(0);

						ArrayList<String> colList = new ArrayList<String>(
								Arrays.asList(TemenosConstants.SAFEKEEPCHARGEINCHARGECURRENCY,
										TemenosConstants.SAFEKEEPCHARGEINTRADECURRENCY,
										TemenosConstants.ADVISORYFEESINCHARGECURRENCY,
										TemenosConstants.ADVISORYFEESINTRADECURRENCY,
										TemenosConstants.INDUCEMENTFEESINCHARGECURRENCY,
										TemenosConstants.INDUCEMENTFEESINTRADECURRENCY, TemenosConstants.TRADECURRENCY,
										TemenosConstants.CHARGECURRENCY));

						colList.forEach(key -> {
							if (fabricObj.has(key) && fabricObj.get(key) != null
									&& fabricObj.get(key).toString().length() > 0
									&& !fabricObj.get(key).toString().equalsIgnoreCase("0")) {
								feesObj.put(key, fabricObj.get(key));
							}
						});
						resultObj.put("feeDetails", feesObj);
					}
					Result res = Utilities.constructResultFromJSONObject(resultObj);
					res.addHttpStatusCodeParam("200");
					res.addOpstatusParam("0");
					res.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					return res;
				}

			} else {
				Record errorRec = result.getRecordById("error");
				Object obj = errorRec.getParam("errorDetails").getObjectValue();
				JSONArray errArr = PortfolioWealthUtils.objectToJSONArray(obj);
				JSONObject errObj = errArr.getJSONObject(0);
				response1.put("errormessage", errObj.get("message"));
				Result errorRes = Utilities.constructResultFromJSONObject(response1);
				return errorRes;
			}
		} catch (Exception e) {
			result.addOpstatusParam("1582");
			result.addHttpStatusCodeParam("0");
			result.addParam(TemenosConstants.STATUS, "Failure");
			return result;
		}
		return result;

	}

}
