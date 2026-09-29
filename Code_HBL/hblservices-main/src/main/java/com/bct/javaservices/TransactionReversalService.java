package com.bct.javaservices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class TransactionReversalService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(TransactionReversalService.class);
	private static final String REVERSE_TRANSACTION_SERVICE = "HBL-T24ISPaymentOrders";
	private static final String REVERSE_TRANSACTION_OPEARATION = "reverseTransaction";
	String status = "";
	String message = "";
	Result result = new Result();

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		String paymentReferenceId = request.getParameter("paymentReferenceId");
		String referenceId = request.getParameter("referenceId");
		String transactionId = request.getParameter("transactionId");

		LOG.debug("paymentReferenceId ### " + paymentReferenceId);
		LOG.debug("referenceId ### " + referenceId);
		LOG.debug("transactionId ### " + transactionId);
		HashMap<String, Object> inputParams = new HashMap<>();
		inputParams.put("paymentReferenceId", paymentReferenceId);
		request.addRequestParam_("paymentReferenceId", paymentReferenceId);
		try {

			result = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
					REVERSE_TRANSACTION_SERVICE, REVERSE_TRANSACTION_OPEARATION, true);

			JSONObject reverseTxResponse = new JSONObject(ResultToJSON.convert(result));

			LOG.debug("reverseTransaction response:" + reverseTxResponse);
			if (result.getParamValueByName("dbpErrCode") != null || result.getParamValueByName("dbpErrMsg") != null) {
				status = "Transaction Reverse Failed";
				message = "Failed while executing at the backend";

				result.addParam("status", status);
				result.addParam("message", message);
				result.addParam("dbpErrCode", result.getParamValueByName("dbpErrCode"));
				result.addParam("dbpErrMsg", result.getParamValueByName("dbpErrMsg"));
			}

			String reversalStatus = result.getParamValueByName("status");
			String id = result.getParamValueByName("id");
			String transactionStatus = result.getParamValueByName("transactionStatus");
			String transactionMessage = result.getParamValueByName("message");
			if (StringUtils.isNotBlank(reversalStatus) && reversalStatus.equalsIgnoreCase("success")) {
				message = "Transaction Reversed";
				status = "Reversed";
				result.addParam("status", status);
				result.addParam("message", message);
				// intrabankDTO.setReferenceId(id);
			} else {
				status = "Transaction Reverse Failed";
				message = transactionMessage;
				result.addParam("status", status);
				result.addParam("message", message);
			}
		} catch (DBPApplicationException e) {
			LOG.debug("Exception Occured while reversing the transaction" + e.toString());
			status = "Transaction Reverse Failed";
			message = e.getLocalizedMessage();
			result.addParam("status", status);
			result.addParam("message", message);
			result.addParam("dbpErrCode", "12601");
			result.addParam("dbpErrMsg", "transaction creation is failed at backend");
		}
		Map<String, Object> confirmationDetails = new HashMap<String, Object>();
		confirmationDetails.put("status", status);
		confirmationDetails.put("confirmationNumber", transactionId);
		updateIntraBankTransaction(request,transactionId,status);

		return result;
	}

	private int updateIntraBankTransaction(DataControllerRequest request, String transactionId, String status) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		LOG.debug("transactionId:" + transactionId);
		LOG.debug("status:" + status);
		inputParams.put("transactionId", transactionId);
		inputParams.put("status", status);
		String serviceName = "dbpRbLocalServicesdb";
		String operationName = "dbxdb_intrabanktransfers_update";
		int isSuccess = 0;
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post update updateIntraBankTransaction");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't update status in updateIntraBankTransaction due to   : " + errMessage);
			isSuccess = 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					isSuccess = 1;
				}
			} catch (Exception e) {
				LOG.debug("Couldn't update status in updateIntraBankTransaction");
				isSuccess = 0;
			}
		}
		return isSuccess;

	}

}
