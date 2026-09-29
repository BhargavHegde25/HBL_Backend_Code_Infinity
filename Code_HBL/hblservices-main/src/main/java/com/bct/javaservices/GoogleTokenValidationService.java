package com.bct.javaservices;

import java.util.HashMap;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.utilities.Utils;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.IGoogleAuthenticator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Base64;

import de.taimos.totp.TOTP;

public class GoogleTokenValidationService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GoogleTokenValidationService.class);
	private static final long TIME_STEP_SECONDS = 30;
	private static final int WINDOW = 2; // Allow 1 time step before and after
	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Result result = new Result();
		try {
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();

			/**
			 * SecretCode is the key configured under Server Properties tab in App Services
			 */
			// String SecretCode = paramHelper.getServerProperty("GOOGLEAUTH_SECRETCODE");
			String code = request.getParameter("totp");
			String UserName = request.getParameter("userName");
			String SecretCode = Utils.generateEncryptedKey(UserName);

			String message = null;
			Integer flagUpdate = null;
			LOG.debug("SecretCode:8***" + SecretCode);
			LOG.debug("code******" + code);
			LOG.debug("UserName*****" + UserName);

			try {

				// if (code.equals(Utils.getTOTPCode(SecretCode))) {
				if (validateOTP(SecretCode, Integer.parseInt(code))) {
					LOG.debug("Logged in successfully");
					message = "totp validation success!";
					String customerId = getCustomerIDFromUsername(request, UserName);
					/*
					 * Updating customer Table IsOlbAllowed Flag with 1 as this user enabled third
					 * party enrollment!
					 */
					flagUpdate = updateThirdpartyAuthCustomerTable(request, "1", UserName, customerId);
					result.setParam(new Param("is2FAEnrollSuccess", "true"));
				} else {
					LOG.debug("Invalid 2FA Code");
					message = "totp validation Failed!";
					result.setParam(new Param("is2FAEnrollSuccess", "false"));
				}

			} catch (Exception e) {
				// message = "totp validation Failed!";
				result.setParam(new Param("is2FAEnrollSuccess", "false"));
			}

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
			result.setParam(new Param("message", message));

		} catch (Exception e) {
			LOG.error("Exception occured in GoogleTokenValidationService:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public static String getTOTPCode(String secretKey) {
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(secretKey);
		String hexKey = Hex.encodeHexString(bytes);
		return TOTP.getOTP(hexKey);
	}
	
	/*public static boolean validateOTP(String secretKey, int otp) {
		// Allow ±1 time-step (30s before or after)
		GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
				.setWindowSize(3) // Accepts OTPs from previous, current, and next time intervals
				.build();
		IGoogleAuthenticator gAuth = new GoogleAuthenticator(config);
		return gAuth.authorize(secretKey, otp);
	}*/
	
	public static boolean validateOTP(String secret, int code) throws GeneralSecurityException {
        long currentTimeSeconds = Instant.now().getEpochSecond();
       // byte[] decodedKey = Base64.getDecoder().decode(secret);
        Base32 base32 = new Base32();
		byte[] decodedKey = base32.decode(secret);
		

        for (int i = -WINDOW; i <= WINDOW; ++i) {
            long timeStep = ((currentTimeSeconds - 60) / TIME_STEP_SECONDS) + i;
            int generatedCode = generateTOTP(decodedKey, timeStep);
            LOG.debug("code: ###"+ code);
            LOG.debug("generatedCode: ###"+ i+" "+generatedCode);
            if (generatedCode == code) {
                return true;
            }
        }
        return false;
    }

	
	private static int generateTOTP(byte[] key, long value) throws GeneralSecurityException {
		byte[] data = new byte[8];
	    //long timeStep = System.currentTimeMillis() / 1000 / 30;
		//long value = System.currentTimeMillis() / 1000 / 30;
		for (int i = 7; i >= 0; i--) {
			data[i] = (byte) (value & 0xFF);
			value >>= 8;
		}

		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);

		int offset = hash[hash.length - 1] & 0xF;
		int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16)
				| ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);

		return binary % 1000000; // 6 digit code
	}
	
	private int updateThirdpartyAuthCustomerTable(DataControllerRequest request, String thirdpartyAuthFlag,
			String UserName, String customerid) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		/*
		 * IsOlbAllowed Flag - 1 as true indicates thirdpartyAuth Enabled IsOlbAllowed
		 * Flag - 0 as false indicates thirdpartyAuth Disabled
		 */
		inputParams.put("IsOlbAllowed", thirdpartyAuthFlag);
		inputParams.put("UserName", UserName);
		inputParams.put("id", customerid);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customer_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updateThirdpartyAuthCustomerTable");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create entry in dbxDb accounts Table due to : " + errMessage);
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					updateResetThirdPartyStatus(request, thirdpartyAuthFlag, UserName, customerid);
					return 1;
				}
				else
					return 0;
			} catch (Exception e) {
				LOG.debug("Couldn't Parse updated records Integer from String");
				return 1;
			}
		}
		return 0;

	}
	
	private int updateResetThirdPartyStatus(DataControllerRequest request, String thirdpartyAuthFlag,
			String UserName, String customerid) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		
		/**
		 * IsStaffMember Flag - 1 indicates that third party feature reset by ADMIN
		 * Flag - 0/NULL indicates that ADMIN didn't reset third party AUTH feature
		 */
		
		
		inputParams.put("IsStaffMember", "0");
		inputParams.put("UserName", UserName);
		inputParams.put("id", customerid);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customer_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updateResetThirdPartyStatus");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create entry in dbxDb accounts Table due to : " + errMessage);
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0)
					return 1;
				else
					return 0;
			} catch (Exception e) {
				LOG.debug("Couldn't Parse updated records Integer from String");
				return 1;
			}
		}
		return 0;
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
			Dataset customerDataset =  result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				customerid = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				LOG.debug("Else getThirdpartyAuthFlag:");
			}
			LOG.debug("getThirdpartyAuthFlag id:" + customerid);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;

	}

}
