package com.temenos.infinity.api.chequemanagement.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface RevokeStopPaymentBackendDelegate extends BackendDelegate {
    /**
     * method to create stop payment  return result Object
     * 
     * @param request
     */
    StopPayment revokeStopPaymentOrder(StopPayment stopPayment, DataControllerRequest request)
            throws Exception; 
    
    /**
     * method to validate stop payment  return result Object
     * 
     * @param request
     */
    StopPayment validateRevokeStopPaymentOrder(StopPayment stopPayment, DataControllerRequest request)
            throws ApplicationException; 

}
