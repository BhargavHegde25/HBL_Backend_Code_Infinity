package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.DashboardDTO;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public interface DashboardBackendDelegate extends BackendDelegate {
    List<DashboardDTO> fetchReceivables(Map<String, Object> inputParams, DataControllerRequest request);

    List<DashboardDTO> fetchPayables(Map<String, Object> inputParams, DataControllerRequest request);

    List<DashboardDTO> fetchAllDetails(Map<String, Object> inputParams, DataControllerRequest request);

    JSONObject fetchTradeDashboardConfiguration(String userId);
    boolean createTradeDashboardConfiguration(Map<String, Object> map);
    boolean updateTradeDashboardConfiguration(Map<String, Object> map);
}
