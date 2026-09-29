package com.hbl.resource.api;

import java.util.Map;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.product.dto.DBXResult;

public interface InfinityUserManagementBusinessDelegateExtn extends InfinityUserManagementBusinessDelegate {
	public DBXResult validateCustomerEnrollmentDetails(String lastName, String taxId, String dateOfBirth, Map<String, Object> headersMap, String companyLegalUnit)
            throws ApplicationException;
	public DBXResult validateHBLCustomerEnrollmentDetails(Map<String, Object> payload, Map<String, Object> headersMap) throws ApplicationException;

}
