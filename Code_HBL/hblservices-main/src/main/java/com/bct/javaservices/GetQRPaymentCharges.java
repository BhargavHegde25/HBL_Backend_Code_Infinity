package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetQRPaymentCharges implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GetQRPaymentCharges.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		LOG.debug("In GetQRPaymentCharges:::");
		JSONObject responseObj = new JSONObject();
		try {
			Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
			String aggregatorType = inputParams.get("aggregatorType") != null
					? inputParams.get("aggregatorType").toString()
					: "";
			LOG.debug("GetQRPaymentCharges aggregatorType:::" + aggregatorType);
			if (StringUtils.isNotBlank(aggregatorType)) {
				Map<String, Object> inputmap = new HashMap<>();
				String filter = "aggregatorType eq '" + aggregatorType + "'";
				inputmap.put(HBLURLConstants.FILTER, filter);
				LOG.debug("GetQRPaymentCharges::: getQRPaymentCharges inputmap:::" + inputmap.toString());
				JSONArray charges = new JSONArray();
				try {
					String qrPaymentChargesresponse = DBPServiceExecutorBuilder.builder()
							.withOperationId(HBLURLConstants.QRPAYMENT_CHARGES_GET).withRequestParameters(inputmap)
							.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE)
							.withRequestHeaders(request.getHeaderMap()).build().getResponse();
					JSONObject responseJSON = new JSONObject(qrPaymentChargesresponse);
					charges = responseJSON.getJSONArray("qrpaymentcharges");
					responseObj.put("qrpaymentCharges", charges);
					result = JSONToResult.convert(responseObj.toString());
					result.addParam(new Param("success", "true"));
					LOG.debug("GetQRPaymentCharges result qrpaymentCharges:::" + ResultToJSON.convert(result));
				} catch (Exception e) {
					LOG.error("Exception while fetching QRPayment charges:::" + e.toString());

				}
			} else {
				result.addParam(new Param("dbpErrCode", "HBL-100"));
				result.addParam(new Param("dbpErrMsg", "Aggregator Type is mandatory! Please provide Aggregator Type"));
				result.addParam(new Param("success", "false"));
			}
		} catch (Exception e) {
			LOG.error("Exception occured while fetching the QRPayment charges  :" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("success", "false"));
		}
		return result;
	}
}
