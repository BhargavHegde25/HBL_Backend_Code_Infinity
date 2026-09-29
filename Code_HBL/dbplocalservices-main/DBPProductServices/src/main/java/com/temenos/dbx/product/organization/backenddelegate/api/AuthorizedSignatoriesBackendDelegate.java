package com.temenos.dbx.product.organization.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;

public interface AuthorizedSignatoriesBackendDelegate extends BackendDelegate {
    public List<MembershipOwnerDTO> getAuthorizedSignatories(String cif, Map<String, Object> headerMap)
            throws ApplicationException;

}
