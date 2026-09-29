package com.bct.postprocessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.CardConstants;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class cardPendingTransPostProcessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(cardPendingTransPostProcessor.class);

	@SuppressWarnings("deprecation")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("cardPendingTransPostProcessor Result:###" + ResultToJSON.convert(result));
			
			// Parse the JSON string into a JSONObject
	        JSONObject jsonObject = new JSONObject(ResultToJSON.convert(result));

	        // Get the "pendingAuthInfo_out" array
	        JSONArray pendingAuthInfo = jsonObject.getJSONArray("pendingAuthInfo_out");

			if (pendingAuthInfo != null && pendingAuthInfo.length() > 0) {
				// Sort the JSON array based on the 'transactionDate' field
				JSONArray sortedArray = sortJsonArrayByTransactionDate(pendingAuthInfo);

				// Print out the sorted array (or process it further)
				System.out.println("Sorted Pending Auth Info (by Transaction Date): ");
				System.out.println(sortedArray.toString(2));
				result.removeDatasetById("pendingAuthInfo_out");

				Dataset dataset = constructDatasetFromJSONArray(sortedArray);
				dataset.setId("pendingAuthInfo_out");
				result.addDataset(dataset);

			}
			
			String errcode = result.getParamValueByName("respCode_out");
			String errmsg = result.getParamValueByName("respLabel_out");
			logger.debug("errmsg :###" + errmsg);
			logger.debug("errcode :###" + errcode);
			
			String regex = "^(?!0{2,3}$)\\d+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(errcode);

			// Find and display matches
			while (matcher.find()) {
				result.addParam("errcode", errcode);
				result.addParam("errmsg", CardConstants.PendingTransErrMessage(errcode));
			}
			
		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}
	
	public static JSONArray sortJsonArrayByTransactionDate(JSONArray jsonArray) throws ParseException {
        // Create a list to store the JSONObjects
        List<JSONObject> jsonList = new ArrayList<>();

        // Convert the JSONArray to a list of JSONObject
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonList.add(jsonArray.getJSONObject(i));
        }

        // Define the date format (assuming ISO-8601 format)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Sort the list based on the 'transactionDate' field
        jsonList.sort((json1, json2) -> {
            try {
                Date date1 = dateFormat.parse(json1.getString("transactionDate"));
                Date date2 = dateFormat.parse(json2.getString("transactionDate"));
                return date2.compareTo(date1); // descending order
            } catch (ParseException e) {
                throw new RuntimeException("Date parsing error", e);
            }
        });

        // Create a new sorted JSONArray
        JSONArray sortedJsonArray = new JSONArray();

        // Add sorted JSONObjects back to the sorted array
        for (JSONObject jsonObject : jsonList) {
            sortedJsonArray.put(jsonObject);
        }

        return sortedJsonArray;
    }
	
	 public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
	        Dataset dataset = new Dataset();
	        for (int count = 0; count < JSONArray.length(); count++) {
	            Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
	            dataset.addRecord(record);
	        }
	        return dataset;
	    }
	 
	 public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
	        Record response = new Record();
	        if (JSONObject == null || JSONObject.length() == 0) {
	            return response;
	        }
	        Iterator<String> keys = JSONObject.keys();

	        while (keys.hasNext()) {
	            String key = (String) keys.next();
	            if (JSONObject.get(key) instanceof Integer) {
	                Param param = new Param(key, JSONObject.get(key).toString(), FabricConstants.INT);
	                response.addParam(param);

	            } else if (JSONObject.get(key) instanceof Boolean) {
	                Param param = new Param(key, JSONObject.get(key).toString(), FabricConstants.BOOLEAN);
	                response.addParam(param);

	            } else if (JSONObject.get(key) instanceof JSONArray) {
	                Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
	                dataset.setId(key);
	                response.addDataset(dataset);
	            } else if (JSONObject.get(key) instanceof JSONObject) {
	                Record record = constructRecordFromJSONObject(JSONObject.getJSONObject(key));
	                record.setId(key);
	                response.addRecord(record);
	            } else {
	                Param param = new Param(key, JSONObject.optString(key), FabricConstants.STRING);
	                response.addParam(param);
	            }
	        }

	        return response;
	    }
}
