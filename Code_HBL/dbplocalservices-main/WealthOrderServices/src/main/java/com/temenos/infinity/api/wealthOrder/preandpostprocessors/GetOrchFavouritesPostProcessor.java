/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetOrchFavouritesPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		JSONObject responseJSON = new JSONObject();

		String errMsg = result.getParamValueByName("errmsg");
		if (errMsg != null && !errMsg.equals("")) {
			JSONObject favouriteObj = new JSONObject();
			favouriteObj.put("favoriteInstruments", new JSONArray());
			favouriteObj.put("totalCount", 0);
			Result res = Utilities.constructResultFromJSONObject(favouriteObj);
			res.addOpstatusParam("0");
			res.addHttpStatusCodeParam("200");
			res.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return res;
		}
		String[] searchArr = null, sortByArr = null, sortOrderArr = null, pageSizeArr = null, pageOffsetArr = null;
		String search = "", sortBy = "", sortOrder = "", pageSize = "", pageOffset = "", instr_app = "";
		int totalCount = 0, pageSizeVal = 0, pageOffsetVal = 0;
		searchArr = request.getParameterValues("searchByInstrumentName");
		sortByArr = request.getParameterValues("sortBy");
		sortOrderArr = request.getParameterValues("sortOrder");
		pageSizeArr = request.getParameterValues("pageSize");
		pageOffsetArr = request.getParameterValues("pageOffset");
		instr_app = request.getParameterValues("instr_app")[0];

		if (searchArr != null && searchArr.length > 0) {
			search = searchArr[0].trim();
		}
		if (sortByArr != null && sortByArr.length > 0) {
			sortBy = sortByArr[0].trim();
		}
		if (sortOrderArr != null && sortOrderArr.length > 0) {
			sortOrder = sortOrderArr[0].trim();
		}
		if (pageSizeArr != null && pageSizeArr.length > 0) {
			pageSize = pageSizeArr[0].trim();
		}
		if (pageOffsetArr != null && pageOffsetArr.length > 0) {
			pageOffset = pageOffsetArr[0].trim();
		}

		pageSizeVal = (pageSize != null && pageSize.trim().length() > 0) ? Integer.parseInt(pageSize) : 0;
		pageOffsetVal = (pageOffset != null && pageOffset.trim().length() > 0) ? Integer.parseInt(pageOffset) : 0;
		Dataset refDataset = result.getDatasetById("refinitivInstruments");
		Dataset t24Dataset = result.getDatasetById("instrumentMinimal");
		HashMap<String, JSONObject> t24List = getInstruments(t24Dataset, "instrumentId");
		HashMap<String, JSONObject> refList = getInstruments(refDataset, "RIC");

		JSONArray finalArray = new JSONArray();
		finalArray = buildWatchList(t24List, refList);
		if (search != null && !search.equals("")) {
			finalArray = returnSearch(finalArray, search);

		}
		totalCount = finalArray.length();
		JSONArray sortedFavoriteInstruments = getSortedJsonArray(finalArray, sortBy, sortOrder, pageSizeVal,
				pageOffsetVal);
		sortedFavoriteInstruments = t24_addapplicationids(sortedFavoriteInstruments, instr_app);
		responseJSON.put("favoriteInstruments", sortedFavoriteInstruments);
		responseJSON.put("totalCount", totalCount);
		responseJSON.put("opstatus", "0");
		responseJSON.put("httpStatusCode", "200");
		return Utilities.constructResultFromJSONObject(responseJSON);
	}

	public HashMap<String, JSONObject> getInstruments(Dataset ds, String id) {
		HashMap<String, JSONObject> instrumentMap = new HashMap<>();

		if (ds == null) {
			return null;
		}

		List<Record> instrumentList = ds.getAllRecords();
		if (instrumentList == null || instrumentList.size() == 0) {
			return null;
		}

		for (int i = 0; i < instrumentList.size(); i++) {
			Record drecord = instrumentList.get(i);
			if (drecord != null) {
				JSONObject instrumentObj = CommonUtils.convertRecordToJSONObject(drecord);
				if (instrumentObj != null && instrumentObj.length() > 0) {
					String key = instrumentObj.getString(id);
					instrumentMap.put(key, instrumentObj);
				}
			}
		}
		return instrumentMap;
	}

	@SuppressWarnings("rawtypes")
	public JSONArray buildWatchList(HashMap<String, JSONObject> t24List, HashMap<String, JSONObject> refList) {
		JSONArray watchlist = new JSONArray();
		if (t24List != null) {
			for (Map.Entry instr : t24List.entrySet()) {
				JSONObject instrument = new JSONObject();
				instrument.put(TemenosConstants.INSTRUMENTID, instr.getKey());
				String strMap = instr.getValue().toString();
				JSONObject t24Obj = new JSONObject(strMap);
				instrument.put(TemenosConstants.INSTRUMENTNAME, t24Obj.getString(TemenosConstants.INSTRUMENTNAME));
				instrument.put(TemenosConstants.REFERENCECURRENCY, t24Obj.getString("instrumentCurrencyId"));
				String ricCode = t24Obj.getString("RICCode");
				instrument.put("RIC", ricCode);
				JSONObject refObj = null;
				if (refList != null) {
					refObj = refList.get(ricCode);
				}
				if (refObj == null) {
					instrument.put("lastRate", t24Obj.getString("marketPrice"));
					instrument.put("dateReceived", t24Obj.getString("dateReceived"));
					instrument.put("timeReceived", "0:00:00");
					instrument.put("netchange", "");
					instrument.put("percentageChange", "");
					instrument.put("bidRate", "");
					instrument.put("askRate", "");
					instrument.put("volume", "");
					instrument.put(TemenosConstants.ISIN, t24Obj.getString(TemenosConstants.ISIN));
					instrument.put("exchange", t24Obj.getString("stockExchange"));
				} else {

					if (refObj.has("marketPrice") && !refObj.get("marketPrice").equals("")
							&& Double.parseDouble(refObj.get("marketPrice").toString()) != 0) {
						instrument.put("lastRate", refObj.get("marketPrice").toString());
					} else if (refObj.has("marketPrice") && !refObj.get("marketPrice").equals("")
							&& Double.parseDouble(refObj.get("marketPrice").toString()) == 0) {
						if (refObj.has("closeValue") && !refObj.get("closeValue").equals("")
								&& Double.parseDouble(refObj.get("closeValue").toString()) != 0) {
							instrument.put("lastRate", refObj.get("closeValue").toString());
						} else {
							instrument.put("lastRate", t24Obj.getString("marketPrice"));
						}
					} else {
						instrument.put("lastRate", t24Obj.getString("marketPrice"));
					}

					if (refObj.has("dateReceived") && !refObj.getString("dateReceived").equals("")) {
						instrument.put("dateReceived", refObj.getString("dateReceived"));
					} else {
						instrument.put("dateReceived", t24Obj.getString("dateReceived"));
					}

					if (refObj.has("timeReceived") && !refObj.getString("timeReceived").equals("")) {
						instrument.put("timeReceived", refObj.getString("timeReceived"));
					} else {
						instrument.put("timeReceived", "0:00:00");
					}

					if (refObj.has("netchange") && !refObj.get("netchange").equals("")) {
						instrument.put("netchange", refObj.get("netchange").toString());
					} else {
						instrument.put("netchange", "");
					}

					if (refObj.has("percentageChange") && !refObj.getString("percentageChange").equals("")) {
						instrument.put("percentageChange", refObj.getString("percentageChange"));
					} else {
						instrument.put("percentageChange", "");
					}

					if (refObj.has("bidRate") && !refObj.getString("bidRate").equals("")) {
						instrument.put("bidRate", refObj.getString("bidRate"));
					} else {
						instrument.put("bidRate", "");
					}

					if (refObj.has("askRate") && !refObj.getString("askRate").equals("")) {
						instrument.put("askRate", refObj.getString("askRate"));
					} else {
						instrument.put("askRate", "");
					}

					if (refObj.has("volume") && !refObj.get("volume").equals("")) {
						instrument.put("volume", refObj.get("volume"));
					} else {
						instrument.put("volume", 0);
					}

					if (refObj.has("ISINCode") && !refObj.get("ISINCode").equals("")) {
						instrument.put(TemenosConstants.ISIN, refObj.get("ISINCode"));
					} else {
						instrument.put(TemenosConstants.ISIN, t24Obj.getString(TemenosConstants.ISIN));
					}

					if (refObj.has("exchange") && !refObj.get("exchange").equals("")) {
						instrument.put("exchange", refObj.get("exchange"));
					} else {
						instrument.put("exchange", t24Obj.getString("stockExchange"));
					}

				}

				watchlist.put(instrument);
			}
		}
		return watchlist;
	}

	public JSONArray returnSearch(JSONArray array, String searchValue) {
		JSONArray filtedArray = new JSONArray();
		JSONArray sortedJSON = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String desc = obj.getString("instrumentName").toLowerCase();
				String isin = obj.getString("ISINCode").toLowerCase();
				String search = searchValue.toLowerCase();
				if (desc.contains(search)) {
					filtedArray.put(obj);
				}
				if (isin.contains(search)) {
					filtedArray.put(obj);
				}
			} catch (Exception e) {

				alert.prepareError("Error while searching Instruments - "
						+ WealthAPIServices.WEALTH_GETFAVORITEINSTRUMENTS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		try {
			Set<String> instruments = new HashSet<String>();
			for (int j = 0; j < filtedArray.length(); j++) {
				String nameCode = filtedArray.getJSONObject(j).getString("instrumentName");
				if (instruments.contains(nameCode)) {
					continue;
				} else {
					instruments.add(nameCode);
					sortedJSON.put(filtedArray.getJSONObject(j));
				}

			}
			filtedArray = sortedJSON;
		} catch (JSONException e) {
			alert.prepareError("Error while invoking Transact - "
					+ WealthAPIServices.WEALTH_GETTRANSACTIONDETAILS.getOperationName() + "  : " + e).log();
			return null;
		}
		return filtedArray;
	}

	private JSONArray getSortedJsonArray(JSONArray jsonArr, String sortBy, String sortOrder, int size, int offset) {

		JSONArray sortedJsonArr = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dtf = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm:ss");
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		if (sortBy == null || sortBy.isEmpty()) {
			sortBy = TemenosConstants.INSTRUMENTNAME;
		}
		String sortValue = sortBy;

		if (sortValue.equalsIgnoreCase(TemenosConstants.INSTRUMENTNAME)
				|| sortValue.equalsIgnoreCase(TemenosConstants.REFERENCECURRENCY)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject instrumentObj = jsonArr.getJSONObject(i);
				Date date = sdf.parse(instrumentObj.get("dateReceived").toString(), new ParsePosition(0));
				String instrumentDate = (date != null) ? df.format(date) : "";
				String instrumentDateTime = instrumentDate + "T" + instrumentObj.get("timeReceived");
				String formatDateTime = (!instrumentDate.equals(""))
						? dtf.format(dt.parse(instrumentDateTime, new ParsePosition(0)))
						: "";
				instrumentObj.put("dateTime", instrumentDateTime);
				instrumentObj.put("formatDateTime", formatDateTime);
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
					str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
					return str1.compareToIgnoreCase(str2);
				}

			});
			if (sortOrder != null && sortOrder.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = jsonArr.length() - 1; i >= 0; i--) {
					sortedJsonArr.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					sortedJsonArr.put(jsonValues.get(i));
				}
			}

		} else if (sortValue.equalsIgnoreCase("bidRate") || sortValue.equalsIgnoreCase("askRate")
				|| sortValue.equalsIgnoreCase("lastRate") || sortBy.equalsIgnoreCase("percentageChange")) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject instrumentObj = jsonArr.getJSONObject(i);
				Date date = sdf.parse(instrumentObj.get("dateReceived").toString(), new ParsePosition(0));
				String instrumentDate = (date != null) ? df.format(date) : "";
				String instrumentDateTime = instrumentDate + "T" + instrumentObj.get("timeReceived");
				String formatDateTime = (!instrumentDate.equals(""))
						? dtf.format(dt.parse(instrumentDateTime, new ParsePosition(0)))
						: "";
				instrumentObj.put("dateTime", instrumentDateTime);
				instrumentObj.put("formatDateTime", formatDateTime);
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					if (a.has(KEY_NAME) && b.has(KEY_NAME) && a.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return (b.get(KEY_NAME).toString().equalsIgnoreCase("")) ? 0 : -1;
					}
					if (b.has(KEY_NAME) && b.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return 1;
					}
					Double dbl1 = null;
					Double dbl2 = null;
					dbl1 = (a.has(KEY_NAME) && a.get(KEY_NAME).toString().length() > 0)
							? Double.parseDouble((a.get(KEY_NAME)).toString())
							: 0;
					dbl2 = (b.has(KEY_NAME) && b.get(KEY_NAME).toString().length() > 0)
							? Double.parseDouble((b.get(KEY_NAME)).toString())
							: 0;
					return dbl1.compareTo(dbl2);
				}

			});
			if (sortOrder != null && sortOrder.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = jsonArr.length() - 1; i >= 0; i--) {
					sortedJsonArr.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					sortedJsonArr.put(jsonValues.get(i));
				}
			}
		} else if (sortValue.equalsIgnoreCase("volume")) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject instrumentObj = jsonArr.getJSONObject(i);
				Date date = sdf.parse(instrumentObj.get("dateReceived").toString(), new ParsePosition(0));
				String instrumentDate = (date != null) ? df.format(date) : "";
				String instrumentDateTime = instrumentDate + "T" + instrumentObj.get("timeReceived");
				String formatDateTime = (!instrumentDate.equals(""))
						? dtf.format(dt.parse(instrumentDateTime, new ParsePosition(0)))
						: "";
				instrumentObj.put("dateTime", instrumentDateTime);
				instrumentObj.put("formatDateTime", formatDateTime);
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {

					if (a.has(KEY_NAME) && b.has(KEY_NAME) && a.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return (b.get(KEY_NAME).toString().equalsIgnoreCase("")) ? 0 : -1;
					}
					if (b.has(KEY_NAME) && b.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return 1;
					}

					Integer l1 = null;
					Integer l2 = null;
					l1 = a.has(KEY_NAME) ? Integer.parseInt(a.get(KEY_NAME).toString()) : 0;
					l2 = b.has(KEY_NAME) ? Integer.parseInt(b.get(KEY_NAME).toString()) : 0;
					return l1.compareTo(l2);
				}

			});
			if (sortOrder != null && sortOrder.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = jsonArr.length() - 1; i >= 0; i--) {
					sortedJsonArr.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					sortedJsonArr.put(jsonValues.get(i));
				}
			}
		} else if (sortValue.equalsIgnoreCase(TemenosConstants.DATE_TIME)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject instrumentObj = jsonArr.getJSONObject(i);
				Date date = sdf.parse(instrumentObj.get("dateReceived").toString(), new ParsePosition(0));
				String instrumentDate = (date != null) ? df.format(date) : "";
				String instrumentDateTime = instrumentDate + "T" + instrumentObj.get("timeReceived");
				String formatDateTime = (!instrumentDate.equals(""))
						? dtf.format(dt.parse(instrumentDateTime, new ParsePosition(0)))
						: "";
				instrumentObj.put("dateTime", instrumentDateTime);
				instrumentObj.put("formatDateTime", formatDateTime);
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = (String) a.get(KEY_NAME);
					str2 = (String) b.get(KEY_NAME);
					return str1.compareToIgnoreCase(str2);
				}

			});
			if (sortOrder != null && sortOrder.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = jsonArr.length() - 1; i >= 0; i--) {
					sortedJsonArr.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					sortedJsonArr.put(jsonValues.get(i));
				}
			}

		}

		if (size > 0 && offset >= 0) {
			sortedJsonArr = pagination(sortedJsonArr, size, offset);
		}
		return sortedJsonArr;
	}

	public JSONArray pagination(JSONArray sortedJSON, int size, int offset) {

		JSONArray paginationJSON = new JSONArray();

		int j = 0;
		for (int i = offset; i < sortedJSON.length(); i++) {
			if (j == size) {
				break;
			} else {
				paginationJSON.put(sortedJSON.get(i));
			}
			j++;
		}

		return paginationJSON;

	}

	private JSONArray t24_addapplicationids(JSONArray sortedFavoriteInstruments, String instr_app) {

		JSONArray appfavArr = new JSONArray();
		String instrumentId = "";
		for (int i = 0; i < sortedFavoriteInstruments.length(); i++) {

			JSONObject favobj = sortedFavoriteInstruments.getJSONObject(i);
			instrumentId = favobj.get("instrumentId").toString();
			if (instr_app != null && instr_app.length() > 0) {
				String favInstrumentIdsArr[] = instr_app.trim().split("@");
				String application = "";
				for (String s : favInstrumentIdsArr) {
					String index[] = s.trim().split("~");
					if (1 < index.length) {
						if (instrumentId.equalsIgnoreCase(s.trim().split("~")[0].trim())) {
							application = application + s.trim().split("~")[1].trim();
							favobj.put(TemenosConstants.APPLICATION, application);
						}
					}
				}

			}

			favobj.put(TemenosConstants.ISINEXCHANGE, ((!favobj.get("ISINCode").toString().equalsIgnoreCase(""))
					? ((!favobj.get("exchange").toString().equalsIgnoreCase(""))
							? (favobj.get("ISINCode").toString() + " | " + favobj.get("exchange").toString())
							: favobj.get("ISINCode").toString())
					: ((!favobj.get("exchange").toString().equalsIgnoreCase("")) ? favobj.get("exchange") : "")));

			appfavArr.put(favobj);
		}

		return appfavArr;
	}
}
