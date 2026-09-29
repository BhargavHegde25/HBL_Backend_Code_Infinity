package com.kony.adminconsole.service.termandcondition.resource.impl;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.termandcondition.businessdelegate.api.TnCBusinessDelegate;
import com.kony.adminconsole.service.termandcondition.preprocessor.TNCTokenPreProcessor;
import com.kony.adminconsole.service.termandcondition.resource.api.TnCResource;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class TnCResourceImpl implements TnCResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final int TERM_AND_CONDITION_TITLE_MIN_CHARACTERS = 3;
	private static final int TERM_AND_CONDITION_TITLE_MAX_CHARACTERS = 50;
	private static final int TERM_AND_CONDITION_DESCRIPTION_MAX_CHARACTERS = 250;
	private static final int TERM_AND_CONDITION_VERSION_DESCRIPTION_MAX_CHARACTERS = 250;

	private static final String DEFAULT_LANGUAGE_CODE = "en-US";
	private static final String DEFAULT_TERM_AND_CONDITION_CODE = "DBX_Default_TnC";
	private static final String DEFAULT_APP = "RETAIL_AND_BUSINESS_BANKING";
	TnCBusinessDelegate tncBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(TnCBusinessDelegate.class);
	
	TNCTokenPreProcessor authObj = new TNCTokenPreProcessor();
	public static final String PARAM_AUTHORIZATION = "Authorization";

	@Override
	public Result createTermsAndConditionsVersion(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		String termAndConditionCode = StringUtils.EMPTY;;
		String languageCode = StringUtils.EMPTY;;
		String contentType = StringUtils.EMPTY;;
		String termAndConditionVersionDescription = StringUtils.EMPTY;;
		String termAndConditionContent = StringUtils.EMPTY;;
		String isSave = StringUtils.EMPTY;
		String leId = StringUtils.EMPTY;
		UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
		
		try {
			termAndConditionCode = requestInstance.getParameter("termsAndConditionsCode");
			languageCode = requestInstance.getParameter("languageCode");
			contentType = requestInstance.getParameter("contentType");
			termAndConditionVersionDescription = requestInstance.getParameter("versionDescription");
			termAndConditionContent = requestInstance.getParameter("termsAndConditionsContent");
			termAndConditionContent = StringEscapeUtils.escapeJava(termAndConditionContent);
			isSave = requestInstance.getParameter("isSave");
			leId = requestInstance.getParameter("legalEntityId");
			String auditSuccessDescription = "Terms and conditions version create successful. Code: "
					+ termAndConditionCode + "/" + "Language Code: " + languageCode + "/" + "Content Type: "
					+ contentType + "/" + "Description: " + termAndConditionVersionDescription;
			if (StringUtils.isBlank(termAndConditionCode)) {
				ErrorCodeEnum.ERR_20266.setErrorCode(result);
				alert.prepareError("Term and condition code is a mandatory input").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.CREATE,
						ActivityStatusEnum.FAILED,
						"Terms and Conditions create failed. Code: " + termAndConditionCode + "/" + "Language Code: "
								+ languageCode + "/" + "Content Type: " + contentType + "/" + "Description: "
								+ termAndConditionVersionDescription);
				return result;
			}

			if (StringUtils.isBlank(languageCode)) {
				languageCode = DEFAULT_LANGUAGE_CODE;
			}
			if (StringUtils.isBlank(contentType)) {
				ErrorCodeEnum.ERR_20272.setErrorCode(result);
				alert.prepareError("Terms and conditions content type is a mandatory input").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.CREATE,
						ActivityStatusEnum.FAILED,
						"Terms and Conditions version create failed. Code: " + termAndConditionCode + "/"
								+ "Language Code: " + languageCode + "/" + "Content Type: " + contentType + "/"
								+ "Description: " + termAndConditionVersionDescription);
				return result;
			}
			if (StringUtils.isBlank(termAndConditionContent)) {
				ErrorCodeEnum.ERR_20270.setErrorCode(result);
				alert.prepareError("Terms and conditions content is a mandatory input").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.CREATE,
						ActivityStatusEnum.FAILED,
						"Terms and Conditions version create failed. Code: " + termAndConditionCode + "/"
								+ "Language Code: " + languageCode + "/" + "Content Type: " + contentType + "/"
								+ "Description: " + termAndConditionVersionDescription);
				return result;
			}
			if (StringUtils.length(
					termAndConditionVersionDescription) > TERM_AND_CONDITION_VERSION_DESCRIPTION_MAX_CHARACTERS) {
				ErrorCodeEnum.ERR_20277.setErrorCode(result);
				alert.prepareError("Terms and conditions version description length is not in range").log();
				result.addParam(new Param("message",
						"Terms and conditions version description should have a maximum of "
								+ TERM_AND_CONDITION_VERSION_DESCRIPTION_MAX_CHARACTERS + " characters",
								FabricConstants.STRING));
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.CREATE,
						ActivityStatusEnum.FAILED,
						"Terms and Conditions version create failed. Code: " + termAndConditionCode + "/"
								+ "Language Code: " + languageCode + "/" + "Content Type: " + contentType + "/"
								+ "Description: " + termAndConditionVersionDescription);
				return result;
			}



			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("termAndConditionCode", termAndConditionCode);	        
			postParametersMap.put("languageCode", languageCode);
			postParametersMap.put("contentType", contentType);
			postParametersMap.put("termAndConditionVersionDescription", termAndConditionVersionDescription);
			postParametersMap.put("termAndConditionContent", termAndConditionContent);
			postParametersMap.put("isSave", isSave);
			postParametersMap.put("legalEntityId", leId);
			String loggedInUserDetailsName =userDetailsBeanInstance.getUserName();
			String loggedInUserId =userDetailsBeanInstance.getId();
			
			postParametersMap.put("loggedInUserDetailsName", loggedInUserDetailsName);
			postParametersMap.put("loggedInUserId", loggedInUserId);
			
			boolean isAuthSuccess = authObj.execute(null, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22151.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			
			result =
					tncBusinessDelegate.createTermsAndConditionsVersion(postParametersMap, backendToken);
			
			return result;
			 
		}catch (Exception e) {
			alert.prepareError("Unexpected Error in createTermsAndConditionsVersion", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22088.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "Term And Condition Creation failed: tncCode:"+termAndConditionCode);
		}

		return result;
	}
	@Override
	public Result deleteTermsAndConditionsVersion(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {

		Result result = new Result();
		String termAndConditionCode = StringUtils.EMPTY;
		String languageCode = StringUtils.EMPTY;
		String leId = StringUtils.EMPTY;
		try {

			termAndConditionCode = requestInstance.getParameter("termsAndConditionsCode");
			languageCode = requestInstance.getParameter("languageCode");
			leId = requestInstance.getParameter("legalEntityId");

			String auditSuccessDescription = "Terms and conditions draft version delete successful. Code: "
					+ termAndConditionCode + "/" + "Language Code: " + languageCode + "/" + "Version: N/A";

			if (StringUtils.isBlank(termAndConditionCode)) {
				ErrorCodeEnum.ERR_20266.setErrorCode(result);
				alert.prepareError("Term and condition code is a mandatory input").log();
				alert.prepareError("Failed to delete Terms and Conditions draft version").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "Failed to delete Terms and Conditions draft version");
				return result;
			}

			if (StringUtils.isBlank(languageCode)) {
				languageCode = DEFAULT_LANGUAGE_CODE;
			}
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("termAndConditionCode", termAndConditionCode);
			postParametersMap.put("languageCode", languageCode);
			postParametersMap.put("legalEntityId", leId);
			String consentBackend = EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND");
			if(ACConstants.DBXDB_BACKEND.equalsIgnoreCase(consentBackend)) {
				result = tncBusinessDelegate.deleteTermsAndConditionsVersionDBXDB(postParametersMap);
			}else {
				boolean isAuthSuccess = authObj.execute(null, requestInstance,response,result);
		    	
				if(!isAuthSuccess) {
					ErrorCodeEnum.ERR_22151.setErrorCode(result);
					return result;
				}
				String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
				result = tncBusinessDelegate.deleteTermsAndConditionsVersion(postParametersMap, backendToken);
			}
			return result;
			
		}catch (Exception e) {
			alert.prepareError("Unexpected Error in deleteTermsAndConditionsVersion", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22090.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.DELETE,
					ActivityStatusEnum.FAILED, "Term And Condition Delete failed: termAndConditionCode:"+termAndConditionCode);
		}

		return result;
	}

	@Override
	public Result getAllTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String leId = requestInstance.getParameter("legalEntityId");
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("legalEntityId", leId);

			boolean isAuthSuccess = authObj.execute(null, requestInstance, response, result);

			if (!isAuthSuccess) {
				ErrorCodeEnum.ERR_22151.setErrorCode(result);
				return result;
			}

			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);

			String consentBackend = EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND");

			if (ACConstants.DBXDB_BACKEND.equalsIgnoreCase(consentBackend)) {
				result = tncBusinessDelegate.getAllTermsAndConditions(postParametersMap, backendToken, requestInstance,
						true);
			} else {
				result = tncBusinessDelegate.getAllTermsAndConditions(postParametersMap, backendToken);
			}
			return result;
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in getAllTermsAndConditions", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22094.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Failed to fetch terms and conditions");
		}
		return result;
	}

	@Override
	public Result getTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {

		Result result = new Result();
		String termAndConditionCode = StringUtils.EMPTY, languageCode = StringUtils.EMPTY, termAndConditonId = StringUtils.EMPTY;
		try {


			termAndConditionCode = requestInstance.getParameter("termsAndConditionsCode");
			languageCode = requestInstance.getParameter("languageCode");
			String appId = requestInstance.getParameter("appId");
			String leId = requestInstance.getParameter("legalEntityId");
			Map<String, Object> postParametersMap = new HashMap<>();

			// If termAndConditionCode is not passed we'll send default T&C
			if (StringUtils.isBlank(termAndConditionCode)) {
				termAndConditionCode = DEFAULT_TERM_AND_CONDITION_CODE;
			}

			// If languageCode is not passed we'll set default languageCode
			if (StringUtils.isBlank(languageCode)) {
				languageCode = DEFAULT_LANGUAGE_CODE;
			}
			if (StringUtils.isBlank(appId)) {
				appId = DEFAULT_APP;
			}
			postParametersMap.put("termAndConditionCode", termAndConditionCode);	        
			postParametersMap.put("languageCode", languageCode);
			postParametersMap.put("appId", appId);
			postParametersMap.put("legalEntityId", leId);
			
			boolean isAuthSuccess = authObj.execute(null, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22151.setErrorCode(result);
				return result;
			}
			
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			
			result = tncBusinessDelegate.getTermsAndConditions(postParametersMap, backendToken);
			
		}catch (Exception e) {
			alert.prepareError("Unexpected Error in deleteSignatoryGroup", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22095.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Failed fetch getTermsAndConditions: termAndConditionCode:"+termAndConditionCode);
		}

		return result;

	}

	@Override
	public Result editTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		String loggedInUserId  = StringUtils.EMPTY;
		String termAndConditionCode =  StringUtils.EMPTY;
		try {
			UserDetailsBean loggedInUserDetails = LoggedInUserHandler.getUserDetails(requestInstance);
			if (!StringUtils.isBlank(loggedInUserId)) {
				loggedInUserId = loggedInUserDetails.getId();
			}

			termAndConditionCode = requestInstance.getParameter("termsAndConditionsCode");
			String languageCode = requestInstance.getParameter("languageCode");
			String contentType = requestInstance.getParameter("contentType");
			String termAndConditionTitle = requestInstance.getParameter("termsAndConditionsTitle");
			String termAndConditionDescription = requestInstance.getParameter("termsAndConditionsDescription");
			String termAndConditionContent = requestInstance.getParameter("termsAndConditionsContent");
			String leId = requestInstance.getParameter("legalEntityId");
			String auditSuccessDescription = "Terms and conditions update successful. Code: " + termAndConditionCode
					+ "/" + "Language Code: " + languageCode + "/" + "Content Type: " + contentType + "/" + "Title: "
					+ termAndConditionTitle + "/" + "Description: " + termAndConditionDescription;

			if (StringUtils.isBlank(termAndConditionCode)) {
				ErrorCodeEnum.ERR_20266.setErrorCode(result);
				alert.prepareError("Term and condition code is a mandatory input").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Terms and Conditions update failed. Code: " + termAndConditionCode + "/" + "Language Code: "
								+ languageCode + "/" + "Title: " + termAndConditionTitle + "/" + "Description: "
								+ termAndConditionDescription);
				return result;
			}

			if (StringUtils.isBlank(languageCode)) {
				languageCode = DEFAULT_LANGUAGE_CODE;
			}

			// Making contentType as non mandatory for backward compatibility
			/*
			 * if (StringUtils.isBlank(contentType)) { ErrorCodeEnum.ERR_20272.setErrorCode(result);
			 * alert.prepareError("Terms and conditions content type is a mandatory input").log();
			 * AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.UPDATE,
			 * ActivityStatusEnum.FAILED, "Terms and Conditions update failed."); return result; }
			 */

			if (StringUtils.isBlank(termAndConditionTitle)) {
				ErrorCodeEnum.ERR_20267.setErrorCode(result);
				alert.prepareError("Terms and conditions title is a mandatory input").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Terms and Conditions update failed. Code: " + termAndConditionCode + "/" + "Language Code: "
								+ languageCode + "/" + "Title: " + termAndConditionTitle + "/" + "Description: "
								+ termAndConditionDescription);
				return result;
			}

			if (StringUtils.length(termAndConditionTitle) < TERM_AND_CONDITION_TITLE_MIN_CHARACTERS
					|| StringUtils.length(termAndConditionTitle) > TERM_AND_CONDITION_TITLE_MAX_CHARACTERS) {
				ErrorCodeEnum.ERR_20269.setErrorCode(result);
				alert.prepareError("Terms and conditions title length is not in range").log();
				result.addParam(new Param("message",
						"Terms and conditions title should have a minimum of " + TERM_AND_CONDITION_TITLE_MIN_CHARACTERS
						+ " and a maximum of " + TERM_AND_CONDITION_TITLE_MAX_CHARACTERS + " characters",
						FabricConstants.STRING));
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Terms and Conditions update failed. Code: " + termAndConditionCode + "/" + "Language Code: "
								+ languageCode + "/" + "Title: " + termAndConditionTitle + "/" + "Description: "
								+ termAndConditionDescription);
				return result;
			}

			if (StringUtils.length(termAndConditionDescription) > TERM_AND_CONDITION_DESCRIPTION_MAX_CHARACTERS) {
				ErrorCodeEnum.ERR_20269.setErrorCode(result);
				alert.prepareError("Terms and conditions description length is not in range").log();
				result.addParam(new Param("message",
						"Terms and conditions description should have a maximum of "
								+ TERM_AND_CONDITION_DESCRIPTION_MAX_CHARACTERS + " characters",
								FabricConstants.STRING));
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Terms and Conditions update failed. Code: " + termAndConditionCode + "/" + "Language Code: "
								+ languageCode + "/" + "Title: " + termAndConditionTitle + "/" + "Description: "
								+ termAndConditionDescription);
				return result;
			}

			// Making termAndConditionContent as non mandatory for backward compatibility
			/*
			 * if (StringUtils.isBlank(termAndConditionContent)) { ErrorCodeEnum.ERR_20270.setErrorCode(result);
			 * alert.prepareError("Terms and conditions content is a mandatory input").log();
			 * AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.UPDATE,
			 * ActivityStatusEnum.FAILED, "Terms and Conditions update failed."); return result; }
			 */

			Map<String, Object> postParametersMap = new HashMap<>();
			if (loggedInUserDetails != null) {
				loggedInUserId = loggedInUserDetails.getId();
			}

			postParametersMap.put("loggedInUserId", loggedInUserId);	
			postParametersMap.put("termAndConditionCode", termAndConditionCode);	        
			postParametersMap.put("languageCode", languageCode);
			postParametersMap.put("contentType", contentType);	        
			postParametersMap.put("termAndConditionTitle", termAndConditionTitle);
			postParametersMap.put("termAndConditionDescription", termAndConditionDescription);	        
			postParametersMap.put("termAndConditionContent", termAndConditionContent);
			postParametersMap.put("legalEntityId", leId);
			
			boolean isAuthSuccess = authObj.execute(null, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22151.setErrorCode(result);
				return result;
			}
			
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			
			String consentBackend = EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND");
			
			if (ACConstants.DBXDB_BACKEND.equalsIgnoreCase(consentBackend)) {
				result = tncBusinessDelegate.editTermsAndConditionsDBXDB(postParametersMap, backendToken, loggedInUserDetails);
			} else {
				result = tncBusinessDelegate.editTermsAndConditions(postParametersMap, backendToken);
			}
			 
		}catch (Exception e) {
			alert.prepareError("Unexpected Error in editTermsAndConditions", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22089.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Term And Condition Update failed: termAndConditionCode:"+termAndConditionCode);
		}

		return result;
	}
	
	@Override
	public Result getRequiredTermsAndConditions(String methodID, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) {
		Result result = new Result();
		String version = StringUtils.EMPTY, languageCode = StringUtils.EMPTY, termAndCondContentId = StringUtils.EMPTY;
		String leId = StringUtils.EMPTY;
		try {

			version = requestInstance.getParameter("version");
			languageCode = requestInstance.getParameter("languageCode");
			termAndCondContentId = requestInstance.getParameter("termAndCondContentId");
			leId = requestInstance.getParameter("legalEntityId");
			Map<String, Object> postParametersMap = new HashMap<>();

			// If languageCode is not passed we'll set default languageCode
			if (StringUtils.isBlank(languageCode)) {
				languageCode = DEFAULT_LANGUAGE_CODE;
			}

			if (StringUtils.isBlank(termAndCondContentId)) {
				ErrorCodeEnum.ERR_20270.setErrorCode(result);
				alert.prepareError("Term and condition content is a mandatory input").log();
				alert.prepareError("Failed to get Terms and Conditions").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED,
						"Failed fetch getTermsAndConditions: termAndCondContentId:" + termAndCondContentId);
				return result;
			}

			postParametersMap.put("termAndCondContentId", termAndCondContentId);
			postParametersMap.put("languageCode", languageCode);
			postParametersMap.put("version", version);
			postParametersMap.put("legalEntityId", leId);

			boolean isAuthSuccess = authObj.execute(null, requestInstance, response, result);

			if (!isAuthSuccess) {
				ErrorCodeEnum.ERR_22151.setErrorCode(result);
				return result;
			}

			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);

			result = tncBusinessDelegate.getRequiredTermsAndConditions(postParametersMap, backendToken);

		} catch (Exception e) {
			alert.prepareError("Unexpected Error in getRequiredTermsAndConditions", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_20264.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.TANDC, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED,
					"Failed fetch getRequiredTermsAndConditions: termAndCondContentId:" + termAndCondContentId);
		}
		return result;
	}

}
