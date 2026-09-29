package com.temenos.infinity.wealth.mock.processor.post;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class GetHealthIfOrderAcceptedPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");


	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response) 
		throws Exception {
		JSONObject response1 = new JSONObject();
		try {
			diagnostic.prepareDebug("==========> GetHealthIfOrderAcceptedPostProcessor Mock - Entered ").log();
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			String type = request.getParameterValues(TemenosConstants.ORDERSVIEW_TYPE)[0];
		
		if (portfolioId.equalsIgnoreCase("100777-5")) {
			JSONArray assetArray = new JSONArray();
			JSONObject assetObj = new JSONObject();

			String Names[] = new String[] {"Share","EU","Food processing", "Pharmaceuticals" , "US", "Beverages (Nonalcoholic)",  "Communications Equipment",
					"Computer Services", "Retail", "Other Regions", "Other Assets"};
			String CWeight[] = new String[] {"99.89", "3.07", "2.29", "0.78", "78.59", "0.11", "1.19", "1.31", "75.98", "18.23", "0.11"};
			String SWeight[] = new String[] {"80.00", "15.00", "8.00", "7.00", "60.00", "3.70", "3.60", "2.70", "50.00", "5.00", "20.00" };
			String HStatus[] = new String[] {"1", "1", "0", "1", "1", "0", "0", "0", "1", "1","1" };
			String level[] = new String[] {"1","2","3","3","2","3","3","3","3","2","1"};
			String parentId[] = new String[] {"0","1","2","2","1","5","5","5","5","1","0"};
			String ID[] = new String[] {"1","2","3","4","5","6","7","8","9","10","11"};
			for (int i = 0; i < Names.length; i++) {
				assetObj.put(TemenosConstants.NAME, Names[i]);
				assetObj.put(TemenosConstants.CURRENTWEIGHT, CWeight[i]);
				assetObj.put(TemenosConstants.STRATEGYWEIGHT, SWeight[i]);
				assetObj.put(TemenosConstants.HEALTHSTATUS, HStatus[i]);
				assetObj.put(TemenosConstants.LEVEL, level[i]);
				assetObj.put(TemenosConstants.PARENTID, parentId[i]);
				assetObj.put(TemenosConstants.ID, ID[i]);
				assetArray.put(assetObj);
				assetObj = new JSONObject();
			}
			response1.put("Assets", assetArray);
			response1.put("status", "success");
			response1.put("opstatus", "0");
			response1.put("portfolioID", portfolioId);
			response1.put("httpStatusCode", "200");
			Result final_result = Utilities.constructResultFromJSONObject(response1);
			result.appendResult(final_result);

			
		} else if ((type != null && type.equalsIgnoreCase("pastProposal")
				&& portfolioId.equalsIgnoreCase("100777-4"))) {
			JSONArray assetArray = new JSONArray();
			JSONObject assetObj = new JSONObject();
			diagnostic.prepareDebug("==========> GetHealthIfOrderAcceptedPostProcessor Mock - Past Proposal ").log();
			String Names[] = new String[] {"Fund", "US" , "Exchange Traded Funds", "Share" , "EU", "Retail" , "US", "Auto & Truck Manufacturers" ,"Beverages (Nonalcoholic)", "Communications Equipment",
					"Computer Services", "Consumer Financial Services","Retail", "Other Regions", "Other Assets"};
			String CWeight[] = new String[] { "6.39","6.39","6.39", "90.96", "3.87", "3.87", "76.27", "0.23", "0.55", "1.06",
					"6.30", "0.68", "67.44","10.82","2.65"};
			String SWeight[] = new String[] {"7.00","7.00", "7.00", "90.00", "5.00", "5.00", "75.00", "2.00", "0.50", "1.20",
					"1.30", "2.50", "67.50","10.00","3.00"};
			String HStatus[] = new String[] {"0","0", "0", "0", "0", "0", "0", "0", "0", "0", "0","0","0","0","0"};
			String level[] = new String[] {"1","2","3","1","2","3","2","3","3","3","3","3","3","2","1"};
			String parentId[] = new String[] {"0","1","2","0","4","5","4","7","7","7","7","7","7","4","0"};
			String ID[] = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
			for (int i = 0; i < Names.length; i++) {
				assetObj.put(TemenosConstants.NAME, Names[i]);
				assetObj.put(TemenosConstants.CURRENTWEIGHT, CWeight[i]);
				assetObj.put(TemenosConstants.STRATEGYWEIGHT, SWeight[i]);
				assetObj.put(TemenosConstants.HEALTHSTATUS, HStatus[i]);
				assetObj.put(TemenosConstants.LEVEL, level[i]);
				assetObj.put(TemenosConstants.PARENTID, parentId[i]);
				assetObj.put(TemenosConstants.ID, ID[i]);
				assetArray.put(assetObj);
				assetObj = new JSONObject();
			}
			response1.put("Assets", assetArray);
			response1.put("status", "success");
			response1.put("opstatus", "0");
			response1.put("portfolioID", portfolioId);
			response1.put("httpStatusCode", "200");
			Result final_result = Utilities.constructResultFromJSONObject(response1);
			result.appendResult(final_result);


		} else {
			response1.put("Assets", new ArrayList<String>());
		}
		//Result final_result = Utilities.constructResultFromJSONObject(response1);
        //final_result.addOpstatusParam("0");
        //final_result.addHttpStatusCodeParam("200");
        //final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
        //result.appendResult(final_result);
        
		  } catch (Exception e) {
              e.getMessage();
              alert.prepareError("==========> GetHealthIfOrderAcceptedPostProcessor Mock - Error: " + e.getMessage()).log();
          
          }
		diagnostic.prepareDebug("==========> GetHealthIfOrderAcceptedPostProcessor Mock - Exited ").log();
		return result;
	}
}

