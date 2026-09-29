package com.temenos.dbx.product.organization.mapper;

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

/**
 * 
 * @author Infinity DBX
 *
 */
public class OrganizationResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		map.put(OrganizationResource.class, OrganizationResourceImpl.class);
		map.put(AuthorizedSignatoriesResource.class, AuthorizedSignatoriesResourceImpl.class);
		map.put(MembershipResource.class, MembershipResourceImpl.class);
		return map;
	}

}
