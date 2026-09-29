package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;

/**
 * 
 * @author DBX Team
 *
 */
public interface KMSBusinessDelegate extends BusinessDelegate {

    /**
     * Sends Email 
     * @param inputParams
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public JsonObject sendKMSEmail(Map<String, Object> inputParams, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * sends SMS
     * @param inputParams
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public JsonObject sendKMSSMS(Map<String, Object> inputParams, Map<String, Object> headersMap)
            throws ApplicationException;

}
