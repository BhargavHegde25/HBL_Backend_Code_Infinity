package com.temenos.infinity.api.chequemanagement.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface RevokeStopPaymentBusinessDelegate extends BusinessDelegate {
    /**
     * method to get the create stop payment return result Object
     * 
     * @param request
     */
    StopPayment revokeStopPayment(StopPayment stopPaymentInstance, DataControllerRequest request)
            throws ApplicationException; 
    
    /**
     * method to get the validate stop payment return result Object
     * 
     * @param request
     */
    StopPayment validateRevokeStopPayment(StopPayment stopPaymentInstance, DataControllerRequest request)
            throws ApplicationException; 

}
