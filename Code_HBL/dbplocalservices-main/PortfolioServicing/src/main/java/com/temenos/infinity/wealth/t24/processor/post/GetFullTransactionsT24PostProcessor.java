package com.temenos.infinity.wealth.t24.processor.post;


import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
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
 *
 * 
 * @author muthukumarv
 *
 */

public class GetFullTransactionsT24PostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetFullTransactionsT24PostProcessor T24 - Entered ").log();
			// Return empty list if transact fails or no records error is thrown
			JSONArray transactionArr = new JSONArray();
			Dataset ds = result.getDatasetById("LoopDataset");
			if (ds != null) {
				List<Record> drecords = ds.getAllRecords();
				diagnostic.prepareDebug("==========> GetFullTransactionsT24PostProcessor T24 - No. of records returned initially: "+ drecords.size()).log();
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
			diagnostic.prepareDebug("==========> GetFullTransactionsT24PostProcessor T24 - No. of transactions returned: "+ transactionArr.length()).log();

			Result transactionRes = Utilities.constructResultFromJSONObject(transactionObj);
			transactionRes.addOpstatusParam("0");
			transactionRes.addHttpStatusCodeParam("200");
			transactionRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		
			return transactionRes;
			
		} catch (Exception e) {
			alert.prepareError("==========> GetFullTransactionsT24PostProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return result;
	}

}
