package com.temenos.dbx.product.contract.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractAddressBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCommunicationBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ExcludedContractAccountsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.impl.ContractAccountsBusinessDelegateImpl;
import com.temenos.dbx.product.contract.businessdelegate.impl.ContractAddressBusinessDelegateImpl;
import com.temenos.dbx.product.contract.businessdelegate.impl.ContractBusinessDelegateImpl;
import com.temenos.dbx.product.contract.businessdelegate.impl.ContractCommunicationBusinessDelegateImpl;
import com.temenos.dbx.product.contract.businessdelegate.impl.ContractCoreCustomerBusinessDelegateImpl;
import com.temenos.dbx.product.contract.businessdelegate.impl.ContractFeatureActionsBusinessDelegateImpl;
import com.temenos.dbx.product.contract.businessdelegate.impl.CoreCustomerBusinessDelegateImpl;
import com.temenos.dbx.product.contract.businessdelegate.impl.ExcludedContractAccountsBusinessDelegateImpl;
import com.temenos.dbx.product.contract.businessdelegate.impl.ServiceDefinitionBusinessDelegateImpl;

public class ContractBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {

        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        map.put(ContractBusinessDelegate.class, ContractBusinessDelegateImpl.class);

        map.put(CoreCustomerBusinessDelegate.class, CoreCustomerBusinessDelegateImpl.class);

        map.put(ContractAccountsBusinessDelegate.class, ContractAccountsBusinessDelegateImpl.class);

        map.put(ContractAddressBusinessDelegate.class, ContractAddressBusinessDelegateImpl.class);

        map.put(ContractCommunicationBusinessDelegate.class, ContractCommunicationBusinessDelegateImpl.class);

        map.put(ContractFeatureActionsBusinessDelegate.class, ContractFeatureActionsBusinessDelegateImpl.class);

        map.put(ContractAccountsBusinessDelegate.class, ContractAccountsBusinessDelegateImpl.class);

        map.put(ContractCoreCustomerBusinessDelegate.class, ContractCoreCustomerBusinessDelegateImpl.class);

        map.put(ServiceDefinitionBusinessDelegate.class, ServiceDefinitionBusinessDelegateImpl.class);

        map.put(ExcludedContractAccountsBusinessDelegate.class, ExcludedContractAccountsBusinessDelegateImpl.class);

        return map;
    }

}
