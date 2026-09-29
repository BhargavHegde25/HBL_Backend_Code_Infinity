/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.post;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthservices.util.T24ErrorAndOverrideHandling;


/**
 * (INFO) Builds the Result in the desired format for the create Order services
 * 
 * @author himaja.sridhar
 *
 */
public class CreateMarketOrderPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		T24ErrorAndOverrideHandling errorHandlingutil = T24ErrorAndOverrideHandling.getInstance();
		if (errorHandlingutil.isErrorResult(result)) {
			return result;
		}
		Record headerRec = result.getRecordById("header");
		String statusVal = headerRec.getParamValueByName("status");
		JSONObject response1 = new JSONObject();
		if (statusVal.equals("success")) {

			Record body = result.getRecordById("body");
			Record customers = body.getDatasetById("customers").getAllRecords().get(0);
			String commission = customers.getParamValueByName("commission") != null
					? customers.getParamValueByName("commission").toString()
					: "0";
			String EBVFee = customers.getParamValueByName("EBVFee") != null
					? customers.getParamValueByName("EBVFee").toString()
					: "0";
			Dataset taxTypes = customers.getDatasetById("customerChargeTaxTypes");
			List<Record> drecords = taxTypes.getAllRecords();
			JSONArray bodyArray = new JSONArray();
			for (int j = 0; j < drecords.size(); j++) {
				JSONObject holdingsObj = new JSONObject();
				Record drecord = drecords.get(j);
				holdingsObj = CommonUtils.convertRecordToJSONObject(drecord);
				bodyArray.put(holdingsObj);
			}

			Double taxAmount = 0.0;
			for (int i = 0; i < bodyArray.length(); i++) {
				JSONObject jsonObject = bodyArray.getJSONObject(i);
				String amount = jsonObject.has("customerChargeTaxAmount")
						? jsonObject.get("customerChargeTaxAmount").toString()
						: "0";
				taxAmount = Double.parseDouble(amount) + taxAmount;
			}
			Double fees = Double.parseDouble(commission) + Double.parseDouble(EBVFee) + taxAmount;
			response1.put("id", headerRec.getParamValueByName("id"));
			response1.put("status", statusVal);
			response1.put("opstatus", "0");
			response1.put("fees", fees);
			response1.put("httpStatusCode", "200");
			if (result.getParamValueByName("messageDetails") == null) {

			} else {
				response1.put("messageDetails", result.getParamValueByName("messageDetails"));
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

		return Utilities.constructResultFromJSONObject(response1);
	}

}
