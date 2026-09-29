package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

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

public class GetStrategyAllocationPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			String INF_WLTH_STRATEGIES = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_STRATEGIES,
					request);

			if (portfolioId.equalsIgnoreCase("100777-4")) {

				JSONObject returnObj = new JSONObject();
				returnObj.put("strategyAlloc", assets(portfolioId));
				returnObj.put(TemenosConstants.PORTFOLIOID, portfolioId);
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);

			} else if (portfolioId.equalsIgnoreCase("100777-5")) {
				JSONObject returnObj = new JSONObject();
				returnObj.put("strategyAlloc", assets(portfolioId));
				returnObj.put(TemenosConstants.PORTFOLIOID, portfolioId);
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);
			}

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("Error while invoking GetMockMyStrategyPostProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return result;
	}

	private JSONArray assets(String portfolioId) {
		String sectorCWeight[] = new String[] {"25.00","25.00","25.00","25.00","15.00","5.00","5.00","20.00", "4.00",
				"1.00","25.00","1.50","2.50","1.50","2.75","1.25","1.50","2.00","1.00","1.00","5.00","5.00","10.00","5.00",
				"5.00","2.00","1.00","1.00","1.00","25.00"};
		String sectorNames[] = new String[] { "Stocks","Bonds","Funds","Cash","US","EU","Asia","US","EU","Asia","US",
				 "Aerospace & Defense","Auto & Truck Manufacturers","Beverages (Nonalcoholic)","Biotechnology & Drugs"
				 ,"Communications Equipment", "Computer Services", "Consumer Financial Services", "Regional Banks", 
				 "Retail" ,"Retail", "Regional Banks","Consumer Financial Services", "Regional Banks","Retail", "Aerospace & Defense",
				 "Auto & Truck Manufacturers", "Beverages (Nonalcoholic)", "Regional Banks", "Exchange Traded Funds"};
		String level[] = new String[] {"1","1","1","1","2","2","2","2","2","2","2","3","3","3","3","3","3","3","3","3","3",
				 "3","3","3","3","3","3","3","3","3" };
		String parentid[] = new String[] { "0","0","0","0","1","1","1","2","2","2","3","5","5","5","5","5","5","5","5","5",
				 "6","7","8","8","8","9","9","9","10","11"};
		String id[] = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19",
				 "20","21","22","23","24","25","26","27","28","29","30" };
		if (portfolioId.equalsIgnoreCase("100777-4")) {
			 sectorCWeight = new String[] {"40.00","30.00","20.00","10.00","20.00","10.00","10.00","15.00", "12.00", "3.00",
						"20.00","2.00","2.00","2.50","2.50","2.00","2.00","3.00","2.00","2.00","10.00","10.00","5.00","5.00"
						,"5.00","4.00" ,"4.00" ,"4.00","3.00" ,"20.00"};
			 sectorNames = new String[] { "Stocks","Bonds","Funds","Cash","US","EU","Asia","US","EU","Asia","US",
					 "Aerospace & Defense","Auto & Truck Manufacturers","Beverages (Nonalcoholic)","Biotechnology & Drugs"
					 ,"Communications Equipment", "Computer Services", "Consumer Financial Services", "Regional Banks", 
					 "Retail" ,"Retail", "Regional Banks","Consumer Financial Services", "Regional Banks","Retail", "Aerospace & Defense",
					 "Auto & Truck Manufacturers", "Beverages (Nonalcoholic)", "Regional Banks", "Exchange Traded Funds"};
			 level = new String[] {"1","1","1","1","2","2","2","2","2","2","2","3","3","3","3","3","3","3","3","3","3",
					 "3","3","3","3","3","3","3","3","3" };
			 parentid = new String[] { "0","0","0","0","1","1","1","2","2","2","3","5","5","5","5","5","5","5","5","5",
					 "6","7","8","8","8","9","9","9","10","11"};
			 id = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19",
					 "20","21","22","23","24","25","26","27","28","29","30" };
		}
		else if (portfolioId.equalsIgnoreCase("100777-5")) {	
		}
		JSONArray al1 = new JSONArray();
		for (int i = 0; i < sectorNames.length; i++) {
			HashMap<String, String> assets_hm = new HashMap<String, String>();
			assets_hm.put("strategyWeight", sectorCWeight[i]);
			assets_hm.put("name", sectorNames[i]);
			assets_hm.put("level", level[i]);
			assets_hm.put("parentId", parentid[i]);
			assets_hm.put("ID", id[i]);
			al1.put(assets_hm);
		}
		return al1;
	}
}
