/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.Arrays;
import java.util.HashMap;
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
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.HoldingsListBackendDelegateImpl;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;

/**
 * @author himaja.sridhar
 *
 */
public class GetHoldingsListPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		WealthMockUtil wealthMockUtil = new WealthMockUtil();
		HoldingsListBackendDelegateImpl holdingsListBackendDelegateImpl = new HoldingsListBackendDelegateImpl();
		String refCcy = "";
		Record headerRec = result.getRecordById("header");
		Record dataRec = headerRec.getRecordById("data");
		String statusVal = headerRec.getParamValueByName("status");
		if (statusVal.equals("success")) {
			HashMap<String, String> dataParams = new HashMap<String, String>();
			for (int k = 0; k < dataRec.getAllParams().size(); k++) {
				dataParams.put(dataRec.getAllParams().get(k).getName(), dataRec.getAllParams().get(k).getValue());
			}
			JSONObject holdingsJSON = new JSONObject(dataParams);

			Dataset bodyDataset = result.getDatasetById("body");

			List<Record> drecords = bodyDataset.getAllRecords();
			JSONArray bodyArray = new JSONArray();
			for (int j = 0; j < drecords.size(); j++) {
				JSONObject holdingsObj = new JSONObject();
				Record drecord = drecords.get(j);
				holdingsObj = CommonUtils.convertRecordToJSONObject(drecord);
				bodyArray.put(holdingsObj);
			}

			// String[] fieldsArray = new String[] { "application" };
			String[] colArray = new String[] { TemenosConstants.HOLDINGSID, "holdingsType",
					TemenosConstants.MARKETPRICE, "ISIN", TemenosConstants.MARKETVALPOS,
					TemenosConstants.WEIGHTPERCENTAGE, TemenosConstants.UNREALPLMKT, TemenosConstants.REGION,
					TemenosConstants.ASSESTCLASS, TemenosConstants.SECTOR, TemenosConstants.SECCCY,
					TemenosConstants.MARKETVALUE, TemenosConstants.COSTVALUE, TemenosConstants.UNREALIZEDPLPERCENTAGE,
					TemenosConstants.QUANTITY, TemenosConstants.COSTPRICE, TemenosConstants.DESCRIPTION,
					TemenosConstants.COSTVALUESECCCY, TemenosConstants.UNREALPLMKTSECCCY,
					TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY, TemenosConstants.SUBASSETCLASS,
					TemenosConstants.EXCHANGERATE, "accruedInterest", TemenosConstants.ISINEXCHANGE,
					TemenosConstants.MARKETVALUEINSECCCY };

			JSONArray holdingsArr = new JSONArray();
			for (int i = 0; i < bodyArray.length(); i++) {
				JSONObject instrumentObj = bodyArray.getJSONObject(i);
				if (instrumentObj.has("application") && ((instrumentObj.get("application").equals("SC")
						|| instrumentObj.get("application").equals("DX"))
						&& !instrumentObj.get("quantity").equals("0"))) {
					for (String key : colArray) {
						if (!instrumentObj.has(key)) {
							instrumentObj.put(key, "");
						}
						if (key.equalsIgnoreCase(TemenosConstants.ISINEXCHANGE)) {
							instrumentObj.put(key, ((!instrumentObj.get("ISIN").toString().equalsIgnoreCase(""))
									? ((!instrumentObj.get(TemenosConstants.HOLDINGS_TYPE).toString()
											.equalsIgnoreCase("")) ? (instrumentObj.get("ISIN").toString() + " | "
													+ instrumentObj.get(TemenosConstants.HOLDINGS_TYPE).toString())
													: instrumentObj.get("ISIN").toString())
									: ((!instrumentObj.get(TemenosConstants.HOLDINGS_TYPE).toString()
											.equalsIgnoreCase("")) ? instrumentObj.get(TemenosConstants.HOLDINGS_TYPE)
													: "")));
						}

						if (key.equalsIgnoreCase(TemenosConstants.MARKETPRICE)) {
							instrumentObj.put(key,
									(instrumentObj.get(TemenosConstants.MARKETPRICE) != null
											&& instrumentObj.get(TemenosConstants.MARKETPRICE).toString().length() > 0
													? instrumentObj.get(TemenosConstants.MARKETPRICE).toString()
													: "0"));
						}
						if (holdingsJSON.has(TemenosConstants.REFERENCECURRENCY)
								&& holdingsJSON.get(TemenosConstants.REFERENCECURRENCY) != null
								&& holdingsJSON.get(TemenosConstants.REFERENCECURRENCY).toString().length() > 0) {
							refCcy = holdingsJSON.get(TemenosConstants.REFERENCECURRENCY).toString();
						}
						instrumentObj.put("referenceCurrency", refCcy);
						instrumentObj.put("isSecurityAsset",
								instrumentObj.get("application").equals("SC") ? true : false);
					}
				} else {
					continue;
				}
				/*
				 * for (String s : fieldsArray) { instrumentObj.remove(s); }
				 */
				holdingsArr.put(instrumentObj);

			}

			String sortBy = request.getParameter("sortBy");
			String sortType = request.getParameter(TemenosConstants.SORTORDER);
			JSONArray sortedJSON = new JSONArray();
			sortedJSON = holdingsArr;
			if (sortBy != null) {
				sortedJSON = holdingsListBackendDelegateImpl.sortArray(sortedJSON, sortBy, sortType);
			} else {
			}

			String searchVal = request.getParameter("searchByInstrumentName");
			String searchID = request.getParameter("instrumentId");
			String search = (searchVal != null && searchVal.trim().length() > 0) ? searchVal : "";
			if (search.equals("") && searchID==null) {
			} else {
				String instrumentId = request.getParameter("instrumentId") != null
						? request.getParameter("instrumentId")
						: "";
				HoldingsListBackendDelegateImpl holdingsListBackendDelegate = new HoldingsListBackendDelegateImpl();
				sortedJSON = holdingsListBackendDelegate.returnSearch(sortedJSON, search, instrumentId);
			}

			int totalCount = 0;
			String limitVal = request.getParameter(TemenosConstants.PAGESIZE);
			String offsetVal = request.getParameter(TemenosConstants.PAGEOFFSET);
			int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
			int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;

			totalCount = sortedJSON.length();
			if (limit > 0 && offset >= 0) {
				sortedJSON = wealthMockUtil.pagination(sortedJSON, limit, offset);
			}

			String fieldValue = Arrays.toString(colArray).replace("[", "").replace("]", "");
			fieldValue = fieldValue.replace("accruedInterest", "");
			holdingsJSON.put("portfolioHoldings", sortedJSON);
			holdingsJSON.put("fieldstoDisplay", fieldValue);
			holdingsJSON.put("portfolioID", request.getParameter("portfolioId"));
			holdingsJSON.put("accountNumber", request.getParameter("portfolioId"));
			holdingsJSON.put(TemenosConstants.SORTBY, sortBy);
			holdingsJSON.put("totalCount", totalCount);

			Result holdingsRes = Utilities.constructResultFromJSONObject(holdingsJSON);
			holdingsRes.addOpstatusParam("0");
			holdingsRes.addHttpStatusCodeParam("200");
			holdingsRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);

			return holdingsRes;
		} else {
			JSONObject response1 = new JSONObject();
			Record errorRec = result.getRecordById("error");
			response1.put("errormessage", errorRec.getParamValueByName("message"));
			Result errorRes = Utilities.constructResultFromJSONObject(response1);
			return errorRes;
		}
	}
}
