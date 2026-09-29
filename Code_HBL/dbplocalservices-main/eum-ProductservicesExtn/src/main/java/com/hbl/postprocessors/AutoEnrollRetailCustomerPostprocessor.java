package com.hbl.postprocessors;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.hbl.dto.PrimaryAccountDTO;
import com.hbl.resource.constants.HBLConstants;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.utils.InfinityConstants;

public class AutoEnrollRetailCustomerPostprocessor implements DataPostProcessor2 {
	LoggerUtil logger = new LoggerUtil(AutoEnrollRetailCustomerPostprocessor.class);
	private String companyId = null;
	private String serviceName = null;
	private String operationName = null;
	private Map<String,Object> headerMap=null;

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		String isDBXUser = result.getParamValueByName("isDBXUser");
		String userName = dcRequest.getParameter("userid");
		String password = dcRequest.getParameter("Password");
		String transactionPin = dcRequest.getParameter("pin");
		String migratedUser = result.getParamValueByName("migratedUser");
		headerMap=dcRequest.getHeaderMap();
		companyId = EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		boolean isMobileUser=userName!=null && (userName.startsWith("M")|| userName.startsWith("m"));
		logger.debug("AutoEnrollRetailCustomerPostprocessor is isMobileUser:" + isMobileUser);
		logger.debug("AutoEnrollRetailCustomer:isDBXUser:" + isDBXUser);
		logger.debug("AutoEnrollRetailCustomer:userName:" + userName);
		logger.debug("AutoEnrollRetailCustomer:password:" + password);
		logger.debug("AutoEnrollRetailCustomer:transactionPin:" + transactionPin);
		logger.debug("AutoEnrollRetailCustomerPostprocessor is migratedUser:" + migratedUser);
		String backendId = null;
		CustomerDTO customerDTO = null;
		 if (StringUtils.isNotBlank(isDBXUser) && isDBXUser.equalsIgnoreCase("false") && isMobileUser) {
			userName = dcRequest.getParameter("userid");
			password = dcRequest.getParameter("Password");
			customerDTO = new CustomerDTO();
			customerDTO.setUserName(userName);
			customerDTO.setPin(transactionPin);
			Result enrollResult=null;
			LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
			JSONObject legacyCustomer = getLegacyCustomer(customerDTO, dcRequest.getHeaderMap(), dcRequest);
			logger.debug("getLegacyCustomer legacyCustomer:" + legacyCustomer);
			if(legacyCustomer != null && !legacyCustomer.isEmpty()) {
				if(StringUtils.isBlank(transactionPin) &&  validateLegacyPassword(dcRequest, legacyCustomer, password)) {
					setError(result, "14020", ErrorCodeEnum.ERR_11026.getMessage());
					return result;
				}
				else if(StringUtils.isNotBlank(transactionPin) && validateLegacyPin(dcRequest, legacyCustomer, transactionPin)) {
					backendId = legacyCustomer.optString("customerId");
					logger.debug("AutoEnrollRetailCustomer backendId:" + backendId);
					enrollResult = postProcess(backendId, dcRequest, result, dcResponse);
					logger.debug("HBL::AutoEnrollRetailCustomer: enrollRetailCustomer: postProcess:enrollResult:" + ResultToJSON.convert(enrollResult));
					if(enrollResult!=null) {
						String dbpErrCode= enrollResult.getParamValueByName("dbpErrCode");
						String dbpErrMsg= enrollResult.getParamValueByName("dbpErrMsg");
						String isUserExists= enrollResult.getParamValueByName("isUserExists");
						String isUserEnrolled= enrollResult.getParamValueByName("isUserEnrolled");
						String channelAccess= enrollResult.getParamValueByName("channelAccess");
						String errmsg = enrollResult.getParamValueByName("errmsg");
						if (StringUtils.isNotBlank(errmsg)) {
							return enrollResult;
						}
							if(StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
								if(StringUtils.isNotBlank(isUserExists) && isUserExists.equalsIgnoreCase("false")) {
									setError(result, ErrorCodeEnum.ERR_10002.getErrorCodeAsString(), ErrorCodeEnum.ERR_10002.getMessage());
								}
								if(StringUtils.isNotBlank(isUserEnrolled) && isUserEnrolled.equalsIgnoreCase("true")) {
									setError(result, ErrorCodeEnum.ERR_10748.getErrorCodeAsString(), ErrorCodeEnum.ERR_10748.getMessage());
								}
								else {
									/*
									 *  Enroll Success
									 */
								logger.debug("AutoEnrollRetailCustomer is auto enrolled successfully:" + ResultToJSON.convert(enrollResult));
								enrollResult.addParam(new Param("isAutoEnrolled", "true"));
								JSONObject resultObj = new JSONObject(ResultToJSON.convert(enrollResult));
								setSuccessError(result, ErrorCodeEnum.ERR_10090.getErrorCodeAsString(), ErrorCodeEnum.ERR_10090.getMessage(), resultObj);
								}
							}
							else {
							setError(result, dbpErrCode, dbpErrMsg);
							}
					}else {
						setError(result, ErrorCodeEnum.ERR_10738.getErrorCodeAsString(), ErrorCodeEnum.ERR_10738.getMessage());
					}
				}
				else 
				{
					//Invalid password or pin
					setError(result, ErrorCodeEnum.ERR_10083.getErrorCodeAsString(), ErrorCodeEnum.ERR_10083.getMessage());
					
				}
			}
		 }
		 else if(StringUtils.isNotBlank(migratedUser) && migratedUser.equalsIgnoreCase("true") && isMobileUser) {
			 	userName = dcRequest.getParameter("userid");
				password = dcRequest.getParameter("Password");
				customerDTO = new CustomerDTO();
				customerDTO.setUserName(userName);
				customerDTO.setPin(transactionPin);
				Result enrollResult=null;
				//LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
				JSONObject legacyCustomer = getLegacyCustomerV1(customerDTO, dcRequest.getHeaderMap(), dcRequest);
				logger.debug("getLegacyCustomer legacyCustomer:" + legacyCustomer);
				if(legacyCustomer != null && !legacyCustomer.isEmpty()) {
					if(StringUtils.isBlank(transactionPin) &&  validateLegacyPassword(dcRequest, legacyCustomer, password)) {
						setError(result, "14020", ErrorCodeEnum.ERR_11026.getMessage());
						return result;
					}
					else if(StringUtils.isNotBlank(transactionPin) && validateLegacyPin(dcRequest, legacyCustomer, transactionPin)) {
						backendId = legacyCustomer.optString("customerId");
						logger.debug("AutoEnrollRetailCustomer backendId:" + backendId);
						enrollResult = sendActivationCode(legacyCustomer, dcRequest);
						logger.debug("HBL::AutoEnrollRetailCustomer: enrollRetailCustomer: postProcess:enrollResult:" + ResultToJSON.convert(enrollResult));
						if(enrollResult!=null) {
							String dbpErrCode= enrollResult.getParamValueByName("dbpErrCode");
							String dbpErrMsg= enrollResult.getParamValueByName("dbpErrMsg");
							String status= enrollResult.getParamValueByName("status");
							String activationCodeDeliveryStatus= enrollResult.getParamValueByName("activationCodeDeliveryStatus");
								if(StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
									if(StringUtils.isNotBlank(status) && status.equalsIgnoreCase("true")) {
										/*
										 *  Enroll Success
										 */
									logger.debug("AutoEnrollRetailCustomer activation code sent successfully:" + ResultToJSON.convert(enrollResult));
									enrollResult.addParam(new Param("isAutoEnrolled", "true"));
									JSONObject resultObj = new JSONObject(ResultToJSON.convert(enrollResult));
									putAdditionalInfo(resultObj);
									result= new Result();
									setSuccessError(result, ErrorCodeEnum.ERR_10090.getErrorCodeAsString(), ErrorCodeEnum.ERR_10090.getMessage(), resultObj);
									}else {
										setError(result, ErrorCodeEnum.ERR_10795.getErrorCodeAsString(), ErrorCodeEnum.ERR_10795.getMessage());
									}
								}
								else{
								setError(result, dbpErrCode, dbpErrMsg);
								}
						}else {
							setError(result, ErrorCodeEnum.ERR_10738.getErrorCodeAsString(), ErrorCodeEnum.ERR_10738.getMessage());
						}
					}
					else 
					{
						//Invalid password or pin
						setError(result, ErrorCodeEnum.ERR_10083.getErrorCodeAsString(), ErrorCodeEnum.ERR_10083.getMessage());
						
					}
				}
			 
		 }
		return result;
	}
	public void putAdditionalInfo(JSONObject resultObj){
		resultObj.put("isUserExists", "true");
		resultObj.put("isActivationCodeSent", "true");
		resultObj.put("firstName", "Checkuser1");
		resultObj.put("legalEntityId", "NP0010001");
		resultObj.put("activationCodeDeliveryStatus", "true");
		resultObj.put("userNameGenerationStatus", "true");
		resultObj.put("userName", "M0102386");
		resultObj.put("activationCode", "Uz4zV");
		resultObj.put("status", "3");
		resultObj.put("phoneNumber", "+977-9704128282");
		resultObj.put("email", "mohan.gn@bahwancybertek.com");
		resultObj.put("channelAccess", "BOTH");
		resultObj.put("isConsentProvided", "true");
		resultObj.put("contractId", "8857611501");
		resultObj.put("opstatus", 0);
		resultObj.put("httpStatusCode", 0);
	}
	private Result postProcess(String backendId,DataControllerRequest dcRequest, Result dbxResult, DataControllerResponse dcResponse) throws DBPApplicationException, ApplicationException {
		JSONObject coreCustomer = null;
		JSONArray accountsArray = null;
		String accountHolderName = null;
		String accountNo = null;
		String emailId = null;
		String phoneNo = null;
		String channelAccess= null;
		String consentProvided= null;
		Result enrollResult = new Result();
		String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
		String integrationType = EnvironmentConfigurationsHandler.getServerProperty("ARRANGEMENTS_BACKEND");
		coreCustomer = searchCoreCustomerById(backendId, dcRequest);
		logger.debug("AutoEnrollRetailCustomer coreCustomer result:" + coreCustomer);
		String dbpErrCode= coreCustomer.optString("dbpErrCode");
		String dbpErrMsg= coreCustomer.optString("dbpErrMsg");
		String isUserExists= enrollResult.getParamValueByName("isUserExists");
		String errorMsg="";
		if (coreCustomer != null) {
			if(StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)){
			accountHolderName = coreCustomer.optString("name");
			emailId = coreCustomer.optString("email");
			phoneNo = coreCustomer.optString("phone");
			channelAccess=coreCustomer.optString("channelAccess");
			consentProvided = coreCustomer.optString("isEnrollConsentProvided");
			MembershipDTO membershipDTO = new MembershipDTO();
			membershipDTO.setId(coreCustomer.optString("id"));
			membershipDTO.setCompanyLegalUnit(companyId);
			if(channelAccess.equalsIgnoreCase("NONE")) {
			errorMsg="Sorry! Can't migrate. Your ebanking access is currently disabled. Please contact support to activate your digital banking.";
			setError(enrollResult, ErrorCodeEnum.ERR_10290.getErrorCodeAsString(), errorMsg);
			return enrollResult;
			}
			if(!consentProvided.equalsIgnoreCase("YES")) {
				errorMsg="Sorry! Can't migrate. Your ebanking consent is not provided. Please contact support to activate your digital banking.";
				setError(enrollResult, ErrorCodeEnum.ERR_10290.getErrorCodeAsString(), errorMsg);
				return enrollResult;
			}
			if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")
					&& StringUtils.isNotBlank(integrationType) && "T24".equalsIgnoreCase(integrationType)) {
				accountsArray = getCoreCustomerAccountsT24(membershipDTO, dcRequest);
				if (accountsArray.length() > 0) {
					JSONObject primaryAccountObj = getPrimaryAccountFromJsonArray(accountsArray, dcRequest);//getPrimaryAccount(accountsArray, dcRequest);
					logger.debug("AutoEnrollRetailCustomer primaryAccountObj:" + primaryAccountObj);
					if (primaryAccountObj != null) {
						accountNo = primaryAccountObj.getString("accountId");
					} else {
						setError(enrollResult, ErrorCodeEnum.ERR_10275.getErrorCodeAsString(), "customer does not contain valid accounts.");
					}
				}
				else{
					logger.debug("no accounts found");
					setError(enrollResult, ErrorCodeEnum.ERR_10275.getErrorCodeAsString(), ErrorCodeEnum.ERR_10275.getMessage());
				}
				if(StringUtils.isNotBlank(accountHolderName) && StringUtils.isNotBlank(accountNo) && StringUtils.isNotBlank(emailId) && StringUtils.isNotBlank(phoneNo)) {
					Map<String, Object> inputMap = new HashMap<String, Object>();
					inputMap.put("accountName", accountHolderName);
					inputMap.put("accountNumber", accountNo);
					inputMap.put("email", emailId);
					inputMap.put("mobileNumber", phoneNo);
					inputMap.put("selfMigrationFlow", "true");
					logger.debug("AutoEnrollRetailCustomer enrollRetailCustomer inputMap:" + inputMap);
					enrollResult = enrollRetailCustomer(inputMap, dcRequest, dcResponse);
				}
			}
		}
		else {
			logger.debug("AutoEnrollRetailCustomer error occured while fetching the customer :" + coreCustomer);
			setError(enrollResult, dbpErrCode, dbpErrMsg);
		}
		}else {
			logger.debug("AutoEnrollRetailCustomer customer Not found:" + coreCustomer);
			setError(enrollResult, ErrorCodeEnum.ERR_10002.getErrorCodeAsString(), ErrorCodeEnum.ERR_10002.getMessage());
		}
		return enrollResult;
	}
	public JSONObject getLegacyCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap,
			DataControllerRequest dcRequest) throws DBPApplicationException {
		Map<String, Object> inputParams = new HashMap<>();
		JSONObject legacyCustomer = null;
		String filter = "username" + DBPUtilitiesConstants.EQUAL + "'" + customerDTO.getUserName() + "'";
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		operationName = "dbxdb_legacycustomer_get";
		JSONObject responseObj = callInternalServiceAndGetResult(serviceName, operationName, inputParams, headerMap);
		if (responseObj != null) {
			JSONArray legacycustomers = responseObj.getJSONArray("legacycustomer");
			legacyCustomer = legacycustomers.length() > 0 ? legacycustomers.getJSONObject(0) : new JSONObject();
		}
		return legacyCustomer;
	}


	private JSONObject callInternalServiceAndGetResult(String serviceid, String operationid,
			Map<String, Object> inputmap, Map<String, Object> headers) throws DBPApplicationException {
		JSONObject responseObj = null;
		try {
			String res = DBPServiceExecutorBuilder.builder().withOperationId(operationid)
					.withRequestParameters(inputmap).withServiceId(serviceid).withRequestHeaders(headers).build()
					.getResponse();
			responseObj = new JSONObject(res);
		} catch (Exception e) {
			logger.error("Exception caught while calling Integration service ", e);
		}
		return responseObj;
	}

	private JSONObject searchCoreCustomerById(String customerId, DataControllerRequest dcRequest)
			throws DBPApplicationException {
		Map<String, Object> inputParams = new HashMap<>();
		JSONObject customerResponse = null;
		HelperMethods.addJWTAuthHeader(dcRequest.getHeaderMap(), AuthConstants.PRE_LOGIN_FLOW);
		Map<String, Object> headersMap = dcRequest.getHeaderMap();
		headersMap.put("companyId", companyId);
		inputParams.put("customerId", customerId);
		serviceName = HBLConstants.HBL_T24ISUSER_INTEGRATION_SERVICE;
		operationName = OperationName.CORE_CUSTOMER_SEARCH;
		JSONObject t24Response = callInternalServiceAndGetResult(serviceName, operationName, inputParams, headersMap);
		logger.debug(
				"HBL::AutoEnrollRetailCustomer: callInternalServiceAndGetString:customer t24 Response :" + t24Response);
		if (t24Response.has(DBPDatasetConstants.DATASET_CUSTOMERS) &&  t24Response.getJSONArray(DBPDatasetConstants.DATASET_CUSTOMERS).length() > 0) {
			customerResponse = t24Response.getJSONArray(DBPDatasetConstants.DATASET_CUSTOMERS).getJSONObject(0);
		} 
		if(t24Response.has("error")) {
			String error= t24Response.getString("error");
			if(StringUtils.isNotBlank(error)) {
				try {
			customerResponse = new JSONObject();
			JSONObject errorObj = new JSONObject(error);
			customerResponse.put("dbpErrCode", "10002");
			customerResponse.put("dbpErrMsg", errorObj.optString("message"));
			}catch (JSONException e) {
				customerResponse.put("dbpErrCode",  "10002");
				customerResponse.put("dbpErrMsg", error);
			}
			}
		}
		return customerResponse;
	}


	public JSONArray getCoreCustomerAccountsT24(MembershipDTO membershipDTO, DataControllerRequest dcRequest)
			throws DBPApplicationException {
		JsonObject response = new JsonObject();
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("customerId", membershipDTO.getId());
		HelperMethods.addJWTAuthHeader(dcRequest.getHeaderMap(), AuthConstants.PRE_LOGIN_FLOW);
		serviceName = com.kony.dbputilities.util.ServiceId.T24ISACCOUNTS_INTEGRATION_SERVICE;
		operationName = OperationName.GET_CORE_CUSTOMER_ACCOUNTS;
		JSONObject customerAccounts = callInternalServiceAndGetResult(serviceName, operationName, inputParams,
				dcRequest.getHeaderMap());
		logger.debug("HBL::AutoEnrollRetailCustomer: getCoreCustomerAccountsT24:customerAccounts Response :"
				+ customerAccounts);
		JSONArray coreCustomerAccounts = new JSONArray();
		if (customerAccounts != null && customerAccounts.has(DBPDatasetConstants.DATASET_RECORDS)
				&& customerAccounts.getJSONArray(DBPDatasetConstants.DATASET_RECORDS).length() > 0) {
			coreCustomerAccounts = customerAccounts.getJSONArray(DBPDatasetConstants.DATASET_RECORDS);
		}
		return coreCustomerAccounts;
	}

	public static Map<String, String> getAccountTypeMapping(Map<String, Object> headersMap) {
		String accountTypeProperties = BundleConfigurationHandler
				.fetchConfigurationValueOnKey(BundleConfigurationHandler.BUNDLEID_DBP, "ACCOUNT_TYPES", headersMap);
		Map<String, String> integratedToBase = new HashMap<>();
		try {
			integratedToBase = new ObjectMapper().readValue(accountTypeProperties, HashMap.class);
		} catch (JsonProcessingException e) {
		}
		return integratedToBase;
	}
	private Boolean validateLegacyPassword(DataControllerRequest dcRequest, JSONObject customer, String password) {
		String dbPassword = customer.optString("password");
		String hashedPassword = hashPassword(password);
		return dbPassword.equalsIgnoreCase(hashedPassword);
	}
	private Boolean validateLegacyPasswordAndPin(DataControllerRequest dcRequest, JSONObject customer, String password, String transactionPin) {
		String dbPassword = customer.optString("password");
		String hashedPassword = hashPassword(password);
		String dbTransactionPin = customer.optString("transactionPin");
		String hashedTransactionPin = hashPassword(transactionPin);
		return dbPassword.equalsIgnoreCase(hashedPassword)&&  dbTransactionPin.equalsIgnoreCase(hashedTransactionPin) ;
	}
	private Boolean validateLegacyPin(DataControllerRequest dcRequest, JSONObject customer, String transactionPin) {
		String dbTransactionPin = customer.optString("transactionPin");
		String hashedTransactionPin = hashPassword(transactionPin);
		return dbTransactionPin.equalsIgnoreCase(hashedTransactionPin) ;
	}

	private String hashPassword(String password) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
			byte[] hash = sha256.digest(bytes);
			return getStringFromHash(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 algorithm not available", e);
		}
	}

	private static String getStringFromHash(byte[] hash) {
		StringBuilder result = new StringBuilder();
		for (byte b : hash) {
			result.append(String.format("%02X", b));
		}
		return result.toString();
	}
	private Result enrollRetailCustomer(Map<String, Object> inputmap, DataControllerRequest dcRequest, DataControllerResponse dcResponse) {
		serviceName="eumProductServices";
		operationName="EnrollRetailUser";
		Result res = new Result();
		try {
		InfinityUserManagementResource resource =
                 DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
		Object[] inputArray= new Object[2];
		inputArray[1]=inputmap;
		res = resource.enrollRetailUserOperation("EnrollRetailUser", inputArray, dcRequest, dcResponse);
		logger.debug("HBL::AutoEnrollRetailCustomer: enrollRetailCustomer: response:" + ResultToJSON.convert(res));
		}catch(ApplicationException e) {
			logger.error("Exception caught while enrollRetailCustomer ", e);
			res.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
			res.addParam(new Param("dbpErrMsg",  e.getErrorCodeEnum().getMessage()));
		}
		catch (Exception e) {
			logger.error("Exception caught while enrollRetailCustomer ", e);
			res.addParam(new Param("dbpErrCode", "500"));
			res.addParam(new Param("dbpErrMsg", e.getMessage()));
		}
		return res;
	}
	 private void setError(Result retValue, String dbxErrorCode, String dbxErrMessage) {
	        retValue.addParam(new Param("errmsg", dbxErrMessage, "String"));
	        retValue.addParam(new Param("backend_error_code", dbxErrorCode, "int"));
	        retValue.addParam(new Param("backend_error_message", dbxErrMessage, "String"));
	        retValue.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "401", "int"));
	        retValue.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "20921", "int"));
	    }
	 private void setSuccessError(Result retValue, String dbxErrorCode, String dbxErrMessage, JSONObject resultObj) {
	        retValue.addParam(new Param("errmsg", dbxErrMessage, "String"));
	        retValue.addParam(new Param("backend_error_code", dbxErrorCode, "int"));
	        retValue.addParam(new Param("backend_error_message", resultObj.toString(), "String"));
	        retValue.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "401", "int"));
	        retValue.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "20921", "int"));
	        logger.debug("HBL::AutoEnrollRetailCustomer: setSuccessError: postProcess:enrollResult:" + retValue);
	    }
	 private JSONObject getPrimaryAccount(JSONArray accountsArray, DataControllerRequest dcRequest) {
			JSONObject account = null;
			Map<String, String> integratedToBase = getAccountTypeMapping(dcRequest.getHeaderMap());
			boolean isAtleastSavingOrCurrentAccountFound = false;
			if (accountsArray != null) {
				for (int i = 0; i < accountsArray.length(); i++) {
					JSONObject accountObj = accountsArray.getJSONObject(i);
					accountObj.put("accountType", integratedToBase.get(accountObj.optString("productId")));
					accountObj.put("Type_id", HelperMethods.getAccountsTypes().get(accountObj.get("accountType")));
					String productId=accountObj.optString("productId");
					String customerAccountType = accountObj.getString("accountType");
					if (customerAccountType.equalsIgnoreCase("Savings") /* || customerAccountType.equalsIgnoreCase("Checking") */) {
						isAtleastSavingOrCurrentAccountFound = true;
						account = accountObj;
						break;
					}
				}
			}
			return account;
		}
	 public static JSONObject getPrimaryAccountFromJsonArray(JSONArray accountArray, DataControllerRequest dcRequest) {
	        if (accountArray == null || accountArray.isEmpty()) {
	            return new JSONObject().put("message", "No accounts available");
	        }
	        // Sort based on customerDetails length to get the primary owner accounts
	        List<JSONObject> list = new ArrayList<>();
	        for (int i = 0; i < accountArray.length(); i++) {
	            list.add(accountArray.getJSONObject(i));
	        }
	        list.sort(Comparator.comparingInt(o -> o.getJSONArray("customerDetails").length()));
	        accountArray=new JSONArray(list);

	        // Find primary non-HRSA
	        JSONObject primaryNonHRSA = findAccount(accountArray, true, false, dcRequest);
	        if (primaryNonHRSA != null) return primaryNonHRSA;

	        //Find non-primary non-HRSA
	        JSONObject nonPrimaryNonHRSA = findAccount(accountArray, false, false, dcRequest);
	        if (nonPrimaryNonHRSA != null) return nonPrimaryNonHRSA;
	        
	     // Find primary HRSA
	        JSONObject primaryHRSA = findAccount(accountArray, true, true, dcRequest);
	        if (primaryHRSA != null) return primaryHRSA;

	        // Fallback HRSA (any HRSA account)
	        JSONObject hrsaFallback = findAccount(accountArray, false, true, dcRequest);
	        if (hrsaFallback != null) return hrsaFallback;

	        //If no match found
	        return null;
	    }

	    /**
	     * Helper function to find an account matching the given criteria.
	     */
	    private static JSONObject findAccount(JSONArray array, boolean mustBePrimary, boolean isHRSA, DataControllerRequest dcRequest) {
	    	Map<String, String> integratedToBase = getAccountTypeMapping(dcRequest.getHeaderMap());
	        for (int i = 0; i < array.length(); i++) {
	            JSONObject accountObj = array.getJSONObject(i);
	            boolean isPrimary = accountObj.optBoolean("isPrimary", false);
	            String productId = accountObj.optString("productId", "");
	            String accountType = integratedToBase.get(accountObj.optString("productId"));
	            if(StringUtils.isNotBlank(accountType) ) {
	            accountObj.put("accountType", accountType);
				accountObj.put("Type_id", HelperMethods.getAccountsTypes().get(accountType));
				if (accountType.equalsIgnoreCase("Savings")){ 
	            boolean hrsaMatch = "HRSA".equalsIgnoreCase(productId);
	            if (mustBePrimary && !isPrimary) continue;  // skip non-primary if we’re looking for primary
	            if (isHRSA && hrsaMatch) return accountObj;
	            if (!isHRSA && !hrsaMatch) return accountObj;
				}
	        }
	        }
	        return null;
	    }
	    public JSONObject getLegacyCustomerV1(CustomerDTO customerDTO, Map<String, Object> headerMap,
				DataControllerRequest dcRequest) throws DBPApplicationException {
			DBXResult dbxResult = new DBXResult();
			Map<String, Object> inputParams = new HashMap<>();
			JSONObject legacyCustomer = null;
			String filter = "username" + DBPUtilitiesConstants.EQUAL + "'" + customerDTO.getUserName() + "'";
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			operationName = "dbxdb_legacycustomerV1_get";
			JSONObject responseObj = callInternalServiceAndGetResult(serviceName, operationName, inputParams, headerMap);
			if (responseObj != null) {
				JSONArray legacycustomers = responseObj.getJSONArray("legacycustomerV1");
				legacyCustomer = legacycustomers.length() > 0 ? legacycustomers.getJSONObject(0) : new JSONObject();
			}
			return legacyCustomer;
		}
	    public JSONObject getDBXCustomer(String customerId, DataControllerRequest dcRequest) {
			String operationName = "dbxdb_customer_get";
			JSONObject dbxCustomer=null;
			try {
			Map<String, Object> inputParams = new HashMap<>();
			String filter = "id" + DBPUtilitiesConstants.EQUAL + "'" + customerId+ "'";
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			Result response = CommonUtils.callIntegrationService(dcRequest, inputParams, dcRequest.getHeaderMap(), serviceName,operationName, false);
			if (response != null) {
				JSONObject responseObj = new JSONObject(ResultToJSON.convert(response));
				JSONArray customers = responseObj.getJSONArray("customer");
				 dbxCustomer = customers.length() > 0 ? customers.getJSONObject(0) : new JSONObject();
			}
			}catch (DBPApplicationException e) {
				logger.debug("Exception occured while getting the DBX customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
			}catch (Exception e) {
				logger.debug("Exception occured while getting the DBX customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
			}
			return dbxCustomer;
		}
	    public Result sendActivationCode(JSONObject legacyDetails, DataControllerRequest dcRequest){
	    	JSONObject customerJson=getDBXCustomer(legacyDetails.getString("infinityId"),dcRequest);
	    	Map<String, String> activationMap = new HashMap<>();
			activationMap.put(InfinityConstants.userId, customerJson.optString("id"));
			activationMap.put("userName",  customerJson.optString("UserName"));
			activationMap.put("contractStatus", DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
			activationMap.put("Phone", legacyDetails.optString("mobileNumber"));
			activationMap.put("Email", legacyDetails.optString("email"));
			Object[] inputArray= new Object[2];
			inputArray[1] = activationMap;
			Result result = new Result();
	        try {
	            InfinityUserManagementResource resource =
	                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
	            result = resource.generateInfinityUserActivationCodeAndUsername("SendInfinityUserUserNameAndActivationCode", inputArray, dcRequest, null);
	        } catch (ApplicationException e) {
	            e.getErrorCodeEnum().setErrorCode(result);
	            logger.debug("Exception occured while generating the username and activation code" + e.getStackTrace());
	        } catch (Exception e) {
	        	logger.debug("Exception occured while generating the username and activation code" + e.getStackTrace());
	            ErrorCodeEnum.ERR_10795.setErrorCode(result);
	        }
	        return result;
	    }

}
