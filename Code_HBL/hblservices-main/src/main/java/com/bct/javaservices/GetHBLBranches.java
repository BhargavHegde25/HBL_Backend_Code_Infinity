package com.bct.javaservices;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetHBLBranches implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GetHBLBranches.class);

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		LOG.debug("HBL:GetHBLBranches :");
		Result result = new Result();
		Dataset branchRes = getHBLBranches(request);
		if (null != branchRes) {
			result.addDataset(branchRes);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		}else {
			result.setParam(new Param("ErrMsg", "Backend service failed!"));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		}
		

		return result;
	}

	private Dataset getHBLBranches(DataControllerRequest request) {
		Dataset branchDataset = null;
		try {
			
			String filter =	"status" + DBPUtilitiesConstants.EQUAL + "1" ;
			LOG.debug("filter##:" + filter);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			//Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
			//		Constants.DBX_DB_SERVICE_NAME, "dbxdb_cardLimit_get", false);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					"dbpRbLocalServicesdb", "dbxdb_hblBranchList_get", false);
			//limitDataset = result.getDatasetById("cardLimit");
			branchDataset = result.getDatasetById("hblBranchList");
			if (null != branchDataset) {
				return branchDataset;
			} else {
				LOG.debug("Else getHBLBranches:");
				return branchDataset;
			}
		} catch (Exception e) {
			LOG.error("Error in getHBLBranches" );
		}
		return branchDataset;
	}
	
}
