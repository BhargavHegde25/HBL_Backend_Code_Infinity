package com.temenos.dbx.eum.product.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.eum.product.resource.api.CustomerResource;
import com.temenos.dbx.eum.product.resource.impl.CustomerResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomRoleResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerAddressResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerCommunicationResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerIdentityAttributesResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerImageResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerPreferenceResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerSecurityQuestionsResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.FeedBackStatusResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.PushExternalEventResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.UserManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.CustomRoleResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.CustomerAddressResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.CustomerCommunicationResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.CustomerIdentityAttributesResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.CustomerImageResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.CustomerPreferenceResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.CustomerSecurityQuestionsResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.FeedbackStatusResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.InfinityUserManagementResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.PushExternalEventResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.UserManagementResourceImpl;
import com.temenos.dbx.product.resource.api.BankResource;
import com.temenos.dbx.product.resource.impl.BankResourceImpl;


public class ProductResourceMapper implements DBPAPIMapper<Resource> {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        map.put(UserManagementResource.class, UserManagementResourceImpl.class);

        map.put(CustomerImageResource.class, CustomerImageResourceImpl.class);

        map.put(CustomerCommunicationResource.class, CustomerCommunicationResourceImpl.class);

        map.put(CustomerAddressResource.class, CustomerAddressResourceImpl.class);

        map.put(CustomRoleResource.class, CustomRoleResourceImpl.class);

        map.put(CustomerPreferenceResource.class, CustomerPreferenceResourceImpl.class);

        map.put(CustomerSecurityQuestionsResource.class, CustomerSecurityQuestionsResourceImpl.class);

        map.put(FeedBackStatusResource.class, FeedbackStatusResourceImpl.class);

        map.put(UserManagementResource.class, UserManagementResourceImpl.class);

        map.put(BankResource.class, BankResourceImpl.class);

        map.put(CustomerIdentityAttributesResource.class, CustomerIdentityAttributesResourceImpl.class);

        map.put(PushExternalEventResource.class, PushExternalEventResourceImpl.class);
        map.put(InfinityUserManagementResource.class, InfinityUserManagementResourceImpl.class);
        map.put(CustomerResource.class, CustomerResourceImpl.class);
        return map;
    }
}
