package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface CustomerPreferenceBusinessDelegate extends BusinessDelegate {

    public DBXResult update(CustomerPreferenceDTO customerPreferenceDTO, Map<String, Object> headerMap);

    public DBXResult get(CustomerPreferenceDTO customerPreferenceDTO, Map<String, Object> headerMap);

    public DBXResult getPreferencesForUserResponse(CustomerPreferenceDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult getPreferencesForLogin(CustomerPreferenceDTO customerPreferenceDTO, Map<String, Object> headerMap);

    public DBXResult getPreferencesIdentityAttributes(CustomerDTO customerDTO,
            Map<String, Object> headerMap) throws ApplicationException;
}
