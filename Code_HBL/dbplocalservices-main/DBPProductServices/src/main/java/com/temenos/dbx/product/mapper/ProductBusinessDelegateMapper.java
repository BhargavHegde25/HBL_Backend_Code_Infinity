package com.temenos.dbx.product.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.AccessPolicyBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.ApproverBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.BusinessConfigurationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.BusinessTypeBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.DependentActionsBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.KMSBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.OrganizationEmployeesBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.OrganizationGroupActionLimitBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.SystemConfigurationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.UpdateProspectBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.UpgradeProspectToRetailCustomerBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.impl.AccessPolicyBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.AddressBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.ApplicationBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.ApproverBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.BusinessConfigurationBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.BusinessTypeBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.DependentActionsBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.FeatureBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.KMSBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.OrganizationEmployeesBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.OrganizationGroupActionLimitBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.SystemConfigurationBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.UpdateProspectBusinessDelegateImpl;
import com.temenos.dbx.product.businessdelegate.impl.UpgradeProspectToRetailCustomerBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.api.AuthorizedSignatoriesBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.MembershipBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.impl.AuthorizedSignatoriesBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.impl.MembershipBusinessDelegateImpl;
import com.temenos.infinity.dms.businessdelegate.api.DMSBusinessDelegate;
import com.temenos.infinity.dms.businessdelegate.impl.DMSBusinessDelegateImpl;
import com.temenos.mfa.businessdelegate.impl.MFAServiceBusinessDelegateImpl;

public class ProductBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        map.put(ApproverBusinessDelegate.class, ApproverBusinessDelegateImpl.class);

        map.put(OrganizationGroupActionLimitBusinessDelegate.class,
                OrganizationGroupActionLimitBusinessDelegateImpl.class);

        map.put(UpdateProspectBusinessDelegate.class, UpdateProspectBusinessDelegateImpl.class);

        map.put(UpgradeProspectToRetailCustomerBusinessDelegate.class,
                UpgradeProspectToRetailCustomerBusinessDelegateImpl.class);

        map.put(OrganizationEmployeesBusinessDelegate.class, OrganizationEmployeesBusinessDelegateImpl.class);

        map.put(FeatureBusinessDelegate.class, FeatureBusinessDelegateImpl.class);

        map.put(BusinessTypeBusinessDelegate.class, BusinessTypeBusinessDelegateImpl.class);

        map.put(AuthorizedSignatoriesBusinessDelegate.class, AuthorizedSignatoriesBusinessDelegateImpl.class);

        map.put(MFAServiceBusinessDelegate.class, MFAServiceBusinessDelegateImpl.class);

        map.put(MembershipBusinessDelegate.class, MembershipBusinessDelegateImpl.class);

        map.put(AddressBusinessDelegate.class, AddressBusinessDelegateImpl.class);

        map.put(BusinessConfigurationBusinessDelegate.class, BusinessConfigurationBusinessDelegateImpl.class);

        map.put(ApplicationBusinessDelegate.class, ApplicationBusinessDelegateImpl.class);

        map.put(KMSBusinessDelegate.class, KMSBusinessDelegateImpl.class);

        map.put(SystemConfigurationBusinessDelegate.class, SystemConfigurationBusinessDelegateImpl.class);

        map.put(DMSBusinessDelegate.class, DMSBusinessDelegateImpl.class);

        map.put(DependentActionsBusinessDelegate.class, DependentActionsBusinessDelegateImpl.class);

        map.put(AccessPolicyBusinessDelegate.class, AccessPolicyBusinessDelegateImpl.class);

        return map;
    }

}
