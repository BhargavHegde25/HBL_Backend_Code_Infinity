package com.kony.adminconsole.utilities;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.exception.CLAuthenticationException;
import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * Collection of operations being consumed from Consumer Lending
 * 
 * 
 */
public class ConsumerLendingServices {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    private static final String APP_KEY_HEADER = "CL-X-Kony-App-Key";
    private static final String APP_SECRET_HEADER = "CL-X-Kony-App-Secret";
    private static final String CL_API_ACCESS_TOKEN_HEADER = "X-Kony-CL-API-Access-Token";
    private static final String CLAIMS_TOKEN_KEY = "claims_token";

    private ConsumerLendingServices() {
        // Private Constructor
    }

    /**
     * Consumer Lending API User Login
     * 
     * @param dataControllerRequest
     * @return Session token
     * @throws CLAuthenticationException
     */
    public static String getDBPServicesClaimsToken(DataControllerRequest dataControllerRequest)
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
                    Executor.invokeService(ServiceURLEnum.CONSUMERLENDINGAUTHSERVICE_LOGIN, new HashMap<>(),
                            headerMap, requestInstance);
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
}
