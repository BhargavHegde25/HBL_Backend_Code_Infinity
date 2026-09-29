package com.kony.externalalerts.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;

public interface ExternalAlertsBusinessDelegate extends BusinessDelegate {

	String getSFAccountId(String customerId);
	void pushToSf(List<Map<String,Object>> inputmap, String sfAccountId);
}
