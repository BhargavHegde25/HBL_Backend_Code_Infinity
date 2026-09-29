package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerDTO;

public interface CustomerIdentityAttributesBusinessDelegate extends BusinessDelegate {

    public JsonObject getUserAttributes(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    public JsonObject getSecurityAttributes(CustomerDTO customerDTO, Map<String, Object> headerMap,String legalEntityId)
            throws ApplicationException;
}
