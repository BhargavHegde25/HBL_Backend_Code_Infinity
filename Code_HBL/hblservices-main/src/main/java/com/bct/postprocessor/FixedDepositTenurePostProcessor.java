package com.bct.postprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class FixedDepositTenurePostProcessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(FixedDepositTenurePostProcessor.class);

	@SuppressWarnings("deprecation")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("FixedDepositTenurePostProcessor Result:###" + ResultToJSON.convert(result));
			logger.debug("FixedDepositTenurePostProcessor Result dataset:###" + result.getDatasetById("result").toString());
			Dataset dataSet = result.getDatasetById("result");
			JSONArray res = new JSONArray();
			res=convertDatasetToJSONArray(dataSet);
			
			JSONObject temp= new JSONObject();
			for(int i=0;i<res.length();i++) {
				JSONObject obj=res.getJSONObject(i);
				temp.put(obj.get("term").toString(),obj);
			}
			logger.debug("FixedDepositTenurePostProcessor final respone"+ temp);
			result.removeDatasetById("result");
			
			result.addParam("result", temp.toString());
			
		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}
	
	public static JSONArray convertDatasetToJSONArray(Dataset dataset) {
		JSONArray array = new JSONArray();
		List<Record> records = new ArrayList<>();

		if (dataset != null && dataset.getAllRecords().size() != 0) {
			records = dataset.getAllRecords();
		}

		for (int i = 0; i < records.size(); i++) {
			array.put(convertRecordToJSONObject(records.get(i)));
		}
		return array;
	}
	
	public static JSONObject convertRecordToJSONObject(Record record) {

		JSONObject jsonObj = new JSONObject();

		List<Param> arList = record.getAllParams();

		Iterator<Param> it = arList.iterator();
		while (it.hasNext()) {
			Param p = it.next();
			String key = p.getName();
			jsonObj.put(key, p.getValue());
		}
		return jsonObj;
	}
}
