package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class QRFeeDAO {

	private static final Logger logger = LogManager.getLogger(QRFeeDAO.class);

	public static Result callFeeAPI() throws Exception {
		Result result = new Result();
		JSONObject responseObj = new JSONObject();
		logger.debug("QRFeeDAO:::");
		Map<String, Object> inputmap = new HashMap<>();
		Map<String, Object> headerMap = new HashMap<>();
		logger.debug("GetQRPaymentCharges::: getQRPaymentCharges inputmap:::" + inputmap.toString());
		JSONArray charges = new JSONArray();
		try {
			String qrPaymentChargesresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.QRPAYMENT_CHARGES_GET).withRequestParameters(inputmap)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(headerMap).build()
					.getResponse();
			JSONObject responseJSON = new JSONObject(qrPaymentChargesresponse);
			charges = responseJSON.getJSONArray("qrpaymentcharges");
			responseObj.put("qrpaymentCharges", charges);
			result = JSONToResult.convert(responseObj.toString());
			result.addParam(new Param("success", "true"));
			logger.debug("GetQRPaymentCharges result qrpaymentCharges:::" + ResultToJSON.convert(result));
		} catch (Exception e) {
			logger.error("Exception while fetching QRPayment charges:::" + e.toString());
			result.addParam(new Param("success", "false"));
		}
		return result;
	}
}
