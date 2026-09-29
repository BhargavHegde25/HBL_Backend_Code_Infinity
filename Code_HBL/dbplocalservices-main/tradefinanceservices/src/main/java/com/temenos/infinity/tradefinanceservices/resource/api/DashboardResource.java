package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

import java.util.Map;

/**
 * @author naveen.yerra
 */
public interface DashboardResource extends Resource {
    Result fetchReceivables(Map<String, Object> inputParams, DataControllerRequest request);

    Result fetchPayables(Map<String, Object> inputParams, DataControllerRequest request);

    Result fetchAllDetails(Map<String, Object> inputParams, DataControllerRequest request);
    Result fetchTradeFinanceConfiguration(Map<String, Object> inputParams, DataControllerRequest request);
    Result createTradeFinanceConfiguration(Map<String, Object> inputParams, DataControllerRequest request);
    Result updateTradeFinanceConfiguration(Map<String, Object> inputParams, DataControllerRequest request);

}
