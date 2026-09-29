package com.temenos.infinity.product.bulkpaymentservices.businessdelegate.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api.BulkPaymentPOBusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentPODTO;
import com.kony.dbputilities.util.DBPUtilitiesConstants;

public class BulkPaymentPOBusinessDelegateImpl implements BulkPaymentPOBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);

	@Override
	public BulkPaymentPODTO createPaymentOrder(BulkPaymentPODTO bulkPaymentPODTO) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTSUBRECORD_CREATE;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkPaymentPODTO).toString(), String.class,
					Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the request params: ", e).log();
			return null;
		}

		String createResponse = null;
		try {
			requestParameters.put("createdts",new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			createResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();

			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			bulkPaymentPODTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentPODTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to create payment order at bulkpaymentPO table: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createPaymentOrder: ", e).log();
			return null;
		}

		return bulkPaymentPODTO;
	}

	@Override
	public List<BulkPaymentPODTO> fetchPaymentOrders(String recordId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTSUBRECORD_GET;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "recordId" + DBPUtilitiesConstants.EQUAL + recordId;
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		List<BulkPaymentPODTO> paymentOrderLi = null;
		String poResponse = null;
		try {
			poResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(poResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			paymentOrderLi = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentPODTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch payment orders by recordId: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchPaymentOrders: ", e).log();
			return null;
		}

		return paymentOrderLi;
	}

	@Override
	public Boolean deletePaymentOrder(String recordId, String paymentOrderId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTSUBRECORD_DELETE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("recordId", recordId);
		requestParams.put("paymentOrderId", paymentOrderId);

		try {
			String deleteResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParams)
					.build().getResponse();
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if (!(jsonRsponse.getString("confirmationNumber").isEmpty())
					&& !(jsonRsponse.getString("status").isEmpty())) {
				return true;
			}
		} catch (JSONException je) {
			alert.prepareError("Failed to delete the payment order at bulkpaymentPO table", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at deletePaymentOrder: ", e).log();
			return null;
		}

		return false;
	}

	@Override
	public BulkPaymentPODTO updatePaymentOrder(BulkPaymentPODTO bulkPaymentPODTO) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTSUBRECORD_UPDATE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("recordId", bulkPaymentPODTO.getRecordId());
		requestParams.put("paymentOrderId", bulkPaymentPODTO.getPaymentOrderId());
		requestParams.put("currency", bulkPaymentPODTO.getCurrency());
		requestParams.put("amount", bulkPaymentPODTO.getAmount());
		requestParams.put("feesPaidBy", bulkPaymentPODTO.getFeesPaidBy());
		requestParams.put("paymentReference", bulkPaymentPODTO.getPaymentReference());
		String editResponse = null;

		try {
			editResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParams)
					.build().getResponse();

			JSONObject response = new JSONObject(editResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			bulkPaymentPODTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentPODTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to edit payment order at bulkpaymentPO table: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at updatePaymentOrder: ", e).log();
			return null;
		}

		return bulkPaymentPODTO;
	}
}
