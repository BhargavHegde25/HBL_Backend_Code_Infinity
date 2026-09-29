package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import org.slf4j.LoggerFactory;

import com.kony.adminconsole.service.mfa.MFAScenarioManageService;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ScaGenericAssessmentPreprocessor implements DataPreProcessor2 {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public boolean execute(@SuppressWarnings("rawtypes") HashMap hashMap, DataControllerRequest request,
			DataControllerResponse response, Result result) throws Exception {
		Log4j2Configurator.getInstance();

		ServicesManager servicesManager = request.getServicesManager();
		ConfigurableParametersHelper configParam = servicesManager.getConfigurableParametersHelper();
		Map<String, String> hm = configParam.getAllServerProperties();

		String RiskAssessment = hm.get("RISK_ASSESSMENT");
		String stepUp = "";
		String action_type = request.getParameter("action_type");
		String appId = request.getParameter("appId");
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("appId", appId);
		requestMap.put("actionId", action_type);
		// calling the below service from adminconsoleservices to fetch the configs from
		// spotlight
		Result scaAttr = new MFAScenarioManageService().getSCAMode(request);
		String isSCARequired = scaAttr.getParamValueByName("isSCARequired") == null ? "true"
				: scaAttr.getParamValueByName("isSCARequired");
		String riskScore = scaAttr.getParamValueByName("risk_score") == null ? "0"
				: scaAttr.getParamValueByName("risk_score");
		boolean isScaEnabled = Boolean.parseBoolean(isSCARequired);
		diagnostic.prepareDebug("SCAMFA Attributes from spotlight mfa: " + isSCARequired + " ,riskscore: " + riskScore).log();
		request.addRequestParam_("riskScore", riskScore);
		request.addRequestParam_("sca", isSCARequired);
		if (StringUtils.isNotEmpty(RiskAssessment) && RiskAssessment.equalsIgnoreCase("true")) {
			// risk evaluation flow -> hits the threatmark api's
			diagnostic.prepareDebug("RISK_ASSESSMENT evaluated to true").log();
			return true;
		} else {
			// Basic active/inactive flow
			stepUp = isScaEnabled == true ? "true" : "false";
			result.addParam("RISK_ASSESSMENT", RiskAssessment);
			result.addParam(new Param("stepUp", stepUp));
			result.addParam("riskscore_spotlight", riskScore);
			result.addParam("sca_spotlight", isSCARequired);
			diagnostic.prepareDebug("RISK_ASSESSMENT evaluated to false and stepUp value is " + stepUp).log();
			return false;
		}
	}
}
