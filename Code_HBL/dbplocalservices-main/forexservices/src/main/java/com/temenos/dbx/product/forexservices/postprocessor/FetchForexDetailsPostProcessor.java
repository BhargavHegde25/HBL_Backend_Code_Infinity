package com.temenos.dbx.product.forexservices.postprocessor;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class FetchForexDetailsPostProcessor extends BasePostProcessor{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {			
			
			String methodID = request.getParameter("methodID");
			switch(methodID)
			{
			case "fetchDashboardCurrencyRates":
	
                String forexDetResponse = response.getResponse();
                String foreignCurrencyList = (forexDetResponse.substring(forexDetResponse.indexOf("foreignCurrencies")-2,forexDetResponse.indexOf("localCurrencyId")-2)) + "}";
                foreignCurrencyList = foreignCurrencyList.replace("foreignCurrencyId", "code");//.replace("currencyMarkets", "markets").replace("currencyMarket", "market");
                foreignCurrencyList = foreignCurrencyList.replace("foreignCurrencyName", "name");
                foreignCurrencyList = foreignCurrencyList.replace("currencyMarkets", "markets");
                foreignCurrencyList = foreignCurrencyList.replace("currencyMarket", "market");

                JSONObject foreignCurrencyObj = new JSONObject(foreignCurrencyList);
    			for(int i=0;i<foreignCurrencyObj.getJSONArray("foreignCurrencies").length(); i++)
    			{				
    				JSONObject marketsArrayObject= new JSONObject();
    				if((foreignCurrencyObj.getJSONArray("foreignCurrencies").getJSONObject(i)).has("markets")){
    					JSONArray marketsArray =  foreignCurrencyObj.getJSONArray("foreignCurrencies").getJSONObject(i).getJSONArray("markets");
    					for(int j=0; j<marketsArray.length();j++) {												
    						String marketValue = marketsArray.getJSONObject(j).getString("market");
    						if(marketValue.equals("1"))
    						{
    							marketsArray.getJSONObject(j).put("market", "Currency");
    						}
    						else {
    							marketsArray.getJSONObject(j).put("market", "TT");
    						}
    					}
    					marketsArrayObject = new JSONObject("{\"markets\":"+marketsArray.toString()+"}");
    				}
    				foreignCurrencyObj = foreignCurrencyObj.accumulate("markets", marketsArrayObject);
    			}
    			String responseString = "{\"ForeignCurrencies\":"+foreignCurrencyObj.getJSONArray("foreignCurrencies").toString()+"}";
    			
                result = JSONToResult.convert(responseString);
                break;
                
			case "fetchBaseCurrency":
				
				String forexResponse = response.getResponse();				
				String localCurrencyIdString = "{"+forexResponse.substring(forexResponse.indexOf("localCurrencyId")-1,forexResponse.lastIndexOf("}")-2) + "}";
				localCurrencyIdString =  localCurrencyIdString.replace("localCurrencyId", "code");
				localCurrencyIdString =  localCurrencyIdString.replace("currencyName", "name");
				result = JSONToResult.convert(localCurrencyIdString);
				break;
				
			case "fetchCurrencyRates":
				String quoteCurrency = request.getParameter("quoteCurrencyCode");
				String forexCurrencyRateResposnse = response.getResponse(); 
				
                String currencyList = (forexCurrencyRateResposnse.substring(forexCurrencyRateResposnse.indexOf("foreignCurrencies")-2,forexCurrencyRateResposnse.indexOf("localCurrencyId")-2)) + "}";
                currencyList = currencyList.replace("foreignCurrencyId", "code");
                currencyList = currencyList.replace("foreignCurrencyName", "name");
                currencyList = currencyList.replace("currencyMarkets", "markets");
                currencyList = currencyList.replace("currencyMarket", "market");
                String currencyRates = "";
                JSONObject CurrencyObj = new JSONObject(currencyList);
    			for(int i=0;i<CurrencyObj.getJSONArray("foreignCurrencies").length(); i++)
    			{				    				
    				String currency = (CurrencyObj.getJSONArray("foreignCurrencies").getJSONObject(i)).getString("code");
    				if(currency.equals(quoteCurrency)){
    					currencyRates = CurrencyObj.getJSONArray("foreignCurrencies").getJSONObject(i).toString();
    					break;
    				}
    			}              
                JSONObject currencyRatesObj = new JSONObject(currencyRates);
                JSONArray marketsArray =  currencyRatesObj.getJSONArray("markets");
                for(int j=0; j<marketsArray.length();j++) {                                               
                    String marketValue = marketsArray.getJSONObject(j).getString("market");
                    if(marketValue.equals("1"))
                    {
                        marketsArray.getJSONObject(j).put("market", "Currency");
                    }
                    else {
                        marketsArray.getJSONObject(j).put("market", "TT");
                    }
                }                
                result = JSONToResult.convert(currencyRatesObj.toString());
                break;
			}
			
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking post processor for FetchForexDetailsPostProcessor: ", e).log();
			return null;
		}
		return result;
	}
}