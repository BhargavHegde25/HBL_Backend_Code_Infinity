package com.bct.javaservices;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetHBLParkingAccounts implements JavaService2{
	private static final String PaybleAccountsConfigFileName="config/PayableAccountsConfig.json";
	//private static final String configFile="config/PayableAccountsConfig.json";
	public static LoggerUtil logger = new LoggerUtil(GetHBLParkingAccounts.class);
	
	public Object invoke1(String methodId, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		
		//if(inputParams.get("featureName"))
		String jsonString="";
		Result result = new Result();
		JSONArray parkingAccountsArray= new JSONArray();
		JSONObject accounts= new JSONObject();
		JSONObject accountType= new JSONObject();
		JSONArray actionsArray= new JSONArray();
		accounts.put("accountId", "15481757");
		accounts.put("accountName", "Gandreddi098");
		accounts.put("accountStatus", "ACTIVE");
		accounts.put("accountType", "Savings");
		accounts.put("actions", actionsArray);
		
		accounts.put("arrangementId", "AA24009RVTH2");
		accounts.put("availableBalance", "16111.01");
		accounts.put("bankName", "Himalayan Bank Ltd");
		accounts.put("categoryId", "6003");
		accounts.put("companyId", "NP0010001");
		accounts.put("coreCustomerId", "9100013");
		accounts.put("coreCustomerName", "Gandreddi098");
		
		accounts.put("currencyCode", "NPR");
		accounts.put("currentBalance", "16111.01");
		accounts.put("displayName", "Mitchal Marsh 1757");
		accounts.put("isBusinessAccount", "false");
		accounts.put("nickName", "Mitchal Marsh 1757");
		accounts.put("productGroup", "HIMAL.SAVINGS");
		accounts.put("productId", "HIMAL.SAVINGS");
		
		accounts.put("supportBillPay", "1");
		accounts.put("supportChecks", "1");
		accounts.put("supportDeposit", "1");
		accounts.put("supportTransferFrom", "1");
		accounts.put("supportTransferTo", "1");
		accountType.put("Credit_Card_Payable_Account", accounts);		
		parkingAccountsArray.put(accountType);
		JSONObject resultObj = new JSONObject();
		resultObj.put("parkingAccounts", parkingAccountsArray);
		 result = JSONToResult.convert(resultObj.toString());
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		return result;
	}
	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest arg2, DataControllerResponse arg3)
			throws Exception {
		//logger.debug("GetHBLParkingAccounts:");
		Result result = new Result();
		String JsonString = readJsonFile();
		JSONArray payableAccountsArray = new JSONArray(JsonString);
		//logger.debug("payableAccountsArray:"+payableAccountsArray);
		JSONObject resultObj = new JSONObject();
		resultObj.put("parkingAccounts", payableAccountsArray);
        result = JSONToResult.convert(resultObj.toString());
		result.setParam(new Param("opstatus", "0"));
		result.setParam(new Param("httpStatusCode", "200"));
		return result;
	}
	 public static String readJsonFile() {
	        String arrayContent="[]";
	        try (InputStream is = URLFinder.class.getClassLoader().getResourceAsStream(PaybleAccountsConfigFileName);
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
	    	    	 JsonElement array = JsonParser.parseReader(reader);
	    	    	 arrayContent= array.getAsJsonArray().toString();
	    	    }
	    	    catch (FileNotFoundException e) {                                  
	    	    	logger.debug("FileNotFoundException Occured in HBL:GetHBLParkingAccounts:readJsonFile:error:" + e.getMessage());
	    	     } catch (IOException e) {                                    
	    	    	 logger.debug("IOException Occured in HBL:GetHBLParkingAccounts:readJsonFile:error:" + e.getMessage());
	    	      }catch (Exception e) {
	    	    	  logger.debug("Exception Occured in HBL:GetHBLParkingAccounts:readJsonFile:error:" + e.getMessage());
	    		}
	        return arrayContent;
	    }
}
