package com.kony.adminconsole.service.productmanagement.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.preprocessor.ProductsAuthorizationPreProcessor;
import com.kony.adminconsole.service.productmanagement.businessdelegate.api.FacilityBusinessDelegate;
import com.kony.adminconsole.service.productmanagement.preprocessor.MCMSTokenGeneration;
import com.kony.adminconsole.service.productmanagement.resource.api.FacilityResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class FacilityResourceImpl implements FacilityResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String INPUT_FACILITY_ID = "facilityId";
	private static final String INPUT_CODE = "code";
	private static final String INPUT_FACILITY_NAME = "facilityName";
	private static final String INPUT_DESCRIPTION = "description";
	private static final String INPUT_FEATURES = "features";
	private static final int FACILITY_NAME_MIN_LENGTH = 1;
	private static final int FACILITY_NAME_MAX_LENGTH = 50;
	private static final int FACILITY_DESC_MIN_LENGTH = 1;
	private static final int FACILITY_DESC_MAX_LENGTH = 300;
	public static final String PARAM_AUTHORIZATION = "Authorization";
	
	
	FacilityBusinessDelegate facilityBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(FacilityBusinessDelegate.class);
	
	ProductsAuthorizationPreProcessor authObj = new ProductsAuthorizationPreProcessor();
	//	MCMSTokenGeneration authObj = new MCMSTokenGeneration();
	@Override
	public Result createFacility(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		String code = StringUtils.EMPTY;
		String facilityName = StringUtils.EMPTY;
		String description = StringUtils.EMPTY;
		String features = StringUtils.EMPTY;
		HashMap<String,String> map = new HashMap<>();
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CODE))) {
	            
				ErrorCodeEnum.ERR_22128.setErrorCode(result);
				return  result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_FACILITY_NAME))) {
	            
				ErrorCodeEnum.ERR_22133.setErrorCode(result);
				return  result;
	        }
			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_FEATURES))) {
	            
				features = requestInstance.getParameter(INPUT_FEATURES);
	        }
			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_DESCRIPTION))) {
				
				description = StringEscapeUtils.escapeHtml(requestInstance.getParameter(INPUT_DESCRIPTION));
			}
			
			
			code = StringEscapeUtils.escapeHtml(requestInstance.getParameter(INPUT_CODE));
			facilityName= StringEscapeUtils.escapeHtml(requestInstance.getParameter(INPUT_FACILITY_NAME));
					
			if (StringUtils.length(facilityName) < FACILITY_NAME_MIN_LENGTH
					|| StringUtils.length(facilityName) > FACILITY_NAME_MAX_LENGTH) {
				ErrorCodeEnum.ERR_22237.setErrorCode(result);
				return  result;
			}
			
			if (StringUtils.length(description) < FACILITY_DESC_MIN_LENGTH
					|| StringUtils.length(description) > FACILITY_DESC_MAX_LENGTH) {
				ErrorCodeEnum.ERR_22238.setErrorCode(result);
				return  result;
			}
			
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("code", code);
	    	postParametersMap.put("facilityName", facilityName);
	    	postParametersMap.put("description", description);
	    	postParametersMap.put("features", features);
	        
	        JSONObject serviceResponse =
	        		facilityBusinessDelegate.createFacility(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22118.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to create Facility: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create Facility for, facilityName: "+ facilityName);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create Facility for, facilityName: "+ facilityName+
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL, "Facility Creation successful, facilityName: "+ facilityName);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in createFacility", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22124.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Failed to create Facility, facilityName: "+ facilityName);
        }

        return result;
	}

	@Override
	public Result editFacility(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String facilityId = StringUtils.EMPTY;
		String facilityName = StringUtils.EMPTY;
		String description = StringUtils.EMPTY;
		String features = StringUtils.EMPTY;
		HashMap<String,String> map = new HashMap<>();
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_FACILITY_ID))) {
	            				
				ErrorCodeEnum.ERR_22134.setErrorCode(result);
				return  result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_FACILITY_NAME))) {
	            
				ErrorCodeEnum.ERR_22133.setErrorCode(result);
				return  result;
	        }
			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_DESCRIPTION))) {
				
				description = StringEscapeUtils.escapeHtml(requestInstance.getParameter(INPUT_DESCRIPTION));
			}
			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_FEATURES))) {
	            
				features = requestInstance.getParameter(INPUT_FEATURES);
	        }
			
			facilityId = requestInstance.getParameter(INPUT_FACILITY_ID);
			facilityName = StringEscapeUtils.escapeHtml(requestInstance.getParameter(INPUT_FACILITY_NAME));
			
			
			if (StringUtils.length(facilityName) < FACILITY_NAME_MIN_LENGTH
					|| StringUtils.length(facilityName) > FACILITY_NAME_MAX_LENGTH) {
				ErrorCodeEnum.ERR_22237.setErrorCode(result);
				return  result;
			}
			
			if (StringUtils.length(description) < FACILITY_DESC_MIN_LENGTH
					|| StringUtils.length(description) > FACILITY_DESC_MAX_LENGTH) {
				ErrorCodeEnum.ERR_22238.setErrorCode(result);
				return  result;
			}
			
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
					
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("facilityId", facilityId);
	    	postParametersMap.put("facilityName", facilityName);
	    	postParametersMap.put("description", description);
	    	postParametersMap.put("features", features);
	        
	        JSONObject serviceResponse =
	        		facilityBusinessDelegate.editFacility(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22125.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to update Facility: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to editFacility for facilityName: "+ facilityName);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update Facility for facilityName: "+ facilityName +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Facility Update successful for facilityName: "+ facilityName);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in editFacility", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22118.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to update Facility for facilityName: "+ facilityName);
        }

        return result;
	}

	@Override
	public Result getFacility(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		String facilityId = StringUtils.EMPTY;
		HashMap<String,String> map = new HashMap<>();
		
		try {
			

			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_FACILITY_ID))) {
	            
				facilityId = requestInstance.getParameter(INPUT_FACILITY_ID);
	        }

			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
					
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	if(StringUtils.isNotBlank(facilityId)) {
	    		postParametersMap.put(INPUT_FACILITY_ID, facilityId);
	    	}
	    	
	    	String marketingCatalogBackend = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",requestInstance);
	    	postParametersMap.put("marketingCatalogBackend", marketingCatalogBackend);
	        
	        JSONObject serviceResponse =
	        		facilityBusinessDelegate.getFacility(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22126.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch Facility: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch getFacility for facilityId: "+ facilityId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch Facility for facilityId: "+ facilityId+
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            	
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully fetch facility for facilityId: "+ facilityId);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in createFacility", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22126.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.FACILITY, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Facility for facilityId: "+ facilityId);
        }

        return result;
	}

}
