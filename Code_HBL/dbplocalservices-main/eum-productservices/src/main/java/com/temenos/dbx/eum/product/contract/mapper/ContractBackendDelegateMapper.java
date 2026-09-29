package com.temenos.dbx.eum.product.contract.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractAccountBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractAddressBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCommunicationBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractFeatureActionsBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ExcludedContractAccountsBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ServiceDefinitionBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ContractAccountBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ContractAddressBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ContractBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ContractCommunicationBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ContractCoreCustomerBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ContractFeatureActionsBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.CoreCustomerBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ExcludedContractAccountsBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ServiceDefinitionBackendDelegateImpl;

public class ContractBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        map.put(ContractBackendDelegate.class, ContractBackendDelegateImpl.class);

        map.put(ContractFeatureActionsBackendDelegate.class, ContractFeatureActionsBackendDelegateImpl.class);

        map.put(ContractAccountBackendDelegate.class, ContractAccountBackendDelegateImpl.class);

        map.put(ContractAddressBackendDelegate.class, ContractAddressBackendDelegateImpl.class);

        map.put(ContractCommunicationBackendDelegate.class, ContractCommunicationBackendDelegateImpl.class);

        map.put(ContractCoreCustomerBackendDelegate.class, ContractCoreCustomerBackendDelegateImpl.class);

        map.put(ServiceDefinitionBackendDelegate.class, ServiceDefinitionBackendDelegateImpl.class);

        map.put(ExcludedContractAccountsBackendDelegate.class, ExcludedContractAccountsBackendDelegateImpl.class);
        return map;
    }

}
