package com.kony.eum.dbputilities.customersecurityservices;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.sessionmanager.SessionManager;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EncryptionUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
//import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.dbputilities.util.logger.LoggerUtil;
//import com.kony.dbx.util.CommonUtils;
import com.kony.eum.dbputilities.customersecurityservices.CustomerLogin;
import com.kony.eum.dbputilities.customersecurityservices.GetCustomerPreferencesConcurrent;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.exceptions.MetricsException;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.javaservice.CustomerGetByUserNameOperation;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.utils.InfinityConstants;

public class CustomerLogin implements JavaService2 {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final int EXPIRY_TIME = (20 * 60);


    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        String ipAddress = dcRequest.getHeader("CLIENT_IP");
        if(StringUtils.isNotBlank(ipAddress)) {
        	ipAddress = HelperMethods.decryptClientIp(ipAddress);
        }
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = (Result) new CustomerGetByUserNameOperation().invoke(methodID, inputArray, dcRequest, dcResponse);
            if (isAccessGrantNeeded(inputParams)
                    && StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "CustomerType_id"))) {
                // if (accessGranted(HelperMethods.getFieldValue(result, "CustomerType_id"), dcRequest, inputParams)) {
            	
            	/*
                 * Migration Flow
                 */
            	 diagnostic.prepareDebug("result : " + ResultToJSON.convert(result)).log();
            	Record user = result.getAllDatasets().get(0).getRecord(0);
                String Is_BBOA = StringUtils.isNotBlank(user.getParamValueByName("Is_BBOA"))? user.getParamValueByName("Is_BBOA"): "";
                if (isUserNew(user, "") && Is_BBOA.equalsIgnoreCase("true")) {
                	result = new Result();
                	 Record record = new Record();
                     record.setId(DBPUtilitiesConstants.USR_ATTR);
                     ErrorCodeEnum.ERR_10095.setErrorCode(result);
                     result.addRecord(record);
                     result.addParam(new Param("migratedUser", "true"));
                     return result;
                }
                diagnostic.prepareDebug("After result : " + ResultToJSON.convert(result)).log();
                /*
                 *  Regular Flow
                 */
                result = postProcess(inputParams, dcRequest, result, methodID, dcResponse, inputArray);
                saveClientIpToCache(result, ipAddress, dcRequest);
                
                // } else {
                // ErrorCodeEnum.ERR_10097.setErrorCode(result);
                // }
            } else {
                String reportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
                if (isAccessGrantNeeded(inputParams) && StringUtils.isBlank(reportingParams)) {
                    ErrorCodeEnum.ERR_10098.setErrorCode(result);
                } else {
                    result = postProcess(inputParams, dcRequest, result, methodID, dcResponse, inputArray);
                }
            }
        }
        return result;
    }

    private boolean isAccessGrantNeeded(Map<String, String> inputParams) {
        String sharedSecret = inputParams.get("sharedSecret");
        return (StringUtils.isBlank(sharedSecret) || "$sharedSecret".equalsIgnoreCase(sharedSecret));
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;

        diagnostic.prepareDebug("InputParams in the beggining of preProcess : " + inputParams).log();

        String username = inputParams.get("userid");
        String password = inputParams.get("Password");
        String pin = inputParams.get(DBPUtilitiesConstants.PIN);
        String sharedSecret = inputParams.get("sharedSecret");
        String csrUserName = inputParams.get("CSRUserName");
        String sessionToken = inputParams.get("session_token");
        String prospectLogin = inputParams.get("prospect");
        String mfaName = getMfaName(dcRequest);

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(password) && !"$password".equalsIgnoreCase(password)) {
            sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append("'").append(username).append("'");
        } else if (StringUtils.isNotBlank(pin) && !"$pin".equalsIgnoreCase(pin)) {
            sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append(username);
        } else if (StringUtils.isNotBlank(sharedSecret) && !"$sharedSecret".equalsIgnoreCase(sharedSecret)
                && (StringUtils.isBlank(csrUserName) || "$CSRUserName".equals(csrUserName))) {
            sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append("admin");
            username = "admin";
        } else if (StringUtils.isNotBlank(sharedSecret) && !"$sharedSecret".equalsIgnoreCase(sharedSecret)) {
            sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append(csrUserName);
            username = csrUserName;
        } else if (StringUtils.isNotBlank(sessionToken) && !"$session_token".equalsIgnoreCase(sessionToken)
                && (StringUtils.isBlank(password) || "$password".equalsIgnoreCase(password))
                && (StringUtils.isBlank(sharedSecret) || "$sharedSecret".equalsIgnoreCase(sharedSecret))
                && (StringUtils.isBlank(pin) || "$pin".equalsIgnoreCase(pin)
                        && (Boolean.parseBoolean(prospectLogin))
                        && (StringUtils.isNotEmpty(mfaName)) && mfaName.equalsIgnoreCase("Origination"))) {
            sb.append("Session_id").append(DBPUtilitiesConstants.EQUAL).append(sessionToken);
        } else if (StringUtils.isNotBlank(sessionToken) && !"$session_token".equalsIgnoreCase(sessionToken)
                && (StringUtils.isBlank(password) || "$password".equalsIgnoreCase(password))
                && (StringUtils.isBlank(sharedSecret) || "$sharedSecret".equalsIgnoreCase(sharedSecret))
                && (StringUtils.isBlank(pin) || "$pin".equalsIgnoreCase(pin))) {

            if (AdminUtil.verifyCSRAssistToken(dcRequest, sessionToken)) {
                sb.append("UserName").append(DBPUtilitiesConstants.EQUAL)
                        .append(dcRequest.getAttribute("CSRAssist_Customer_username").toString());
                username = dcRequest.getAttribute("CSRAssist_Customer_username").toString();
            } else {
                ErrorCodeEnum.ERR_10099.setErrorCode(result,
                        String.valueOf(dcRequest.getAttribute("CSR_Response").toString()));
                status = false;
            }

        } else {
            ErrorCodeEnum.ERR_10083.setErrorCode(result);
            status = false;
        }

        inputParams.put("UserName", username);

        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());

        diagnostic.prepareDebug("InputParams from preProcess : " + inputParams).log();

        return status;
    }

    private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
            String methodID, DataControllerResponse dcResponse, Object[] inputArray)
            throws MetricsException, ParseException, HttpCallException, Exception {
        Result retVal = null;
        diagnostic.prepareDebug("Input Params in the beggining of postProcess : " + inputParams).log();
        String password = inputParams.get("Password");
        String pin = inputParams.get(DBPUtilitiesConstants.PIN);
        String sharedSecret = inputParams.get("sharedSecret");
        String sessionToken = inputParams.get("session_token");
        String prospectLogin = inputParams.get("prospect");
        String mfaName = getMfaName(dcRequest);
        Record user = result.getAllDatasets().get(0).getRecord(0);
        
        if(StringUtils.isNotEmpty(mfaName) && mfaName.equalsIgnoreCase("Origination") && !isUserActive(user,mfaName)) {
        	retVal = new Result();
        	ErrorCodeEnum.ERR_10092.setErrorCode(retVal);
        }
        else if (StringUtils.isNotBlank(pin) && !"$pin".equalsIgnoreCase(pin)) {
            retVal = postProcessForPin(inputParams, dcRequest, result, inputParams, methodID, dcResponse, inputArray);
        } else if (StringUtils.isNotBlank(sessionToken) && !"$session_token".equalsIgnoreCase(sessionToken)
                && (StringUtils.isBlank(password) || "$password".equalsIgnoreCase(password))
                && (StringUtils.isBlank(sharedSecret) || "$sharedSecret".equalsIgnoreCase(sharedSecret))
                && (StringUtils.isBlank(pin) || "$pin".equalsIgnoreCase(pin)
                        && (Boolean.parseBoolean(prospectLogin))
                        && (StringUtils.isNotEmpty(mfaName)) && mfaName.equalsIgnoreCase("Origination"))) {
            retVal = postProcessForOnboardingST(inputParams, dcRequest, result, methodID, dcResponse, inputArray);
        } else if ((StringUtils.isNotBlank(sessionToken) && !"$session_token".equalsIgnoreCase(sessionToken))
                && (StringUtils.isBlank(password) || "$password".equalsIgnoreCase(password))
                && (StringUtils.isBlank(sharedSecret) || "$sharedSecret".equalsIgnoreCase(sharedSecret))
                && (StringUtils.isBlank(pin) || "$pin".equalsIgnoreCase(pin))) {
            retVal = postProcessForST(inputParams, dcRequest, result, methodID, dcResponse, inputArray);
        } else if (StringUtils.isNotBlank(password) && !"$password".equalsIgnoreCase(password)) {
            retVal = postProcessForUserName(inputParams, dcRequest, result, methodID, dcResponse, inputArray);
        } else {
            retVal = postProcessForSharedSecret(sharedSecret, dcRequest, result, inputParams, methodID, dcResponse,
                    inputArray);
        }
        
        /**
         * set defaultLegalEntity in cache when no error and defaultLegalEntity is set
         */
        
		if (!HelperMethods.hasDBPErrorMSG(retVal) && user != null) {
			String defaultLegalEntity = user.getParamValueByName("defaultLegalEntity");
			
			/**
	         * set homeLegalEntity as defaultLegalEntity in cache when no error for prospect so that
	         * getFeaturesAndPermissions would work
	         */
			
			if(Boolean.parseBoolean(prospectLogin)) {
				defaultLegalEntity = user.getParamValueByName("homeLegalEntity");
				user.addStringParam("defaultLegalEntity", defaultLegalEntity);
			}
			
			if (StringUtils.isNoneBlank(defaultLegalEntity)) {
				Record securityAttributes = retVal.getRecordById("security_attributes");
				LegalEntityUtil.setCurrentLegalEntityIdInCache(
						securityAttributes.getParamValueByName("session_token"), defaultLegalEntity);
			}
		}

		return retVal;
    }

    private Result postProcessForSharedSecret(String sharedSecret, DataControllerRequest dcRequest, Result result,
            Map<String, String> inputParams, String methodID, DataControllerResponse dcResponse, Object[] inputArray) {
        String sharedSecretFromDB = "";
        String key = "";
        String filter = "";
        Result retVal = new Result();
        Result systemConfig = new Result();

        filter = "PropertyName" + DBPUtilitiesConstants.EQUAL + URLConstants.DBP_API_ACCESS_TOKEN;
        try {
            systemConfig = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.SYSTEM_CONFIGURATION_GET);
        } catch (HttpCallException e) {
            alert.prepareError("Caught exception while Getting System Configurations: ", e).log();
        }
        sharedSecretFromDB = HelperMethods.getFieldValue(systemConfig, "PropertyValue");
        try {
            key = URLFinder.getPathUrl(URLConstants.DBP_ACCESS_TOKEN, dcRequest);
        } catch (Exception e) {
            key = "";
            alert.prepareError("Caught exception while Getting DBP_ACCCESS_TOKEN: ", e).log();
        }
        if (StringUtils.isBlank(key) || StringUtils.isBlank(sharedSecretFromDB) || StringUtils.isBlank(sharedSecret)) {
            ErrorCodeEnum.ERR_10086.setErrorCode(retVal);
            return retVal;
        }
        try {
            sharedSecretFromDB = EncryptionUtils.decrypt(sharedSecretFromDB, key);
        } catch (Exception e) {

            alert.prepareError("Caught exception while Decrypting: ", e).log();
        }
        if (sharedSecret.equals(sharedSecretFromDB)) {
            try {
                retVal = sessionAttributes(dcRequest, result, inputParams, methodID, dcResponse, inputArray);
            } catch (Exception e) {
                retVal = new Result();
                ErrorCodeEnum.ERR_10086.setErrorCode(retVal);
                alert.prepareError("Caught exception while setting SessionAttributes: ", e).log();
            }
        } else {
            ErrorCodeEnum.ERR_10086.setErrorCode(retVal);
        }

        diagnostic.prepareDebug("Response from postProcessForSharedSecret : " + ResultToJSON.convert(retVal)).log();

        return retVal;
    }

    private Result postProcessForUserName(Map<String, String> inputParams, DataControllerRequest dcRequest,
            Result result, String methodID, DataControllerResponse dcResponse, Object[] inputArray)
            throws MetricsException, ParseException, HttpCallException, Exception {
        Result retVal = new Result();
        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
        String mfaName = getMfaName(dcRequest);
        diagnostic.prepareDebug("Application name"+ mfaName).log();
        if (StringUtils.isNotBlank(pm.getDbpErrorCode())) {
            ErrorCodeEnum.ERR_10164.setErrorCode(retVal, pm.getDbpErrorCode(), pm.getDbpErrorMessage());
            return retVal;
        }

        String password = inputParams.get("Password");

        String prospectLogin = inputParams.get("prospect");

        if (StringUtils.isBlank(prospectLogin)) {
            prospectLogin = "false";
        }

        if (HelperMethods.hasRecords(result)) {
        	/*
        	 * Verifying channel Access from CBS
        	 */
            Record user = result.getAllDatasets().get(0).getRecord(0);
            String customerId = user.getParam("id").getValue();
            String coreCustomerId=getCoreCustomerIdFromCustomerId(customerId, dcRequest);
            String legalEntityId = user.getParamValueByName("companyLegalUnit");
            MembershipDTO membershipDTO = new MembershipDTO();
        	membershipDTO.setId(coreCustomerId);
        	membershipDTO.setCompanyLegalUnit(legalEntityId);
        	try {
        	JsonObject coreCustomerJson = searchCoreCustomers(membershipDTO, dcRequest.getHeaderMap());
        	JSONObject channelAccessObj = verifyChannelAccess(coreCustomerJson, dcRequest);
 			String isUserHasChannelAccess=channelAccessObj.has("isUserHasChannelAccess")?channelAccessObj.getString("isUserHasChannelAccess"):"";
 			if(isUserHasChannelAccess.equalsIgnoreCase("No") || StringUtils.isEmpty(isUserHasChannelAccess)) {
 				retVal.addParam(new Param("dbpErrMsg", channelAccessObj.getString("dbpErrMsg")));
 				retVal.addParam(new Param("dbpErrCode", channelAccessObj.getString("dbpErrCode")));
 				retVal.addParam(new Param("opstatus", "0"));
 				return retVal;	
 			}
        	}catch (Exception e) {
        		ErrorCodeEnum.ERR_10278.setErrorCode(retVal);
        		//ErrorCodeEnum.ERR_30000.setErrorCode(retVal);
				return retVal;
			}
            
            
            
            boolean eSignAgreementRequired = false;
            boolean isEagreementSigned = esignStatus(user);
            if (!isEagreementSigned && HelperMethods.isBusinessUserType(user.getParamValueByName("CustomerType_id"))) {
                eSignAgreementRequired = isUserEsignAgreementReq(user, dcRequest);
            }

            boolean isProspect = false;
            if (Boolean.parseBoolean(prospectLogin)
                    && user.getParamValueByName("CustomerType_id").equalsIgnoreCase("TYPE_ID_PROSPECT")
                    && (StringUtils.isNotEmpty(mfaName)) && mfaName.equalsIgnoreCase("Origination")) {
                isProspect = true;
            }
            
			if (StringUtils.isNotBlank(EnvironmentConfigurationsHandler.getValue(URLConstants.IS_SALESFORCE_INTEGRATED))
					&& EnvironmentConfigurationsHandler.getValue(URLConstants.IS_SALESFORCE_INTEGRATED)
							.equalsIgnoreCase("true")
					&& !isProspect) {
				if (StringUtils.isEmpty(user.getParamValueByName("SFDC_accountId"))) {
					ErrorCodeEnum.ERR_29025.setErrorCode(retVal);
					return retVal;
				}
			}
			
			String dbPassword = HelperMethods.getFieldValue(user, "Password");
			if(Boolean.parseBoolean(prospectLogin) && dbPassword.equalsIgnoreCase(InfinityConstants.defaultPassword)) {
				ErrorCodeEnum.ERR_29032.setErrorCode(retVal);
				return retVal;
			}

			boolean isPasswordExpired = isPasswordExpired(dcRequest, user, pm, isProspect);

            if (!Boolean.parseBoolean(prospectLogin)
                    && user.getParamValueByName("CustomerType_id").equalsIgnoreCase("TYPE_ID_PROSPECT")
                    && (StringUtils.isNotEmpty(mfaName)) && mfaName.equalsIgnoreCase("Origination")) {
                ErrorCodeEnum.ERR_10092.setErrorCode(retVal);
            } else if (!isProspect && isPasswordExpired) {
                ErrorCodeEnum.ERR_10135.setErrorCode(retVal);
            } else if (isUserExpired(user)) {
                ErrorCodeEnum.ERR_10087.setErrorCode(retVal);
            //} else if (isUserLocked(user, dcRequest, pm) && !tryAutoLockReset(user, dcRequest, pm)) {
            } else if (isUserLocked(user, dcRequest, pm)) {
                int x = pm.getAutoUnLockPeriod();
                ErrorCodeEnum.ERR_10088.setErrorCode(retVal,
                        "Your account has been temporarily locked due to multiple unsuccessful Sign in attempts. To unlock it, please contact our customer support.");
            } else if (isUserSuspended(user)) {
                ErrorCodeEnum.ERR_10089.setErrorCode(retVal);
            } else if (isUserNew(user, mfaName)) {
                ErrorCodeEnum.ERR_10090.setErrorCode(retVal);
                if (hasPassword(user) && !isEagreementSigned && eSignAgreementRequired) {
                    ErrorCodeEnum.ERR_10091.setErrorCode(retVal);
                }
            } else if (!isUserActive(user, mfaName)) {
                ErrorCodeEnum.ERR_10092.setErrorCode(retVal);
            } else {
                if (eSignAgreementRequired) {
                    if (!isEagreementSigned) {
                        ErrorCodeEnum.ERR_10093.setErrorCode(retVal);
                        retVal.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "200", "int"));
                        retVal.addParam(new Param("isEAgreementRequired", "true", "String"));
                        retVal.addParam(new Param("isEagreementSigned", "false", "String"));
                        retVal.addParam(new Param("isProspectExpired", "true", "String"));
                        return retVal;
                    }
                }

				if (validatePassword(dcRequest, result, password)) {

					/*
					 * TOTP validation logic from Google Authenticator This validation is required
					 * only for Desktop channel
					 */

					ServicesManager sm = dcRequest.getServicesManager();
					ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();

					String ThirdpartyFlag = paramHelper.getServerProperty("THIRDPARTY_AUTH_ENABLE");
					String username = inputParams.get("UserName");
					if (getRestThirdPartyStatus(dcRequest, username) == "false"){
						/* getRestThirdPartyStatus method using to retrieve THIRDPARTY AUTH reset flag
						 * Which is reset by ADMIN from spotlight. If , this flag is true then we won't allow
						 * user to login into the application and will display a message like , 'please reset your third party AUTH'
						 */
						diagnostic.prepareDebug("ThirdpartyFlag ******:" + ThirdpartyFlag);
						if (ThirdpartyFlag.equalsIgnoreCase("true")) {
							String totp = inputParams.get("totp");
							String userid = inputParams.get("userid");
							diagnostic.prepareDebug("totp *****:" + totp);
							diagnostic.prepareDebug("username ******:" + username);
							diagnostic.prepareDebug("userid ******:" + userid);

							UserAgentUtil ua = new UserAgentUtil(dcRequest);
							diagnostic.prepareDebug("Channel:********" + ua.getChannel());

							if (ua.getChannel().equalsIgnoreCase("desktop")) {
								if (validateTOTP(dcRequest, userid, totp) == "true") {
									retVal.addParam(new Param("totpValidation", "Success", "String"));
								} else {
									// retVal.addParam(new Param("totpValidation", "Failed", "String"));
									// ErrorCodeEnum.ERR_87421.setErrorCode(retVal);

									Record record = new Record();
									record.setId(DBPUtilitiesConstants.USR_ATTR);
									retVal.addRecord(record);
									ErrorCodeEnum.ERR_87421.setErrorCode(retVal);

									return retVal;
								}
							}
						}
					} else {
						UserAgentUtil ua = new UserAgentUtil(dcRequest);
						if (ua.getChannel().equalsIgnoreCase("desktop")) {
							diagnostic.prepareDebug("In Else of getRestThirdPartyStatus **");
							Record record = new Record();
							record.setId(DBPUtilitiesConstants.USR_ATTR);
							retVal.addRecord(record);
							ErrorCodeEnum.ERR_87422.setErrorCode(retVal);

							return retVal;
						}
					}
					retVal = sessionAttributes(dcRequest, result, inputParams, methodID, dcResponse, inputArray);
					Record record = retVal.getRecordById(DBPUtilitiesConstants.USR_ATTR);
					record.addParam(new Param("isEAgreementRequired", "" + eSignAgreementRequired, "String"));
					record.addParam(new Param("isEagreementSigned", "" + isEagreementSigned, "String"));
					if (isProspect && isPasswordExpired) {
						record.addParam(new Param("isProspectExpired", "true", "String"));
						retVal.addParam(new Param("isProspectExpired", "true", "String"));
					}

                    updateUserDetails(dcRequest, result, retVal);
                } else if (checkIsCoreIdentityScope(result)) {
                    ErrorCodeEnum.ERR_10094.setErrorCode(retVal);
                } else {
                    Record record = new Record();
                    record.setId(DBPUtilitiesConstants.USR_ATTR);
                    retVal.addRecord(record);
                    
                    String lockcount = HelperMethods.getFieldValue(user, "lockCount");
                    int thresholdCount = pm.getAccountLockoutThreshold();
                    diagnostic.prepareDebug("thresholdCount *****:" + thresholdCount+" ");
                    int invalidAttCount = Integer.parseInt(lockcount);
                    diagnostic.prepareDebug("invalidAttCount *****:" + invalidAttCount + " ");
                    int remainingAttempt = (thresholdCount - invalidAttCount);
                    diagnostic.prepareDebug("remainingAttempt *****:" + remainingAttempt + " ");
                    
                    ErrorCodeEnum.ERR_10095.setErrorCode(retVal, "Incorrect username or password. "+ (remainingAttempt - 1)+" attempts left.");
                    updateLockCount(dcRequest, user, lockcount);
                    if (isUserLockedAfterUpdateCount(lockcount, pm)) {
                        int x = pm.getAutoUnLockPeriod();
                        ErrorCodeEnum.ERR_10088.setErrorCode(retVal,
                                "Your account has been temporarily locked due to multiple unsuccessful Sign in attempts. To unlock it, please contact our customer support.");
                    }

                }

                if (isProspect && isPasswordExpired) {
                    retVal.addParam(new Param("isProspectExpired", "true", "String"));
                }
                retVal.addParam(new Param("isEAgreementRequired", "" + eSignAgreementRequired, "String"));
                retVal.addParam(new Param("isEagreementSigned", "" + isEagreementSigned, "String"));
                Param p = new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "200", "int");
                retVal.addParam(p);
            }
        } else {
            Record record = new Record();
            record.setId(DBPUtilitiesConstants.USR_ATTR);
            retVal.addRecord(record);
            retVal.addParam(new Param("isDBXUser", "false"));
            ErrorCodeEnum.ERR_10095.setErrorCode(retVal);
        }

        diagnostic.prepareDebug("Response from postProcessForUserName : " + ResultToJSON.convert(retVal)).log();
        return retVal;
    }

    private boolean isUserLockedAfterUpdateCount(String lockcount, PasswordHistoryManagement pm)
            throws HttpCallException {

        boolean isUserLockedAfterUpdateCount = false;
        if (StringUtils.isNotBlank(lockcount)) {
            int count = Integer.parseInt(lockcount);
            isUserLockedAfterUpdateCount = count >= pm.getAccountLockoutThreshold();
        } else
            isUserLockedAfterUpdateCount = false;

        diagnostic.prepareDebug("Response from isUserLockedAfterUpdateCount : " + isUserLockedAfterUpdateCount).log();

        return isUserLockedAfterUpdateCount;

    }

    private boolean isPasswordExpired(DataControllerRequest dcRequest, Record record, PasswordHistoryManagement pm,
            boolean isProspect) throws HttpCallException {
        String customerId = record.getParam("id").getValue();
        return pm.isPasswordExpired(dcRequest, customerId, isProspect);
    }

    private boolean hasPassword(Record user) {
        diagnostic.prepareDebug(
                "Response from hasPassword : " + StringUtils.isNotBlank(HelperMethods.getFieldValue(user, "Password"))).log();
        return StringUtils.isNotBlank(HelperMethods.getFieldValue(user, "Password"));
    }

    private boolean esignStatus(Record user) {
        return "true".equalsIgnoreCase(HelperMethods.getFieldValue(user, "isEagreementSigned"));
    }

    private boolean isUserActive(Record user, String mfaName) throws HttpCallException {

		boolean isUserActive = false;
		String status_id = HelperMethods.getFieldValue(user, "Status_id");
		if (StringUtils.isNotEmpty(mfaName) && mfaName.equalsIgnoreCase("Origination")) {
			if (status_id.equalsIgnoreCase("SID_CUS_ERASURE_INPROGRESS")
					|| status_id.equalsIgnoreCase("SID_CUS_ERASURE_COMPLETED")) {
				isUserActive = false;
			} else {
				isUserActive = true;
			}
		} else {
			if (status_id.equalsIgnoreCase("SID_CUS_ACTIVE")) {
				isUserActive = true;
			} else {
				isUserActive = false;
			}
		}
        diagnostic.prepareDebug("Response from isUserActive : " + isUserActive).log();
        return isUserActive;
    }

    private boolean isUserSuspended(Record user) throws HttpCallException {

        boolean isUserSuspended = false;

        String status_id = HelperMethods.getFieldValue(user, "Status_id");
        if (status_id.equalsIgnoreCase(InfinityConstants.SID_CUS_SUSPENDED)) {
            isUserSuspended = true;
        } else {
            isUserSuspended = false;
        }

        diagnostic.prepareDebug("Response from isUserSuspended : " + isUserSuspended).log();

        return isUserSuspended;
    }

    private boolean isUserNew(Record user, String mfaName) throws HttpCallException {

        boolean isUserNew = false;

        String status_id = HelperMethods.getFieldValue(user, "Status_id");
        if (status_id.equalsIgnoreCase("SID_CUS_NEW")) {
            isUserNew = true;
        } else {
            isUserNew = false;
        }

        if ((StringUtils.isNotEmpty(mfaName)) && mfaName.equalsIgnoreCase("Origination"))// Skip check for Onboarding app
            isUserNew = false;

        return isUserNew;
    }

    private boolean isUserEsignAgreementReq(Record user, DataControllerRequest dcRequest) throws HttpCallException {

        Map<String, String> input = new HashMap<>();
        input.put("_customerId", HelperMethods.getFieldValue(user, "id"));

        JsonObject response = HelperMethods.callApiJson(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_EAGREEMENT_GET);
        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("records")) {
            alert.prepareError("Exception occured while fetching the customer eagreement status").log();
            return false;
        }
        if (response.has("records") && response.get("records").getAsJsonArray().size() != 0
                && response.get("records").getAsJsonArray().get(0) != null && response.get("records").getAsJsonArray()
                        .get(0).getAsJsonObject().get("isEAgreementActive") != null) {
            return response.get("records").getAsJsonArray().get(0).getAsJsonObject().get("isEAgreementActive")
                    .getAsBoolean();
        }

        return false;
    }

    private Boolean checkIsCoreIdentityScope(Result result) {
        String isCoreIdentityScope = HelperMethods.getFieldValue(result, "IsCoreIdentityScope");

        if (("1".equals(isCoreIdentityScope) || "true".equalsIgnoreCase(isCoreIdentityScope))) {
            diagnostic.prepareDebug("Response from checkIsCoreIdentityScope : " + true).log();
            return true;
        }
        diagnostic.prepareDebug("Response from checkIsCoreIdentityScope : " + false).log();
        return false;
    }

    private Boolean validatePassword(DataControllerRequest dcRequest, Result result, String password)
            throws HttpCallException {
        String dbPassword = HelperMethods.getFieldValue(result, "Password");
        boolean isPasswordValid = false;
        try {
            isPasswordValid = BCrypt.checkpw(password, dbPassword);
        } catch (Exception e) {
        }
        diagnostic.prepareDebug("Response from isPasswordValid  : " + isPasswordValid).log();
        return isPasswordValid;
    }

    private Result postProcessForST(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
			String methodID, DataControllerResponse dcResponse, Object[] inputArray) throws HttpCallException {
		Result retVal = new Result();
		if (HelperMethods.hasRecords(result)) {
			dcRequest.addRequestParam_("isCSRAssistMode", "true");
			retVal = sessionAttributes(dcRequest, result, inputParams, methodID, dcResponse, inputArray);
		} else {
			ErrorCodeEnum.ERR_10086.setErrorCode(retVal);
		}
		diagnostic.prepareDebug("Response from postProcessForST : " + ResultToJSON.convert(retVal)).log();
		return retVal;
	}

    private Result postProcessForOnboardingST(Map<String, String> inputParams, DataControllerRequest dcRequest,
            Result result, String methodID, DataControllerResponse dcResponse, Object[] inputArray)
            throws HttpCallException {
        Result retVal = new Result();
        if (HelperMethods.hasRecords(result)) {
            Record user = result.getAllDatasets().get(0).getRecord(0);
            String cusSessionId = HelperMethods.getFieldValue(user, "Session_id");
            String inpSessionId = inputParams.get("session_token");
            if (!StringUtils.equals(inpSessionId, cusSessionId)) {
                ErrorCodeEnum.ERR_10086.setErrorCode(retVal);
            } else {
                retVal = sessionAttributes(dcRequest, result, inputParams, methodID, dcResponse, inputArray);
            }
        } else {
            ErrorCodeEnum.ERR_10086.setErrorCode(retVal);
        }
        diagnostic.prepareDebug("Response from postProcessForOnboardingST : " + ResultToJSON.convert(retVal)).log();
        return retVal;
    }

    private Result postProcessForPin(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> inputParams2, String methodID, DataControllerResponse dcResponse, Object[] inputArray)
            throws MetricsException, ParseException, HttpCallException {
        Result retVal = new Result();
        if (HelperMethods.hasRecords(result)) {
            String deviceId = HelperMethods.getDeviceId(dcRequest);
            if (!isDeviceRegistered(dcRequest, deviceId, HelperMethods.getFieldValue(result, "id"))) {
                ErrorCodeEnum.ERR_10117.setErrorCode(retVal);
            } else {

                String inputPin = inputParams.get("pin");
                String dbPing = HelperMethods.getFieldValue(result, "Pin");

                if (StringUtils.isNotBlank(dbPing) && StringUtils.isNotBlank(inputPin) && dbPing.equals(inputPin)) {
                    retVal = sessionAttributes(dcRequest, result, inputParams, methodID, dcResponse, inputArray);
                    updateUserDetails(dcRequest, result, retVal);
                } else {
                    ErrorCodeEnum.ERR_10118.setErrorCode(retVal);
                }
            }
        } else {
            ErrorCodeEnum.ERR_10119.setErrorCode(retVal);
            retVal.addParam(new Param("isDBXUser", "false"));
        }

        diagnostic.prepareDebug("Response from postProcessForPin : " + ResultToJSON.convert(retVal)).log();
        return retVal;
    }

    public static boolean isDeviceRegistered(DataControllerRequest dcRequest, String deviceId, String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(userId).append(DBPUtilitiesConstants.AND)
                .append("Device_id").append(DBPUtilitiesConstants.EQUAL).append(deviceId)
                .append(DBPUtilitiesConstants.AND).append("Status_id").append(DBPUtilitiesConstants.EQUAL)
                .append("SID_DEVICE_REGISTERED");
        Result device = new Result();
        try {
            device = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
                    URLConstants.DEVICEREGISTRATION_GET);
        } catch (HttpCallException e) {

            alert.prepareError("Caught exception while getting device registration: ", e).log();
        }

        diagnostic.prepareDebug("Response from isDeviceRegistered  : " + HelperMethods.hasRecords(device)).log();
        return HelperMethods.hasRecords(device);
    }

    private boolean isUserExpired(Record user) {

        String isEnrolled = HelperMethods.getFieldValue(user, "isEnrolled");
        String validDate = HelperMethods.getFieldValue(user, "ValidDate");
        if (("1".equals(isEnrolled) || "true".equalsIgnoreCase(isEnrolled))
                && new Date().after(HelperMethods.getFormattedTimeStamp(validDate))) {
            diagnostic.prepareDebug("Response from isUserExpired : " + true).log();
            return true;
        } else {
            diagnostic.prepareDebug("Response from isUserExpired : " + false).log();
            return false;
        }

    }

    private static void updateUserDetails(DataControllerRequest dcRequest, Result result, Result retVal) {
        Map<String, String> input = new HashMap<>();
        input.put("Lastlogintime", HelperMethods.getCurrentTimeStamp());
        if (StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "CurrentLoginTime"))) {
            try {
                input.put("Lastlogintime", HelperMethods.convertDateFormat(
                        HelperMethods.getFieldValue(result, "CurrentLoginTime"), "yyyy-MM-dd'T'HH:mm:ss"));
            } catch (ParseException e) {
                alert.prepareError("Caught exception while Converting date :", e).log();
            }
        }
        input.put("id", retVal.getRecordById(DBPUtilitiesConstants.USR_ATTR).getParam("customer_id").getValue());
        input.put("lockCount", "0");
        input.put("CurrentLoginTime", HelperMethods.getCurrentTimeStamp());
        input.put("Session_id", retVal.getRecordById("security_attributes").getParam("session_token").getValue());
        HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_UPDATE);
    }

    private static void updateLockCount(Map<String, String> input, DataControllerRequest dcRequest) {
        HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_UPDATE);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void updateLockCount(DataControllerRequest dcRequest, Record user, String lockcount)
            throws HttpCallException {
        String id = HelperMethods.getFieldValue(user, "id");
        int count = 1;

        if (StringUtils.isNotBlank(lockcount)) {
            count = Integer.parseInt(lockcount) + 1;
        }
        Map input = new HashMap();
        input.put("id", id);
        input.put("lockCount", String.valueOf(count));
        input.put("lockedOn", HelperMethods.getCurrentTimeStamp());
        updateLockCount(input, dcRequest);
    }

    private boolean isUserLocked(Record user, DataControllerRequest dcRequest, PasswordHistoryManagement pm)
            throws HttpCallException {

        boolean isUserLocked = false;
        String lockcount = HelperMethods.getFieldValue(user, "lockCount");
        if (StringUtils.isNotBlank(lockcount)) {
            int count = Integer.parseInt(lockcount);
            isUserLocked = (count + 1) >= pm.getAccountLockoutThreshold();
        }

        diagnostic.prepareDebug("Response from isUserLocked : " + isUserLocked).log();

        return isUserLocked;
    }

    private boolean tryAutoLockReset(Record user, DataControllerRequest dcRequest, PasswordHistoryManagement pm)
            throws HttpCallException {

        String lockedOn = HelperMethods.getFieldValue(user, "lockedOn");
        int autoUnlockPeriod = pm.getAutoUnLockPeriod();
        Date today = HelperMethods.getFormattedTimeStamp(HelperMethods.getCurrentTimeStamp());
        Calendar cal = Calendar.getInstance();
        cal.setTime(HelperMethods.getFormattedTimeStamp(lockedOn));
        cal.add(Calendar.MINUTE, autoUnlockPeriod);
        if (-1 != autoUnlockPeriod && today.after(cal.getTime())) {
            diagnostic.prepareDebug("Response from tryAutoLockReset : " + true).log();
            return true;
        }
        diagnostic.prepareDebug("Response from tryAutoLockReset : " + false).log();
        return false;

    }

    private Result sessionAttributes(DataControllerRequest dcRequest, Result result, Map<String, String> inputParams,
            String methodID, DataControllerResponse dcResponse, Object[] inputArray) {
        Result retVal = result;
        Dataset ds = result.getDatasetById("customer");
        Record sessionAttr = new Record();
        sessionAttr.setId("security_attributes");

        Record usrAttr = ds.getRecord(0);

        usrAttr.setId(DBPUtilitiesConstants.USR_ATTR);
        usrAttr.addParam(new Param("customer_id", HelperMethods.getFieldValue(result, "id"), "String"));
        usrAttr.addParam(new Param("UserName", HelperMethods.getFieldValue(result, "UserName"), "String"));
        String FirstName=usrAttr.getParamValueByName("FirstName");
        if(StringUtils.isBlank(FirstName)) {
        	 usrAttr.addParam("FirstName", usrAttr.getParamValueByName("FullName"));
        }
        //usrAttr.addParam(new Param("customer_id", HelperMethods.getFieldValue(result, "id"), "String"));
       
        String token = SessionManager.createSession(usrAttr.getParam("customer_id").getValue());

        Result cusComm = new Result();

        inputParams.put("id", HelperMethods.getFieldValue(result, "id"));

        if (StringUtils.isNotBlank(getDeviceId(dcRequest))) {
            usrAttr.addParam(new Param(DBPUtilitiesConstants.IS_DEVICE_REGISTERED,
                    isDeviceRegistered(dcRequest, getDeviceId(dcRequest), inputParams.get("id")) + ""));
        }

        cusComm = (Result) new GetCustomerPreferencesConcurrent().invoke(methodID, inputArray, dcRequest, dcResponse);

        diagnostic.prepareDebug("Response from CustomerPreferencesConcurrent : " + ResultToJSON.convert(cusComm));
        for (Param param : cusComm.getAllParams()) {
            usrAttr.addParam(param);
        }

        usrAttr.addParam("userFirstName", usrAttr.getParamValueByName("FirstName"));
        usrAttr.addParam("userLastName", usrAttr.getParamValueByName("LastName"));
        usrAttr.addParam("gender", usrAttr.getParamValueByName("Gender"));
        usrAttr.addParam("isPinSet", usrAttr.getParamValueByName("IsPinSet"));
        usrAttr.addParam("noofdependents", usrAttr.getParamValueByName("NoOfDependents"));
        usrAttr.addParam("spousefirstname", usrAttr.getParamValueByName("SpouseName"));
        usrAttr.addParam("userImage", usrAttr.getParamValueByName("UserImage"));
        usrAttr.addParam("ssn", usrAttr.getParamValueByName("Ssn"));
        usrAttr.addParam("taxid", usrAttr.getParamValueByName("Ssn"));
        usrAttr.addParam("maritalstatus", usrAttr.getParamValueByName("MaritalStatus_id"));
        usrAttr.addParam("lastlogintime", usrAttr.getParamValueByName("Lastlogintime"));
        usrAttr.addParam("isCombinedUser", usrAttr.getParamValueByName("isCombinedUser"));
        usrAttr.addParam("organizationType", usrAttr.getParamValueByName("organizationType"));
        usrAttr.addParam(new Param("CSR_User_Id",
                StringUtils.isNotBlank(dcRequest.getAttribute("CSR_User_Id")) ? dcRequest.getAttribute("CSR_User_Id")
                        : "",
                "String"));
		usrAttr.addParam(new Param("DefaultLanguage",StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "DefaultLanguage"))
                        ? HelperMethods.getFieldValue(result, "DefaultLanguage")
                        : "",
                "String"));
        usrAttr.addParam(new Param("CSR_Role",
                StringUtils.isNotBlank(dcRequest.getAttribute("CSR_Role")) ? dcRequest.getAttribute("CSR_Role") : "",
                "String"));
        usrAttr.addParam(new Param("CSR_Name",
                StringUtils.isNotBlank(dcRequest.getAttribute("CSR_Name")) ? dcRequest.getAttribute("CSR_Name") : "",
                "String"));
        usrAttr.addParam(new Param("CSR_Username",
                StringUtils.isNotBlank(dcRequest.getAttribute("CSR_Username")) ? dcRequest.getAttribute("CSR_Username")
                        : "",
                "String"));
        usrAttr.addParam(new Param("user_type",
                StringUtils.isNotBlank(dcRequest.getAttribute("CSRAssist_User_Type"))
                        ? dcRequest.getAttribute("CSRAssist_User_Type")
                        : "",
                "String"));
        usrAttr.addParam(new Param("CustomerUsername",
                StringUtils.isNotBlank(dcRequest.getAttribute("CSRAssist_Customer_username"))
                        ? dcRequest.getAttribute("CSRAssist_Customer_username")
                        : "",
                "String"));
        usrAttr.addParam(new Param("CustomerId",
                StringUtils.isNotBlank(dcRequest.getAttribute("CSRAssist_Customer_id"))
                        ? dcRequest.getAttribute("CSRAssist_Customer_id")
                        : "",
                "String"));
        usrAttr.addParam(new Param("accountId",
                StringUtils.isNotBlank(dcRequest.getAttribute("accountId"))
                        ? dcRequest.getAttribute("accountId")
                        : "",
                "String"));
        usrAttr.addParam(new Param("customerTypeId",
                StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "CustomerType_id"))
                        ? HelperMethods.getFieldValue(result, "CustomerType_id")
                        : "",
                "String"));

        sessionAttr.addParam(new Param("permissions",
                StringUtils.isNotBlank(dcRequest.getAttribute("permissions")) ? dcRequest.getAttribute("permissions")
                        : "",
                "String"));

        sessionAttr.addParam(new Param("features",
                StringUtils.isNotBlank(dcRequest.getAttribute("features")) ? dcRequest.getAttribute("features") : "",
                "String"));

        String isCSRAssistMode = new String();
        if (StringUtils.isNotBlank(dcRequest.getParameter("isCSRAssistMode"))) {
            isCSRAssistMode = dcRequest.getParameter("isCSRAssistMode");
        }

        if (StringUtils.isBlank(isCSRAssistMode)) {
            usrAttr.addParam(new Param("isCSRAssistMode", "false", "String"));
        } else {
            usrAttr.addParam(new Param("isCSRAssistMode", "true", "String"));
        }
        
        try {
            usrAttr.addParam(new Param("Lastlogintime", HelperMethods.convertDateFormat(
                    HelperMethods.getFieldValue(result, "CurrentLoginTime"), "yyyy-MM-dd'T'HH:mm:ss"), "String"));
        } catch (ParseException e) {

            alert.prepareError("Caught exception while converting DateFormat: ", e).log();
        }


        String filterQuery = "Customer_id" + DBPUtilitiesConstants.EQUAL
                + HelperMethods.getFieldValue(usrAttr, "customer_id");
        Result backendIdentifiers = new Result();
        try {
            backendIdentifiers = HelperMethods.callGetApi(dcRequest, filterQuery,
                    HelperMethods.getHeaders(dcRequest), URLConstants.BACKENDIDENTIFIER_GET);
        } catch (HttpCallException e) {

            alert.prepareError("Caught exception while getting Backend Identifiers: ", e).log();
        }

        Map<String, String> backend_identifiers = new HashMap<String, String>();
        if (HelperMethods.hasRecords(backendIdentifiers)) {
            backend_identifiers = getBackendIdentifiers(backendIdentifiers);
            usrAttr.addParam(new Param("backendIdentifiers", backend_identifiers.get("backendIdentifier"), "String"));
            if (StringUtils.isNotEmpty(backend_identifiers.get("companyId")))
                usrAttr.addParam(new Param("companyId", backend_identifiers.get("companyId"), "String"));
        }
        if (StringUtils.isBlank(usrAttr.getParamValueByName("companyId"))) {
            usrAttr.addParam(new Param("companyId",
                    EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE)));
        }
        
        Param sessionId = new Param("session_token", token, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        Param sessionTtl = new Param("session_ttl", null, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        Param permissionsEndpoint = new Param("permissionsEndpoint",
        		"authProductServices/GetCustomerFeaturesAndPermissions", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        sessionAttr.addParam(sessionId);
        sessionAttr.addParam(sessionTtl);
        sessionAttr.addParam(permissionsEndpoint);
        retVal.addRecord(usrAttr);
        retVal.addRecord(sessionAttr);
        retVal.removeRecordById("customer");
        diagnostic.prepareDebug("Response from sessionAttributes : " + ResultToJSON.convert(retVal)).log();
        return retVal;
    }

    private Map<String, String> getBackendIdentifiers(Result backendIdentifiers) {

        Map<String, String> backend_identifiers = new HashMap<String, String>();
        String returnCompanyId = "";
        List<Record> identifiers = backendIdentifiers.getAllDatasets().get(0).getAllRecords();

        JSONObject json = new JSONObject();

        for (Record record : identifiers) {

            String backendType = HelperMethods.getFieldValue(record, "BackendType");

            Map<String, String> map = new HashMap<>();
            map.put("sequence_number", String.valueOf(HelperMethods.getFieldValue(record, "sequenceNumber")));
            map.put("BackendId", HelperMethods.getFieldValue(record, "BackendId"));
            map.put("identifier_name", HelperMethods.getFieldValue(record, "identifier_name"));
            String companyId = HelperMethods.getFieldValue(record, "CompanyId");
            map.put("CompanyId", companyId);
            map.put("contractId", HelperMethods.getFieldValue(record, "contractId"));
            map.put("contractTypeId", HelperMethods.getFieldValue(record, "contractTypeId"));
            if (StringUtils.isNotEmpty(companyId) && StringUtils.isEmpty(returnCompanyId))
                returnCompanyId = companyId;

            if (json.has(backendType)) {

                JSONArray value = json.getJSONArray(backendType);
                value.put(map);
            } else {

                JSONArray value = new JSONArray();
                value.put(map);
                json.put(backendType, value);
            }
        }
        backend_identifiers.put("backendIdentifier", String.valueOf(json));
        backend_identifiers.put("companyId", returnCompanyId);
        return backend_identifiers;
    }

    private String getDeviceId(DataControllerRequest dcRequest) {

        String reportingParams = dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
        try {
            reportingParams = URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            alert.prepareError("Caught exception while Decoding Reporting Params : ", e);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(reportingParams);

            if (reportingParams.contains("did")) {
                return jsonObject.getString("did");
            }
        } catch (JSONException e) {
            alert.prepareError("Caught exception while Getting DeviceId from reporting Params: ", e);
        }

        return "";
    }

    private String getMfaName(DataControllerRequest dcRequest) {
        String reportingParams = dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
        try {
            reportingParams = URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            alert.prepareError("Caught exception while Decoding Reporting Params : ", e).log();
        }
        try {
            JSONObject jsonObject = new JSONObject(reportingParams);
            if (reportingParams.contains("mfaname")) {
                return jsonObject.getString("mfaname");
            }
        } catch (JSONException e) {
            alert.prepareError("Caught exception while Getting mfaname from reporting Params: ", e).log();
        }
        return "";
    }
	
	private String validateTOTP(DataControllerRequest dcRequest,String userName,String totp) throws Exception {
    	
    	diagnostic.prepareDebug("validateTOTP **********");
    	HashMap<String, Object> params = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		params.put("userName", userName);
		params.put("totp", totp);
    	
    	String serviceName = "GoogleAuth";
    	String operationName = "GoogleTokenValidation";
    	
    	Result result = CommonUtils.callIntegrationService(dcRequest, params, serviceHeaders, serviceName, operationName,
    							false);
    	diagnostic.prepareDebug("validateTOTP Result:"+ result);
    	String validationStatus = result.getParamValueByName("is2FAEnrollSuccess");
		return validationStatus;
    }
 
	private String getRestThirdPartyStatus(DataControllerRequest dcRequest, String userName)
			throws Exception {
		String resetThirdpartyStatus = "";
		diagnostic.prepareDebug("getRestThirdPartyStatus **********");
		HashMap<String, Object> params = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		params.put("userName", userName);

		String serviceName = "GoogleAuth";
		String operationName = "getResetThirdPartyStatus";

		Result result = CommonUtils.callIntegrationService(dcRequest, params, serviceHeaders, serviceName,
				operationName, false);
		diagnostic.prepareDebug("getRestThirdPartyStatus Result:" + result);
		resetThirdpartyStatus = result.getParamValueByName("resetThirdpartyFlag");
		diagnostic.prepareDebug("resetThirdpartyStatus value:" + resetThirdpartyStatus);
		return resetThirdpartyStatus;
	}
    
	private void saveClientIpToCache(Result result, String clientIp, DataControllerRequest request) {
		Record secAttr = result.getRecordById(DBPUtilitiesConstants.SECURITY_ATTRIBUTES);
		if (StringUtils.isBlank(clientIp) || secAttr == null || !secAttr.hasParamByName("session_token")) {
			return;
		}
		String sessionId = secAttr.getParamValueByName("session_token");
		String key = sessionId + "_CLIENT_IP";
		MemoryManager.saveIntoCache(key, clientIp, EXPIRY_TIME);
	}
	private String getCoreCustomerIdFromCustomerId(String customerId, DataControllerRequest request)
			throws ApplicationException {
		String backendId = "";
		BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
		String legalEntityId = EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		backendIdentifierDTO.setCustomer_id(customerId);
		backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
		backendIdentifierDTO.setBackendType("T24");
		if (StringUtils.isNotBlank(backendIdentifierDTO.getCustomer_id())) {
			BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
			DBXResult backendIdentifiers = backendIdentifierBusinessDelegate.get(backendIdentifierDTO,
					request.getHeaderMap());
			if (backendIdentifiers != null && backendIdentifiers.getResponse() != null) {
				backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifiers.getResponse();
				backendId = backendIdentifierDTO.getBackendId();
			}
		}
		return backendId;
	}
	public JsonObject searchCoreCustomers(MembershipDTO membershipDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		DBXResult response = new DBXResult();
		JsonObject coreCustomerJson = new JsonObject();
		try {
			CoreCustomerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(CoreCustomerBackendDelegate.class);
			DBXResult coreResult = backendDelegate.searchCoreCustomers(membershipDTO, headersMap);
			JsonArray coreCustomerRecords = new JsonArray();
			if (coreResult != null && coreResult.getResponse() != null) {
				coreCustomerRecords = (JsonArray) coreResult.getResponse();
			}
			diagnostic.prepareDebug("hbl::CustomerLogin:searchCoreCustomers coreCustomerRecords:"
					+ coreCustomerRecords);
			if (coreCustomerRecords.size() == 0) {
				throw new ApplicationException(ErrorCodeEnum.ERR_30010);
			}
			coreCustomerJson = coreCustomerRecords.get(0).getAsJsonObject();
		} catch (ApplicationException e) {
			alert.prepareError("CustomerLogin : Exception occured while searching core customers"
					+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("CustomerLogin : Exception occured while searching core customers "
					+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10757);
		}
		return coreCustomerJson;
	}
	public JSONObject verifyChannelAccess(JsonObject coreCustomerJson, DataControllerRequest dcRequest) throws Exception {
		String channelAccess = JSONUtil.getString(coreCustomerJson, "channelAccess");
		UserAgentUtil ua = new UserAgentUtil(dcRequest);
		String channel=ua.getChannel();
		diagnostic.prepareDebug("hbl::CustomerLogin:getChannelAccess channelAccess:"+channelAccess);
		diagnostic.prepareDebug("hbl::CustomerLogin:getChannelAccess channel name:"+channel);
		String errorMsg="";
		JSONObject enorllObject = new JSONObject();
		if(channelAccess.equalsIgnoreCase("ONLINE_BANKING_ONLY")) {
			if(channel.equalsIgnoreCase("desktop")) {
				enorllObject.put("isUserHasChannelAccess", "Yes");
				enorllObject.put("channelAccess", channelAccess);
			}else if(channel.equalsIgnoreCase("mobile")){
				errorMsg="Sorry! Your Mobile banking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
				enorllObject.put("isUserHasChannelAccess", "No");
				enorllObject.put("dbpErrMsg", errorMsg);
				enorllObject.put("dbpErrCode", "30001");	
			}
			else {
				errorMsg="Sorry! Your ebanking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
				enorllObject.put("isUserHasChannelAccess", "No");
				enorllObject.put("dbpErrMsg", errorMsg);
				enorllObject.put("dbpErrCode", "30003");
			}
		}
		else if(channelAccess.equalsIgnoreCase("MOBILE_BANKING_ONLY")) {
			if(channel.equalsIgnoreCase("mobile")) {
				enorllObject.put("isUserHasChannelAccess", "Yes");
				enorllObject.put("channelAccess", channelAccess);
			}else if(channel.equalsIgnoreCase("desktop")){
				errorMsg="Sorry! Your Online banking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
				enorllObject.put("isUserHasChannelAccess", "No");
				enorllObject.put("dbpErrMsg", errorMsg);
				enorllObject.put("dbpErrCode", "30002");
			}
			else {
				errorMsg="Sorry! Your ebanking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
				enorllObject.put("isUserHasChannelAccess", "No");
				enorllObject.put("dbpErrMsg", errorMsg);
				enorllObject.put("dbpErrCode", "30003");
			}
		}
		else if(channelAccess.equalsIgnoreCase("BOTH")) {
			enorllObject.put("isUserHasChannelAccess", "Yes");
			enorllObject.put("channelAccess", channelAccess);
		}
		else {
			errorMsg="Sorry! Your ebanking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
			enorllObject.put("isUserHasChannelAccess", "No");
			enorllObject.put("dbpErrMsg", errorMsg);
			enorllObject.put("dbpErrCode", "30003");
		}
		return enorllObject;
	}
	

}