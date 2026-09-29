package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UserLoginPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    private static final String APP_KEY_HEADER = "DBP-X-Kony-App-Key";
    private static final String APP_SECRET_HEADER = "DBP-X-Kony-App-Secret";
    private static final String DBP_REPORTING_PARAMETERS_HEADER = "X-Kony-ReportingParams";
    private static final String CLAIMS_TOKEN_KEY = "claims_token";

    @SuppressWarnings("rawtypes")
    @Override
    public boolean execute(HashMap inputMap, DataControllerRequest dcRequest, DataControllerResponse dcResponse,
            Result result) throws Exception {
		Log4j2Configurator.getInstance();
        dcRequest.getSession().setAttribute("backendToken", getDBPServicesClaimsToken(dcRequest));
        return true;
    }

    public static String getDBPServicesClaimsToken(DataControllerRequest dataControllerRequest)
            throws DBPAuthenticationException {
        String[] loginKeys = authenticateToDBP(null, null, null, dataControllerRequest);
        return loginKeys[0];
    }

    private static String[] authenticateToDBP(String customerUsername, JSONObject adminConsoleUserDetails,
            JSONArray CSRAssistCompositePermissions, DataControllerRequest requestInstance)
            throws DBPAuthenticationException {
        String[] loginKeys = new String[2];
        try {

            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("username", EnvironmentConfiguration.USERNAME.getValue(requestInstance));
            inputMap.put("password", EnvironmentConfiguration.PASSWORD.getValue(requestInstance));
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put(APP_KEY_HEADER, EnvironmentConfiguration.AC_DBP_APP_KEY.getValue(requestInstance));
            headerMap.put(APP_SECRET_HEADER, EnvironmentConfiguration.AC_DBP_APP_SECRET.getValue(requestInstance));
            headerMap.put(DBP_REPORTING_PARAMETERS_HEADER,
                    EnvironmentConfiguration.AC_DBP_REPORTING_PARAMS.getValue(requestInstance));

            String dbpLoginResponse =
                    Executor.invokeService(ServiceURLEnum.KONY_DBP_USER_IDENTITYSERVICE, inputMap,
                            headerMap, requestInstance);
            JSONObject dbpLoginResponseJSON = CommonUtilities.getStringAsJSONObject(dbpLoginResponse);

            if (dbpLoginResponseJSON != null && dbpLoginResponseJSON.has(CLAIMS_TOKEN_KEY)
                    && StringUtils.isNotBlank(dbpLoginResponseJSON.optString(CLAIMS_TOKEN_KEY))) {
                diagnostic.prepareDebug("DBP Login Successful").log();
                loginKeys[0] = dbpLoginResponseJSON.optString(CLAIMS_TOKEN_KEY);
            } else {
                alert.prepareError("Failed to login into DBP. No Tokens found on response.").log();
                throw new DBPAuthenticationException(ErrorCodeEnum.ERR_20933);
            }

        } catch (DBPAuthenticationException authenticationException) {
            throw authenticationException;
        } catch (Exception exception) {
            alert.prepareError("Internal exception while logging to DBP", exception).log();
            throw new DBPAuthenticationException(ErrorCodeEnum.ERR_20933);
        }
        return loginKeys;
    }

}
