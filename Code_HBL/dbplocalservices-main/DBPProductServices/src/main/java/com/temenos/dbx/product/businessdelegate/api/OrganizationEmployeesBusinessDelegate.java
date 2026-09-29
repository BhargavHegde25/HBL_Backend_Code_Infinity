package com.temenos.dbx.product.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.OrganisationEmployeesViewDTO;
import com.temenos.dbx.product.dto.OrganizationEmployeesDTO;

public interface OrganizationEmployeesBusinessDelegate extends BusinessDelegate {
    JsonObject getOrgEmployeeList(String orgId, String filterColumnName, String filterValue, String urlToFetchList,
            Map<String, ? extends Object> headerMap);

    OrganizationEmployeesDTO createOrganizationEmployee(OrganizationEmployeesDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;
    
    public List<OrganisationEmployeesViewDTO> getOrganizationEmployees(OrganisationEmployeesViewDTO inputDTO,
			Map<String, Object> headersMap) throws ApplicationException;
}
