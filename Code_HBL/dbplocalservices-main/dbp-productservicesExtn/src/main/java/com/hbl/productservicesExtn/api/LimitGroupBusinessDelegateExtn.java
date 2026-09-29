package com.hbl.productservicesExtn.api;

import java.util.List;

import com.hbl.productservicesExtn.dto.LimitsDTOExtn;
import com.temenos.dbx.product.commons.businessdelegate.api.LimitGroupBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitGroupDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface LimitGroupBusinessDelegateExtn extends LimitGroupBusinessDelegate{

public List<LimitGroupDTO> fetchLimitGroups();
	
	public LimitsDTOExtn fetchLimits(String customerId, String contractId, String coreCustomerId, String limitGroupId);

	public LimitsDTOExtn fetchExhaustedLimits(String contractId, String coreCustomerId, String customerId, String featureActionID, String date);
	
}
