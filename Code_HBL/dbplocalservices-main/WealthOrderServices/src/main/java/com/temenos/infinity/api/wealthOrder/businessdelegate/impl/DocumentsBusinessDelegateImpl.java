package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.DocumentsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.DocumentsBusinessDelegate;

public class DocumentsBusinessDelegateImpl implements DocumentsBusinessDelegate {

	@Override
	public Result getDocuments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		DocumentsBackendDelegate documentsBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(DocumentsBackendDelegate.class);
		result = documentsBackendDelegate.getDocuments(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
