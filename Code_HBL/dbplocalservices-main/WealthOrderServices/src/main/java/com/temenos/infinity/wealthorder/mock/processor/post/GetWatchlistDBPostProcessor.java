package com.temenos.infinity.wealthorder.mock.processor.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.infinity.dbx.temenos.constants.TemenosConstants;

public class GetWatchlistDBPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
				diagnostic.prepareDebug("==========> GetWatchlistDBPostProcessor Mock - Entered").log();
		// TODO Auto-generated method stub
		Map<String, String> input = new HashMap<>();
		String filter = null;
		JSONObject resultJson = new JSONObject();
		Result finalResult = new Result();

		filter = "userId" + DBPUtilitiesConstants.EQUAL + request.getParameter(TemenosConstants.USER_ID);

		input.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			result = HelperMethods.callGetApi(request, input.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(request), URLConstants.WEALTH_USER_FAVORITES_GET);
			if (HelperMethods.hasRecords(result)) {
				List<Dataset> dataset = result.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
				// Splitted to instrumentids
				String favInstrumentIds = resultJson.get("favInstrumentIds").toString();
				if (favInstrumentIds != null && favInstrumentIds.length() > 0) {
					String favInstrumentIdsArr[] = favInstrumentIds.trim().split("@");
					String instrumentId = "";
					for (String s : favInstrumentIdsArr) {
						instrumentId = instrumentId + s.trim().split("~")[0].trim() + " ";
					}
					resultJson.put("favInstrumentIds", instrumentId.trim().replace(" ", "@"));
					resultJson.put("instr_app", favInstrumentIds);
					resultJson.put("instrumentId", instrumentId.trim());
					resultJson.put("T24Favourite", "true");
					resultJson.put("T24Instrumentids", instrumentId.trim());
				}
			}

			resultJson.put("opstatus", result.getOpstatusParamValue());
			resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
			finalResult = Utilities.constructResultFromJSONObject(resultJson);
		} catch (Exception e) {
			alert.prepareError("==========> GetWatchlistDBPostProcessor Mock - Error: " + e.getMessage()).log();

		}
		diagnostic.prepareDebug("==========> GetWatchlistDBPostProcessor Mock - Exiting with success").log();
		return finalResult;
	}

}
