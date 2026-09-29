package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.ProfileManagementBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.ProfileManagementBusinessDelegateImpl;

public class ProfileManagementBusinessDelegateImpl implements ProfileManagementBusinessDelegate {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public DBXResult getCustomerForUserResponse(CustomerDTO customerDTO, Map<String, Object> headerMap) {

		DBXResult dbxResult = new DBXResult();
		try {
			ProfileManagementBackendDelegate backendDelegate  = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ProfileManagementBackendDelegate.class);
			dbxResult = backendDelegate.getCustomerForUserResponse(customerDTO, headerMap);
			
			if (dbxResult.getResponse() != null) {
	            Map<String, String> map = (Map<String, String>) dbxResult.getResponse();
	            if(StringUtils.isBlank(map.get("isQRPaymentActivated"))) {
	            	map.put("isQRPaymentActivated", "false");
	            }
			}
			return dbxResult;
		} catch (Exception e) {
			alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
			// TODO: assign the error code and error message
			dbxResult.setDbpErrCode("");
			dbxResult.setDbpErrMsg("");
		}
		return dbxResult;
	}

	@Override
	public DBXResult getUserResponse(CustomerDTO customerDTO, Map<String, Object> map) {

		DBXResult dbxResult = new DBXResult();
		try {
			ProfileManagementBackendDelegate backendDelegate  = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ProfileManagementBackendDelegate.class);
			return backendDelegate.getUserResponse(customerDTO, map);
		} catch (Exception e) {
			alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
			// TODO: assign the error code and error message
			dbxResult.setDbpErrCode("");
			dbxResult.setDbpErrMsg("");
		}
		return dbxResult;
	}

	@Override
	public DBXResult createCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
		return backendDelegate.createCustomer(customerDTO, headerMap);
	}

	@Override
	public DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
		return backendDelegate.updateCustomer(customerDTO, headerMap);
	}

	@Override
	public DBXResult searchCustomer(Map<String, String> configurations, MemberSearchBean memberSearchBean, Map<String, Object> headerMap, PasswordHistoryManagement pm) {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
		return backendDelegate.searchCustomer(configurations, memberSearchBean, headerMap, pm);
	}

	@Override
	public DBXResult getBasicInformation(Map<String, String> configurations, CustomerDTO customerDTO, Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm) {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
		return backendDelegate.getBasicInformation(configurations, customerDTO, headerMap, isCustomerSearch, pm);
	}

	@Override
	public DBXResult getCustomerDetailsToAdmin(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class); 
		return backendDelegate.getCustomerDetailsToAdmin(customerDTO, headerMap);
	}

	@Override
	public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class); 
		return backendDelegate.get(customerDTO, headerMap);
	}

    @Override
    public DBXResult checkifUserEnrolled(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class); 
        return backendDelegate.checkifUserEnrolled(customerDTO, headerMap);
    }

    @Override
    public DBXResult sendCustomerUnlockEmail(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class); 
        return backendDelegate.sendCustomerUnlockEmail(customerDTO, headerMap);
    }

    @Override
    public DBXResult getAddressTypes(Map<String, Object> map) {
       
        ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class); 
        return backendDelegate.getAddressTypes(map);
    }
    
    @Override
    public DBXResult userIdSearch(String userName, Map<String, Object> headerMap)
            throws ApplicationException {
        	ProfileManagementBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
            return backendDelegate.userIdSearch(userName, headerMap);
        
    }

	@Override
	public DBXResult customerLegalEntitiesGet(CustomerDTO customerDTO, Map<String, Object> headerMap) throws ApplicationException {
		ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
		return backendDelegate.customerLegalEntitiesGet(customerDTO, headerMap);
	}

}
