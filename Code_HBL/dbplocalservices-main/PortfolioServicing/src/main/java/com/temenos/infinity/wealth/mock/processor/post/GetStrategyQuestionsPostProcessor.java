package com.temenos.infinity.wealth.mock.processor.post;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetStrategyQuestionsPostProcessor implements DataPostProcessor2  {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			alert.prepareError("==========> GetStrategyQuestionsPostProcessor Mock - Entered").log();
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
		
			
			if(portfolioId != null &&  portfolioId.trim().length() > 0 ) {
							
				String questions[]    = new String[] {"You fall within the age category of?","For how long would you expect most of your money to be invested before you would need to access it?","What would you do with your investment if the market falls?","How easily can you meet your unforeseen financial needs without liquidating your current investment?","How would you best describe your income expectations over the next five years?","How would you describe your knowledge of investments?","What is your primary objective for Investment?","What is the maximum loss that you can tolerate (within 1 year) in a worst case scenario?" };
				String questionCode[] = new String[] {"Age Category","investment_period","Market Fall Behaviour","financial_needs","income_expectation","investment_knowledge","investment_objective","max_loss_tolerance_capacity"};
				String desc1[]= new String[] { "18 to below 30", "30 to below 40", "40 to below 50","50 to below 60","60 & Above"};
				String name1[]= new String[] { "18_TO_BELOW_30", "30_TO_BELOW_40", "40_TO_BELOW_50","50_TO_BELOW_60","60_AND_ABOVE"};
				String desc2[]= new String[] {  "Less than 3 years", "Between 3 & 5 years","Between 5 & 10 years","10 years & above"};
				String name2[]= new String[] {  "LESS_THAN_3_YEARS", "BETWEEN_3_AND_5_YEARS","BETWEEN_5_AND_10_YEARS","10_YEARS_AND_ABOVE"};
				String desc3[]= new String[] { "I am willing to accept minimal declines in the value of my investments, as capital preservation is my primary objective.", "I am willing to accept some declines, but I am not comfortable with moderate to extreme drops in the value of my investments.","I am willing to accept moderate declines, but I am not comfortable with extreme drops in the value of my investments.","I am prepared to take losses and large fluctuations in the value of my investments in order to maximize my long term returns."};
				String name3[]= new String[] {  "ACCEPT_MINIMAL_DECLINES", "ACCEPT_SOME_DECLINES","ACCEPT_MODERATE_DECLINES","TAKE_LOSSES_AND_LARGE_FLUCT"};
				String desc4[]= new String[] {"It would be difficult with some planning.", "Easily with some planning.","It would be very difficult with some planning.","Very easily after some planning."};
				String name4[]= new String[] {  "DIFFICULT", "EASILY","VERY_DIFFICULT","VERY_EASILY"};
				String desc5[]= new String[] {"I expect my income to fall (as a result of retirement, reduced working-hours etc.).", "My income fluctuates from year to year (e.g. for self-employed investors).","I expect my income to keep pace with inflation.","I expect my income to rise well ahead of inflation (through promotion, career developments etc.)."};
				String name5[]= new String[] {  "FALL", "FLUCTUATE_FROM_YEAR_TO_YEAR","KEEP_PACE_WITH_INFLATION","RISE_WELL_AHEAD_OF_INFLATION"};
				String desc6[]= new String[] {"Extensive","Good","Limited","None"};
				String name6[]= new String[] {  "EXTENSIVE", "GOOD","LIMITED","NONE"};
				String desc7[]= new String[] {"Preservation of capital with minimal opportunity for capital growth.","High capital appreciation with minimum income generation.","Generate a stream of income with less emphasis on capital appreciation.","Steady capital appreciation with less emphasis on income generation."};
				String name7[]= new String[] {  "CAPITAL_PRESERVATION", "HIGH_CAPITAL_APPRECIATION","STEADY_CAPITAL_APPRECIATION","GOOD"};
				String desc8[]= new String[] {"Between 10% and 20%","Between 20% and 25%","Between 3% and 10%","Less than 3%","Over 25%"};
				String name8[]= new String[] { "BETWEEN_10_AND_20","BETWEEN_20_AND_25","BETWEEN_3_AND_10","LESS_THAN_3_PERCENT","OVER_25_PERCENT"};
				
				ArrayList<String[]> descList= new ArrayList<String[]>();
				descList.add(desc1);
				descList.add(desc2);
				descList.add(desc3);
				descList.add(desc4);
				descList.add(desc5);
				descList.add(desc6);
				descList.add(desc7);
				descList.add(desc8);
				
				ArrayList<String[]> codeList= new ArrayList<String[]>();
				codeList.add(name1);
				codeList.add(name2);
				codeList.add(name3);
				codeList.add(name4);
				codeList.add(name5);
				codeList.add(name6);
				codeList.add(name7);
				codeList.add(name8);

				
				JSONObject FinalObj = new JSONObject();
				JSONArray innerList= new JSONArray();
				
				for(int i=0;i<questions.length;i++) {
					
					JSONObject innerObj = new JSONObject();
					innerObj.put("question",questions[i]);
					
//					if(i == 0) {
//						
//						innerObj.put("name", questionCode[i]);
//					}
//					
//					else {
//						
//						innerObj.put("questionCode", questionCode[i]);
//					}
//					
					JSONArray options = option(descList.get(i),codeList.get(i));
					innerObj.put("questionCode", questionCode[i]);
					innerObj.put("option", options);
					innerList.put(innerObj);
				}
				
				
				FinalObj.put("questions",innerList);


				Result final_result = Utilities.constructResultFromJSONObject(FinalObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				final_result.addParam("code", "PCK_DS_INVEST_PROF");
				result.appendResult(final_result);
			}
					
			else {
				result.appendResult(PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID));
				return result;
			}
		}
		 catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetStrategyQuestionsPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		alert.prepareError("==========> GetStrategyQuestionsPostProcessor Mock - Exited").log();
		return result;
		
		
	}
	
	private static JSONArray option(String[] desc, String[] name) {
		
		JSONArray ar = new JSONArray();
		for (int i = 0; i < desc.length; i++) {
			JSONObject option = new JSONObject();
			option.put("desc", desc[i]);
			option.put("defValueF", false);
			option.put("name", name[i]);
			ar.put(option);
		}
		return ar;
	}
	
	
}




