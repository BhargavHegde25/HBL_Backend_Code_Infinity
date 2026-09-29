package com.temenos.dbx.product.organization.businessdelegate.impl;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;
import com.temenos.dbx.product.organization.backenddelegate.api.MembershipBackendDelegate;
import com.temenos.dbx.product.organization.backenddelegate.api.MembershipOwnerBackendDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.MembershipBusinessDelegate;

public class MembershipBusinessDelegateImpl implements MembershipBusinessDelegate {

    LoggerUtil logger = new LoggerUtil(MembershipBusinessDelegateImpl.class);

    @Override
    public MembershipDTO getMembershipDetails(String membershipId, Map<String, Object> headerMap)
            throws ApplicationException {
        MembershipBackendDelegate membershipBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(MembershipBackendDelegate.class);
        return membershipBD.getMembershipDetails(membershipId, headerMap);
    }

    @Override
    public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, Map<String, Object> headerMap)
            throws ApplicationException {
        MembershipBackendDelegate membershipBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(MembershipBackendDelegate.class);
        return membershipBD.getMembershipDetailsByTaxid(taxID, companyName, headerMap);
    }

    @Override
    public List<MembershipOwnerDTO> getMembershipOwnerDetails(List<MembershipOwnerDTO> inputDTOList,
            Map<String, Object> headerMap) throws ApplicationException {
        MembershipOwnerBackendDelegate membershipOwnerBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(MembershipOwnerBackendDelegate.class);

        return membershipOwnerBD.getMembershipOwnerDetails(inputDTOList, headerMap);
    }

}
