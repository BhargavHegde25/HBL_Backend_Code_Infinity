/**
 * 
 */
package com.temenos.infinity.wealth.t24.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;

/**
 * @author muthukumarv
 *
 */
public class GetBankDateT24PostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetBankDateT24PostProcessor T24 - Entered ").log();
			Record headerRec = result.getRecordById("header");
			Dataset bodyDataSet = result.getDatasetById("body");
			String bankDate = bodyDataSet.getRecord(0).getParamByName("bankDate").getValue();
			JSONObject responseJSON = new JSONObject();
			String statusVal = headerRec.getParamValueByName("status");
			if (null != statusVal && statusVal.equalsIgnoreCase("success") && null != bankDate && !bankDate.isEmpty()) {
				diagnostic.prepareDebug("==========> GetBankDateT24PostProcessor T24 - Status Success ").log();
				responseJSON.put("opstatus", "0");
				responseJSON.put("httpStatusCode", "200");
				responseJSON.put("bankDate", bankDate);
			} else {
				responseJSON.put("opstatus", "0");
				responseJSON.put("httpStatusCode", "200");
				responseJSON.put("bankDate", "");
			}
			diagnostic.prepareDebug("==========> GetBankDateT24PostProcessor T24 - Exited ").log();
			return Utilities.constructResultFromJSONObject(responseJSON);
		} catch (Exception e) {
			alert.prepareError("==========> GetBankDateT24PostProcessor T24 - Error: " + e.getMessage()).log();
		}

		return result;
	}

}
