package com.bct.custom.backenddeligate.api;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.BranchDetails;
import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;


public interface BranchdetailsBackendDelegate extends BackendDelegate {

	public JSONArray getBranchDetails(String input, Map<String, Object> inputmap, DataControllerRequest dcRequest) throws ApplicationException;

	public Result createBranchDetails(ArrayList<BranchDetails> branchDetailsArray, Map<String, String> inputParams,
			DataControllerRequest dcRequest) throws ApplicationException;
	
}
