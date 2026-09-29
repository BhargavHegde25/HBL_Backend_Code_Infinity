package com.temenos.dbx.product.usermanagement.resource.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerAddressViewDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerAddressResource;
import com.temenos.dbx.product.usermanagement.resource.api.OrganizationUserManagementResource;

public class OrganizationManagementResourceImpl implements OrganizationUserManagementResource {

    @Override
    public Result checkIfMemberExists(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
       
        
        
        
        return result;
    }

}
