package com.temenos.dbx.product.organization.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.businessdelegate.api.OrganizationEmployeesBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.impl.OrganizationEmployeesBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.api.AuthorizedSignatoriesBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.BusinessSignatoryBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationAddressBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationCommunicationBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationFeaturesActionsBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationMembershipBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.SignatoryTypeBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.impl.AuthorizedSignatoriesBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.impl.BusinessSignatoryBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.impl.OrganizationAddressBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.impl.OrganizationBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.impl.OrganizationCommunicationBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.impl.OrganizationFeaturesActionsBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.impl.OrganizationMembershipBusinessDelegateImpl;
import com.temenos.dbx.product.organization.businessdelegate.impl.SignatoryTypeBusinessDelegateImpl;

/**
 * 
 * @author Infinity DBX
 *
 */
public class OrganizationBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		map.put(OrganizationBusinessDelegate.class, OrganizationBusinessDelegateImpl.class);

		map.put(AuthorizedSignatoriesBusinessDelegate.class, AuthorizedSignatoriesBusinessDelegateImpl.class);

		map.put(OrganizationMembershipBusinessDelegate.class, OrganizationMembershipBusinessDelegateImpl.class);

		map.put(OrganizationFeaturesActionsBusinessDelegate.class,
				OrganizationFeaturesActionsBusinessDelegateImpl.class);

		map.put(OrganizationAddressBusinessDelegate.class, OrganizationAddressBusinessDelegateImpl.class);

		map.put(OrganizationCommunicationBusinessDelegate.class, OrganizationCommunicationBusinessDelegateImpl.class);

		map.put(BusinessSignatoryBusinessDelegate.class, BusinessSignatoryBusinessDelegateImpl.class);

		map.put(SignatoryTypeBusinessDelegate.class, SignatoryTypeBusinessDelegateImpl.class);

		map.put(OrganizationEmployeesBusinessDelegate.class, OrganizationEmployeesBusinessDelegateImpl.class);
		
		return map;
	}

}
