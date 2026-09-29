package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.DBXResult;

public interface DependentActionsBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param headersMap
     * @return DBXResult.map containing key : actionId value : Set of actions which are dependant actions
     * @throws ApplicationException
     */
    public DBXResult getDependentActions(Map<String, Object> headersMap) throws ApplicationException;
}
