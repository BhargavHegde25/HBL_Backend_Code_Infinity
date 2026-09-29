package com.temenos.infinity.api.arrangements.businessdelegate.api;

import java.util.List;

import org.json.JSONObject;
import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface GetAccountsBySearchBusinessDelegate extends BusinessDelegate {

    List<ArrangementsDTO> getAccounts(String filterKey, String filterValue, String authToken)
            throws ApplicationException;

    JSONObject getAccountsByCoreCustomerIdSearch(String coreCustomerId, String authToken)
            throws ApplicationException;

}
