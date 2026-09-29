package com.temenos.dbx.core.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.core.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.core.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;

public class CoreCustomerBusinessDelegateImpl implements CoreCustomerBusinessDelegate {

    @Override
    public DBXResult saveCustomerFromParty(PartyDTO partyDTO, Map<String, Object> inputParams, String coreURL) {

       CoreCustomerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
       return backendDelegate.saveCustomerFromParty(partyDTO, inputParams, coreURL);
       
    }


    @Override
    public DBXResult updateCustomerfromParty(PartyDTO partyDTO, Map<String, Object> inputParams,String coreURL) {
        CoreCustomerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
        return backendDelegate.updateCustomerfromParty(partyDTO, inputParams, coreURL);
    }


    @Override
    public DBXResult saveCustomerFromDBX(CustomerDTO customerDTO, Map<String, Object> map, String coreURL) {
        CoreCustomerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
        return backendDelegate.saveCustomerFromDBX(customerDTO, map, coreURL);
    }

    @Override
    public DBXResult updateCustomerfromDBX(CustomerDTO customerDTO, Map<String, Object> map, String coreURL) {
        CoreCustomerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
        return backendDelegate.updateCustomerfromDBX(customerDTO, map, coreURL);
    }


    @Override
    public DBXResult activateCustomer(CustomerDTO customerDTO, Map<String, Object> map, String coreURL) {
        CoreCustomerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
        return backendDelegate.activateCustomer(customerDTO, map, coreURL);
    }


}
