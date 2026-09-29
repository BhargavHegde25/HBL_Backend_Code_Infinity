package com.kony.scaintegration.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.dataobject.Result;

public interface UpdateVerifyFlagBusinessDelegate extends BusinessDelegate {

	public Result updateFlag(String serviceKey, String customerId);

}
