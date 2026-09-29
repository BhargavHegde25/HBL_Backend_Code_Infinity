package com.kony.adminconsole.service.customer.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.customer.backenddelegate.api.InfinityCustomerBackendDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class InfinityCustomerBackendDelegateImpl implements InfinityCustomerBackendDelegate {

	@Override
	public JSONObject getConvertedAmount(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {

		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CURRENCY_MANAGEMENT)
                        .withOperationId(OperationName.OP_GETCONVERTEDAMOUNT)
                        .withRequestParameters(postParametersMap)
                        .build()
                        .getResponse();
        
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

}
