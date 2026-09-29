package com.temenos.dbx.product.usermanagement.backenddelegate.api;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;

/**
 * 
 * @author DBX Team
 *
 */
public interface ProfileManagementBackendDelegate extends BackendDelegate {

    public DBXResult getCustomerForUserResponse(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult getUserResponse(CustomerDTO customerDTO, Map<String, Object> map);

    public DBXResult createCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult verifyCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap, String deploymentPlatform) throws ApplicationException, JSONException, DBPApplicationException, MiddlewareException, UnsupportedEncodingException;

    public DBXResult searchCustomer(Map<String, String> configurations, MemberSearchBean memberSearchBean,
            Map<String, Object> headerMap);

    public DBXResult checkifUserEnrolled(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult getBasicInformation(Map<String, String> configurations, CustomerDTO customerDTO,
            Map<String, Object> headerMap, boolean isCustomerSearch);

    public DBXResult getCustomerDetailsToAdmin(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult sendCustomerUnlockEmail(CustomerDTO customerDTO, Map<String, Object> headerMap);

    /**
     * Create customerId for enrollment
     * 
     * @param customerDTO
     * @param headerMap
     * @return
     * @throws ApplicationException
     */
    public DBXResult fetchCustomerIdForEnrollment(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    public DBXResult getAddressTypes(Map<String, Object> map);

    public DBXResult fetchRetailCustomerDetails(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException;
}
