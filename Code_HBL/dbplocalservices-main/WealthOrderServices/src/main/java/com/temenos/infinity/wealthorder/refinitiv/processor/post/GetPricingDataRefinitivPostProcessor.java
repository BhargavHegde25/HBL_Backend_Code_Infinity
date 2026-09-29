/**
 * 
 */
package com.temenos.infinity.wealthorder.refinitiv.processor.post;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;

/**
 * @author himaja.sridhar
 *
 */
public class GetPricingDataRefinitivPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetPricingDataRefinitivPostProcessor Refinitiv - Entered ").log();
			List<Param> dParams = result.getAllParams();
			dParams.get(0).getObjectValue().toString();
			Utilities.convertStringToJSONArray(dParams.get(0).getObjectValue().toString());
			JSONObject resultObj = new JSONObject();
			resultObj.put("Field", Utilities.convertStringToJSONArray(dParams.get(0).getObjectValue().toString()));
			JSONArray refnitivArray = resultObj.getJSONArray("Field");
			if (refnitivArray != null && refnitivArray.length() > 0) {
				JSONObject responseJSON = new JSONObject();
				JSONObject finalresponse = new JSONObject();
				for (int i = 0; i < refnitivArray.length(); i++) {
					JSONObject refnitivObj = refnitivArray.getJSONObject(i);
					if (!refnitivObj.get("Name").toString().equalsIgnoreCase("PRCTCK_1")) {

						if (refnitivObj.get("Name").toString().equalsIgnoreCase("ISIN_CODE")) {

							finalresponse.put("ISINCode",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_NAME")) {

							finalresponse.put("instrumentName",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_EXCHNG")) {

							finalresponse.put("stockExchange",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_CURRENCY")) {

							finalresponse.put("referenceCurrency",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("TRDPRC_1")) {

							finalresponse.put("marketPrice",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_NETCHNG")) {

							finalresponse.put("netchange",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("PCTCHNG")) {
							DecimalFormat df = new DecimalFormat("0.00");

							double perValue = Double.parseDouble(refnitivObj.has(refnitivObj.get("DataType").toString())
									? refnitivObj.get(refnitivObj.get("DataType").toString()).toString()
									: "0.00");

							finalresponse.put("percentageChange", df.format(perValue));

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_DATE")) {
							if (refnitivObj.has(refnitivObj.get("DataType").toString())) {
								String dateRecStr = refnitivObj.get(refnitivObj.get("DataType").toString()).toString();
								try {
									SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
									Date date = formatter.parse(dateRecStr);
									String formattedDate = formatter.format(date);
									finalresponse.put("dateReceived", formattedDate);
								} catch (Exception e) {
									finalresponse.put("dateReceived", dateRecStr);
								}
							} else {
								finalresponse.put("dateReceived", "00:00:00");
							}

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_TIME")) {

							finalresponse.put("timeReceived",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "00:00:00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_BID")) {

							finalresponse.put("bidRate",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("BIDSIZE")) {

							finalresponse.put("bidVolume",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_ASK")) {

							finalresponse.put("askRate",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("ASKSIZE")) {

							finalresponse.put("askVolume",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_OPEN")) {

							finalresponse.put("openRate",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_CLOSE")) {

							finalresponse.put("closeRate",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_VOLUME")) {

							finalresponse.put("volume",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("52WK_HIGH")) {

							finalresponse.put("high52W",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("52WK_LOW")) {

							finalresponse.put("low52W",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_LAST")) {

							finalresponse.put("latestRate",
									refnitivObj.has(refnitivObj.get("DataType").toString())
											? refnitivObj.get(refnitivObj.get("DataType").toString())
											: "0.00");

						}
					}
				}
				responseJSON.put(request.getParameter("objName"), finalresponse.toString());
				responseJSON.put("opstatus", "0");
				responseJSON.put("httpStatusCode", "200");
				diagnostic.prepareDebug("==========> GetPricingDataRefinitivPostProcessor Refinitiv - Exiting with success ").log();
				return Utilities.constructResultFromJSONObject(responseJSON);
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetPricingDataRefinitivPostProcessor Refinitiv - Error: " + e.getMessage()).log();
			return result;
		}
		diagnostic.prepareDebug("==========> GetPricingDataRefinitivPostProcessor Refinitiv - Exiting with no data ").log();
		return result;
	}

}
