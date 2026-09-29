package com.temenos.infinity.wealth.mock.processor.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

public class GetAllStrategiesPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			diagnostic.prepareDebug("==========> GetAllStrategiesPostProcessor Mock - Entered ").log();
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			String INF_WLTH_STRATEGIES = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_STRATEGIES,
					request);

			if (portfolioId.equalsIgnoreCase("100777-4")) {

				JSONObject returnObj = new JSONObject();
				returnObj.put("recStrategy", recStrategy());	
				returnObj.put("alternateStrategy", alternateStrategy());
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);

			} else if (portfolioId.equalsIgnoreCase("100777-5")) {

				JSONObject returnObj = new JSONObject();
				returnObj.put("recStrategy", recStrategy1());	
				returnObj.put("alternateStrategy", alternateStrategy1());	
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);
			}

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetAllStrategiesPostProcessor Mock - Error: " + e.getMessage()).log();	
		}
		diagnostic.prepareDebug("==========> GetAllStrategiesPostProcessor Mock - Exited ").log();
		return result;
	}

	public JSONArray recStrategy() {
		String assetName[] = new String[] { "Stocks", "Bonds", "Funds", "Cash" };
		JSONObject n = new JSONObject();
		String weight[] = new String[] { "45.00", "35.00", "10.00", "10.00" };		
		JSONArray a_return= new JSONArray();
		JSONArray al1 = new JSONArray();
		n.put("strategyName", "Dynamic");
		n.put("strategyId", "100");
		for (int i = 0; i < assetName.length; i++) {
			HashMap<String, String> assets_hm = new HashMap<String, String>();
			assets_hm.put("assetName", assetName[i]);
			assets_hm.put("weight", weight[i]);
	        al1.put(assets_hm);
		}
		n.put("assetsCompo", al1);
		a_return.put(n);

		diagnostic.prepareDebug("==========> recStrategy Mock - Executed ").log();
		return a_return;
		
	}
	public JSONArray alternateStrategy() {
		String assetName1[] = new String[] { "Stocks", "Bonds", "Cash", "Funds" };
		JSONObject x = new JSONObject();
		String weight1[] = new String[] { "30.00", "30.00", "20.00", "20.00" };	
		JSONArray b_return= new JSONArray();
		JSONArray al2 = new JSONArray();
		x.put("strategyName", "Secure");
		x.put("strategyId", "101");
		for (int i = 0; i < assetName1.length; i++) {
			HashMap<String, String> assets_hm1 = new HashMap<String, String>();
			assets_hm1.put("assetName", assetName1[i]);
			assets_hm1.put("weight", weight1[i]);
	        al2.put(assets_hm1);
		}
		x.put("assetsCompo", al2);
		b_return.put(x);

		
		String assetName2[] = new String[] { "Cash", "Funds", "Stocks" , "Bonds" };
		JSONObject y = new JSONObject();
		JSONArray al3 = new JSONArray();
		String weight2[] = new String[] { "30.00", "30.00", "20.00", "20.00" };
		y.put("strategyName", "Conservative");
		y.put("strategyId", "102");
		for (int i = 0; i < assetName2.length; i++) {
			HashMap<String, String> assets_hm2 = new HashMap<String, String>();
			assets_hm2.put("assetName", assetName2[i]);
			assets_hm2.put("weight", weight2[i]);
	        al3.put(assets_hm2);
	        
		}
		y.put("assetsCompo", al3);
		b_return.put(y);
		
	
	
		
		String assetName3[] = new String[] { "Stocks", "Bonds", "Cash" , "Funds" };
		JSONObject z = new JSONObject();
		JSONArray al4 = new JSONArray();
		String weight3[] = new String[] { "25.00", "25.00", "25.00", "25.00" };
		z.put("strategyName", "Balanced");
		z.put("strategyId", "103");
		for (int i = 0; i < assetName3.length; i++) {
			HashMap<String, String> assets_hm3 = new HashMap<String, String>();
			assets_hm3.put("assetName", assetName3[i]);
			assets_hm3.put("weight", weight3[i]);
	        al4.put(assets_hm3);
	       
		}
		z.put("assetsCompo", al4);
		b_return.put(z);
	
		
		String assetName4[] = new String[] { "Stocks", "Bonds", "Cash", "Funds" };
		JSONObject g = new JSONObject();
		JSONArray al5 = new JSONArray();
		String weight4[] = new String[] { "40.00", "30.00", "10.00", "20.00" };
		g.put("strategyName", "Active");
		g.put("strategyId", "104");
		for (int i = 0; i < assetName4.length; i++) {
			HashMap<String, String> assets_hm4 = new HashMap<String, String>();
			assets_hm4.put("assetName", assetName4[i]);
			assets_hm4.put("weight", weight4[i]);
	        al5.put(assets_hm4);
		}
		g.put("assetsCompo", al5);
		b_return.put(g);
		
		

		diagnostic.prepareDebug("==========> alternateStrategy Mock - Executed ").log();
		return b_return;
		
	}
	

	public JSONArray recStrategy1() {
		String assetName6[] = new String[] { "Stocks", "Bonds", "Funds", "Cash" };
		JSONObject obj = new JSONObject();
		String weight6[] = new String[] { "25.00", "25.00", "25.00", "25.00" };		
		JSONArray i_return= new JSONArray();
		JSONArray assest1 = new JSONArray();
		obj.put("strategyName", "Balanced");
		obj.put("strategyId", "105");
		for (int i = 0; i < assetName6.length; i++) {
			HashMap<String, String> assets_hm6 = new HashMap<String, String>();
			assets_hm6.put("assetName", assetName6[i]);
			assets_hm6.put("weight", weight6[i]);
			assest1.put(assets_hm6);
		}
		obj.put("assetsCompo", assest1);
		i_return.put(obj);
		diagnostic.prepareDebug("==========> recStrategy1 Mock - Executed ").log();
		return i_return;
		
	}
	public JSONArray alternateStrategy1() {
		String assetName7[] = new String[] { "Cash", "Funds", "Stocks", "Bonds" };
		JSONObject obj1 = new JSONObject();
		String weight7[] = new String[] { "30.00", "30.00", "20.00", "20.00" };	
		JSONArray k_return= new JSONArray();
		JSONArray assest2 = new JSONArray();
		obj1.put("strategyName", "Conservative");
		obj1.put("strategyId", "106");
		for (int i = 0; i < assetName7.length; i++) {
			HashMap<String, String> assets_hm1 = new HashMap<String, String>();
			assets_hm1.put("assetName", assetName7[i]);
			assets_hm1.put("weight", weight7[i]);
			assest2.put(assets_hm1);
		}
		obj1.put("assetsCompo", assest2);
		k_return.put(obj1);

		
		String assetName8[] = new String[] { "Stocks", "Bonds", "Cash" , "Funds" };
		JSONObject obj2 = new JSONObject();
		JSONArray assest3 = new JSONArray();
		String weight8[] = new String[] { "30.00", "30.00", "20.00", "20.00" };
		obj2.put("strategyName", "Secure");
		obj2.put("strategyId", "107");
		for (int i = 0; i < assetName8.length; i++) {
			HashMap<String, String> assets_hm21 = new HashMap<String, String>();
			assets_hm21.put("assetName", assetName8[i]);
			assets_hm21.put("weight", weight8[i]);
			assest3.put(assets_hm21);
	        
		}
		obj2.put("assetsCompo", assest3);
		k_return.put(obj2);
		
	
	
		
		String assetName9[] = new String[] { "Stocks", "Bonds", "Cash" , "Funds" };
		JSONObject obj3 = new JSONObject();
		JSONArray assest4 = new JSONArray();
		String weight9[] = new String[] { "40.00", "30.00", "10.00", "20.00" };
		obj3.put("strategyName", "Active");
		obj3.put("strategyId", "108");
		for (int i = 0; i < assetName9.length; i++) {
			HashMap<String, String> assets_hm33 = new HashMap<String, String>();
			assets_hm33.put("assetName", assetName9[i]);
			assets_hm33.put("weight", weight9[i]);
			assest4.put(assets_hm33);
	       
		}
		obj3.put("assetsCompo", assest4);
		k_return.put(obj3);
	
		
		String assetName10[] = new String[] { "Stocks", "Bonds", "Cash", "Funds" };
		JSONObject obj4 = new JSONObject();
		JSONArray assest5 = new JSONArray();
		String weight10[] = new String[] { "45.00", "35.00", "10.00", "10.00" };
		obj4.put("strategyName", "Dynamic");
		obj4.put("strategyId", "109");
		for (int i = 0; i < assetName10.length; i++) {
			HashMap<String, String> assets_hm44 = new HashMap<String, String>();
			assets_hm44.put("assetName", assetName10[i]);
			assets_hm44.put("weight", weight10[i]);
			assest5.put(assets_hm44);
		}
		obj4.put("assetsCompo", assest5);
		k_return.put(obj4);
		
		
		diagnostic.prepareDebug("==========> alternateStrategy1 Mock - Executed ").log();
		return k_return;
		
	}
	
		



}
