package com.hbl.productservicesExtn.api;
import org.json.JSONArray;

import com.hbl.productservicesExtn.dto.LimitsDTOExtn;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface ContractBusinessDelegateExtn extends ContractBusinessDelegate{
	public LimitsDTOExtn fetchAllLimits(String contractId, String coreCustomerId, String actionId, String legalEntityId);
	public JSONArray fetchContractActionLimits(String contractId, String coreCustomerId, String actionId);
	public LimitsDTOExtn fetchExhaustedLimits(String contractId, String coreCustomerId, String featureActionID, String date);
	public LimitsDTOExtn fetchExhaustedLimits(String contractId, String coreCustomerId, String featureActionID, String date, String channel);
}
