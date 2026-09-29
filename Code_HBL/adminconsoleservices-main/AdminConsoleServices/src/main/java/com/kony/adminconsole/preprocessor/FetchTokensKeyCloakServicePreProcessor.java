package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * Preprocessor to append client id,client secret and grant type so that access token can be generated
 * 
 * @author Sri Kavya Pitchika
 *
 */
public class FetchTokensKeyCloakServicePreProcessor implements DataPreProcessor2 {

	public static final String CLIENT_CREDENTIALS = "client_credentials";
	public static final String GRANT_TYPE = "grant_type";
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_SECRET= "client_secret";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(@SuppressWarnings("rawtypes") HashMap inputMap, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance, Result serviceResult) throws Exception {
		Log4j2Configurator.getInstance();

        try {
        	inputMap.put(CLIENT_ID, EnvironmentConfiguration.KEYCLOAK_SERVICE_ACCOUNT_CLIENT_ID.getValue(requestInstance));
            inputMap.put(CLIENT_SECRET, EnvironmentConfiguration.KEYCLOAK_SERVICE_ACCOUNT_CLIENT_SECRET.getValue(requestInstance));
            inputMap.put(GRANT_TYPE, CLIENT_CREDENTIALS);			
            diagnostic.prepareDebug("Added clientid,clientsecret,granttype to input map. Returning true").log();
            return true;
        }catch (Exception e) {
            alert.prepareError("Unhandled Exception. Trace:", e).log();
        }
        return false;

    }
}
