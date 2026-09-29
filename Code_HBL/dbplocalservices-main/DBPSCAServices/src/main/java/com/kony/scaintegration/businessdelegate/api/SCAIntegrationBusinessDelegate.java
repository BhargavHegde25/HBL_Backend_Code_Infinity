package com.kony.scaintegration.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.dataobject.Result;

public interface SCAIntegrationBusinessDelegate extends BusinessDelegate {
	
	Result getServiceKey(String payload, String userId);
	Result getRequestPayload(String payload, String userId, boolean includeIsVerifiedFlag);
}
