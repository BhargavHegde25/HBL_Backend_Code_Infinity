package com.temenos.infinity.wealth.mock.processor.post;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
/**
 * @author GAAYATHRI R
 *
 */

public class GetRiskAnalysisIPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");


	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetRiskAnalysisIPPostProcessor Mock - Entered ").log();
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			String INF_WLTH_STRATEGIES = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_STRATEGIES,
					request);

			if (portfolioId.equalsIgnoreCase("100777-4")) {

				JSONObject returnObj = new JSONObject();
				returnObj.put("riskAnalysis", assets(portfolioId));
				returnObj.put(TemenosConstants.PORTFOLIOID, portfolioId);
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);

			} else if (portfolioId.equalsIgnoreCase("100777-5")) {
				JSONObject returnObj = new JSONObject();
				returnObj.put("riskAnalysis", assets(portfolioId));
				returnObj.put(TemenosConstants.PORTFOLIOID, portfolioId);
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);
			}

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetRiskAnalysisIPPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> GetRiskAnalysisIPPostProcessor Mock - Exited ").log();
		return result;
	}

	private JSONArray assets(String portfolioId) {
		diagnostic.prepareDebug("==========> assets Mock - Entered ").log();
		String sectorNames[] = new String[] {"riskStatus", "riskPortMeasureP", "riskToleranceMinP","riskToleranceMaxP","riskObjectiveP"};
		String sectorCWeight[] = new String[] {"0","5.00",  "4.00", "7.00", "6.00" };
		
		if (portfolioId.equalsIgnoreCase("100777-4")) {
			 sectorNames = new String[] {"riskStatus", "riskPortMeasureP", "riskToleranceMinP","riskToleranceMaxP","riskObjectiveP"};
			 sectorCWeight = new String[] {"0","4.00", "4.00", "6.00", "6.00" };
			
		}
		else if (portfolioId.equalsIgnoreCase("100777-5")) {	
		}
		JSONArray al1 = new JSONArray();
		JSONObject assets_hm = new JSONObject();
		for (int i = 0; i < sectorNames.length; i++) {
			assets_hm.put(sectorNames[i], sectorCWeight[i]);
			
		}
		al1.put(assets_hm);
		diagnostic.prepareDebug("==========> assets Mock - Exited ").log();
		return al1;
	}}