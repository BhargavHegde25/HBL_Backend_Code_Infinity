package com.kony.adminconsole.service.campaignmanagement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to fetch list of attributes from the back end
 * 
 * @author Mohit Khosla (KH2356)
 */

public class AttributesFetchService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result result = new Result();

		try {
			
			Map<String, JSONObject> options = getAttributeOptions(requestInstance);
			
			JSONArray attributesArray = getAttributes(requestInstance);
			
			processResult(options,attributesArray, result);
			
		} catch (ApplicationException ae) {
			ae.getErrorCodeEnum().setErrorCode(result);
			alert.prepareError("ApplicationException occured in AttributesFetchService JAVA service. Error: ", ae).log();
		} catch (Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in AttributesFetchService JAVA service. Error: ", e).log();
		}

		return result;
	}

	private void processResult(Map<String, JSONObject> options, JSONArray attributesArray, Result result) {
		
		Dataset datasets = new Dataset("datasets");

		if (attributesArray.length() > 0) {
			
			String attributeId ;
			
			JSONObject datasetObj = attributesArray.optJSONObject(0);
			
			Record attributeRecords = new Record();
			attributeRecords.setId("attributes");
			
			JSONObject jsonObject ;
			
			Iterator<String> keys ;
			
			String key;
			
			for (int i = 0; i < attributesArray.length(); ++i) {

				Record attributeRecord = new Record();
				
				attributeId = attributesArray.optJSONObject(i).optString("attributendpoint");
				attributeRecord.setId(attributeId);

				attributeRecord.addParam(new Param("id",
						attributesArray.optJSONObject(i).getString("attributeid"), FabricConstants.STRING));
				attributeRecord.addParam(new Param("name",
						attributesArray.optJSONObject(i).getString("attributename"), FabricConstants.STRING));
				attributeRecord.addParam(new Param("attributendpoint",
						attributesArray.optJSONObject(i).getString("attributendpoint"), FabricConstants.STRING));
				attributeRecord.addParam(new Param("type",
						attributesArray.optJSONObject(i).getString("attributetype"), FabricConstants.STRING));
				attributeRecord.addParam(new Param("range", attributesArray.optJSONObject(i).optString("range"),
						FabricConstants.STRING));
				attributeRecord.addParam(new Param("helpText",
						attributesArray.optJSONObject(i).optString("helptext"), FabricConstants.STRING));
				
				JSONArray criteriasJSONArray = CommonUtilities
						.getStringAsJSONArray(attributesArray.optJSONObject(i).optString("criterias"));
				
				Record criteriaRecord = new Record();
				criteriaRecord.setId("criterias");
				if(criteriasJSONArray != null) {
					for(int j=0; j<criteriasJSONArray.length(); j++) {
						jsonObject = criteriasJSONArray.getJSONObject(j);
						keys = jsonObject.keys();
						while (keys.hasNext()) {
							key = (String) keys.next();
							criteriaRecord.addParam(new Param(key, jsonObject.optString(key)));
						}
					}
				}
				attributeRecord.addRecord(criteriaRecord);
				
				JSONArray optionsJSONArray = CommonUtilities.getStringAsJSONArray(attributesArray.optJSONObject(i).optString("options"));
				Record optionRecord = new Record();
				optionRecord.setId("options");
				if(optionsJSONArray != null) {
					for(int j=0; j<optionsJSONArray.length(); j++) {
						jsonObject = optionsJSONArray.getJSONObject(j);
						keys = jsonObject.keys();
						while (keys.hasNext()) {
							key = (String) keys.next();
							if(options.containsKey(key)) {
								optionRecord.addParam(new Param(options.get(key).getString("key"),options.get(key).getString("value")));
							}else {
								optionRecord.addParam(new Param(key, jsonObject.optString(key)));
							}
						}
					}
				}
				attributeRecord.addRecord(optionRecord);
				boolean isSameModel = datasetObj.optString("modelId").equalsIgnoreCase(attributesArray.optJSONObject(i).optString("modelId"));
				if(!isSameModel && attributesArray.length() != i + 1) {
					Record datasetRecord = new Record();
					datasetRecord.addParam(new Param("id", datasetObj.getString("modelId"),
							FabricConstants.STRING));
					datasetRecord.addParam(new Param("name",
							datasetObj.getString("modelName"), FabricConstants.STRING));
					datasetRecord.addParam(new Param("endpoint",
							datasetObj.optString("endpoint"), FabricConstants.STRING));
					datasetRecord.addRecord(attributeRecords);
					datasets.addRecord(datasetRecord);
					attributeRecords = new Record();
					datasetObj = attributesArray.optJSONObject(i);
					attributeRecords.addRecord(attributeRecord);
					attributeRecords.setId("attributes");
				} else if(isSameModel && attributesArray.length() != i + 1) {
					attributeRecords.addRecord(attributeRecord);
				} 
				if(attributesArray.length() == i + 1) {
					if(!isSameModel) {
						Record datasetRecord = new Record();
						datasetRecord.addParam(new Param("id", datasetObj.getString("modelId"),
								FabricConstants.STRING));
						datasetRecord.addParam(new Param("name",
								datasetObj.getString("modelName"), FabricConstants.STRING));
						datasetRecord.addParam(new Param("endpoint",
								datasetObj.getString("endpoint"), FabricConstants.STRING));
						datasetRecord.addRecord(attributeRecords);
						datasets.addRecord(datasetRecord);
						attributeRecords = new Record();
						datasetObj = attributesArray.optJSONObject(i);
						attributeRecords.setId("attributes");
						
					}
					attributeRecords.addRecord(attributeRecord);
					Record datasetRecord = new Record();
					datasetRecord.addParam(new Param("id", datasetObj.getString("modelId"),
							FabricConstants.STRING));
					datasetRecord.addParam(new Param("name",
							datasetObj.getString("modelName"), FabricConstants.STRING));
					datasetRecord.addParam(new Param("endpoint",
							datasetObj.getString("endpoint"), FabricConstants.STRING));
					datasetRecord.addRecord(attributeRecords);
					datasets.addRecord(datasetRecord);
				}
			}

		result.addDataset(datasets);
		}
	}

	private Map<String, JSONObject> getAttributeOptions(DataControllerRequest requestInstance)
            throws ApplicationException {

        Map<String, JSONObject> attributeOptionMap = new HashMap<String, JSONObject>();

        String response = Executor.invokeService(ServiceURLEnum.ATTRIBUTEOPTION_READ, new HashMap<>(), null,
                requestInstance);
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
        
        JSONObject data ;

        if (responseJSON != null && responseJSON.has(FabricConstants.OPSTATUS)
                && responseJSON.getInt(FabricConstants.OPSTATUS) == 0 && responseJSON.has("attributeoption")) {

            JSONArray responseJSONArray = responseJSON.getJSONArray("attributeoption");

            for (int i = 0; i < responseJSONArray.length(); ++i) {
                JSONObject attributeOptionResponse = responseJSONArray.getJSONObject(i);

                data = new JSONObject()
                		.put("key", attributeOptionResponse.getString("endpoint_attributeoption_id"))
                		.put("value", attributeOptionResponse.getString("name"));
                attributeOptionMap.put(attributeOptionResponse.getString("id"), data);
            }
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_21769);
        }

        return attributeOptionMap;
    }
	
	private JSONArray getAttributes(DataControllerRequest requestInstance)
			throws ApplicationException {

		// ** Reading from 'attribute' table **
		Map<String, String> attributeMap = new HashMap<>();

		String response = Executor.invokeService(ServiceURLEnum.DATASETATTRIBUTES_PROC, attributeMap, null,
				requestInstance);
		JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);

		if (responseJSON != null && responseJSON.has(FabricConstants.OPSTATUS)
				&& responseJSON.getInt(FabricConstants.OPSTATUS) == 0 && responseJSON.has("records")) {

			JSONArray responseJSONArray = responseJSON.getJSONArray("records");
			
			return responseJSONArray;

		} else {
			throw new ApplicationException(ErrorCodeEnum.ERR_21761);
		}

	}

}