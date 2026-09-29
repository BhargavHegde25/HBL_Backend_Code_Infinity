package com.hbl.productservicesExtn.api;

import org.json.JSONArray;

import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface FeatureActionBusinessDelegateExtn extends FeatureActionBusinessDelegate{
	public LimitsDTO fetchLimits(String actionId, String legalEntityId);
	public JSONArray fetchAllLimits(String actionId, String legalEntityId);
}
