package com.temenos.infinity.api.arrangements.backenddelegate.api;

import java.util.List;
import org.json.JSONObject;
import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface GetAccountsArrangementsExperienceAPIBackendDelegate extends BackendDelegate {

    List<ArrangementsDTO> getArrangements(String filterKey, String filterValue, String authToken)
            throws ApplicationException;

    JSONObject getCoreCustomerArrangements(String coreCustomerId, String authToken)
            throws ApplicationException;
}
