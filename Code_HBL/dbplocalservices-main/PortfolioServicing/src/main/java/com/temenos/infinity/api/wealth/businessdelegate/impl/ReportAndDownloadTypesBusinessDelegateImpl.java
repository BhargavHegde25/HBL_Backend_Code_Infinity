/**
 * 
 */
package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.ReportAndDownloadTypesBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.ReportAndDownloadTypesBusinessDelegate;

/**
 * @author m.ashwath
 *
 */
public class ReportAndDownloadTypesBusinessDelegateImpl implements ReportAndDownloadTypesBusinessDelegate {

	@Override
	public Result getReportAndDownloadTypes(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		ReportAndDownloadTypesBackendDelegate reportAndDownloadTypesBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(ReportAndDownloadTypesBackendDelegate.class);
		result = reportAndDownloadTypesBackendDelegate.getReportAndDownloadTypes(methodID, inputArray, request, response, headersMap);
		return result;
	}
}
