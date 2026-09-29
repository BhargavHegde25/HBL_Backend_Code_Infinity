package com.bct.postprocessor;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.user.HBLGetUserPostProcessor;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class HBLCoreCustomerSearchPostProcessor implements DataPostProcessor2 {
	LoggerUtil logger = new LoggerUtil(HBLCoreCustomerSearchPostProcessor.class);
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		  JSONObject responseObj= new JSONObject(ResultToJSON.convert(result));
	        logger.debug("HBL::HBLCoreCustomerSearchPostProcessor::responseObj:"+responseObj);
	       
	        JSONArray customersArray = responseObj.has("customers")?responseObj.getJSONArray("customers"):new JSONArray();
	        JSONArray customersArrayNew = new JSONArray();
	        for(int i=0;i<customersArray.length();i++) {
	        JSONObject customer = customersArray.getJSONObject(i);
	        logger.debug("HBL::HBLCoreCustomerSearchPostProcessor::customer:"+customer);
	        JSONArray contactDetails = customer.has("contactDetails")?customer.getJSONArray("contactDetails"):new JSONArray();
		  logger.debug("HBL::HBLCoreCustomerSearchPostProcessor::contactDetails:"+contactDetails);
		 String phone="";
		 String countryCode="";
		 String email="";
		 int phoneCount=0;
		 int emailCount=0;
		 for(int j=0;j< contactDetails.length();j++) {
			 JSONObject obj= contactDetails.getJSONObject(j);
			 String contactType= obj.optString("contactType");
			 if(phoneCount==0 && contactType.equalsIgnoreCase("MOBILE")) {
				 phone=obj.optString("contactData");
				 countryCode=obj.optString("iddPrefixPhone");
				 phone= countryCode+"-"+phone;
				 customer.put("phone", phone);
				 logger.debug("HBL::HBLCoreCustomerSearchPostProcessor::phone:"+phone);
				 phoneCount++;
				
			 }
			 if(emailCount==0 && contactType.equalsIgnoreCase("EMAIL")) {
				 email=obj.optString("contactData");
				 customer.put("email", email);
		    	 logger.debug("HBL::HBLCoreCustomerSearchPostProcessor::EMAIL:"+email);
		    	 emailCount++;
			 }
		 }
		 customersArrayNew.put(customer);
	   }
	     responseObj.put("customers", customersArrayNew);
	     Dataset ds=constructDatasetFromJSONArray(customersArrayNew);
	     ds.setId("customers");
	     result.addDataset(ds);
	     logger.debug("HBL::HBLCoreCustomerSearchPostProcessor:final:result:"+ResultToJSON.convert(result));
	     //result=JSONToResult.convert(responseObj.toString());
	        
		return result;
	}
	 public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
	        Dataset dataset = new Dataset();
	        for (int count = 0; count < JSONArray.length(); count++) {
	            Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
	            dataset.addRecord(record);
	        }
	        return dataset;
	    }

	    /**
	     * @param JSONObject
	     * @return
	     */
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

	    /**
	     * @param JSONObject
	     * @return
	     */
	    public static Result constructResultFromJSONObject(JSONObject JSONObject) {
	        Result response = new Result();
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
