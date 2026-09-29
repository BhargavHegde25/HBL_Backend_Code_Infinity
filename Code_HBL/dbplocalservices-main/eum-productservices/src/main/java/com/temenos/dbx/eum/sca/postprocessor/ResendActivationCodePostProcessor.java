package com.temenos.dbx.eum.sca.postprocessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang.StringUtils;

import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.sca.ErrorCodeEnum;
import com.temenos.dbx.eum.sca.SCAConstants;

/**
 * This Postprocessor resends or updates activation code of an already existing user with a configured SCA vendor.
 * This is a postprocessor to services/eumProductServices/SendActivationCodeForEnroll integration service under External User Management Microapp
 *
 * @author Karthik Bhuvanagiri
 */
public class ResendActivationCodePostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	LoggerUtil logger = new LoggerUtil(ResendActivationCodePostProcessor.class);

	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		boolean isSCAEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty(SCAConstants.ENV_IS_SCA_ENABLED));
		
		if(!isSCAEnabled)
			return result;
		
		String dbpErrMsg = result.getParamValueByName(ErrorCodeEnum.ERROR_MESSAGE_KEY);
		if(StringUtils.isNotBlank(dbpErrMsg)) {
			diagnostic.prepareDebug(ResultToJSON.convert(result)).log();
			return result;
		}
		
		String errMsg = result.getErrMsgParamValue();
		if(StringUtils.isNotBlank(errMsg)) {
			diagnostic.prepareDebug(ResultToJSON.convert(result)).log();
			return result;
		}
		
		Map<String, Object> requestParameters = buildRequestParameters(request, result);
		String userId = (String) requestParameters.get(SCAConstants.PAYLOAD_USER_ID);
		if(StringUtils.isBlank(userId)) {
			diagnostic.prepareDebug("Cannot Resend Activation Code to SCA Vendor as User Id is Empty").log();
			return ErrorCodeEnum.ERR_99503.setErrorCode(result);
		}
		
		String activationCode = (String) requestParameters.get(SCAConstants.PAYLOAD_ACTIVATION_CODE);
		if(StringUtils.isBlank(activationCode)) {
			diagnostic.prepareDebug("Cannot Resend Activation Code to SCA Vendor as Activation code is Empty").log();
			return ErrorCodeEnum.ERR_99504.setErrorCode(result);
		}
		
		DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
											.withServiceId(SCAConstants.INTEGRATION_SERVICE_SCA_SERVICE_ID)
											.withOperationId(SCAConstants.SCA_RESEND_ACTIVATION_CODE_OPERATION_ID)
											.withRequestParameters(requestParameters)
											.build();
		Result scaResult = serviceExecutor.getResult();
		diagnostic.prepareDebug(ResultToJSON.convert(scaResult)).log();
		String responseCode = scaResult.getParamValueByName("responseCode");
		if(StringUtils.isBlank(responseCode) || !responseCode.equals("0"))
			ErrorCodeEnum.ERR_99505.setErrorCode(result, scaResult.getParamValueByName("errorMessage"));
		return result;
	}
	
	private Map<String, Object> buildRequestParameters(DataControllerRequest request, Result result) throws IOException{
		String userId = request.getParameter("userId");
		String activationCode = request.getParameter("activationCode");
		
		diagnostic.prepareDebug("UserId: "+userId).log();
		diagnostic.prepareDebug("Activation Code: "+activationCode).log();
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(SCAConstants.PAYLOAD_USER_ID, userId);
		requestParameters.put(SCAConstants.PAYLOAD_ACTIVATION_CODE, activationCode);		
		return requestParameters;
	}

}
