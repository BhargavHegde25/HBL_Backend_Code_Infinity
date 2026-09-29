package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.exception.CLAuthenticationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * Preprocessor to fetch the Consumer Lending Auth Token. Adds the authentication token to the request scope
 * 
 * @author Aditya Mankal
 *
 */
public class ConsumerLendingServiceIntPreProcessor implements DataPreProcessor2 {

    private static final String APP_KEY_HEADER = "CL-X-Kony-App-Key";
    private static final String APP_SECRET_HEADER = "CL-X-Kony-App-Secret";
    private static final String CL_API_ACCESS_TOKEN_HEADER = "X-Kony-CL-API-Access-Token";
    private static final String CLAIMS_TOKEN_KEY = "claims_token";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(@SuppressWarnings("rawtypes") HashMap inputMap, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance, Result serviceResult) throws Exception {
		Log4j2Configurator.getInstance();

        try {
            String claimsToken = getConsumerLendingClaimsToken(requestInstance);
            inputMap.put("backendToken", claimsToken);
            diagnostic.prepareDebug("Added the authentication token to the input map. Returning true").log();
            return true;
        } catch (CLAuthenticationException authException) {
            alert.prepareError("CLAuthenticationException, Setting error code").log();
            authException.getErrorCodeEnum().setErrorCode(serviceResult);
        } catch (Exception e) {
            alert.prepareError("Unhandled Exception. Trace:", e).log();
        }
        return false;

    }

    /**
     * Consumer Lending API User Login
     * 
     * @param dataControllerRequest
     * @return Session token
     * @throws CLAuthenticationException
     */
    public static String getConsumerLendingClaimsToken(DataControllerRequest dataControllerRequest)
            throws CLAuthenticationException {
        String[] loginKeys = authenticateToConsumerLending(dataControllerRequest);
        return loginKeys[0];
    }

    private static String[] authenticateToConsumerLending(DataControllerRequest requestInstance)
            throws CLAuthenticationException {
        String[] loginKeys = new String[2];
        try {

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put(APP_KEY_HEADER, EnvironmentConfiguration.AC_CL_APP_KEY.getValue(requestInstance));
            headerMap.put(APP_SECRET_HEADER, EnvironmentConfiguration.AC_CL_APP_SECRET.getValue(requestInstance));
            headerMap.put(CL_API_ACCESS_TOKEN_HEADER,
                    EnvironmentConfiguration.AC_CL_SHARED_SECRET.getValue(requestInstance));

            String loginResponse =
                    Executor.invokeService(ServiceURLEnum.CONSUMERLENDINGAUTHSERVICE_LOGIN, new HashMap<>(), headerMap,
                            requestInstance);
            JSONObject loginResponseJSON = CommonUtilities.getStringAsJSONObject(loginResponse);

            if (loginResponseJSON != null && loginResponseJSON.has(CLAIMS_TOKEN_KEY)
                    && StringUtils.isNotBlank(loginResponseJSON.optString(CLAIMS_TOKEN_KEY))) {
                diagnostic.prepareDebug("Consumer Lending API User Login Successful").log();
                loginKeys[0] = loginResponseJSON.optString(CLAIMS_TOKEN_KEY);
            } else {
                alert.prepareError("Failed Consumer Lending API User Login. No Tokens found in response.Response:"
                        + loginResponse).log();
                throw new CLAuthenticationException(ErrorCodeEnum.ERR_21900);
            }

        } catch (CLAuthenticationException authenticationException) {
            throw authenticationException;
        } catch (Exception exception) {
            alert.prepareError("Internal exception while logging to DBP", exception).log();
            throw new CLAuthenticationException(ErrorCodeEnum.ERR_20001);
        }
        return loginKeys;
    }

    // public String getConsumerLendingAuthToken(DataControllerRequest requestInstance) {
    //
    // // ResultCache resultCache = null;
    // String authToken = null;
    //
    // // try {
    // // resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
    // // } catch (Exception e) {
    // // alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
    // // }
    // //
    // // // try to retrieve from cache
    // // if (resultCache != null) {
    // // authToken = (String) resultCache.retrieveFromCache(CACHE_KEY);
    // // }
    // //
    // // // if found in cache, return it
    // // if (StringUtils.isBlank(authToken)) {
    // // // if not found in cache, make a service call
    // //
    // // // caching the response of service call
    // // if (resultCache != null) {
    // // resultCache.insertIntoCache(CACHE_KEY, authToken, CACHE_IN_SECONDS);
    // // }
    // // }
    //
    // return authToken;
    //
    // }
}
