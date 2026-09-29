package com.hbl.resource.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.SMSInvokeHelper;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.UserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.InfinityUserManagementBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.api.KMSBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.InfinityConstants;

public class InfinityUserManagementBusinessDelegateImplExtn extends InfinityUserManagementBusinessDelegateImpl{
	LoggerUtil logger = new LoggerUtil(InfinityUserManagementBusinessDelegateImplExtn.class);
	@Override
	public DBXResult validateCustomerEnrollmentDetails(String lastName, String taxId, String dateOfBirth,
			Map<String, Object> headersMap,String companyLegalUnit) throws ApplicationException {
		logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn");
		DBXResult response = new DBXResult();
		ProfileManagementBackendDelegate profileManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ProfileManagementBackendDelegate.class);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setLastName(lastName);
		customerDTO.setDateOfBirth(dateOfBirth);
		customerDTO.setTaxID(taxId);
		customerDTO.setCompanyLegalUnit(companyLegalUnit);
		DBXResult coreCustomer = new DBXResult();

		try {
			coreCustomer = profileManagementBackendDelegate.fetchRetailCustomerDetails(customerDTO, headersMap);

			if (coreCustomer == null)
				return response;

			JsonObject coreCustomerJson = (JsonObject) coreCustomer.getResponse();
			BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setBackendId(JSONUtil.getString(coreCustomerJson, "id"));
			BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
			DBXResult backendIdentifiers = backendIdentifierBusinessDelegate.get(backendIdentifierDTO, headersMap);

			if (backendIdentifiers != null && backendIdentifiers.getResponse() != null) {
				backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifiers.getResponse();
				coreCustomerJson.addProperty("infinityUserId", backendIdentifierDTO.getCustomer_id());
				coreCustomerJson.addProperty("contractId", backendIdentifierDTO.getContractId());
			}

			if (StringUtils.isNotBlank(JSONUtil.getString(coreCustomerJson, "infinityUserId"))) {
				UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(UserManagementBackendDelegate.class);
				CustomerDTO inputDTO = new CustomerDTO();
				inputDTO.setId(JSONUtil.getString(coreCustomerJson, "infinityUserId"));
				inputDTO = userManagementBackendDelegate.getCustomerDetails(inputDTO, headersMap).get(0);
				logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:isUserEnrolled:Customer:"+inputDTO.getIsEnrolled());
				logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:getStatus_id:"+inputDTO.getStatus_id());
				logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:inputDTO:"+inputDTO.toString());
				if (inputDTO.getIsEnrolled()
						&& DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE.equalsIgnoreCase(inputDTO.getStatus_id())) {
					coreCustomerJson.addProperty("isUserEnrolled", "true");
				}
				else {
					coreCustomerJson.addProperty("isUserEnrolled", "false");
				}
			}
			response.setResponse(coreCustomerJson);
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10806);
		}
		return response;
	}
	public DBXResult validateHBLCustomerEnrollmentDetails(Map<String, Object> payload, Map<String, Object> headersMap) throws ApplicationException {
		logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn");
		DBXResult response = new DBXResult();
		DBXResult coreCustomer = new DBXResult();
		ProfileManagementBackendDelegateImplExtn profileManagementBackendDelegateExtn = new ProfileManagementBackendDelegateImplExtn();
		try {
			coreCustomer = profileManagementBackendDelegateExtn.fetchHBLRetailCustomerDetails(payload, headersMap);

			if (coreCustomer == null)
				return response;

			JsonObject coreCustomerJson = (JsonObject) coreCustomer.getResponse();
			BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setBackendId(JSONUtil.getString(coreCustomerJson, "id"));
			BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
			DBXResult backendIdentifiers = backendIdentifierBusinessDelegate.get(backendIdentifierDTO, headersMap);

			if (backendIdentifiers != null && backendIdentifiers.getResponse() != null) {
				backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifiers.getResponse();
				coreCustomerJson.addProperty("infinityUserId", backendIdentifierDTO.getCustomer_id());
				coreCustomerJson.addProperty("contractId", backendIdentifierDTO.getContractId());
			}

			if (StringUtils.isNotBlank(JSONUtil.getString(coreCustomerJson, "infinityUserId"))) {
				UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(UserManagementBackendDelegate.class);
				CustomerDTO inputDTO = new CustomerDTO();
				inputDTO.setId(JSONUtil.getString(coreCustomerJson, "infinityUserId"));
				inputDTO = userManagementBackendDelegate.getCustomerDetails(inputDTO, headersMap).get(0);
				logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:isUserEnrolled:Customer:"+inputDTO.getIsEnrolled());
				logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:getStatus_id:"+inputDTO.getStatus_id());
				logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:inputDTO:"+inputDTO.toString());
				if (inputDTO.getIsEnrolled()
						&& DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE.equalsIgnoreCase(inputDTO.getStatus_id())) {
					coreCustomerJson.addProperty("isUserEnrolled", "true");
				}
				else {
					coreCustomerJson.addProperty("isUserEnrolled", "false");
				}
			}
			response.setResponse(coreCustomerJson);
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10806);
		}
		return response;
	}
	@Override
	public DBXResult generateInfinityUserActivationCodeAndUsername(Map<String, String> bundleConfigurations,
			Map<String, String> inputParams, Map<String, Object> headersMap) throws ApplicationException {
		boolean status = false;
		DBXResult result = new DBXResult();
		JsonObject resultObject = new JsonObject();
		try {

			InfinityUserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			UserManagementBackendDelegate usermanagementBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(UserManagementBackendDelegate.class);


			String userId = inputParams.get(InfinityConstants.userId);
			String userName = inputParams.get(InfinityConstants.userName);
			boolean isOnBoradingFlow = Boolean.parseBoolean(inputParams.get(InfinityConstants.isOnBoradingFlow));
			boolean isProspectFlow = Boolean.parseBoolean(inputParams.get(InfinityConstants.isProspectFlow));

			DBXResult usernameresponse = backendDelegate
					.generateInfinityUserName(bundleConfigurations.get(BundleConfigurationHandler.USERNAME_LENGTH));
			DBXResult activatiocoderesponse = backendDelegate
					.generateActivationCode(bundleConfigurations.get(BundleConfigurationHandler.ACTIVATIONCODE_LENGTH));
			String activationCode = inputParams.get(InfinityConstants.password);
			String applicationid = inputParams.get(InfinityConstants.applicationid);
			if (StringUtils.isBlank(userName) && usernameresponse != null && usernameresponse.getResponse() != null) {
				userName = (String) usernameresponse.getResponse();
			}
			if (StringUtils.isBlank(activationCode) && activatiocoderesponse != null
					&& activatiocoderesponse.getResponse() != null) {
				activationCode = (String) activatiocoderesponse.getResponse();
			}
			if (StringUtils.isBlank(userName) || StringUtils.isBlank(activationCode)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10795);
			}

			inputParams.put(InfinityConstants.userName, userName);

			String phone = inputParams.get(DTOConstants.PHONE);
			String email = inputParams.get(DTOConstants.EMAIL);
			JsonObject customerCommunication = new JsonObject();
			JsonArray communicationArray = new JsonArray();
			logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:generateInfinityUserActivationCodeAndUsername:phone,email:"+phone+","+email);
			if (StringUtils.isBlank(phone) || StringUtils.isBlank(email)) {
				CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(CommunicationBackendDelegate.class);
				CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
				customerCommunicationDTO.setCustomer_id(userId);
				DBXResult communicationResponse = communicationBackendDelegate
						.getPrimaryMFACommunicationDetails(customerCommunicationDTO, headersMap);
				customerCommunication = ((JsonObject) communicationResponse.getResponse());
				logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:generateInfinityUserActivationCodeAndUsername:customerCommunication:"+customerCommunication);
				if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
						&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
					communicationArray = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
							.getAsJsonArray();
					for (JsonElement jsonelement : communicationArray) {
						JsonObject object = jsonelement.getAsJsonObject();
						if (StringUtils.isBlank(email)) {
							if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
								email = JSONUtil.getString(object, "Value");
						}
						if (StringUtils.isBlank(phone)) {
							if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
								phone = JSONUtil.getString(object, "Value");
						}
					}
				}
				else if(customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)  && customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonObject()) {
					JsonObject commobject = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonObject();
					if (StringUtils.isBlank(email)) {
						email = JSONUtil.getString(commobject, "Email");
					}
					if (StringUtils.isBlank(phone)) {
						phone = JSONUtil.getString(commobject, "Phone");
					}
				}
			}
			CustomerDTO customerInfo = new CustomerDTO();
			customerInfo.setId(userId);
			DBXResult customerResponse = usermanagementBackendDelegate.get(customerInfo, headersMap);
			if (customerResponse != null && customerResponse.getResponse() != null)
				customerInfo = (CustomerDTO) customerResponse.getResponse();

			if (!isOnBoradingFlow && !isProspectFlow && customerInfo.getUserName().equalsIgnoreCase(userId))
				customerInfo.setUserName(userName);

			String emailTemplate = inputParams.get(InfinityConstants.EMAIL_TEMPLATE);
			String smsTemplate = inputParams.get(InfinityConstants.SMS_TEMPLATE);

			if (!isProspectFlow) {
				UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(UserManagementBackendDelegate.class);
				userManagementBackendDelegate.createEntryForCredentailCheckerTable(bundleConfigurations, customerInfo,
						activationCode, headersMap);
			}

			if (!isOnBoradingFlow) {
				usermanagementBackendDelegate.updateCustomerDetails(customerInfo, headersMap);
				emailTemplate = "BCT_ENROLLMENT_USERNAME_TEMPLATE";//DBPUtilitiesConstants.ENROLLMENT_USERNAME_TEMPLATE;
				smsTemplate = "BCT_ENROLLMENT_ACTIVATIONCODE_TEMPLATE";
			}
			customerInfo = (CustomerDTO) usermanagementBackendDelegate.get(customerInfo, headersMap).getResponse();
			customerInfo.setApplicationID(applicationid);
			if(StringUtils.isBlank(customerInfo.getFullName()))
			customerInfo.setFullName(inputParams.get("customerName"));
			boolean isSCAEnabled = false;
			boolean isSCACommunicationEnabled = false;
			isSCAEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("IS_SCA_ENABLED"));
			isSCACommunicationEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("SCA_COMMUNICATION"));
			if(!isSCAEnabled || isSCACommunicationEnabled) {
				if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(emailTemplate)) {
					sendGeneratedUsernameToEmail(bundleConfigurations, customerInfo, email, headersMap, emailTemplate, activationCode);
				}
				if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(smsTemplate)) {
					//sendGeneratedActivationcodeToMobile(phone, activationCode, headersMap, smsTemplate);
					sendGeneratedActivationcodeToMobileNew(phone,customerInfo, activationCode, headersMap, smsTemplate);
				}
			}
			status = true;

			resultObject.addProperty(InfinityConstants.activationCode, activationCode);
			resultObject.addProperty(InfinityConstants.userId, customerInfo.getUserName());

		} catch (ApplicationException e) {
			logger.error(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while generating username and activation code "
							,e);
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			logger.error(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while generating username and activation code "
							,e);
			throw new ApplicationException(ErrorCodeEnum.ERR_10795);
		}
		resultObject.addProperty(InfinityConstants.status, status);
		result.setResponse(resultObject);
		return result;
	}
	public DBXResult SendEnrollmentRejectedEmail(Map<String, String> inputParams, Map<String, Object> headersMap) throws ApplicationException {
		boolean status = false;
		DBXResult result = new DBXResult();
		JsonObject resultObject = new JsonObject();
		try {
			UserManagementBackendDelegate usermanagementBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(UserManagementBackendDelegate.class);


			String userId = inputParams.get(InfinityConstants.userId);
			//String userName = inputParams.get(InfinityConstants.userName);
			boolean isOnBoradingFlow = Boolean.parseBoolean(inputParams.get(InfinityConstants.isOnBoradingFlow));
			//boolean isProspectFlow = Boolean.parseBoolean(inputParams.get(InfinityConstants.isProspectFlow));

			String applicationid = inputParams.get(InfinityConstants.applicationid);

			//inputParams.put(InfinityConstants.userName, userName);

			String phone = inputParams.get(DTOConstants.PHONE);
			String email = inputParams.get(DTOConstants.EMAIL);
			JsonObject customerCommunication = new JsonObject();
			JsonArray communicationArray = new JsonArray();
			if (StringUtils.isBlank(phone) || StringUtils.isBlank(email)) {
				CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(CommunicationBackendDelegate.class);
				CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
				customerCommunicationDTO.setCustomer_id(userId);
				DBXResult communicationResponse = communicationBackendDelegate
						.getPrimaryMFACommunicationDetails(customerCommunicationDTO, headersMap);
				customerCommunication = ((JsonObject) communicationResponse.getResponse());

				if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
						&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
					communicationArray = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
							.getAsJsonArray();
					for (JsonElement jsonelement : communicationArray) {
						JsonObject object = jsonelement.getAsJsonObject();
						if (StringUtils.isBlank(email)) {
							if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
								email = JSONUtil.getString(object, "Value");
						}
						if (StringUtils.isBlank(phone)) {
							if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
								phone = JSONUtil.getString(object, "Value");
						}
					}
				}
				else if(customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)  && customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonObject()) {
					JsonObject commobject = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonObject();
					if (StringUtils.isBlank(email)) {
						email = JSONUtil.getString(commobject, "Email");
					}
					if (StringUtils.isBlank(phone)) {
						phone = JSONUtil.getString(commobject, "Phone");
					}
				}
			}
			CustomerDTO customerInfo = new CustomerDTO();
			customerInfo.setId(userId);
			customerInfo.setFullName(inputParams.get("customerName"));
			DBXResult customerResponse = usermanagementBackendDelegate.get(customerInfo, headersMap);
			if (customerResponse != null && customerResponse.getResponse() != null)
				customerInfo = (CustomerDTO) customerResponse.getResponse();

			String emailTemplate = inputParams.get(InfinityConstants.EMAIL_TEMPLATE);
			String smsTemplate = inputParams.get(InfinityConstants.SMS_TEMPLATE);

			if (!isOnBoradingFlow) {
				usermanagementBackendDelegate.updateCustomerDetails(customerInfo, headersMap);
				emailTemplate = "HBL_ENROLLMENT_REJECTED_TEMPLATE";
				smsTemplate = "HBL_ENROLLMENT_REJECTED_TEMPLATE";//DBPUtilitiesConstants.ENROLLMENT_ACTIVATIONCODE_TEMPLATE;
			}
			customerInfo = (CustomerDTO) usermanagementBackendDelegate.get(customerInfo, headersMap).getResponse();
			customerInfo.setApplicationID(applicationid);
			boolean isSCAEnabled = false;
			boolean isSCACommunicationEnabled = false;
			isSCAEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("IS_SCA_ENABLED"));
			isSCACommunicationEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("SCA_COMMUNICATION"));
			logger.debug("HBL::InfinityUserManagementBusinessDelegateImplExtn:SendEnrollmentRejectedEmail:email:"+email+",emailTemplate:"+emailTemplate);
			if(!isSCAEnabled || isSCACommunicationEnabled) {
				if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(emailTemplate)) {
					sendRejectedEmail(customerInfo, email, headersMap, emailTemplate);
				}
				if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(smsTemplate)) {
					sendEnrollmentRejectedSMS(phone, headersMap, smsTemplate);
				}
			}
			status = true;

			resultObject.addProperty("emailDelivered", status);
			resultObject.addProperty("smsDelivered", status);

		} catch (ApplicationException e) {
			logger.error(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while generating username and activation code "
							,e);
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			logger.error(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while generating username and activation code "
							,e);
			throw new ApplicationException(ErrorCodeEnum.ERR_10795);
		}
		resultObject.addProperty(InfinityConstants.status, status);
		result.setResponse(resultObject);
		return result;
	}

	
	private void sendRejectedEmail(CustomerDTO customerInfo,
			String email, Map<String, Object> headersMap, String templateName) throws ApplicationException {
		try {
			KMSBusinessDelegate kmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(KMSBusinessDelegate.class);
			Map<String, Object> input = new HashMap<>();
			if(StringUtils.isBlank(customerInfo.getFullName())){
			input.put("FirstName", customerInfo.getFirstName());
			input.put("LastName", customerInfo.getLastName());
			}else
			input.put("FirstName", customerInfo.getFullName());
			input.put("EmailType", templateName);
			JSONObject addContext = new JSONObject();
			logger.debug("Context" + addContext);
			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			kmsBusinessDelegate.sendKMSEmail(input, headersMap);
		} catch (Exception e) {
			logger.error("Exception occured while sending Enrollment Rejected email " + e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_10796);
		}
	}
	private void sendEnrollmentRejectedSMS(String phone,
			Map<String, Object> headersMap, String templateName) throws ApplicationException {
		try {
			KMSBusinessDelegate kmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(KMSBusinessDelegate.class);
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("Phone", phone);
			inputParams.put("MessageType", templateName);
			kmsBusinessDelegate.sendKMSSMS(inputParams, headersMap);
		} catch (Exception e) {
			logger.error("Exception occured while sending Enrollment Rejected SMS" + e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_10797);
		}

	}

	private void sendGeneratedUsernameToEmail(Map<String, String> configurations, CustomerDTO customerInfo,
			String email, Map<String, Object> headersMap, String templateName, String activationCode) throws ApplicationException {
		try {
			KMSBusinessDelegate kmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(KMSBusinessDelegate.class);
			Map<String, Object> input = new HashMap<>();
			if(StringUtils.isBlank(customerInfo.getFullName())){
			input.put("FirstName", customerInfo.getFirstName());
			input.put("LastName", customerInfo.getLastName());
			}else
			input.put("FirstName", customerInfo.getFullName());
			input.put("EmailType", templateName);
			input.put("otp", activationCode);
			JSONObject addContext = new JSONObject();
			String activationLink=EnvironmentConfigurationsHandler.getValue("DBP_OLB_BASE_URL"); // http://20.197.50.171:8080/apps/OnlineBanking
			activationLink=activationLink+"/#/AuthenticationMA/frmAccountActivation";//?data="+customerInfo.getUserName();
			//addContext.put("resetPasswordLink", EnvironmentConfigurationsHandler.getValue("DBP_OLB_BASE_URL"));
			addContext.put("resetPasswordLink", activationLink);
			addContext.put("userName", customerInfo.getUserName());
			if (customerInfo.getApplicationID() != null || StringUtils.isNotBlank(customerInfo.getApplicationID())) {
				addContext.put("applicationID", customerInfo.getApplicationID());
			}
			addContext.put("activationCodeExpiry",
					String.valueOf(
							(Integer.parseInt(configurations.get(BundleConfigurationHandler.ACTIVATIONCODE_EXPIRYTIME))
									/ 1440)));
			logger.debug("Context" + addContext);
			input.put("AdditionalContext", KMSUtil.getOTPContent(activationCode, null, addContext));
			input.put("Email", email);
			kmsBusinessDelegate.sendKMSEmail(input, headersMap);
		} catch (Exception e) {
			logger.error("Exception occured while sending email " + e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_10796);
		}
	}

	private void sendGeneratedActivationcodeToMobile(String phone, String activationCode,
			Map<String, Object> headersMap, String templateName) throws ApplicationException {
		try {
			KMSBusinessDelegate kmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(KMSBusinessDelegate.class);
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("Phone", phone);
			inputParams.put("otp", activationCode);
			inputParams.put("MessageType", templateName);
			kmsBusinessDelegate.sendKMSSMS(inputParams, headersMap);
		} catch (Exception e) {
			logger.error("Exception occured while sending SMS" + e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_10797);
		}

	}
	private void sendGeneratedActivationcodeToMobileNew(String phone, CustomerDTO customerInfo, String activationCode,
			Map<String, Object> headersMap, String templateName) throws ApplicationException {
		try {
			KMSBusinessDelegate kmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(KMSBusinessDelegate.class);
			HashMap<String, Object> inputParams = new HashMap<>();
			inputParams.put("phone", phone);
			inputParams.put("messageType", templateName);
			
			JSONObject addContext = new JSONObject();
			addContext.put("otp", activationCode);
			if(StringUtils.isBlank(customerInfo.getFullName())){
			addContext.put("firstName", customerInfo.getFirstName());
			addContext.put("lastName", customerInfo.getLastName());
			}else
			addContext.put("firstName", customerInfo.getFullName());
			
			inputParams.put("content",  KMSUtil.getOTPContent(activationCode, null, addContext));
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
			Result smsResult = SMSInvokeHelper.invokeSMSService(inputParams, headerParams);
			JsonObject smsResponse = new JsonParser().parse(smsResult.toString()).getAsJsonObject();
			logger.debug("sending SMS result in sendGeneratedActivationcodeToMobileNew:"+smsResponse);
			/*JsonObject logparamsObject = new JsonObject();
			logparamsObject.addProperty("eventType", "OTP");
			logparamsObject.addProperty("eventSubtype", "OTP");
			logparamsObject.addProperty("isAlertsEngine", "OTP");
			if (logparamsObject.get("isAlertsEngine") != null && logparamsObject.get("isAlertsEngine").getAsBoolean()) {
				JSONToResult.convert(smsResult.toString());
			}
			HelperMethods.insertToAlertHistory(inputParams, logparamsObject, smsResponse, "CH_SMS");
			*/
			JSONToResult.convert(smsResponse.toString());
		} catch (Exception e) {
			logger.error("Exception occured while sending SMS in sendGeneratedActivationcodeToMobileNew" + e.getMessage());
			//throw new ApplicationException(ErrorCodeEnum.ERR_10745);
		}

	}
}
