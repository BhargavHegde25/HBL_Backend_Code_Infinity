package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;

public interface ProfileManagementBusinessDelegate extends BusinessDelegate {

	public DBXResult getCustomerForUserResponse(CustomerDTO customerDTO, Map<String, Object> headerMap);

	public DBXResult getUserResponse(CustomerDTO customerDTO, Map<String, Object> map);

	public DBXResult createCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap);
	
	public DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap);

	public DBXResult searchCustomer(Map<String, String> configurations, MemberSearchBean memberSearchBean, Map<String, Object> headerMap);

	public DBXResult getBasicInformation(Map<String, String> configurations, CustomerDTO customerDTO, Map<String, Object> headerMap, boolean isCustomerSearch);

	public DBXResult getCustomerDetailsToAdmin(CustomerDTO customerDTO, Map<String, Object> headerMap);

	public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap);
	
	public DBXResult checkifUserEnrolled(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult sendCustomerUnlockEmail(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult getAddressTypes(Map<String, Object> map);

}
