package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.ReportAndDownloadTypesBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.ReportAndDownloadTypesResource;

public class ReportAndDownloadTypesResourceImpl implements ReportAndDownloadTypesResource {

	@Override
	public Result getReportAndDownloadTypes(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if ((!userPermissions.contains("WEALTH_REPORT_MANAGEMENT_REPORT_CREATE"))
				&& (!userPermissions.contains("WEALTH_REPORT_MANAGEMENT_REPORT_DOWNLOAD"))) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}
		
		ReportAndDownloadTypesBusinessDelegate reportAndDownloadTypesBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ReportAndDownloadTypesBusinessDelegate.class);
		result = reportAndDownloadTypesBusinessDelegate.getReportAndDownloadTypes(methodID, inputArray, request,
				response, headersMap);
		return result;
	}

}
