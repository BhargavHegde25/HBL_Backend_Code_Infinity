package com.temenos.dbx.usermanagement.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.ProfileManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.ProfileManagementBusinessDelegateImpl;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;

public class PartyProfileManagementBusinessDelegateImpl implements ProfileManagementBusinessDelegate{

	@Override
	public DBXResult getCustomerForUserResponse(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.getCustomerForUserResponse(customerDTO,headerMap);
	}

	@Override
	public DBXResult getUserResponse(CustomerDTO customerDTO, Map<String, Object> map) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.getUserResponse(customerDTO,map);
	}

	@Override
	public DBXResult createCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.createCustomer(customerDTO,headerMap);
	}

	@Override
	public DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.updateCustomer(customerDTO,headerMap);
	}

	@Override
	public DBXResult searchCustomer(Map<String, String> configurations, MemberSearchBean memberSearchBean, Map<String, Object> headerMap, PasswordHistoryManagement pm) {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.
				getBackendDelegate(ProfileManagementBackendDelegate.class);
		return backendDelegate.searchCustomer(configurations, memberSearchBean, headerMap , pm);
	}

	@Override
	public DBXResult getBasicInformation(Map<String, String> configurations, CustomerDTO customerDTO, Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm) {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.
				getBackendDelegate(ProfileManagementBackendDelegate.class);
		return backendDelegate.getBasicInformation(configurations, customerDTO, headerMap, isCustomerSearch, pm);
	}

	@Override
	public DBXResult getCustomerDetailsToAdmin(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.getCustomerDetailsToAdmin(customerDTO,headerMap);
	}

	@Override
	public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.get(customerDTO,headerMap);
	}

	@Override
	public DBXResult checkifUserEnrolled(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.checkifUserEnrolled(customerDTO,headerMap);
	}

	@Override
	public DBXResult sendCustomerUnlockEmail(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.sendCustomerUnlockEmail(customerDTO,headerMap);
	}

	@Override
	public DBXResult getAddressTypes(Map<String, Object> map) {
		ProfileManagementBusinessDelegateImpl businessDelegate = new ProfileManagementBusinessDelegateImpl();
        return businessDelegate.getAddressTypes(map);
	}
	
	 @Override
	    public DBXResult userIdSearch(String userName, Map<String, Object> headerMap)
	            throws ApplicationException {
	        	ProfileManagementBackendDelegate backendDelegate =
	                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
	            return backendDelegate.userIdSearch(userName, headerMap);
	        
	    }

	@Override
	public DBXResult customerLegalEntitiesGet(CustomerDTO customerDTO, Map<String, Object> headerMap)
			throws ApplicationException {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.
				getBackendDelegate(ProfileManagementBackendDelegate.class);
		return backendDelegate.customerLegalEntitiesGet(customerDTO, headerMap);
	}

}