package com.temenos.infinity.api.savingspot.resource.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.memorymgmt.SavingsPotManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.savingspot.businessdelegate.api.SavingsPotBusinessDelegate;
import com.temenos.infinity.api.savingspot.constants.SavingsPotFeatureAction;
import com.temenos.infinity.api.savingspot.constants.TemenosConstants;
import com.temenos.infinity.api.savingspot.dto.SavingsPotCategoriesDTO;
import com.temenos.infinity.api.savingspot.dto.SavingsPotDTO;
import com.temenos.infinity.api.savingspot.resource.api.SavingsPotResource;
import com.temenos.infinity.api.savingspot.util.CustomerSession;
import com.temenos.infinity.api.savingspot.util.SavingsPotUtilites;


	
public class SavingsPotResourceImpl implements SavingsPotResource {	
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	 
	 @Override
	    public Result getAllSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request, 
				DataControllerResponse response) {
		 @SuppressWarnings("unchecked")
			Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
	        Map<String, Object> headersMap = HelperMethods.addMSJWTAuthHeader(request, request.getHeaderMap(),
	                AuthConstants.POST_LOGIN_FLOW);
	        SavingsPotUtilites.setCompanyIdToRequest(request);
			Result result = new Result();
			
			AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
			
			Map<String,Object> customer = CustomerSession.getCustomerMap(request);
		    String user_id = CustomerSession.getCustomerId(customer);
		    String featureAction1 = SavingsPotFeatureAction.BUDGET_POT_VIEW;
		    String featureAction2 = SavingsPotFeatureAction.GOAL_POT_VIEW;
		      
		    Boolean isViewGoalPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer));
		    Boolean isViewBudgetPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer));
		    
		    if(!isViewGoalPermitted && !isViewBudgetPermitted) {
		    	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		    }
			List<SavingsPotDTO> savingsPotDTOList;
			Object fundingAccountIdObj =inputParams.get(TemenosConstants.FUNDINGACCOUNTID);
			String fundingAccountId=null;
				if(fundingAccountIdObj!= null)
				{
					fundingAccountId = inputParams.get(TemenosConstants.FUNDINGACCOUNTID).toString();
				}
	             SavingsPotBusinessDelegate savingsPotBusinessDelegate =
	                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(SavingsPotBusinessDelegate.class);
	             try {
	            	  savingsPotDTOList = savingsPotBusinessDelegate.getAllSavingsPot(fundingAccountId,headersMap);
			}catch (Exception e) {
    		    alert.prepareError("Error while fetching SavingsPotDTO from SavingsPotBusinessDelegate : " + e).log();
    		    return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
    		} 
	             try{
	            	 String savingsPotStr;
	            	 if(savingsPotDTOList==null)
	            	 {
	            		 JSONArray savingsPotJSONArr = new JSONArray();
	            		 JSONObject resultJSON = new JSONObject();
				         resultJSON.put(TemenosConstants.SAVINGSPOT, savingsPotJSONArr);
				         result = JSONToResult.convert(resultJSON.toString());
				         return result;
	            	 }
					 savingsPotStr = JSONUtils.stringifyCollectionWithTypeInfo(savingsPotDTOList, SavingsPotDTO.class);
					 JSONArray savingsPotJSONArr = new JSONArray(savingsPotStr);  
					 JSONObject savingsPot = null;
					 String potAmtPercentage = "";
					 String potType = "";
					 String potCurrStatus = "";
					 String frequencyDay = "";
					 String remainingSavings = "";
					 long monthsLeftForCompletion = 0 ; 
					 String convertedStartDate =null;
					 String convertedEndDate = null;
					 JSONArray finalSavingsPotJSONArr = new JSONArray();  
					 if(!isViewGoalPermitted) {
						 for(int i = 0;i<savingsPotJSONArr.length();i++) {
								 savingsPot = savingsPotJSONArr.getJSONObject(i);
								 potType = savingsPot.get(TemenosConstants.POTTYPE).toString();
								 if(potType.equalsIgnoreCase(TemenosConstants.BUDGET)) {
									 finalSavingsPotJSONArr.put(savingsPot);
								 }
									 
						 }
					 }else if(!isViewBudgetPermitted) {
						 for(int i = 0;i<savingsPotJSONArr.length();i++) {
						 savingsPot = savingsPotJSONArr.getJSONObject(i);
						 potType = savingsPot.get(TemenosConstants.POTTYPE).toString();
						 if(potType.equalsIgnoreCase(TemenosConstants.GOAL))
							 finalSavingsPotJSONArr.put(savingsPot);	
						 }
					 }else {
						 finalSavingsPotJSONArr = savingsPotJSONArr;
					 }
					 for(int i = 0;i<finalSavingsPotJSONArr.length();i++) {
						 savingsPot = finalSavingsPotJSONArr.getJSONObject(i);
						 potType = savingsPot.get(TemenosConstants.POTTYPE).toString();
						 String dateStart = savingsPot.get(TemenosConstants.STARTDATE).toString();
						 String dateEnd = savingsPot.get(TemenosConstants.ENDDATE).toString();
						 if(!dateStart.equals("null") && !dateEnd.equals("null"))
						 {
							 convertedStartDate= SavingsPotUtilites.changeDateToMMDDYYYY(dateStart.toString());
							 convertedEndDate = SavingsPotUtilites.changeDateToMMDDYYYY(dateEnd.toString());
						 }

						 potAmtPercentage = SavingsPotUtilites.getPotAmountPercentage(savingsPot.getString(TemenosConstants.TARGETAMOUNT), savingsPot.getString(TemenosConstants.AVAILABLEBALANCE));
						 if(potType.equalsIgnoreCase(TemenosConstants.GOAL))
						 {
							 if(!savingsPot.isNull(TemenosConstants.FREQUENCY) && !savingsPot.isNull(TemenosConstants.PERIODICCONTRIBUTION) && !savingsPot.isNull(TemenosConstants.STARTDATE))
							 {
								 potCurrStatus = SavingsPotUtilites.getPotCurrentStatus(savingsPot.getString(TemenosConstants.TARGETAMOUNT), savingsPot.getString(TemenosConstants.AVAILABLEBALANCE), potType,dateStart, savingsPot.get(TemenosConstants.PERIODICCONTRIBUTION).toString(), savingsPot.get(TemenosConstants.FREQUENCY).toString());
							 }
						 }
						 else
						 {
							 potCurrStatus = SavingsPotUtilites.getPotCurrentStatus(savingsPot.getString(TemenosConstants.TARGETAMOUNT), savingsPot.getString(TemenosConstants.AVAILABLEBALANCE),potType,null,null,null);
						 }
						 
					     savingsPot.put(TemenosConstants.POTAMOUNTPERCENTAGE, potAmtPercentage == null ? JSONObject.NULL : potAmtPercentage);
						 savingsPot.put(TemenosConstants.POTCURRENTSTATUS, potCurrStatus == null ? JSONObject.NULL : potCurrStatus);
						 
						 if(potType.equalsIgnoreCase(TemenosConstants.GOAL)) {
							 frequencyDay = SavingsPotUtilites.getFrequencyDay(savingsPot.getString(TemenosConstants.STARTDATE), savingsPot.getString(TemenosConstants.FREQUENCY));
							 remainingSavings = SavingsPotUtilites.getRemainingBalances(savingsPot.getString(TemenosConstants.TARGETAMOUNT), savingsPot.getString(TemenosConstants.AVAILABLEBALANCE));	 
							 monthsLeftForCompletion = SavingsPotUtilites.getMonthsLeft(dateEnd);
							 String monthsLeft =monthsLeftForCompletion + " Months left ";
							 savingsPot.put(TemenosConstants.FREQUENCYDAY, frequencyDay == null ? JSONObject.NULL : frequencyDay);
							 savingsPot.put(TemenosConstants.REMAININGSAVINGS, remainingSavings== null ? JSONObject.NULL : remainingSavings );
						 savingsPot.put(TemenosConstants.MONTHSLEFTFORCOMPLETION, monthsLeft == null ? JSONObject.NULL : monthsLeft );
					     } 
						 savingsPot.put(TemenosConstants.STARTDATE, convertedStartDate);
						 savingsPot.put(TemenosConstants.ENDDATE, convertedEndDate);
					 }
			            JSONObject resultJSON = new JSONObject();
			            resultJSON.put(TemenosConstants.SAVINGSPOT, finalSavingsPotJSONArr);
			            result = JSONToResult.convert(resultJSON.toString());
			             			
				}  catch (Exception e) {
		        	alert.prepareError("Error while converting response SavingsPotCategoriesDTO to result " + e).log();
				    return null;
		       }
		   	return result;  
	    }

	 @Override
		public Result getCategories(String methodID, Object[] inputArray, DataControllerRequest request, 
				DataControllerResponse response)  {
			   Result result = new Result();
			   Map<String, Object> headersMap = HelperMethods.addMSJWTAuthHeader(request, request.getHeaderMap(),
		                AuthConstants.POST_LOGIN_FLOW);
			   SavingsPotUtilites.setCompanyIdToRequest(request);
			   List<SavingsPotCategoriesDTO> categoriesDTO;
	            SavingsPotBusinessDelegate savingsPotBusinessDelegate =
	                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(SavingsPotBusinessDelegate.class);
                //To fetch category of savingsPot, the micro service that we are hitting is expecting type as "SavingsType"
	            String type = TemenosConstants.TYPEFORGOALCATEGORY;
	            try{
	            	 categoriesDTO = savingsPotBusinessDelegate.getCategories(type,headersMap);
	            }catch (Exception e) {
	    		    alert.prepareError("Error while fetching SavingsPotCategoriesDTO from SavingsPotBusinessDelegate : " + e).log();
	    		    return null;
	    		}
	           try {
	        	   String categoriesStr = JSONUtils.stringifyCollectionWithTypeInfo(categoriesDTO, SavingsPotCategoriesDTO.class);
	            JSONArray categoriesJSONArr = new JSONArray(categoriesStr);
	            if (categoriesJSONArr.length() <= 0) {
	                return ErrorCodeEnum.ERR_20042.setErrorCode(new Result());
	            }
	            JSONObject resultJSON = new JSONObject();
	            resultJSON.put("category", categoriesJSONArr);
	            result = JSONToResult.convert(resultJSON.toString());
	            return result;
	        } catch (Exception e) {
	        	alert.prepareError("Error while converting response SavingsPotCategoriesDTO to result " + e).log();
			    return null;
	       }
		}

	@Override
	public Result createSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Map<String, Object> headersMap = HelperMethods.addMSJWTAuthHeader(request, request.getHeaderMap(),
                AuthConstants.POST_LOGIN_FLOW);
		SavingsPotUtilites.setCompanyIdToRequest(request);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		Map<String,Object> customer = CustomerSession.getCustomerMap(request);
	    String user_id = CustomerSession.getCustomerId(customer);
	    String featureAction1 = SavingsPotFeatureAction.BUDGET_POT_CREATE;
	    String featureAction2 = SavingsPotFeatureAction.GOAL_POT_CREATE;
	    
	    String potType =null;
        if(inputParams.get(TemenosConstants.POTTYPE)!=null)
        {
        	potType = inputParams.get(TemenosConstants.POTTYPE).toString();
        }
        if(potType.equalsIgnoreCase(TemenosConstants.GOAL)) {
        	Boolean isCreateGoalPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer));
    	    if(!isCreateGoalPermitted) {
    	    	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	    }  	
        }else if(potType.equalsIgnoreCase(TemenosConstants.BUDGET)) {
        	Boolean isCreateBudgetPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer));
        	if(!isCreateBudgetPermitted) {
    	    	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	    } 
        }
	    
		SavingsPotBusinessDelegate savingsPotDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SavingsPotBusinessDelegate.class);
		
		String partyId = CustomerSession.getCustomerId(customer);
        String fundingAccountHoldingId =null;
        if(inputParams.get(TemenosConstants.FUNDINGACCOUNTID)!=null)
        {
        	fundingAccountHoldingId = inputParams.get(TemenosConstants.FUNDINGACCOUNTID).toString();
        }
        String productId =null;
        if(inputParams.get(TemenosConstants.PRODUCTID)!=null)
        {
        	productId = inputParams.get(TemenosConstants.PRODUCTID).toString();
        }
        String potAccountId =null;
        if(inputParams.get(TemenosConstants.POTACCOUNTID)!=null)
        {
        	potAccountId = inputParams.get(TemenosConstants.POTACCOUNTID).toString();
        }
        String potName =null;
        if(inputParams.get(TemenosConstants.POTNAME)!=null)
        {
        	potName = inputParams.get(TemenosConstants.POTNAME).toString();
        }
        
        String currency =null;
        if(inputParams.get(TemenosConstants.CURRENCY)!=null)
        {
        	currency = inputParams.get(TemenosConstants.CURRENCY).toString();
        }
        String targetAmount =null;
        if(inputParams.get(TemenosConstants.TARGETAMOUNT)!=null)
        {
        	targetAmount = inputParams.get(TemenosConstants.TARGETAMOUNT).toString();
        }
		String startDate = "";
		String endDate = "";
		String periodicContribution = "";
	    String targetPeriod = "";
	    String frequency = null;
	    String savingsType = "";
	    if(inputParams.get(TemenosConstants.SAVINGSTYPE)!=null)
        {
        	savingsType = inputParams.get(TemenosConstants.SAVINGSTYPE).toString();
        }
	    if(inputParams.get(TemenosConstants.FREQUENCY)!=null)
        {
        	frequency = inputParams.get(TemenosConstants.FREQUENCY).toString();
        }
	    if(potType.equalsIgnoreCase(TemenosConstants.GOAL)){
	        if(inputParams.get(TemenosConstants.PERIODICCONTRIBUTION)!=null)
	        {
	        	periodicContribution = inputParams.get(TemenosConstants.PERIODICCONTRIBUTION).toString();
	        }
	        if(inputParams.get(TemenosConstants.TARGETPERIOD)!=null)
	        {
	        	targetPeriod = inputParams.get(TemenosConstants.TARGETPERIOD).toString();
	        }
	        
	        if(inputParams.get(TemenosConstants.STARTDATE)!=null)
	        {
	        	startDate = inputParams.get(TemenosConstants.STARTDATE).toString();
	        }
	        if(inputParams.get(TemenosConstants.ENDDATE)!=null)
	        {
	        	endDate = inputParams.get(TemenosConstants.ENDDATE).toString();
	        }
	        
			 startDate = SavingsPotUtilites.changeDateToYYYYMMDD(startDate);
			 endDate = SavingsPotUtilites.changeDateToYYYYMMDD(endDate);
			 		
		}
		 	
		SavingsPotDTO savingsPotDTO = savingsPotDelegate.createSavingsPot(fundingAccountHoldingId,partyId,productId,potAccountId,potName,potType,savingsType,currency,targetAmount,targetPeriod,frequency,startDate,endDate,periodicContribution,headersMap);
		JSONObject JSONResponse = new JSONObject(savingsPotDTO);
		if(JSONResponse.has(TemenosConstants.ERRMESSAGE) && JSONResponse.get(TemenosConstants.ERRMESSAGE)!=""){
			alert.prepareError(JSONResponse.get(TemenosConstants.ERRMSG).toString()).log();
			String errorMessage = JSONResponse.get(TemenosConstants.ERRMESSAGE).toString();
		    return ErrorCodeEnum.ERR_20041.setErrorCode(result, errorMessage);
		}else if(JSONResponse.has(TemenosConstants.CODE)){
			alert.prepareError(JSONResponse.get(TemenosConstants.MESSAGE).toString()).log();
			String message = JSONResponse.get(TemenosConstants.MESSAGE).toString();
		    return ErrorCodeEnum.ERR_20041.setErrorCode(result,message);
		}else{
	    result = JSONToResult.convert(JSONResponse.toString());
	    return result;
		}
	}

	@Override
	public Result closeSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Map<String, Object> headersMap = HelperMethods.addMSJWTAuthHeader(request, request.getHeaderMap(),
                AuthConstants.POST_LOGIN_FLOW);
		SavingsPotUtilites.setCompanyIdToRequest(request);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		Map<String,Object> customer = CustomerSession.getCustomerMap(request);
	    String user_id = CustomerSession.getCustomerId(customer);
	    String featureAction1 = SavingsPotFeatureAction.BUDGET_POT_CLOSE;
	    String featureAction2 = SavingsPotFeatureAction.GOAL_POT_CLOSE;
	    
	    String savingsPotId = inputParams.get(TemenosConstants.SAVINGSPOTID).toString();
	    SavingsPotManager savingsPotManager = new SavingsPotManager(user_id);
	    String potType = savingsPotManager.getPotType(user_id,savingsPotId);
        if(potType.equalsIgnoreCase(TemenosConstants.GOAL)) {
        	Boolean isCloseGoalPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer));
    	    if(!isCloseGoalPermitted) {
    	    	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	    }  	
        }else if(potType.equalsIgnoreCase(TemenosConstants.BUDGET)) {
        	Boolean isCloseBudgetPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer));
        	if(!isCloseBudgetPermitted) {
    	    	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	    } 
        }
        
        SavingsPotBusinessDelegate savingsPotDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SavingsPotBusinessDelegate.class);
		SavingsPotDTO savingsPotDTO = savingsPotDelegate.closeSavingsPot(savingsPotId,headersMap);
		JSONObject JSONResponse = new JSONObject(savingsPotDTO);
		if(JSONResponse.has(TemenosConstants.ERRMESSAGE) && JSONResponse.get(TemenosConstants.ERRMESSAGE)!=""){
			alert.prepareError(JSONResponse.get(TemenosConstants.ERRMSG).toString()).log();
			String errorMessage = JSONResponse.get(TemenosConstants.ERRMESSAGE).toString();
		    return ErrorCodeEnum.ERR_20041.setErrorCode(result, errorMessage);
		}else if(JSONResponse.has(TemenosConstants.CODE)){
			alert.prepareError(JSONResponse.get(TemenosConstants.MESSAGE).toString()).log();
			String message = JSONResponse.get(TemenosConstants.MESSAGE).toString();
		    return ErrorCodeEnum.ERR_20041.setErrorCode(result,message);
		}else {
	    result = JSONToResult.convert(JSONResponse.toString());
	    return result;
		}
	}

	@Override
	public Result updateSavingsPotBalance(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Map<String, Object> headersMap = HelperMethods.addMSJWTAuthHeader(request, request.getHeaderMap(),
                AuthConstants.POST_LOGIN_FLOW);
		SavingsPotUtilites.setCompanyIdToRequest(request);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		Map<String,Object> customer = CustomerSession.getCustomerMap(request);
	    String user_id = CustomerSession.getCustomerId(customer);
	    String featureAction1 = SavingsPotFeatureAction.BUDGET_POT_ADHOC_FUND;
	    String featureAction2 = SavingsPotFeatureAction.GOAL_POT_ADHOC_FUND;
	    String featureAction3 = SavingsPotFeatureAction.BUDGET_POT_WITHDRAW_FUND;
	    String featureAction4 = SavingsPotFeatureAction.GOAL_POT_WITHDRAW_FUND;
	    
	    String savingsPotId = inputParams.get(TemenosConstants.SAVINGSPOTID).toString();
	    SavingsPotManager savingsPotManager = new SavingsPotManager(user_id);
	    String potType = savingsPotManager.getPotType(user_id,savingsPotId);
	    String isCreditDebit = inputParams.get(TemenosConstants.ISCREDITDEBIT).toString();
        if(potType.equalsIgnoreCase(TemenosConstants.GOAL)) {
        	if(isCreditDebit.equalsIgnoreCase(TemenosConstants.CREDIT)) {
    	    	Boolean isFundGoalPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer));
            	if(!isFundGoalPermitted)
            		return ErrorCodeEnum.ERR_12001.setErrorCode(result);   	
    	    }else {
    	       Boolean isWithdrawGoalPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction4, null, CustomerSession.IsCombinedUser(customer));
               if(!isWithdrawGoalPermitted)
            		return ErrorCodeEnum.ERR_12001.setErrorCode(result); 
    	    }
        }else{
        	if(isCreditDebit.equalsIgnoreCase(TemenosConstants.CREDIT)) {
        		Boolean isFundBudgetPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer));
            	if(!isFundBudgetPermitted)
            		return ErrorCodeEnum.ERR_12001.setErrorCode(result);   	
    	    }else {
    	    	Boolean isWithdrawBudgetPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction3, null, CustomerSession.IsCombinedUser(customer));
            	if(!isWithdrawBudgetPermitted)
            		return ErrorCodeEnum.ERR_12001.setErrorCode(result); 
    	    }
        }
        
		SavingsPotBusinessDelegate savingsPotDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SavingsPotBusinessDelegate.class);
		String amount = inputParams.get(TemenosConstants.AMOUNT).toString();
		SavingsPotDTO savingsPotDTO = savingsPotDelegate.updateSavingsPotBalance(savingsPotId,amount,isCreditDebit,headersMap);
		JSONObject JSONResponse = new JSONObject(savingsPotDTO);
		if(JSONResponse.has(TemenosConstants.ERRMESSAGE) && JSONResponse.get(TemenosConstants.ERRMESSAGE)!=""){
			alert.prepareError(JSONResponse.get(TemenosConstants.ERRMSG).toString()).log();
			String errorMessage = JSONResponse.get(TemenosConstants.ERRMESSAGE).toString();
		    return ErrorCodeEnum.ERR_20041.setErrorCode(result, errorMessage);
		}else if(JSONResponse.has(TemenosConstants.CODE)){
			alert.prepareError(JSONResponse.get(TemenosConstants.MESSAGE).toString()).log();
			String message = JSONResponse.get(TemenosConstants.MESSAGE).toString();
		    return ErrorCodeEnum.ERR_20041.setErrorCode(result,message);
		}else {
	    result = JSONToResult.convert(JSONResponse.toString());
	    return result;
		}
	}

	@Override
	public Result updateSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Map<String, Object> headersMap = HelperMethods.addMSJWTAuthHeader(request, request.getHeaderMap(),
                AuthConstants.POST_LOGIN_FLOW);
		SavingsPotUtilites.setCompanyIdToRequest(request);
		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		Map<String,Object> customer = CustomerSession.getCustomerMap(request);
	    String user_id = CustomerSession.getCustomerId(customer);
	    String featureAction1 = SavingsPotFeatureAction.BUDGET_POT_EDIT;
	    String featureAction2 = SavingsPotFeatureAction.GOAL_POT_EDIT;
	    String savingsPotId = inputParams.get(TemenosConstants.SAVINGSPOTID).toString();
	    String potType = "";
	    if(inputParams.get(TemenosConstants.POTTYPE)!=null)
        {
         potType = inputParams.get(TemenosConstants.POTTYPE).toString();
        }else {
        	SavingsPotManager savingsPotManager = new SavingsPotManager(user_id);
    	    potType = savingsPotManager.getPotType(user_id,savingsPotId);
        }
        if(potType.equalsIgnoreCase(TemenosConstants.GOAL)) {
        	Boolean isEditGoalPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer));
    	    if(!isEditGoalPermitted) {
    	    	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	    }  	
        }else if(potType.equalsIgnoreCase(TemenosConstants.BUDGET)) {
        	Boolean isEditBudgetPermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer));
        	if(!isEditBudgetPermitted) {
    	    	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
    	    } 
        }
        
        SavingsPotBusinessDelegate savingsPotDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SavingsPotBusinessDelegate.class);
		Map<String, Object> updateSavingsPotMap = new HashMap<>();
		updateSavingsPotMap.put(TemenosConstants.SAVINGSPOTID, savingsPotId);
		updateSavingsPotMap.put(TemenosConstants.POTNAME, inputParams.get(TemenosConstants.POTNAME));
		updateSavingsPotMap.put(TemenosConstants.POTTYPE, inputParams.get(TemenosConstants.POTTYPE));
		String startDate = null;
		String endDate = null;
		if(inputParams.get(TemenosConstants.STARTDATE) != null && inputParams.get(TemenosConstants.STARTDATE) != "")
		{
			startDate = SavingsPotUtilites.changeDateToYYYYMMDD(inputParams.get(TemenosConstants.STARTDATE).toString());
		}
		if(inputParams.get(TemenosConstants.ENDDATE) != null && inputParams.get(TemenosConstants.ENDDATE) != "")
		{
			endDate = SavingsPotUtilites.changeDateToYYYYMMDD(inputParams.get(TemenosConstants.ENDDATE).toString());
		}
		updateSavingsPotMap.put(TemenosConstants.STARTDATE, startDate);
		updateSavingsPotMap.put(TemenosConstants.ENDDATE, endDate);
		updateSavingsPotMap.put(TemenosConstants.FREQUENCY, inputParams.get(TemenosConstants.FREQUENCY));
		updateSavingsPotMap.put(TemenosConstants.TARGETAMOUNT, inputParams.get(TemenosConstants.TARGETAMOUNT));
		updateSavingsPotMap.put(TemenosConstants.TARGETPERIOD, inputParams.get(TemenosConstants.TARGETPERIOD));
		updateSavingsPotMap.put(TemenosConstants.PERIODICCONTRIBUTION, inputParams.get(TemenosConstants.PERIODICCONTRIBUTION));
		updateSavingsPotMap.put(TemenosConstants.SAVINGSTYPE, inputParams.get(TemenosConstants.SAVINGSTYPE));
		SavingsPotDTO savingsPotDTO = savingsPotDelegate.updateSavingsPot(updateSavingsPotMap,headersMap);
		JSONObject JSONResponse = new JSONObject(savingsPotDTO);
		if(JSONResponse.has(TemenosConstants.ERRMESSAGE) && JSONResponse.get(TemenosConstants.ERRMESSAGE)!=""){
			alert.prepareError(JSONResponse.get(TemenosConstants.ERRMSG).toString()).log();
			String errorMessage = JSONResponse.get(TemenosConstants.ERRMESSAGE).toString();
		    return ErrorCodeEnum.ERR_20041.setErrorCode(result, errorMessage);
		}else if(JSONResponse.has(TemenosConstants.CODE)){
			alert.prepareError(JSONResponse.get(TemenosConstants.MESSAGE).toString()).log();
			String message = JSONResponse.get(TemenosConstants.MESSAGE).toString();
		    return ErrorCodeEnum.ERR_20041.setErrorCode(result,message);
		}else {
	    result = JSONToResult.convert(JSONResponse.toString());
	    return result;
		}
	}
}
