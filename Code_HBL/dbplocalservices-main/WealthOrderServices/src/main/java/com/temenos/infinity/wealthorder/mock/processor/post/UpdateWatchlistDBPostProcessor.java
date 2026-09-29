package com.temenos.infinity.wealthorder.mock.processor.post;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

public class UpdateWatchlistDBPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		diagnostic.prepareDebug("==========> UpdateWatchlistDBPostProcessor Mock - Entered").log();
		Result result1 = new Result();
		Result finalResult = null;
		Map<String, Object> inputMap = new HashMap<>();
		// String operation = null;
		String userId = HelperMethods.getUserIdFromSession(request);

		result = OrderServiceUtils.getFavouriteInstruments(userId, request);
		if (HelperMethods.hasRecords(result)) {

			Record existingRecord = result.getAllDatasets().get(0).getAllRecords().get(0);

			String id = HelperMethods.getFieldValue(existingRecord, "id");

			String favInstrumentCodes = HelperMethods.getFieldValue(existingRecord,
					TemenosConstants.USERFAVORITESCODES);
			String favInstrumentCodesArr[] = favInstrumentCodes.trim().split("@");
			String favInstrumentIds = HelperMethods.getFieldValue(existingRecord, TemenosConstants.USERFAVORITESIDS);
			if (request.getParameter(TemenosConstants.OPERATION).equalsIgnoreCase("Add")) {

				if (!request.getParameter(TemenosConstants.RICCODE).equals("") && (!Arrays.asList(favInstrumentCodesArr)
						.contains(request.getParameter(TemenosConstants.RICCODE))
						|| request.getParameter(TemenosConstants.RICCODE).equalsIgnoreCase("RICCode"))) {
					if (favInstrumentCodes.equals("")) {
						favInstrumentCodes = request.getParameter(TemenosConstants.RICCODE);

					} else {
						favInstrumentCodes = favInstrumentCodes + "@" + request.getParameter(TemenosConstants.RICCODE);
					}
				}
				if (!request.getParameter(TemenosConstants.INSTRUMENTID).equals("")
						&& !favInstrumentIds.contains(request.getParameter(TemenosConstants.INSTRUMENTID))) {
					if (favInstrumentIds.equals("")) {
						favInstrumentIds = request.getParameter(TemenosConstants.INSTRUMENTID);
					} else {
						favInstrumentIds = favInstrumentIds + "@" + request.getParameter(TemenosConstants.INSTRUMENTID);
					}
				}
			}
			List<String> ricList = Arrays.asList(favInstrumentCodesArr);
			if (request.getParameter(TemenosConstants.OPERATION).equalsIgnoreCase("Remove")
					&& (!request.getParameter(TemenosConstants.RICCODE).equals(""))) {
				if (ricList.contains(request.getParameter(TemenosConstants.RICCODE))) {
					int index = ricList.indexOf(request.getParameter(TemenosConstants.RICCODE));
					favInstrumentCodesArr = ArrayUtils.remove(favInstrumentCodesArr, index);
					String removedList = StringUtils.join(favInstrumentCodesArr, "@");
					favInstrumentCodes = removedList;
				}

			}
			if (request.getParameter(TemenosConstants.OPERATION).equalsIgnoreCase("Remove")
					&& (!request.getParameter(TemenosConstants.INSTRUMENTID).equals(""))) {
				String strToRemove1 = "@" + request.getParameter(TemenosConstants.INSTRUMENTID);
				String strToRemove2 = request.getParameter(TemenosConstants.INSTRUMENTID) + "@";
				favInstrumentIds = favInstrumentIds.replace(strToRemove1, "");
				favInstrumentIds = favInstrumentIds.replace(strToRemove2, "");
				favInstrumentIds = favInstrumentIds.replace(request.getParameter(TemenosConstants.INSTRUMENTID), "");
			}
			inputMap.put("id", id);
			inputMap.put(TemenosConstants.USERFAVORITESCODES, favInstrumentCodes);
			inputMap.put(TemenosConstants.USERFAVORITESIDS, favInstrumentIds);
			result1 = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
					URLConstants.WEALTH_USER_FAVORITES_UPDATE);
			List<Dataset> dataset = result1.getAllDatasets();
			List<Record> drecords = dataset.get(0).getAllRecords();
			JSONObject resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
			resultJson.put("opstatus", result.getOpstatusParamValue());
			resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
			finalResult = Utilities.constructResultFromJSONObject(resultJson);
			finalResult = OrderServiceUtils.splitfavInstrumentIds(finalResult);
			finalResult.addParam("status", "Success");
			finalResult.addParam("msg", "User favourites updated successfully");
			diagnostic.prepareDebug("==========> UpdateWatchlistDBPostProcessor Mock - Entering with success").log();
			return finalResult;

		} else {
			if (request.getParameter(TemenosConstants.OPERATION).toString().equalsIgnoreCase("Add")) {
				result1 = OrderServiceUtils.createFavouriteInstruments(request, response);
				List<Dataset> dataset = result1.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				JSONObject resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
				resultJson.put("opstatus", result.getOpstatusParamValue());
				resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
				finalResult = Utilities.constructResultFromJSONObject(resultJson);
				finalResult = OrderServiceUtils.splitfavInstrumentIds(finalResult);
				finalResult.addParam("status", "Success");
				finalResult.addParam("msg", "User favourites updated successfully");
				diagnostic.prepareDebug("==========> UpdateWatchlistDBPostProcessor Mock - Entering with success").log();
				return finalResult;
			} else {
				JSONObject resultJson = new JSONObject();
				finalResult = Utilities.constructResultFromJSONObject(resultJson);
				finalResult.addParam("status", "Success");
				finalResult.addParam("msg", "Instrument was not in Favourites");
				diagnostic.prepareDebug("==========> UpdateWatchlistDBPostProcessor Mock - Exiting with msg").log();
				return finalResult;
			}
		}
	}

}
