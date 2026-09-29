package com.temenos.dbx.transaction.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.transaction.businessdelegate.api.SavingsPotBusinessDelegate;
import com.temenos.dbx.transaction.dto.SavingsPotDTO;
import com.temenos.dbx.transaction.resource.api.SavingsPotResource;

public class SavingsPotResourceImpl implements SavingsPotResource{

	@Override
	public Result createSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		SavingsPotBusinessDelegate savingsPotDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SavingsPotBusinessDelegate.class);
		SavingsPotDTO savingsPotDTO = savingsPotDelegate.createSavingsPot(inputParams);
		JSONObject JSONResponse = new JSONObject(savingsPotDTO);
	    result = JSONToResult.convert(JSONResponse.toString());
	    return result;
	}

	@Override
	public Result closeSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		SavingsPotBusinessDelegate savingsPotDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SavingsPotBusinessDelegate.class);
			SavingsPotDTO savingsPotDTO = savingsPotDelegate.closeSavingsPot(inputParams.get("potAccountId").toString());
		JSONObject JSONResponse = new JSONObject(savingsPotDTO);
	    result = JSONToResult.convert(JSONResponse.toString());
	    return result;
	}

	@Override
	public Result updateSavingsPotBalance(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		SavingsPotBusinessDelegate savingsPotDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SavingsPotBusinessDelegate.class);
		SavingsPotDTO savingsPotDTO = savingsPotDelegate.updateSavingsPotBalance(inputParams.get("potAccountId").toString(), inputParams.get("amount").toString(), inputParams.get("isCreditDebit").toString());
		JSONObject JSONResponse = new JSONObject(savingsPotDTO);
	    result = JSONToResult.convert(JSONResponse.toString());
	    return result;
	}

	@Override
	public Result updateSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		SavingsPotBusinessDelegate savingsPotDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SavingsPotBusinessDelegate.class);
		Map<String, Object> updateSavingsPotMap = new HashMap<>();
		updateSavingsPotMap.put("potContractId", inputParams.get("potAccountId"));
		updateSavingsPotMap.put("potName", inputParams.get("potName"));
		updateSavingsPotMap.put("potType", inputParams.get("potType "));
		updateSavingsPotMap.put("startDate", inputParams.get("startDate"));
		updateSavingsPotMap.put("endDate", inputParams.get("endDate"));
		updateSavingsPotMap.put("frequency", inputParams.get("frequency"));
		updateSavingsPotMap.put("targetAmount", inputParams.get("targetAmount"));
		updateSavingsPotMap.put("targetPeriod", inputParams.get("targetPeriod"));
		updateSavingsPotMap.put("scheduledPaymentAmount", inputParams.get("periodicContribution"));
		SavingsPotDTO savingsPotDTO = savingsPotDelegate.updateSavingsPot(updateSavingsPotMap);
				//updateSavingsPotBalance(, inputParams.get("amount").toString(), inputParams.get("isCreditDebit").toString());
		JSONObject JSONResponse = new JSONObject(savingsPotDTO);
	    result = JSONToResult.convert(JSONResponse.toString());
	    return result;
	}

}
