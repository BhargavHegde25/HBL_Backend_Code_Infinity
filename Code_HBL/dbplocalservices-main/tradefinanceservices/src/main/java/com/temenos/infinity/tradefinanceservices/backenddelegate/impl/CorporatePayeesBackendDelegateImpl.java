/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.CorporatePayeesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices;
import com.temenos.infinity.tradefinanceservices.dto.CorporatePayeesDto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class CorporatePayeesBackendDelegateImpl implements CorporatePayeesBackendDelegate {

	public List<CorporatePayeesDto> getCorporatePayees(Map<String, List<String>> inputCifMap, HashMap inputParams, String select, String externalFilter, DataControllerRequest request)
            throws Exception {
		List<CorporatePayeesDto> dtos;
		Set<String> associatedCifs = getContractCifs(inputCifMap);
		Map<String, Object> requestParams = new HashMap<>();
		StringBuilder filter = new StringBuilder();
		filter.append("(");
		for (String cif : associatedCifs) {
			if(filter.length() == 1) {
				filter.append("cif" + DBPUtilitiesConstants.EQUAL).append(cif);// new StringBuilder("cif" + DBPUtilitiesConstants.EQUAL + cif);
			} else
				filter.append(DBPUtilitiesConstants.OR).append("cif").append(DBPUtilitiesConstants.EQUAL).append(cif);
		}
		filter.append(")");
		if(externalFilter!=null)
			filter.append(externalFilter);

		requestParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
		requestParams.put("$select", select);

		String fetchResponse = DBPServiceExecutorBuilder.builder().
				withServiceId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_PAYEE_GET.getServiceName()).
				withObjectId(null).
				withOperationId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_PAYEE_GET.getOperationName()).
				withRequestParameters(requestParams).
				build().getResponse();

		JSONObject jsonRsponse = new JSONObject(fetchResponse);
		JSONArray countJsonArray = jsonRsponse.getJSONArray("corporatepayees");
		dtos = JSONUtils.parseAsList(countJsonArray.toString(), CorporatePayeesDto.class);

		return dtos;

	}
	public String createCorporatePayee(HashMap corporatePayeeMap, DataControllerRequest request) throws Exception {
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		corporatePayeeMap.put("customerId",CustomerSession.getCustomerId(customer));
		JSONObject payee;
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_PAYEE_CREATE.getServiceName()).
					withObjectId(null).
					withOperationId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_PAYEE_CREATE.getOperationName()).
					withRequestParameters(corporatePayeeMap).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray jsonArray = jsonRsponse.getJSONArray("corporatepayees");
			payee= jsonArray.getJSONObject(0);
		}
		catch (Exception e){
			return "Failed";
		}
		return (String) payee.get("id");
	}
	public String editExternalPayee(HashMap externalPayeeMap, DataControllerRequest request) {
		JSONObject payee;
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_PAYEMENT_PAYEE_UPDATE.getServiceName()).
					withObjectId(null).
					withOperationId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_PAYEMENT_PAYEE_UPDATE.getOperationName()).
					withRequestParameters(externalPayeeMap).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(fetchResponse);
			JSONArray jsonArray = jsonResponse.getJSONArray("externalaccount");
			payee= jsonArray.getJSONObject(0);
		}
		catch (Exception e){
			return "Failed";
		}
		return (String) payee.get("Id");
	}

	@Override
	public String editCorporatePayee(HashMap inputParams, DataControllerRequest request) {
		JSONObject payee;
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_PAYEE_UPDATE.getServiceName()).
					withObjectId(null).
					withOperationId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_PAYEE_UPDATE.getOperationName()).
					withRequestParameters(inputParams).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(fetchResponse);
			JSONArray jsonArray = jsonResponse.getJSONArray("corporatepayees");
			payee= jsonArray.getJSONObject(0);
		}
		catch (Exception e){
			return "Failed";
		}
		return (String) payee.get("id");
	}

	private Set<String> getContractCifs(Map<String, List<String>> cifsMap){
		Set<String> associatedCifs = new HashSet<>();
		for(String key : cifsMap.keySet()){
			associatedCifs.addAll(cifsMap.get(key));
		}
		return associatedCifs;
	}
}
