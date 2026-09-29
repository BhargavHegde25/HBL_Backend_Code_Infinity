package com.bct.custom.businessdeligate.impl;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.BillPaymentHistoryBackendDeligate;
import com.bct.custom.backenddeligate.api.BranchdetailsBackendDelegate;
import com.bct.custom.businessdeligate.api.BranchDetailsBusinessDelegate;
import com.bct.custom.dto.BranchDetails;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class BranchDetailsBusinessDeligateImpl implements BranchDetailsBusinessDelegate{

	@Override
	public JSONArray getBranchDetails(String input, Map<String, Object> inputArray, DataControllerRequest dcRequest)
			throws ApplicationException {
		BranchdetailsBackendDelegate backendDeligate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(BranchdetailsBackendDelegate.class);
				return backendDeligate.getBranchDetails(input, inputArray, dcRequest);
	}

	@Override
	public Result createBranchDetails(ArrayList<BranchDetails> branchDetailsArray, Map<String, String> inputParams,
			DataControllerRequest dcRequest) throws ApplicationException {
		BranchdetailsBackendDelegate backendDeligate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(BranchdetailsBackendDelegate.class);
				return backendDeligate.createBranchDetails(branchDetailsArray, inputParams, dcRequest);
	}

}
