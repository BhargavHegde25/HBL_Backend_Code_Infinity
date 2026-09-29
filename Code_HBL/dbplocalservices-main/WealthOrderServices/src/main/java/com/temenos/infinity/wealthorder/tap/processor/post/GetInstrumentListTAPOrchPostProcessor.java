/**
 * 
 */
package com.temenos.infinity.wealthorder.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */
public class GetInstrumentListTAPOrchPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetInstrumentListTAPOrchPostProcessor TAP - Entered ").log();
		try {

			Dataset ricSet = result.getDatasetById("RICSet");
			JSONArray ricArr = ResultToJSON.convertDataset(ricSet);

			Dataset searchSet = result.getDatasetById("instrumentList");
			JSONArray searchArr = ResultToJSON.convertDataset(searchSet);
			if (searchSet != null) {

				for (int i = 0; i < searchArr.length(); i++) {
					JSONObject searchJSON = searchArr.getJSONObject(i);
					for (int j = 0; j < ricArr.length(); j++) {
						JSONObject ricJSON = ricArr.getJSONObject(j);
						String searchID = searchJSON.getString("idCode");
						String ricID = ricJSON.getString("id");
						if (searchID.equalsIgnoreCase(ricID)) {
							result.getDatasetById("instrumentList").getRecord(i).removeParamByName("idCode");
							result.getDatasetById("instrumentList").getRecord(i).addParam("RICCode",
									ricJSON.get("RICCode").toString());
							result.removeParamByName("idCnt");
							result.removeParamByName("id");
							break;
						}
					}
				}
			} else {
				result.removeParamByName("idCnt");
				result.removeParamByName("id");
			}
			result.removeDatasetById("RICSet");
			diagnostic.prepareDebug("==========> GetInstrumentListTAPOrchPostProcessor TAP -  Exiting with success ").log();
			return result;
		} catch (Exception e) {
			alert.prepareError("==========> GetInstrumentListTAPOrchPostProcessor TAP - Error: " + e.getMessage()).log();
			return result;
		}

	}

}
