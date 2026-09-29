/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

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

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentMinimalOrchPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		if (request.getParameter("T24Favourite") != null) {
			JSONObject instrumentJSON = new JSONObject();
			JSONArray finalArr = new JSONArray();
			Dataset instrumentMinimalds = result.getDatasetById("instrumentMinimal");
			Dataset instrumentMinimalDXds = result.getDatasetById("instrumentMinimalDX");
			Dataset instrumentMinimalDXMasterds = result.getDatasetById("instrumentMinimalDXMaster");

			if (instrumentMinimalds != null) {
				JSONObject instrumentMinimalObj = new JSONObject();
				instrumentMinimalObj.put("Field", Utilities
						.convertStringToJSONArray(ResultToJSON.convertDataset(instrumentMinimalds).toString()));
				JSONArray instrumentMinimalArray = instrumentMinimalObj.getJSONArray("Field");
				if (instrumentMinimalArray != null && instrumentMinimalArray.length() > 0) {
					for (int i = 0; i < instrumentMinimalArray.length(); i++) {
						JSONObject instMinimalobj = instrumentMinimalArray.getJSONObject(i);
						finalArr.put(instMinimalobj);
					}
				}
			}
			if (instrumentMinimalDXds != null) {
				JSONObject instrumentMinimalObj = new JSONObject();
				instrumentMinimalObj.put("Field", Utilities
						.convertStringToJSONArray(ResultToJSON.convertDataset(instrumentMinimalDXds).toString()));
				JSONArray instrumentMinimalArray = instrumentMinimalObj.getJSONArray("Field");
				if (instrumentMinimalArray != null && instrumentMinimalArray.length() > 0) {
					for (int i = 0; i < instrumentMinimalArray.length(); i++) {
						JSONObject instMinimalobj = instrumentMinimalArray.getJSONObject(i);
						finalArr.put(instMinimalobj);
					}
				}
			}
			if (instrumentMinimalDXMasterds != null) {
				JSONObject instrumentMinimalObj = new JSONObject();
				instrumentMinimalObj.put("Field", Utilities
						.convertStringToJSONArray(ResultToJSON.convertDataset(instrumentMinimalDXMasterds).toString()));
				JSONArray instrumentMinimalArray = instrumentMinimalObj.getJSONArray("Field");
				if (instrumentMinimalArray != null && instrumentMinimalArray.length() > 0) {
					for (int i = 0; i < instrumentMinimalArray.length(); i++) {
						JSONObject instMinimalobj = instrumentMinimalArray.getJSONObject(i);
						finalArr.put(instMinimalobj);
					}
				}
			}

			instrumentJSON.put("instrumentMinimal", finalArr);

			Result instrRes = Utilities.constructResultFromJSONObject(instrumentJSON);
			instrRes.addOpstatusParam("0");
			instrRes.addHttpStatusCodeParam("200");
			instrRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return instrRes;

		} else {
			Dataset ds = result.getDatasetById("instrumentMinimal");
			Record rd = result.getRecordById("instrumentAssets");
			if (ds != null) {
				if (result.getDatasetById("instrumentMinimal").getAllRecords().isEmpty()) {
					String instrumentName = result.getRecordById("instrumentAssets").getRecordById("assetTypes")
							.getParamValueByName("underlying");
					Record record = new Record();
					record.addParam("instrumentName", instrumentName);

					String[] portfolioIdArr = request.getParameterValues(TemenosConstants.INSTRUMENTID);
					if (portfolioIdArr != null && portfolioIdArr.length > 0) {
						String instrumentId = portfolioIdArr[0].trim();
						record.addParam("instrumentId", instrumentId);
					}

					result.getDatasetById("instrumentMinimal").addRecord(record);

					return result;
				}
			} else if (ds == null && rd != null) {
				String instrumentName = result.getRecordById("instrumentAssets").getRecordById("assetTypes")
						.getParamValueByName("underlying");
				JSONObject instMinimalobj = new JSONObject();
				JSONObject instrumentJSON = new JSONObject();
				JSONArray finalArr = new JSONArray();
				instMinimalobj.put("instrumentName", instrumentName);

				String[] portfolioIdArr = request.getParameterValues(TemenosConstants.INSTRUMENTID);
				if (portfolioIdArr != null && portfolioIdArr.length > 0) {
					String instrumentId = portfolioIdArr[0].trim();
					instMinimalobj.put("instrumentId", instrumentId);
				}
				finalArr.put(instMinimalobj);
				instrumentJSON.put("instrumentMinimal", finalArr);
				Result instrRes = Utilities.constructResultFromJSONObject(instrumentJSON);
				instrRes.addOpstatusParam("0");
				instrRes.addHttpStatusCodeParam("200");
				instrRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(instrRes);
				return result;
			}
		}
		return result;
	}

}
