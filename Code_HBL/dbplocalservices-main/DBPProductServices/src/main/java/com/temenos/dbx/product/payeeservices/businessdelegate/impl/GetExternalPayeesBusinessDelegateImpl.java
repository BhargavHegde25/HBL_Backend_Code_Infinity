package com.temenos.dbx.product.payeeservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.GetExternalPayeesBackendDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.GetExternalPayeesBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.ExternalPayeeListDTO;

import java.util.List;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public class GetExternalPayeesBusinessDelegateImpl implements GetExternalPayeesBusinessDelegate {

    public List<ExternalPayeeListDTO> fetchPayees(Map<String, Object> requestParameters, DataControllerRequest dcRequest){

        GetExternalPayeesBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(
                GetExternalPayeesBackendDelegate.class);

        return backendDelegate.fetchPayees(requestParameters, dcRequest);

    }
}
