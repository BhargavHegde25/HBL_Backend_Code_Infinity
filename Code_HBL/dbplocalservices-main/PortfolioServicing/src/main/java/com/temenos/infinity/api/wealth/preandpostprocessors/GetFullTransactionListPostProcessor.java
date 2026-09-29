/**
 * 
 */
package com.temenos.infinity.api.wealth.preandpostprocessors;

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
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author emilia.ivanov
 *
 */
public class GetFullTransactionListPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

// Return empty list if transact fails or no records error is thrown
		JSONArray transactionArr = new JSONArray();
		Dataset ds = result.getDatasetById("LoopDataset");
		if (ds != null) {
			List<Record> drecords = ds.getAllRecords();
			if (drecords != null && drecords.size() > 0) {
				for (int i=0; i < drecords.size(); i++) {
					Record dr = drecords.get(i);
					Dataset transactiosDs = dr.getDatasetById("portfolioTransactions");				
					if (transactiosDs != null) {
						List<Record> transactioList = transactiosDs.getAllRecords();
						if (transactioList != null && transactioList.size() > 0) {
							for (int j = 0; j < transactioList.size(); j++) {
								Record transactionRecord = transactioList.get(j);
								JSONObject transactionObj = CommonUtils.convertRecordToJSONObject(transactionRecord);
								if (transactionObj.has("transactionType")) {
									String orderType = transactionObj.getString("transactionType");
									if (!orderType.equals("") && (orderType.equalsIgnoreCase("BUY") || orderType.equalsIgnoreCase("SEL"))) {
										transactionArr.put(transactionObj);
									}
								}
							}
						}
					}
				}
			}
			
		}

		JSONObject transactionObj = new JSONObject();
		
		transactionObj.put("transactionList", transactionArr);

		Result transactionRes = Utilities.constructResultFromJSONObject(transactionObj);
		transactionRes.addOpstatusParam("0");
		transactionRes.addHttpStatusCodeParam("200");
		transactionRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
	
		return transactionRes;
	}

}
