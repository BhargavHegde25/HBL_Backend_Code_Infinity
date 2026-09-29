/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */
public class GetSearchInstrumentListOrchPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			// String instrumentIds = getUserFavorites(request);
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

			return result;
		} catch (Exception e) {

			alert.prepareError("Error while invoking GetInstrumentTotalPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
			return null;
		}

	}

	public String getUserFavorites(DataControllerRequest request) throws ApplicationException {
		Map<String, String> input = new HashMap<>();
		String userId = HelperMethods.getUserIdFromSession(request);
		Result result = new Result();
		Result finalResult = new Result();
		JSONObject resultJson = new JSONObject();
		String filter = "userId" + DBPUtilitiesConstants.EQUAL + userId;
		input.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			result = HelperMethods.callGetApi(request, input.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(request), URLConstants.WEALTH_USER_FAVORITES_GET);
			if (HelperMethods.hasRecords(result)) {
				List<Dataset> dataset = result.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
			}
			resultJson.put("opstatus", result.getOpstatusParamValue());
			resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
			finalResult = Utilities.constructResultFromJSONObject(resultJson);

		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the field order from backend delegate :" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21001);
		}
		String instrumentIds = finalResult.getParamValueByName(TemenosConstants.USERFAVORITESIDS);
		return instrumentIds;

	}
}
