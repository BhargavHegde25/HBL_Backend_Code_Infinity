package com.hbl.resource.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hbl.utility.DeviceInfo;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.resource.api.CoreCustomerResource;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.UserManagementResourceImpl;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class UserManagementResourceImplExtn extends UserManagementResourceImpl{
	 private static LoggerUtil logger = new LoggerUtil(UserManagementResourceImplExtn.class);
	 private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	 private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	 @Override
	    public Result verifyCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws JSONException, UnsupportedEncodingException, DBPApplicationException, MiddlewareException {

	        Result result = new Result();
	        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
	        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
	        CustomerDTO customerDTO = new CustomerDTO();

	        
	        String ssn = StringUtils.isNotBlank(map.get("Ssn")) ? map.get("Ssn") : dcRequest.getParameter("Ssn");
	        String lastName = StringUtils.isNotBlank(map.get("LastName")) ? map.get("LastName")
	                : dcRequest.getParameter("LastName");
	        String dateOfBirth = StringUtils.isNotBlank(map.get("DateOfBirth")) ? map.get("DateOfBirth")
	                : dcRequest.getParameter("DateOfBirth");
	                
	        
	        String serviceKey = StringUtils.isNotBlank(map.get("serviceKey")) ? map.get("serviceKey")
	                : dcRequest.getParameter("serviceKey");
	        String name = StringUtils.isNotBlank(map.get("Name")) ? map.get("Name")
	                : dcRequest.getParameter("Name");
	        String accountNumber = StringUtils.isNotBlank(map.get("AccountNumber")) ? map.get("AccountNumber")
	                : dcRequest.getParameter("AccountNumber");
	        String email = StringUtils.isNotBlank(map.get("Email")) ? map.get("Email") : dcRequest.getParameter("Email");
	        String phone = StringUtils.isNotBlank(map.get("Phone")) ? map.get("Phone") : dcRequest.getParameter("Phone");
	        String mobileNumber = StringUtils.isNotBlank(map.get("Phone")) ? map.get("Phone") : dcRequest.getParameter("Phone");
	        String legalEntityId = EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
	        if (StringUtils.isBlank(serviceKey)) {
	            return result;
	        }

	        try {
	            MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(BusinessDelegateFactory.class)
	                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);
	            MFAServiceDTO mfaServiceDTO = new MFAServiceDTO();
	            mfaServiceDTO.setServiceKey(serviceKey);
	            List<MFAServiceDTO> dtoList = null;
	            dtoList = mfaserviceBD.getMfaService(mfaServiceDTO, null, dcRequest.getHeaderMap());
	            if (null == dtoList || dtoList.isEmpty() || StringUtils.isBlank(dtoList.get(0).getServiceKey())) {
	                return result;
	            }

	            if (!"true".equalsIgnoreCase(dtoList.get(0).getIsVerified())) {
	                return result;
	            }
	            mfaserviceBD.deleteMfaService(serviceKey, dcRequest.getHeaderMap());

	            customerDTO.setLastName(lastName);
	            customerDTO.setDateOfBirth(dateOfBirth);
	            customerDTO.setSsn(ssn);
	            
	            customerDTO.setFullName(name);
	            customerDTO.setTaxID(accountNumber); //mapping accountNumber to TAXID as we don't have account number field in customerDTO 
	            CustomerCommunicationDTO commDTO = new CustomerCommunicationDTO();
	            commDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_EMAIL);
	            commDTO.setValue(email);
	            customerDTO.setCustomerCommuncation(commDTO);
	            commDTO = new CustomerCommunicationDTO();
	            commDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_PHONE);
	            commDTO.setValue(mobileNumber);
	            customerDTO.setLegalEntityId(legalEntityId);
	            customerDTO.setCustomerCommuncation(commDTO);

	            
	            UserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
	                    .getBusinessDelegate(UserManagementBusinessDelegate.class);
	            String deploymentPlatform = EnvironmentConfigurationsHandler.getValue("ODMS_DEPLOYMENT_PLATFORM",dcRequest);
	            DBXResult dbxResult = businessDelegate.verifyCustomer(customerDTO, dcRequest.getHeaderMap(), deploymentPlatform);
	            if (dbxResult.getResponse() != null) {
	                JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
	                result = JSONToResult.convert(jsonObject.toString());
	            } else {
	                HelperMethods.addError(result, dbxResult);
	            }

	        } catch (ApplicationException e) {
	            e.getErrorCodeEnum().setErrorCode(result);
	            logger.error(e.getMessage());
	        }
	        return result;
	    }
	 @Override
	 public Result validateEnrollmentActivation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws ApplicationException {
	        Result result = new Result();
	        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
	        String userName = StringUtils.isNotBlank(map.get("UserName")) ? map.get("UserName")
	                : dcRequest.getParameter("UserName");
	        String activationCode = StringUtils.isNotBlank(map.get("activationCode")) ? map.get("activationCode")
	                : dcRequest.getParameter("activationCode");
	        if (StringUtils.isBlank(activationCode) || StringUtils.isBlank(userName)) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10742);
	        }

	        Map<String, String> configurations = BundleConfigurationHandler
	                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
	        CoreCustomerResource coreCustomerResource = DBPAPIAbstractFactoryImpl
                    .getResource(CoreCustomerResource.class);
	        String legalEntityId=EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
	        try {
	            UserManagementBusinessDelegate userManagementbusinessDelegate = DBPAPIAbstractFactoryImpl
	                    .getBusinessDelegate(UserManagementBusinessDelegate.class);
	            CredentialCheckerDTO credentialcheckerDTO = new CredentialCheckerDTO();
	            credentialcheckerDTO.setId(activationCode);
	            credentialcheckerDTO.setUserName(userName);
	            boolean status = userManagementbusinessDelegate.activationCodeValidationForEnrollment(configurations,
	                    credentialcheckerDTO, dcRequest.getHeaderMap());
	            if (!status) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10741);
	            }
	            result.addStringParam("isActivationCodeValid", String.valueOf(status));
	            if (status) {
	            	String backendId=getBackendIdFromUsername(userName, dcRequest);
	            	logger.debug("hbl::UserManagementResourceImplExtn:validateEnrollmentActivation backendId:"+backendId);
	            	if(StringUtils.isNotBlank(backendId)) {
	            	MembershipDTO membershipDTO = new MembershipDTO();
	            	membershipDTO.setId(backendId);
	            	membershipDTO.setCompanyLegalUnit(legalEntityId);
	            	JsonObject coreCustomerJson = searchCoreCustomers(membershipDTO, dcRequest.getHeaderMap());
	            	JSONObject elgibilityObj = verifyChannelAccess(coreCustomerJson, dcRequest);
	     			String isUserEligibleForActivation=elgibilityObj.has("isUserEligibleForActivation")?elgibilityObj.getString("isUserEligibleForActivation"):"";
	     			if(isUserEligibleForActivation.equalsIgnoreCase("No") || StringUtils.isEmpty(isUserEligibleForActivation)) {
	     				result.addStringParam("dbpErrMsg", elgibilityObj.getString("dbpErrMsg"));
	     				result.addStringParam("dbpErrCode", elgibilityObj.getString("dbpErrCode"));
	     				return result;	
	     			}
	                String serviceKey = userManagementbusinessDelegate.generateServiceKeyForEnrollment(credentialcheckerDTO,
	                        dcRequest.getHeaderMap());
	                result.addStringParam("serviceKey", serviceKey);
	            	}else {
	            		alert.prepareError("Exception occured while validating the customer in activation flow").log();
	    	            throw new ApplicationException(ErrorCodeEnum.ERR_12427);
	            	}
	            }
	           
	        } catch (ApplicationException e) {
	            throw new ApplicationException(e.getErrorCodeEnum());
	        } catch (Exception e) {
	            alert.prepareError("Exception occured while validating the activation code" + e.getMessage()).log();
	            throw new ApplicationException(ErrorCodeEnum.ERR_10741);
	        }
	        return result;
	    }
	 public JsonObject searchCoreCustomers(MembershipDTO membershipDTO,
	            Map<String, Object> headersMap)
	            throws ApplicationException {
	        DBXResult response = new DBXResult();
	        JsonObject coreCustomerJson = new JsonObject();
	        try {
	            CoreCustomerBackendDelegate backendDelegate =
	                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
	            DBXResult coreResult = backendDelegate.searchCoreCustomers(membershipDTO, headersMap);
	            JsonArray coreCustomerRecords = new JsonArray();
                if (coreResult != null && coreResult.getResponse() != null) {
                    coreCustomerRecords = (JsonArray) coreResult.getResponse();
                }
                logger.debug("hbl::UserManagementResourceImplExtn:searchCoreCustomers coreCustomerRecords:"+coreCustomerRecords);
                if (coreCustomerRecords.size() > 1) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10804);
                }
                if (coreCustomerRecords.size() == 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10757);
                }
                coreCustomerJson = coreCustomerRecords.get(0).getAsJsonObject();
	        } catch (ApplicationException e) {
	            alert.prepareError("UserManagementResourceImplExtn : Exception occured while searching core customers"
	                    + e.getMessage()).log();
	            throw new ApplicationException(e.getErrorCodeEnum());
	        } catch (Exception e) {
	            alert.prepareError("UserManagementResourceImplExtn : Exception occured while searching core customers "
	                    + e.getMessage()).log();
	            throw new ApplicationException(ErrorCodeEnum.ERR_10757);
	        }
	        return coreCustomerJson;
	    }
	 public JSONObject verifyChannelAccess(JsonObject coreCustomerJson, DataControllerRequest dcRequest) {
			String channelAccess = JSONUtil.getString(coreCustomerJson, "channelAccess");
			String channel=DeviceInfo.GetUserDeviceInfo(dcRequest, null).get("channel_id").toString();
			logger.debug("hbl::UserManagementResourceImplExtn:checkEligibilityForActivate channelAccess:"+channelAccess);
			logger.debug("hbl::UserManagementResourceImplExtn:checkEligibilityForActivate channel name:"+channel);
			String errorMsg="";
			JSONObject enorllObject = new JSONObject();
			if(channelAccess.equalsIgnoreCase("ONLINE_BANKING_ONLY")) {
				if(channel.equalsIgnoreCase("desktop")) {
					enorllObject.put("isUserEligibleForActivation", "Yes");
					enorllObject.put("channelAccess", channelAccess);
				}else {
					errorMsg="Sorry! Can’t Activate your profile. Your Mobile banking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
					enorllObject.put("isUserEligibleForActivation", "No");
					enorllObject.put("dbpErrMsg", errorMsg);
					enorllObject.put("dbpErrCode", "30001");
					
				}
			}
			else if(channelAccess.equalsIgnoreCase("MOBILE_BANKING_ONLY")) {
				if(channel.equalsIgnoreCase("mobile")) {
					enorllObject.put("isUserEligibleForActivation", "Yes");
					enorllObject.put("channelAccess", channelAccess);
				}else {
					errorMsg="Sorry! Can’t Activate your profile. Your Online banking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
					enorllObject.put("isUserEligibleForActivation", "No");
					enorllObject.put("dbpErrMsg", errorMsg);
					enorllObject.put("dbpErrCode", "30002");
				}
			}
			else if(channelAccess.equalsIgnoreCase("BOTH")) {
				enorllObject.put("isUserEligibleForActivation", "Yes");
				enorllObject.put("channelAccess", channelAccess);
			}else {
				errorMsg="Sorry! Can’t Activate your profile. Your ebanking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
				enorllObject.put("isUserEligibleForActivation", "No");
				enorllObject.put("dbpErrMsg", errorMsg);
				enorllObject.put("dbpErrCode", "30003");
			}
			return enorllObject;
		}
	 private String getBackendIdFromUsername(String userName, DataControllerRequest request ) throws ApplicationException {
			String backendId="";
			BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
			String legalEntityId=EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		    String customerId=getCustomerIDFromUsername(request, userName);
		    backendIdentifierDTO.setCustomer_id(customerId);
		    backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
		    backendIdentifierDTO.setBackendType("T24");
			if(StringUtils.isNotBlank(backendIdentifierDTO.getCustomer_id())) {
			BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
			DBXResult backendIdentifiers = backendIdentifierBusinessDelegate.get(backendIdentifierDTO, request.getHeaderMap());
			if (backendIdentifiers != null && backendIdentifiers.getResponse() != null) {
				backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifiers.getResponse();
				backendId= backendIdentifierDTO.getBackendId();
			}
			}
			return backendId;
	 }
	 private String getCustomerIDFromUsername(DataControllerRequest request, String UserName) {

			String customerid = "";
			try {
				String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
				HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
				HashMap<String, Object> svcParams = new HashMap<String, Object>();

				svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
				Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
						Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
				Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
				if (null != customerDataset) {
					customerid = customerDataset.getRecord(0).getParamValueByName("id");
				} else {
					logger.debug("Else getCustomerIDFromUsername:");
				}
				logger.debug("getCustomerIDFromUsername id:" + customerid);
			} catch (Exception e) {
				logger.error("Error while retrieving CustomerType_id for Customer " + UserName);
			}
			return customerid;
		}
	 @Override
	 public Result sendActivationCodeAndUsername(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws ApplicationException {
	        Result result = new Result();
	        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
	        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
	        String id = StringUtils.isNotBlank(map.get("id")) ? map.get("id") : dcRequest.getParameter("id");
	        String sendEmail = StringUtils.isNotBlank(map.get("sendEmail")) ? map.get("sendEmail") : "true"; 

	        if (StringUtils.isBlank(id)) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10739);
	        }
	        Boolean isAdmin = true;
	        /*Map<String, String> info = HelperMethods.getCustomerFromIdentityService(dcRequest);
	        if (HelperMethods.isAuthenticationCheckRequiredForService(info)) {
	            isAdmin = false;
	            if (!id.equalsIgnoreCase(info.get("user_id"))) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10739);
	            }
	          }
	        
	         */
	        logger.debug("UserManagementResourceImplExtn:sendActivationCodeAndUsername: customerId:" +id);
	        Map<String, String> configurations = BundleConfigurationHandler
	                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
	        try {
	            CustomerDTO inputCustomerDTO = new CustomerDTO();
	            inputCustomerDTO.setId(id);
	            
	           
	            /**
	             * Fetches the userId for enrolling a customer
	             */
	            UserManagementBusinessDelegate userManagementbusinessDelegate = DBPAPIAbstractFactoryImpl
	                    .getBusinessDelegate(UserManagementBusinessDelegate.class);
	            CustomerDTO responseCustomerDTO = userManagementbusinessDelegate
	                    .fetchCustomerIdForEnrollment(inputCustomerDTO, dcRequest.getHeaderMap());
	            
	            String activationCodeLength = configurations.get(BundleConfigurationHandler.ACTIVATIONCODE_LENGTH);
	            if (StringUtils.isBlank(activationCodeLength))
	                activationCodeLength = "10";
	            String activationCode = generateAlphaNumericString(Integer.parseInt(activationCodeLength));

	            /**
	             * Credential checker entry for tracking activation code
	             */
	            userManagementbusinessDelegate.createEntryForCredentailCheckerTable(configurations, responseCustomerDTO,
	                    activationCode, dcRequest.getHeaderMap());
	            
	            boolean isSCAEnabled = false;
	            boolean isSCACommunicationEnabled = false;
	            isSCAEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("IS_SCA_ENABLED"));
				isSCACommunicationEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("SCA_COMMUNICATION"));
				if(!isSCAEnabled || isSCACommunicationEnabled) {
		            /**
		             * Fetch enrolling customer communication information
		             */
		            CustomerCommunicationDTO inputCustomerCommunicationDTO = new CustomerCommunicationDTO();
		            inputCustomerCommunicationDTO.setCustomer_id(responseCustomerDTO.getId());
		            inputCustomerCommunicationDTO.setCompanyLegalUnit(responseCustomerDTO.getHomeLegalEntity());
		            List<CustomerCommunicationDTO> customerCommunicationResponse = userManagementbusinessDelegate
		                    .fetchCustomerCommunicationDetailsForEnrollment(inputCustomerCommunicationDTO,
		                            dcRequest.getHeaderMap());
		            for (CustomerCommunicationDTO customerCommunicationDTO : customerCommunicationResponse) {
		                if (Boolean.parseBoolean(sendEmail) && DBPUtilitiesConstants.COMM_TYPE_EMAIL.equalsIgnoreCase(customerCommunicationDTO.getType_id())) {
		                    if (isAdmin)
		                        userManagementbusinessDelegate.sendEnrollUserIdToEmail(configurations, responseCustomerDTO,
		                                customerCommunicationDTO, activationCode, dcRequest.getHeaderMap());
		                }
		                logger.debug("sendActivationCodeAndUsername: customerCommunicationDTO.getValue()" +customerCommunicationDTO.getValue());
		                if (DBPUtilitiesConstants.COMM_TYPE_PHONE.equalsIgnoreCase(customerCommunicationDTO.getType_id())) {
		                    userManagementbusinessDelegate.sendEnrollActivationCodeToMobile(isAdmin,
		                            responseCustomerDTO.getIsEnrolled(),
		                            customerCommunicationDTO,
		                            activationCode, dcRequest.getHeaderMap());
		                }
		            }
				}
				
	           
	            result.addParam(new Param("status", "success"));
	            dcRequest.addRequestParam_("userId", responseCustomerDTO.getUserName());
	            dcRequest.addRequestParam_("activationCode", activationCode);
	        } catch (ApplicationException e) {
	            alert.prepareError("Exception occured while fetching enrolling userId" + e.getMessage()).log();
	            throw new ApplicationException(e.getErrorCodeEnum());
	        } catch (Exception e) {
	            alert.prepareError("Exception occured while fetching enrolling userId" + e.getMessage()).log();
	            throw new ApplicationException(ErrorCodeEnum.ERR_10738);
	            
	        }
	      
	        return result;
	    }

	    public String generateAlphaNumericString(int size) {
	        StringBuilder sb = new StringBuilder(size);
	        String alphaNumbericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
	        for (int i = 0; i < size; i++) {
	            int index = (int) (alphaNumbericString.length() * Math.random());
	            sb.append(alphaNumbericString.charAt(index));
	        }
	        return sb.toString();
	    }

	 


}
