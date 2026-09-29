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

import com.kony.adminconsole.commons.utils.AuthenticationC360;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.jwt.auth.AuthConstantsC360;
import com.kony.adminconsole.jwt.auth.utils.CommonUtilsC360;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class DueDiligenecePreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest dcRequest, DataControllerResponse dcResponse,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			String coreInfo = (String) inputMap.get(TemenosConstantsC360.CORE_IDENTIFIER);
			if (StringUtils.isNotBlank(coreInfo)) {
				String userID = CommonUtilsC360.getBackendId(coreInfo, TemenosConstantsC360.CONSTANT_TEMPLATE_NAME);
				if (StringUtils.isNotBlank(userID)) {
					inputMap.put(TemenosConstantsC360.USER_ID, userID);
				}
			}
			dcRequest.addRequestParam_(TemenosConstantsC360.FLOW_TYPE, TemenosConstantsC360.POST_LOGIN_FLOW);
			dcRequest.addRequestParam_(TemenosConstantsC360.ROLE_ID, AuthConstantsC360.PROP_ROLE_MARKETINGCATALOGUE);
			String authToken = AuthenticationC360.getAuthToken(dcRequest);
			if (StringUtils.isBlank(authToken)) {
				alert.prepareError("Error - JWT authToken generated for Due Diligence is empty").log();
				return Boolean.FALSE;
			}
			dcRequest.addRequestParam_(TemenosConstantsC360.PARAM_AUTHORIZATION, authToken);
			diagnostic.prepareInfo("Auth Token generated from DueDiligence PreProcessor" + authToken).log();
			
			if (StringUtils.isNotBlank(EnvironmentConfiguration.DUE_DILIGENCE_DEPLOYMENT_PLATFORM.getValue(dcRequest))) {
				if (StringUtils.equals(EnvironmentConfiguration.DUE_DILIGENCE_DEPLOYMENT_PLATFORM.getValue(dcRequest), ACConstants.AWS))
					dcRequest.addRequestParam_("x-api-key",
							EnvironmentConfiguration.DUE_DILIGENCE_AUTHORIZATION_KEY.getValue(dcRequest));
				if (StringUtils.equals(EnvironmentConfiguration.DUE_DILIGENCE_DEPLOYMENT_PLATFORM.getValue(dcRequest), ACConstants.AZURE))
					dcRequest.addRequestParam_("x-functions-key",
							EnvironmentConfiguration.DUE_DILIGENCE_AUTHORIZATION_KEY.getValue(dcRequest));
			}
			
			String customerID = "";
			if (inputMap.containsKey("id"))
				customerID = inputMap.get("id").toString();
			String partyID = getPartyID(customerID, result, dcRequest);
			if (StringUtils.isBlank(partyID)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_21873);
			}
			inputMap.put("partyID", partyID);
			return true;
		} catch (ApplicationException applicationException) {
			alert.prepareError("Error occured in Due Diligence preprocessor", applicationException).log();
			applicationException.getErrorCodeEnum().setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Error occured in Due Diligence preprocessor" + e).log();
		}
		return false;
	}

	private String getPartyID(String customerID, Result result, DataControllerRequest dcRequest) {
		String partyID = "";
		try {
			Map<String, String> postParametersMap = new HashMap<String, String>();
			postParametersMap.put(ODataQueryConstants.FILTER,
					"Customer_id eq '" + customerID + "' and BackendType eq PARTY");
			String readResponse = Executor.invokeService(ServiceURLEnum.BACKENDIDENTIFIER_READ, postParametersMap, null,
					dcRequest);
			JSONObject readResponseJSON = CommonUtilities.getStringAsJSONObject(readResponse);
			JSONArray readResponseJSONArray = readResponseJSON.getJSONArray("backendidentifier");
			if (readResponseJSONArray.getJSONObject(0).has("BackendId")) {
				partyID = readResponseJSONArray.getJSONObject(0).getString("BackendId");
			}
		} catch (Exception e) {
			alert.prepareError("Error in DueDiligenecePreProcessor-getPartyID Method : " + e.toString()).log();
		}
		return partyID;
	}

}
