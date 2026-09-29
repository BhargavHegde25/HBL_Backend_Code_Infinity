/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.CorporatePayeesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.CorporatePayeesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.CorporatePayeesDto;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.*;
import java.util.stream.Collectors;

public class CorporatePayeesBusinessDelegateImpl implements CorporatePayeesBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CorporatePayeesBackendDelegate corporatePayeesBackendDelegate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(CorporatePayeesBackendDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	public List<CorporatePayeesDto> getCorporatePayees(Map<String, List<String>> inputCifMap,HashMap inputParams, DataControllerRequest request)
			throws Exception {

		String select = "name, address1, address2, city, state, country, zipcode";
		return corporatePayeesBackendDelegate.getCorporatePayees(inputCifMap, inputParams, select, null, request);
	}

	public String createCorporatePayee(HashMap corporatePayeesDto, DataControllerRequest request) throws Exception {

		return corporatePayeesBackendDelegate.createCorporatePayee(corporatePayeesDto, request);
	}

	public String editExternalPayee(HashMap<String, String> inputParams, DataControllerRequest request){
		CorporatePayeesBackendDelegate corporatePayeesBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CorporatePayeesBackendDelegate.class);
		_getExternalPayeeMap(inputParams);
		return corporatePayeesBackendDelegate.editExternalPayee(inputParams, request);
	}

	@Override
	public String editCorporatePayee(HashMap inputParams, DataControllerRequest request) throws Exception {

		List<String> featureActions = new ArrayList<>();
		featureActions.add(TradeFinanceConstants.TRADEFINANCE_PAYEE_CREATE);
		Map<String, List<String>> inputCifMap = authorizationChecksBusinessDelegate.getAuthorizedCifs(featureActions, request.getHeaderMap(), request);
		if(inputCifMap == null || inputCifMap.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return "Unauthorized";
		}
		String name = (String) inputParams.getOrDefault("previousName","");
		if(StringUtils.isBlank(name)){
			return "Failed";
		}
		Set<String> payeeIds = _getPayeeIdsByName(name, inputCifMap, request);
		try {
			for (String id : payeeIds) {
				inputParams.put("id", id);
				String response = corporatePayeesBackendDelegate.editCorporatePayee(inputParams, request);
				if(response.equalsIgnoreCase("failed"))
					return "Failed";
			}
		}
		catch (Exception e){
			alert.prepareError("Error occurred while editing payee.", e).log();
		}
		return "success";
	}

	private void _getExternalPayeeMap(HashMap<String, String> inputParams){
		inputParams.put("Id", inputParams.get("id"));
		inputParams.put("beneficiaryName", inputParams.get("name"));
		inputParams.put("addressLine1", inputParams.get("address1"));
		inputParams.put("addressLine2", inputParams.get("address2"));
	}

	private Set<String> _getPayeeIdsByName(String name, Map<String, List<String>> inputCifMap, DataControllerRequest request) throws Exception {
		String select = "id";
		String filter = DBPUtilitiesConstants.AND + "name" + DBPUtilitiesConstants.EQUAL + name;
		List<CorporatePayeesDto> payeesDtoList = corporatePayeesBackendDelegate.getCorporatePayees(inputCifMap, new HashMap(), select, filter, request);
		return payeesDtoList.stream().map(CorporatePayeesDto::getId).collect(Collectors.toSet());
	}
}
