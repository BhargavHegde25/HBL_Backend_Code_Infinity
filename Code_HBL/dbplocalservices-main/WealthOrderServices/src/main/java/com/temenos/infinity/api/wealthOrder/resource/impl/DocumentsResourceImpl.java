package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.DocumentsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.DocumentsResource;

public class DocumentsResourceImpl implements DocumentsResource {

	@Override
	public Result getDocuments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_NEWS_AND_DOCUMENTS_DOCUMENTS_VIEW")) {
			// ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		Map<String, Object> headers = request.getHeaderMap();
		DocumentsBusinessDelegate documentsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(DocumentsBusinessDelegate.class);
		result = documentsBusinessDelegate.getDocuments(methodID, inputArray, request, response, headers);
		return result;
	}

}
