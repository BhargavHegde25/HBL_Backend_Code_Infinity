package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;

public interface SystemConfigurationBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param key
     * @param headersMap
     * @return
     */
    public String getSystemConfigurationValue(String key, Map<String, Object> headersMap);
}
