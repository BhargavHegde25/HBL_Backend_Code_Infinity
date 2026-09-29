package com.kony.adminconsole.campaign.resource.impl;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.campaign.businessdelegate.api.DataStorageBusinessDelegate;
import com.kony.adminconsole.campaign.resource.CustomerManagementResource;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


public class CustomerManagementResourceImpl implements CustomerManagementResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Result getCustomerApplications(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Boolean isSME) {
		try {
			Result processedResult = new Result();
			
			String customerId = request.getParameter("Customer_id");
			String applicationStatus = request.getParameter("Application_status");
			boolean isCDPFlow = (request.getParameter("isCDPFlow")!=null && request.getParameter("isCDPFlow").equalsIgnoreCase("true") )?true:false  ;
			String entityDefinitionCode = isSME ? EnvironmentConfiguration.SME_ONBOARDING_ENTITY_DEFINTION.getValue(request) : EnvironmentConfiguration.ONBOARDING_ENTITY_DEFINTION.getValue(request);

			// Checking mandatory fields
			if (StringUtils.isBlank(customerId)) {
				ErrorCodeEnum.ERR_20565.setErrorCode(processedResult);
				return processedResult;
			}

			if (StringUtils.isBlank(applicationStatus) && !isCDPFlow) {
				ErrorCodeEnum.ERR_20148.setErrorCode(processedResult);
				return processedResult;
			}
			
			if (StringUtils.isBlank(entityDefinitionCode)) {
				ErrorCodeEnum.ERR_20152.setErrorCode(processedResult);
				return processedResult;
			}
			
			diagnostic.prepareInfo("CustomerManagementResourceImpl customerID :"+customerId).log();
			diagnostic.prepareInfo("CustomerManagementResourceImpl applicationStatus :"+applicationStatus).log();
			diagnostic.prepareInfo("CustomerManagementResourceImpl entityDefinitionCode :"+entityDefinitionCode).log();
			
			JSONObject requestPayload = new JSONObject();
			requestPayload.put("applicationStatus", applicationStatus);
			requestPayload.put("entityDefinitionCode", entityDefinitionCode);
			requestPayload.put("customerId", customerId);
			requestPayload.put("isCDPFlow",isCDPFlow);
			
			
			DataStorageBusinessDelegate storageBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(DataStorageBusinessDelegate.class); 
			
			String deploymentPlatform = EnvironmentConfiguration.ODMS_DEPLOYMENT_PLATFORM.getValue(request);
			JSONObject customerapplications = storageBusinessDelegate.getCustomerApplications(requestPayload,isSME, deploymentPlatform); 
			if (customerapplications == null || !customerapplications.has("customerapplications")) {
				alert.prepareError("Failed to fetch customerapplications for payload :"+ requestPayload).log();
				ErrorCodeEnum.ERR_21868.setErrorCode(processedResult);
				return processedResult;
			}
			processedResult = CommonUtilities.getResultObjectFromJSONObject(customerapplications);
			processedResult.addOpstatusParam(0);
			processedResult.addHttpStatusCodeParam(200);
			return processedResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_21868.setErrorCode(errorResult);
			return errorResult;
		}
	}

}
