package com.temenos.infinity.api.QRPayments.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.QRPayments.backenddelegate.api.QRPaymentBackendDelegate;
import com.temenos.infinity.api.QRPayments.constants.Constants;
import com.temenos.infinity.api.QRPayments.dto.QRPaymentDTO;
import com.temenos.infinity.api.srmstransactions.config.SRMSTypeSubTypeConfiguration;
import com.temenos.infinity.api.srmstransactions.config.SrmsTransactionsAPIServices;

public class QRPaymentBackendDelegateImpl implements Constants, QRPaymentBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public QRPaymentDTO createQRPayment(Map<String, Object> requestParameters, DataControllerRequest request) {

		HashMap<String, Object> srmsParams = new HashMap<String, Object>();
		QRPaymentDTO input = null;
		try {
			QRPaymentDTO paymentDTO = JSONUtils.parse(new JSONObject(requestParameters).toString(), QRPaymentDTO.class);

			String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue("PAYMENT_BACKEND");
			String createOrderResponse = "";
			if("SRMS".equalsIgnoreCase(PAYMENT_BACKEND)) {
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.QR_PAYMENT.getType());
				srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.QR_PAYMENT.getSubType());
				srmsParams.put(PARAM_REQUEST_BODY, new JSONObject(paymentDTO).toString().replaceAll("\"", "'"));
				
				alert.prepareError("QR Payment SRMS Input :" + srmsParams.toString()).log();
				diagnostic.prepareDebug("QR Payment Order Request :" + srmsParams.toString()).log();
				HashMap<String, Object> headerMap = new HashMap<String, Object>();
				headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
				headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

				// Making a call to order request API

				createOrderResponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(SrmsTransactionsAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
						.withOperationId(SrmsTransactionsAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
						.withRequestParameters(srmsParams).withRequestHeaders(headerMap).withDataControllerRequest(request)
						.build().getResponse();
			} else {
				//Making a call to Stub API
				createOrderResponse = DBPServiceExecutorBuilder.
						builder().
						withServiceId("MOCKQRPaymentAPIs").
						withObjectId(null).
						withOperationId("createQRPayment").
						withRequestParameters(requestParameters).withRequestHeaders(request.getHeaderMap()).withDataControllerRequest(request)
						.build().getResponse();
			}
			
			JSONObject responseObject = null;
			if (StringUtils.isNotBlank(createOrderResponse)) {
				responseObject = new JSONObject(createOrderResponse);
				if (!responseObject.has("dbpErrMsg")) {
					responseObject.put("referenceId", responseObject.get("orderId").toString());
				}
				if (responseObject.has("additionalInfo")
						&& StringUtils.isNotBlank(responseObject.getString("additionalInfo"))) {
					responseObject.put("messageDetails", responseObject.get("additionalInfo").toString());
				}
				if (responseObject.has("errorDetail")
						&& StringUtils.isNotBlank(responseObject.getString("errorDetail"))) {
					responseObject.put("errorDetails", responseObject.get("errorDetail").toString());
				}
			}
			createOrderResponse = responseObject.toString();
			input = JSONUtils.parse(createOrderResponse, QRPaymentDTO.class);

		} catch (Exception e) {
			alert.prepareError("Caught exception at create transaction without approval: ", e).log();

		}

		return input;
	}
}
