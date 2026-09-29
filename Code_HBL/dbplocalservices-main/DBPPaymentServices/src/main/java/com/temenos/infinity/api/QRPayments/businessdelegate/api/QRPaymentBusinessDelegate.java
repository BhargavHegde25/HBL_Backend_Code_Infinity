package com.temenos.infinity.api.QRPayments.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.QRPayments.dto.QRPaymentDTO;

public interface QRPaymentBusinessDelegate extends BusinessDelegate {

	public QRPaymentDTO createQRPayment(QRPaymentDTO QRPayemntDTO, DataControllerRequest request);

}
