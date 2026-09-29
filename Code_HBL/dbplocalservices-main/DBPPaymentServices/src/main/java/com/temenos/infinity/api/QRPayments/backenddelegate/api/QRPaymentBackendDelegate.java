package com.temenos.infinity.api.QRPayments.backenddelegate.api;

import java.util.Map;
import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.QRPayments.dto.QRPaymentDTO;

public interface QRPaymentBackendDelegate extends BackendDelegate {

	public QRPaymentDTO createQRPayment(Map<String, Object> requestParameters, DataControllerRequest request);

}
