package com.hbl.backenddeligate.api;

import java.util.Map;

import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api.LimitsAndPermissionsBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;

public interface LimitsAndPermissionsBackendDelegateExtn extends LimitsAndPermissionsBackendDelegate{
	public boolean addActionsToCustomer(ActionLimitsDTO actionLimit, Map<String, Object> headerMap);
	public boolean addActionsToCustomRole(ActionLimitsDTO actionLimit, Map<String, Object> headerMap);

}
