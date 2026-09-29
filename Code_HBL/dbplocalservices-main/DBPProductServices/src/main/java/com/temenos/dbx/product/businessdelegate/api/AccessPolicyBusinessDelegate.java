package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.DBXResult;

/**
 * 
 * @author sowmya.vandanapu
 *
 */
public interface AccessPolicyBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param headersMap
     * @return access policies master data
     * @throws ApplicationException
     */
    public DBXResult getAccessPolicies(Map<String, Object> headersMap) throws ApplicationException;
}
