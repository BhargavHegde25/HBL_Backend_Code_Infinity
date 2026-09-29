package com.temenos.infinity.api.QRPayments.businessdelegate.impl;

import java.io.IOException;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.QRPayments.backenddelegate.api.QRPaymentBackendDelegate;
import com.temenos.infinity.api.QRPayments.businessdelegate.api.QRPaymentBusinessDelegate;
import com.temenos.infinity.api.QRPayments.dto.QRPaymentDTO;

public class QRPaymentBusinessDelegateImpl implements QRPaymentBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private final QRPaymentBackendDelegate QRPaymentBackendDelegate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(QRPaymentBackendDelegate.class);

	public QRPaymentDTO createQRPayment(QRPaymentDTO QRPaymentDTO, DataControllerRequest request) {
		QRPaymentDTO QRPayment = null;
		Map<String, Object> requestParameters;

		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(QRPaymentDTO).toString(), String.class,
					Object.class);
			QRPayment = QRPaymentBackendDelegate.createQRPayment(requestParameters, request);
		} catch (IOException e) {
			alert.prepareError("Exception occured while creating record at backend", e).log();
		}

		return QRPayment;
	}
}
