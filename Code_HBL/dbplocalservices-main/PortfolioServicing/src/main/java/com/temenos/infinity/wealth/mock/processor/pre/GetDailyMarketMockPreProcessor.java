package com.temenos.infinity.wealth.mock.processor.pre;


import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetDailyMarketMockPreProcessor implements DataPreProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetDailyMarketMockPreProcessor Mock - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			
			if (wealthCore != null && (wealthCore.equalsIgnoreCase("Mock"))) {

			/*	Object marketIndexObj = TemenosConstants.MARKETINDEX;
				String marketIndex = null;
				String inputValue = "";

				if (marketIndexObj != null && marketIndexObj.toString().trim().length() > 0) {
					marketIndex = (TemenosConstants.MARKETINDEX).toString();
					String marketsArr[] = marketIndex.toUpperCase().trim().split("\\s*,\\s*");
					for (String marketsVal : marketsArr) {
						String forQuotes = "\"";
						inputValue = inputValue.concat(forQuotes.concat(marketsVal.concat("\",")));
					}
					inputValue = inputValue.substring(0, inputValue.length() - 1);
					inputMap.put(TemenosConstants.MARKETS, inputValue);
					request.addRequestParam_(TemenosConstants.MARKETS,inputValue);
				}	 */
				String markVal = (TemenosConstants.MARKETINDEX).toString();
				inputMap.put(TemenosConstants.MARKETS, markVal);
				request.addRequestParam_(TemenosConstants.MARKETS,markVal);
				diagnostic.prepareDebug("==========> GetDailyMarketMockPreProcessor Mock - Core check done").log();
				return true;

				}
			 else {
					                result.addOpstatusParam("0");
					                result.addHttpStatusCodeParam("200");
					                result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					                diagnostic.prepareDebug("==========> GetDailyMarketMockPreProcessor Mock - Exiting for token generation").log();
					                return false;
					            }

					 

					        } catch (Exception e) {
					        	alert.prepareError("==========> GetDailyMarketMockPreProcessor Mock - Error: " + e.getMessage()).log();
					            e.getMessage();
					            return false;
					        }
}}