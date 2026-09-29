package com.temenos.dbx.product.organization.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.MembershipDTO;

public interface MembershipBackendDelegate extends BackendDelegate {

    public MembershipDTO getMembershipDetails(String membershipId, Map<String, Object> headerMap)
            throws ApplicationException;

    public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, Map<String, Object> headerMap)
            throws ApplicationException;

}
