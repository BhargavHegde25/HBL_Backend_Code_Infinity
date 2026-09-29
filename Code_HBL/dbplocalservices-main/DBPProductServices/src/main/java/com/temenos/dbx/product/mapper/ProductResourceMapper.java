package com.temenos.dbx.product.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.organization.resource.api.AuthorizedSignatoriesResource;
import com.temenos.dbx.product.organization.resource.api.MembershipResource;
import com.temenos.dbx.product.organization.resource.api.OrganizationResource;
import com.temenos.dbx.product.organization.resource.impl.AuthorizedSignatoriesResourceImpl;
import com.temenos.dbx.product.organization.resource.impl.MembershipResourceImpl;
import com.temenos.dbx.product.organization.resource.impl.OrganizationResourceImpl;
import com.temenos.dbx.product.resource.api.ApproverResource;
import com.temenos.dbx.product.resource.api.BusinessTypeResource;
import com.temenos.dbx.product.resource.api.CombinedUserResource;
import com.temenos.dbx.product.resource.api.CustomerResource;
import com.temenos.dbx.product.resource.api.FeatureResource;
import com.temenos.dbx.product.resource.api.OrganisationEmployeesResource;
import com.temenos.dbx.product.resource.api.OrganizationGroupActionLimitsResource;
import com.temenos.dbx.product.resource.api.UpdateProspectResource;
import com.temenos.dbx.product.resource.api.UpgradeProspectToRetailResource;
import com.temenos.dbx.product.resource.impl.ApproverResourceImpl;
import com.temenos.dbx.product.resource.impl.BusinessTypeResourceImpl;
import com.temenos.dbx.product.resource.impl.CombinesUserResourceImpl;
import com.temenos.dbx.product.resource.impl.CustomerResourceImpl;
import com.temenos.dbx.product.resource.impl.FeatureResourceImpl;
import com.temenos.dbx.product.resource.impl.OrganisationEmployeesResourceImpl;
import com.temenos.dbx.product.resource.impl.OrganizationGroupActionLimitsResourceImpl;
import com.temenos.dbx.product.resource.impl.UpdateProspectResourceImpl;
import com.temenos.dbx.product.resource.impl.UpgradeProspectToRetailResourceImpl;
import com.temenos.infinity.dms.resource.api.DMSResource;
import com.temenos.infinity.dms.resource.impl.DMSResourceImpl;

public class ProductResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        map.put(CustomerResource.class, CustomerResourceImpl.class);

        map.put(ApproverResource.class, ApproverResourceImpl.class);

        map.put(OrganizationGroupActionLimitsResource.class, OrganizationGroupActionLimitsResourceImpl.class);

        map.put(UpdateProspectResource.class, UpdateProspectResourceImpl.class);

        map.put(UpgradeProspectToRetailResource.class, UpgradeProspectToRetailResourceImpl.class);

        map.put(OrganisationEmployeesResource.class, OrganisationEmployeesResourceImpl.class);

        map.put(OrganizationResource.class, OrganizationResourceImpl.class);

        map.put(FeatureResource.class, FeatureResourceImpl.class);

        map.put(BusinessTypeResource.class, BusinessTypeResourceImpl.class);

        map.put(AuthorizedSignatoriesResource.class, AuthorizedSignatoriesResourceImpl.class);

        map.put(MembershipResource.class, MembershipResourceImpl.class);

        map.put(CombinedUserResource.class, CombinesUserResourceImpl.class);

        map.put(DMSResource.class, DMSResourceImpl.class);

        return map;
    }
}
