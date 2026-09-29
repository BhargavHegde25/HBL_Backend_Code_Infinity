/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.mockdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 * @author himaja.sridhar
 *
 */
public class GetStockNewsMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		WealthMockUtil wealthmMockUtil = new WealthMockUtil();
		try {
			String ricCode = request.getParameter("instrumentCode");
			String limitVal = request.getParameter("pageSize");
			String offsetVal = request.getParameter("pageOffset");
			JSONArray stockNews = wealthmMockUtil.getStockNews(ricCode);
			JSONArray sortedJSON = new JSONArray();	
			int totalCount = 0;			
			
			if (stockNews != null && stockNews.length() > 0) {
				totalCount = stockNews.length();
				List<JSONObject> stockList = new ArrayList<JSONObject>();
				for (int i = 0; i < stockNews.length(); i++)
					stockList.add(stockNews.getJSONObject(i));

				Collections.sort(stockList, new Comparator<JSONObject>() {
					private final String KEY_NAME = "CT";

					@Override
					public int compare(JSONObject a, JSONObject b) {
						Date d1 = new Date();
						Date d2 = new Date();
						try {
							String str = a.has(KEY_NAME) ? a.get(KEY_NAME).toString() : "";
							if (str != "") {
								d1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(str.substring(0, str.length() - 6));
							}
							str = b.has(KEY_NAME) ? b.get(KEY_NAME).toString() : "";
							if (str != "") {
								d2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(str.substring(0, str.length() - 6));
							}
						} catch (JSONException e) {
							e.getMessage();
						} catch (ParseException e) {
							e.getMessage();
						}
						return d1.compareTo(d2);
					}

				});

				for (int i = stockList.size() - 1; i >= 0; i--) {
					sortedJSON.put(stockList.get(i));
				}

				int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
				int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
				
				if (limit > 0 && offset >= 0) {
					sortedJSON = pagination(sortedJSON, limit, offset);
				}	
				
			}
			
			response1.put("stockNews", sortedJSON);
			response1.put("totalCount",totalCount);
			response1.put("httpStatusCode", "200");
			response1.put("opstatus", "0");
			return Utilities.constructResultFromJSONObject(response1);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of getPricingData: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}
	
	private JSONArray pagination(JSONArray jsonArray, int limit, int offset) {
		JSONArray paginationJSON = new JSONArray();

		int j = 0;
		for (int i = offset; i < jsonArray.length(); i++) {
			if (j == limit) {
				break;
			} else {
				paginationJSON.put(jsonArray.get(i));
			}
			j++;
		}
		return paginationJSON;
	}
}
