package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;

public interface BusinessConfigurationBusinessDelegate extends BusinessDelegate {

    public String getAutoApprovalStatus(Map<String, Object> headersMap) throws ApplicationException;

}
