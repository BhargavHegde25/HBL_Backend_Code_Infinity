package com.bct.custom.businessdeligate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.FavoriteMerchantBusinessDelegate;
import com.bct.custom.dto.FavoriteMerchantDTO;
import com.bct.custom.resource.impl.FavoriteMerchantManageResourceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class FavoriteMerchantBusinessDelegateImpl implements FavoriteMerchantBusinessDelegate{
	private static final Logger LOG = LogManager.getLogger(FavoriteMerchantBusinessDelegateImpl.class);

	@Override
	public JSONArray getFavoriteMerchants(String input, Map<String, Object> inputArray, DataControllerRequest dcRequest)
			throws ApplicationException {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		JSONArray array = new JSONArray();
		Result response;
		try {
			response = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, null,
					HelperMethods.getHeaders(dcRequest), URLConstants.PAYEES_OS_GET);
			LOG.debug("HBL:FavoriteMerchantBusinessDelegateImpl:getFavoriteMerchants:errMsg:"+response.getErrMsgParamValue());
			LOG.debug("HBL:FavoriteMerchantBusinessDelegateImpl:getFavoriteMerchants:PayeeDataSet:"+response.getDatasetById("Payee"));
		if (HelperMethods.hasRecords(response)) {
			Dataset payees = response.getDatasetById("Payee");
			if (null != payees) {
				// Converting dataset to json array
				 array = processFavoriteMerchants(ResultToJSON.convertDataset(payees));
			}
		}
		} catch (HttpCallException e) {
			LOG.error("Error: HBL:FavoriteMerchantBusinessDelegateImpl:getFavoriteMerchants:exception:"+e.getLocalizedMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_12057,"Something went wrong");
		}
		return array;
	}

	@Override
	public Result createFavoriteMerchant(FavoriteMerchantDTO favoriteMerchant, Map<String, Object> inputParams,
			DataControllerRequest dcRequest) throws ApplicationException {
		Result response= null;
		String url="favorite_merchant_create";
		JSONObject contractCustomer = getContractCustomers(dcRequest);
		Map<String, Object> payload = CreatePayeePayload(favoriteMerchant, contractCustomer);
		LOG.debug("HBL:FavoriteMerchantBusinessDelegateImpl:createFavoriteMerchant:payload:"+payload.toString());
		try {
			response = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, payload,
					HelperMethods.getHeaders(dcRequest),url);
			LOG.debug("HBL:FavoriteMerchantBusinessDelegateImpl:createFavoriteMerchant:errMsg:"+response.getErrMsgParamValue());
			
		} catch (HttpCallException e) {
			LOG.error("Error: HBL:FavoriteMerchantBusinessDelegateImpl:createFavoriteMerchant:exception:"+e.getLocalizedMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_12053,"Something went wrong");
		}
		return response;
	}

	@Override
	public Result updateFavoriteMerchant(FavoriteMerchantDTO favoriteMerchant, Map<String, Object> inputParams,
			DataControllerRequest dcRequest) throws ApplicationException {
		Result response= null;
		String url="favorite_merchant_update";
		try {
			response = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, inputParams,
					HelperMethods.getHeaders(dcRequest),url);
			LOG.debug("HBL:FavoriteMerchantBusinessDelegateImpl:updateFavoriteMerchant:errMsg:"+response.getErrMsgParamValue());
			
		} catch (HttpCallException e) {
			LOG.error("Error: HBL:FavoriteMerchantBusinessDelegateImpl:updateFavoriteMerchant:exception:"+e.getLocalizedMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_12055,"Something went wrong");
		}
		return response;
	}

	@Override
	public Result deleteFavoriteMerchant(Map<String, Object> inputParams, DataControllerRequest dcRequest)
			throws ApplicationException {
		Result response= null;
		String url="favorite_merchant_delete";
		try {
			response = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, inputParams,
					HelperMethods.getHeaders(dcRequest),url);
			LOG.debug("HBL:FavoriteMerchantBusinessDelegateImpl:deleteFavoriteMerchant:errMsg:"+response.getErrMsgParamValue());
			
		} catch (HttpCallException e) {
			LOG.error("Error: HBL:FavoriteMerchantBusinessDelegateImpl:deleteFavoriteMerchant:exception:"+e.getLocalizedMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_12055,"Something went wrong");
		}
		return response;
	}
	public Map<String, Object> CreatePayeePayload(FavoriteMerchantDTO favoriteMerchant, JSONObject contractCustomer){
		
		String contractId= contractCustomer.has("contractId")? contractCustomer.getString("contractId"):""; 
		String coreCustomerId= contractCustomer.has("coreCustomerId")? contractCustomer.getString("coreCustomerId"):""; 
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("accountNumber", favoriteMerchant.getAccountNumber());
		payload.put("payeeNickName", favoriteMerchant.getPayeeNickName());
		payload.put("companyName", favoriteMerchant.getCompanyName());
		payload.put("isManuallyAdded", favoriteMerchant.getIsManuallyAdded());
		payload.put("billerId", favoriteMerchant.getBillerId());
		JSONArray array = new JSONArray();
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("contractId", contractId);
		jsonObj.put("coreCustomerId", coreCustomerId);
		array.put(jsonObj);
		payload.put("bankAddressLine1", favoriteMerchant.getPaymentAggregator());
		payload.put("bankAddressLine2", favoriteMerchant.getLogoUrl());
		payload.put("cif", array.toString());
		return payload;
		
	}
	public JSONArray processFavoriteMerchants(JSONArray responseArray) {
		JSONArray array = new JSONArray();
		for(int i=0;i<responseArray.length();i++) {
			if(responseArray.getJSONObject(i).getString("isManuallyAdded")!=null) {
				Boolean isFavoriteMerchant= Boolean.valueOf(responseArray.getJSONObject(i).getString("isManuallyAdded"));
				if(isFavoriteMerchant==true) {
					array.put(responseArray.getJSONObject(i));
				}
			}
		}
		return array;
	}
	private JSONObject getContractCustomers(DataControllerRequest request) {
		String customerId = CommonUtils.getUserAttributeFromIdentity(request, Constants.PARAM_USER_ID);
		JSONObject customerInfo = new JSONObject();
		if(StringUtils.isNotBlank(customerId)) {
		try {

			String filter = CommonUtils.buildOdataCondition("customerId", Constants.EQUAL, customerId);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			LOG.debug("getContractCustomers contractcustomers:filter:" + filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CONTRACT_CUSTOMERS_GET, false);
			Dataset contractcustomers = result.getDatasetById("contractcustomers");
			
			if (null != contractcustomers) {
				//customerid = contractcustomers.getRecord(0).getParamValueByName("id");
				// Converting dataset to json array
				JSONArray array = ResultToJSON.convertDataset(contractcustomers);
				customerInfo=array.getJSONObject(0);
			}
			LOG.debug("getContractCustomers contractcustomers:" + customerInfo);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerIDFromUsername for Customer " + customerId);
		}
		}
		return customerInfo;

	}

}
