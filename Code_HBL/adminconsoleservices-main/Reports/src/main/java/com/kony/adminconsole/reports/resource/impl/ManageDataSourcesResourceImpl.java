package com.kony.adminconsole.reports.resource.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.reports.businessdelegate.api.ManageDataSourcesBusinessDelegate;
import com.kony.adminconsole.reports.resource.api.ManageDataSourcesResource;
import com.kony.adminconsole.reports.utilities.ErrorCodeEnum;
import com.kony.adminconsole.reports.utilities.ReportsConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ManageDataSourcesResourceImpl implements ManageDataSourcesResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	private static final String GET_DATASOURCES_LIST_OPERATION_NAME = "getDataSourcesList";

	@Override
	public Result getDataSourcesList(String methodID, Object[] inputArray,
			DataControllerRequest request,
			DataControllerResponse response) {

		Result processedResult = new Result();
		try {
			String authToken = request.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
			String userID = userDetailsBeanInstance.getId();

			ManageDataSourcesBusinessDelegate dataSourcesBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ManageDataSourcesBusinessDelegate.class);
			JSONObject getDataSourcesListResponseJSON = dataSourcesBusinessDelegate.getDataSourcesList(userID,authToken);
			if (getDataSourcesListResponseJSON != null && getDataSourcesListResponseJSON.has("reportdatasource")) {
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.REPORTS, EventEnum.SEARCH,
						ActivityStatusEnum.SUCCESSFUL, "Get Data sources list successful");
				processedResult = CommonUtilities.getResultObjectFromJSONObject(getDataSourcesListResponseJSON);
				processedResult.addOpstatusParam(0);
				processedResult.addHttpStatusCodeParam(200);
			}
			else {
				alert.prepareError("Failed CRUD Operation.Response:" + getDataSourcesListResponseJSON).log();
				processedResult.addParam(new Param(ReportsConstants.STATUS, ReportsConstants.FAILURE, FabricConstants.STRING));
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.REPORTS, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "Get Data sources list falied");
				ErrorCodeEnum.ERR_29006.setErrorCode(processedResult);
			}
			return processedResult;
		} catch (Exception e) {
			processedResult = new Result();
			alert.prepareError("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_29006.setErrorCode(processedResult);
			return processedResult;
		}
	}
}
