package com.temenos.msArrangement.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.msArrangement.dto.ArrangementsDTO;


/**
 * 
 * @author smugesh 
 * @version 1.0
 * Interface for AccountsResource extends {@link BusinessDelegate}
 *
 */
public interface ArrangementsBusinessDelegate extends BusinessDelegate {

    
    /**
     *  method to get the Arrangements
     *  return JSONObject of accounts
     */
    List<ArrangementsDTO> getArrangements(String partyId, String productLineId);

}
