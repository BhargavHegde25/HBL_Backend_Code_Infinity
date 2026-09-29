package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;

public interface MembershipBusinessDelegate extends BusinessDelegate {

	public MembershipDTO getMembershipDetails(String membershipId, Map<String, Object> headerMap)
			throws ApplicationException;

	public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, Map<String, Object> headerMap)
			throws ApplicationException;

	public List<MembershipOwnerDTO> getMembershipOwnerDetails(List<MembershipOwnerDTO> inputDTOList,
			Map<String, Object> headerMap) throws ApplicationException;

}
