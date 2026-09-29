package com.bct.postprocessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.BranchDetailsBusinessDelegate;
import com.bct.custom.resource.api.BranchDetailsResource;
import com.bct.javaservices.BranchdetailsManageservice;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetBanksListPostProcessor implements DataPostProcessor2 {
	private static final Logger LOG = LogManager.getLogger(GetBanksListPostProcessor.class);

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {
		// TODO Auto-generated method stub
		//Result result = new Result();
		 LOG.debug("HBL:BranchdetailsManageservice:result:"+ResultToJSON.convert(result));
      try {
    	  BranchDetailsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
  				.getBusinessDelegate(BranchDetailsBusinessDelegate.class);
    	  String input ="";
    	  Map<String, Object> inputsArray = new HashMap<String, Object>();
    	  Dataset ds = new Dataset();
    	  JSONArray branchResponse = businessDelegate.getBranchDetails(input, inputsArray, dcRequest);
    		if (result != null || !HelperMethods.hasError(result) ) {
    			String jsonString= ResultToJSON.convert(result);
    			JSONObject jsonResult= new JSONObject(jsonString);
    			if(jsonResult!=null) {
    				JSONArray responseBankDetails = jsonResult.has("bankDetails")?jsonResult.getJSONArray("bankDetails"):null;
    				if(responseBankDetails!=null) {
    					JSONArray mergedResponse = addSwiftCode(responseBankDetails, branchResponse);
    					LOG.debug("HBL:BranchdetailsManageservice:addSwiftCode:mergedResponse"+mergedResponse);
    					JSONObject newResObj= new JSONObject();
    					newResObj.put("bankDetails", mergedResponse);
    					LOG.debug("Result branchdetails:" + newResObj);
    					result.appendJson(newResObj.toString());
    					result.addParam(new Param("opstatus", "0"));
    					result.addParam(new Param("httpStatusCode", "200"));
    					result.addParam(new Param("success", "true"));
    			}
    		}
    	}
      } catch (ApplicationException e) {
			LOG.error("Exception occured while fetching the branch details  :" + e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception occured while fetching the branch details  :" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
		}
			return result;
	}

	public JSONArray addSwiftCode(JSONArray backendResponse, JSONArray dbResponse){
		JSONArray newBackendResponse = new JSONArray();
		 LOG.debug("HBL:BranchdetailsManageservice:addSwiftCode:backendResponse"+backendResponse);
		for(int i=0;i<backendResponse.length();i++) {
			JSONObject backendObj = backendResponse.getJSONObject(i);
			String bankCode=backendObj.optString("bankCode");
			if(StringUtils.isNotBlank(bankCode)) {
				for( int j=0;j<dbResponse.length(); j++) {
					JSONObject dbObj = dbResponse.getJSONObject(j);
					String dbBankCode=dbObj.optString("bank_cd");
					int code=Integer.valueOf(dbBankCode);
					dbBankCode = String.format("%04d", code);
					if(bankCode.equalsIgnoreCase(dbBankCode) && dbObj.optBoolean("Status")==true){
						backendObj.put("bankSwift", dbObj.optString("bank_swift"));
					}
				}
			}
			if(backendObj.has("bankSwift")) {
				newBackendResponse.put(backendObj);
			}
			
		}
		return newBackendResponse;
	}

public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
	Dataset dataset = new Dataset();
	for (int count = 0; count < JSONArray.length(); count++) {
		Record record = new Record();
		Record resRecord = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
		if(resRecord.getParamValueByName("branch_mge_email")!=null) {
		record.addParam("branch_mge_email", resRecord.getParamValueByName("branch_mge_email"));
		}
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
		String key = keys.next();
		if (JSONObject.get(key) instanceof String) {
			Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			response.addParam(param);

		} else if (JSONObject.get(key) instanceof Integer) {
			Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
			response.addParam(param);

		} else if (JSONObject.get(key) instanceof Boolean) {
			Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
			response.addParam(param);

		} else if (JSONObject.get(key) instanceof JSONArray) {
			Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
			dataset.setId(key);
			response.addDataset(dataset);
		}
	}

	return response;
}

}
