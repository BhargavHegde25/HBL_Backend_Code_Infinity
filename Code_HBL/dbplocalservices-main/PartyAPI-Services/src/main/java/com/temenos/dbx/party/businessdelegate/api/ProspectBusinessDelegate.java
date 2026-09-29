package com.temenos.dbx.party.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;

public interface ProspectBusinessDelegate extends BusinessDelegate {

    DBXResult saveProspect(PartyDTO partyDTO, Map<String, Object> map);

    DBXResult updateProspect(PartyDTO partyDTO, Map<String, Object> map);
    
}
