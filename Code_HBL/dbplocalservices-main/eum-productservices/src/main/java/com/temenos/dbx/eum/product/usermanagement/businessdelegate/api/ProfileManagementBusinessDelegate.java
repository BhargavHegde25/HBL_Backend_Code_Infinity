package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;

public interface ProfileManagementBusinessDelegate extends BusinessDelegate {

	public DBXResult getCustomerForUserResponse(CustomerDTO customerDTO, Map<String, Object> headerMap);

	public DBXResult getUserResponse(CustomerDTO customerDTO, Map<String, Object> map);

	public DBXResult createCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap);
	
	public DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap);

	public DBXResult searchCustomer(Map<String, String> configurations, MemberSearchBean memberSearchBean, Map<String, Object> headerMap, PasswordHistoryManagement pm);

	public DBXResult customerLegalEntitiesGet(CustomerDTO customerDTO, Map<String, Object> headerMap) throws ApplicationException;
	
	public DBXResult getBasicInformation(Map<String, String> configurations, CustomerDTO customerDTO, Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm);

	public DBXResult getCustomerDetailsToAdmin(CustomerDTO customerDTO, Map<String, Object> headerMap);

	public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap);
	
	public DBXResult checkifUserEnrolled(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult sendCustomerUnlockEmail(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult getAddressTypes(Map<String, Object> map);
    
    public DBXResult userIdSearch(String userName, Map<String, Object> headerMap) throws ApplicationException;

}
