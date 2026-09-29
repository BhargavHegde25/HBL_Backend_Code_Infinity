package com.bct.custom.backenddeligate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.CIPSPaymentHistoryBackendDeligate;
import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class CIPSPaymentHistoryBackendDeligateImpl implements CIPSPaymentHistoryBackendDeligate{
	private static final Logger logger = LogManager.getLogger(CIPSPaymentHistoryBackendDeligateImpl.class);
	public static final String DBPERRMSG = "dbpErrMsg";
	public static final String DBPERRCODE = "dbpErrCode";
	@Override
	public JSONArray getCIPSPaymentHistory(String biilerId, Map<String, Object> inputmap,
			DataControllerRequest dcRequest) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getCIPSTransactionById(Map<String, Object> inputParams, DataControllerRequest dcRequest)
			throws ApplicationException {
		JsonObject responseJSON = null;
		String filter ="";
		String transactionId=inputParams.get("transactionId")!=null?inputParams.get("transactionId").toString():"";
		if(StringUtils.isNotBlank(transactionId)) {
		 filter = "transactionId  eq '" + transactionId + "'";
		}
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::CIPSPaymentHistoryBackendDeligateImpl:getCIPSTransactionById: inputmap:"+
				inputmap);
		JSONArray response = new JSONArray();
		try {
			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CIPS_TRANS_HISTORY_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			logger.debug("BCT::CIPSPaymentHistoryBackendDeligateImpl: getCIPSTransactionById response:"+dbresponse);
			 responseJSON =  new JsonParser().parse(dbresponse).getAsJsonObject();
		}catch (Exception e) {
			logger.error("Exception caught while fetching CIPS transactions:" +e.toString());
			
		}
		return responseJSON;
	}
}
