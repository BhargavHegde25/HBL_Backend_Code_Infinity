package com.temenos.dbx.eum.product.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CommunicationBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CredentialCheckerBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomRoleBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerBusinessTypeBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerIdentityAttributesBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerImageBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerSecurityQuestionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.FeedBackStatusBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.PasswordHistoryBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.ProfileManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.PushExternalEventBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.AddressBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.BackendIdentifierBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CommunicationBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CredentialCheckerBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomRoleBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerAccountsBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerActionsBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerBusinessTypeBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerGroupBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerIdentityAttributesBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerImageBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerPreferenceBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerSecurityQuestionsBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.FeedBackStatusBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.InfinityUserManagementBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.PasswordHistoryBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.ProfileManagementBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.PushExternalEventBusinessDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.UserManagementBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.api.BankBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.impl.BankBusinessDelegateImpl;

public class UserManagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        map.put(UserManagementBusinessDelegate.class, UserManagementBusinessDelegateImpl.class);

        map.put(BankBusinessDelegate.class, BankBusinessDelegateImpl.class);

        map.put(CustomerImageBusinessDelegate.class, CustomerImageBusinessDelegateImpl.class);

        map.put(CustomRoleBusinessDelegate.class, CustomRoleBusinessDelegateImpl.class);

        map.put(CustomerAccountsBusinessDelegate.class, CustomerAccountsBusinessDelegateImpl.class);

        map.put(CustomerGroupBusinessDelegate.class, CustomerGroupBusinessDelegateImpl.class);

        map.put(CredentialCheckerBusinessDelegate.class, CredentialCheckerBusinessDelegateImpl.class);

        map.put(CustomerBusinessTypeBusinessDelegate.class, CustomerBusinessTypeBusinessDelegateImpl.class);

        map.put(AddressBusinessDelegate.class, AddressBusinessDelegateImpl.class);

        map.put(CommunicationBusinessDelegate.class, CommunicationBusinessDelegateImpl.class);
        map.put(CustomerPreferenceBusinessDelegate.class, CustomerPreferenceBusinessDelegateImpl.class);
        map.put(CustomerSecurityQuestionsBusinessDelegate.class, CustomerSecurityQuestionsBusinessDelegateImpl.class);
        map.put(FeedBackStatusBusinessDelegate.class, FeedBackStatusBusinessDelegateImpl.class);

        map.put(PasswordHistoryBusinessDelegate.class, PasswordHistoryBusinessDelegateImpl.class);

        map.put(ProfileManagementBusinessDelegate.class, ProfileManagementBusinessDelegateImpl.class);

        map.put(CustomerIdentityAttributesBusinessDelegate.class, CustomerIdentityAttributesBusinessDelegateImpl.class);

        map.put(BackendIdentifierBusinessDelegate.class, BackendIdentifierBusinessDelegateImpl.class);

        map.put(CustomerActionsBusinessDelegate.class, CustomerActionsBusinessDelegateImpl.class);

        map.put(PushExternalEventBusinessDelegate.class, PushExternalEventBusinessDelegateImpl.class);
        map.put(InfinityUserManagementBusinessDelegate.class, InfinityUserManagementBusinessDelegateImpl.class);
        return map;
    }

}
