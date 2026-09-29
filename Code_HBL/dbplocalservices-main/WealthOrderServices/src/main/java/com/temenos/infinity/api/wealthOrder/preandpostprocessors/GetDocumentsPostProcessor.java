/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;

/**
 * (INFO) Builds the Result in the desired format for the Refinitiv service.
 * 
 * @author himaja.sridhar
 *
 */
public class GetDocumentsPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			JSONObject resultObj = new JSONObject();
			JSONObject responseJSON = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			if (resultObj != null && resultObj.length() > 0) {
				JSONArray refnitivArray = resultObj.getJSONArray("document");

				if (refnitivArray != null && refnitivArray.length() > 0) {

					for (int i = 0; i < refnitivArray.length(); i++) {

						JSONObject refnitivObj = refnitivArray.getJSONObject(i);
						JSONObject finalresponse = new JSONObject();

						finalresponse.put("link", refnitivObj.has("hubLink") ? refnitivObj.get("hubLink") : "");
						finalresponse.put("mediaType",
								refnitivObj.has("mediaType") ? refnitivObj.get("mediaType") : "");
						finalresponse.put("type",
								refnitivObj.has("detailedDocumentType") ? refnitivObj.get("detailedDocumentType") : "");
						jsonArray.put(finalresponse);

					}
				}
			}
			responseJSON.put("opstatus", "0");
			responseJSON.put("httpStatusCode", "200");
			responseJSON.put("documentDetails", jsonArray);
			return Utilities.constructResultFromJSONObject(responseJSON);

		} catch (Exception e) {
			alert.prepareError("Error while invoking Doc Refinitiv - " + WealthAPIServices.WEALTH_GETDOCUMENTS.getOperationName()
					+ "  : " + e).log();
			return result;
		}
	}

}
