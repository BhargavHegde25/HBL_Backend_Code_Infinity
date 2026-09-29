package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.businessdelegate.impl.BillPayTransactionBusinessDelegateImpl;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class BillPayTransactionBusinessDelegateImplExtn extends BillPayTransactionBusinessDelegateImpl {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	
	@Override
	public  BillPayTransactionDTO createTransactionAtDBX(BillPayTransactionDTO billpayTransactionDTO) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BILLPAYTRANSFERS_CREATE;
		String createResponse = null;
		
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(billpayTransactionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}
		try {
			requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			requestParameters.put("transactionId", HelperMethods.getUniqueNumericString(13));
			HashMap<String, Object> externalpayload = billpayTransactionDTO.getExternalApiPayload();
			String paymentId= externalpayload.get("paymentId")!=null?externalpayload.get("paymentId").toString():"";
			externalpayload.remove("paymentId");
			LOG.debug("BillPayTransactionBusinessDelegateImplExtn:createTransactionAtDBX: externalpayload:" + externalpayload);
			JSONObject externalServicePayload = new JSONObject(externalpayload);
			requestParameters.put("externalServicePayload",externalServicePayload.toString());
			requestParameters.put("paymentId",paymentId);
			LOG.debug("BillPayTransactionBusinessDelegateImplExtn:createTransactionAtDBX: requestParameters:" + requestParameters);
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);
			
			billpayTransactionDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), BillPayTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create billpay transaction entry into billpaytransfers table: ", e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create billpay transaction entry: ", e).log();
			return null;
		}
		
		return billpayTransactionDTO;
	}

}
