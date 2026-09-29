package com.temenos.infinity.tradesupplyfinance.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.AnchorInvoiceResource;

public class ApproveAnchorInvoiceOperation implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        AnchorInvoiceResource requestResource = DBPAPIAbstractFactoryImpl.getResource(AnchorInvoiceResource.class);
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        AnchorInvoiceDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), AnchorInvoiceDTO.class);
        return requestResource.approveAnchorInvoice(inputDto, request);
	}

}
